/**
 * 
 */
package net.freecoder.restdemo.aop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.annotation.WlsModule;
import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.UserType;
import net.freecoder.restdemo.controller.CommonController;
import net.freecoder.restdemo.exception.ServiceDaoException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author JiTing
 */
public class ControllerPrivilegeValidator {

	final static Logger logger = LoggerFactory
			.getLogger(ControllerPrivilegeValidator.class);

	/**
	 * Get WlsModule annotation from method definition. When add @WlsModule
	 * annotation before method declearation, should use this method.
	 * 
	 * @param joinPoint
	 * @param wlsModule
	 *            Must define in aop configuration to get this parameter.
	 *            <aop:around pointcut="@annotation(wlsModule)"
	 *            method="validate" arg-names="joinPoint, wlsModule" />
	 * @throws Throwable
	 */
	public Object validateMethod(ProceedingJoinPoint joinPoint,
			WlsModule wlsModule) throws Throwable {
		CommonController controller = (CommonController) joinPoint.getThis();
		checkUserModule(wlsModule, controller);

		// no exception throwed in upon validation, proceed the request method.
		return joinPoint.proceed();
	}

	/**
	 * Get WlsModule annotation from class definition.
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	public Object validateClass(ProceedingJoinPoint joinPoint) throws Throwable {
		Object targetClass = joinPoint.getTarget();
		WlsModule wlsModule = targetClass.getClass().getAnnotation(
				WlsModule.class);
		// the requested class should have @WlsModule annotation.
		if (wlsModule != null) {
			// the requested class should inherit from CommonController
			if (targetClass instanceof CommonController) {
				CommonController controller = (CommonController) targetClass;
				checkUserModule(wlsModule, controller);
			}
		}

		// no exception throwed in upon validation, proceed the request method.
		return joinPoint.proceed();
	}

	private void checkUserModule(WlsModule wlsModule,
			CommonController controller) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String cred = request.getParameter("c");
		if (cred != null && controller.cred2VipId(cred, true) == null) {
			// some interfaces needn't verify user credential, module checking
			// shouldn't break them. So only continue
			// check when request contains 'c' paramter. Target interface will
			// handle credential varification too.
			// Also, doesn't check module privilege for VIP users.
			UserType userType = controller.cred2UserType(cred);
			if (!UserType.ADM.equals(userType)
					&& !UserType.CSR.equals(userType)) {
				List<String> userModules = controller.getUserCred(cred)
						.getModules();
				if (userModules == null
						|| !userModules.contains(wlsModule.value().toString())) {
					String cred2UserId = controller.cred2UserId(cred, true);
					logger.warn(
							"User {} doesn't have privilege to access controller {}.",
							cred2UserId, controller);
					throw new ServiceDaoException(
							ErrorCode.NO_PRIVILIEGE.toString());
				}
			}
		}
	}
}
