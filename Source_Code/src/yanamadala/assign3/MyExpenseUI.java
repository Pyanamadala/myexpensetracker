package yanamadala.assign3;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.*;
// 
	  class MyExpenseUI extends JFrame{
  
	    DisplayView disp;
	    TableView txt;
		JPanel expenseLimitPanel,mainPanel,panel, displayPanel ;
		JLabel expenseLimitLabel,periodLabel;
		JTextField expenseLimitField;
		JComboBox limitPerPeriod;
		Font font;
		double amtSpent,totalAmtSpent=0.0;
	    String limitPeriod;
	    double expenseLimitValue;
	    ExpenseUpdater updater;
	    DatePicker picker;
	    CardLayout cardLayout;
	    JPanel cardPanel;
	    protected int periodInDays;
	    String startDateEx,endDateEx;
		double total=0.0;
	    // Constructor
		public MyExpenseUI(ExpenseUpdater updater){
			super("MyExpenseTracker");
			this.updater=updater;
			disp=new DisplayView(updater);
			txt= new TableView(updater);
			picker= new DatePicker();
			cardLayout=new CardLayout();
			enterExpenseLimit();
			enterExpenses();
			//panel to hold display the visualization
			displayPanel = new JPanel(new GridLayout(0,2));
			displayPanel.add(txt.addTablePanel());
			txt.updateEarlierExpensesInLimit(updater.getExpensesFromLimitPeriod(updater.getLimitStartDate(),updater.getLimitEndDate()));
			dateSelectionPanelForTableView();
			disp.setBackground(Color.WHITE);
			disp.setBorder(
		            BorderFactory.createTitledBorder(
		                    BorderFactory.createEtchedBorder( Color.BLACK
		                            , Color.BLUE),"Visualization"));
			displayPanel.add(disp.addDisplayPanel().add(disp));
			
			displayPanel.setPreferredSize(new Dimension(500,450));
			mainPanel.add(displayPanel,BorderLayout.SOUTH);
			
			Container c= getContentPane();
			setSize(1500,1000);
			c.add(mainPanel);
			setVisible(true);
			
		}
		//Method to create a combo box that gives drop down to select a period for the expense limit
		public JComboBox createComboBoxForPeriod(){
			
		 String[]period ={"per Day","per Week","per Month"};
		 limitPerPeriod= new JComboBox(period);
		 limitPerPeriod.setForeground(Color.BLUE);
		 limitPerPeriod.setFont(new Font("Sans Serif",Font.BOLD,16));
	
		 return limitPerPeriod;	
		}
		
// Method to add components to a panel that allows the user to enter the expense limit value, period and save them.
		public void enterExpenseLimit(){
			
            //panel that holds the textfield to take users input and a button to save it.
			expenseLimitPanel= new JPanel();
			font = new Font("Arial",Font.BOLD, 18);
			
			// panel that uses cardlayout to change card
			cardPanel = new JPanel(cardLayout);
			cardPanel.add( expenseLimitSaved(),"SavedLimit");
			
			expenseLimitValue=updater.getLimitValue();
			limitPeriod=updater.getLimitPeriod();
			
			//shows the panel expenseLimitPanel.
			cardLayout.show(cardPanel,"ExpenseLimit");
			
			JPanel titlePanel = new JPanel(new BorderLayout());
			titlePanel.setPreferredSize(new Dimension(500,100));
			
			mainPanel= new JPanel(new BorderLayout());
			
			JLabel title= new JLabel("My Expense Tracker");
			title.setFont(new Font("SansSerif",Font.BOLD,30));
			title.setForeground(Color.WHITE);
			titlePanel.add(title,BorderLayout.CENTER);
			
			panel = new JPanel(new BorderLayout());
			
			// action listener for the Save button
			JButton saveBtn = new JButton("Save");
			saveBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					txt.removeRows(0);
					String limit=expenseLimitField.getText();
					try {
				        int i = Integer.parseInt(limit);
				        expenseLimitValue=i;
				   } catch (NumberFormatException e) {
				        if (limit.matches("-?\\d+(\\.\\d+)?")) {
				            double d = Double.parseDouble(limit);
				            expenseLimitValue=d;
				        } else {
				            JOptionPane.showMessageDialog(null,"Please enter a valid amount");
				            expenseLimitField.setText("");
				        }
				     
				    }
					limitPeriod=((String)limitPerPeriod.getSelectedItem()).substring(4); 
					updater.updateTotalAmtSpent(0.0);
					updater.updateLimitDetails(expenseLimitValue, limitPeriod,getPeriodInDays(limitPeriod),updater.getTotalAmtSpent());
					cardPanel.add( expenseLimitSaved(),"SavedLimit");
					cardLayout.show(cardPanel,"SavedLimit");
				}
			});
			
			
			expenseLimitLabel= new JLabel("Expense Limit($):");
			expenseLimitLabel.setFont(font);
			
			expenseLimitField= new JTextField();
			expenseLimitField.setPreferredSize(new Dimension(150,35));
			expenseLimitField.setFont(new Font("Arial",Font.PLAIN,20));
			
			periodLabel = new JLabel("Select a period");
			periodLabel.setFont(font);
			
		
			JComboBox selectPeriod=createComboBoxForPeriod();
			
			expenseLimitPanel.add(expenseLimitLabel);
			expenseLimitPanel.add(expenseLimitField);
			expenseLimitPanel.add(selectPeriod);
			expenseLimitPanel.add(saveBtn);
			expenseLimitPanel.setBackground(Color.WHITE);
			cardPanel.add(expenseLimitPanel,"ExpenseLimit");
			
			panel.add(cardPanel,BorderLayout.NORTH);
			
			titlePanel.setBackground(Color.BLUE);
			mainPanel.add(titlePanel,BorderLayout.NORTH);
			mainPanel.add(panel,BorderLayout.CENTER);
			
			
		}
		// Method that returns a panel containing the saved limit value and period,a button to change their values.
		public JPanel expenseLimitSaved(){
			
			JPanel limitSavedPanel = new JPanel();
			
			JLabel savedLabel = new JLabel("    Expense Limit of $"+updater.getLimitValue()+" is set per "+updater.getLimitPeriod()+"    ");
			savedLabel.setFont(new Font("SansSerif",Font.BOLD,20));
			savedLabel.setForeground(Color.DARK_GRAY);
			JButton changeBtn = new JButton("Change");
			changeBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					cardLayout.show(cardPanel,"ExpenseLimit");
					}
			});
			limitSavedPanel.add(savedLabel);
			limitSavedPanel.add(changeBtn);
			limitSavedPanel.setBackground(Color.WHITE);
			return limitSavedPanel;
		}
		//Creates a panel that allows user to choose a particular category and enter expenses under that category.
		public void enterExpenses(){
			JPanel enterExpensePanel = new JPanel();
			
			//Categories displayed in the comboBox.
			String[] categories={"Food","Entertainment","Gas","Groceries","Shopping","MISC"};
			JComboBox categoryBox = new JComboBox(categories);
			categoryBox.setForeground(Color.BLUE);
			
			JLabel categoryLabel=new JLabel("Category :   ");
			categoryLabel.setFont(font);
			
			JLabel amtSpentLabel = new JLabel ("   Amount Spent ($):   ");
			amtSpentLabel.setFont(font);
			
			JTextField amtSpentField = new JTextField();
			amtSpentField.setFont(font);
			amtSpentField.setPreferredSize(new Dimension(150,30));
			
			//Action Listener for the add button 
			JButton addBtn = new JButton("Add");
			addBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					//checks if the current date is past the limit end date and if it is the start and end date of the period,total amount spent are reset.
					boolean status=updater.checkIfTotAmtSpentIsToBeReset(getDate());
					if(status){
						//method call to update the details
						updater.updateLimitDetails(expenseLimitValue, limitPeriod,getPeriodInDays(limitPeriod),0.0);
					}
					String category = (String)categoryBox.getSelectedItem();
					String amt=amtSpentField.getText();
					//checks if the entered amount is in the correct format.
					try {
				        int i = Integer.parseInt(amt);
				        amtSpent=i;
				        updater.addExpenses(category, amtSpent);
				        totalAmtSpent=updater.getTotalAmtSpent();
						updater.updateDb(category,amtSpent);
				   } catch (NumberFormatException e) {
				        if (amt.matches("-?\\d+(\\.\\d+)?")) {
				            double d = Double.parseDouble(amt);
				            amtSpent=d;
				            updater.addExpenses(category, amtSpent);
				            totalAmtSpent=updater.getTotalAmtSpent();
							updater.updateDb(category,amtSpent);
				        } else {
				            JOptionPane.showMessageDialog(null,"Please enter a valid amount");
				            amtSpentField.setText("");
				        } 
				        
				   }// if the expense limit is exceeded alert is shown
					if (totalAmtSpent> expenseLimitValue){
						JOptionPane.showMessageDialog(null,"You have exceeded your budget by $"+Double.toString(totalAmtSpent-expenseLimitValue),"Alert",JOptionPane.WARNING_MESSAGE);
					}
					amtSpentField.setText("");
				   }
			});
			enterExpensePanel.add(categoryLabel);
			enterExpensePanel.add(categoryBox);
			enterExpensePanel.add(amtSpentLabel);
			enterExpensePanel.add(amtSpentField);
			enterExpensePanel.add(addBtn);
			enterExpensePanel.setBackground(Color.WHITE);
			panel.add(enterExpensePanel,BorderLayout.CENTER);
		}
	    //method that returns a JButton which is added later to the text display
	     public JButton addUpdateButton(){
	        	JButton updateBtn= new JButton("Update");
	        	
	            updateBtn.addActionListener(new ActionListener(){
	            	public void actionPerformed(ActionEvent e){
	            		// gets the dates selected from the calendar.
	            		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            		Calendar cal = Calendar.getInstance();
	            		
	            		Date fromDate = (Date) picker.getFromDatePicker().getModel().getValue();
	            	    cal.setTime(fromDate);
	            	    startDateEx=dateFormat.format(cal.getTime());

	            		Date toDate = (Date) picker.getToDatePicker().getModel().getValue();
	            	    cal.setTime(toDate);
	            		endDateEx=dateFormat.format(cal.getTime());
	            		updateSelectedPeriodExpenses();
	            	}
	            });return updateBtn;
	        	
	        }
	     //Updates the JTable to display the expenses from the selected period.
	     public void updateSelectedPeriodExpenses(){
	    	 txt.removeRows(1);
	    	
	    	 for(String key:updater.getExpensesFromLimitPeriod(startDateEx,endDateEx).keySet()){
	    	
     			total=total+updater.getExpensesFromLimitPeriod(startDateEx,endDateEx).get(key);
     		}
     		txt.updateRow("",updater.getExpensesFromLimitPeriod(startDateEx,endDateEx),total);
     		total=0.0;
     		
	     }
	     //method that adds the calendar component to the panel
	     public void dateSelectionPanelForTableView(){
				txt.addComponentToTablePanel(picker.addFromDate(),picker.addToDate(addUpdateButton()));
		    }
	// returns expense limit set
		public double getExpenseLimitValue(){
			return expenseLimitValue;
		}
		//calculates the no of days to be added to the start date to get the end date of the limit period
		public int getPeriodInDays(String limitPeriod){
		if(limitPeriod.toUpperCase().equals("DAY")){
			periodInDays=0;
		}
		else if(limitPeriod.toUpperCase().equals("WEEK")){
			periodInDays=6;
		}else if(limitPeriod.toUpperCase().equals("MONTH")){
			//here 1 is the month and not no of days
				periodInDays=1;
		}
		return periodInDays;	
		}
		// Method that gets current date.
		public String getDate(){
			  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			   Calendar cal = Calendar.getInstance();
			   return dateFormat.format(cal.getTime());

		}

	}

 


