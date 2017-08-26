package rmi.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputValidation {

	private static Scanner input = new Scanner(System.in);

	public static int getIntegerInput(String prompt) {
		int value = -1;
		while (value < 0) {
			try {
				System.out.print(prompt);
				value = input.nextInt();
				input.nextLine();
			} catch (InputMismatchException ime) {
				System.err.println("Incorrect entry. Please input only a positive integer.");
				input.nextLine();
			}
		}
		return value;
	}

	public static Date getDate() {
		Date date = null;
		int value = -1;
		System.out.println("Date [dd-MM-yyyy hh:mm:ss]");
		while (value < 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String dateString = input.nextLine();
			try {
				date = formatter.parse(dateString);
				value = 1;
			} catch (ParseException e) {
				System.out.println("Date format must be: [dd-M-yyyy hh:mm:ss]");
			}
		}
		return date;

	}
	
	public static String getStringInput(String inputInfo) {
		System.out.println(inputInfo);
		if (inputInfo.equals("Your email: "))
			return input.nextLine().toLowerCase();
		else
			return input.nextLine();
	}


}
