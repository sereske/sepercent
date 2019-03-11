package ru.se.percent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TableHelper {
	
	public static Object[] getReportTableCols() {
		List<String> reportCols = new ArrayList<>();
		reportCols.add("Начало периода");
		reportCols.add("Конец периода");
		reportCols.add("Кол-во дней");
		reportCols.add("Ставка");
		reportCols.add("Начислено процентов");
		reportCols.add("Основной долг");
		reportCols.add("Проценты нарастающие");
		return reportCols.toArray();
	}
	
	public static Object[][] getReportTableData(Loan loan, boolean isDateDivided) {
		Object[][] reportData = null;
		if (loan != null) { // && loan.getInitialSum() != 0) {
			List<LocalDate> dates = isDateDivided ? loan.getDatesDivided() : loan.getDates();
			try {
				if (loan.getInitialSum() != 0) {
					reportData = new Object[dates.size() / 2][7];
					int i = 0;
					for (int k = 0; k < dates.size() - 1; k += 2) {
						LocalDate startDate = dates.get(k);
						LocalDate endDate = dates.get(k + 1);
						double percent = loan.getPercent(startDate, endDate);
						double rate = loan.getCurrentRate(endDate);
						long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
						double debt = loan.getCurrentDebt(endDate);
						double percentTotal = loan.getTotalPercent(endDate) + loan.getPercent(loan.getStartDate(), endDate);
						reportData[i][0] = startDate;
						reportData[i][1] = endDate;
						reportData[i][2] = daysBetween;
						reportData[i][3] = rate;
						reportData[i][4] = percent;
						reportData[i][5] = debt;
						reportData[i][6] = percentTotal;
						i++;
					}
				} else {
					reportData = new Object[0][6];
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reportData;
	}
	
	public static Object[] getOperationsTableCols() {
		List<String> operationsCols = new ArrayList<>();
		operationsCols.add("Дата");
		operationsCols.add("Тип");
		operationsCols.add("Сумма");
		return operationsCols.toArray();
	}
	
	public static Object[][] getOperationsTableData(Loan loan) {
		Object[][] operationsData = null;
		if (loan != null) {
			operationsData = new Object[loan.getOperations().size()][3];
			int i = 0;
			List<Operation> operations = loan.getOperations();
			Collections.sort(operations);
			for (Operation op : operations) {
				operationsData[i][0] = op.getDate();
				operationsData[i][1] = op.getType();
				operationsData[i][2] = op.getSum();
				i++;
			}
		}
		return operationsData;
	}
	
	public static Object[][] getRateTableData(Loan loan) {
		Object[][] rateData = null;
		if (loan != null) {
			Map<LocalDate, Double> newRates = new TreeMap<>();
			for (Map.Entry<LocalDate, Double> pair : loan.getRates().entrySet()) {
				newRates.put(pair.getKey(), pair.getValue());
			}
			rateData = new Object[newRates.entrySet().size()][2];
			int i = 0;
			for (Map.Entry<LocalDate, Double> pair : newRates.entrySet()) {
				rateData[i][0] = pair.getKey();
				rateData[i][1] = pair.getValue();
				i++;
			}
		} else {
			
		}
		return rateData;
	}
	
	public static Object[] getRateTableCols() {
		List<String> rateCols = new ArrayList<>();
		rateCols.add("Дата");
		rateCols.add("Процентная ставка");
		return rateCols.toArray();
	}
	
	public static Object[][] getPropertiesData(Loan loan) {
		Object[][] propertiesData = null;
		if (loan != null) {
			propertiesData = new Object[5][2];
			propertiesData[0][0] = "Банк";
			propertiesData[0][1] = loan.getBank();
			propertiesData[1][0] = "№ договора";
			propertiesData[1][1] = loan.getNumber();
			propertiesData[2][0] = "Дата начала";
			propertiesData[2][1] = loan.getStartDate();
			propertiesData[3][0] = "Дата окончания";
			propertiesData[3][1] = loan.getEndDate();
			propertiesData[4][0] = "День расчета";
			propertiesData[4][1] = loan.getCheckDay();
		}
		return propertiesData;
	}
}
