package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.constants.BookGenre;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BookTest {
    @Test
    void testIsKidFriendlyEligible() {
        // Test 1: Book's genre is PHILOSOPHY -> false
        Book book = BookmarkManager.getInstance().createBook(4000, "Walden", 1854, "Wilder Publications", new String[]{"Henry", "David", "Thoreau"}, BookGenre.PHILOSOPHY, 4.3);

        boolean isKidFriendlyEligible = book.isKidFriendlyEligible();

        assertFalse(isKidFriendlyEligible, "Book's genre is PHILOSOPHY -> isKidFriendlyEligible() must return false");


        // Test 2: Book's genre is SELF HELP -> fase
         book = BookmarkManager.getInstance().createBook(4000, "Walden", 1854, "Wilder Publications", new String[]{"Henry", "David", "Thoreau"}, BookGenre.SELF_HELP, 4.3);

       isKidFriendlyEligible = book.isKidFriendlyEligible();

        assertFalse(isKidFriendlyEligible, "Book's genre is SELF_HELP -> isKidFriendlyEligible() must return false");
    }
}
