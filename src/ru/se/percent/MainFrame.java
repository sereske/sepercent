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
import java.io.StringBufferInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.EtchedBorder;

public class MainFrame extends JFrame {

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
	private JComboBox<Type> cbOperation;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1042, 581);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		contentPane.add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new GridLayout(0, 4, 5, 5));
		
		JLabel lblBank = new JLabel("Банк");
		panelNorth.add(lblBank);
		
		tfBank = new JTextField();
		panelNorth.add(tfBank);
		tfBank.setColumns(10);
		
		JLabel lblNumber = new JLabel("№ договора");
		panelNorth.add(lblNumber);
		
		tfNumber = new JTextField();
		panelNorth.add(tfNumber);
		tfNumber.setColumns(10);
		
		JLabel lblStartDate = new JLabel("Дата начала");
		panelNorth.add(lblStartDate);
		
		tfStartDate = new JTextField();
		panelNorth.add(tfStartDate);
		tfStartDate.setColumns(10);
		
		JLabel lblEndDate = new JLabel("Дата окончания");
		panelNorth.add(lblEndDate);
		
		tfEndDate = new JTextField();
		tfEndDate.setColumns(10);
		panelNorth.add(tfEndDate);
		
		JLabel lblSum = new JLabel("Сумма кредита");
		panelNorth.add(lblSum);
		
		tfSum = new JTextField();
		tfSum.setColumns(10);
		panelNorth.add(tfSum);
		
		JLabel lblCheckDay = new JLabel("День расчета");
		panelNorth.add(lblCheckDay);
		
		tfCheckDay = new JTextField();
		tfCheckDay.setColumns(10);
		panelNorth.add(tfCheckDay);
		
		JPanel panelWest = new JPanel();
		contentPane.add(panelWest, BorderLayout.WEST);
		panelWest.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelRate = new JPanel();
		panelRate.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelWest.add(panelRate);
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
		panelRate.add(panelRateForm, BorderLayout.WEST);
		panelRateForm.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelRateFormDate = new JPanel();
		panelRateForm.add(panelRateFormDate);
		
		JLabel lblRateDateTitle = new JLabel("Дата");
		panelRateFormDate.add(lblRateDateTitle);
		
		tfRateDate = new JTextField();
		panelRateFormDate.add(tfRateDate);
		tfRateDate.setColumns(10);
		
		JPanel panelRateFormValue = new JPanel();
		panelRateForm.add(panelRateFormValue);
		
		JLabel lblRateValueTitle = new JLabel("Процент");
		panelRateFormValue.add(lblRateValueTitle);
		
		tfRateValue = new JTextField();
		panelRateFormValue.add(tfRateValue);
		tfRateValue.setColumns(10);
		
		JPanel panelRateCmd = new JPanel();
		panelRateForm.add(panelRateCmd);
		
		JButton btnAddRate = new JButton();
		panelRateCmd.add(btnAddRate);
		btnAddRate.setIcon(new ImageIcon(MainFrame.class.getResource("/images/icons8-plus-sign.png")));
		
		JButton btnDeleteRate = new JButton();
		btnDeleteRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) tableRate.getModel();
				int[] selectedRows = tableRate.getSelectedRows();
				for (Integer row : selectedRows) {
					model.removeRow(row);
				}
			}
		});
		panelRateCmd.add(btnDeleteRate);
		btnDeleteRate.setIcon(new ImageIcon(MainFrame.class.getResource("/images/icons8-delete-button.png")));
		//System.out.println(MainFrame.class.getResource("/images/icons8-plus-sign.png"));
		//System.out.println(MainFrame.class.getResource("/images/icons8-edit.png"));
		btnAddRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String rateValue = tfRateValue.getText();
				String rateDate = tfRateDate.getText();
				if (rateDate != null && !rateDate.isEmpty() && rateValue != null && !rateValue.isEmpty()) {
					DefaultTableModel model = (DefaultTableModel) tableRate.getModel();
					model.addRow(new Object[] {rateDate, rateValue});
				}
			}
		});
		
		JPanel panelOperations = new JPanel();
		panelOperations.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelWest.add(panelOperations);
		panelOperations.setLayout(new BorderLayout(0, 0));
		
		JPanel panelOperationsTitle = new JPanel();
		panelOperations.add(panelOperationsTitle, BorderLayout.NORTH);
		
		JLabel lblOperations = new JLabel("Операции по кредиту");
		panelOperationsTitle.add(lblOperations);
		lblOperations.setHorizontalAlignment(SwingConstants.CENTER);
		
		tableOperations = new JTable();
		//scrollPane3.add(tableOperations);
		
		JScrollPane scrollPane3 = new JScrollPane(tableOperations);
		tableOperations.setFillsViewportHeight(true);
		panelOperations.add(scrollPane3, BorderLayout.CENTER);
		
		JPanel panelOperationsForm = new JPanel();
		panelOperations.add(panelOperationsForm, BorderLayout.WEST);
		panelOperationsForm.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelOperationsFormDate = new JPanel();
		panelOperationsForm.add(panelOperationsFormDate);
		
		JLabel lblOperationsDate = new JLabel("Дата");
		panelOperationsFormDate.add(lblOperationsDate);
		
		tfOperationsDate = new JTextField();
		panelOperationsFormDate.add(tfOperationsDate);
		tfOperationsDate.setColumns(10);
		
		JPanel panelOperationsFormOperation = new JPanel();
		panelOperationsForm.add(panelOperationsFormOperation);
		
		JLabel lblOperation = new JLabel("Операция");
		panelOperationsFormOperation.add(lblOperation);
		
		cbOperation = new JComboBox<>();
		cbOperation.setModel(new DefaultComboBoxModel(ru.se.percent.Type.values()));
		panelOperationsFormOperation.add(cbOperation);
		
		JPanel panelOperationsFormSum = new JPanel();
		panelOperationsForm.add(panelOperationsFormSum);
		
		JLabel lblOperationsFormSum = new JLabel("Сумма");
		panelOperationsFormSum.add(lblOperationsFormSum);
		
		tfOperationsSum = new JTextField();
		panelOperationsFormSum.add(tfOperationsSum);
		tfOperationsSum.setColumns(10);
		
		JPanel panelOperationsCmd = new JPanel();
		panelOperationsForm.add(panelOperationsCmd);
		
		JButton btnAddOperation = new JButton("");
		btnAddOperation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String date = tfOperationsDate.getText();
				Object op = cbOperation.getSelectedItem();
				String sum = tfOperationsSum.getText();
				DefaultTableModel model = (DefaultTableModel) tableOperations.getModel();
				model.addRow(new Object[] {date, op, sum});
			}
		});
		btnAddOperation.setIcon(new ImageIcon(MainFrame.class.getResource("/images/icons8-plus-sign.png")));
		panelOperationsCmd.add(btnAddOperation);
		
		JButton btnDeleteOperation = new JButton("");
		btnDeleteOperation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) tableOperations.getModel();
				int[] selectedRows = tableOperations.getSelectedRows();
				for (Integer rowIndex : selectedRows) {
					
					model.removeRow(rowIndex);
				}
			}
		});
		btnDeleteOperation.setIcon(new ImageIcon(MainFrame.class.getResource("/images/icons8-delete-button.png")));
		panelOperationsCmd.add(btnDeleteOperation);
		
		JPanel panelStatus = new JPanel();
		contentPane.add(panelStatus, BorderLayout.SOUTH);
		
		JPanel panelReport = new JPanel();
		panelReport.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panelReport, BorderLayout.CENTER);
		panelReport.setLayout(new BorderLayout(0, 0));
		
		JPanel panelReportCmd = new JPanel();
		panelReport.add(panelReportCmd, BorderLayout.NORTH);
		
		JLabel lblReportTitle = new JLabel("Отчет по процентам");
		panelReportCmd.add(lblReportTitle);
		lblReportTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnRefresh = new JButton("Обновить");
		panelReportCmd.add(btnRefresh);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		
		tableReport = new JTable();
		//scrollPane4.add(tableReport);
		tableReport.setFillsViewportHeight(true);
		
		JScrollPane scrollPane4 = new JScrollPane(tableReport);
		panelReport.add(scrollPane4, BorderLayout.CENTER);
		
		prepareProperties();
		prepareTables();
	}
	
	private void prepareProperties() {
		Loan loan = Loan.getTestLoan();
		
		tfBank.setText(loan.getBank());
		tfCheckDay.setText(String.valueOf(loan.getCheckDay()));
		tfNumber.setText(loan.getNumber());
		tfStartDate.setText(String.valueOf(loan.getStartDate()));
		tfEndDate.setText(String.valueOf(loan.getEndDate()));
		tfSum.setText(String.valueOf(loan.getOperations().get(0) == null ? 0 : loan.getOperations().get(0).getSum()));
	}
	
	private void prepareTables() {
		Loan loan = Loan.getTestLoan();
		
		List<String> rateCols = new ArrayList<>();
		rateCols.add("Дата");
		rateCols.add("Процентная ставка");
		Object[][] rateData = new Object[loan.getRates().entrySet().size()][2];
		int i = 0;
		for (Map.Entry<LocalDate, Double> pair : loan.getRates().entrySet()) {
			rateData[i][0] = pair.getKey();
			rateData[i][1] = pair.getValue();
			i++;
			//System.out.println(rateData[i][0] + " " + rateData[i][1]);
		}
		TableModel rateModel = new DefaultTableModel(rateData, rateCols.toArray());
		tableRate.setModel(rateModel);
		
		//((DefaultTableModel) tableRate.getModel()).fireTableDataChanged();
		
		List<String> operationsCols = new ArrayList<>();
		operationsCols.add("Дата");
		operationsCols.add("Тип");
		operationsCols.add("Сумма");
		Object[][] operationsData = new Object[loan.getOperations().size()][3];
		i = 0;
		for (Operation op : loan.getOperations()) {
			operationsData[i][0] = op.getDate();
			operationsData[i][1] = op.getType();
			operationsData[i][2] = op.getSum();
			i++;
			//System.out.println(operationsData[i][0] + " " + operationsData[i][1] + " " + operationsData[i][2]);
		}
		TableModel operationsModel = new DefaultTableModel(operationsData, operationsCols.toArray());
		tableOperations.setModel(operationsModel);
		//((DefaultTableModel) tableOperations.getModel()).fireTableDataChanged();
		
		List<String> reportCols = new ArrayList<>();
		reportCols.add("Начало периода");
		reportCols.add("Конец периода");
		reportCols.add("Кол-во дней");
		reportCols.add("Ставка");
		reportCols.add("Начислено процентов");
		reportCols.add("Основной долг");
		List<LocalDate> dates = loan.getDatesDivided();
		Object[][] reportData = new Object[dates.size() / 2][6];
		i = 0;
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
		TableModel reportModel = new DefaultTableModel(reportData, reportCols.toArray());
		tableReport.setModel(reportModel);
	}

}