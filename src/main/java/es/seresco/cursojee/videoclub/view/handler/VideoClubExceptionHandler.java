package es.seresco.cursojee.videoclub.view.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import es.seresco.cursojee.videoclub.exception.NoSuchEntityException;
import es.seresco.cursojee.videoclub.exception.BadRequestException;
import es.seresco.cursojee.videoclub.view.dto.ErrorInfo;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class VideoClubExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String DOT = ".";



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
			} else if (ex instanceof ConstraintViolationException) {
				body = buildErrorInfoForConstraintViolation((ConstraintViolationException) ex, request);
			}
			// TODO: handle other significant Exceptions mapped in ResponseEntityExceptionHandler
			else {
				body = buildErrorInfo(ex, request);
			}
		}
		if (log.isInfoEnabled()) {
			log.info("Requested URL {} ends with a {} :: {}",
					resolveRequestURL(request, null), status, body,
					ex);
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
		ConstraintViolationException.class
	})
	@Nullable
	public ResponseEntity<Object> handleConstraintViolations(
			final Exception ex, WebRequest request)
	{
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.BAD_REQUEST;

		return handleExceptionInternal(ex, null, headers, status, request);
	}

	protected ErrorInfo<Object> buildErrorInfoForConstraintViolation(
			final ConstraintViolationException ex, final WebRequest request)
	{
		final String requestUrl = resolveRequestURL(request, null);
		// TODO: make a DTO mapping the most important exception details or specialize ErrorInfo DTO
		final Map<String, Object> details = new LinkedHashMap<>(1);
		details.put("errors", ex.getConstraintViolations().stream()
				.map(this::mapConstraintViolationToFieldError).collect(Collectors.toSet()));
		return new ErrorInfo<>(requestUrl, ex, details);
	}

	protected FieldError mapConstraintViolationToFieldError(
			final ConstraintViolation<?> violation)
	{
		String violationMessageTemplate = violation.getMessageTemplate();
		String violationSingleName = StringUtils.substringAfterLast(
				violation.getConstraintDescriptor().getAnnotation().annotationType().getTypeName(), DOT);
		String rootBeanSingleName = StringUtils.substringAfterLast(violation.getRootBeanClass().getTypeName(), DOT);
		Object rejectedValue = violation.getInvalidValue();
		String rejectedValueTypeName = rejectedValue == null ? null : rejectedValue.getClass().getTypeName();
		String field = violation.getPropertyPath().toString();
		// deinterpolate template: ussually "{ key }"
		String msgKey = StringUtils.removeEnd(StringUtils.removeStart(violationMessageTemplate, "{") , "}");
		msgKey = StringUtils.removeEnd(msgKey, ".message");

		return new FieldError(rootBeanSingleName, field, rejectedValue, false,
		// compose message keys of violation according to `org.springframework.web.bind.MethodArgumentNotValidException` format
				new String[] {
					String.join(DOT, msgKey, rootBeanSingleName, field),
					String.join(DOT, msgKey, field),
					String.join(DOT, msgKey, field, rejectedValueTypeName),
					msgKey,
					String.join(DOT, violationSingleName, rootBeanSingleName, field),
					String.join(DOT, violationSingleName, field),
					String.join(DOT, violationSingleName, field, rejectedValueTypeName),
					violationSingleName
				},
		// and of the arguments too
				StreamSupport.stream(
						violation.getPropertyPath().spliterator(), false)
					.map(this::mapConstraintViolationPathNodeToFieldErrorArgument)
					.collect(Collectors.toList())
					.toArray(),
		// interpolated message as default
				violation.getMessage());
	}

	protected Map<String, Object> mapConstraintViolationPathNodeToFieldErrorArgument(
			final Path.Node pathNode)
	{
		Map<String, Object> arg = new LinkedHashMap<>(10);
		ElementKind kind;
		arg.put("codes", List.of(pathNode.getName()));
		arg.put("defaultMessage", pathNode.toString());
		arg.put("code", pathNode.getName());
		arg.put("kind", kind = pathNode.getKind());
		arg.put("index", pathNode.getIndex());
		arg.put("key", pathNode.getKey());
		if (pathNode instanceof NodeImpl) { // Hibernate
			NodeImpl node = (NodeImpl) pathNode;
			try { arg.put("typeArgumentIndex", node.getTypeArgumentIndex()); } catch (Exception e) { /*NOSONAR*/ }
			try {
				Class<?> containerClass = node.getContainerClass();
				arg.put("containerType", containerClass != null
						? StringUtils.substringAfterLast(containerClass.getTypeName(), DOT)
						: StringUtils.substringAfterLast(
								node.getParent().getParent().getParameterTypes().get(
										node.getParent().getParameterIndex()
								).getTypeName(), DOT));
			} catch (Exception e) { /*NOSONAR*/ }
			if (kind == ElementKind.METHOD) {
				try { arg.put("parameterTypes", node.getParameterTypes()); } catch (Exception e) { /*NOSONAR*/ }
			}
			try {
				arg.put("index", node.getParameterIndex());
				arg.put("objectType", StringUtils.substringAfterLast(
						node.getParent().getParameterTypes().get(node.getParameterIndex()).getTypeName(), DOT));
			} catch (Exception e) { /*NOSONAR*/ }
		}
		return arg;
	}



	@ExceptionHandler({
		NoSuchElementException.class,
		NoSuchEntityException.class
	})
	@Nullable
	public ResponseEntity<Object> handleNotFoundExceptions(
			final Exception ex, WebRequest request)
	{
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.NOT_FOUND;
		Object body;
		if (ex instanceof NoSuchEntityException) {
			body = buildErrorInfoForElementoNoExistente((NoSuchEntityException) ex, request);
		} else {
			body = buildErrorInfo(ex, request);
		}

		return handleExceptionInternal(ex, body, headers, status, request);
	}

	protected Object buildErrorInfoForElementoNoExistente(
			final NoSuchEntityException ex, final WebRequest request)
	{
		final String requestUrl = resolveRequestURL(request, null);
		// TODO: make a DTO mapping the most important exception details or specialize ErrorInfo DTO
		final Map<String, Object> details = new LinkedHashMap<>(2);
		details.put("type", ex.getType());
		details.put("value", ex.getId());
		return new ErrorInfo<>(requestUrl, ex, details);
	}



	@ExceptionHandler({
		BadRequestException.class
	})
	@Nullable
	public ResponseEntity<Object> handlePeticionInconsistenteException(
			final BadRequestException ex, WebRequest request)
	{
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		String body = ex.getLocalizedMessage();
		if (log.isInfoEnabled()) {
			log.info("Requested URL {} ends with a {} :: {}",
					resolveRequestURL(request, null), status, body,
					ex);
		}
		return ResponseEntity.status(status).body(body);
	}

}
