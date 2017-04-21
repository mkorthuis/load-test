package com.candescent.selenium.util;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.candescent.selenium.Constants;

public class Output {

	private PrintWriter writer;

	private static Output output;

	private Output() {
		try {
			writer = new PrintWriter(Constants.OUTPUT_LOCATION, "UTF-8");
		} catch (Exception e) {
			System.out.println("Cannot Initialize Output.  Output will not be stored.");
		}
	}

	public void write(String[] items) {
		StringBuilder sb = new StringBuilder();
		Boolean isFirst = Boolean.TRUE;
		for (String item : items) {
			if (!isFirst) {
				sb.append(",");
			}
			sb.append(item);
			isFirst = Boolean.FALSE;
		}
		System.out.println(sb.toString());
		writer.println(sb.toString());
	}

	public void write(Integer index, String line) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		System.out.println(dateFormat.format(cal.getTime()) + ", " + index + ", " + line.toString());
		writer.println(dateFormat.format(cal.getTime()) + ", " + index + ", " + line.toString());
	}

	public void writeLine(String line) {
		System.out.println(line.toString());
		writer.println(line.toString());
	}

	public void close() {
		writer.close();
	}

	public static Output getInstance() {
		if (output == null) {
			output = new Output();
		}
		return output;
	}

}
