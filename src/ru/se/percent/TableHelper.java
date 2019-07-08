package ru.se.percent;

import java.time.LocalDate;
import java.time.Month;
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
		reportCols.add("Основной долг");
		reportCols.add("Начислено процентов");
		reportCols.add("Уплачено процентов");
		reportCols.add("Проценты нарастающие");
		reportCols.add("Сальдо");
		return reportCols.toArray();
	}
	
	public static Object[][] getReportTableData(Loan loan, boolean isDateDivided) {
		Object[][] reportData = null;
		if (loan != null) { // && loan.getInitialSum() != 0) {
			List<LocalDate> dates = isDateDivided ? loan.getDatesDivided() : loan.getDates();
			try {
				if (loan.getInitialSum() != 0) {
					reportData = new Object[dates.size() / 2][9];
					int i = 0;
					for (int k = 0; k < dates.size() - 1; k += 2) {
						LocalDate startDate = dates.get(k);
						LocalDate endDate = dates.get(k + 1);
						double percent = loan.getPercent(startDate, endDate);
						double paid = loan.getPercentPaid(startDate, isDateDivided);
						/*
						if (startDate.getDayOfMonth() == 1 && startDate.lengthOfMonth() >= loan.getCheckDay()) {
							//loan.getPercentPaid(startDate);
							paid = 0.0;
						} else {
							LocalDate date1 = loan.getCheckDate(startDate.minusMonths(1)).plusDays(1);
							LocalDate date2 = loan.getCheckDate(startDate);
							paid = loan.getPercent(date1, date2);
						}
						*/
						double rate = loan.getCurrentRate(endDate);
						long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
						double debt = loan.getCurrentDebt(endDate);
						//double percentTotal = loan.getTotalPercent(endDate) + loan.getPercent(loan.getStartDate(), endDate);
						double percentTotal = loan.getPercent(loan.getStartDate(), endDate);
						double saldo = loan.getSaldo(endDate, isDateDivided);
						reportData[i][0] = startDate;
						reportData[i][1] = endDate;
						reportData[i][2] = daysBetween;
						reportData[i][3] = rate;
						reportData[i][4] = debt;
						reportData[i][5] = percent;
						reportData[i][6] = paid;
						reportData[i][7] = percentTotal;
						reportData[i][8] = saldo;
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
	
	public static Object[][] getPercentTableData(List<Loan> loans, int year, boolean received, boolean isDateDivided) {
		Object[][] percentData = new Object[loans.size() + 1][19];
		for (int i = 0; i < loans.size(); i++) {
			Loan loan = loans.get(i);
			percentData[i][0] = loan.getBank();
			percentData[i][1] = loan.getNumber();
			for (int j = 2; j <= 18; j++) {
				LocalDate startDate;
				LocalDate endDate;
				/*
				 * j = 2 - Январь 1
				 * j = 3 - Февраль 2
				 * j = 4 - Март 3
				 * j = 5 - 1 квартал 
				 * j = 6 - Апрель 4
				 * j = 7 - Май 5
				 * j = 8 - Июнь 6
				 * j = 9 - 2 квартал
				 * j = 10 - Июль 7
				 * j = 11 - Август 8
				 * j = 12 - Сентябрь 9
				 * j = 13 - 3 квартал
				 * j = 14 - Октябрь 10
				 * j = 15 - Ноябрь 11
				 * j = 16 - Декабрь 12
				 * j = 17 - 4 квартал
				 * j = 18 - Год
				 */
				if (j <= 4) {
					Month month = Month.of(j - 1);
					startDate = LocalDate.of(year, month, 1);
					int lastDay = startDate.lengthOfMonth();
					endDate = LocalDate.of(year, month, lastDay);
				} else if (j == 5) {
					startDate = LocalDate.of(year, 1, 1);
					endDate = LocalDate.of(year, 3, 31);
				} else if (j <= 8) {
					Month month = Month.of(j - 2);
					startDate = LocalDate.of(year, month, 1);
					int lastDay = startDate.lengthOfMonth();
					endDate = LocalDate.of(year, month, lastDay);
				} else if (j == 9) {
					startDate = LocalDate.of(year, 4, 1);
					endDate = LocalDate.of(year, 6, 30);
				} else if (j <= 12) {
					Month month = Month.of(j - 3);
					startDate = LocalDate.of(year, month, 1);
					int lastDay = startDate.lengthOfMonth();
					endDate = LocalDate.of(year, month, lastDay);
				} else if (j == 13) {
					startDate = LocalDate.of(year, 7, 1);
					endDate = LocalDate.of(year, 9, 30);
				} else if (j <= 16) {
					Month month = Month.of(j - 4);
					startDate = LocalDate.of(year, month, 1);
					int lastDay = startDate.lengthOfMonth();
					endDate = LocalDate.of(year, month, lastDay);
				} else if (j == 17) {
					startDate = LocalDate.of(year, 10, 1);
					endDate = LocalDate.of(year, 12, 31);
				} else {
					startDate = LocalDate.of(year,  1,  1);
					endDate = LocalDate.of(year, 12, 31);
				}
				if (received) {
					percentData[i][j] = loan.getPercent(startDate, endDate);
				} else {
					percentData[i][j] = loan.getPercentPaid(endDate, isDateDivided);
				}
			}
		}
		int totalRow = loans.size();
		percentData[totalRow][0] = "Итого";
		percentData[totalRow][1] = "";
		for (int j = 2; j <= 18; j++) {
			double total = 0.0;
			for (int i = 0; i < loans.size(); i++) {
				total += Double.parseDouble(percentData[i][j].toString());
			}
			percentData[totalRow][j] = total;
		}
		return percentData;
	}
	
	public static Object[] getPercentTableCols() {
		List<String> percentCols = new ArrayList<>();
		percentCols.add("Банк");
		percentCols.add("Договор");
		percentCols.add("Январь");
		percentCols.add("Февраль");
		percentCols.add("Март");
		percentCols.add("1 Квартал");
		percentCols.add("Апрель");
		percentCols.add("Май");
		percentCols.add("Июнь");
		percentCols.add("2 Квартал");
		percentCols.add("Июль");
		percentCols.add("Август");
		percentCols.add("Сентябрь");
		percentCols.add("3 Квартал");
		percentCols.add("Октябрь");
		percentCols.add("Ноябрь");
		percentCols.add("Декабрь");
		percentCols.add("4 Квартал");
		percentCols.add("Итого");
		return percentCols.toArray();
	}
}
