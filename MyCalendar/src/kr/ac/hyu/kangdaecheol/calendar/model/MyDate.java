package kr.ac.hyu.kangdaecheol.calendar.model;

public class MyDate implements Comparable<MyDate> {

	int year;
	int month;
	int day;
	
	public MyDate() {
	}

	public MyDate(int year, int month, int day) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	
	@Override
	public int compareTo(MyDate day) {
		if (this.day > day.getDay()) {
			return 1;
		} else if (this.day == day.getDay()) {
			return 0;
		} else {
			return -1;
		}
	}
	
}
