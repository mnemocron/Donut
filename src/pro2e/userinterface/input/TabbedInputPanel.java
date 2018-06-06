package pro2e.userinterface.input;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Observable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pro2e.DPIFixV3;
import pro2e.TraceV1;
import pro2e.controller.Controller;
import pro2e.model.Model;
import pro2e.userinterface.MyBorderFactory;
import pro2e.userinterface.plots.AntennaPicturePanelCircular;
import pro2e.userinterface.plots.PlotPanelAmplituden;

public class TabbedInputPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TraceV1 trace = new TraceV1(this);

	@SuppressWarnings("unused")
	private Controller controller;
	private AntennaInputPanel antennaInputPanel;
	private PhasedArrayInputPanel phasedInputPanel;
	private SidelobeInputPanel sidelobeInputPanel;
	private PlotPanelAmplituden windowPlot = new PlotPanelAmplituden();
	private PanelPlotLegende legendePanel = new PanelPlotLegende();

	private JPanel panelAntennas = new JPanel(new GridBagLayout());
	private JPanel panelAdvanced = new JPanel(new GridBagLayout());

	private AntennaPicturePanelCircular kreisBild = new AntennaPicturePanelCircular();

	private JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

	public TabbedInputPanel(Controller controller) {
		super(new GridLayout());
		trace.constructorCall();
		this.controller = controller;
		antennaInputPanel = new AntennaInputPanel(controller);
		phasedInputPanel = new PhasedArrayInputPanel(controller);
		sidelobeInputPanel = new SidelobeInputPanel(controller);

		setMinimumSize(new Dimension(DPIFixV3.screen.width / 5, 0));
		setBorder(MyBorderFactory.createMyBorder(" Eingabe "));

		JLabel fillerA = new JLabel("");
		JLabel fillerB = new JLabel("");

		panelAntennas.add(antennaInputPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		panelAntennas.add(phasedInputPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		panelAntennas.add(fillerA, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		/** TODO Sizing */
		panelAntennas.add(kreisBild, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		kreisBild.setVisible(false);
		panelAntennas.setMaximumSize(new Dimension(DPIFixV3.screen.width / 3, DPIFixV3.screen.height));

		panelAdvanced.add(sidelobeInputPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		panelAdvanced.add(fillerB, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		/* Anzeige der Fensterfunktion */
		windowPlot.setMinimumSize(new Dimension(DPIFixV3.screen.width / 6, DPIFixV3.screen.height / 6));
		panelAdvanced.add(windowPlot, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		panelAdvanced.add(legendePanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		tabpane.addTab("Antennen", panelAntennas);
		tabpane.addTab("Nebenkeulen", panelAdvanced);
		add(tabpane);
		setVisible(true);

	}

	/**
	 * @brief Delegiert den Befehl, die Werte in den Controller zu speichern weiter
	 * @note nach dem ersten valuesToController werden unter Umständen die Werte in
	 *       der nachfolgenden Kompenente wider vergessen weil in der Zwischenzeit
	 *       ein update() ausgelöst wurde
	 */
	public void valuesToController() {
		trace.methodeCall();
		antennaInputPanel.valuesToController();
		phasedInputPanel.valuesToController();
	}

	@Override
	public void paintComponent(Graphics g) {
		trace.methodeCall();
		super.paintComponent(g);
	}

	/**
	 * @brief delegiert den update Befehl and die darauf befindenden Komponente
	 *        weiter
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj) {
		trace.methodeCall();
		antennaInputPanel.update(obs, obj);
		phasedInputPanel.update(obs, obj);
		sidelobeInputPanel.update(obs, obj);
		windowPlot.update(obs, obj);
		kreisBild.update(obs, obj);
		panelAdvanced.setEnabled(false);
		Model model = (Model) obs;
		tabpane.setEnabledAt(1, model.showLinearArrayOptions());
		kreisBild.setVisible(!model.showLinearArrayOptions());
	}

}
