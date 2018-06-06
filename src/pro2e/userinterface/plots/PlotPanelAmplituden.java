package pro2e.userinterface.plots;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import pro2e.DPIFixV3;
import pro2e.DonutFramework;
import pro2e.TraceV1;
import pro2e.matlabfunctions.Fensterfunktionen;
import pro2e.matlabfunctions.MiniMatlab;
import pro2e.model.Model;
import pro2e.userinterface.MyBorderFactory;

public class PlotPanelAmplituden extends JPanel {
	private static final long serialVersionUID = -4522467773085225830L;
	private JFreeChart chart = ChartFactory.createXYLineChart("", "Antennen", "Amplitude", null,
			PlotOrientation.VERTICAL, false, false, false);
	private TraceV1 trace = new TraceV1(this);

	public PlotPanelAmplituden() {
		super(new BorderLayout());
		trace.constructorCall();
		setPreferredSize(new Dimension(DPIFixV3.screen.width / 5, DPIFixV3.screen.height / 3));
		setBorder(MyBorderFactory.createMyBorder(" Fensterfunktion "));

		// Farben und Settings
		chart.setBackgroundPaint(getBackground());
		XYPlot xyplot = chart.getXYPlot();

		ValueAxis xAxis = xyplot.getDomainAxis(0);
		xAxis.setRange(0.0, 1.0);
		xAxis.setAutoRange(false);
		xAxis.setTickLabelsVisible(false);

		ValueAxis yAxis = xyplot.getRangeAxis(0);
		yAxis.setRange(0.0, 1.1);
		yAxis.setAutoRange(false);
		yAxis.setTickLabelsVisible(false);
		yAxis.setTickLabelsVisible(true);

		// MUSS VOR setRenderer AUFGERUFEN WERDEN
		JFreeChartDPIFix.applyChartTheme(chart);
		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(Color.GRAY);
		xyplot.setRangeGridlinePaint(Color.GRAY);
		chart.setBackgroundPaint(this.getBackground());

		XYItemRenderer renderer = new StandardXYItemRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesPaint(0, DonutFramework.Colors.DonutPink);
		xyplot.setRenderer(0, renderer);

		renderer = new StandardXYItemRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesPaint(0, Color.BLACK);
		xyplot.setRenderer(1, renderer);

		renderer = new StandardXYItemRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		renderer.setSeriesPaint(0, Color.GRAY);
		xyplot.setRenderer(2, renderer);

		add(new ChartPanel(chart));
	}

	/**
	 * @brief setzt die Darstellung (zoom) auf den Ursprungszustand zurück
	 */
	public void resetAxis() {
		trace.methodeCall();
		XYPlot xyplot = chart.getXYPlot();
		ValueAxis xAxis = xyplot.getDomainAxis(0);
		xAxis.setRange(0.0, 1.0);
		xAxis.setAutoRange(false);

		ValueAxis yAxis = xyplot.getRangeAxis(0);
		yAxis.setRange(0.0, 1.1);
		yAxis.setAutoRange(false);
	}

	/**
	 * @brief setzt die Daten für 3 Plots y = f(x)
	 * @param x
	 * @param y1
	 * @param y2
	 * @param y3
	 */
	public void setData(double[] x, double[] y1, double[] y2, double[] y3) {
		trace.methodeCall();
		XYSeries series = new XYSeries("Plot1");
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y1[i]);
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		chart.getXYPlot().setDataset(2, dataset);

		series = new XYSeries("Plot2");
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y2[i]);
		}
		dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		chart.getXYPlot().setDataset(1, dataset);

		series = new XYSeries("Plot3");
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y3[i]);
		}
		dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		chart.getXYPlot().setDataset(0, dataset);

		repaint();
		resetAxis(); // muss hier aufgerufen werden
	}

	/**
	 * @brief holt die Daten aus dem Model und generiert 3 verschiedene Kurven
	 * @details (Funktion, Funkion mit n-Antennen, Diskretisierte Funktion mit
	 *          n-Antennen)
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj) {
		trace.methodeCall();
		Model model = (Model) obs;
		int nAnt = model.getNAnt();

		int nw = 301; // gewuenschte Anzahl Punkte
		int d = nw / nAnt; // Breite (in Punkten) zwischen den Antennen
		int N = d * nAnt; // tatsaechliche Anzahl Punkte

		double[] xx = MiniMatlab.linspace(0, 1, N);
		double[] y1 = new double[N];
		double[] y3 = new double[N];

		int type = model.getWindow();

		double[] y4small = new double[nAnt];
		switch (type) {
		case Fensterfunktionen.BINOMIAL:
			// y1 = Fensterfunktionen.Binominal(N);
			y4small = Fensterfunktionen.Binominal(nAnt);
			break;
		case Fensterfunktionen.CHEBYSHEV:
			// y1 = Fensterfunktionen.Chebwin(N, model.getWindowParam());
			y4small = Fensterfunktionen.Chebwin(nAnt, model.getWindowParam());
			break;
		case Fensterfunktionen.COSINE:
			y1 = Fensterfunktionen.Cosin(N, model.getWindowParam());
			y4small = Fensterfunktionen.Cosin(nAnt, model.getWindowParam());
			break;
		case Fensterfunktionen.COSINESQUARE:
			y1 = Fensterfunktionen.CosinSquare(N, model.getWindowParam());
			y4small = Fensterfunktionen.CosinSquare(nAnt, model.getWindowParam());
			break;
		case Fensterfunktionen.EXPONENTIAL:
			y1 = Fensterfunktionen.Exponential(N, model.getWindowParam());
			y4small = Fensterfunktionen.Exponential(nAnt, model.getWindowParam());
			break;
		case Fensterfunktionen.TRIANGULAR:
			y1 = Fensterfunktionen.Triangular(N);
			y4small = Fensterfunktionen.Triangular(nAnt);
			break;
		default:
			y1 = Fensterfunktionen.Rectangular(N);
			y4small = Fensterfunktionen.Rectangular(nAnt);
		}

		double[] y4 = new double[N];
		for (int i = 0; i < y4small.length; i++) {
			for (int j = 0; j < d; j++) {
				y4[i * d + j] = y4small[i];
			}
		}

		if (model.isDiskretisierung()) {
			y3 = Fensterfunktionen.Quantize(y4, model.getnBits());
		} else {
			y3 = y4;
		}

		if (type == Fensterfunktionen.CHEBYSHEV || type == Fensterfunktionen.BINOMIAL) {
			y1 = y4;
		}

		setData(xx, y1, y4, y3);
	}

}
