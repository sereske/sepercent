package ru.se.percent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.JTabbedPane;

public class CreditFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField tfBank;
	private JTextField tfNumber;
	private JFormattedTextField tfStartDate;
	private JFormattedTextField tfEndDate;
	private JTextField tfCheckDay;
	
	private JTable tableRate;
	private JTable tableOperations;
	private JTable tableReport;
	
	private JFormattedTextField tfRateDate;
	//private JFormattedTextField tfRateValue;
	private JTextField tfRateValue;
	private JFormattedTextField tfOperationsDate;
	//private JFormattedTextField tfOperationsSum;
	private JTextField tfOperationsSum;
	private JComboBox<ru.se.percent.Type> cmbOperation;
	private JCheckBox chbDateDivision;
	private JButton btnAddOperation;
	private JButton btnDeleteOperation;
	private JButton btnAddRate;
	private JButton btnDeleteRate;
	private JButton btnUpdateLoan;
	private JButton btnDeleteLoan;
	private JButton btnAddLoan;

	private DataHelper dataHelper;
	private ExcelHelper excelHelper;
	private List<Loan> loans;
	private Loan loan;
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	//private NumberFormat numberFormat = NumberFormat.getInstance();
	
	private static final int DEFAULT_CHECKDAY = 28;
	private static final int MIN_YEAR = 2000;
	private static final int MAX_YEAR = 2050;
	
	//private static final String DATE_REG_EXP = "^[1-2][0-9]{3}-[0-1][0-2]-[1-3][0]$";
	
	private JList<Loan> listLoan;
	private JTable tableTotalPercentReceived;
	private JTable tableTotalPercentPaid;
	private JComboBox<Integer> cmbYear;
	
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
		dataHelper = DataHelper.getInstance();
		excelHelper = ExcelHelper.getInstance();
		loans = dataHelper.loadLoans();
		loan = null;
		createFrame();
		
		prepareProperties();
		refreshLoanList();
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
			//tfSum.setText(String.valueOf(loan.getOperations().size() == 0 || loan.getOperations().get(0) == null ? 0 : loan.getOperations().get(0).getSum()));
		} else {
			tfBank.setText("");
			tfCheckDay.setText("");
			tfNumber.setText("");
			tfStartDate.setText("");
			tfEndDate.setText("");
		}
	}
	
	private void prepareTables() {
		refreshRateTable();
		refreshOperationsTable();
		refreshReportTable();
	}
	
	private void refreshLoanList() {
		DefaultListModel<Loan> loanModel = new DefaultListModel<>();
		for (Loan loan : loans) {
			loanModel.addElement(loan);
		}
		listLoan.setModel(loanModel);
	}
	
	private void refreshRateTable() {
		/*
		if (loan != null) {
			List<String> rateCols = new ArrayList<>();
			rateCols.add("Дата");
			rateCols.add("Процентная ставка");
			Map<LocalDate, Double> newRates = new TreeMap<>();
			for (Map.Entry<LocalDate, Double> pair : loan.getRates().entrySet()) {
				newRates.put(pair.getKey(), pair.getValue());
			}
			Object[][] rateData = new Object[newRates.entrySet().size()][2];
			int i = 0;
			for (Map.Entry<LocalDate, Double> pair : newRates.entrySet()) {
				rateData[i][0] = pair.getKey();
				rateData[i][1] = pair.getValue();
				i++;
			}
			TableModel rateModel = new DefaultTableModel(rateData, rateCols.toArray());
			tableRate.setModel(rateModel);
		}
		*/
		Object[][] rateData = TableHelper.getRateTableData(loan);
		Object[] rateCols = TableHelper.getRateTableCols();
		TableModel rateModel = new DefaultTableModel(rateData, rateCols);
		tableRate.setModel(rateModel);
	}
	
	private void refreshOperationsTable() {
		/*
		if (loan != null) {
			List<String> operationsCols = new ArrayList<>();
			operationsCols.add("Дата");
			operationsCols.add("Тип");
			operationsCols.add("Сумма");
			Object[][] operationsData = new Object[loan.getOperations().size()][3];
			int i = 0;
			List<Operation> operations = loan.getOperations();
			Collections.sort(operations);
			for (Operation op : operations) {
				operationsData[i][0] = op.getDate();
				operationsData[i][1] = op.getType();
				operationsData[i][2] = op.getSum();
				i++;
			}
			TableModel operationsModel = new DefaultTableModel(operationsData, operationsCols.toArray());
			tableOperations.setModel(operationsModel);
		}
		*/
		Object[][] operationsData = TableHelper.getOperationsTableData(loan);
		Object[] operationsCols = TableHelper.getOperationsTableCols();
		TableModel operationsModel = new DefaultTableModel(operationsData, operationsCols);
		tableOperations.setModel(operationsModel);
		tableOperations.getColumnModel().getColumn(2).setCellRenderer(new CustomRenderer());
	}
	
	private void refreshReportTable() {
		Object[][] reportData = TableHelper.getReportTableData(loan, chbDateDivision.isSelected());
		Object[] reportCols = TableHelper.getReportTableCols();
		TableModel reportModel = new DefaultTableModel(reportData, reportCols);
		tableReport.setModel(reportModel);
		/*
		tableReport.getColumnModel().getColumn(0).setCellRenderer(new ColorRenderer());
		tableReport.getColumnModel().getColumn(4).setCellRenderer(new DecimalFormatRenderer());
		tableReport.getColumnModel().getColumn(5).setCellRenderer(new DecimalFormatRenderer());
		tableReport.getColumnModel().getColumn(6).setCellRenderer(new DecimalFormatRenderer());
		*/
		tableReport.getColumnModel().getColumn(0).setCellRenderer(new CustomRenderer());
		tableReport.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer());
		tableReport.getColumnModel().getColumn(5).setCellRenderer(new CustomRenderer());
		tableReport.getColumnModel().getColumn(6).setCellRenderer(new CustomRenderer());
		tableReport.getColumnModel().getColumn(7).setCellRenderer(new CustomRenderer());
		tableReport.getColumnModel().getColumn(8).setCellRenderer(new CustomRenderer());
	}
	
	private void refreshPercentTables() {
		Object[][] receivedPercentData = TableHelper.getPercentTableData(loans, new Integer(cmbYear.getSelectedItem().toString()), true, chbDateDivision.isSelected());
		Object[][] paidPercentData = TableHelper.getPercentTableData(loans, new Integer(cmbYear.getSelectedItem().toString()), false, chbDateDivision.isSelected());
		Object[] percentCols = TableHelper.getPercentTableCols();
		TableModel receivedPercentModel = new DefaultTableModel(receivedPercentData, percentCols);
		tableTotalPercentReceived.setModel(receivedPercentModel);
		for (int j = 2; j <= 18; j++) {
			tableTotalPercentReceived.getColumnModel().getColumn(j).setCellRenderer(new CustomRenderer());
		}
		TableModel paidPercentModel = new DefaultTableModel(paidPercentData, percentCols);
		tableTotalPercentPaid.setModel(paidPercentModel);
		for (int j = 2; j <= 18; j++) {
			tableTotalPercentPaid.getColumnModel().getColumn(j).setCellRenderer(new CustomRenderer());
		}
	}
	
	private void updateLoanPropsFromUI() {
		loan.setBank(tfBank.getText());
		loan.setNumber(tfNumber.getText());
		
		//if () {
			loan.setCheckDay(Integer.parseInt(tfCheckDay.getText().isEmpty() ? String.valueOf(DEFAULT_CHECKDAY) : tfCheckDay.getText()));
		//}
		loan.setStartDate(LocalDate.parse(tfStartDate.getText().isEmpty() ? LocalDate.now().toString() : tfStartDate.getText()));
		loan.setEndDate(LocalDate.parse(tfEndDate.getText().isEmpty() ? LocalDate.now().plusYears(1).toString() : tfEndDate.getText()));
	}
	
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
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane.getSelectedIndex() == 1) {
					refreshPercentTables();
				}
			}
			
		});
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel loanTab = new JPanel();
		tabbedPane.addTab("Кредиты", null, loanTab, null);
		loanTab.setLayout(new BorderLayout(0, 0));
		
		
		JPanel panelEast = new JPanel();
		loanTab.add(panelEast, BorderLayout.EAST);
		panelEast.setLayout(new BoxLayout(panelEast, BoxLayout.Y_AXIS));
		
		JPanel panelLoanList = new JPanel();
		panelEast.add(panelLoanList);
		panelLoanList.setLayout(new BorderLayout(0, 0));
		
		JPanel panelListCmd = new JPanel();
		panelLoanList.add(panelListCmd, BorderLayout.NORTH);
		
		JLabel lblListTitle = new JLabel("Список кредитов");
		panelListCmd.add(lblListTitle);
		
		listLoan = new JList<>();
		
		JScrollPane scrollPane5 = new JScrollPane(listLoan);
		panelLoanList.add(scrollPane5, BorderLayout.CENTER);
		
		listLoan.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
	              loan = listLoan.getSelectedValue();
                  prepareProperties();	  
                  prepareTables();
                  activateRateOperationButtons();
			}
			
		});
		listLoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panelLoan = new JPanel();
		panelEast.add(panelLoan);
		panelLoan.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panelLoan.add(panel, BorderLayout.NORTH);
		
		JLabel lblCreditLabel = new JLabel("Кредит");
		panel.add(lblCreditLabel);
		lblCreditLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnAddLoan = new JButton("+");
		btnAddLoan.setToolTipText("Добавить кредит");
		btnAddLoan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loan = new Loan();
				updateLoanPropsFromUI();
				loans.add(loan);
				dataHelper.saveLoan(loan.getFileName(), loan);
				refreshLoanList();
				prepareTables();
			}
		});
		panel.add(btnAddLoan);
		
		btnDeleteLoan = new JButton("x");
		btnDeleteLoan.setToolTipText("Удалить кредит");
		btnDeleteLoan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (loan != null) {
					loans.remove(loan);
					dataHelper.delete(loan.getFileName());
					refreshLoanList();
					loan = null;
					prepareProperties();
					prepareTables();
				}
			}
		});
		panel.add(btnDeleteLoan);
		
		btnUpdateLoan = new JButton("Обновить");
		btnUpdateLoan.setToolTipText("Обновить список кредитов");
		btnUpdateLoan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (loan == null) {
					return;
				}
				Loan oldLoan = new Loan(loan);
				updateLoanPropsFromUI();
				int indexOfLoan = 0;
				for (int i = 0; i < loans.size(); i++) {
					if (loans.get(i).getId() == loan.getId()) {
						indexOfLoan = i;
						break;
					}
				}
				loans.set(indexOfLoan, loan);
				if (loan.getFileName().equals(oldLoan.getFileName())) {
					dataHelper.saveLoan(loan.getFileName(), loan);
				} else {
					dataHelper.delete(oldLoan.getFileName());
					dataHelper.saveLoan(loan.getFileName(), loan);
				}
				refreshLoanList();
				prepareTables();
			}
		});
		panel.add(btnUpdateLoan);
		
		JPanel panelProps = new JPanel();
		panelLoan.add(panelProps, BorderLayout.CENTER);
		panelProps.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelProps.setLayout(new GridLayout(0, 2, 5, 5));
		
		JLabel lblBank = new JLabel("Банк");
		panelProps.add(lblBank);
		
		tfBank = new JTextField();
		tfBank.setToolTipText("Банк");
		panelProps.add(tfBank);
		tfBank.setColumns(10);
		
		JLabel lblNumber = new JLabel("№ договора");
		panelProps.add(lblNumber);
		
		tfNumber = new JTextField();
		tfNumber.setToolTipText("№ договора");
		panelProps.add(tfNumber);
		tfNumber.setColumns(10);
		
		JLabel lblStartDate = new JLabel("Дата начала (в формате yyyy-MM-dd)");
		panelProps.add(lblStartDate);
		
		tfStartDate = new JFormattedTextField(dateFormat);
		tfStartDate.setToolTipText("Дата начала");
		panelProps.add(tfStartDate);
		tfStartDate.setColumns(10);
		
		JLabel lblEndDate = new JLabel("Дата окончания (в формате yyyy-MM-dd)");
		panelProps.add(lblEndDate);
		
		tfEndDate = new JFormattedTextField(dateFormat);
		tfEndDate.setToolTipText("Дата окончания");
		tfEndDate.setColumns(10);
		panelProps.add(tfEndDate);
		
		JLabel lblCheckDay = new JLabel("День расчета (от 1 до 31)");
		panelProps.add(lblCheckDay);
		
		tfCheckDay = new JTextField();
		tfCheckDay.setToolTipText("День расчета");
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
		
		tfRateDate = new JFormattedTextField(dateFormat);
		tfRateDate.setToolTipText("Дата изменения процента");
		panelRateForm.add(tfRateDate);
		tfRateDate.setColumns(10);
		
		JLabel lblRateValueTitle = new JLabel("Процент");
		panelRateForm.add(lblRateValueTitle);
		
		//NumberFormatter rateFormatter = new NumberFormatter(numberFormat);
		//rateFormatter.setValueClass(Double.class);
		//rateFormatter.setMinimum(0);
		//rateFormatter.setMaximum(100.0);
		//rateFormatter.setAllowsInvalid(false);
		//rateFormatter.setCommitsOnValidEdit(true);
		//tfRateValue = new JFormattedTextField(rateFormatter);
		tfRateValue = new JTextField();
		tfRateValue.setToolTipText("Процент (от 0 до 100)");
		panelRateForm.add(tfRateValue);
		tfRateValue.setColumns(10);
		
		btnAddRate = new JButton();
		btnAddRate.setToolTipText("Добавить процент");
		btnAddRate.setText("+");
		panelRateForm.add(btnAddRate);
		
		btnDeleteRate = new JButton();
		btnDeleteRate.setToolTipText("Удалить процент");
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
				dataHelper.saveLoan(loan.getFileName(), loan);
				prepareTables();
			}
		});
		btnAddRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String rateValue = tfRateValue.getText();
				String rateDate = tfRateDate.getText();
				if (rateDate != null && !rateDate.isEmpty() && rateValue != null && !rateValue.isEmpty()) {
					if (rateValue.matches("^[0-9]*\\.?[0-9]{0,2}$")) {// && rateDate.matches(DATE_REG_EXP)) {
						LocalDate date = LocalDate.parse(rateDate);
						if (!date.isBefore(loan.getStartDate()) && !date.isAfter(loan.getEndDate())) {
							DefaultTableModel model = (DefaultTableModel) tableRate.getModel();
							model.addRow(new Object[] {rateDate, rateValue});
							loan.addRate(date, Double.parseDouble(rateValue));
							prepareTables();
							dataHelper.saveLoan(loan.getFileName(), loan);
						}
					}
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
		
		tfOperationsDate = new JFormattedTextField(dateFormat);
		tfOperationsDate.setToolTipText("Дата операции");
		panelOperationsForm.add(tfOperationsDate);
		tfOperationsDate.setColumns(10);
		
		JLabel lblOperation = new JLabel("Операция");
		panelOperationsForm.add(lblOperation);
		
		cmbOperation = new JComboBox<ru.se.percent.Type>();
		cmbOperation.setToolTipText("Операция");
		panelOperationsForm.add(cmbOperation);
		cmbOperation.setModel(new DefaultComboBoxModel<>(ru.se.percent.Type.values()));
		
		JLabel lblOperationsFormSum = new JLabel("Сумма");
		panelOperationsForm.add(lblOperationsFormSum);
		
		//NumberFormatter operationSumFormatter = new NumberFormatter(numberFormat);
		//operationSumFormatter.setValueClass(Double.class);
		//operationSumFormatter.setMinimum(0.0);
		//operationSumFormatter.setAllowsInvalid(false);
		//operationSumFormatter.setCommitsOnValidEdit(true);
		tfOperationsSum = new JTextField();
		tfOperationsSum.setToolTipText("Сумма операции");
		panelOperationsForm.add(tfOperationsSum);
		tfOperationsSum.setColumns(10);
		
		btnAddOperation = new JButton("+");
		btnAddOperation.setToolTipText("Добавить операцию");
		panelOperationsForm.add(btnAddOperation);
		btnAddOperation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String date = tfOperationsDate.getText();
				ru.se.percent.Type type = (ru.se.percent.Type) cmbOperation.getSelectedItem();
				String sum = tfOperationsSum.getText();
				if (date!= null && !date.isEmpty() && sum != null && !sum.isEmpty()) {
					if (sum.matches("^[0-9]+\\.?[0-9]*$")) {// && date.matches(DATE_REG_EXP)) {
						LocalDate ld = LocalDate.parse(date);
						if (!ld.isBefore(loan.getStartDate()) && !ld.isAfter(loan.getEndDate())) {
							DefaultTableModel model = (DefaultTableModel) tableOperations.getModel();
							model.addRow(new Object[] {date, type, sum});
							Operation op = new Operation(ld, Double.parseDouble(sum), type);
							loan.addOperation(op);
							dataHelper.saveLoan(loan.getFileName(), loan);
							logOperations();
							prepareTables();
						}
					}
				}
			}
		});
		btnAddOperation.setIcon(null);
		
		btnDeleteOperation = new JButton("x");
		btnDeleteOperation.setToolTipText("Удалить операцию");
		panelOperationsForm.add(btnDeleteOperation);
		
		JPanel panelStatus = new JPanel();
		loanTab.add(panelStatus, BorderLayout.SOUTH);
		FlowLayout flowLayout_1 = (FlowLayout) panelStatus.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEADING);
		
		JLabel lblStatus = new JLabel("Статус:");
		panelStatus.add(lblStatus);
		
		JPanel panelReport = new JPanel();
		loanTab.add(panelReport, BorderLayout.CENTER);
		panelReport.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
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
		
		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excelHelper.exportReportFile(loan, chbDateDivision.isSelected());
			}
		});
		btnExcel.setToolTipText("Выгрузить в Excel");
		panelReportCmd.add(btnExcel);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshReportTable();
			}
		});
		
		
		tableReport = new JTable();
		tableReport.setFillsViewportHeight(true);
		
		JScrollPane scrollPane4 = new JScrollPane(tableReport);
		panelReport.add(scrollPane4, BorderLayout.CENTER);
		
		JPanel reportTab = new JPanel();
		tabbedPane.addTab("Общий отчет", null, reportTab, null);
		reportTab.setLayout(new BoxLayout(reportTab, BoxLayout.Y_AXIS));
		
		tableTotalPercentReceived = new JTable();
		tableTotalPercentReceived.setFillsViewportHeight(true);
		
		//reportTab.add(tableTotalPercentPaid);
		
		tableTotalPercentPaid = new JTable();
		tableTotalPercentPaid.setFillsViewportHeight(true);
		
		JPanel panel_1 = new JPanel();
		reportTab.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Начислено процентов АО Сахаэнерго за");
		panel_1.add(lblNewLabel);
		
		cmbYear = new JComboBox<>();
		cmbYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshPercentTables();
			}
		});
		cmbYear.setToolTipText("Год");
		Integer[] years = new Integer[MAX_YEAR - MIN_YEAR + 1];
		for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
			years[i - MIN_YEAR] = i;
		}
		cmbYear.setModel(new DefaultComboBoxModel<Integer>(years));
		int currentYear = LocalDate.now().getYear();
		int currentYearIndex = currentYear - MIN_YEAR;
		cmbYear.setSelectedIndex(currentYearIndex);
		panel_1.add(cmbYear);
		
		JLabel lblNewLabel_1 = new JLabel("год");
		panel_1.add(lblNewLabel_1);
		
		JButton btnCommonExcel = new JButton("Excel");
		btnCommonExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int year = new Integer(cmbYear.getSelectedItem().toString());
				ExcelHelper.getInstance().exportCommonReportFile(loans, year, chbDateDivision.isSelected());
			}
		});
		panel_1.add(btnCommonExcel);
		//reportTab.add(tableTotalPercentReceived);
		
		JScrollPane scrollPane6 = new JScrollPane(tableTotalPercentReceived);
		reportTab.add(scrollPane6);
		
		JPanel panel_2 = new JPanel();
		reportTab.add(panel_2);
		
		JLabel lblNewLabel_2 = new JLabel("Уплачено процентов");
		panel_2.add(lblNewLabel_2);
		
		JScrollPane scrollPane7 = new JScrollPane(tableTotalPercentPaid);
		reportTab.add(scrollPane7);
		
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
				dataHelper.saveLoan(loan.getFileName(), loan);
				prepareTables();
			}
		});
		
		activateRateOperationButtons();
	}
	
	private void activateRateOperationButtons() {
		if (loan == null) {
			btnAddRate.setEnabled(false);
			btnDeleteRate.setEnabled(false);
			btnAddOperation.setEnabled(false);
			btnDeleteOperation.setEnabled(false);
		} else {
			btnAddRate.setEnabled(true);
			btnDeleteRate.setEnabled(true);
			btnAddOperation.setEnabled(true);
			btnDeleteOperation.setEnabled(true);
		}
	}
	
	private class CustomRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		
		private final DecimalFormat formatter = new DecimalFormat( "###,###.##" );
		 
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (table.getColumnCount() == TableHelper.getReportTableCols().length) {
				if (column == 0) {
					LocalDate date = (LocalDate) value;
					LocalDate startDate = date.withDayOfMonth(1);
					LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
					boolean mainPaid = false;
					boolean mainReceived = false;
					for (Operation op : loan.getOperations()) {
						if (!op.getDate().isBefore(startDate) && !op.getDate().isAfter(endDate)) {
							switch (op.getType()) {
							case MAIN_PAID:
								mainPaid = true;
								break;
							case MAIN_RECEIVED:
								mainReceived = true;
								break;
							}
						}
					}
					
					if (mainReceived && mainPaid) {
						component.setBackground(Color.GREEN);
					} else if (mainReceived) {
						component.setBackground(Color.ORANGE);
					} else if (mainPaid) {
						component.setBackground(Color.YELLOW);
					} else {
						component.setBackground(Color.WHITE);
					}
				}
				if (column == 4 || column == 5 || column == 6 || column == 7 || column == 8) {
					value = formatter.format((Number)value);
					return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				}
			} else if (table.getColumnCount() == TableHelper.getPercentTableCols().length) {
				value = formatter.format((Number)value);
				component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (column == 5 || column == 9 || column == 13 || column == 17) {
					component.setBackground(Color.ORANGE);
				} else if (column == 18) {
					component.setBackground(Color.GREEN);
				}
			} else if (table.getColumnCount() == TableHelper.getOperationsTableCols().length) {
				value = formatter.format((Number)value);
				component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
			return component;
		}
	}
	
	/*
	private class ColorRenderer extends DefaultTableCellRenderer {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (table.getColumnCount() == TableHelper.getReportTableCols().length) {
				LocalDate date = (LocalDate) value;
				LocalDate startDate = date.withDayOfMonth(1);
				LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
				boolean mainPaid = false;
				boolean mainReceived = false;
				for (Operation op : loan.getOperations()) {
					if (!op.getDate().isBefore(startDate) && !op.getDate().isAfter(endDate)) {
						switch (op.getType()) {
						case MAIN_PAID:
							mainPaid = true;
							break;
						case MAIN_RECEIVED:
							mainReceived = true;
							break;
						}
					}
				}
				
				if (mainReceived && mainPaid) {
					component.setBackground(Color.GREEN);
				} else if (mainReceived) {
					component.setBackground(Color.ORANGE);
				} else if (mainPaid) {
					component.setBackground(Color.YELLOW);
				} else {
					component.setBackground(Color.WHITE);
				}
			} else if (table.getColumnCount() == TableHelper.getPercentTableCols().length) {
				
			}
			return component;
		}
		
	}
	*/
}
