package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.constants.KidFriendlyStatus;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebLinkTest {

    @Test
    public void testIsKidFriendlyEligible() {
        // Test 1: porn in url -- false
        WebLink webLink = BookmarkManager.getInstance().createWeblink(2000, "Taming Tiger, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-porn--part-2.html", "http://www.javaworld.com", KidFriendlyStatus.UNKNOWN);

        boolean isKidFriendlyEligible = webLink.isKidFriendlyEligible();

        assertFalse(isKidFriendlyEligible, "For porn in url - isKidFriendlyEligible() must return false");

        // Test 2: porn in title -- false
        webLink = BookmarkManager.getInstance().createWeblink(2000, "Taming porn, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.javaworld.com", KidFriendlyStatus.UNKNOWN);
        isKidFriendlyEligible = webLink.isKidFriendlyEligible();

        assertFalse(isKidFriendlyEligible, "For porn in title - isKidFriendly() must return false ");

        // Test 3: adult in host -- false
        webLink = BookmarkManager.getInstance().createWeblink(2000 ,"Taming Tiger, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.adult.com", KidFriendlyStatus.UNKNOWN);
        isKidFriendlyEligible = webLink.isKidFriendlyEligible();

        assertFalse(isKidFriendlyEligible, "For adult in host - isKidFriendly() must return false ");

        // Test 4: adult in url, but not in host -- true
        webLink = BookmarkManager.getInstance().createWeblink(2000 ,"Taming Adult, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.javaworld.com", KidFriendlyStatus.UNKNOWN);
        isKidFriendlyEligible = webLink.isKidFriendlyEligible();

        assertTrue(isKidFriendlyEligible, "For adult in url but not in host - isKidFriendly() must return true ");

        // Test 5: adult in title only -- true
        webLink = BookmarkManager.getInstance().createWeblink(2000 ,"Taming adult, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.javaworld.com", KidFriendlyStatus.UNKNOWN);
        isKidFriendlyEligible = webLink.isKidFriendlyEligible();

        assertTrue(isKidFriendlyEligible, "For adult in title only - isKidFriendly() must return true ");
    }
}
