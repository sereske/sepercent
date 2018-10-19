package ru.se.percent;

import java.time.LocalDate;

public class Operation {
	private LocalDate date;
	private double sum;
	private Type type;
	
	public Operation(LocalDate date, double sum, Type type) {
		super();
		this.date = date;
		this.sum = sum;
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}	
}
