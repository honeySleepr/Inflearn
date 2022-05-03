package jpabook.jpashop.domain;

import java.util.Objects;
import javax.persistence.Entity;

@Entity
public class Movie extends Item {

	private String director;
	private String actor;

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Movie movie = (Movie) o;
		return Objects.equals(director, movie.director) && Objects.equals(actor,
			movie.actor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(director, actor);
	}
}
