package pro2e.userinterface;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class MyBorderFactory {

	public static Border createMyBorder(String title) {
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		Border titled = BorderFactory.createTitledBorder(loweredetched, title);
		return titled;
	}
}
