package pro2e.userinterface.input;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
//import java.util.Hashtable;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import javax.swing.event.ChangeListener;

import pro2e.TraceV1;
import pro2e.controller.Controller;
import pro2e.matlabfunctions.Fensterfunktionen;
import pro2e.model.Model;
import pro2e.userinterface.MyBorderFactory;
import pro2e.userinterface.StatusBar;

public class SidelobeInputPanel extends JPanel
		implements ActionListener, ItemListener, ChangeListener, KeyListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private TraceV1 trace = new TraceV1(this);

	private Controller controller;
	private JRadioButton onSidelobe = new JRadioButton("On", false);
	private JRadioButton offSidelobe = new JRadioButton("Off", true);
	private ButtonGroup sidelobeOnOff = new ButtonGroup();
	private String windowTypeListe[] = { "Rechteck (keine)", "Dreieck", "Exponential", "Cosinus", "Cosinus-quadrat",
			"Dolph-Chebyshev", "Binomial" };
	private JComboBox<Object> selectWindow = new JComboBox<Object>(windowTypeListe);

	private JLabel lbChebyAtten = new JLabel("Dämpfung dB"); // als Objekt - damit es invisible gesetzt werden kann
	private JDoubleTextField tfChebyAtten = new JDoubleTextField("20.0", 1, false);
	private JSlider sliderCheby = new JSlider(JSlider.HORIZONTAL, 0, 60, 20);

	private JLabel lbCosAtten = new JLabel("Offset"); // als Objekt - damit es invisible gesetzt werden kann
	private JDoubleTextField tfCosAtten = new JDoubleTextField("0.5", 1, false);
	private JSlider sliderCosin = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

	private JRadioButton onQuantize = new JRadioButton("On", false);
	private JRadioButton offQuantize = new JRadioButton("Off", true);
	private ButtonGroup quantizeOnOff = new ButtonGroup();
	private String bitrateListe[] = { "   2 Bit", "   3 Bit", "   4 Bit", "   5 Bit", "   8 Bit" };
	private JComboBox<Object> selectBitrate = new JComboBox<Object>(bitrateListe);

	public SidelobeInputPanel(Controller controller) {
		super(new GridBagLayout());
		trace.constructorCall();
		this.controller = controller;
		// setPreferredSize(new Dimension(DPIFixV3.screen.width / 4,
		// DPIFixV3.screen.height / 2));
		setBorder(MyBorderFactory.createMyBorder(" Nebenkeulen "));

		/* Fensterfunktionen */
		add(new JLabel("Unterdrückung"), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		add(onSidelobe, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		add(offSidelobe, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		add(new JLabel("Fensterfunktion"), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		add(selectWindow, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		selectWindow.setEnabled(false); // disabled at startup
		selectWindow.addActionListener(this); // react to change of selection
		sidelobeOnOff.add(onSidelobe);
		sidelobeOnOff.add(offSidelobe);
		// onSidelobe.addItemListener(this); // es braucht nur 1 Listener, da beide
		// Buttons verbunden sind
		offSidelobe.addItemListener(this); // wird auf offSidelobe registrierd, da im Handler nur dieser Button
											// abgefragt wird
											// es kann sonst zu doppelten Events kommen weil beide Buttons den State
											// wechseln

		/* Parameter für Fensterfunktionen */
		add(lbChebyAtten, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0)); // am selben Ort wie vv
		add(lbCosAtten, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0)); // am selben Ort wie ^^
		lbChebyAtten.setVisible(false); // default nicht visible
		lbCosAtten.setVisible(false); // default nicht visible
		add(tfChebyAtten, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		add(tfCosAtten, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		tfChebyAtten.setEnabled(false);
		tfCosAtten.setEnabled(false);
		tfChebyAtten.setVisible(false);
		tfCosAtten.setVisible(false);
		add(sliderCheby, new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		sliderCheby.setMajorTickSpacing(10); // pro 10 dB
		sliderCheby.setMinorTickSpacing(2); // pro 2 dB
		sliderCheby.setPaintTicks(true);
		sliderCheby.setPaintLabels(true);
		sliderCheby.addChangeListener(this);
		sliderCheby.addMouseWheelListener(this);
		sliderCheby.setEnabled(false);
		sliderCheby.setVisible(false);
		// JSlider kann nur Integer. Darum ein slider von 0 bis 100 mit entsprechenden
		// Labels von 0.00 bis 1.00
		// Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		// labelTable.put( new Integer( 0 ), new JLabel("0.0") );
		// labelTable.put( new Integer( 50 ), new JLabel("0.5") );
		// labelTable.put( new Integer( 100 ), new JLabel("1.0") );
		// sliderCosin.setLabelTable( labelTable );
		/**
		 * @BUG Der Code oben verursacht Probleme mit dem JSlider. Der JSlider ist dann
		 *      hinter anderen Elementen. Vermutlich ist dieser Code auch verantwortlich
		 *      für das Stottern auf einen 0-Wert zwischen 99 und 100
		 */
		add(sliderCosin, new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		// sliderCosin.setMajorTickSpacing(10); /** @BUG ^^ darum werden gar keine
		// Wertlabels angezeigt */
		sliderCosin.setMinorTickSpacing(2);
		sliderCosin.setMinorTickSpacing(10);
		sliderCosin.setPaintTicks(true);
		sliderCosin.setPaintLabels(true);
		sliderCosin.addChangeListener(this);
		sliderCosin.addMouseWheelListener(this);
		sliderCosin.setEnabled(false);
		sliderCosin.setVisible(false);

		/* Quantisierung */
		add(new JLabel("Diskretisierung"), new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		add(onQuantize, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		add(offQuantize, new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		add(new JLabel("Wertebereich"), new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		add(selectBitrate, new GridBagConstraints(1, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		onQuantize.setEnabled(false);
		offQuantize.setEnabled(false);
		selectBitrate.setEnabled(false);
		selectBitrate.addActionListener(this);
		quantizeOnOff.add(onQuantize);
		quantizeOnOff.add(offQuantize);
		// onQuantize.addItemListener(this); // es braucht nur 1 Listener, da beide
		// Buttons verbunden sind
		offQuantize.addItemListener(this); // wird auf offSidelobe registrierd, da im Handler nur dieser Button
											// abgefragt wird
											// es kann sonst zu doppelten Events kommen weil beide Buttons den State
											// wechseln
		tfChebyAtten.addKeyListener(this);
		tfCosAtten.addKeyListener(this);
		tfChebyAtten.addMouseWheelListener(this);
		tfCosAtten.addMouseWheelListener(this);

		/* Tooltips */
		// tfChebyAtten.setToolTipText("Dämpfung aller Nebenkeulen in dB");
		tfCosAtten.setToolTipText("Parameter zum Anpassen der Fensterfunktion");
		selectBitrate.setToolTipText("Auflösung in Bit für die Diskretisierung");
		onQuantize.setToolTipText("Fensterfunktion auf einen wertdiskreten Bereich anpassen");
		offQuantize.setToolTipText("Diskretisierung ausschalten");
		onSidelobe.setToolTipText("Nebenkeulenunterdrückung einschalten");
		offSidelobe.setToolTipText("Nebenkeulenunterdrückung ausschalten");
		sliderCosin.setToolTipText(tfCosAtten.getToolTipText());
		selectWindow.setToolTipText("Fensterfunktion die auf die Amplituden der einzelnen Strahler angewendet wird");

		setVisible(true);
	}

	/**
	 * @brief übergibt alle Werte des Panels in den Controller
	 */
	public void valuesToController() {
		trace.methodeCall();
		if (offSidelobe.isSelected()) {
			controller.setWindow(Fensterfunktionen.RECTANGULAR);
		} else {
			double value = 0;
			int window = getSelectedWindow();
			if (window == Fensterfunktionen.CHEBYSHEV || window == Fensterfunktionen.EXPONENTIAL) {
				if (!tfChebyAtten.getText().isEmpty()) {
					value = Double.parseDouble(tfChebyAtten.getText());
				}
				sliderCheby.setValue((int) value);
			}
			if (window == Fensterfunktionen.COSINE || window == Fensterfunktionen.COSINESQUARE) {
				if (!tfCosAtten.getText().isEmpty()) {
					value = Double.parseDouble(tfCosAtten.getText());
				}
				// sliderCosin.setValue((int) value);
			}
			controller.setWindowParam(window, value);
		}
	}

	private int getSelectedWindow() {
		int index = selectWindow.getSelectedIndex();
		switch (index) {
		case 0:
			return Fensterfunktionen.RECTANGULAR;
		case 1:
			return Fensterfunktionen.TRIANGULAR;
		case 2:
			return Fensterfunktionen.EXPONENTIAL;
		case 3:
			return Fensterfunktionen.COSINE;
		case 4:
			return Fensterfunktionen.COSINESQUARE;
		case 5:
			return Fensterfunktionen.CHEBYSHEV;
		case 6:
			return Fensterfunktionen.BINOMIAL;
		default:
			return Fensterfunktionen.RECTANGULAR;
		}
	}

	private int getSelectedBits() {
		switch (selectBitrate.getSelectedIndex()) {
		case 0:
			StatusBar.showStatus("Discretisation: 2 Bit");
			return 2;
		case 1:
			StatusBar.showStatus("Discretisation: 3 Bit");
			return 3;
		case 2:
			StatusBar.showStatus("Discretisation: 4 Bit");
			return 4;
		case 3:
			StatusBar.showStatus("Discretisation: 5 Bit");
			return 5;
		default:
			StatusBar.showStatus("Discretisation: 8 Bit");
			return 8;
		}
	}

	private void visibilityHandler() {
		if (offQuantize.isSelected()) {
			selectBitrate.setEnabled(false);
		} else {
			selectBitrate.setEnabled(true);
		}
		if (offSidelobe.isSelected()) {
			selectWindow.setEnabled(false);
			offQuantize.setEnabled(false);
			onQuantize.setEnabled(false);
			sliderCheby.setEnabled(false);
			sliderCosin.setEnabled(false);
			tfChebyAtten.setEnabled(false);
			tfCosAtten.setEditable(false);
			lbChebyAtten.setEnabled(false);
			lbCosAtten.setEnabled(false);
			selectBitrate.setEnabled(false);
		} else {
			selectWindow.setEnabled(true);
			offQuantize.setEnabled(true);
			onQuantize.setEnabled(true);
			sliderCheby.setEnabled(true);
			sliderCosin.setEnabled(true);
			tfChebyAtten.setEnabled(true);
			tfCosAtten.setEditable(true);
			lbChebyAtten.setEnabled(true);
			lbCosAtten.setEnabled(true);
		}
		int window = getSelectedWindow();
		if (window == Fensterfunktionen.CHEBYSHEV || window == Fensterfunktionen.EXPONENTIAL) {
			lbChebyAtten.setVisible(true);
			tfChebyAtten.setVisible(true);
			sliderCheby.setVisible(true);
			if (window == Fensterfunktionen.CHEBYSHEV) {
				tfChebyAtten.setToolTipText("Dämpfung aller Nebenkeulen in dB");
				sliderCheby.setToolTipText(tfChebyAtten.getToolTipText());
			} else {
				tfChebyAtten.setToolTipText("Verändert die Steigung der Fensterfunktion");
				sliderCheby.setToolTipText(tfChebyAtten.getToolTipText());
			}
		} else {
			lbChebyAtten.setVisible(false);
			tfChebyAtten.setVisible(false);
			sliderCheby.setVisible(false);
		}
		if (window == Fensterfunktionen.COSINE || window == Fensterfunktionen.COSINESQUARE) {
			lbCosAtten.setVisible(true);
			tfCosAtten.setVisible(true);
			tfCosAtten.setEnabled(true);
			sliderCosin.setVisible(true);
		} else {
			lbCosAtten.setVisible(false);
			tfCosAtten.setVisible(false);
			tfCosAtten.setEnabled(false);
			sliderCosin.setVisible(false);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		trace.methodeCall();
		super.paintComponent(g);
	}

	/**
	 * @brief übernimmt die Werte aus dem Model und setzt sie aufs Userinterface
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj) {
		trace.methodeCall();
		Model model = (Model) obs;
		// System.out.println("[update] " + model.getWindowParam() + " " +
		// tfCosAtten.getText());
		if (getSelectedWindow() == Fensterfunktionen.COSINE || getSelectedWindow() == Fensterfunktionen.COSINESQUARE) {
			tfCosAtten.setText(Double.toString(model.getWindowParam()));
		} else {
			sliderCheby.setValue((int) model.getWindowParam());
			tfChebyAtten.setText(Double.toString(model.getWindowParam()));
		}

	}

	/**
	 * @brief Handler für KeyEvents in TextFields
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		trace.methodeCall();
		if (e.getKeyCode() == KeyEvent.VK_ENTER) { // hört nur auf ENTER
			valuesToController();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * @brief Handler für die JSlider
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		trace.methodeCall();
		int window = getSelectedWindow();
		if (window == Fensterfunktionen.CHEBYSHEV || window == Fensterfunktionen.EXPONENTIAL) {
			double value = (double) sliderCheby.getValue();
			controller.setWindowParam(window, value);
			tfChebyAtten.setText(Double.toString(value));
		}
		if (window == Fensterfunktionen.COSINE || window == Fensterfunktionen.COSINESQUARE) {
			double value = (double) sliderCosin.getValue();
			if (value != 56) {
				value /= 100;
				controller.setWindowParam(window, value);
				tfCosAtten.setText(Double.toString(value));
			}
		}
	}

	/**
	 * @brief Handler für on/off Buttons
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		trace.methodeCall();
		Object source = e.getSource();
		if (source == offSidelobe) {
			if (offSidelobe.isSelected()) {
				StatusBar.showStatus("Sidelobe surpression: OFF");
				controller.setWindow(Fensterfunktionen.RECTANGULAR);
			} else {
				StatusBar.showStatus("Sidelobe surpression: ON");
			}
		}
		if (source == offQuantize) { // Diskretisierung einschalten
			if (offQuantize.isSelected()) {
				StatusBar.showStatus("Discretisation: OFF");
				controller.setDiskretisierung(false);
			} else {
				StatusBar.showStatus("Discretisation: ON");
				controller.setDiskretisierung(true, getSelectedBits());
				StatusBar.showStatus("Bitrange: " + getSelectedBits());
			}
		}
		visibilityHandler();
		valuesToController();
	}

	@Override /* Drop Down Menu */
	public void actionPerformed(ActionEvent e) {
		trace.methodeCall();
		Object source = e.getSource();
		if (source == selectWindow) {
			int window = getSelectedWindow();
			switch (window) {
			case Fensterfunktionen.RECTANGULAR:
				StatusBar.showStatus("Window: rectangular");
				controller.setWindow(Fensterfunktionen.RECTANGULAR);
				break;
			case Fensterfunktionen.TRIANGULAR:
				StatusBar.showStatus("Window: triangular");
				controller.setWindow(Fensterfunktionen.TRIANGULAR);
				break;
			case Fensterfunktionen.EXPONENTIAL:
				StatusBar.showStatus("Window: exponential");
				controller.setWindowParam(Fensterfunktionen.EXPONENTIAL, 20);
				lbChebyAtten.setText("Steigung");
				break;
			case Fensterfunktionen.COSINE:
				StatusBar.showStatus("Window: cosine");
				controller.setWindowParam(Fensterfunktionen.COSINE, 0.5);
				sliderCosin.setValue(50);
				break;
			case Fensterfunktionen.COSINESQUARE:
				StatusBar.showStatus("Window: cosine square");
				controller.setWindowParam(Fensterfunktionen.COSINESQUARE, 0.5);
				sliderCosin.setValue(50);
				break;
			case Fensterfunktionen.CHEBYSHEV:
				StatusBar.showStatus("Window: chebyshev");
				controller.setWindow(Fensterfunktionen.CHEBYSHEV);
				tfChebyAtten.setText("20.0"); // Initialwert
				controller.setWindowParam(Fensterfunktionen.CHEBYSHEV, 20);
				lbChebyAtten.setText("Dämpfung dB");
				sliderCheby.setValue(20);
				break;
			case Fensterfunktionen.BINOMIAL:
				StatusBar.showStatus("Window: binomial");
				controller.setWindow(Fensterfunktionen.BINOMIAL);
				break;
			default:
				StatusBar.showStatus("Window: rectangular");
				controller.setWindow(Fensterfunktionen.RECTANGULAR);
				break;
			}
		}
		if (source == selectBitrate) {
			controller.setDiskretisierung(true, getSelectedBits());
			StatusBar.showStatus("Bitrange: " + getSelectedBits());
		}
		visibilityHandler();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		trace.methodeCall();
		Object source = e.getSource();
		if (source == sliderCheby || source == tfChebyAtten) {
			if (e.getWheelRotation() > 0) { // mouse wheel was rotated down
				// int newVal = sliderCheby.getValue() - sliderPhase.getMinorTickSpacing(); // -
				int newVal = sliderCheby.getValue() - 1;
				if (newVal >= sliderCheby.getMinimum()) {
					sliderCheby.setValue(newVal);
				} else {
					sliderCheby.setValue(0);
				}
			} else { // mouse wheel was rotated up/away from the user// mouse wheel was rotated down
				// int newVal = sliderCheby.getValue() + sliderCheby.getMinorTickSpacing(); // +
				int newVal = sliderCheby.getValue() + 1;
				if (newVal <= sliderCheby.getMaximum()) {
					sliderCheby.setValue(newVal);
				} else {
					sliderCheby.setValue(sliderCheby.getMaximum());
				}
			}
		}
		if (source == sliderCosin || source == tfCosAtten) {
			if (e.getWheelRotation() > 0) { // mouse wheel was rotated down
				int newVal = sliderCosin.getValue() - 1;
				if (newVal >= sliderCosin.getMinimum()) {
					sliderCosin.setValue(newVal);
				} else {
					sliderCosin.setValue(0);
				}
			} else { // mouse wheel was rotated up/away from the user// mouse wheel was rotated down
				int newVal = sliderCosin.getValue() + 1;
				sliderCosin.setValue(newVal);
				if (newVal <= sliderCosin.getMaximum()) {
					sliderCosin.setValue(newVal);
				} else {
					sliderCosin.setValue(sliderCosin.getMaximum());
				}
			}
		}
	}
}
