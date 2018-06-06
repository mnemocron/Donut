package pro2e;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import pro2e.controller.Controller;
import pro2e.model.Model;
import pro2e.userinterface.MenuBar;
import pro2e.userinterface.TopView;
import pro2e.userinterface.tutorial.SwingFXWebView;

public class DonutFramework extends JFrame {
	private static final long serialVersionUID = 1L;
	// private TraceV1 trace = new TraceV1(this);
	private static final String SHOW_TUTORIAL = "show_tutorial";

	private enum Mode {
		FIXED, PACKED, FIXEDRESIZABLE, PACKEDRESIZABLE
	};

	private Mode mode = Mode.PACKEDRESIZABLE;
	private int width = 1200, height = 800;
	private Model model = new Model();
	private Controller controller = new Controller(model, this);
	private TopView view = new TopView(controller);
	private MenuBar menuBar = new MenuBar(controller, this);

	public static class Colors {
		public static final Color Pink = new Color(0xFF4085);
		public static final Color Beige = new Color(0xFFD170);
		public static final Color Blue = new Color(0x63B6E6);
		public static final Color Green = new Color(0x84E060);
		public static final Color Purple = new Color(0x9729CC);
	}

	public void init() {
		model.addObserver(view);
		System.out.println("view @" + view.hashCode());
		model.addObserver(menuBar);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(view, BorderLayout.CENTER);
		setJMenuBar(menuBar);

		DPIFixV3.scaleSwingFonts();
		SwingUtilities.updateComponentTreeUI(this);
		pack();

		switch (mode) {
		case FIXED:
			setMinimumSize(getPreferredSize());
			setSize(width, height);
			setResizable(false);
			validate();
			break;
		case FIXEDRESIZABLE:
			setMinimumSize(getPreferredSize());
			setSize(width, height);
			setResizable(true);
			validate();
			break;
		case PACKED:
			setMinimumSize(getPreferredSize());
			setResizable(false);
			break;
		case PACKEDRESIZABLE:
			setMinimumSize(getPreferredSize());
			setResizable(true);
			break;
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		view.actionPerformed(null); // erstes Update ausl�sen um einen Graphen anzuzeigen
	}

	/**
	 * <pre>
	 * speichert den Wert, ob das Tutorial beim Programmstart angezeigt
	 * werden soll im System ab
	 * </pre>
	 * 
	 * @param value
	 */
	public void setShowOnStartup(boolean value) {
		Preferences prefs = Preferences.userNodeForPackage(DonutFramework.class);
		prefs.putBoolean(SHOW_TUTORIAL, value);
	}

	/**
	 * <pre>
	 * gibt den im System gespeicherten Wert zurück, ob das Tutorial beim
	 * Programmstart angezeigt werden soll
	 * </pre>
	 * 
	 * @return
	 */
	public boolean getShowOnStartup() {
		Preferences prefs = Preferences.userNodeForPackage(DonutFramework.class);
		return prefs.getBoolean(SHOW_TUTORIAL, true);
	}

	/**
	 * <pre>
	 * öffnet das Tutorial frame
	 * </pre>
	 */
	public void showTutorial() {
		JFrame tutorial = new JFrame();
		tutorial.setTitle("Tutorial");
		try {
			tutorial.setIconImage(ImageIO.read(DonutFramework.class.getResourceAsStream("Icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		tutorial.setSize(screenSize.width / 2, screenSize.height / 5 * 4);
		tutorial.setLocation((screenSize.width) / 4, (screenSize.height) / 7);
		tutorial.setMinimumSize(new Dimension(screenSize.width / 5, screenSize.height / 5));

		SwingFXWebView tutorialWindow = new SwingFXWebView();

		tutorial.add(tutorialWindow);
		tutorial.setVisible(true);
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				DonutFramework frame = new DonutFramework();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setTitle("Donut Antennentool");
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int width = screenSize.width;
				int height = screenSize.height;
				frame.setPreferredSize(new Dimension(2 * width / 5, 3 * height / 5));

				try {
					frame.setIconImage(ImageIO.read(DonutFramework.class.getResourceAsStream("Icon.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}

				frame.init();
				frame.setVisible(true);

				// frame.setShowOnStartup(true);

				if (frame.getShowOnStartup() == true) {
					BufferedImage logo = null;
					try {
						logo = ImageIO.read(DonutFramework.class.getResourceAsStream("Icon.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					ImageIcon logoIcon = new ImageIcon(logo);

					JCheckBox checkbox = new JCheckBox("Diesen Dialog nicht mehr anzeigen.");
					Object[] message = { "Willkommen beim Donut Antennentool", "Wollen Sie das Tutorial starten?",
							logoIcon, checkbox };
					JOptionPane startupDialog = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE,
							JOptionPane.YES_NO_OPTION);

					JDialog dialog = startupDialog.createDialog(null, "Tutorial");
					width /= 3;
					height /= 3;
					dialog.setLocation(screenSize.width / 3, screenSize.height / 3);
					if (dialog.getHeight() > height)
						height = dialog.getHeight();
					if (dialog.getWidth() > width)
						width = dialog.getWidth();
					dialog.setSize(width, height);
					dialog.setVisible(true);

					Object selectedValue = startupDialog.getValue();
					int n = -1;

					if (selectedValue == null)
						n = JOptionPane.CLOSED_OPTION;
					else
						n = Integer.parseInt(selectedValue.toString());

					if (n == JOptionPane.YES_OPTION) {
						frame.showTutorial();
					}
					if (checkbox.isSelected()) {
						System.out.println("nicht mehr anzeigen");
						frame.setShowOnStartup(false);
					}
				}
			}
		});
	}
}
