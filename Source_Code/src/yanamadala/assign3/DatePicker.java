package yanamadala.assign3;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.Date;
import org.jdatepicker.impl.*;
import org.jdatepicker.util.*;
import org.jdatepicker.*;

//class that extends from JPanel to add calendar 
  
class DatePicker extends JPanel {
	
	JDatePanelImpl fromDatePanel,toDatePanel;
	JDatePickerImpl fromDatePicker,toDatePicker;
	
	//getter method
	public JDatePickerImpl getFromDatePicker(){
		return fromDatePicker;
	}
	//getter method
	public JDatePickerImpl getToDatePicker(){
		return toDatePicker;
	}
	// returns a panel that allows the user to select from date to view the expenses
        public JPanel addFromDate(){
        	
        JPanel fromPanel = new JPanel();
        fromPanel.setBackground(Color.WHITE);
        JLabel fromLabel = new JLabel("From");
        fromLabel.setFont(new Font("SansSerif",Font.BOLD,16));
        fromPanel.add(fromLabel);
        
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        model.setDate(2015,10,30);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        fromDatePanel = new JDatePanelImpl(model,p);
        fromDatePicker = new JDatePickerImpl(fromDatePanel,new DateLabelFormatter());
        fromPanel.add(fromDatePicker);
        return fromPanel;
        
    }
     // returns a panel that allows the user to select to date to view the expenses
        public JPanel addToDate(JButton Btn){
        	JPanel toPanel = new JPanel();
        	toPanel.setBackground(Color.WHITE);
            JLabel toLabel = new JLabel("To");
            toLabel.setFont(new Font("SansSerif",Font.BOLD,16));
            toPanel.add(toLabel);
            
        	UtilDateModel model = new UtilDateModel();
        	model.setSelected(true);
            model.setDate(2015,10,30);
            
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");
            
            toDatePanel = new JDatePanelImpl(model,p);
            toDatePicker = new JDatePickerImpl(toDatePanel,new DateLabelFormatter());
            toPanel.add(toDatePicker);
            toPanel.add(Btn);
            return toPanel;
        	
        }
       
}

class DateLabelFormatter extends AbstractFormatter {

    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }

}
