package yanamadala.assign3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Observer;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.html.HTMLDocument.Iterator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
// View class that displays expenses in textual format
public class TableView extends AbstractView {
	JTable table;
	DefaultTableModel data;
	ExpenseUpdater exUp;
	int sno=1;
	JPanel tableViewPanel;
	JPanel wholePanel;
	Set<String>key;
    HashMap<String,Double> exp;
	public TableView(ExpenseUpdater exUp) {
		super(exUp);
        exp= new LinkedHashMap<String,Double>();
	} 
//Method that adds the JTable to a scroll pane and returns it
	public JScrollPane addTablePanel() {
		tableViewPanel = new JPanel(new BorderLayout());
		wholePanel= new JPanel(new BorderLayout());
		wholePanel.setBackground(Color.WHITE);
		tableViewPanel.setBorder(BorderFactory
				.createTitledBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.BLUE), "Tabular View"));
		tableViewPanel.setBackground(Color.WHITE);
		tableViewPanel.add(addJTable(),BorderLayout.CENTER);
		tableViewPanel.setLayout(new BorderLayout());
		tableViewPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		tableViewPanel.add(table, BorderLayout.CENTER);
		wholePanel.add(tableViewPanel, BorderLayout.CENTER);
		JScrollPane scroller = new JScrollPane(wholePanel);
		return scroller;
	}
	//Method that adds the panels to allow the user to pick from and to to dates to view the expenses.
	public void addComponentToTablePanel(JPanel fromPanel,JPanel toPanel){
		Box fromToDatePanel=Box.createVerticalBox();
		fromToDatePanel.setBackground(Color.WHITE);
		fromToDatePanel.add(fromPanel);
		fromToDatePanel.add(toPanel);
		wholePanel.add(fromToDatePanel,BorderLayout.NORTH);
	}
//Method that creates a JTable with the given columns and dimensions and returns it
	public JTable addJTable() {
		String[] columns = { "Sno","Date","Category", "Amount Spent" };
		
		data = new DefaultTableModel(0, 0);
		table = new JTable();
		table.setRowHeight(30);
		table.setFont(new Font("Serif", Font.PLAIN, 16));
		data.setColumnIdentifiers(columns);
		table.setModel(data);
		TableColumn column = null;
		for (int i = 0; i < 4; i++) {
			column = table.getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(10);
			} else {
				column.setPreferredWidth(70);
			}
		}
		return table;
	}
	public void loadExpenses(HashMap<String,Double> updatedExp){
		for(String key:updatedExp.keySet()){
		exp.put(key,updatedExp.get(key));}
		
	}
    //Method that updates the rows of the JTable with the expenses 
	public void updateRow(String date,HashMap<String,Double> hmap,double totalAmtSpent){
			removeRows(1);
			loadExpenses(hmap);
			for(String key: exp.keySet()){
				data.addRow(new Object[]{sno,date,key,exp.get(key)});
				sno=sno+1;
			}
			sno=1;
			data.addRow(new Object[]{"","","Total",totalAmtSpent});
		
		
	}
	//updates the JTable with earlier expenses in the same limit period.
	public void updateEarlierExpensesInLimit(HashMap<String,Double> expensesInLimitPeriod){
		loadExpenses(expensesInLimitPeriod);
			for (String key:exp.keySet()){
				data.addRow(new Object[]{sno,"",key,exp.get(key)});
				sno=sno+1;
			}	
		
	}
	// removes rows from the JTable
	public void removeRows(int value){
		data.setRowCount(0);
	    sno=value;
	}
	//updates display whenever the database is updated.
	public void updateDisplay(){
		updateRow(getExpenseUpdater().getDate(),getExpenseUpdater().getExpenses(),getExpenseUpdater().getTotalAmtSpent());
	}
}
