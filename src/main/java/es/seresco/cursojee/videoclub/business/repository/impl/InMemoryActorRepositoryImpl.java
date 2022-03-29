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

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.business.repository.ActorRepository;
import es.seresco.cursojee.videoclub.business.repository.core.AbstractRepository;
import es.seresco.cursojee.videoclub.business.repository.core.CrudRepository;
import es.seresco.cursojee.videoclub.mapper.ActorCloner;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository(ActorRepository.BEAN_NAME)
@NoArgsConstructor
@Slf4j
public class InMemoryActorRepositoryImpl
		extends AbstractRepository<Actor, Long>
		implements CrudRepository<Actor, Long>,
				ActorRepository
{

	private static final AtomicLong backedIdSeeder = new AtomicLong(0L);
	private static final AtomicReference<Map<Long, Actor>> backedRef = new AtomicReference<>();

	@Resource
	private ActorCloner actorCloner;

	@PostConstruct
	private void init()
	{
		Map<Long, Actor> actores = initBackedReference();
		log.debug("Initialized with: {}", actores);
	}

	protected @NonNull Optional<Map<Long, Actor>> backedReference()
	{
		Map<Long, Actor> ref;
//		ref = backedRef.getAcquire();
//		ref = backedRef.getOpaque();
//		ref = backedRef.getPlain();
		ref = backedRef.get();
		return Optional.ofNullable(ref);
	}

	protected @NonNull Map<Long, Actor> initBackedReference()
	{
		log.debug("initBackedReference(empty={})", backedReference().isEmpty());
		return backedRef
			// Init collection if not yet initialized.
			// We use Map to boost findById search,
			// it's ordered by keys to preserve insert order and
			// last but not least, concurrent to ensure operational atomicity.
			.updateAndGet(ref -> ref == null ? new ConcurrentSkipListMap<>(): ref);
	}

	protected Function<Map<Long, Actor>, Actor> refFinder(final Long id) {
		return map -> map.get(id);
	}

	protected Function<Map<Long, Actor>, Actor> refRemover(final Long id) {
		return map -> map.remove(id);
	}


	@Override
	public @NonNull Collection<Actor> findAll()
	{
		Optional<Map<Long, Actor>> ref = backedReference();
		log.debug("findAllActores(empty={})", ref.isEmpty());
		return new LinkedList<>(ref.map(Map::values).orElseGet(List::of));
	}

	@Override
	public @NonNull Optional<Actor> findById(final @NonNull Long id)
	{
		Optional<Map<Long, Actor>> ref = backedReference();
		log.debug("findByIdActor(empty={})", ref.isEmpty());
		return ref.map(refFinder(id));
	}

	@Override
	public @NonNull long count()
	{
		Optional<Map<Long, Actor>> ref = backedReference();
		log.debug("countActores(empty={})", ref.isEmpty());
		return ref.map(Map::size).orElse(0).longValue();
	}

	@Override
	public @NonNull <S extends Actor> S create(final @NonNull S entity)
	{
		Long id;
		entity.setId(id = backedIdSeeder.incrementAndGet());
		initBackedReference().put(id, entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public @NonNull <S extends Actor> S update(final @NonNull S entity)
	{
		Long id;
		Objects.requireNonNull(id = entity.getId(), "`actor.id` must be non-null");
		// find
		final Optional<Actor> entitial = backedReference().map(refFinder(id));
		// do update
		final Consumer<Actor> updater =
				target -> actorCloner.copyWithoutIdInto(entity, target);
		entitial.ifPresent(updater);
		// return altered instance, throws NoSuchElementException if `entitial` not found
		return (S) entitial.orElseThrow();
	}

	@Override
	public @NonNull boolean delete(final @NonNull Actor entity)
	{
		Long id;
		Objects.requireNonNull(id = entity.getId(), "`actor.id` must be non-null");
		// find
		Optional<Map<Long, Actor>> ref = backedReference();
		// do remove
		return ref.isPresent() && ref.map(refRemover(id)).isPresent();
	}

}
