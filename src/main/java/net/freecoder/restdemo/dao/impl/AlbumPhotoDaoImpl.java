/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.dao.AlbumPhotoDao;
import net.freecoder.restdemo.model.WlsAlbumPhoto;

import org.springframework.stereotype.Repository;

/**
 * @author JiTing
 */
@Repository
public class AlbumPhotoDaoImpl extends CommonDaoImpl<WlsAlbumPhoto> implements
		AlbumPhotoDao {

	@Override
	public WlsAlbumPhoto getById(String id) {
		return super.get(WlsAlbumPhoto.class, id);
	}

	@Override
	public List<WlsAlbumPhoto> getListByAlbum(String albumId) {
		List<WlsAlbumPhoto> ret;
		if (albumId == null) {
			String hql = "from WlsAlbumPhoto where wlsAlbum.id is null order by isPin desc, appLastModifyTime desc";
			ret = super.find(hql);
		} else {
			String hql = "from WlsAlbumPhoto where wlsAlbum.id=:albumId order by isPin desc, appLastModifyTime desc";
			Map<String, String> params = new HashMap<String, String>(1);
			params.put("albumId", albumId);
			ret = super.find(hql, params);
		}
		return ret;
	}

	@Override
	public void deleteById(String id) {
		String hql = "delete from WlsAlbumPhoto where id=:id";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("id", id);
		super.execute(hql, params);
	}

}
