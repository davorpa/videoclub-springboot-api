package es.seresco.cursojee.videoclub.view.dto;

import java.util.Objects;
import java.util.function.Predicate;

public interface Identificable<PK> {

	PK getId();

	static <PK> Predicate<Identificable<PK>> finder(final PK id) {
		return i -> Objects.equals(i.getId(), id);
	}
}
