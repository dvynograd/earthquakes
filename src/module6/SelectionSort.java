package module6;

public class SelectionSort<T extends Comparable<T>> {
	
	public void sort(T[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			int minIndex = i;
			for (int k = i + 1; k < array.length; k++) {
				if (array[k].compareTo(array[minIndex]) < 0) {
					minIndex = k;
				}
			}
			T tmp = array[i];
			array[i] = array[minIndex];
			array[minIndex] = tmp;
		}
	}
	
	public static void main (String[] args) {
		Integer[] array = new Integer[5];
		array[0] = 34;
		array[1] = 99;
		array[2] = 1;
		array[3] = 18;
		array[4] = 33;
		SelectionSort<Integer> sort = new SelectionSort<Integer>();
		sort.sort(array);
		for (int i = 0; i < array.length; i ++) {
			System.out.println("index " + i + ", value = " + array[i]);
		}	
	}

}
