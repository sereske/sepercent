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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
		setBounds(100, 100, 563, 421);
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
		panelWest.add(panelRate);
		panelRate.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRate = new JLabel("Процентная ставка");
		lblRate.setHorizontalAlignment(SwingConstants.CENTER);
		panelRate.add(lblRate, BorderLayout.NORTH);
		
		tableRate = new JTable();
		//scrollPane1.add(tableRate);
		
		JScrollPane scrollPane1 = new JScrollPane(tableRate);
		tableRate.setFillsViewportHeight(true);
		panelRate.add(scrollPane1);
		
		JPanel panelRateCmd = new JPanel();
		panelRate.add(panelRateCmd, BorderLayout.SOUTH);
		
		JButton btnAddRate = new JButton("+");
		panelRateCmd.add(btnAddRate);
		
		JButton btnDeleteRate = new JButton("-");
		panelRateCmd.add(btnDeleteRate);
		
		JPanel panelOperations = new JPanel();
		panelWest.add(panelOperations);
		panelOperations.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOperations = new JLabel("Операции по кредиту");
		lblOperations.setHorizontalAlignment(SwingConstants.CENTER);
		panelOperations.add(lblOperations, BorderLayout.NORTH);
		
		tableOperations = new JTable();
		//scrollPane3.add(tableOperations);
		
		JScrollPane scrollPane3 = new JScrollPane(tableOperations);
		tableOperations.setFillsViewportHeight(true);
		panelOperations.add(scrollPane3);
		
		JPanel panelOperationCmd = new JPanel();
		panelOperations.add(panelOperationCmd, BorderLayout.SOUTH);
		
		JButton btnAddOperation = new JButton("+");
		panelOperationCmd.add(btnAddOperation);
		
		JButton btnDeleteOperation = new JButton("-");
		panelOperationCmd.add(btnDeleteOperation);
		
		JPanel panelCmd = new JPanel();
		contentPane.add(panelCmd, BorderLayout.SOUTH);
		
		JButton btnRefresh = new JButton("Обновить");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panelCmd.add(btnRefresh);
		
		JPanel panelReport = new JPanel();
		contentPane.add(panelReport, BorderLayout.CENTER);
		panelReport.setLayout(new BorderLayout(0, 0));
		
		JLabel lblReportTitle = new JLabel("Отчет по процентам");
		lblReportTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panelReport.add(lblReportTitle, BorderLayout.NORTH);
		
		
		tableReport = new JTable();
		//scrollPane4.add(tableReport);
		tableReport.setFillsViewportHeight(true);
		
		JScrollPane scrollPane4 = new JScrollPane(tableReport);
		panelReport.add(scrollPane4, BorderLayout.CENTER);
		
		prepareTables();
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
	}

}
