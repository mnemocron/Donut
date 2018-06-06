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
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pro2e.TraceV1;
import pro2e.matlabfunctions.MiniMatlab;
import pro2e.model.Model;

public class PlotPanelPol extends JPanel {
	private static final long serialVersionUID = -4522467773085225830L;
	private JFreeChart chart;
	private TraceV1 trace = new TraceV1(this);

	public PlotPanelPol() {
		super(new GridBagLayout());
		trace.constructorCall();
		// setPreferredSize(new Dimension(DPIFixV3.screen.width / 3,
		// DPIFixV3.screen.height / 2));

		chart = ChartFactory.createPolarChart("Strahlungsdiagramm", null, false, false, false);

		// Farben und Settings
		chart.setBackgroundPaint(getBackground());
		PolarPlot polplot = (PolarPlot) chart.getPlot();

		// MUSS VOR setRenderer AUFGERUFEN WERDEN
		JFreeChartDPIFix.applyChartTheme(chart);

		polplot.setRadiusMinorGridlinesVisible(false);
		polplot.setBackgroundPaint(Color.WHITE);
		polplot.setAngleGridlinePaint(Color.GRAY);
		polplot.setRadiusGridlinePaint(Color.GRAY);
		chart.setBackgroundPaint(Color.WHITE);
		// chart.setBackgroundPaint(this.getBackground());

		ValueAxis rAxis = polplot.getAxis(0);
		rAxis.setRange(-60.0, 0.0);
		rAxis.setAutoRange(false);
		rAxis.setTickLabelsVisible(true);
		rAxis.setLabel("Intensität dB");

		DefaultPolarItemRenderer renderer = (DefaultPolarItemRenderer) polplot.getRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(2f));
		renderer.setSeriesPaint(0, Color.darkGray);
		renderer.setShapesVisible(false);
		polplot.setRenderer(0, renderer);

		renderer = new DefaultPolarItemRenderer();
		renderer.setSeriesStroke(1, new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesPaint(0, Color.GRAY);
		renderer.setShapesVisible(false);
		polplot.setRenderer(1, renderer);

		polplot.setAngleOffset(180);

		add(new ChartPanel(chart), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	}

	/**
	 * @brief setzt die Darstellung (zoom) auf den Ursprungszustand zurück
	 */
	public void resetAxis(boolean plotScaleDb) {
		trace.methodeCall();
		PolarPlot polplot = (PolarPlot) chart.getPlot();
		ValueAxis rAxis = polplot.getAxis(0);
		if (plotScaleDb) {
			rAxis.setRange(-60.0, 0.0);
			rAxis.setLabel("Intensität dB");
		} else {
			rAxis.setRange(0.0, 1.0);
			rAxis.setLabel("Intensität normiert");
		}
		rAxis.setAutoRange(false);
	}

	/**
	 * @brief setzt die Daten für den Plot y1 = f(x)
	 * @param x
	 * @param y1
	 */
	public void setData(double[] x, double[] y1) {
		trace.methodeCall();

		XYSeries series = new XYSeries("Plot1");
		for (int i = 0; i < y1.length; i++) {
			if (Double.isNaN(y1[i])) {
				y1[i] = -500; // Polar Plots können NaN nicht verarbeiten
			}
		}
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y1[i]);
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		((PolarPlot) chart.getPlot()).setDataset(0, dataset);

		series = new XYSeries("Plot2");
		for (int i = 0; i < x.length; i++) {
			// series.add(x[i], -13.0);
		}
		dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		((PolarPlot) chart.getPlot()).setDataset(1, dataset);

		repaint();
	}

	/**
	 * @brief holt die Daten aus dem Model und setzt sie in den Plot
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj) {
		trace.methodeCall();
		Model model = (Model) obs;

		double[] phi = MiniMatlab.linspace(0, 360, 361);
		model.getAntArray().setnMeasures(phi.length);
		double[] intensity = model.getAntArray().getLinearPhasedArrayPlot();

		setData(phi, intensity);
	}

}
