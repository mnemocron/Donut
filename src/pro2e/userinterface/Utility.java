package pro2e.userinterface;

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.ImageIcon;

public class Utility {

	private static Container p = new Container();
	private static String stPackage = "";

	// public static File loadResourceFile(String strFile) {
	// File fil = (new File(Utility.class.getResource("tutorial/html" + "/" +
	// strFile).getFile()));
	// return fil;
	// }

	/**
	 * <pre>
	 * holt einen Text aus einer Datei im Resource Ordner, funktioniert auch, wenn als jar gepackt
 	 * </pre>
	 * @param datei
	 * @return
	 */
	public static String loadResourceText(String datei) {
		// Looks like resources within jar want to be accessed with "/" and not
		// with File.separator!
		String res = stPackage + "tutorial/html" + "/" + datei;
		URL url = Utility.class.getResource(res);
		String txt = "";
		try {
			String inputLine;
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((inputLine = in.readLine()) != null) {
				txt += inputLine + "\n";
			}
			in.close();
		} catch (Exception e) {
			System.out.println("Can not load File: " + datei);
		}
		return txt;
	}

	/**
	 * <pre>
	 * gibt die absolute URL zu einem File auf dem System zurück
 	 * </pre>
	 * @param datei
	 * @return
	 */
	public static URL loadResourceUrl(String datei) {
		// Looks like resources within jar want to be accessed with "/" and not
		// with File.separator!
		String res = stPackage + "tutorial/html" + "/" + datei;
		URL url = Utility.class.getResource(res);
		return url;
	}

	public static Image loadImage(String strBild) {
		MediaTracker tracker = new MediaTracker(p);
		Image img = (new ImageIcon(strBild)).getImage();
		tracker.addImage(img, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException ex) {
			System.err.println("Can not load image: " + strBild);
		}
		return img;
	}

	public static Image loadResourceImage(String strBild) {
		MediaTracker tracker = new MediaTracker(p);
		Image img = (new ImageIcon(Utility.class.getResource("images" + "/" + strBild))).getImage();
		tracker.addImage(img, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException ex) {
			System.err.println("Can not load image: " + strBild);
		}
		return img;
	}

	public static ImageIcon loadResourceIcon(String strBild) {
		MediaTracker tracker = new MediaTracker(p);
		ImageIcon icon = new ImageIcon(Utility.class.getResource("icons" + "/" + strBild));
		// System.out.println(Utility.class.getResource("icons" + File.separator +
		// strBild));
		tracker.addImage(icon.getImage(), 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException ex) {
			System.err.println("Can not load image: " + strBild);
		}
		return icon;
	}
}
