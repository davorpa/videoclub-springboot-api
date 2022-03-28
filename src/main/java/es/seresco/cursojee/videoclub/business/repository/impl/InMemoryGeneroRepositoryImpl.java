package es.seresco.cursojee.videoclub.business.repository.impl;

import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.CIENCIA_FICCION;
import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.COMEDIA;
import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.HISTORICA;
import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.ROMANTICA;
import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.WESTERN;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import es.seresco.cursojee.videoclub.business.model.Genero;
import es.seresco.cursojee.videoclub.business.repository.GeneroRepository;
import es.seresco.cursojee.videoclub.business.repository.core.AbstractRepository;
import es.seresco.cursojee.videoclub.business.repository.core.ReadOnlyRepository;
import es.seresco.cursojee.videoclub.view.dto.Identificable;
import es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum;
import lombok.extern.slf4j.Slf4j;

@Repository(GeneroRepository.BEAN_NAME)
@Slf4j
public class InMemoryGeneroRepositoryImpl
		extends AbstractRepository<Genero, CodigoGeneroEnum>
		implements ReadOnlyRepository<Genero, CodigoGeneroEnum>,
				GeneroRepository
{

	private static final AtomicReference<List<Genero>> backedRef = new AtomicReference<>();


	@PostConstruct
	private void init()
	{
		List<Genero> generos = initBackedReference();
		generos.add(Genero.builder().codigo(COMEDIA).descripcion("Comedia").build());
		generos.add(Genero.builder().codigo(WESTERN).descripcion("Western").build());
		generos.add(Genero.builder().codigo(CIENCIA_FICCION).descripcion("Ciencia-Ficción").build());
		generos.add(Genero.builder().codigo(ROMANTICA).descripcion("Romántica").build());
		generos.add(Genero.builder().codigo(HISTORICA).descripcion("Histórica").build());
		log.debug("Initialized with: {}", generos);
	}

	protected @NonNull Optional<List<Genero>> backedReference()
	{
		List<Genero> ref;
//		ref = backedRef.getAcquire();
//		ref = backedRef.getOpaque();
//		ref = backedRef.getPlain();
		ref = backedRef.get();
		return Optional.ofNullable(ref);
	}

	protected @NonNull List<Genero> initBackedReference()
	{
		log.debug("initBackedReference(empty={})", backedReference().isEmpty());
		return backedRef
			// init collection if not yet initialized
			.updateAndGet(ref -> ref == null ? new LinkedList<>(): ref);
	}

	@Override
	public @NonNull Collection<Genero> findAll()
	{
		Optional<List<Genero>> ref = backedReference();
		log.debug("findAllGeneros(empty={})", ref.isEmpty());
		return new LinkedList<>(ref.orElseGet(List::of));
	}

	@Override
	public @NonNull Optional<Genero> findById(final CodigoGeneroEnum id)
	{
		Optional<List<Genero>> ref = backedReference();
		log.debug("findByIdGenero(empty={})", ref.isEmpty());
		return ref.orElseGet(List::of)
				.stream()
				.filter(Identificable.finder(id))
				.findFirst();
	}

	@Override
	public @NonNull long count()
	{
		Optional<List<Genero>> ref = backedReference();
		log.debug("countGeneros(empty={})", ref.isEmpty());
		return ref.map(Collection::size).orElse(0).longValue();
	}

}
