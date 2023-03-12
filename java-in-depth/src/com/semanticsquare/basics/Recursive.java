public class Recursive {
	public static int factorial(int n) {
		if (n == 0)
			return 1;
		return n * factorial(n - 1);
	}

	public static void main(String[] args) {
		// System.out.println(factorial(6));
		System.out.println(binarySearch(new int[] { 1, 3, 5, 7, 9 }, 0, 4, 12));
	}

	public static int binarySearch(int[] a, int begin, int end, int key) {
		if (begin > end) {
			return -1;
		}
		if (begin == end) {
			if (a[begin] == key) {
				return begin;
			}
		}
		int mid = (begin + end) / 2;

		if (a[mid] == key) {
			return mid;
		} else if (a[mid] < key) {
			return binarySearch(a, begin + 1, end, key);
		} else
			return binarySearch(a, begin, mid - 1, key);
	}
}
