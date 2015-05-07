package net.freecoder.restdemo.dao;

import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.model.WlsAlbum;
import net.freecoder.restdemo.model.WlsAlbumEntry;

/**
 * @author JiTing
 */
public interface AlbumDao extends CommonDao<WlsAlbum> {

	WlsAlbum getById(String id);

	/**
	 * Get all top albums of special account, include public and privite.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	List<WlsAlbum> getTopList(String wxAccountId);

	/**
	 * Get public top albums of special account.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	List<WlsAlbum> getPublicTopList(String wxAccountId);

	List<WlsAlbum> getChildList(String parentAlbumId);

	List<WlsAlbum> getPublicChildList(String parentAlbumId);

	/**
	 * Get album information include direct child album count, direct photo
	 * count, etc.
	 * 
	 * @param albumId
	 * @return
	 */
	Map<String, Object> getContentInfo(String albumId);

	Map<String, Object> getPublicContentInfo(String albumId);

	void deleteById(String id);

	WlsAlbumEntry getEntry(String wxAccountId);

	void saveEntry(WlsAlbumEntry entry);

	void updateEntry(WlsAlbumEntry entry);

	void deleteEntry(String wxAccountId);
}
