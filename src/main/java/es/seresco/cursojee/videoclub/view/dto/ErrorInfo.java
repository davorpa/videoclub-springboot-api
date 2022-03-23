package es.seresco.cursojee.videoclub.view.dto;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@RequiredArgsConstructor
@SuperBuilder
public class ErrorInfo<D> {

	@JsonProperty(value = "url")
	public final String url;

	@JsonProperty(value = "type")
	public final String type;

	@JsonProperty(value = "message")
	public final String message;

	@JsonProperty(value = "details")
	public final D details;


	public ErrorInfo(
			final String url, final @NonNull Throwable exception,
			final D details)
	{
		this(url, exception.getClass(), exception.getLocalizedMessage(), details);
	}

	public ErrorInfo(
			final String url, final @NonNull Class<?> type, final @NonNull String message,
			final D details)
	{
		this(url, type.getTypeName(), message, details);
	}

	public ErrorInfo(final String url, final @NonNull Throwable exception)
	{
		this(url, exception, null);
	}

	public ErrorInfo(final String url, final @NonNull Class<?> type, final @NonNull String message)
	{
		this(url, type, message, null);
	}

}
