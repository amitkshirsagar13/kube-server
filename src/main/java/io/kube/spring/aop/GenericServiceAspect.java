/**
 * 
 */
package io.kube.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import io.kube.spring.data.Response;

/**
 * <p>
 * <b>Overview:</b>
 * <p>
 * 
 * 
 * <pre>
 * &#64;projectName kube-server
 * Creation date: Mar 31, 2018
 * &#64;author Amit Kshirsagar
 * &#64;version 1.0
 * &#64;since
 * 
 * <p><b>Modification History:</b><p>
 * 
 * 
 * </pre>
 */
@Aspect
@Configuration
public class GenericServiceAspect {

	@Value("${server.address}")
	private String serverName = null;

	@Value("${server.port}")
	private String serverPort = null;

	@Around("execution(public * *.service.*save(..))")
	public Response aroundSaveResponse(ProceedingJoinPoint pjp) {
		return populateResponseStats(pjp);
	}

	@Around("execution(public * *.service.*find*(..))")
	public Response aroundFindResponse(ProceedingJoinPoint pjp) {
		return populateResponseStats(pjp);
	}

	private Response populateResponseStats(ProceedingJoinPoint pjp) {
		Response response = null;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			response = (Response) pjp.proceed();
		} catch (Throwable e) {

		}
		stopWatch.stop();
		response.setResponseTime(stopWatch.getTotalTimeMillis()).setServer(serverName).setPort(serverPort);
		return response;
	}

}
