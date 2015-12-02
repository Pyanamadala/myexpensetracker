package yanamadala.assign3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
//abstract class that extends from JPanel and implements Observer
abstract class AbstractView extends JPanel implements Observer{
private ExpenseUpdater updater;
	public AbstractView(ExpenseUpdater updater){
		this.updater= updater;
		//registers to be an observer
	  updater.addObserver(this);
		
	}
	public ExpenseUpdater getExpenseUpdater(){
		return updater;
	}
	// abstract method which will be implemented by the sub classes
	protected	abstract  void updateDisplay();
	public void update(Observable observable,Object object){
		updateDisplay();
	}

}
