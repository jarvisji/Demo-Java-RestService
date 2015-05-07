/**
 * 
 */
package net.freecoder.restdemo.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import net.freecoder.emailengine.exception.SmsProviderException;
import net.freecoder.restdemo.model.WlsSmsHistory;
import net.freecoder.restdemo.service.SmsProvider;
import net.freecoder.restdemo.util.SecurityUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation base on YunSms provider.
 * 
 * @author JiTing
 */
public class YunSmsProvider implements SmsProvider {
	Logger logger = LoggerFactory.getLogger(YunSmsProvider.class);

	private String proxyUrl = "http://http.yunsms.cn";
	private String uid;
	private String pass;
	private boolean async = false;
	private final String CMD_RX = "{proxyUrl}/rx/";
	private final String CMD_TX = "{proxyUrl}/tx/?uid={uid}&pwd={pass}&mobile={phoneNumbers}&content={content}";

	/**
	 * @return the proxUrl
	 */
	public String getProxyUrl() {
		return proxyUrl;
	}

	/**
	 * @param proxUrl
	 *            the proxUrl to set
	 */
	public void setProxyUrl(String proxyUrl) {
		if (proxyUrl != null && !proxyUrl.isEmpty()) {
			this.proxyUrl = proxyUrl;
		}
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * YunSms asks encode password with MD5.
	 * 
	 * @param pass
	 *            the pass to set
	 */
	public void setPass(String pass) {
		MessageDigest instance;
		try {
			instance = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		try {
			instance.update(pass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		byte[] digest = instance.digest();

		this.pass = SecurityUtil.bytes2HexString(digest);
	}

	/**
	 * @return the async
	 */
	public boolean isAsync() {
		return async;
	}

	/**
	 * @param async
	 *            the async to set
	 */
	public void setAsync(boolean async) {
		this.async = async;
	}

	@Override
	public WlsSmsHistory send(String phoneNumber, String content) {
		String respText = new RestTemplate().getForObject(CMD_TX, String.class,
				proxyUrl, uid, pass, phoneNumber, encodeContent(content));
		return createSmsHistory(phoneNumber, content, respText, CMD_TX);
	}

	@Override
	public WlsSmsHistory send(String[] phoneNumbers, String content) {
		String numberStr = StringUtils.join(phoneNumbers, ",");
		String respText = new RestTemplate().getForObject(CMD_TX, String.class,
				proxyUrl, uid, pass, numberStr, encodeContent(content));
		return createSmsHistory(numberStr, content, respText, CMD_TX);
	}

	@Override
	public WlsSmsHistory send(Map<String, String> mapNumberContent) {
		throw new SmsProviderException("Not supported by YunSms.");
	}

	@Override
	public int countPieces(int length) {
		int charsInSingle = 70;
		int iCharsInMulti = 62;
		float fCharsInMulti = 62f;

		int ret = 0;
		if (length <= charsInSingle) {
			ret = 1;
		} else if (length / iCharsInMulti == length / fCharsInMulti) {
			ret = length / iCharsInMulti;
		} else {
			ret = length / iCharsInMulti + 1;
		}
		return ret;
	}

	private WlsSmsHistory createSmsHistory(String phoneNumber, String content,
			String respText, String cmd) {
		WlsSmsHistory wlsSmsHistory = new WlsSmsHistory();
		wlsSmsHistory.setId(UUIDUtil.uuid8());
		wlsSmsHistory.setPhoneNumber(phoneNumber);
		wlsSmsHistory.setContent(content);
		wlsSmsHistory.setProxyResponse(respText);
		wlsSmsHistory.setProxyServer(proxyUrl);
		wlsSmsHistory.setProxyCall(cmd);
		return wlsSmsHistory;
	}

	/**
	 * YunSms requires encode content, default is "GBK".
	 * 
	 * @param content
	 * @return
	 * @throws SmsProviderException
	 */
	private String encodeContent(String content) throws SmsProviderException {
		String ret = "";
		try {
			ret = URLEncoder.encode(content, "GBK");
		} catch (UnsupportedEncodingException e) {
			throw new SmsProviderException(e);
		}
		return ret;
	}

	@Override
	public void checkResponse(String respText) throws SmsProviderException {
		logger.debug("YunSms returns: {}", respText);
		int respInt = Integer.valueOf(respText);
		String errMsg = null;
		switch (respInt) {
		case 101:
			errMsg = "验证失败";
			break;
		case 102:
			errMsg = "短信不足";
			break;
		case 103:
			errMsg = "操作失败";
			break;
		case 104:
			errMsg = "非法字符";
			break;
		case 105:
			errMsg = "内容过多";
			break;
		case 106:
			errMsg = "号码过多";
			break;
		case 107:
			errMsg = "频率过快";
			break;
		case 108:
			errMsg = "号码内容空";
			break;
		case 109:
			errMsg = "账号冻结";
			break;
		case 110:
			errMsg = "禁止频繁单条发送";
			break;
		case 111:
			errMsg = "系统暂定发送";
			break;
		case 112:
			errMsg = "号码错误";
			break;
		case 113:
			errMsg = "定时时间格式不对";
			break;
		case 114:
			errMsg = "账号被锁，10分钟后登录";
			break;
		case 115:
			errMsg = "连接失败";
			break;
		case 116:
			errMsg = "禁止接口发送";
			break;
		case 117:
			errMsg = "绑定IP不正确";
			break;
		case 120:
			errMsg = "系统升级";
			break;
		}
		if (errMsg != null) {
			logger.error("YunSms error: {}, {}", respText, errMsg);
			throw new SmsProviderException(errMsg);
		}
	}

}
