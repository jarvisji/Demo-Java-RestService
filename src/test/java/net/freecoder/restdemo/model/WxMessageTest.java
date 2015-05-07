/**
 * 
 */
package net.freecoder.restdemo.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.freecoder.restdemo.constant.WxMessageType;
import net.freecoder.restdemo.model.WxArticle;
import net.freecoder.restdemo.model.WxMessage;

import org.junit.Test;

/**
 * @author JiTing
 */

public class WxMessageTest {
	@Test
	public void empty() {
		// this test has no real cases currently.
	}

	public void testNew() throws JAXBException {
		WxMessage msg = new WxMessage();
		msg.setToUserName("toUser");
		msg.setMsgType(WxMessageType.NEWS.value());

		List<WxArticle> articles = new ArrayList<WxArticle>();
		WxArticle article = new WxArticle();
		article.setTitle("article1");
		articles.add(article);
		msg.setArticles(articles);
		msg.setArticleCount(articles.size());
		JAXBContext jc = JAXBContext.newInstance(WxMessage.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.marshal(msg, System.out);
	}

	public void testNews() throws Exception {
		WxMessage msg = new WxMessage();
		msg.setToUserName("toUser");
		msg.setMsgType(WxMessageType.NEWS.value());

		int testCount = 3;

		List<WxArticle> articles = new ArrayList<WxArticle>(testCount);
		for (int i = 0; i < testCount; i++) {
			WxArticle article = new WxArticle();
			article.setTitle("article" + i);
			articles.add(article);
		}
		msg.setArticleCount(articles.size());
		msg.setArticles(articles);

		JAXBContext jc = JAXBContext.newInstance(WxMessage.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.marshal(msg, System.out);

		// Assert.assertTrue(condition);
	}

}
