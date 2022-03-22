package es.seresco.cursojee.videoclub.business.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import es.seresco.cursojee.videoclub.view.dto.Identificable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
public class Pelicula implements Serializable, Identificable<Long>
{
	private static final long serialVersionUID = 2258210047583395920L;

	@EqualsAndHashCode.Include
	private Long id;

	private String titulo;

	private Integer duracion;

	private Integer anio;

	private Genero genero;

	private List<Actor> actores;

	public List<Actor> getActores() {
		return new LinkedList<>(_getActores());
	}

	List<Actor> _getActores() {
		if (actores == null) {
			actores = new LinkedList<>();
		}
		return actores;
	}

	public boolean addActor(final Actor actor) {
		return _getActores().add(actor);
	}

	public boolean removeActor(final Actor actor) {
		return _getActores().remove(actor);
	}

	public List<Actor> findActores(final @NonNull Predicate<Actor> filter) {
		return _getActores().stream()
				.filter(filter)
				.collect(Collectors.toCollection(LinkedList::new));
	}

	public Optional<Actor> findActor(final Long id) {
		return _getActores().stream()
				.filter(Identificable.finder(id))
				.findFirst();
	}

}
