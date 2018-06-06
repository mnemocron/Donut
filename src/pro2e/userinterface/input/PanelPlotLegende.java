package pro2e.userinterface.input;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pro2e.DonutFramework;
import pro2e.TraceV1;
import pro2e.userinterface.MyBorderFactory;

public class PanelPlotLegende extends JPanel {
	private static final long serialVersionUID = 1L;
	private TraceV1 trace = new TraceV1(this);

	public PanelPlotLegende() {
		super(new GridBagLayout());
		trace.constructorCall();
		setBorder(MyBorderFactory.createMyBorder(""));

		JLabel lbGray = new JLabel("grau");
		lbGray.setForeground(Color.GRAY);
		JLabel lbBlack = new JLabel("schwarz");
		lbBlack.setForeground(Color.BLACK);
		JLabel lbBlue = new JLabel("rot");
		lbBlue.setForeground(DonutFramework.Colors.Pink);

		add(lbGray, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		add(new JLabel("Fensterfunktion"), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		add(lbBlack, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		add(new JLabel("Amplituden"), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		add(lbBlue, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		add(new JLabel("diskretisierte Amplituden"), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		setVisible(true);

	}

	@Override
	public void paintComponent(Graphics g) {
		trace.methodeCall();
		super.paintComponent(g);
	}

}
