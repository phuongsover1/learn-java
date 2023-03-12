package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.constants.MovieGenre;
import org.apache.commons.lang3.StringUtils;

public class Movie extends Bookmark {
	private int releaseYear;
	private String[] cast;
	private String[] directors;
	private MovieGenre genre;
	private double imdbRating;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Movie{");
		sb.append("releaseYear=").append(releaseYear);
		sb.append(", cast=").append(StringUtils.join(cast, ","));
		sb.append(", directors=").append(StringUtils.join(directors, ","));
		sb.append(", genre='").append(genre).append('\'');
		sb.append(", imdbRating=").append(imdbRating);
		sb.append('}');
		return sb.toString();
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String[] getCast() {
		return cast;
	}

	public void setCast(String[] cast) {
		this.cast = cast;
	}

	public String[] getDirectors() {
		return directors;
	}

	public void setDirectors(String[] directors) {
		this.directors = directors;
	}

	public MovieGenre getGenre() {
		return genre;
	}

	public void setGenre(MovieGenre genre) {
		this.genre = genre;
	}

	public double getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(double imdbRating) {
		this.imdbRating = imdbRating;
	}

	@Override
	public boolean isKidFriendlyEligible() {
		return !genre.equals(MovieGenre.HORROR) && !genre.equals(MovieGenre.THRILLERS);
	}
}
