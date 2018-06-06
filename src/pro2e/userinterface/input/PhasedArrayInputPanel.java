package pro2e.userinterface.input;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import javax.swing.event.ChangeListener;

import pro2e.TraceV1;
import pro2e.controller.Controller;
import pro2e.model.Model;
import pro2e.userinterface.MyBorderFactory;
import pro2e.userinterface.StatusBar;

public class PhasedArrayInputPanel extends JPanel
		implements KeyListener, ItemListener, ChangeListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private TraceV1 trace = new TraceV1(this);

	private Controller controller;
	private JRadioButton onPhase = new JRadioButton("On", false);
	private JRadioButton offPhase = new JRadioButton("Off", true);
	private ButtonGroup phaseOnOff;
	private JDoubleTextField tfPhaseDist = new JDoubleTextField("90", 1, false);
	private JSlider sliderPhase = new JSlider(JSlider.HORIZONTAL, 0, 180, 90);
	private JLabel lbPhase = new JLabel("Phase °");
	private JPanel pnPhasedArray = new JPanel(new GridBagLayout());

	public PhasedArrayInputPanel(Controller controller) {
		super(new GridBagLayout());
		trace.constructorCall();
		this.controller = controller;
		pnPhasedArray.setBorder(MyBorderFactory.createMyBorder(" Phased Array "));

		pnPhasedArray.add(new JLabel("Phase"), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		pnPhasedArray.add(onPhase, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pnPhasedArray.add(offPhase, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		phaseOnOff = new ButtonGroup();
		phaseOnOff.add(onPhase);
		phaseOnOff.add(offPhase);

		onPhase.addItemListener(this);
		offPhase.addItemListener(this);

		pnPhasedArray.add(lbPhase, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		pnPhasedArray.add(tfPhaseDist, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		pnPhasedArray.add(sliderPhase, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		sliderPhase.setMajorTickSpacing(45);
		sliderPhase.setMinorTickSpacing(5);
		sliderPhase.setPaintTicks(true);
		sliderPhase.setPaintLabels(true);
		sliderPhase.addChangeListener(this);
		sliderPhase.addMouseWheelListener(this);
		sliderPhase.setEnabled(false);

		tfPhaseDist.setEditable(false);
		tfPhaseDist.addKeyListener(this);
		tfPhaseDist.addMouseWheelListener(this);

		/* Tooltips */
		tfPhaseDist.setToolTipText("Abstrahlwinkel der Hauptkeule in Grad");
		onPhase.setToolTipText("Phase dazuschalten");
		offPhase.setToolTipText("Phase ausschalten");
		sliderPhase.setToolTipText(tfPhaseDist.getToolTipText());

		add(pnPhasedArray, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		setVisible(true);
	}

	/**
	 * <pre>
	 * übergibt alle Werte des Panels in den Controller
 	 * </pre>
	 */
	public void valuesToController() {
		trace.methodeCall();
		double phase = 90;
		if (onPhase.isSelected()) {
			if (!tfPhaseDist.getText().isEmpty()) {
				phase = Double.parseDouble(tfPhaseDist.getText());
			}
		}
		controller.setPhase(phase);
		sliderPhase.setValue((int) phase);
	}

	@Override
	public void paintComponent(Graphics g) {
		trace.methodeCall();
		super.paintComponent(g);
	}

	/**
	 * <pre>
	 * übernimmt die Werte aus dem Model und setzt sie aufs Userinterface
 	 * </pre>
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj) {
		trace.methodeCall();
		Model model = (Model) obs;
		// tfPhaseDist.setText(Double.toString(model.getPhase()));
		boolean show = model.showLinearArrayOptions();
		lbPhase.setEnabled(onPhase.isSelected());
		this.setVisible(show);

	}

	/**
	 * <pre>
	 * Handler für on/off Buttons
 	 * </pre>
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		trace.methodeCall();
		if (offPhase == e.getItem()) {
			StatusBar.showStatus("Phase off");
			controller.setPhase(90);
			tfPhaseDist.setEditable(false);
			sliderPhase.setEnabled(false);
		} else {
			StatusBar.showStatus("Phase on");
			tfPhaseDist.setEditable(true);
			sliderPhase.setEnabled(true);
			controller.setPhase(Double.parseDouble(tfPhaseDist.getText()));
		}
	}

	/**
	 * <pre>
	 * Handler für die JSlider
 	 * </pre>
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		trace.methodeCall();
		if (e.getSource() == sliderPhase) {
			double phase = (double) sliderPhase.getValue();
			controller.setPhase(180.0 - phase); // 180° invertieren
			tfPhaseDist.setText(Double.toString(phase));
		}
	}

	/**
	 * <pre>
	 * Handler für KeyEvents in TextFields
 	 * </pre>
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		trace.methodeCall();
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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
	 * <pre>
	 * Handler für Maus Scrollrad auf Slider und TextField
 	 * </pre>
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		trace.methodeCall();
		if (e.getWheelRotation() > 0) { // mouse wheel was rotated down
			// int newVal = sliderPhase.getValue() - sliderPhase.getMinorTickSpacing(); // -
			// 5
			int newVal = sliderPhase.getValue() - 1;
			if (newVal >= sliderPhase.getMinimum()) {
				sliderPhase.setValue(newVal);
			} else {
				sliderPhase.setValue(0);
			}
		} else { // mouse wheel was rotated up/away from the user
			// int newVal = sliderPhase.getValue() + sliderPhase.getMinorTickSpacing(); // +
			// 5
			int newVal = sliderPhase.getValue() + 1;
			if (newVal <= sliderPhase.getMaximum()) {
				sliderPhase.setValue(newVal);
			} else {
				sliderPhase.setValue(sliderPhase.getMaximum());
			}
		}
	}
}
