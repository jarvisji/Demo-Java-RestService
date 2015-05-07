package net.freecoder.restdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.annotation.WlsModule;
import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.SystemModules;
import net.freecoder.restdemo.dao.AlbumDao;
import net.freecoder.restdemo.dao.AlbumPhotoDao;
import net.freecoder.restdemo.model.WlsAlbum;
import net.freecoder.restdemo.model.WlsAlbumEntry;
import net.freecoder.restdemo.model.WlsAlbumPhoto;
import net.freecoder.restdemo.util.UUIDUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author JiTing
 */
@Controller
@RequestMapping("/album")
@WlsModule(SystemModules.M_ALBUM)
public class AlbumController extends CommonController {

	@Autowired
	AlbumDao albumDao;
	@Autowired
	AlbumPhotoDao photoDao;

	// This const is used to describe non-album between client and server.
	private final static String NON_ALBUMID = "nonalbum";
	private static Logger logger = LoggerFactory
			.getLogger(AlbumController.class);

	/**
	 * Everyone has privilege to view the public albums. Only the album owner
	 * has privilege to view private albums.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	@RequestMapping(value = "/{wxAccountId}/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getAlbumList(@PathVariable("wxAccountId") String wxAccountId,
			HttpServletRequest request) {
		String cred = getParamCred(request, true);
		List<WlsAlbum> listByAccount;
		if (isAlbumOwner(wxAccountId, cred)) {
			listByAccount = albumDao.getTopList(wxAccountId);
		} else {
			listByAccount = albumDao.getPublicTopList(wxAccountId);
		}

		List<Map<String, Object>> ret = getAlbumContentInfo(listByAccount,
				isAlbumOwner(wxAccountId, cred));
		Result result = new Result().setEntity(ret);
		return result;
	}

	@RequestMapping(value = "/{wxAccountId}/{parentAlbumId}/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getChildAlbumList(@PathVariable String wxAccountId,
			@PathVariable String parentAlbumId, HttpServletRequest request) {
		String cred = getParamCred(request, true);
		List<WlsAlbum> childAlbums;
		if (isAlbumOwner(wxAccountId, cred)) {
			childAlbums = albumDao.getChildList(parentAlbumId);
		} else {
			childAlbums = albumDao.getPublicChildList(parentAlbumId);
		}
		List<Map<String, Object>> ret = getAlbumContentInfo(childAlbums,
				isAlbumOwner(wxAccountId, cred));
		Result result = new Result().setEntity(ret);
		return result;
	}

	@RequestMapping(value = "/{wxAccountId}", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createAlbum(@PathVariable String wxAccountId,
			@RequestParam("c") String cred, @RequestBody WlsAlbum album) {
		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds == null || !cred2AccountIds.contains(wxAccountId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		album.setId(UUIDUtil.uuid8());
		albumDao.save(album);
		return new Result().setEntity(album);
	}

	@RequestMapping(value = "/{wxAccountId}/{albumId}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updateAlbum(@PathVariable String wxAccountId,
			@PathVariable String albumId, @RequestParam("c") String cred,
			@RequestBody WlsAlbum album) {
		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds == null || !cred2AccountIds.contains(wxAccountId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		if (!albumId.equals(album.getId())) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA);
		}
		albumDao.update(album);
		return new Result();
	}

	@RequestMapping(value = "/{wxAccountId}/{albumId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteAlbum(@PathVariable String wxAccountId,
			@PathVariable String albumId, @RequestParam("c") String cred) {
		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds == null || !cred2AccountIds.contains(wxAccountId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		albumDao.deleteById(albumId);
		return new Result();
	}

	@RequestMapping(value = "/{wxAccountId}/{albumId}/photo/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getPhotoList(@PathVariable String wxAccountId,
			@PathVariable String albumId, HttpServletRequest request) {
		String cred = getParamCred(request, true);
		if (NON_ALBUMID.equalsIgnoreCase(albumId)) {
			// get photos those not in albums.
			albumId = null;
			if (!isAlbumOwner(wxAccountId, cred)) {
				return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
			}
		} else {
			WlsAlbum album = albumDao.getById(albumId);
			if (album == null) {
				return new Result().setErrCode(ErrorCode.NOT_FOUND);
			}
			// if album is private, only owner has privilege to get it.
			if (album.getIsPrivate() && !isAlbumOwner(wxAccountId, cred)) {
				return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
			}
		}

		List<WlsAlbumPhoto> photos = photoDao.getListByAlbum(albumId);
		Result result = new Result().setEntity(photos);
		return result;
	}

	@RequestMapping(value = "/{wxAccountId}/{albumId}/photo", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createPhoto(@PathVariable String wxAccountId,
			@PathVariable String albumId, @RequestParam("c") String cred,
			@RequestBody WlsAlbumPhoto photo) {
		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds == null || !cred2AccountIds.contains(wxAccountId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		checkPhotoAlbum(wxAccountId, albumId, photo);
		photoDao.save(photo);
		return new Result().setEntity(photo);
	}

	@RequestMapping(value = "/{wxAccountId}/{albumId}/photo/{photoId}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updatePhoto(@PathVariable String wxAccountId,
			@PathVariable String albumId, @PathVariable String photoId,
			@RequestParam("c") String cred, @RequestBody WlsAlbumPhoto photo) {
		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds == null || !cred2AccountIds.contains(wxAccountId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		if (!photoId.equals(photo.getId())) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA);
		}
		checkPhotoAlbum(wxAccountId, albumId, photo);
		photoDao.update(photo);
		return new Result();
	}

	@RequestMapping(value = "/{wxAccountId}/{albumId}/photo/{photoId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deletePhoto(@PathVariable String wxAccountId,
			@PathVariable String albumId, @PathVariable String photoId,
			@RequestParam("c") String cred) {
		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds == null || !cred2AccountIds.contains(wxAccountId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		photoDao.deleteById(photoId);
		return new Result();
	}

	@RequestMapping(value = "/entry", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createEntry(@RequestParam("c") String cred,
			@RequestBody WlsAlbumEntry entry) {
		if (!validateAccountIdWithCred(entry.getWlsWxAccountId(), cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, entry.getWlsWxAccountId());
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		albumDao.saveEntry(entry);
		return new Result().setEntity(entry);
	}

	@RequestMapping(value = "/entry/{id}", method = RequestMethod.DELETE, headers = "Content-type=application/json")
	public @ResponseBody
	Result deleteEntry(@PathVariable("id") String wxAccountId,
			@RequestParam("c") String cred) {
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, wxAccountId);
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		albumDao.deleteEntry(wxAccountId);
		return new Result();
	}

	@RequestMapping(value = "/entry/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getEntry(@PathVariable("id") String wxAccountId) {
		WlsAlbumEntry entry = albumDao.getEntry(wxAccountId);
		Result result = new Result();
		if (entry != null) {
			result.setEntity(entry);
		}
		return result;
	}

	@RequestMapping(value = "/entry/{id}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updateEntry(@PathVariable("id") String wxAccountId,
			@RequestParam("c") String cred, @RequestBody WlsAlbumEntry entry) {
		if (!validateAccountIdWithCred(entry.getWlsWxAccountId(), cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, entry.getWlsWxAccountId());
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		if (!wxAccountId.equals(entry.getWlsWxAccountId())) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA);
		}
		albumDao.updateEntry(entry);
		return new Result();
	}

	// Get direct child album count and direct photo count of each top album.
	// TODO: current implementation is not good, too many DB queries. Consider
	// to use one SQL query to instead.
	private List<Map<String, Object>> getAlbumContentInfo(
			List<WlsAlbum> albumList, boolean onlyPublic) {
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>(
				albumList.size());
		for (int i = 0; i < albumList.size(); i++) {
			WlsAlbum wlsAlbum = albumList.get(i);
			Map<String, Object> contentInfo;
			if (onlyPublic) {
				contentInfo = albumDao.getPublicContentInfo(wlsAlbum.getId());
			} else {
				contentInfo = albumDao.getContentInfo(wlsAlbum.getId());
			}
			contentInfo.put(wlsAlbum.getClass().getSimpleName(), wlsAlbum);
			ret.add(contentInfo);
		}
		return ret;
	}

	private boolean isAlbumOwner(String albumWxAccountId, String cred) {
		boolean ret = false;
		if (cred != null && cred2AccountIds(cred).contains(albumWxAccountId)) {
			ret = true;
		}
		return ret;
	}

	private void checkPhotoAlbum(String wxAccountId, String albumId,
			WlsAlbumPhoto photo) {
		if (photo.getId() == null) {
			photo.setId(UUIDUtil.uuid8());
		}
		photo.setWlsWxAccountId(wxAccountId);
		if (NON_ALBUMID.equalsIgnoreCase(albumId)) {
			photo.setWlsAlbum(null);
		} else if (photo.getWlsAlbum() == null) {
			photo.setWlsAlbum(new WlsAlbum(albumId, wxAccountId));
		} else {
			photo.getWlsAlbum().setId(albumId);
		}
	}
}
