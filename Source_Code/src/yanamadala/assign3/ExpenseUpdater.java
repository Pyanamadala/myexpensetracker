package yanamadala.assign3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.Box;
import javax.swing.JButton;
// Model class that updates the database when the user changes the expense limit or enters expenses for a category
public class ExpenseUpdater extends Observable {
	HashMap<String,Double>expenses;
	double totalAmtSpent;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/MYEXPENSETRACKER";
	static final String USERNAME = "root";
	static final String PASSWORD = "";
	private Connection con;
	private Statement stmt;
	String sql;
	ResultSet rs;
	HashMap<String,Double> expensesInLimitPeriod;
	
	
	public ExpenseUpdater(){
		expenses=new HashMap<String,Double>();
		expensesInLimitPeriod=new LinkedHashMap<String,Double>();
	
	}
	// getter method that returns expenses
	public HashMap<String,Double> getExpenses(){
		return expenses;	
	}
	//Method that checks if the current date is past the limit period end date and sets the amount spent to 0 if it has.
	public boolean checkIfTotAmtSpentIsToBeReset(String date){
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         try {
			Date curDate = dateFormat.parse(date);
			Date endDate = dateFormat.parse(getLimitEndDate());
			if(curDate.after(endDate)){
				totalAmtSpent=0.0;
				return true;
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} 
         return false;
	}
		
		private void getConnection() {
			try {
				Class.forName(JDBC_DRIVER);
				this.con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
				stmt = this.con.createStatement();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		//stores expenses entered by the user
		public void addExpenses(String category,double amt){
			totalAmtSpent=getTotalAmtSpent()+amt;
			updateTotalAmtSpent(totalAmtSpent);
			if(expenses.containsKey(category)){
				expenses.put(category,expenses.get(category)+amt);
			}
			else{
			expenses.put(category, amt);
			}
			//notifies the text and display views that the model has changed
			setChanged();
			notifyObservers();
		}
		public String getDate(){
			  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			   Calendar cal = Calendar.getInstance();
			   return dateFormat.format(cal.getTime());

		}
		//updates the database 
		public void updateDb(String category,double amtSpent){
			this.getConnection();
			sql="insert into EXPENSES (CATEGORY,AMOUNT,TIMESTAMP) VALUES ('"+category+"',"+amtSpent+",now())";
			try{
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    try { rs.close(); } catch (Exception e) { }
			    try { con.close(); } catch (Exception e) {  }
			}
			
		}
		//updates the expense limit details in the database
		public void updateLimitDetails(double limit,String period,int days,double totAmtSpent){
			this.getConnection();
			if(period.equals("Month")){
				sql="update  EXPENSELIMIT  set LIMITVALUE="+limit+",LIMITPERIOD='"+period+"',STARTDATE=now(),ENDDATE=DATE_ADD(now(),interval "+days+" month)-interval 1 day,AMTSPENT="+totAmtSpent+" where ID=1";
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				   } finally {
					    try { rs.close(); } catch (Exception e) { }
					    try { con.close(); } catch (Exception e) {  }
					}setChanged();
					notifyObservers();
				}
			else{
			sql="update  EXPENSELIMIT  set LIMITVALUE="+limit+",LIMITPERIOD='"+period+"',STARTDATE=now(),ENDDATE=now()+interval "+days+" day,AMTSPENT="+totAmtSpent+" where ID=1";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 finally {
			    try { rs.close(); } catch (Exception e) { }
			    try { con.close(); } catch (Exception e) {  }
		 }
			}
			//notifies the text and display view s
			setChanged();
			notifyObservers();
		
		}
		// returns the limit value
		public double getLimitValue(){
			double limitValue=0.0;
			this.getConnection();
			sql="select LIMITVALUE FROM EXPENSELIMIT WHERE ID=1;";
			try {
				rs=stmt.executeQuery(sql);
				rs.next();
				limitValue = rs.getDouble("LIMITVALUE");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    try { rs.close(); } catch (Exception e) { }
			    try { con.close(); } catch (Exception e) {  }
			}
			return limitValue;
		}//returns the limit period
		public String getLimitPeriod(){
			String limitPeriod="";
			this.getConnection();
			sql="select LIMITPERIOD FROM EXPENSELIMIT WHERE ID=1;";
			try {
				rs=stmt.executeQuery(sql);
				rs.next();
				limitPeriod = rs.getString("LIMITPERIOD");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    try { rs.close(); } catch (Exception e) { }
			    try { con.close(); } catch (Exception e) {  }
			}
			return limitPeriod;
		}
		//returns the expense limit start date
		public String getLimitStartDate(){
			String startDate="";
			this.getConnection();
			sql="select STARTDATE FROM EXPENSELIMIT WHERE ID=1;";
			try {
				rs=stmt.executeQuery(sql);
				rs.next();
				startDate = rs.getString("STARTDATE");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    try { rs.close(); } catch (Exception e) { }
			    try { con.close(); } catch (Exception e) {  }
			}
			
			return startDate;
		}
		//updates the limit start date 
		public void setLimitStartEndDate(){
			String startDate="";
			this.getConnection();
			sql="select STARTDATE FROM EXPENSELIMIT WHERE ID=1;";
			try {
				rs=stmt.executeQuery(sql);
				rs.next();
				startDate = rs.getString("STARTDATE");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    try { rs.close(); } catch (Exception e) { }
			    try { con.close(); } catch (Exception e) {  }
			}
		}
		//returns limit end date
		public String getLimitEndDate(){
			String endDate="";
			this.getConnection();
			sql="select ENDDATE FROM EXPENSELIMIT WHERE ID=1;";
			try {
				rs=stmt.executeQuery(sql);
				rs.next();
				endDate = rs.getString("ENDDATE");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    try { rs.close(); } catch (Exception e) { }
			    try { con.close(); } catch (Exception e) {  }
			}
			
			return endDate;
		}
		//updates the total amount spent thus far in the limit period
		public void updateTotalAmtSpent(double totalAmtSpent){
			this.getConnection();
			sql="update  EXPENSELIMIT  set AMTSPENT="+totalAmtSpent+" where ID=1";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    try { rs.close(); } catch (Exception e) { }
			    try { con.close(); } catch (Exception e) {  }
			}
		}
		//returns total amount spent thus far in the limit period
	   public double getTotalAmtSpent(){
		   this.getConnection();
		   double tot=0.0;
			sql="select AMTSPENT FROM  EXPENSELIMIT  where ID=1";
			try {
				rs=stmt.executeQuery(sql);
				rs.next();
				tot=rs.getDouble("AMTSPENT");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    try { rs.close(); } catch (Exception e) { }
			    try { con.close(); } catch (Exception e) {  }
			}
	   return tot;
	}  
	   //returns expenses from the given start and end date.
	   public HashMap<String,Double> getExpensesFromLimitPeriod(String startDate,String endDate){
		   String[] cat ={"Food","Entertainment","Gas","Groceries","Shopping","MISC"};
		   String category;
		   double amt;
		   this.getConnection();
		   for(int i =0;i<cat.length;i++){   
		   sql="select sum(AMOUNT) FROM EXPENSES WHERE TIMESTAMP>='"+startDate+"' and TimeStamp<='"+endDate+"' and  CATEGORY='"+cat[i]+"'";
		   try {
			rs=stmt.executeQuery(sql);
			rs.next();
			amt= rs.getDouble(1);
			expensesInLimitPeriod.put(cat[i],amt);
		} catch (SQLException e){
			e.printStackTrace();
		}    
		   }
		   try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		   
		   return expensesInLimitPeriod;
		   
	   }
	}



