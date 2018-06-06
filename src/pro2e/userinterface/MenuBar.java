package pro2e.userinterface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import pro2e.DonutFramework;
import pro2e.controller.Controller;

public class MenuBar extends JMenuBar implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;

	JMenu tutorialMenu, help;
	JMenuItem showTutorial = new JMenuItem("Tutorial anzeigen");
	JMenuItem showAbout = new JMenuItem("About");
	JCheckBoxMenuItem checkTutorialStartup = new JCheckBoxMenuItem("beim aufstarten anzeigen");

	DonutFramework frame;
	Controller controller;

	public MenuBar(Controller controller, DonutFramework frame) {
		this.frame = frame;
		this.controller = controller;
		help = new JMenu("Hilfe");
		help.setMnemonic(KeyEvent.VK_H);

		// menu.addSeparator();
		tutorialMenu = new JMenu("Tutorial");
		tutorialMenu.setMnemonic(KeyEvent.VK_T);
		tutorialMenu.add(showTutorial);
		tutorialMenu.add(checkTutorialStartup);
		checkTutorialStartup.setSelected(frame.getShowOnStartup());
		help.add(tutorialMenu);
		help.add(showAbout);
		showAbout.addActionListener(this);

		showTutorial.addActionListener(this);
		checkTutorialStartup.addActionListener(this);

		add(help);
	}

	public void update(Observable o, Object obj) {
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == showAbout) {
			BufferedImage logo = null;
			try {
				logo = ImageIO.read(DonutFramework.class.getResourceAsStream("Icon.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			ImageIcon logoIcon = new ImageIcon(logo);

			Object[] message = { "Willkommen beim Donut Antennentool", logoIcon, "Projektleiter:", "- Dagelet Patrice",
					new JLabel(), "Programmierer:", "- Burkhardt Simon", "- Stadlin Christian", new JLabel(),
					"Mathematiker:", "- Enderlin Stefan", "- Studer Mischa", "- Frey Fabian", "- Läderach Reto",
					new JLabel(), "(c) 2018 - Fachhochschule Nordwestschweiz" };
			JOptionPane aboutDialog = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE);

			JDialog dialog = aboutDialog.createDialog(null, "About");
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			dialog.setLocation(screenSize.width / 3, screenSize.height / 3);
			dialog.setVisible(true);
		}
		if (source == showTutorial) {
			frame.showTutorial();
		}
		if (source == checkTutorialStartup) {
			frame.setShowOnStartup(checkTutorialStartup.isSelected());
			System.out.println(checkTutorialStartup.isSelected());
		}
	}
}
