package es.seresco.cursojee.videoclub.exception;

import static java.lang.String.format;

import java.io.Serializable;
import java.util.function.Supplier;

import es.seresco.cursojee.videoclub.view.dto.Identificable;

public class EntityAlreadyExistsException
		extends PreconditionalException
		implements Identificable<Serializable>
{

	private static final long serialVersionUID = 4187554634461423264L;

	//
	// Campos
	//

	private final String type;

	private final Serializable id;

	//
	// Constructores.
	// Al menos con uno sin parametros (Bean) y otros con los NotNull
	//

	public EntityAlreadyExistsException(final String type, final Serializable id)
	{
		super(format("The `%s` identified by `%s` already exist.", type, id));
		this.type = type;
		this.id = id;
	}

	public EntityAlreadyExistsException(final String type, final Serializable id, final Throwable cause)
	{
		this(type, id);
		initCause(cause);
	}

	public EntityAlreadyExistsException(final Class<?> type, final Serializable id)
	{
		this(type.getTypeName(), id);
	}

	public EntityAlreadyExistsException(final Class<?> type, final Serializable id, final Throwable cause)
	{
		this(type, id);
		initCause(cause);
	}

	public static Supplier<EntityAlreadyExistsException> creater(final String type, final Serializable id) {
		return () -> new EntityAlreadyExistsException(type, id);
	}

	public static Supplier<EntityAlreadyExistsException> creater(final Class<?> type, final Serializable id) {
		return () -> new EntityAlreadyExistsException(type, id);
	}

	//
	// Getters y setters
	//

	public String getType() {
		return type;
	}

	@Override
	public Serializable getId() {
		return id;
	}

}
