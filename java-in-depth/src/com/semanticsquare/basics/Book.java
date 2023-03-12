public class Book {
	public String title;

	public Book(String title) {
		this.title = title;
	}

	public Book() {
	}

	@Override
	public String toString() {
		return "Book [title=" + title + "]";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
