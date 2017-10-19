package de.maltorpro.shop.util;

import static net.logstash.logback.marker.Markers.append;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import de.maltorpro.shop.model.enums.Event;
import de.maltorpro.shop.model.enums.LogtashMarker;

@Aspect
@Component
public class LoggingAspect {

	static Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

	@Autowired
	private Tracer tracer;

	@Around("execution(* de.maltorpro.shop.service..*(..))")
	public Object profileExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

		long start = System.currentTimeMillis();
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		String apiName = className + "." + methodName;

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Span currentSpan = tracer.getCurrentSpan();

		if (requestAttributes != null && currentSpan != null) {

			Object result = joinPoint.proceed();
			String resultStr = "NO RESUALT";
			if (result != null) {
				resultStr = result.toString();
			}

			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();

			String traceId = Long.toHexString(currentSpan.getTraceId());
			String spanId = Long.toHexString(currentSpan.getSpanId());

			long elapsedTime = System.currentTimeMillis() - start;
			String args = Arrays.toString(joinPoint.getArgs());

			LOGGER.info(
					append(LogtashMarker.EVENT.name(), Event.SERVICE_CALL.name())
							.and(append(LogtashMarker.EXECUTION_TIME.name(), elapsedTime))
							.and(append(LogtashMarker.API_NAME.name(), apiName))
							.and(append(LogtashMarker.TRACE_ID.name(), traceId))
							.and(append(LogtashMarker.SPAN_ID.name(), spanId))
							.and(append(LogtashMarker.RESPONSE_OBJECT.name(), resultStr)),
					"----->>>>>\n" + "HOST: {} HttpMethod: {}\n" + "URI: {}\nAPI: {}\n" + "Arguments: {}\n"
							+ "Response object: {}\n" + "Execution time: {}\n" + "----->>>>>",
					request.getHeader("host"), request.getMethod(), request.getRequestURI(), apiName, args, resultStr,
					elapsedTime);

			return result;
		} else {
			return joinPoint.proceed();
		}

	}
}