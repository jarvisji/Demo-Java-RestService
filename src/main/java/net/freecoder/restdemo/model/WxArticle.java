/**
 * 
 */
package net.freecoder.restdemo.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 * @author JiTing
 */
@XmlRootElement(name = "item")
public class WxArticle {
	private String title;
	private String description;
	private String picUrl;
	private String url;

	public WxArticle() {
	}

	/**
	 * @return the title
	 */
	@XmlElement(name = "Title")
	@XmlCDATA
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public WxArticle setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * @return the description
	 */
	@XmlElement(name = "Description")
	@XmlCDATA
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public WxArticle setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * @return the picUrl
	 */
	@XmlElement(name = "PicUrl")
	@XmlCDATA
	public String getPicUrl() {
		return picUrl;
	}

	/**
	 * @param picUrl
	 *            the picUrl to set
	 */
	public WxArticle setPicUrl(String picUrl) {
		this.picUrl = picUrl;
		return this;
	}

	/**
	 * @return the url
	 */
	@XmlElement(name = "Url")
	@XmlCDATA
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public WxArticle setUrl(String url) {
		this.url = url;
		return this;
	}

}
