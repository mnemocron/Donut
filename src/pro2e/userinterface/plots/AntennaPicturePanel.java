package pro2e.userinterface.plots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pro2e.DPIFixV3;
import pro2e.TraceV1;
import pro2e.model.Antenna;
import pro2e.model.LinearAntennaArray;
import pro2e.model.Model;
import pro2e.userinterface.MyBorderFactory;
import pro2e.userinterface.Utility;

public class AntennaPicturePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TraceV1 trace = new TraceV1(this);

	private Image imgDipolZeileNoReflector = Utility.loadResourceImage("DipolZeileNoReflector.png");
	private Image imgDipolLinieNoReflector = Utility.loadResourceImage("DipolLinieNoReflector.png");
	private Image imgDipolZeileReflectorBroadside = Utility.loadResourceImage("DipolZeileReflectorBroadside.png");
	private Image imgDipolLinieReflectorBroadside = Utility.loadResourceImage("DipolLinieReflectorBroadside.png");
	private Image imgDipolZeileReflectorEndfire = Utility.loadResourceImage("DipolZeileReflectorEndfire.png");
	private Image imgDipolLinieReflectorEndfire = Utility.loadResourceImage("DipolLinieReflectorEndfire.png");

	private Image imgAusrichtung;

	private JLabel lbImage = new JLabel();

	public AntennaPicturePanel() {
		super(new GridBagLayout());
		trace.constructorCall();
		setMaximumSize(new Dimension(DPIFixV3.screen.width / 10, DPIFixV3.screen.height / 100));
		setBorder(MyBorderFactory.createMyBorder(" Anordnung "));

		JPanel background = new JPanel(new GridBagLayout());
		background.setBackground(Color.WHITE);
		add(background, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		imgAusrichtung = imgDipolLinieNoReflector;
		ImageIcon imageIcon = new ImageIcon(imgAusrichtung.getScaledInstance((int) DPIFixV3.screen.getWidth() / 6,
				(int) DPIFixV3.screen.getHeight() / 12, Image.SCALE_SMOOTH));

		lbImage = new JLabel(imageIcon, JLabel.CENTER); // muss hier einmal instanziert werden, damit das Image im
														// CENTER ist

		background.add(lbImage, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		lbImage.setToolTipText("Räumliche Anordnung der Strahler und Reflektoren");

		setVisible(true);

	}

	private double getAspectRatio(Image im) {
		int width = im.getWidth(null);
		int height = im.getHeight(null);
		return (double) width / (double) height;
	}

	private double getAspectRatio(JLabel lb) {
		int width = lb.getWidth();
		int height = lb.getHeight();
		return (double) width / (double) height;
	}

	private ImageIcon resizedIcon() {
		double imageAspect = getAspectRatio(imgAusrichtung);
		double labelAspect = getAspectRatio(lbImage);
		int newWidth = 0;
		int newHeight = lbImage.getHeight();
		if (labelAspect < imageAspect) { // Label Width wird zu klein für das Image --> Image Height muss auch angepasst
											// werden
			newWidth = (int) (lbImage.getHeight() * labelAspect);
			newHeight = (int) (newWidth * (1.0 / imageAspect));
		} else {
			newWidth = (int) (lbImage.getHeight() * imageAspect);
		}
		ImageIcon imageIcon = new ImageIcon(imgAusrichtung.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
		return imageIcon;
	}

	@Override
	public void paintComponent(Graphics g) {
		trace.methodeCall();
		super.paintComponent(g);
		lbImage.setIcon(resizedIcon());
	}

	/**
	 * <pre>
	 * setzt das passende Bild zu der ausgewählten Antennengeometrie
 	 * </pre>
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj) {
		trace.methodeCall();
		Model model = (Model) obs;

		int abstrahlung = model.getAntArray().getAntenne().getAbstrahlung();
		boolean reflektor = model.getAntArray().isReflektor();
		int reflektorPosition = model.getAntArray().getReflektorPosition();
		if (reflektor == true) {
			if (abstrahlung == Antenna.ISOTROP) {
				if (reflektorPosition == LinearAntennaArray.POSITION_BROADSIDE) {
					imgAusrichtung = imgDipolZeileReflectorBroadside;
				}
				if (reflektorPosition == LinearAntennaArray.POSITION_ENDFIRE) {
					imgAusrichtung = imgDipolZeileReflectorEndfire;
				}
			}
			if (abstrahlung == Antenna.LAMBERT || abstrahlung == Antenna.HALBWELLENDIPOL
					|| abstrahlung == Antenna.HERTZDIPOL) {
				if (reflektorPosition == LinearAntennaArray.POSITION_BROADSIDE) {
					imgAusrichtung = imgDipolLinieReflectorBroadside;
				}
				if (reflektorPosition == LinearAntennaArray.POSITION_ENDFIRE) {
					imgAusrichtung = imgDipolLinieReflectorEndfire;
				}
			}
		} else { // no reflektor
			if (abstrahlung == Antenna.ISOTROP) {
				imgAusrichtung = imgDipolZeileNoReflector;
			}
			if (abstrahlung == Antenna.LAMBERT || abstrahlung == Antenna.HALBWELLENDIPOL
					|| abstrahlung == Antenna.HERTZDIPOL) {
				imgAusrichtung = imgDipolLinieNoReflector;
			}
		}
		setVisible(model.showLinearArrayOptions());

		repaint();
	}
}
