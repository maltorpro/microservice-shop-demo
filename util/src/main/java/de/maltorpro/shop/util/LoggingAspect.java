package de.maltorpro.shop.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.Span;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import de.maltorpro.shop.model.enums.Event;
import de.maltorpro.shop.model.enums.LogtashMarker;

import javax.servlet.http.HttpServletRequest;

import static net.logstash.logback.marker.Markers.append;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
public class LoggingAspect {


	static Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);
	
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
			
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			String traceId = Long.toHexString(currentSpan.getTraceId());
			String spanId = Long.toHexString(currentSpan.getSpanId());
			
			LOG.info(append(LogtashMarker.EVENT.name(), Event.SERVICE_CALL.name()),
					"----->>>>>\nHOST: {} HttpMethod: {}\nURI: {}\nAPI: {}\nArguments: {}\n----->>>>>",
					request.getHeader("host"), request.getMethod(), request.getRequestURI(), apiName,
					Arrays.toString(joinPoint.getArgs()));
		

			Object result = joinPoint.proceed();
			
			LOG.info(append(LogtashMarker.RESPONSE_OBJECT.name(),result.toString()), "Response object: {}", result);
			
			long elapsedTime = System.currentTimeMillis() - start;
			LOG.info(append(LogtashMarker.EXECUTION_TIME.name(),elapsedTime), "Execution time: {}", elapsedTime);
			LOG.info(append(LogtashMarker.TRACE_ID.name(),traceId), "Trace id: {}", traceId);
			LOG.info(append(LogtashMarker.SPAN_ID.name(),spanId), "Span id: {}", spanId);
			LOG.info(append(LogtashMarker.API_NAME.name(),apiName), "Api name: {}", apiName);
			
			return result;
		} else {
			return joinPoint.proceed();
		}

	}
}