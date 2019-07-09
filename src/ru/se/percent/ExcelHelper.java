package ru.se.percent;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {
	
	private static final String EXCEL_FOLDER = "excel";
	
	private static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
	private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private static ExcelHelper instance;
	
	private ExcelHelper() {}
	
	public static ExcelHelper getInstance() {
		if (instance == null) {
			instance = new ExcelHelper();
		}
		if (!Files.exists(Paths.get(EXCEL_FOLDER))) {
			try {
				Files.createDirectory(Paths.get(EXCEL_FOLDER));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public void exportReportFile(Loan loan, boolean isDateDivided) {
		//Object[][] rateData = TableHelper.getRateTableData(loan);
		Object[][] reportData = TableHelper.getReportTableData(loan, isDateDivided);
		Object[][] operationsData = TableHelper.getOperationsTableData(loan);
		Object[][] rateData = TableHelper.getRateTableData(loan);
		Object[][] propertiesData = TableHelper.getPropertiesData(loan);
		if (operationsData != null || rateData != null) {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Процентщица");
			
			int rowNum = 0;
			// Area Properties
			//Object[] colsProperties = TableHelper.getPropertiesData(loan);
			for (Object[] data : propertiesData) {
	            Row row = sheet.createRow(rowNum++);
	            int colNum = 0;
	            for (Object field : data) {
	                Cell cell = row.createCell(colNum++);
	                if (field instanceof String) {
	                    cell.setCellValue((String) field);
	                } else if (field instanceof Integer) {
	                    cell.setCellValue((Integer) field);
	                } else if (field instanceof LocalDate) {
	                	LocalDate localDate = (LocalDate) field;
	                	cell.setCellValue(localDate.format(DATE_FORMATTER));
	                } else if (field instanceof Long) {
	                	cell.setCellValue((Long) field);
	                } else if (field instanceof Double) {
	                	cell.setCellValue((Double) field);
	                } else if (field instanceof Type) {
	                	cell.setCellValue(((Type) field).toString());
	                }
	            }
	        }
			
			rowNum++;
			
			// Area Rate
			Object[] colsRate = TableHelper.getRateTableCols();
			Row rateHeadRow = sheet.createRow(rowNum);
			for (int i = 0; i < 2; i++) {
				Cell cell = rateHeadRow.createCell(i);
				cell.setCellValue((String) colsRate[i]); 
			}
			
			rowNum++;
			
			for (Object[] data : rateData) {
	            Row row = sheet.createRow(rowNum++);
	            int colNum = 0;
	            for (Object field : data) {
	                Cell cell = row.createCell(colNum++);
	                if (field instanceof String) {
	                    cell.setCellValue((String) field);
	                } else if (field instanceof Integer) {
	                    cell.setCellValue((Integer) field);
	                } else if (field instanceof LocalDate) {
	                	LocalDate localDate = (LocalDate) field;
	                	cell.setCellValue(localDate.format(DATE_FORMATTER));
	                } else if (field instanceof Long) {
	                	cell.setCellValue((Long) field);
	                } else if (field instanceof Double) {
	                	cell.setCellValue((Double) field);
	                } else if (field instanceof Type) {
	                	cell.setCellValue(((Type) field).toString());
	                }
	            }
	        }
			
			rowNum++;
			
			// Area Operations
			Object[] colsOperations = TableHelper.getOperationsTableCols();
			Row operationsHeadRow = sheet.createRow(rowNum);
			for (int i = 0; i < 3; i++) {
				Cell cell = operationsHeadRow.createCell(i);
				cell.setCellValue((String) colsOperations[i]); 
			}
			
			rowNum++;
			
			for (Object[] data : operationsData) {
	            Row row = sheet.createRow(rowNum++);
	            int colNum = 0;
	            for (Object field : data) {
	                Cell cell = row.createCell(colNum++);
	                if (field instanceof String) {
	                    cell.setCellValue((String) field);
	                } else if (field instanceof Integer) {
	                    cell.setCellValue((Integer) field);
	                } else if (field instanceof LocalDate) {
	                	LocalDate localDate = (LocalDate) field;
	                	cell.setCellValue(localDate.format(DATE_FORMATTER));
	                } else if (field instanceof Long) {
	                	cell.setCellValue((Long) field);
	                } else if (field instanceof Double) {
	                	cell.setCellValue((Double) field);
	                } else if (field instanceof Type) {
	                	cell.setCellValue(((Type) field).toString());
	                }
	            }
	        }
			
			rowNum++;
			
			// Area Report
			if (reportData != null) {
				Object[] colsReport = TableHelper.getReportTableCols();
				Row reportHeadRow = sheet.createRow(rowNum);
				for (int i = 0; i < colsReport.length; i++) {
					Cell cell = reportHeadRow.createCell(i);
					cell.setCellValue((String) colsReport[i]); 
				}
				
		        rowNum++;
		        
		        for (Object[] data : reportData) {
		            Row row = sheet.createRow(rowNum++);
		            int colNum = 0;
		            for (Object field : data) {
		                Cell cell = row.createCell(colNum++);
		                if (field instanceof String) {
		                    cell.setCellValue((String) field);
		                } else if (field instanceof Integer) {
		                    cell.setCellValue((Integer) field);
		                } else if (field instanceof LocalDate) {
		                	LocalDate localDate = (LocalDate) field;
		                	cell.setCellValue(localDate.format(DATE_FORMATTER));
		                } else if (field instanceof Long) {
		                	cell.setCellValue((Long) field);
		                } else if (field instanceof Double) {
		                	cell.setCellValue((Double) field);
		                }
		            }
		        }
			}
			
			String formatDateTime = LocalDateTime.now().format(DATETIME_FORMATTER);       
	        String fileName = loan.getFileName() + "_" + formatDateTime;
	        try (FileOutputStream outputStream = new FileOutputStream(EXCEL_FOLDER + "\\" + fileName + ".xlsx")) {
	            workbook.write(outputStream);
	            workbook.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	public void exportExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Datatypes in Java");
        Object[][] datatypes = {
                {"Datatype", "Type", "Size(in bytes)"},
                {"int", "Primitive", 2},
                {"float", "Primitive", 4},
                {"double", "Primitive", 8},
                {"char", "Primitive", 1},
                {"String", "Non-Primitive", "No fixed size"}
        };

        int rowNum = 0;
        System.out.println("Creating excel");

        for (Object[] datatype : datatypes) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
        	workbook.write(baos);
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
        	try {
        		baos.close();
        		workbook.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
	}
	
	public void exportCommonReportFile(List<Loan> loans, int year, boolean isDateDivided) {
		Object[] percentCols = TableHelper.getPercentTableCols();
		Object[][]  receivedPercentData = TableHelper.getPercentTableData(loans, year, true, false);
		Object[][]  paidPercentData = TableHelper.getPercentTableData(loans, year, false, false);
		if (receivedPercentData != null && paidPercentData != null) {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Общий отчет");
			
			CellStyle greenBackground = workbook.createCellStyle();
			//greenBackground.setFillPattern(FillPatternType.BIG_SPOTS);  
			greenBackground.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			greenBackground.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			CellStyle orangeBackground = workbook.createCellStyle();
			//orangeBackground.setFillPattern(FillPatternType.BIG_SPOTS);
			orangeBackground.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			orangeBackground.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			int rowNum = 1;
			int headerColNum = 8;
			Row headerRow = sheet.createRow(rowNum++);
			Cell headerCell = headerRow.createCell(headerColNum);
			headerCell.setCellValue("Проценты Сахаэнерго, начисленные в " + year + " году.");
			rowNum++;
			
			int i = 0;
			Row monthRow = sheet.createRow(rowNum);
			for (Object data : percentCols) {
				Cell cell = monthRow.createCell(i);
				if (data instanceof String) {
            		cell.setCellValue((String) data);
            	}
	            i++;
			}
			rowNum++;
			
			for (Object[] data : receivedPercentData) {
				Row row = sheet.createRow(rowNum++);
	            int colNum = 0;
	            for (Object field : data) {
	            	Cell cell = row.createCell(colNum++);
	            	if (field instanceof String) {
	            		cell.setCellValue((String) field);
	            	} else if (field instanceof Double) {
	            		cell.setCellValue((Double) field);
	            	} else if (field instanceof Integer) {
	            		cell.setCellValue((Integer) field);
	            	}
	            	if (colNum == 6 || colNum == 10 || colNum == 14 || colNum == 18) {
	            		cell.setCellStyle(orangeBackground);
	            	}
	            	if (colNum == 19) {
	            		cell.setCellStyle(greenBackground);
	            	}
	            }
			}
			
			rowNum++;
			headerRow = sheet.createRow(rowNum++);
			headerCell = headerRow.createCell(headerColNum);
			headerCell.setCellValue("Проценты Сахаэнерго, уплаченные в " + year + " году.");
			rowNum++;
			
			i = 0;
			monthRow = sheet.createRow(rowNum);
			for (Object data : percentCols) {
				Cell cell = monthRow.createCell(i);
				if (data instanceof String) {
            		cell.setCellValue((String) data);
            	}
	            i++;
			}
			rowNum++;
			
			for (Object[] data : paidPercentData) {
				Row row = sheet.createRow(rowNum++);
	            int colNum = 0;
	            for (Object field : data) {
	            	Cell cell = row.createCell(colNum++);
	            	if (field instanceof String) {
	            		cell.setCellValue((String) field);
	            	} else if (field instanceof Double) {
	            		cell.setCellValue((Double) field);
	            	} else if (field instanceof Integer) {
	            		cell.setCellValue((Integer) field);
	            	}
	            	if (colNum == 6 || colNum == 10 || colNum == 14 || colNum == 18) {
	            		cell.setCellStyle(orangeBackground);
	            	}
	            	if (colNum == 19) {
	            		cell.setCellStyle(greenBackground);
	            	}
	            }
			}
			
			String formatDateTime = LocalDateTime.now().format(DATETIME_FORMATTER);       
	        String fileName = "Общий отчет" + "_" + formatDateTime;
	        try (FileOutputStream outputStream = new FileOutputStream(EXCEL_FOLDER + "\\" + fileName + ".xlsx")) {
	            workbook.write(outputStream);
	            workbook.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
}
