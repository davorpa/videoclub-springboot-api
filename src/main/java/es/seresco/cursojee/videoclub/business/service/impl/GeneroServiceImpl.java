package es.seresco.cursojee.videoclub.business.service.impl;

import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.CIENCIA_FICCION;
import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.COMEDIA;
import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.HISTORICA;
import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.ROMANTICA;
import static es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum.WESTERN;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import es.seresco.cursojee.videoclub.business.model.Genero;
import es.seresco.cursojee.videoclub.business.service.GeneroService;
import es.seresco.cursojee.videoclub.mapper.GeneroMapper;
import es.seresco.cursojee.videoclub.view.dto.genero.GeneroDTO;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service(GeneroService.BEAN_NAME)
@Setter
@NoArgsConstructor
@Slf4j
public class GeneroServiceImpl implements GeneroService {

	private static final AtomicLong backedIdSeeder = new AtomicLong(0L);
	private static final AtomicReference<List<Genero>> backedRef = new AtomicReference<>();


	@Resource
	private GeneroMapper generoMapper;


	@PostConstruct
	private void init() {
		List<Genero> generos = initBackedReference();
		generos.add(Genero.builder().codigo(COMEDIA).descripcion("Comedia").build());
		generos.add(Genero.builder().codigo(WESTERN).descripcion("Western").build());
		generos.add(Genero.builder().codigo(CIENCIA_FICCION).descripcion("Ciencia-Ficción").build());
		generos.add(Genero.builder().codigo(ROMANTICA).descripcion("Romántica").build());
		generos.add(Genero.builder().codigo(HISTORICA).descripcion("Histórica").build());
		log.debug("Initialized with: {}", generos);
	}


	@Override
	public List<GeneroDTO> findAll()
	{
		log.debug("findAll");
		return generoMapper.fromGeneros(backedReference());
	}


	protected Long nextId() {
		log.debug("nextId");
		return backedIdSeeder.incrementAndGet();
	}

	protected List<Genero> backedReference() {
//		return backedRef.getAcquire();
//		return backedRef.getOpaque();
//		return backedRef.getPlain();
		return backedRef.get();
	}

	protected List<Genero> initBackedReference() {
		log.debug("initBackedReference");
		return backedRef
			// init collection if not yet initialized
			.updateAndGet(ref -> ref == null ? new LinkedList<>(): ref);
	}

}
