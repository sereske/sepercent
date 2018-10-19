package ru.se.percent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Loan {
	private String bank;
	private String number;
	private List<Operation> operations = new ArrayList<>();
	private LocalDate startDate;
	private LocalDate endDate;
	private int checkDay;
	private Map<LocalDate, Double> rates = new TreeMap<>();
	
	public Loan(String bank, String number, List<Operation> operations, LocalDate startDate,
			LocalDate endDate, int checkDay, Map<LocalDate, Double> rates) {
		super();
		this.bank = bank;
		this.number = number;
		this.operations = operations;
		this.startDate = startDate;
		this.endDate = endDate;
		this.checkDay = checkDay;
		this.rates = rates;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
	
	public void addOperation(Operation op) {
		this.operations.add(op);
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getCheckDay() {
		return checkDay;
	}

	public void setCheckDay(int checkDay) {
		this.checkDay = checkDay;
	}

	public Map<LocalDate, Double> getRates() {
		return rates;
	}

	public void setRates(Map<LocalDate, Double> rates) {
		this.rates = rates;
	}
	
	public void addRate(LocalDate date, double rate) {
		this.rates.put(date, rate);
	}
	
	public double getPercent(LocalDate startDate, LocalDate endDate) {
		double currentPercent = 0.0;
		double currentDebt = 0.0;
		double currentRate = 0.0;
		LocalDate currentDate = startDate;
		while (!currentDate.equals(endDate)) {
			currentDebt = getCurrentDebt(currentDate);
			currentRate = getCurrentRate(currentDate);
			currentPercent += currentDebt * currentRate / currentDate.lengthOfYear();
			currentDate = currentDate.plusDays(1);
		}
		return currentPercent;
	}
	
	public double getCurrentDebt(LocalDate date) {
		double currentDebt = 0.0;
		for (Operation op : operations) {
			if (op.getDate().isBefore(date)) {
				if (op.getType() == Type.MAIN_PAID) {
					currentDebt -= op.getSum();
				} else if (op.getType() == Type.MAIN_RECEIVED) {
					currentDebt += op.getSum();
				}
			}
		}
		return currentDebt;
	}
	
	public double getCurrentRate(LocalDate date) {
		LocalDate currentDate = date;
		while (!rates.containsKey(currentDate)) {
			currentDate = currentDate.minusDays(1);
		}
		return rates.get(currentDate);
	}
}
