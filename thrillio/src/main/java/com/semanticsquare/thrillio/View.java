package com.semanticsquare.thrillio;

import java.util.List;

import com.semanticsquare.thrillio.constants.KidFriendlyStatus;
import com.semanticsquare.thrillio.constants.UserType;
import com.semanticsquare.thrillio.controllers.BookmarkController;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.partners.Shareable;

public class View {
	private View() {
	}

	public static void browse(User user, List<List<Bookmark>> bookmarks) {
		System.out.println("\n" + user.getEmail() + " is browsing items ...");

		for (List<Bookmark> bookmarkList : bookmarks) {
			for (Bookmark bookmark : bookmarkList) {
				boolean isBookmarked = getBookmarkDecision(bookmark);
				if (isBookmarked) {

					BookmarkController.getInstance().saveUserBookmark(user, bookmark);

					System.out.println("New item bookmarked -- " + bookmark);
				}
				// }

				if (user.getUserType().equals(UserType.EDITOR) || user.getUserType().equals(UserType.CHIEF_EDITOR)) {
					// Mark as kid-friendly
					if (bookmark.isKidFriendlyEligible()
							&& bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)) {
						KidFriendlyStatus kidFriendlyStatusDecision = getKidFriendlyStatusDecision();
						if (!kidFriendlyStatusDecision.equals(KidFriendlyStatus.UNKNOWN)) {
							BookmarkController.getInstance().setKidFriendlyStatus(user, kidFriendlyStatusDecision,
									bookmark);

						}
					}

					// Sharing !!!
					if (bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED)
							&& bookmark instanceof Shareable) {
						boolean isShared = getSharedDecision();
						if (isShared) {
							BookmarkController.getInstance().share(user, bookmark);
						}
					}

				}
			}
		}

	}

	// TODO: Below methods simulate user input. After IO, we take input via console.
	private static boolean getSharedDecision() {
		return Math.random() < 0.5;
	}

	private static KidFriendlyStatus getKidFriendlyStatusDecision() {
		float randomNumber = (float) Math.random();
		return randomNumber < 0.4 ? KidFriendlyStatus.APPROVED
				: (randomNumber >= 0.4 && randomNumber <= 0.8) ? KidFriendlyStatus.REJECTED : KidFriendlyStatus.UNKNOWN;
	}

	private static boolean getBookmarkDecision(Bookmark bookmark) {
		return Math.random() > 0.5;
	}
	/*
	 * public static void bookmark(User user, Bookmark[][] bookmarks) {
	 * StringBuilder stringBuilder = new StringBuilder("\n");
	 * stringBuilder.append(user.getEmail()) .append(" is bookmarking");
	 * System.out.println(stringBuilder.toString());
	 * 
	 * for (int i = 0; i < DataStore.USER_BOOKMARK_LIMIT; i++) { int typeOffset =
	 * (int)(Math.random() * DataStore.BOOKMARK_TYPES_COUNT); int bookmarkOffset =
	 * (int)(Math.random() * DataStore.BOOKMARK_COUNT_PER_TYPE);
	 * 
	 * Bookmark bookmark = bookmarks[typeOffset][bookmarkOffset];
	 * 
	 * BookmarkController.getInstance().saveUserBookmark(user, bookmark);
	 * 
	 * System.out.println(bookmark); }
	 * 
	 * }
	 */
}
