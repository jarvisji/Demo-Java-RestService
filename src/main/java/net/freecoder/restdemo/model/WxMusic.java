/**
 * 
 */
package net.freecoder.restdemo.model;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 * @author JiTing
 */
public class WxMusic {
	private String title;
	private String description;
	private String musicUrl;
	private String hqMusicUrl;
	private String thumbMediaId;

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
	public void setTitle(String title) {
		this.title = title;
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
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the musicUrl
	 */
	@XmlElement(name = "MusicUrl")
	@XmlCDATA
	public String getMusicUrl() {
		return musicUrl;
	}

	/**
	 * @param musicUrl
	 *            the musicUrl to set
	 */
	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	/**
	 * @return the hqMusicUrl
	 */
	@XmlElement(name = "HQMusicUrl")
	@XmlCDATA
	public String getHqMusicUrl() {
		return hqMusicUrl;
	}

	/**
	 * @param hqMusicUrl
	 *            the hqMusicUrl to set
	 */
	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}

	/**
	 * @return the thumbMediaId
	 */
	@XmlElement(name = "ThumbMediaId")
	@XmlCDATA
	public String getThumbMediaId() {
		return thumbMediaId;
	}

	/**
	 * @param thumbMediaId
	 *            the thumbMediaId to set
	 */
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
}
