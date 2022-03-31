package es.seresco.cursojee.videoclub.business.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import es.seresco.cursojee.videoclub.view.dto.genero.GeneroDTO;

@Validated
public interface GeneroService
{

	static final String BEAN_NAME = "generoService";


	@NonNull List<GeneroDTO> findAll();

}
