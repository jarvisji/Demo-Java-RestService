package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.model.WlsAlbumPhoto;

/**
 * @author JiTing
 */
public interface AlbumPhotoDao extends CommonDao<WlsAlbumPhoto> {
	WlsAlbumPhoto getById(String id);

	List<WlsAlbumPhoto> getListByAlbum(String albumId);

	void deleteById(String id);

}
