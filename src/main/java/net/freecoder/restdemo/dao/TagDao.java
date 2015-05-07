/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.model.MaterialType;
import net.freecoder.restdemo.model.WlsTag;

/**
 * @author JiTing
 */
public interface TagDao extends CommonDao<WlsTag> {
	/**
	 * Get tag list of the special weixin account.
	 * 
	 * @param wxAccountId
	 * 
	 * @return List
	 */
	List<WlsTag> getTagsByAccount(String wxAccountId);

	/**
	 * Get tag by accountId and tag name. Usually this interface are used to
	 * check tag duplication.
	 * 
	 * @param wxAccountId
	 * @param tag
	 * @return
	 */
	WlsTag get(String wxAccountId, String tag);

	/**
	 * Save a collection of WlsTag in batch.
	 * 
	 * @param wxAccountId
	 * @param entities
	 * @return List<WlsTag>
	 */
	List<WlsTag> saveTagsForAccount(String wxAccountId, WlsTag[] entities);

	/**
	 * Delete tags of weixin account.
	 * 
	 * @param wxAccountId
	 * @param entities
	 */
	void deleteTagsForAccount(String wxAccountId, WlsTag[] entities);

	/**
	 * Add tag to a material.
	 * 
	 * @param tag
	 * @param materialId
	 * @param materialType
	 */
	void addTagOnMaterial(WlsTag tag, String materialId,
			MaterialType materialType);

	/**
	 * Remove tag from a material.
	 * 
	 * @param tagId
	 * @param materialId
	 */
	void removeTagFromMaterial(String tagId, String materialId);

	/**
	 * Get tags of special material.
	 * 
	 * @param materialId
	 * @param materialType
	 * @return
	 */
	List<WlsTag> getTagsByMaterial(String materialId, MaterialType materialType);

	/**
	 * Get all tags are used in a kind of materials.
	 * 
	 * @param wxAccountId
	 * @param materialType
	 * 
	 * @return
	 */
	List<WlsTag> getTagsByMaterialType(String wxAccountId,
			MaterialType materialType);

	/**
	 * Get array of material id by special weixin account and tag.
	 * 
	 * @param wxAccountId
	 * @param tagId
	 * @param materialType
	 * @return String[]
	 */
	String[] getMaterialIdByTag(String wxAccountId, String tagId,
			MaterialType materialType);

}
