package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.constants.MovieGenre;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MovieTest {

    @Test
    void testIsKidFriendlyEligible() {

        // Test 1: Movie genre is HORROR -> false
        Movie movie = BookmarkManager.getInstance().createMovie(3000, "Citizen Kane", 1941, new String[]{"Orson Welles", "Joseph Cotten"}, new String[]{"Orson Welles"}, MovieGenre.HORROR, 8.5);

        boolean isKidFriendlyEligible = movie.isKidFriendlyEligible();
        assertFalse(isKidFriendlyEligible, "Movie genre is HORROR -> isKidFriendlyEligible() must return false");

        // Test 2: Movie genre is THRILLER -> false
        movie = BookmarkManager.getInstance().createMovie(3000, "Citizen Kane", 1941, new String[]{"Orson Welles", "Joseph Cotten"}, new String[]{"Orson Welles"}, MovieGenre.THRILLERS, 8.5);

        isKidFriendlyEligible = movie.isKidFriendlyEligible();

        assertFalse(isKidFriendlyEligible, "Movie genre is THRILLER -> isKidFriendlyEligible() must return false");

    }

}
