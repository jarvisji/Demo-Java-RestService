/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.freecoder.restdemo.dao.TagDao;
import net.freecoder.restdemo.model.MaterialType;
import net.freecoder.restdemo.model.WlsTag;
import net.freecoder.restdemo.model.WlsTagMaterial;
import net.freecoder.restdemo.model.WlsTagMaterialId;
import net.freecoder.restdemo.util.UUIDUtil;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class TagDaoImpl extends CommonDaoImpl<WlsTag> implements TagDao {

	public WlsTag get(String tagId) {
		return (WlsTag) currentSession().get(WlsTag.class, tagId);
	}

	@Override
	public List<WlsTag> getTagsByAccount(String wxAccountId) {
		String hql = "from WlsTag where wlsWxAccountId=?";
		List<WlsTag> ret = currentSession().createQuery(hql)
				.setString(0, wxAccountId).list();
		return ret;
	}

	@Override
	public WlsTag get(String wxAccountId, String tag) {
		String hql = "from WlsTag where wlsWxAccountId=? and tag=?";
		List<WlsTag> ret = currentSession().createQuery(hql)
				.setString(0, wxAccountId).setString(1, tag).list();
		if (ret.size() > 0) {
			return ret.get(0);
		}
		return null;
	}

	@Override
	public List<WlsTag> saveTagsForAccount(String wxAccountId, WlsTag[] entities) {
		List<WlsTag> retList = new ArrayList<WlsTag>();
		for (int i = 0; i < entities.length; i++) {
			WlsTag entity = entities[i];
			if (entity.getId() == null) {
				entity.setWlsWxAccountId(wxAccountId);
				entity.setId(UUIDUtil.uuid8());
				save(entity);
			}
			retList.add(entity);
		}
		return retList;
	}

	@Override
	public void deleteTagsForAccount(String wxAccountId, WlsTag[] entities) {
		for (int i = 0; i < entities.length; i++) {
			delete(entities[i]);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addTagOnMaterial(WlsTag tag, String materialId,
			MaterialType materialType) {
		if (tag.getId() == null) {
			WlsTag dbTag = get(tag.getWlsWxAccountId(), tag.getTag());
			if (dbTag == null) {
				tag.setId(UUIDUtil.uuid8());
				save(tag);
			} else {
				tag.setId(dbTag.getId());
			}
		}

		WlsTagMaterial wlsTagMaterial = new WlsTagMaterial();
		wlsTagMaterial.setId(new WlsTagMaterialId(tag.getId(), materialId));
		wlsTagMaterial.setWlsWxAccountId(tag.getWlsWxAccountId());
		wlsTagMaterial.setMaterialType(materialType.value());
		currentSession().save(wlsTagMaterial);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeTagFromMaterial(String tagId, String materialId) {
		WlsTagMaterialId wlsTagMaterialId = new WlsTagMaterialId(tagId,
				materialId);
		currentSession().delete(
				currentSession().get(WlsTagMaterial.class, wlsTagMaterialId));

		String hql = "select count(id.materialId) from WlsTagMaterial where id.tagId=?";
		List<Long> countRet = currentSession().createQuery(hql)
				.setString(0, tagId).list();
		if (countRet.get(0) == 0) {
			delete(get(tagId));
		}
	}

	@Override
	public String[] getMaterialIdByTag(String wxAccountId, String tagId,
			MaterialType materialType) {
		String hql = "select id.materialId from WlsTagMaterial where id.tagId=? and materialType=?";
		List<String> idList = currentSession().createQuery(hql)
				.setString(0, tagId).setString(1, materialType.value()).list();
		String[] ret = new String[] {};
		return idList.toArray(ret);
	}

	@Override
	public List<WlsTag> getTagsByMaterial(String materialId,
			MaterialType materialType) {
		String hql = "from WlsTag where id in (select id.tagId from WlsTagMaterial where id.materialId=? and materialType=?)";
		List<WlsTag> tagList = currentSession().createQuery(hql)
				.setString(0, materialId).setString(1, materialType.value())
				.list();
		return tagList;
	}

	@Override
	public List<WlsTag> getTagsByMaterialType(String wxAccountId,
			MaterialType materialType) {
		String hql = "select id, tag from WlsTag where wlsWxAccountId=? and id in (select id.tagId from WlsTagMaterial where wlsWxAccountId=? and materialType=?)";
		List<WlsTag> tagList = currentSession().createQuery(hql)
				.setString(0, wxAccountId).setString(1, wxAccountId)
				.setString(2, materialType.value()).list();
		return tagList;
	}

}
