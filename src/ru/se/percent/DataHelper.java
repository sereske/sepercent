package ru.se.percent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DataHelper {
	
	private static final String DATA_FOLDER = "data";
	
	private static DataHelper instance;
	
	private DataHelper() {}
	
	public static DataHelper getInstance() {
		if (instance == null) {
			instance = new DataHelper();
		}
		return instance;
	}
	
	public void delete(String fileName) {
		File file = new File(DATA_FOLDER + "\\" + fileName);
		if (file.delete()) {
			System.out.println(fileName + " is deleted");
		} else {
			System.out.println(fileName + " does not exist");
		}
	}
	
	public void saveLoanJackson(String fileName, Loan loan) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT, true);
		try {
			mapper.writeValue(new FileOutputStream(DATA_FOLDER + "\\" + fileName), loan);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Loan loadLoanJackson(String fileName) {
		Loan loan = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try (FileInputStream fis = new FileInputStream(fileName)) {
			loan = (Loan) mapper.readValue(fis, Loan.class);
			return loan;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void saveLoan(String fileName, Loan loan) {
		saveLoanJackson(fileName, loan);
	}
	
	public List<Loan> loadLoans() {
		List<Loan> loans = new ArrayList<>();
		File folder = new File(DATA_FOLDER);
		for (File file : folder.listFiles()) {
			Loan loan = loadLoanJackson(file.getAbsolutePath());
			loans.add(loan);
		}
		return loans;
	}
	
}
