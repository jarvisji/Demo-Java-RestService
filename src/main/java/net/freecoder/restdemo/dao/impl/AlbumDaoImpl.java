/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.dao.AlbumDao;
import net.freecoder.restdemo.model.WlsAlbum;
import net.freecoder.restdemo.model.WlsAlbumEntry;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;

/**
 * @author JiTing
 */
@Repository
public class AlbumDaoImpl extends CommonDaoImpl<WlsAlbum> implements AlbumDao {
	@Override
	public WlsAlbum getById(String id) {
		return super.get(WlsAlbum.class, id);
	}

	@Override
	public List<WlsAlbum> getTopList(String wxAccountId) {
		String hql = "from WlsAlbum where wlsWxAccountId=:wxAccountId and parentId is null order by isPin desc, appLastModifyTime desc";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("wxAccountId", wxAccountId);
		return super.find(hql, params);
	}

	@Override
	public List<WlsAlbum> getPublicTopList(String wxAccountId) {
		String hql = "from WlsAlbum where wlsWxAccountId=:wxAccountId and parentId is null and isPrivate=false order by isPin desc, appLastModifyTime desc";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("wxAccountId", wxAccountId);
		return super.find(hql, params);
	}

	@Override
	public List<WlsAlbum> getChildList(String parentAlbumId) {
		String hql = "from WlsAlbum where parentId=:parentId order by isPin desc, appLastModifyTime desc";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("parentId", parentAlbumId);
		return super.find(hql, params);
	}

	@Override
	public List<WlsAlbum> getPublicChildList(String parentAlbumId) {
		String hql = "from WlsAlbum where parentId=:parentId and isPrivate=false order by isPin desc, appLastModifyTime desc";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("parentId", parentAlbumId);
		return super.find(hql, params);
	}

	@Override
	public Map<String, Object> getContentInfo(String albumId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*), 'childAlbumCount' from wls_album where parent_id=:parentId");
		sb.append(" union all ");
		sb.append("select count(*), 'photoCount' from wls_album_photo where album_id=:parentId");

		List<?> list = currentSession().createSQLQuery(sb.toString())
				.setString("parentId", albumId).list();
		// here SQLQuery returns an list of Object array, each array should has
		// two elements, corresponding to select
		// items.
		Map<String, Object> ret = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			Object[] row = (Object[]) list.get(i);
			ret.put((String) row[1], row[0]);
		}
		return ret;
	}

	@Override
	public Map<String, Object> getPublicContentInfo(String albumId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*), 'childAlbumCount' from wls_album where parent_id=:parentId and is_private=false");
		sb.append(" union all ");
		sb.append("select count(*), 'photoCount' from wls_album_photo where album_id=:parentId");

		List<?> list = currentSession().createSQLQuery(sb.toString())
				.setString("parentId", albumId).list();
		Map<String, Object> ret = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			Object[] row = (Object[]) list.get(i);
			ret.put((String) row[1], row[0]);
		}
		return ret;
	}

	@Override
	public void deleteById(String id) {
		String hql = "update WlsAlbumPhoto set wlsAlbum=null where wlsAlbum.id=:id";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("id", id);
		super.execute(hql, params);

		hql = "delete from WlsAlbum where id=:id";
		super.execute(hql, params);
	}

	@Override
	public WlsAlbumEntry getEntry(String wxAccountId) {
		SimpleExpression eq = Restrictions.eq("wlsWxAccountId", wxAccountId);
		List<?> list = currentSession().createCriteria(WlsAlbumEntry.class)
				.add(eq).list();
		WlsAlbumEntry ret = null;
		if (list.size() > 0) {
			ret = (WlsAlbumEntry) list.get(0);
		}
		return ret;
	}

	@Override
	public void saveEntry(WlsAlbumEntry entry) {
		currentSession().save(entry);
	}

	@Override
	public void updateEntry(WlsAlbumEntry entry) {
		currentSession().update(entry);
	}

	@Override
	public void deleteEntry(String wxAccountId) {
		String hql = "delete from WlsAlbumEntry where wlsWxAccountId=:wxAccountId";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("wxAccountId", wxAccountId);
		super.execute(hql, params);
	}
}
