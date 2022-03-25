package es.seresco.cursojee.videoclub.exception;

import static java.lang.String.format;

import java.io.Serializable;
import java.util.function.Supplier;

import es.seresco.cursojee.videoclub.view.dto.Identificable;

public class ElementoNoExistenteException extends Exception implements Identificable<Serializable> {

	private static final long serialVersionUID = 6004871815354527714L;

	private final String type;

	private final Serializable id;


	public ElementoNoExistenteException(final String type, final Serializable id)
	{
		super(format("The `%s` identified by `%s` doesn't exist.", type, id));
		this.type = type;
		this.id = id;
	}

	public ElementoNoExistenteException(final String type, final Serializable id, final Throwable cause)
	{
		this(type, id);
		initCause(cause);
	}

	public ElementoNoExistenteException(final Class<?> type, final Serializable id)
	{
		this(type.getTypeName(), id);
	}

	public ElementoNoExistenteException(final Class<?> type, final Serializable id, final Throwable cause)
	{
		this(type, id);
		initCause(cause);
	}

	public static Supplier<ElementoNoExistenteException> creater(final String type, final Serializable id) {
		return () -> new ElementoNoExistenteException(type, id);
	}

	public static Supplier<ElementoNoExistenteException> creater(final Class<?> type, final Serializable id) {
		return () -> new ElementoNoExistenteException(type, id);
	}


	public String getType() {
		return type;
	}

	@Override
	public Serializable getId() {
		return id;
	}

}
