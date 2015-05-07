/**
 * 
 */
package net.freecoder.restdemo.aop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.controller.Result;
import net.freecoder.restdemo.exception.ServiceException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is AOP class to valid credential before controller methods execute.
 * 
 * @author JiTing
 */

@Aspect
public class CredValidator {

	Logger logger = LoggerFactory.getLogger(CredValidator.class);

	private static Map<String, TimelyCred> userCredMap = new HashMap<String, TimelyCred>();

	class TimelyCred {
		@SuppressWarnings("unused")
		private String userId;
		private String cred;
		private long expireTime;

		public TimelyCred(String userId, String cred) {
			this.userId = userId;
			this.cred = cred;
			// expire in 2 hours
			this.expireTime = System.currentTimeMillis() + 7200000;
		}

		public long getExpreTime() {
			return this.expireTime;
		}

		public boolean checkCred(String cred) {
			return this.cred.equals(cred);
		}

		public boolean checkExpire(long ts) {
			return this.expireTime > ts;
		}
	}

	@Pointcut("execution(* net.freecoder.restdemo.controller.CredentialController.generateCredential(..))")
	public void sellerLogin() {
	}

	/**
	 * After seller login, we need save the credential to memory.
	 */
	@AfterReturning(pointcut = "sellerLogin()", returning = "ret")
	public void afterSellerLogin(JoinPoint joinpoint, Object ret) {
		logger.debug("afterSellerLogin(), saving credential for later verify.");
		Result result = (Result) ret;
		if (result.isSuccess()) {
			String userId = (String) result.getData("userId");
			String cred = (String) result.getData("cred");
			TimelyCred timelyCred = new TimelyCred(userId, cred);
			userCredMap.put(userId, timelyCred);
			logger.debug("Credential of user {} will expire at {}", userId,
					timelyCred.getExpreTime());
		}
	}

	@Pointcut("execution(* net.freecoder.restdemo.controller.UserController.getUser(javax.servlet.http.HttpServletRequest)) && args(request)")
	public void getUser(HttpServletRequest request) {
	}

	@Before("getUser(request)")
	public void before(HttpServletRequest request) {
		logger.debug("Before GET* interfaces, check user and cred.");
		String userId = request.getParameter("u");
		String cred = request.getParameter("c");
		if (userId == null || cred == null) {
			throw new IllegalArgumentException(
					"Missed userId or cred in request.");
		}
		TimelyCred timelyCred = userCredMap.get(userId);
		if (timelyCred != null) {
			if (!timelyCred.checkCred(cred)) {
				throw new ServiceException("Invalid cred for user.");
			}
			if (!timelyCred.checkExpire(System.currentTimeMillis())) {
				throw new ServiceException("Cred expired, please re-login.");
			}
		} else {
			throw new ServiceException("User hasn't login.");
		}
	}
}
