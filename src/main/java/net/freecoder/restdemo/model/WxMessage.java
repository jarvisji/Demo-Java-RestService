/**
 * 
 */
package net.freecoder.restdemo.model;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 * @author JiTing
 */
@XmlRootElement(name = "xml")
public class WxMessage {
	private String toUserName;
	private String fromUserName;
	private Long createTime;
	private String msgType;
	private String event;
	private String eventKey;
	private String ticket;
	private String latitude;
	private String longitude;
	private String precision;
	private String content;
	private String msgId;
	private String picUrl;
	private String mediaId;
	private String format;
	private String thumbMediaId;
	private String location_X;
	private String location_Y;
	private String scale;
	private String label;
	private String title;
	private String description;
	private String url;
	private WxMusic music;
	private Integer articleCount;
	private List<WxArticle> articles;

	public String toString() {
		StringBuffer sb = new StringBuffer(" WxMessage: fromUserName=")
				.append(fromUserName).append(", msgType=").append(msgType)
				.append(", event=").append(event).append(", eventKey=")
				.append(eventKey);
		return sb.toString();
	}

	public WxMessage() {

	}

	/**
	 * @return the toUserName
	 */
	@XmlElement(name = "ToUserName")
	@XmlCDATA
	public String getToUserName() {
		return toUserName;
	}

	/**
	 * @param toUserName
	 *            the toUserName to set
	 */
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	/**
	 * @return the fromUserName
	 */
	@XmlElement(name = "FromUserName")
	@XmlCDATA
	public String getFromUserName() {
		return fromUserName;
	}

	/**
	 * @param fromUserName
	 *            the fromUserName to set
	 */
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	/**
	 * @return the createTime
	 */
	@XmlElement(name = "CreateTime")
	public Long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the msgType
	 */
	@XmlElement(name = "MsgType")
	@XmlCDATA
	public String getMsgType() {
		return msgType;
	}

	/**
	 * @param msgType
	 *            the msgType to set
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	/**
	 * @return the event
	 */
	@XmlElement(name = "Event")
	@XmlCDATA
	public String getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the eventKey
	 */
	@XmlElement(name = "EventKey")
	public String getEventKey() {
		return eventKey;
	}

	/**
	 * @param eventKey
	 *            the eventKey to set
	 */
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	/**
	 * @return the ticket
	 */
	@XmlElement(name = "Ticket")
	public String getTicket() {
		return ticket;
	}

	/**
	 * @param ticket
	 *            the ticket to set
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	/**
	 * @return the latitude
	 */
	@XmlElement(name = "Latitude")
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	@XmlElement(name = "Longitude")
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the precision
	 */
	@XmlElement(name = "Precision")
	public String getPrecision() {
		return precision;
	}

	/**
	 * @param precision
	 *            the precision to set
	 */
	public void setPrecision(String precision) {
		this.precision = precision;
	}

	/**
	 * @return the content
	 */
	@XmlElement(name = "Content")
	@XmlCDATA
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the msgId
	 */
	@XmlElement(name = "MsgId")
	public String getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId
	 *            the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the picUrl
	 */
	@XmlElement(name = "PicUrl")
	public String getPicUrl() {
		return picUrl;
	}

	/**
	 * @param picUrl
	 *            the picUrl to set
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	/**
	 * @return the mediaId
	 */
	@XmlElement(name = "MediaId")
	public String getMediaId() {
		return mediaId;
	}

	/**
	 * @param mediaId
	 *            the mediaId to set
	 */
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	/**
	 * @return the format
	 */
	@XmlElement(name = "Format")
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the thumbMediaId
	 */
	@XmlElement(name = "ThumbMediaId")
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

	/**
	 * @return the location_X
	 */
	@XmlElement(name = "Location_X")
	public String getLocation_X() {
		return location_X;
	}

	/**
	 * @param location_X
	 *            the location_X to set
	 */
	public void setLocation_X(String location_X) {
		this.location_X = location_X;
	}

	/**
	 * @return the location_Y
	 */
	@XmlElement(name = "Location_Y")
	public String getLocation_Y() {
		return location_Y;
	}

	/**
	 * @param location_Y
	 *            the location_Y to set
	 */
	public void setLocation_Y(String location_Y) {
		this.location_Y = location_Y;
	}

	/**
	 * @return the scale
	 */
	@XmlElement(name = "Scale")
	public String getScale() {
		return scale;
	}

	/**
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}

	/**
	 * @return the label
	 */
	@XmlElement(name = "Label")
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the title
	 */
	@XmlElement(name = "Title")
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
	 * @return the url
	 */
	@XmlElement(name = "Url")
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the music
	 */
	@XmlElement(name = "Music")
	public WxMusic getMusic() {
		return music;
	}

	/**
	 * @param music
	 *            the music to set
	 */
	public void setMusic(WxMusic music) {
		this.music = music;
	}

	/**
	 * @return the articleCount
	 */
	@XmlElement(name = "ArticleCount")
	public Integer getArticleCount() {
		return articleCount;
	}

	/**
	 * @param articleCount
	 *            the articleCount to set
	 */
	public void setArticleCount(Integer articleCount) {
		this.articleCount = articleCount;
	}

	/**
	 * @return the articles
	 */
	@XmlElementWrapper(name = "Articles")
	@XmlElementRef
	public List<WxArticle> getArticles() {
		return articles;
	}

	/**
	 * @param articles
	 *            the articles to set
	 */
	public void setArticles(List<WxArticle> articles) {
		this.articles = articles;
	}

	public String toXml() {
		String xml = "";
		try {
			Marshaller marshaller = JAXBContext.newInstance(WxMessage.class)
					.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(this, writer);
			xml = writer.toString();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xml;
	}

}
