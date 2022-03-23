package es.seresco.cursojee.videoclub.view.dto.pelicula;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.seresco.cursojee.videoclub.view.dto.CodigoGeneroEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
public class PeliculaDTO implements Serializable
{

	private static final long serialVersionUID = -9057641858169408552L;

	@JsonProperty("titulo")
	@NotBlank
	private String titulo;

	@JsonProperty("duracion")
	@PositiveOrZero
	@NotNull
	private Integer duracion;

	@JsonProperty("anio")
	@NotNull
	private Integer anio;

	@JsonProperty("actores")
	@Size(min = 1)
	@NotNull
	private List<Long> actores;

	@JsonProperty("codigoGenero")
	@NotNull
	private CodigoGeneroEnum codigoGenero;

}
