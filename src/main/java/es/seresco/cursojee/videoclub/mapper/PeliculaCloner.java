package es.seresco.cursojee.videoclub.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.BeforeMapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.control.DeepClone;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.business.model.Pelicula;

@Mapper(componentModel = "spring",
		collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
		uses = { ActorCloner.class })
public interface PeliculaCloner
{
	static final String CLONE     = "Pelicula#clone";

	static final String DEEPCLONE = "Pelicula#deepClone";


	@BeforeMapping
	default void setupCopyInto(
			final Pelicula source,
			final @MappingTarget Pelicula target)
	{
		// emulate collectionMappingStrategy: ADDER_PREFERRED + ACCESOR_ONLY
		// because setActores returns a new reference to ensure encapsulation
		Collection<Actor> actores = source.getActores();
		if (actores != null) {
			target.removeActores();
			//target.addActores(actores);
		}
	}

	/**
	 * Transfiere haciendo una copia profunda de todas las propiedades
	 * excepto del identificador
	 *
	 * @param source instancia a tratar
	 * @param target instancia destino de la copia
	 */
	@MappingWithoutId
	@Mapping(target = "actoresIds", ignore = true)
	//@Mapping(target = "actores", qualifiedByName = ActorCloner.CLONE)
	void copyWithoutIdInto(
			final Pelicula source,
			final @MappingTarget Pelicula target);

	/**
	 * Transfiere haciendo una copia profunda de todas las propiedades
	 * excepto del identificador
	 *
	 * @param source instancia a tratar
	 * @param target instancia destino de la copia
	 */
	@DeepClone
	@MappingWithoutId
	@Mapping(target = "actoresIds", ignore = true)
	//@Mapping(target = "actores", qualifiedByName = ActorCloner.DEEPCLONE)
	void deepCopyWithoutIdInto(
			final Pelicula source,
			final @MappingTarget Pelicula target);

	/**
	 * Realiza una copia de todas las propiedades de <tt>source</tt>.
	 * @param source instancia a tratar
	 * @return
	 */
	@Named(CLONE)
	@Mapping(target = "actores", qualifiedByName = ActorCloner.CLONE)
	Pelicula clone( // NOSONAR
			final Pelicula source);

	/**
	 * Realiza una copia de todas las propiedades de <tt>source</tt>.
	 * @param source instancia a tratar
	 * @return
	 */
	@Named(CLONE)
	@IterableMapping(qualifiedByName = CLONE)
	List<Pelicula> clone( // NOSONAR
			final List<Pelicula> source);

	/**
	 * Realiza una copia profunda de todas las propiedades de <tt>source</tt>.
	 * @param source instancia a tratar
	 * @return
	 */
	@DeepClone
	@Named(DEEPCLONE)
	@Mapping(target = "actores", qualifiedByName = ActorCloner.DEEPCLONE)
	Pelicula deepClone( // NOSONAR
			final Pelicula source);

	/**
	 * Realiza una copia profunda de todas las propiedades de <tt>source</tt>.
	 * @param source instancia a tratar
	 * @return
	 */
	@DeepClone
	@Named(DEEPCLONE)
	@IterableMapping(elementMappingControl = DeepClone.class, qualifiedByName = DEEPCLONE)
	List<Pelicula> deepClone( // NOSONAR
			final List<Pelicula> source);

}
