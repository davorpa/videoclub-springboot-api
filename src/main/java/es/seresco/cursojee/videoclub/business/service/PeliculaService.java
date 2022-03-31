package es.seresco.cursojee.videoclub.business.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import es.seresco.cursojee.videoclub.exception.ElementoNoExistenteException;
import es.seresco.cursojee.videoclub.view.dto.pelicula.CustomSearchPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestActualizarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestBorrarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestCrearPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponsePeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponseSearchPeliculaDTO;

@Validated
public interface PeliculaService
{

	static final String BEAN_NAME = "peliculaService";


	@NonNull List<ResponsePeliculaDTO> findAll();

	@NonNull List<ResponseSearchPeliculaDTO> search(
			final @Valid @Nullable CustomSearchPeliculaDTO query);

	@NonNull ResponsePeliculaDTO findById(
			final @NotNull @NonNull Long id)
					throws ElementoNoExistenteException;

	@NonNull ResponsePeliculaDTO create(
			final @Valid @NotNull @NonNull RequestCrearPeliculaDTO requestCrearPeliculaDTO)
					throws ElementoNoExistenteException;

	@NonNull ResponsePeliculaDTO update(
			final @Valid @NotNull @NonNull RequestActualizarPeliculaDTO requestActualizarPeliculaDTO)
					throws ElementoNoExistenteException;

	void delete(
			final @Valid @NotNull @NonNull RequestBorrarPeliculaDTO requestBorrarPeliculaDTO)
					throws ElementoNoExistenteException;

}
