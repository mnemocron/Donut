package pro2e.userinterface.plots;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pro2e.DPIFixV3;
import pro2e.matlabfunctions.Fensterfunktionen;
import pro2e.matlabfunctions.MiniMatlab;

public class PlotPanel extends JPanel {
	private static final long serialVersionUID = -4522467773085225830L;
	private JFreeChart chart = ChartFactory.createXYLineChart("Titel", "x - Achse", "y - Achse", null,
			PlotOrientation.VERTICAL, false, false, false);

	public PlotPanel() {
		super(new BorderLayout());
		setPreferredSize(new Dimension(DPIFixV3.screen.width / 3, DPIFixV3.screen.height / 3));

		// Farben und Settings
		chart.setBackgroundPaint(getBackground());
		XYPlot xyplot = chart.getXYPlot();
		xyplot.setBackgroundPaint(getBackground());
		xyplot.setRangeGridlinePaint(Color.black);
		xyplot.setDomainGridlinePaint(Color.black);

		ValueAxis xAxis = xyplot.getDomainAxis(0);
		xAxis.setRange(0, 1);
		xAxis.setAutoRange(false);
		xAxis.setTickLabelsVisible(true);

		ValueAxis yAxis = xyplot.getRangeAxis(0);
		yAxis.setRange(0, 1.0);
		yAxis.setAutoRange(false);
		yAxis.setTickLabelsVisible(true);

		JFreeChartDPIFix.applyChartTheme(chart);

		XYItemRenderer renderer = new StandardXYItemRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesPaint(0, Color.blue);
		xyplot.setRenderer(0, renderer);

		renderer = new StandardXYItemRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesPaint(0, Color.red);
		xyplot.setRenderer(1, renderer);

		add(new ChartPanel(chart));

		// Test:
		update(null, null);
	}

	public void setData(double[] x, double[] y1, double[] y2) {
		XYPlot xyplot = chart.getXYPlot();

		ValueAxis xAxis = xyplot.getDomainAxis(0);
		xAxis.setRange(0, x.length);

		XYSeries series = new XYSeries("Plot1");
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y1[i]);
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		chart.getXYPlot().setDataset(0, dataset);

		series = new XYSeries("Plot2");
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y2[i]);
		}
		dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		chart.getXYPlot().setDataset(1, dataset);

		repaint();
	}

	public void update(Observable obs, Object obj) {
		double[] n = MiniMatlab.linspace(0, 99, 100);

		double[] f1 = Fensterfunktionen.Cosin(n.length, 0);
		// double[] f2 = Fensterfunktionen.CosinSquare(n.length, 0.5);
		double[] f2 = new double[n.length];
		for (int i = 0; i < f2.length; i++) {
			double phi = i * 2 * Math.PI / n.length;
			// out[i] = this.amplitude * Math.cos(Math.PI/2*Math.cos(phi))/Math.sin(phi);
			if (Math.abs(Math.sin(phi)) > 0.00001) {
				f2[i] = Math.abs(Math.cos((Math.PI / 2.0) * Math.cos(phi)) / Math.sin(phi));
			} else {
				f2[i] = 0;
			}
		}
		System.out.println(f1.length);
		System.out.println(f1[0]);

		setData(n, f2, f2);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

				} catch (Exception exception) {
					exception.printStackTrace();
				}
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setTitle("MVCFramework");
				frame.add(new PlotPanel());
				DPIFixV3.scaleSwingFonts();
				SwingUtilities.updateComponentTreeUI(frame);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

}
