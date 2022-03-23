package es.seresco.cursojee.videoclub.view.handler;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import es.seresco.cursojee.videoclub.exception.ElementoNoExistenteException;
import es.seresco.cursojee.videoclub.exception.PeticionInconsistenteException;
import es.seresco.cursojee.videoclub.view.dto.ErrorInfo;

@ControllerAdvice
public class VideoClubExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			final Exception ex, Object body,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request)
	{
		if (body == null) {
			// replace with the parametrized body
			if (ex instanceof MissingServletRequestParameterException) {
				body = buildErrorInfoForMissingServletRequestParameter((MissingServletRequestParameterException) ex, request);
			} else if (ex instanceof TypeMismatchException) {
				body = buildErrorInfoForTypeMismatch((TypeMismatchException) ex, request);
			} else if (ex instanceof BindException) {
				body = buildErrorInfoForBindException((BindException) ex, request);
			}
			// TODO: handle other significant Exceptions mapped in ResponseEntityExceptionHandler
			else {
				body = buildErrorInfo(ex, request);
			}
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	protected String resolveRequestURL(
			final WebRequest webRequest,
			HttpServletRequest servletRequest)
	{
		// resolve request object
		if (servletRequest == null && webRequest instanceof NativeWebRequest) {
			servletRequest = ((NativeWebRequest) webRequest).getNativeRequest(HttpServletRequest.class);
		}
		// unwrap if needed
		servletRequest = WebUtils.getNativeRequest(servletRequest, HttpServletRequest.class);
		// extract request URL
		StringBuffer requestUrl = servletRequest.getRequestURL();
		return requestUrl == null ? null : requestUrl.toString();
	}

	protected ErrorInfo<Object> buildErrorInfo(Exception ex, WebRequest request) {
		final String requestUrl = resolveRequestURL(request, null);
		return new ErrorInfo<>(requestUrl, ex);
	}

	protected ErrorInfo<Object> buildErrorInfoForMissingServletRequestParameter(
			final MissingServletRequestParameterException ex, final WebRequest request)
	{
		final String requestUrl = resolveRequestURL(request, null);
		// TODO: make a DTO mapping the most important exception details or specialize ErrorInfo DTO
		final Map<String, Object> details = new LinkedHashMap<>(3);
		details.put("parameterName", ex.getParameterName());
		details.put("parameterType", ex.getParameterType());
		details.put("missingAfterConversion", ex.isMissingAfterConversion());
		return new ErrorInfo<>(requestUrl, ex, details);
	}

	protected ErrorInfo<Object> buildErrorInfoForTypeMismatch(
			final TypeMismatchException ex, final WebRequest request)
	{
		final String requestUrl = resolveRequestURL(request, null);
		// TODO: make a DTO mapping the most important exception details or specialize ErrorInfo DTO
		final Map<String, Object> details = new LinkedHashMap<>(3);
		details.put("propertyName", ex.getPropertyName());
		details.put("requiredType", ex.getRequiredType());
		details.put("value", ex.getValue());
		return new ErrorInfo<>(requestUrl, ex, details);
	}

	protected ErrorInfo<Object> buildErrorInfoForBindException(
			final BindException ex, final WebRequest request)
	{
		final String requestUrl = resolveRequestURL(request, null);
		// TODO: make a DTO mapping the most important exception details or specialize ErrorInfo DTO
		final Map<String, Object> details = new LinkedHashMap<>(6);
		details.put("errors", ex.getAllErrors());
		details.put("globalErrors", ex.getGlobalErrors());
		details.put("fieldErrors", ex.getFieldErrors());
		details.put("nestedPath", ex.getNestedPath());
		details.put("objectName", ex.getObjectName());
		details.put("suppressedFields", ex.getSuppressedFields());
		return new ErrorInfo<>(requestUrl, ex, details);
	}


	@ExceptionHandler({
		NoSuchElementException.class,
		ElementoNoExistenteException.class
	})
	public ResponseEntity<String> handleNotFoundExceptions(
			final ElementoNoExistenteException exception, final Locale locale)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
	}

	@ExceptionHandler({
		PeticionInconsistenteException.class
	})
	public ResponseEntity<String> handlePeticionInconsistenteException(
			final PeticionInconsistenteException exception, final Locale locale)
	{
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getLocalizedMessage());
	}


}
