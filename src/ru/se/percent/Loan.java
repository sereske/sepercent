package ru.se.percent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Loan implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private String bank;
	private String number;
	private List<Operation> operations = new ArrayList<>();
	private LocalDate startDate;
	private LocalDate endDate;
	private int checkDay;
	private Map<LocalDate, Double> rates = new TreeMap<>();
	
	public Loan() {
		id = UUID.randomUUID();
		//bank = "Банк";
		//number = "1";
		//startDate = LocalDate.now();
		//endDate = startDate.plusYears(1);
		//checkDay = 28;
		//operations.add(new Operation(startDate, 100_000_000, Type.MAIN_RECEIVED));
		//rates.put(startDate, 10.0);
	}
	
	public Loan(UUID id, String bank, String number, List<Operation> operations, LocalDate startDate,
			LocalDate endDate, int checkDay, Map<LocalDate, Double> rates) {
		super();
		this.id = id;
		this.bank = bank;
		this.number = number;
		this.operations = operations;
		this.startDate = startDate;
		this.endDate = endDate;
		this.checkDay = checkDay;
		this.rates = rates;
	}
	
	public Loan(String bank, String number, List<Operation> operations, LocalDate startDate,
			LocalDate endDate, int checkDay, Map<LocalDate, Double> rates) {
		this(UUID.randomUUID(), bank, number, operations, startDate, endDate, checkDay, rates);
	}
	
	public Loan(Loan loan) {
		this(loan.id, loan.bank, loan.number, loan.operations, loan.startDate, loan.endDate, loan.checkDay, loan.rates);
	}

	public UUID getId() {
		return id;
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
	
	@JsonIgnore
	public double getInitialSum() {
		return getCurrentDebt(startDate);
	}
	
	public double getPercent(LocalDate startDate, LocalDate endDate) {
		double currentPercent = 0.0;
		double currentDebt = 0.0;
		double currentRate = 0.0;
		LocalDate currentDate = startDate;
		while (!currentDate.equals(endDate.plusDays(1))) {
			currentDebt = getCurrentDebt(currentDate);
			currentRate = getCurrentRate(currentDate);
			currentPercent += round(currentDebt * currentRate / currentDate.lengthOfYear(), 2);
			currentDate = currentDate.plusDays(1);
		}
		return currentPercent;
	}
	
	public double getPercentPaid(LocalDate date, boolean isDateDivided) {
		if (date.getDayOfMonth() == 1 && date.lengthOfMonth() > checkDay && isDateDivided) {
			return 0.0;
		} else {
			LocalDate date1 = getCheckDate(date.minusMonths(1)).plusDays(1);
			LocalDate date2 = getCheckDate(date);
			return getPercent(date1, date2);
		}
	}
	
	public double getSaldo(LocalDate endDate, boolean isDateDivided) {
		if (endDate.getDayOfMonth() == endDate.lengthOfMonth()) {
			return endDate.equals(this.endDate) ? 0 : getPercent(startDate, endDate) - getPercent(startDate, getCheckDate(endDate));
		} else {
			return 0.0;
		}
	}
	
	public LocalDate getCheckDate(LocalDate currentDate) {
		int checkDay = getCheckDay();
		if (getCheckDay() > 28 && currentDate.getMonth() == Month.FEBRUARY) {
			checkDay = currentDate.isLeapYear() ? 29 : 28;
		} else if (getCheckDay() > 30) {
			checkDay = currentDate.lengthOfMonth();
		}
		LocalDate checkDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), checkDay);
		return checkDate;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
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
	/*
	public double getTotalPercent(LocalDate date) {
		double totalPercent = 0.0;
		for (Operation op : operations) {
			if (op.getDate().isEqual(date) || op.getDate().isBefore(date)) {
				if (op.getType() == Type.PERCENT_PAID) {
					totalPercent -= op.getSum();
				} else if (op.getType() == Type.PERCENT_RECEIVED) {
					totalPercent += op.getSum();
				}
			}
		}
		return totalPercent;
	}
	*/
	public double getCurrentRate(LocalDate date) {
		LocalDate currentDate = date;
		//while (!rates.isEmpty() && !rates.containsKey(currentDate)) {
		if (date.isBefore(startDate) || date.isAfter(endDate)) {
			return 0.0;
		}
		while (!rates.isEmpty() && !rates.containsKey(currentDate)) {
			currentDate = currentDate.minusDays(1);
		}
		return rates.get(currentDate) / 100;
	}
	
	@JsonIgnore
	public List<LocalDate> getDatesDivided() {
		List<LocalDate> dates = new ArrayList<>();
		LocalDate currentDate = startDate;
		while (!currentDate.equals(getEndDate().plusDays(1))) {
			/*
			int checkDay = getCheckDay();
			if (getCheckDay() > 28 && currentDate.getMonth() == Month.FEBRUARY) {
				checkDay = currentDate.isLeapYear() ? 29 : 28;
			} else if (getCheckDay() > 30) {
				checkDay = currentDate.lengthOfMonth();
			}
			LocalDate checkDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), checkDay);
			*/
			LocalDate checkDate = getCheckDate(currentDate);
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
	
	@JsonIgnore
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
	
	@JsonIgnore
	public String getFileName() {
		return bank + "_" + number;
	}

	@Override
	public String toString() {
		//return "Loan [bank=" + bank + ", number=" + number + ", operations=" + operations + ", startDate=" + startDate
		//		+ ", endDate=" + endDate + ", checkDay=" + checkDay + ", rates=" + rates + "]";
		return bank + " договор №" + number + " от " + startDate;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Loan)) return false;
		Loan other = (Loan) obj;
		return this.id == other.id;
	}
}
