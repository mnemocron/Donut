package pro2e.userinterface.plots;

import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.Plot;

import pro2e.DPIFixV3;

public class JFreeChartDPIFix extends JFreeChart {

	private static final long serialVersionUID = 1L;

	public JFreeChartDPIFix(Plot plot) {
		super(plot);
	}

	public JFreeChartDPIFix(String title, Plot plot) {
		super(title, plot);

	}

	public JFreeChartDPIFix(String title, Font titleFont, Plot plot, boolean createLegend) {
		super(title, titleFont, plot, createLegend);

	}

	public static void applyChartTheme(JFreeChart chart) {
		final StandardChartTheme chartTheme = (StandardChartTheme) org.jfree.chart.StandardChartTheme
				.createJFreeTheme();

		final Font oldExtraLargeFont = chartTheme.getExtraLargeFont();
		final Font oldLargeFont = chartTheme.getLargeFont();
		final Font oldRegularFont = chartTheme.getRegularFont();
		final Font oldSmallFont = chartTheme.getSmallFont();

		final Font extraLargeFont = new Font("Sans-serif", oldExtraLargeFont.getStyle(),
				(int) (oldExtraLargeFont.getSize() * DPIFixV3.fontFactor * 0.8));
		final Font largeFont = new Font("Sans-serif", oldLargeFont.getStyle(),
				(int) (oldLargeFont.getSize() * DPIFixV3.fontFactor * 1.2));
		final Font regularFont = new Font("Sans-serif", oldRegularFont.getStyle(),
				(int) (oldRegularFont.getSize() * DPIFixV3.fontFactor * 1.2));
		final Font smallFont = new Font("Sans-serif", oldSmallFont.getStyle(),
				(int) (oldSmallFont.getSize() * DPIFixV3.fontFactor * 1.2));

		chartTheme.setExtraLargeFont(extraLargeFont);
		chartTheme.setLargeFont(largeFont);
		chartTheme.setRegularFont(regularFont);
		chartTheme.setSmallFont(smallFont);

		chartTheme.apply(chart);
	}

}
