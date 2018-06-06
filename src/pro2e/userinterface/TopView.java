package pro2e.userinterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pro2e.controller.Controller;
import pro2e.userinterface.input.TabbedInputPanel;
import pro2e.userinterface.plots.AntennaPicturePanel;
import pro2e.userinterface.plots.PlotPanelAmplituden;
import pro2e.userinterface.plots.TabbedPlotPanel;

public class TopView extends JPanel implements Observer, ActionListener {
	private static final long serialVersionUID = 1L;

	private TabbedPlotPanel plotPanel = new TabbedPlotPanel();
	private TabbedInputPanel inputPanel;
	private PlotPanelAmplituden plotAmplituden = new PlotPanelAmplituden();
	private AntennaPicturePanel picturePanel = new AntennaPicturePanel();
	private JButton btReset = new JButton("Reset Plot-Achsen");
	private JButton btScale = new JButton("normierte Skala");
	private JLabel lb3db = new JLabel("0");

	private JPanel containerPlots = new JPanel(new GridBagLayout());
	private JPanel containerInput = new JPanel(new GridBagLayout());

	private boolean plotScaleDb = true;

	private Controller controller;

	public TopView(Controller controller) {
		super(new GridBagLayout());
		this.controller = controller;
		inputPanel = new TabbedInputPanel(this.controller);
		// setBorder(MyBorderFactory.createMyBorder(" Antennentool "));

		containerInput.add(inputPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		containerPlots.add(plotPanel, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		/*
		 * containerPlots.add(new JLabel("3db Öffnungswinkel: "), new
		 * GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		 * GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		 * 
		 * containerPlots.add(lb3db, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
		 * GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0,
		 * 0));
		 */
		containerPlots.add(btScale, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		containerPlots.add(btReset, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		containerPlots.add(picturePanel, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		add(containerInput, new GridBagConstraints(0, 0, 1, 1, 0.3, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		add(containerPlots, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		btReset.setToolTipText("Setzt die Achsenskalierung aller Plots auf den Ursprungswert zurück");
		btScale.setToolTipText("Skala ändern");
		btReset.addActionListener(this);
		btScale.addActionListener(this);
	}

	/**
	 * @brief Leitet das update() Ereignis and die entsprechenden Komponenten weiter
	 */
	public void update(Observable obs, Object obj) {
		plotPanel.update(obs, obj);
		picturePanel.update(obs, obj);
		inputPanel.update(obs, obj);
		plotAmplituden.update(obs, obj);
		lb3db.setText(String.format("%.2f", plotPanel.get3dbAngle(obs, obj)) + "°");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e == null) {
			inputPanel.valuesToController();
		} else {
			if (e.getSource() == btReset) {
				plotPanel.resetAxis(plotScaleDb);
				plotAmplituden.resetAxis();
				inputPanel.valuesToController();
			}
			if (e.getSource() == btScale) {
				plotScaleDb = !plotScaleDb;
				if (plotScaleDb) {
					btScale.setText("normierte Skala     ");
				} else {
					btScale.setText("logarithmische Skala");
				}
				controller.setPlotScaleDb(plotScaleDb);
				plotPanel.resetAxis(plotScaleDb);
				inputPanel.valuesToController(); // neu berechnen in neuer Skala
			}
		}
	}

}
