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
		Loan loan = new Loan("Росбанк", "1", new ArrayList<>(), LocalDate.of(2018, 1, 1), LocalDate.of(2019, 12, 31), 26, new TreeMap<>());
		loan.addOperation(new Operation(LocalDate.of(2018,  1,  1), 100_000_000, Type.MAIN_RECEIVED));
		loan.addOperation(new Operation(LocalDate.of(2018,  6,  30), 20_000_000, Type.MAIN_PAID));
		loan.addOperation(new Operation(LocalDate.of(2018,  12, 31), 20_000_000, Type.MAIN_PAID));
		loan.addOperation(new Operation(LocalDate.of(2019,  12, 31), 60_000_000, Type.MAIN_PAID));
		loan.addRate(loan.getStartDate(), 10.0);
		loan.addRate(loan.getStartDate(), 15.0);
		
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
}
