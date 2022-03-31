package es.seresco.cursojee.videoclub.view.dto.pelicula;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.seresco.cursojee.videoclub.view.dto.Identificable;
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
public class PeliculaActorDTO implements Serializable, Identificable<Long>
{

	private static final long serialVersionUID = -1634027359299634110L;

	@JsonProperty(value = "id")
	@EqualsAndHashCode.Include
	private Long id;

	@JsonProperty("nombre")
	private String nombre;

	@JsonProperty("fechaNacimiento")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date fechaNacimiento;

}
