package ru.se.percent;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Main {
	
	public static void main(String[] args) {
		Loan loan = getTestLoan();
				
	}
	
	public static Loan getTestLoan() {
		Loan loan = new Loan("Росбанк", "1", new ArrayList<>(), LocalDate.of(2018, 1, 1), LocalDate.of(2019, 12, 31), 29, new TreeMap<>());
		loan.addOperation(new Operation(LocalDate.of(2018,  1,  1), 100_000_000, Type.MAIN_RECEIVED));
		loan.addOperation(new Operation(LocalDate.of(2018,  6,  30), 20_000_000, Type.MAIN_PAID));
		loan.addOperation(new Operation(LocalDate.of(2018,  12, 31), 20_000_000, Type.MAIN_PAID));
		loan.addOperation(new Operation(LocalDate.of(2019,  12, 31), 60_000_000, Type.MAIN_PAID));
		loan.addRate(loan.getStartDate(), 10.0);
		
		List<LocalDate> dates = new ArrayList<>();
		LocalDate currentDate = loan.getStartDate();
		while (!currentDate.equals(loan.getEndDate())) {
			int checkDay = loan.getCheckDay();
			if (loan.getCheckDay() > 28 && currentDate.getMonth() == Month.FEBRUARY) {
				checkDay = currentDate.isLeapYear() ? 29 : 28;
			}
			else if (loan.getCheckDay() > 30) {
				checkDay = 31;
			}
			LocalDate checkDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), checkDay);
			LocalDate checkDatePlusOne = checkDate.plusDays(1);
			if (
					currentDate.equals(loan.getStartDate()) || 
					currentDate.equals(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), checkDay))|| 
					currentDate.equals(checkDatePlusOne) || 
					currentDate.equals(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1)) || 
					currentDate.equals(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), currentDate.lengthOfMonth()))
					) {
				dates.add(currentDate);
			}
			currentDate = currentDate.plusDays(1);
		}
		
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
		
		//System.out.println(dates.size());
		return loan;
	}
}
