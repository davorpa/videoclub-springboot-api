package es.seresco.cursojee.videoclub.view.dto.pelicula;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.seresco.cursojee.videoclub.view.dto.Identificable;
import es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class ResponseSearchPeliculaDTO implements Serializable, Identificable<Long>
{

	private static final long serialVersionUID = -5284736856115509760L;

	@JsonProperty(value = "id")
	@EqualsAndHashCode.Include
	private Long id;

	@JsonProperty("titulo")
	private String titulo;

	@JsonProperty("duracion")
	private Integer duracion;

	@JsonProperty("anio")
	private Integer anio;

	@JsonProperty("genero")
	private CodigoGeneroEnum codigoGenero;

	@JsonProperty("actores")
	private List<PeliculaActorDTO> actores;
}
