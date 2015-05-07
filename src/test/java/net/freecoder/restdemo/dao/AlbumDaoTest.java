/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.dao.AlbumDao;
import net.freecoder.restdemo.dao.AlbumPhotoDao;
import net.freecoder.restdemo.model.WlsAlbum;
import net.freecoder.restdemo.model.WlsAlbumPhoto;
import net.freecoder.restdemo.util.UUIDUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author JiTing
 */
@RunWith(OrderedSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context-application.xml" })
public class AlbumDaoTest {

	static final String wxAccountId = UUIDUtil.uuid8();
	static final String albumId = UUIDUtil.uuid8();
	static final String photoId = UUIDUtil.uuid8();

	@Autowired
	AlbumDao albumDao;
	@Autowired
	AlbumPhotoDao photoDao;

	@Test
	@Order(10)
	public void testCreateAlbum() {
		WlsAlbum wlsAlbum = new WlsAlbum(albumId, wxAccountId);
		albumDao.save(wlsAlbum);
		WlsAlbum dbAlbum = albumDao.getById(albumId);
		Assert.assertNotNull(dbAlbum);
		Assert.assertEquals(dbAlbum.getAppCreateTime(),
				dbAlbum.getAppLastModifyTime());

		List<WlsAlbum> dbAlbums = albumDao.getTopList(wxAccountId);
		Assert.assertEquals(1, dbAlbums.size());

		Map<String, Object> albumContentInfo = albumDao
				.getPublicContentInfo(albumId);
		Assert.assertTrue(albumContentInfo.containsKey("photoCount"));
		Assert.assertTrue(albumContentInfo.containsKey("childAlbumCount"));
	}

	@Test
	@Order(20)
	public void testCreatePhoto() {
		// create photo not in album.
		WlsAlbumPhoto wlsAlbumPhoto = new WlsAlbumPhoto(photoId, "filename",
				wxAccountId);
		photoDao.save(wlsAlbumPhoto);
		// update photo to album.
		WlsAlbum wlsAlbum = new WlsAlbum(albumId, wxAccountId);
		wlsAlbumPhoto.setWlsAlbum(wlsAlbum);
		photoDao.update(wlsAlbumPhoto);

		WlsAlbumPhoto dbPhoto = photoDao.getById(photoId);
		Assert.assertNotNull(dbPhoto);
		Assert.assertEquals(albumId, dbPhoto.getWlsAlbum().getId());
	}

	@Test
	@Order(30)
	public void testUpdate() {
		WlsAlbumPhoto dbPhoto = photoDao.getById(photoId);
		Assert.assertFalse(dbPhoto.getIsPin());
		String albumId = dbPhoto.getWlsAlbum().getId();
		WlsAlbum dbAlbum = albumDao.getById(albumId);
		Assert.assertFalse(dbAlbum.getIsPrivate());

		// update value.
		dbPhoto.setIsPin(true);
		dbAlbum.setIsPrivate(true);
		photoDao.update(dbPhoto);
		albumDao.update(dbAlbum);

		WlsAlbum newAlbum = albumDao.getById(albumId);
		WlsAlbumPhoto newPhoto = photoDao.getById(photoId);
		Assert.assertTrue(newAlbum.getIsPrivate());
		Assert.assertTrue(newPhoto.getIsPin());
	}

	@Test
	@Order(40)
	public void testChildAlbum() {
		List<WlsAlbum> childAlbums = albumDao.getChildList(albumId);
		Assert.assertEquals(0, childAlbums.size());

		WlsAlbum childAlbum = new WlsAlbum(UUIDUtil.uuid8(), wxAccountId);
		childAlbum.setParentId(albumId);
		albumDao.save(childAlbum);

		childAlbums = albumDao.getChildList(albumId);
		Assert.assertEquals(1, childAlbums.size());
	}

	@Test
	@Order(1000)
	public void testDelete() {
		// delete album should not delete photos in it, the photos will be
		// updated without album.
		albumDao.deleteById(albumId);
		WlsAlbum dbAlbum = albumDao.getById(albumId);
		Assert.assertNull(dbAlbum);

		WlsAlbumPhoto dbPhoto = photoDao.getById(photoId);
		Assert.assertNotNull(dbPhoto);
		Assert.assertNull(dbPhoto.getWlsAlbum());

		// delete photo
		photoDao.deleteById(photoId);
		dbPhoto = photoDao.getById(photoId);
		Assert.assertNull(dbPhoto);
	}
}
