package pro2e.userinterface.input;
/*
 * Copyright (c) 2014 Mueller Fabian, Duerner Daniel, Risi Julian, Walzer Ken,
 * Mijnssen Raphael, Pluess Jonas
 * 
 * Authors: Pluess Jonas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JDoubleTextField extends JTextField implements FocusListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private boolean allowNegativeValues, mousePressed;

	public JDoubleTextField(String text, int columns, boolean allowNegativeValues) {
		super(text);
		setColumns(columns);
		this.allowNegativeValues = allowNegativeValues;
		addFocusListener(this);
		addMouseListener(this);
	}

	protected Document createDefaultModel() {
		return new DoubleDocument(this);
	}

	/**
	 * <pre>
	 * Returns true if the text field allows negative values (&lt; 0.0)
	 * </pre>
	 * @return boolean
	 */
	public boolean getAllowNegativeValues() {
		return allowNegativeValues;
	}

	public void focusGained(FocusEvent arg0) {
		new Thread() {
			public void run() {
				while (mousePressed == true) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				selectAll();
			}
		}.start();
		selectAll();
	}

	public void focusLost(FocusEvent arg0) {
		fireActionPerformed();
	}

	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
		mousePressed = true;
	}

	public void mouseReleased(MouseEvent arg0) {
		mousePressed = false;
	}

	class DoubleDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;
		private JDoubleTextField tf;

		public DoubleDocument(JDoubleTextField tf) {
			super();
			this.tf = tf;
		}

		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			try {
				if (tf.allowNegativeValues == true) {
					if (str.equals("-") || str.equals("+") || str.equals("e") || str.equals(".")) {
						Double.parseDouble(tf.getText() + str + "0");
					} else {
						Double.parseDouble(tf.getText() + str);
					}
				} else {
					if (str.equals("-") && tf.getText().equals("")) {
						return;
					} else {
						Double.parseDouble(tf.getText() + str + "0");
					}
				}
			} catch (NumberFormatException e) {
				return;
			}
			super.insertString(offs, str, a);
		}
	}
}
