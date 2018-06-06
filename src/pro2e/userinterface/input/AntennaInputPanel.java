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
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pro2e.TraceV1;
import pro2e.controller.Controller;
import pro2e.model.Antenna;
import pro2e.model.LinearAntennaArray;
import pro2e.model.Model;
import pro2e.userinterface.MyBorderFactory;
import pro2e.userinterface.StatusBar;

public class AntennaInputPanel extends JPanel implements ActionListener, ItemListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private TraceV1 trace = new TraceV1(this);

	private Controller controller;
	private JDoubleTextField tfDLam = new JDoubleTextField("0.5", 1, false);
	private JDoubleTextField tfNAnt = new JDoubleTextField("8", 1, false);
	// private String antennaTypeListe[] = { "Dipol Zeile", "Lambert", "hertzscher
	// Dipol Linie", "λ/2 Dipol Linie" };
	private String antennaTypeListe[] = { "Dipol Zeile", "hertzscher Dipol Linie", "λ/2 Dipol Linie" };
	private JComboBox<Object> selectAntennaType = new JComboBox<Object>(antennaTypeListe);

	private String reflektorPosListe[] = { "parallel", "senkrecht" };
	private JComboBox<Object> selectReflektorPos = new JComboBox<Object>(reflektorPosListe);

	private String antennenGeometrieListe[] = { "linear", "Kreis" };
	private JComboBox<Object> selectGeometrie = new JComboBox<Object>(antennenGeometrieListe);

	private JRadioButton onReflektor = new JRadioButton("On", false);
	private JRadioButton offReflektor = new JRadioButton("Off", true);
	private ButtonGroup reflektorOnOff;
	private JDoubleTextField tfReflDist = new JDoubleTextField("0.25", 1, false);

	private JLabel lbGeometrie = new JLabel("Arraygeometrie");
	private JLabel lbAntAbstand = new JLabel("Antennenabstand d/λ");
	private JLabel lbRefAbstand = new JLabel("Reflektor Abstand  d/λ");
	private JLabel lbNAnt = new JLabel("Anzahl Antennen");
	private JLabel lbAusrichtung = new JLabel("Ausrichtung");
	private JLabel lbReflektor = new JLabel("Reflektor");
	private JLabel lbPosition = new JLabel("Position");

	private JPanel pnAntenna = new JPanel(new GridBagLayout());
	private JPanel pnReflect = new JPanel(new GridBagLayout());

	public AntennaInputPanel(Controller controller) {
		super(new GridBagLayout());
		trace.constructorCall();
		this.controller = controller;

		pnAntenna.setBorder(MyBorderFactory.createMyBorder(" Antenne "));
		pnReflect.setBorder(MyBorderFactory.createMyBorder("Reflektor"));

		pnAntenna.add(lbGeometrie, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		pnAntenna.add(selectGeometrie, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		pnAntenna.add(lbAntAbstand, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		pnAntenna.add(tfDLam, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		pnAntenna.add(lbNAnt, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		pnAntenna.add(tfNAnt, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		pnAntenna.add(lbAusrichtung, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		pnAntenna.add(selectAntennaType, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		pnReflect.add(lbReflektor, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		pnReflect.add(onReflektor, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pnReflect.add(offReflektor, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		reflektorOnOff = new ButtonGroup();
		reflektorOnOff.add(onReflektor);
		reflektorOnOff.add(offReflektor);

		onReflektor.addItemListener(this);
		offReflektor.addItemListener(this);

		pnReflect.add(lbPosition, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		pnReflect.add(selectReflektorPos, new GridBagConstraints(1, 6, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		selectReflektorPos.setEnabled(false);

		pnReflect.add(lbRefAbstand, new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		pnReflect.add(tfReflDist, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		tfReflDist.setEditable(false);

		tfDLam.addKeyListener(this);
		tfNAnt.addKeyListener(this);
		tfReflDist.addKeyListener(this);
		selectAntennaType.addActionListener(this);
		selectReflektorPos.addActionListener(this);
		selectGeometrie.addActionListener(this);

		/* Tooltips */
		selectGeometrie.setToolTipText("geometrische Anordnung der einzelnen Strahler");
		tfNAnt.setToolTipText("Anzahl der einzelnen Strahler");
		selectAntennaType.setToolTipText("Abstrahlungstyp eines einzelnen Strahlers");
		tfDLam.setToolTipText(
				"Einheitsloses Verhältniss zwischen der Wellenlänge und dem Abstand der einzelnen Strahler");
		tfNAnt.setToolTipText("Anzahl einzelner Strahler im Array");
		tfReflDist.setToolTipText(
				"Einheitsloses Verhältniss zwischen der Wellenlänge und dem Abstand des Reflektors zum Array");
		selectAntennaType.setToolTipText("2D Abstrahlcharakteristik eines einzelnen Strahlers");
		selectReflektorPos.setToolTipText("Position des Reflektors zum Array");
		onReflektor.setToolTipText("Reflektor hinzufügen");
		offReflektor.setToolTipText("Reflektor entfernen");
		lbAntAbstand.setToolTipText(tfDLam.getToolTipText());
		lbNAnt.setToolTipText(tfNAnt.getToolTipText());
		lbRefAbstand.setToolTipText(tfReflDist.getToolTipText());
		lbAusrichtung.setToolTipText(selectAntennaType.getToolTipText());
		lbPosition.setToolTipText(selectReflektorPos.getToolTipText());
		lbGeometrie.setToolTipText(selectGeometrie.getToolTipText());

		add(pnAntenna, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));
		add(pnReflect, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));

		setVisible(true);

	}

	private int getSelectedPosition() {
		int index = selectReflektorPos.getSelectedIndex();
		switch (index) {
		case 0:
			return LinearAntennaArray.POSITION_BROADSIDE;
		default:
			return LinearAntennaArray.POSITION_ENDFIRE;
		}
	}

	/**
	 * @brief übergibt alle Werte des Panels in den Controller
	 */
	public void valuesToController() {
		trace.methodeCall();
		int type = getSelectedAntenna();
		double dLam = 0.0;
		if (!tfDLam.getText().isEmpty()) {
			dLam = Double.parseDouble(tfDLam.getText());
		}
		int nAnt = 1;
		if (!tfNAnt.getText().isEmpty()) {
			nAnt = (int) Double.parseDouble(tfNAnt.getText());
		}
		double dLamRef = 0.25;
		if (!tfReflDist.getText().isEmpty()) {
			dLamRef = Double.parseDouble(tfReflDist.getText());
		}
		controller.setAntenna(selectGeometrie.getSelectedIndex(), dLam, // Verhältnis d/Lam
				nAnt, // Abstand Antenne
				type, // Abstrahltyp
				onReflektor.isSelected(), // Reflektor on/off
				dLamRef, // Reflektor Abstand
				getSelectedPosition()); // Reflektor Position
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
		tfNAnt.setText(Integer.toString(model.getNAnt()));
		tfDLam.setText(Double.toString(model.getDLam()));
		tfReflDist.setText(Double.toString(model.getdLamReflektor()));
		boolean show = model.showLinearArrayOptions();
		onReflektor.setVisible(show);
		offReflektor.setVisible(show);
		selectReflektorPos.setVisible(show);
		tfReflDist.setVisible(show);
		lbReflektor.setVisible(show);
		lbRefAbstand.setVisible(show);
		lbPosition.setVisible(show);
		if (show) {
			lbAntAbstand.setText("Antennenabstand d/λ");
		} else {
			lbAntAbstand.setText("Radius r/λ");
		}
	}

	private int getSelectedAntenna() {
		int type = selectAntennaType.getSelectedIndex();
		switch (type) {
		case 0:
			return Antenna.ISOTROP;
		// case 1:
		// return Antenna.LAMBERT;
		case 1:
			return Antenna.HERTZDIPOL;
		case 2:
			return Antenna.HALBWELLENDIPOL;
		default:
			return Antenna.ISOTROP;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		trace.methodeCall();
		controller.setAntennaType(getSelectedAntenna());
		valuesToController();
	}

	/**
	 * @brief Handler für on/off Buttons
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		trace.methodeCall();
		if (offReflektor == e.getItem()) {
			StatusBar.showStatus("Reflektor off");
			tfReflDist.setEditable(false);
			selectReflektorPos.setEnabled(false);
		} else {
			StatusBar.showStatus("Reflektor on");
			tfReflDist.setEditable(true);
			selectReflektorPos.setEnabled(true);
		}
		valuesToController();
	}

	/**
	 * Handler für Tastenevents in TextFields
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

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
