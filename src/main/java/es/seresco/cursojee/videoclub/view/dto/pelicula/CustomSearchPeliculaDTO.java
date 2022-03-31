package es.seresco.cursojee.videoclub.view.dto.pelicula;

import java.io.Serializable;

import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomSearchPeliculaDTO implements Serializable
{

	private static final long serialVersionUID = -7043871767773185547L;

	@JsonProperty("titulo")
	private String titulo;

	@JsonProperty("duracion")
	@PositiveOrZero
	private Integer duracion;

	@JsonProperty("anio")
	@PositiveOrZero
	private Integer anio;

	@JsonProperty("actor")
	private String actor;

}
