package com.semanticsquare.thrillio.entities;

import java.util.Arrays;

public class Book extends Bookmark {
    private int publicationYear;
    private String publisher;
    private String[] authors;
    private String genre;
    private double amazonRating;

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }


    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Book{");
        sb.append("publicationYear=").append(publicationYear);
        sb.append(", publisher='").append(publisher).append('\'');
        sb.append(", authors=").append(Arrays.toString(authors));
        sb.append(", genre='").append(genre).append('\'');
        sb.append(", amazonRating=").append(amazonRating);
        sb.append('}');
        return sb.toString();
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getAmazonRating() {
        return amazonRating;
    }

    public void setAmazonRating(double amazonRating) {
        this.amazonRating = amazonRating;
    }

}
