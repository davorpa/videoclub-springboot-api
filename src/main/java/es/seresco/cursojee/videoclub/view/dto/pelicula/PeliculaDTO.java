package es.seresco.cursojee.videoclub.view.dto.pelicula;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum;
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
	@Max(500)
	@NotNull
	private Integer duracion;

	@JsonProperty("anio")
	@PositiveOrZero
	@Min(1900)
	@NotNull
	private Integer anio;

	@JsonProperty("genero")
	@NotNull
	private CodigoGeneroEnum codigoGenero;

	@JsonProperty("actores")
	@Size(min = 1)
	@UniqueElements
	@NotNull
	private List<Long> actores;

}
