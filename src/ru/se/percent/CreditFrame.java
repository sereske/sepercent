package ru.se.percent;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTree;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class CreditFrame extends JFrame {

	private JPanel contentPane;
	
	private JTextField tfBank;
	private JTextField tfNumber;
	private JTextField tfStartDate;
	private JTextField tfEndDate;
	private JTextField tfSum;
	private JTextField tfCheckDay;
	
	private JTable tableRate;
	private JTable tableOperations;
	private JTable tableReport;
	
	private JTextField tfRateDate;
	private JTextField tfRateValue;
	private JTextField tfOperationsDate;
	private JTextField tfOperationsSum;
	private JComboBox<ru.se.percent.Type> cmbOperation;
	private JCheckBox chbDateDivision;

	private DataHelper helper;
	private List<Loan> loans;
	private Loan loan;
	
	//private static final String DEFAULT_FILE_NAME = "data\\sepercent.txt";
	//private static final String SETTINGS_FILE_NAME = "settings.properties";
	private static final String DATA_FOLDER = "data";
	
	private static final int DEFAULT_CHECKDAY = 28;
	private JList<Loan> tableCreditList;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreditFrame frame = new CreditFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CreditFrame() {
		helper = DataHelper.getInstance();
		loans = helper.loadLoans(DATA_FOLDER);
		loan = null;
		createFrame();
		
		prepareProperties();
		prepareTables();
	}
	
	private void logRates() {
		loan.getRates().forEach((k, v) -> System.out.println(k + " " + v));
	}
	
	private void logOperations() {
		loan.getOperations().stream().forEach(e -> System.out.println(e.getDate() + " " + e.getType() + " " + e.getSum()));
	}
	
	private void prepareProperties() {
		if (loan != null) {
			tfBank.setText(loan.getBank());
			tfCheckDay.setText(String.valueOf(loan.getCheckDay()));
			tfNumber.setText(loan.getNumber());
			tfStartDate.setText(String.valueOf(loan.getStartDate()));
			tfEndDate.setText(String.valueOf(loan.getEndDate()));
			tfSum.setText(String.valueOf(loan.getOperations().size() == 0 || loan.getOperations().get(0) == null ? 0 : loan.getOperations().get(0).getSum()));
		}
	}
	
	private void prepareTables() {
		refreshCreditList();
		refreshRateTable();
		refreshOperationsTable();
		refreshReportTable();
	}
	
	private void refreshCreditList() {
		
	}
	
	private void refreshRateTable() {
		List<String> rateCols = new ArrayList<>();
		rateCols.add("Дата");
		rateCols.add("Процентная ставка");
		Object[][] rateData = new Object[loan.getRates().entrySet().size()][2];
		int i = 0;
		for (Map.Entry<LocalDate, Double> pair : loan.getRates().entrySet()) {
			rateData[i][0] = pair.getKey();
			rateData[i][1] = pair.getValue();
			i++;
		}
		TableModel rateModel = new DefaultTableModel(rateData, rateCols.toArray());
		tableRate.setModel(rateModel);
	}
	
	private void refreshOperationsTable() {
		List<String> operationsCols = new ArrayList<>();
		operationsCols.add("Дата");
		operationsCols.add("Тип");
		operationsCols.add("Сумма");
		Object[][] operationsData = new Object[loan.getOperations().size()][3];
		int i = 0;
		for (Operation op : loan.getOperations()) {
			operationsData[i][0] = op.getDate();
			operationsData[i][1] = op.getType();
			operationsData[i][2] = op.getSum();
			i++;
		}
		TableModel operationsModel = new DefaultTableModel(operationsData, operationsCols.toArray());
		tableOperations.setModel(operationsModel);
	}
	
	private void refreshReportTable() {
		List<String> reportCols = new ArrayList<>();
		reportCols.add("Начало периода");
		reportCols.add("Конец периода");
		reportCols.add("Кол-во дней");
		reportCols.add("Ставка");
		reportCols.add("Начислено процентов");
		reportCols.add("Основной долг");
		List<LocalDate> dates = chbDateDivision.isSelected() ? loan.getDatesDivided() : loan.getDates();
		Object[][] reportData = new Object[dates.size() / 2][6];
		int i = 0;
		try {
		for (int k = 0; k < dates.size() - 1; k += 2) {
			LocalDate startDate = dates.get(k);
			LocalDate endDate = dates.get(k + 1);
			double percent = loan.getPercent(startDate, endDate);
			double rate = loan.getCurrentRate(endDate);
			long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
			double debt = loan.getCurrentDebt(endDate);
			reportData[i][0] = startDate;
			reportData[i][1] = endDate;
			reportData[i][2] = daysBetween;
			reportData[i][3] = rate;
			reportData[i][4] = percent;
			reportData[i][5] = debt;
			i++;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TableModel reportModel = new DefaultTableModel(reportData, reportCols.toArray());
		tableReport.setModel(reportModel);
	}
	
	private void updateLoanPropsFromUI() {
		loan.setBank(tfBank.getText());
		loan.setNumber(tfNumber.getText());
		loan.setCheckDay(Integer.parseInt(tfCheckDay.getText().isEmpty() ? String.valueOf(DEFAULT_CHECKDAY) : tfCheckDay.getText()));
		loan.setStartDate(LocalDate.parse(tfStartDate.getText().isEmpty() ? LocalDate.now().toString() : tfStartDate.getText()));
		loan.setEndDate(LocalDate.parse(tfEndDate.getText().isEmpty() ? LocalDate.now().plusYears(1).toString() : tfEndDate.getText()));
	}
	
	/*
	private void loadSettings() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(SETTINGS_FILE_NAME));
			currentFileName = props.getProperty("currentFileName");
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(CreditFrame.this, 
					"Ошибка при чтении настроек", 
					e.getMessage(), 
					JOptionPane.ERROR_MESSAGE);
			CreditFrame.this.setVisible(false);
			CreditFrame.this.dispose();
		} 
	}
	
	private void saveSettings() {
		
	}
	*/
	
	private void createFrame() {
		setTitle("Кредит СЭ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1530, 850);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMain = new JMenu("Главная");
		menuBar.add(mnMain);
		
		JMenuItem mntmSave = new JMenuItem("Сохранить в файл");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//saveLoan(currentFileName);
			}
		});
		mnMain.add(mntmSave);
		
		JMenuItem mntmLoad = new JMenuItem("Загрузить из файла");
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//loadLoan();
			}
		});
		mnMain.add(mntmLoad);
		
		JMenu mnHelp = new JMenu("Справка");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("О программе");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(CreditFrame.this, 
						"Кредит СЭ 1.0 - калькулятор расчета процентов кредитов с нефиксированной процентной ставкой\nОРиПИС УИТ \"АО Сахаэнерго\"", 
						"О программе", 
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		JPanel panelEast = new JPanel();
		contentPane.add(panelEast, BorderLayout.EAST);
		panelEast.setLayout(new BoxLayout(panelEast, BoxLayout.Y_AXIS));
		
		JPanel panelCreditList = new JPanel();
		panelEast.add(panelCreditList);
		panelCreditList.setLayout(new BorderLayout(0, 0));
		
		JPanel panelListCmd = new JPanel();
		panelCreditList.add(panelListCmd, BorderLayout.NORTH);
		
		JLabel lblListTitle = new JLabel("Список кредитов");
		panelListCmd.add(lblListTitle);
		
		tableCreditList = new JList();
		
		JScrollPane scrollPane5 = new JScrollPane(tableCreditList);
		panelCreditList.add(scrollPane5, BorderLayout.CENTER);
		
		JPanel panelCredit = new JPanel();
		panelEast.add(panelCredit);
		panelCredit.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panelCredit.add(panel, BorderLayout.NORTH);
		
		JLabel lblCreditLabel = new JLabel("Кредит");
		panel.add(lblCreditLabel);
		lblCreditLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnAddCredit = new JButton("+");
		panel.add(btnAddCredit);
		
		JButton btnDeleteCredit = new JButton("x");
		panel.add(btnDeleteCredit);
		
		JPanel panelProps = new JPanel();
		panelCredit.add(panelProps, BorderLayout.CENTER);
		panelProps.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelProps.setLayout(new GridLayout(0, 2, 5, 5));
		
		JLabel lblBank = new JLabel("Банк");
		panelProps.add(lblBank);
		
		tfBank = new JTextField();
		panelProps.add(tfBank);
		tfBank.setColumns(10);
		
		JLabel lblNumber = new JLabel("№ договора");
		panelProps.add(lblNumber);
		
		tfNumber = new JTextField();
		panelProps.add(tfNumber);
		tfNumber.setColumns(10);
		
		JLabel lblStartDate = new JLabel("Дата начала");
		panelProps.add(lblStartDate);
		
		tfStartDate = new JTextField();
		panelProps.add(tfStartDate);
		tfStartDate.setColumns(10);
		
		JLabel lblEndDate = new JLabel("Дата окончания");
		panelProps.add(lblEndDate);
		
		tfEndDate = new JTextField();
		tfEndDate.setColumns(10);
		panelProps.add(tfEndDate);
		
		JLabel lblSum = new JLabel("Сумма кредита");
		panelProps.add(lblSum);
		
		tfSum = new JTextField();
		tfSum.setColumns(10);
		panelProps.add(tfSum);
		
		JLabel lblCheckDay = new JLabel("День расчета");
		panelProps.add(lblCheckDay);
		
		tfCheckDay = new JTextField();
		tfCheckDay.setColumns(10);
		panelProps.add(tfCheckDay);
		
		JPanel panelRate = new JPanel();
		panelRate.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelEast.add(panelRate);
		panelRate.setLayout(new BorderLayout(0, 0));
		
		tableRate = new JTable();
		//scrollPane1.add(tableRate);
		
		JScrollPane scrollPane1 = new JScrollPane(tableRate);
		tableRate.setFillsViewportHeight(true);
		panelRate.add(scrollPane1);
		
		JPanel panelRateTitle = new JPanel();
		panelRate.add(panelRateTitle, BorderLayout.NORTH);
		
		JLabel lblRate = new JLabel("Процентная ставка");
		panelRateTitle.add(lblRate);
		lblRate.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panelRateForm = new JPanel();
		panelRate.add(panelRateForm, BorderLayout.SOUTH);
		panelRateForm.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblRateDateTitle = new JLabel("Дата");
		panelRateForm.add(lblRateDateTitle);
		
		tfRateDate = new JTextField();
		panelRateForm.add(tfRateDate);
		tfRateDate.setColumns(10);
		
		JLabel lblRateValueTitle = new JLabel("Процент");
		panelRateForm.add(lblRateValueTitle);
		
		tfRateValue = new JTextField();
		panelRateForm.add(tfRateValue);
		tfRateValue.setColumns(10);
		
		JButton btnAddRate = new JButton();
		btnAddRate.setText("+");
		panelRateForm.add(btnAddRate);
		
		JButton btnDeleteRate = new JButton();
		btnDeleteRate.setText("x");
		panelRateForm.add(btnDeleteRate);
		btnDeleteRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) tableRate.getModel();
				int[] selectedRows = tableRate.getSelectedRows();
				System.out.println(model.getColumnCount());
				for (Integer rowIndex : selectedRows) {
					String strDate = model.getValueAt(rowIndex, 0).toString();
					LocalDate date = LocalDate.parse(strDate);
					model.removeRow(rowIndex);
					loan.removeRate(date);
				}
				prepareTables();
			}
		});
		btnAddRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String rateValue = tfRateValue.getText();
				String rateDate = tfRateDate.getText();
				if (rateDate != null && !rateDate.isEmpty() && rateValue != null && !rateValue.isEmpty()) {
					DefaultTableModel model = (DefaultTableModel) tableRate.getModel();
					model.addRow(new Object[] {rateDate, rateValue});
					loan.addRate(LocalDate.parse(rateDate), Double.parseDouble(rateValue));
					prepareTables();
				}
			}
		});
		
		JPanel panelOperations = new JPanel();
		panelOperations.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelEast.add(panelOperations);
		panelOperations.setLayout(new BorderLayout(0, 0));
		
		JPanel panelOperationsTitle = new JPanel();
		panelOperations.add(panelOperationsTitle, BorderLayout.NORTH);
		
		JLabel lblOperations = new JLabel("Операции по кредиту");
		panelOperationsTitle.add(lblOperations);
		lblOperations.setHorizontalAlignment(SwingConstants.CENTER);
		
		tableOperations = new JTable();
		
		JScrollPane scrollPane3 = new JScrollPane(tableOperations);
		tableOperations.setFillsViewportHeight(true);
		panelOperations.add(scrollPane3, BorderLayout.CENTER);
		
		JPanel panelOperationsForm = new JPanel();
		panelOperations.add(panelOperationsForm, BorderLayout.SOUTH);
		panelOperationsForm.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblOperationsDate = new JLabel("Дата");
		panelOperationsForm.add(lblOperationsDate);
		
		tfOperationsDate = new JTextField();
		panelOperationsForm.add(tfOperationsDate);
		tfOperationsDate.setColumns(10);
		
		JLabel lblOperation = new JLabel("Операция");
		panelOperationsForm.add(lblOperation);
		
		cmbOperation = new JComboBox<ru.se.percent.Type>();
		panelOperationsForm.add(cmbOperation);
		cmbOperation.setModel(new DefaultComboBoxModel<>(ru.se.percent.Type.values()));
		
		JLabel lblOperationsFormSum = new JLabel("Сумма");
		panelOperationsForm.add(lblOperationsFormSum);
		
		tfOperationsSum = new JTextField();
		panelOperationsForm.add(tfOperationsSum);
		tfOperationsSum.setColumns(10);
		
		JButton btnAddOperation = new JButton("+");
		panelOperationsForm.add(btnAddOperation);
		btnAddOperation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String date = tfOperationsDate.getText();
				ru.se.percent.Type type = (ru.se.percent.Type) cmbOperation.getSelectedItem();
				String sum = tfOperationsSum.getText();
				DefaultTableModel model = (DefaultTableModel) tableOperations.getModel();
				model.addRow(new Object[] {date, type, sum});
				Operation op = new Operation(LocalDate.parse(date), Double.parseDouble(sum), type);
				loan.addOperation(op);
				logOperations();
				prepareTables();
			}
		});
		btnAddOperation.setIcon(null);
		
		JButton btnDeleteOperation = new JButton("x");
		panelOperationsForm.add(btnDeleteOperation);
		btnDeleteOperation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) tableOperations.getModel();
				int[] selectedRows = tableOperations.getSelectedRows();
				for (Integer rowIndex : selectedRows) {
					String strDate = model.getValueAt(rowIndex, 0).toString();
					double sum = Double.parseDouble(model.getValueAt(rowIndex, 2).toString());
					ru.se.percent.Type type = (ru.se.percent.Type) model.getValueAt(rowIndex, 1);
					Operation op = new Operation(LocalDate.parse(strDate), sum, type);
					loan.removeOperation(op);
					model.removeRow(rowIndex);
				}
				logOperations();
				prepareTables();
			}
		});
		
		JPanel panelStatus = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelStatus.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEADING);
		contentPane.add(panelStatus, BorderLayout.SOUTH);
		
		JLabel lblStatus = new JLabel("Статус:");
		panelStatus.add(lblStatus);
		
		JPanel panelReport = new JPanel();
		panelReport.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panelReport, BorderLayout.CENTER);
		panelReport.setLayout(new BorderLayout(0, 0));
		
		JPanel panelReportCmd = new JPanel();
		panelReport.add(panelReportCmd, BorderLayout.NORTH);
		
		JLabel lblReportTitle = new JLabel("Отчет по процентам");
		panelReportCmd.add(lblReportTitle);
		lblReportTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		chbDateDivision = new JCheckBox("Разделение месяца по дню расчета");
		panelReportCmd.add(chbDateDivision);
		chbDateDivision.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshReportTable();
			}
		});
		chbDateDivision.setSelected(true);
		
		JButton btnRefresh = new JButton("Обновить отчет по процентам");
		panelReportCmd.add(btnRefresh);
		btnRefresh.setToolTipText("Обновить отчет по процентам");
		btnRefresh.setIcon(new ImageIcon(CreditFrame.class.getResource("/images/icons8-available-updates-40.png")));
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshReportTable();
			}
		});
		
		
		tableReport = new JTable();
		tableReport.setFillsViewportHeight(true);
		
		JScrollPane scrollPane4 = new JScrollPane(tableReport);
		panelReport.add(scrollPane4, BorderLayout.CENTER);
	}
	
	private void loadLoan(String fileName) {
		loan = new Loan();
		try (FileInputStream fis
			      = new FileInputStream(fileName);
			    ObjectInputStream ois = new ObjectInputStream(fis)) {
			loan = (Loan) ois.readObject();
			System.out.println(loan);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(CreditFrame.this, 
					"Ошибка при чтении - FileNotFoundException", 
					e.getMessage(), 
					JOptionPane.ERROR_MESSAGE);
			
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(CreditFrame.this, 
					"Ошибка при чтении - IOException", 
					e.getMessage(), 
					JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(CreditFrame.this, 
					"Ошибка при чтении - ClassNotFoundException", 
					e.getMessage(), 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	

}
