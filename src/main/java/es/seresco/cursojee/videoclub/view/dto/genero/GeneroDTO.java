package es.seresco.cursojee.videoclub.view.dto.genero;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeneroDTO {
	
	@JsonProperty("codigo")
	@NotBlank
	private CodigoGeneroEnum codigo;
	
	@JsonProperty("descripcion")
	@NotBlank
	private String descripcion;

}
