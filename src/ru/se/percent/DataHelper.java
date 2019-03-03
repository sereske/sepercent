package ru.se.percent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
	
	public static void saveLoanJackson(String fileName, Loan loan) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
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
	
	public static Loan loadLoanJackson(String fileName) {
		Loan loan = null;
		ObjectMapper mapper = new ObjectMapper();
		//SimpleModule simpleModule = new SimpleModule();
		//simpleModule.addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer());
		//mapper.registerModule(simpleModule);
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
	
	public static void saveLoan(String fileName, Loan loan) {
		saveLoanJackson(fileName, loan);
	}
	
	public static List<Loan> loadLoans() {
		List<Loan> loans = new ArrayList<>();
		File folder = new File(DATA_FOLDER);
		for (File file : folder.listFiles()) {
			Loan loan = loadLoanJackson(file.getAbsolutePath());
			loans.add(loan);
		}
		return loans;
	}
	
	private static class LocalDateKeyDeserializer extends KeyDeserializer {

		@Override
		public Object deserializeKey(String key, DeserializationContext ctx) throws IOException {
			 try {
		            return LocalDate.parse(key, DateTimeFormatter.ISO_LOCAL_DATE);
		        } catch (DateTimeException e) {
		            return null;
		        }
		}
		
	}
	
}
