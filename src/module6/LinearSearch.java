package module6;

import demos.Airport;

public class LinearSearch {
	public static String findAirportCode(String toFind, Airport[] airports) {
		int index = 0;
		while (index < airports.length) {
			if (airports[index].getCity().equals(toFind)) {
				return airports[index].getCode3();
			}
			index++;
		}
		return null;
	}

	public static void main(String[] args) {
		Airport[] airportsArray = new Airport[3];
		airportsArray[0] = new Airport(1, "Goroka", "Goroka", "Papua New Guinea", "GKA", "AYGA", -6.081689, 145.391881,
				5282, 10, 'U', "Pacific/Port_Moresby");

		airportsArray[1] = new Airport(2, "Madang", "Madang", "Papua New Guinea", "MAG", "AYMD", -5.207083, 145.7887,
				20, 10, 'U', "Pacific/Port_Moresby");
		airportsArray[2] = new Airport(3, "Mount Hagen", "Mount Hagen", "Papua New Guinea", "HGU", "AYMH", -5.826789,
				144.295861, 5388, 10, 'U', "Pacific/Port_Moresby");
		
		
		System.out.println(findAirportCode("Mount Hagen 1", airportsArray));
	}

}
