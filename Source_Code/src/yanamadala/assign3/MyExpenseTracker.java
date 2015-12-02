package yanamadala.assign3;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
// Main class to start the application.
public class MyExpenseTracker extends JFrame {
	public MyExpenseTracker(){
		ExpenseUpdater up = new ExpenseUpdater();
		MyExpenseUI Ui= new MyExpenseUI(up); 	
		
	}
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				MyExpenseTracker tracker= new MyExpenseTracker();
		tracker.setExtendedState(tracker.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		tracker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
		});
	}
}
