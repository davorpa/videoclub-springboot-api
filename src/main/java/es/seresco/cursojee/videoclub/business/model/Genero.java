package es.seresco.cursojee.videoclub.business.model;

import java.io.Serializable;

import es.seresco.cursojee.videoclub.view.dto.Identificable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
public class Genero implements Serializable, Identificable<CodigoGeneroEnum>
{
	private static final long serialVersionUID = -5244464357032577373L;

	@EqualsAndHashCode.Include
	private CodigoGeneroEnum codigo;

	private String descripcion;

	@Override
	public CodigoGeneroEnum getId() {
		return codigo;
	}

}
