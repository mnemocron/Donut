package pro2e.userinterface.plots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pro2e.DPIFixV3;
import pro2e.TraceV1;
import pro2e.matlabfunctions.MiniMatlab;
import pro2e.model.Model;
import pro2e.userinterface.MyBorderFactory;

// import pro2e.teamX.model.Model;

public class TabbedPlotPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TraceV1 trace = new TraceV1(this);

	private PlotPanelKart plotPanelKart = new PlotPanelKart();
	private PlotPanelPol plotPanelPol = new PlotPanelPol();

	public TabbedPlotPanel() {
		super(new GridLayout());
		trace.constructorCall();
		setMinimumSize(new Dimension(DPIFixV3.screen.width / 5, 0));
		setBorder(MyBorderFactory.createMyBorder(" Plots "));

		JPanel asdf = new JPanel();
		asdf.setBackground(Color.black);

		JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		tabpane.addTab("kartesisch", plotPanelKart);
		tabpane.addTab("polar", plotPanelPol);

		tabpane.setForeground(Color.WHITE);

		add(tabpane);
		setVisible(true);

	}

	public double get3dbAngle(Observable obs, Object obj) {
		trace.methodeCall();
		Model model = (Model) obs;
		return model.getAntArray().get3dbAngle();
	}

	@Override
	public void paintComponent(Graphics g) {
		trace.methodeCall();
		super.paintComponent(g);
	}

	/**
	 * @brief Ruft die update() Methoden der darauf befindenden Plots Panels auf
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj) {
		trace.methodeCall();
		Model model = (Model) obs;
		// plotPanelKart.update(obs, obj);
		// plotPanelPol.update(obs, obj);

		double[] phi = MiniMatlab.linspace(0, 360, 361);
		model.setnMeasures(phi.length);
		// double[] intensity = model.getAntArray().getLinearPhasedArrayPlot();
		double[] intensity = model.getPlot();
		plotPanelKart.setData(phi, intensity); // hier di Punkte nur einmal berechnen ist effizienter als
		plotPanelPol.setData(phi, intensity); // über die update() Funktion der einzelnen Plots
	}

	/**
	 * @brief Setzt die Darstellung der darauf befindenden Plot Panels zurück
	 */
	public void resetAxis(boolean plotAxisDb) {
		trace.methodeCall();
		plotPanelKart.resetAxis(plotAxisDb);
		plotPanelPol.resetAxis(plotAxisDb);
	}

}
