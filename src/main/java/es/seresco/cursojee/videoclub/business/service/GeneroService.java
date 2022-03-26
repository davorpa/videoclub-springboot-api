package es.seresco.cursojee.videoclub.business.service;

import java.util.List;

import es.seresco.cursojee.videoclub.view.dto.genero.GeneroDTO;

public interface GeneroService
{

	static final String BEAN_NAME = "generoService";


	public List<GeneroDTO> findAll();

}
