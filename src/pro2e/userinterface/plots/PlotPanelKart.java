package pro2e.userinterface.plots;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;

import javax.swing.JPanel;

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

import pro2e.DonutFramework;
import pro2e.TraceV1;
import pro2e.matlabfunctions.MiniMatlab;
import pro2e.model.Model;

public class PlotPanelKart extends JPanel {
	private static final long serialVersionUID = -4522467773085225830L;
	private JFreeChart chart = ChartFactory.createXYLineChart("Strahlungsdiagramm", "Abstrahlwinkel [φ] °",
			"Intensität dB", null, PlotOrientation.VERTICAL, false, false, false);
	private TraceV1 trace = new TraceV1(this);

	public PlotPanelKart() {
		super(new GridBagLayout());
		trace.constructorCall();

		// Farben und Settings
		chart.setBackgroundPaint(getBackground());
		XYPlot xyplot = chart.getXYPlot();

		ValueAxis xAxis = xyplot.getDomainAxis(0);
		xAxis.setRange(0.0, 360);
		xAxis.setAutoRange(false);
		xAxis.setTickLabelsVisible(true);

		ValueAxis yAxis = xyplot.getRangeAxis(0);
		yAxis.setRange(-60.0, 0.0);
		yAxis.setAutoRange(false);
		yAxis.setTickLabelsVisible(true);

		/** IMPORTANT MUSS VOR setRenderer AUFGERUFEN WERDEN ! */
		JFreeChartDPIFix.applyChartTheme(chart);

		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(Color.GRAY);
		xyplot.setRangeGridlinePaint(Color.GRAY);
		chart.setBackgroundPaint(Color.WHITE);

		XYItemRenderer renderer = new StandardXYItemRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesPaint(0, DonutFramework.Colors.Blue);
		xyplot.setRenderer(0, renderer);

		renderer = new StandardXYItemRenderer();
		renderer.setSeriesStroke(1, new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesPaint(0, Color.GRAY);
		xyplot.setRenderer(1, renderer);

		add(new ChartPanel(chart), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	}

	/**
	 * <pre>
	 * setzt die Darstellung (zoom) auf den Ursprungszustand zurück
	 * </pre>
	 * 
	 * @param plotScaleDb
	 *            true wenn die logarithmische Skalierung verwendet werden soll
	 */
	public void resetAxis(boolean plotScaleDb) {
		trace.methodeCall();
		XYPlot xyplot = chart.getXYPlot();
		ValueAxis xAxis = xyplot.getDomainAxis(0);
		xAxis.setRange(0.0, 360);
		xAxis.setAutoRange(false);

		ValueAxis yAxis = xyplot.getRangeAxis(0);
		if (plotScaleDb) {
			yAxis.setRange(-60.0, 0.0);
			yAxis.setLabel("Intensität dB");
		} else {
			yAxis.setRange(0.0, 1.0);
			yAxis.setLabel("Intensität linear");
		}
		yAxis.setAutoRange(false);
	}

	/**
	 * <pre>
	 * setzt die Daten für den Plot y1 = f(x)
	 * </pre>
	 * 
	 * @param x
	 * @param y1
	 */
	public void setData(double[] x, double[] y1) {
		trace.methodeCall();
		XYSeries series = new XYSeries("Plot1");
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y1[i]);
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		chart.getXYPlot().setDataset(0, dataset);
		dataset = new XYSeriesCollection();

		series = new XYSeries("Plot2");
		for (int i = 0; i < x.length; i++) {
			// series.add(x[i], -13.0);
		}
		dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		chart.getXYPlot().setDataset(1, dataset);

		repaint();
	}

	/**
	 * <pre>
	 * holt die Daten aus dem Model und setzt sie in den Plot
	 * </pre>
	 * 
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj) {
		trace.methodeCall();
		Model model = (Model) obs;

		double[] phi = MiniMatlab.linspace(0, 360, 361);
		// model.getAntArray().setnMeasures(phi.length);
		double[] intensity = model.getAntArray().getLinearPhasedArrayPlot();

		setData(phi, intensity);
	}

}
