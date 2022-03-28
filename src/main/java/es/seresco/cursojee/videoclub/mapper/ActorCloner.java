package es.seresco.cursojee.videoclub.mapper;

import java.util.List;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.control.DeepClone;

import es.seresco.cursojee.videoclub.business.model.Actor;

@Mapper(componentModel = "spring",
		collectionMappingStrategy = CollectionMappingStrategy.ACCESSOR_ONLY)
public interface ActorCloner
{
	static final String CLONE     = "Actor#clone";

	static final String DEEPCLONE = "Actor#deepClone";


	/**
	 * Transfiere haciendo una copia profunda de todas las propiedades
	 * excepto del identificador
	 *
	 * @param source instancia a tratar
	 * @param target instancia destino de la copia
	 */
	@MappingWithoutId
	void copyWithoutIdInto(
			final Actor source,
			final @MappingTarget Actor target);

	/**
	 * Transfiere haciendo una copia profunda de todas las propiedades
	 * excepto del identificador
	 *
	 * @param source instancia a tratar
	 * @param target instancia destino de la copia
	 */
	@DeepClone
	@MappingWithoutId
	void deepCopyWithoutIdInto(
			final Actor source,
			final @MappingTarget Actor target);

	/**
	 * Realiza una copia de todas las propiedades de <tt>source</tt>.
	 * @param source instancia a tratar
	 * @return
	 */
	@Named(CLONE)
	Actor clone( // NOSONAR
			final Actor source);

	/**
	 * Realiza una copia de todas las propiedades de <tt>source</tt>.
	 * @param source instancia a tratar
	 * @return
	 */
	@Named(CLONE)
	@IterableMapping(qualifiedByName = CLONE)
	List<Actor> clone( // NOSONAR
			final List<Actor> source);

	/**
	 * Realiza una copia profunda de todas las propiedades de <tt>source</tt>.
	 * @param source instancia a tratar
	 * @return
	 */
	@DeepClone
	@Named(DEEPCLONE)
	Actor deepClone( // NOSONAR
			final Actor source);

	/**
	 * Realiza una copia profunda de todas las propiedades de <tt>source</tt>.
	 * @param source instancia a tratar
	 * @return
	 */
	@DeepClone
	@Named(DEEPCLONE)
	@IterableMapping(elementMappingControl = DeepClone.class, qualifiedByName = DEEPCLONE)
	List<Actor> deepClone( // NOSONAR
			final List<Actor> source);

}
