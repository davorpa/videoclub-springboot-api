package es.seresco.cursojee.videoclub.business.model;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

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

	/**
	 * Concatena {@link #getNombre()} y {@link #getApellido()} para formar el
	 * nombre completo del empleado.
	 * @return
	 */
	public String getNombreCompleto() {
		return Stream.of(getNombre(), getPrimerApellido(), getSegundoApellido())
				// filter and transform: trimToNull
				.map(StringUtils::trimToNull)
				.filter(Objects::nonNull)
				// join
				.collect(Collectors.joining(StringUtils.SPACE));
	}

}
