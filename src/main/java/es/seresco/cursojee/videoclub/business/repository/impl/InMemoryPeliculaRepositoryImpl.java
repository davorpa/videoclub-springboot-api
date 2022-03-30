package es.seresco.cursojee.videoclub.business.repository.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import es.seresco.cursojee.videoclub.business.model.Pelicula;
import es.seresco.cursojee.videoclub.business.repository.PeliculaRepository;
import es.seresco.cursojee.videoclub.business.repository.core.AbstractRepository;
import es.seresco.cursojee.videoclub.business.repository.core.CrudRepository;
import es.seresco.cursojee.videoclub.mapper.PeliculaCloner;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository(PeliculaRepository.BEAN_NAME)
@NoArgsConstructor
@Slf4j
public class InMemoryPeliculaRepositoryImpl
		extends AbstractRepository<Pelicula, Long>
		implements CrudRepository<Pelicula, Long>,
				PeliculaRepository
{

	private static final AtomicLong backedIdSeeder = new AtomicLong(0L);
	private static final AtomicReference<Map<Long, Pelicula>> backedRef = new AtomicReference<>();

	@Resource
	private PeliculaCloner peliculaCloner;

	@PostConstruct
	private void init()
	{
		Map<Long, Pelicula> peliculas = initBackedReference();
		log.debug("Initialized with: {}", peliculas);
	}

	protected @NonNull Optional<Map<Long, Pelicula>> backedReference()
	{
		Map<Long, Pelicula> ref;
//		ref = backedRef.getAcquire();
//		ref = backedRef.getOpaque();
//		ref = backedRef.getPlain();
		ref = backedRef.get();
		return Optional.ofNullable(ref);
	}

	protected @NonNull Map<Long, Pelicula> initBackedReference()
	{
		log.debug("initBackedReference(empty={})", backedReference().isEmpty());
		return backedRef
			// Init collection if not yet initialized.
			// We use Map to boost findById search,
			// it's ordered by keys to preserve insert order and
			// last but not least, concurrent to ensure operational atomicity.
			.updateAndGet(ref -> ref == null ? new ConcurrentSkipListMap<>(): ref);
	}

	protected Function<Map<Long, Pelicula>, Pelicula> refFinder(final Long id) {
		return map -> map.get(id);
	}

	protected Function<Map<Long, Pelicula>, Pelicula> refRemover(final Long id) {
		return map -> map.remove(id);
	}


	@Override
	public @NonNull Collection<Pelicula> findAll()
	{
		Optional<Map<Long, Pelicula>> ref = backedReference();
		log.debug("findAllPeliculas(empty={})", ref.isEmpty());
		return new LinkedList<>(ref.map(Map::values).orElseGet(List::of));
	}

	@Override
	public @NonNull Optional<Pelicula> findById(final @NonNull Long id)
	{
		Optional<Map<Long, Pelicula>> ref = backedReference();
		log.debug("findByIdPelicula(empty={})", ref.isEmpty());
		return ref.map(refFinder(id));
	}

	@Override
	public @NonNull long count()
	{
		Optional<Map<Long, Pelicula>> ref = backedReference();
		log.debug("countPeliculas(empty={})", ref.isEmpty());
		return ref.map(Map::size).orElse(0).longValue();
	}

	@Override
	public @NonNull <S extends Pelicula> S create(final @NonNull S entity)
	{
		Long id;
		entity.setId(id = backedIdSeeder.incrementAndGet());
		initBackedReference().put(id, entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public @NonNull <S extends Pelicula> S update(final @NonNull S entity)
	{
		Long id;
		Objects.requireNonNull(id = entity.getId(), "`pelicula.id` must be non-null");
		// find
		final Optional<Pelicula> entitial = backedReference().map(refFinder(id));
		// do update
		final Consumer<Pelicula> updater =
				target -> peliculaCloner.copyWithoutIdInto(entity, target);
		entitial.ifPresent(updater);
		// return altered instance, throws NoSuchElementException if `entitial` not found
		return (S) entitial.orElseThrow();
	}

	@Override
	public @NonNull boolean delete(final @NonNull Pelicula entity)
	{
		Long id;
		Objects.requireNonNull(id = entity.getId(), "`pelicula.id` must be non-null");
		// find
		Optional<Map<Long, Pelicula>> ref = backedReference();
		// do remove
		Optional<Pelicula> pelicula;
		if (ref.isPresent() && (pelicula = ref.map(refRemover(id))).isPresent()) {
			// perform cascade clean-up to avoid memory leaks
			pelicula.ifPresent(Pelicula::removeActores);
			return true;
		}
		return false;
	}

}
