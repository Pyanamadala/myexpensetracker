package yanamadala.assign3;

import javax.swing.border.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

//View class that shows the output in the form of visualization 

public class DisplayView extends AbstractView {
	JPanel visualizationPanel;
	private Color[] col = new Color[] { Color.red, Color.green, Color.magenta, Color.blue, Color.cyan, Color.black,
			Color.yellow, Color.gray };
	String title = " Expenses ";

	private Map<String, Double> bars = new LinkedHashMap<String, Double>();

	public DisplayView(ExpenseUpdater updater) {
		super(updater);

	}
	// Method that creates a new panel to add the visualization and adds it to
	// the main panel

	public JPanel addDisplayPanel() {
		visualizationPanel = new JPanel();
		visualizationPanel.setBackground(Color.WHITE);
		addBar(getExpenseUpdater().getExpensesFromLimitPeriod(getExpenseUpdater().getLimitStartDate(),
				getExpenseUpdater().getLimitEndDate()));

		return visualizationPanel;

	}

	// Method that updates display by calling repaint() which calls paint and
	// then paint calls paintComponent()
	public void updateDisplay() {
		repaint();
	}

	// Method that loads the expenses in the HashMap to display
	public void addBar(HashMap<String, Double> expenses) {
		bars.put("ExpenseLimit", getExpenseUpdater().getLimitValue());
		bars.put("Amount Spent", getExpenseUpdater().getTotalAmtSpent());
		for (String key : expenses.keySet()) {
			bars.put(key, expenses.get(key));
		}

		repaint();
	}

	// paintComponent(g) is overridden to draw the line graph to display
	// expenses
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		addBar(getExpenseUpdater().getExpenses());
		Font titleFont = new Font("SansSerif", Font.BOLD, 20);
		FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
		Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
		FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

		int titleWidth = titleFontMetrics.stringWidth(title);
		int y = titleFontMetrics.getAscent();
		int x = (getWidth() - titleWidth) / 2;
		g.setFont(titleFont);
		g.drawString(title, x, y + 30);

		double max = Integer.MIN_VALUE;
		double min = Integer.MAX_VALUE;

		for (Double value : bars.values()) {
			max = Math.max(max, value);
			min = Math.max(min, value);
		}
		int maxStr = 0;
		int minStr = 5;
		for (String str : bars.keySet()) {
			maxStr = Math.max(maxStr, labelFontMetrics.stringWidth(str));
			minStr = Math.max(minStr, labelFontMetrics.stringWidth(str));
		}
		int top = titleFontMetrics.getHeight();
		int bottom = labelFontMetrics.getHeight();
		y = getHeight() - labelFontMetrics.getDescent();
		g.setFont(labelFont);
		int gap = 30;
		int start = 50 + maxStr + 2;
		int end = y - 3 * gap;
		int i = 0;
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);

		// vertical line to show start point of the lines
		Line2D verticalLine = new Line2D.Double(start - 1, end, start, 100);
		((Graphics2D) g).draw(verticalLine);

		for (String cat : bars.keySet()) {
			double value = bars.get(cat);
			int length = (int) ((getWidth() - (start + maxStr + 2)) * ((double) value / max));
			g.setColor(col[i]);
			((Graphics2D) g).setStroke(new BasicStroke(5.0f));
			// draws lines for the line graph
			Line2D line = new Line2D.Double(start, end - 1, start + length, end - 1);
			((Graphics2D) g).draw(line);
			g.setColor(Color.black);
			int labelWidth = labelFontMetrics.stringWidth(cat);
			x = 30;
			g.drawString(cat, x, end);
			g.drawString(String.valueOf(bars.get(cat)), start + length + 4, end);
			end = end - gap;
			i = i + 1;
		}
	}
}
