package ru.se.percent;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.SwingConstants;

public class LoanFrame extends JFrame {

	private JPanel contentPane;
	private JTable tableRates;
	private JTable tableOperations;
	private JList listLoan;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoanFrame frame = new LoanFrame();
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
	public LoanFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panelLoanList = new JPanel();
		contentPane.add(panelLoanList, BorderLayout.WEST);
		panelLoanList.setLayout(new BorderLayout(0, 0));
		
		JPanel panelListCmd = new JPanel();
		panelLoanList.add(panelListCmd, BorderLayout.NORTH);
		
		JButton btnLoanAdd = new JButton("+");
		panelListCmd.add(btnLoanAdd);
		
		JButton btnloanUpdate = new JButton("f5");
		panelListCmd.add(btnloanUpdate);
		
		JButton btnLoadDelete = new JButton("x");
		panelListCmd.add(btnLoadDelete);
		
		listLoan = new JList();
		JScrollPane scrollPane = new JScrollPane(listLoan);
		panelLoanList.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panelLoan = new JPanel();
		contentPane.add(panelLoan, BorderLayout.CENTER);
		panelLoan.setLayout(new BorderLayout(0, 0));
		
		JPanel panelPropsAndActions = new JPanel();
		panelLoan.add(panelPropsAndActions, BorderLayout.WEST);
		panelPropsAndActions.setLayout(new BoxLayout(panelPropsAndActions, BoxLayout.Y_AXIS));
		
		JPanel panelProps = new JPanel();
		panelPropsAndActions.add(panelProps);
		panelProps.setLayout(new BoxLayout(panelProps, BoxLayout.Y_AXIS));
		
		JLabel lblBank = new JLabel("Банк:");
		panelProps.add(lblBank);
		
		JLabel lblNumber = new JLabel("№ договора:");
		panelProps.add(lblNumber);
		
		JLabel lblBeginDate = new JLabel("Дата начала:");
		panelProps.add(lblBeginDate);
		
		JLabel lblEndDate = new JLabel("Дата окончания:");
		panelProps.add(lblEndDate);
		
		JLabel lblAmount = new JLabel("Сумма займа:");
		panelProps.add(lblAmount);
		
		JLabel lblCheckDay = new JLabel("День расчета:");
		panelProps.add(lblCheckDay);
		
		JPanel panelRates = new JPanel();
		panelPropsAndActions.add(panelRates);
		panelRates.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRateTitle = new JLabel("Проценты");
		lblRateTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panelRates.add(lblRateTitle, BorderLayout.NORTH);
		
		tableRates = new JTable();
		
		JScrollPane scrollPaneRates = new JScrollPane(tableRates);
		panelRates.add(scrollPaneRates, BorderLayout.CENTER);
		
		JPanel panelOperations = new JPanel();
		panelPropsAndActions.add(panelOperations);
		panelOperations.setLayout(new BorderLayout(0, 0));
		
		tableOperations = new JTable();
		panelOperations.add(tableOperations);
		
		JPanel panelPercent = new JPanel();
		panelLoan.add(panelPercent);
	}

}
