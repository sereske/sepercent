package ru.se.percent;

import java.io.Serializable;
import java.time.LocalDate;

public class Operation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private LocalDate date;
	private double sum;
	private Type type;
	
	public Operation () {}
	
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

	@Override
	public int hashCode() {
		int hash = date.hashCode();
		hash = 31 * hash + type.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Operation)) return false;
		if (obj == this) return true;
		Operation other = (Operation) obj;
		return this.date.equals(other.date) && this.type == other.type;
	}

	@Override
	public String toString() {
		return date + " " + type + " " + sum;
	}	
}
