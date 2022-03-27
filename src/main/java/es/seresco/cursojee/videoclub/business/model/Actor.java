package es.seresco.cursojee.videoclub.business.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
public class Actor implements Entity<Long>
{
	private static final long serialVersionUID = -3999305628353816581L;

	@EqualsAndHashCode.Include
	private Long id;

	private String nombre;

	private String primerApellido;

	private String segundoApellido;

	private Date fechaNacimiento;

}
