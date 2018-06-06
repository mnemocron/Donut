package pro2e.userinterface.tutorial;

import com.sun.javafx.application.PlatformImpl;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// access restriction: not api: https://stackoverflow.com/a/29329228
// accessible: javafx/**
// accessible: con/sun/javafx/application/**
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import pro2e.userinterface.Utility;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @see https://gist.github.com/a1o1/1877012
 * @author a1o1
 */
public class SwingFXWebView extends JPanel {
	private static final long serialVersionUID = 1837587643723839656L;
	private Stage stage;
	private WebView browser;
	private JFXPanel jfxPanel;
	private WebEngine webEngine;
	private JButton swingButton;

	public SwingFXWebView() {
		initComponents();
	}

	public static void main(String... args) {
		// Run this later:
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new JFrame();

				frame.getContentPane().add(new SwingFXWebView());
				frame.setMinimumSize(new Dimension(640, 480));
				frame.setVisible(true);
			}
		});
	}

	private void initComponents() {

		jfxPanel = new JFXPanel();
		createScene();

		setLayout(new GridBagLayout());

		swingButton = new JButton();
		swingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (webEngine.getHistory().getCurrentIndex() != 0) {
							goHome();
						}
					}
				});
			}
		});
		swingButton.setText("Tutorial neu starten");

		add(jfxPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));
		add(swingButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	}

	// private void goBack() {
	// WebHistory history = webEngine.getHistory();
	// int currentIndex = history.getCurrentIndex();
	// if (currentIndex != 0) {
	// Platform.runLater(new Runnable() {
	// public void run() {
	// history.go(-1);
	// }
	// });
	// }
	// }

	private void goHome() {
		WebHistory history = webEngine.getHistory();
		Platform.runLater(new Runnable() {
			public void run() {
				history.go(-history.getCurrentIndex());
			}
		});
	}

	/**
	 * createScene
	 * 
	 * Note: Key is that Scene needs to be created and run on "FX user thread" NOT
	 * on the AWT-EventQueue Thread
	 * 
	 */
	private void createScene() {
		PlatformImpl.startup(new Runnable() {
			@Override
			public void run() {
				stage = new Stage();

				stage.setTitle("Donut Tutorial");
				stage.setResizable(false);

				StackPane root = new StackPane();

				Scene scene = new Scene(root, 80, 20);
				stage.setScene(scene);

				// Set up the embedded browser:
				browser = new WebView();
				webEngine = browser.getEngine();
				URL url = Utility.loadResourceUrl("1_Antennen.html");
				webEngine.load(url.toString());

				ObservableList<Node> children = root.getChildren();
				children.add(browser);

				jfxPanel.setScene(scene);
			}
		});
	}
}
