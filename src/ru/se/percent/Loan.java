package ru.se.percent;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Loan implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String bank;
	private String number;
	private List<Operation> operations = new ArrayList<>();
	private LocalDate startDate;
	private LocalDate endDate;
	private int checkDay;
	private Map<LocalDate, Double> rates = new TreeMap<>();
	
	public Loan() {}
	
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
	
	public void removeOperation(Operation op) {
		this.operations.remove(op);
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
	
	public void removeRate(LocalDate date) {
		this.rates.remove(date);
	}
	
	public double getPercent(LocalDate startDate, LocalDate endDate) {
		double currentPercent = 0.0;
		double currentDebt = 0.0;
		double currentRate = 0.0;
		LocalDate currentDate = startDate;
		while (!currentDate.equals(endDate.plusDays(1))) {
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
			if (op.getDate().isEqual(date) || op.getDate().isBefore(date)) {
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
		return rates.get(currentDate) / 100;
	}
	
	public List<LocalDate> getDatesDivided() {
		List<LocalDate> dates = new ArrayList<>();
		LocalDate currentDate = startDate;
		while (!currentDate.equals(getEndDate().plusDays(1))) {
			int checkDay = getCheckDay();
			if (getCheckDay() > 28 && currentDate.getMonth() == Month.FEBRUARY) {
				checkDay = currentDate.isLeapYear() ? 29 : 28;
			} else if (getCheckDay() > 30) {
				checkDay = currentDate.lengthOfMonth();
			}
			LocalDate checkDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), checkDay);
			if (
					currentDate.equals(getStartDate()) || 
					currentDate.equals(getEndDate()) ||
					currentDate.equals(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)) || 
					currentDate.equals(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), currentDate.lengthOfMonth()))
					) {
				dates.add(currentDate);
			}
			if (currentDate.equals(checkDate) && !currentDate.equals(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), currentDate.lengthOfMonth()))) {
				dates.add(checkDate);
				int monthLength = currentDate.lengthOfMonth();
				if (monthLength - checkDay >= 1) {
					dates.add(checkDate.plusDays(1));
				}
			}
			currentDate = currentDate.plusDays(1);
		}
		return dates;
	}
	
	public List<LocalDate> getDates() {
		List<LocalDate> dates = new ArrayList<>();
		LocalDate currentDate = startDate;
		while (!currentDate.equals(getEndDate().plusDays(1))) {
			if (
					currentDate.equals(getStartDate()) || 
					currentDate.equals(getEndDate()) ||
					currentDate.equals(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)) || 
					currentDate.equals(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), currentDate.lengthOfMonth()))
					) {
				dates.add(currentDate);
			}
			currentDate = currentDate.plusDays(1);
		}
		return dates;
	}
	
	public static Loan getTestLoan() {
		Loan loan = new Loan("Росбанк", "1", new ArrayList<>(), LocalDate.of(2018, 1, 1), LocalDate.of(2019, 12, 31), 26, new TreeMap<>());
		loan.addOperation(new Operation(LocalDate.of(2018,  1,  1), 100_000_000, Type.MAIN_RECEIVED));
		loan.addOperation(new Operation(LocalDate.of(2018,  6,  30), 20_000_000, Type.MAIN_PAID));
		loan.addOperation(new Operation(LocalDate.of(2018,  12, 31), 20_000_000, Type.MAIN_PAID));
		loan.addOperation(new Operation(LocalDate.of(2019,  12, 31), 60_000_000, Type.MAIN_PAID));
		loan.addRate(loan.getStartDate(), 10.0);
		loan.addRate(LocalDate.of(2018, 6, 1), 15.0);
		
		/*
		List<LocalDate> dates = loan.getDates();
		
		LocalDate startDate = null;
		LocalDate endDate = null;
		for (int i = 0; i < dates.size() - 1; i+=2) {
			startDate = dates.get(i);
			endDate = dates.get(i + 1);
			double percent = loan.getPercent(startDate, endDate);
			System.out.println("Percent between " + startDate + " and " + endDate + " is " + percent);
		}
		
		for (int i = 0; i < dates.size(); i++) {
			System.out.println(dates.get(i));
		}
		*/
		
		return loan;
	}

	@Override
	public String toString() {
		return "Loan [bank=" + bank + ", number=" + number + ", operations=" + operations + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", checkDay=" + checkDay + ", rates=" + rates + "]";
	}
}
