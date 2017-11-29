package module6;

import demos.Airport;

public class BinarySearch {

	public static String findAirportCodeBs(String toFind, Airport[] airports) {
		int low = 0;
		int high = 0;
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			mid = low + ((high - low) / 2);
			int compare = airports[mid].getName().compareTo(toFind);
			if (compare < 0) {
				low = mid + 1;
			} else if (compare > 0) {
				high = mid - 1;
			} else {
				return airports[mid].getCode3();
			}
		}
		return null;
	}

}
