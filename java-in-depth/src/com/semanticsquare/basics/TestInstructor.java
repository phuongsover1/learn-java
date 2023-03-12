public class TestInstructor {
	public static void main(String[] args) {
		Instructor phuong = new Instructor(2, "Phuong", "Gau", "IT",
				new Book[] { new Book("Java 1"), new Book("JavaScript"), new Book("CSS") });
		System.out.println(phuong);
	}
}
