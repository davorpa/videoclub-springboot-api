package es.seresco.cursojee.videoclub.business.repository.core;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import es.seresco.cursojee.videoclub.business.model.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AbstractRepository<T extends Entity<ID> & Serializable, ID extends Serializable>
		implements Repository<T, ID>
{

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		if (entityClass == null) {
			//only works if one extends AbstractRepository, we will take care of it with CDI
			ParameterizedType clazz = (ParameterizedType) getClass().getGenericSuperclass();
			// 0: <T> value type
			// 1: <ID> value type
			entityClass = (Class<T>) clazz.getActualTypeArguments()[0];
		}
		return entityClass;
	}

	private Class<ID> idClass;

	@SuppressWarnings("unchecked")
	protected Class<ID> getIdClass() {
		if (idClass == null) {
			//only works if one extends AbstractRepository, we will take care of it with CDI
			ParameterizedType clazz = (ParameterizedType) getClass().getGenericSuperclass();
			// 0: <T> value type
			// 1: <ID> value type
			idClass = (Class<ID>) clazz.getActualTypeArguments()[1];
		}
		return idClass;
	}

	protected String entityClassName() {
		String name = getEntityClass().getTypeName(); // NOSONAR
		// unproxied name
		return name;
	}

}
