package es.seresco.cursojee.videoclub.business.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import es.seresco.cursojee.videoclub.exception.ElementoNoExistenteException;
import es.seresco.cursojee.videoclub.view.dto.pelicula.CustomSearchPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestActualizarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestBorrarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestCrearPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponsePeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponseSearchPeliculaDTO;

public interface PeliculaService
{

	static final String BEAN_NAME = "peliculaService";


	List<ResponsePeliculaDTO> findAll();

	List<ResponseSearchPeliculaDTO> search(
			final @Nullable CustomSearchPeliculaDTO query);

	ResponsePeliculaDTO findById(
			final @NonNull Long id) throws ElementoNoExistenteException;

	ResponsePeliculaDTO create(
			final @NonNull RequestCrearPeliculaDTO requestCrearPeliculaDTO) throws ElementoNoExistenteException;

	ResponsePeliculaDTO update(
			final @NonNull RequestActualizarPeliculaDTO requestActualizarPeliculaDTO) throws ElementoNoExistenteException;

	void delete(
			final @NonNull RequestBorrarPeliculaDTO requestBorrarPeliculaDTO) throws ElementoNoExistenteException;

}
