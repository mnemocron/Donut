package pro2e.matlabfunctions;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;

public class MiniMatlab {
	static final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
	static final SplineInterpolator interpolator = new SplineInterpolator();

	/**
	 * <pre>
	 * berechnet den Binomialkoeffizient n tief k
	 * </pre>
	 * 
	 * @param n
	 * @param k
	 * @return
	 */
	public static int nchoosek(int n, int k) {
		return factorial(n) / (factorial(k) * factorial(n - k));
	}

	/**
	 * <pre>
	 * berechnet den Binomialkoeffizient n tief k
	 * </pre>
	 * 
	 * @param n
	 * @param k
	 * @return
	 */
	public static BigDecimal nchoosekBig(int n, int k) {
		return MiniMatlab.factorialBig(n)
				.divide((MiniMatlab.factorialBig(k)).multiply(MiniMatlab.factorialBig((n) - k)));
	}

	/**
	 * <pre>
	 * gibt den Index des Maximums zurück
	 * </pre>
	 * 
	 * @param a
	 * @return
	 */
	public static int getIndexOfMax(double[] a) {
		int index = 0;
		double max = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] >= max) {
				max = a[i];
				index = i;
			}
		}
		return index;
	}

	/**
	 * <pre>
	 * skaliert die Werte auf die logarithmische Skala
	 * 
	 * @param a
	 * @return
	 */
	public static double[] db20(double[] a) {
		for (int i = 0; i < a.length; i++) {
			a[i] = 20 * Math.log10(a[i]);
			if (!Double.isFinite(a[i])) {
				// a[i] = Long.MAX_VALUE + 1; // auf kleinstmöglichen Wert setzen
				a[i] = Double.NaN; // auf NaN setzen, JFreeChart ignoriert diese Punkte dann einfach
			}
		}

		return a;
	}

	/**
	 * <pre>
	 * berechnet die Summe aller Werte im Array a
	 * </pre>
	 * 
	 * @param a
	 * @return
	 */
	public static double sum(double[] a) {
		double sum = 0.0;
		for (int ii = 0; ii < a.length; ii++) {
			sum += a[ii];
		}
		return sum;
	}

	/**
	 * <pre>
	 * gibt Absolutwerte eines Complex Arrays zurück
	 * </pre>
	 * 
	 * @param z
	 * @return
	 */
	public static double[] abs(Complex[] z) {
		double[] ret = new double[z.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = z[i].abs();
		}
		return ret;
	}

	/**
	 * <pre>
	 * gibt den Absolutwert der Zahl a zurück
	 * </pre>
	 * 
	 * @param z
	 * @return
	 */
	public static double abs(double a) {
		if (a >= 0)
			return a;
		else
			return -a;
	}

	/**
	 * <pre>
	 * gibt Absolutwerte eines double Arrays zurück
	 * </pre>
	 * 
	 * @param z
	 * @return
	 */
	public static double[] abs(double[] a) {
		double[] ret = new double[a.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = abs(a[i]);
		}
		return ret;
	}

	/**
	 * <pre>
	 * gibt die kleinere Zahl von a und b zurück
	 * </pre>
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int min(int a, int b) {
		if (a < b)
			return a;
		else
			return b;
	}

	/**
	 * <pre>
	 * gibt die kleinste Zahl aus dem Array a zurück
	 * </pre>
	 * 
	 * @param a
	 * @return
	 */
	public static int min(int[] a) {
		int min = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * <pre>
	 * gibt die grössere Zahl von a und b zurück
	 * </pre>
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int max(int a, int b) {
		if (a > b)
			return a;
		else
			return b;
	}

	/**
	 * <pre>
	 * gibt die grösste Zahl aus dem Array a zurück
	 * </pre>
	 * 
	 * @param a
	 * @return
	 */
	public static int max(int[] a) {
		int max = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * <pre>
	 * gibt die grösste Zahl aus dem Array a zurück
	 * </pre>
	 * 
	 * @param a
	 * @return
	 */
	public static double max(double[] a) {
		double max = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * <pre>
	 * gibt die grösste Zahl aus dem Array a zurück
	 * </pre>
	 * 
	 * @param a
	 * @return
	 */
	public static BigDecimal max(BigDecimal[] a) {
		BigDecimal max = BigDecimal.ZERO;
		for (int i = 0; i < a.length; i++) {
			if (a[i].compareTo(max) == 1)
				max = a[i];
		}
		return max;
	}

	/**
	 * <pre>
	 * normalisiert das Array a auf eine Skala von -1 bis +1
	 * </pre>
	 * 
	 * @param a
	 * @return
	 */
	public static double[] normalize(double[] a) {
		double max = max(abs(a));
		for (int i = 0; i < a.length; i++) {
			a[i] /= max;
		}
		return a;
	}

	/**
	 * <pre>
	 * Berechnet die Absolutwerte eines BigDecimal Arrays
	 * </pre>
	 * 
	 * @param a
	 * @return
	 */
	public static BigDecimal[] abs(BigDecimal[] a) {
		for (int i = 0; i < a.length; i++) {
			a[i] = a[i].abs();
		}
		return a;
	}

	/**
	 * <pre>
	 * normalisiert das Array a auf eine Skala von -1 bis +1
	 * </pre>
	 * 
	 * @param a
	 * @return
	 */
	public static double[] normalize(BigDecimal[] a) {
		BigDecimal max = max(abs(a));
		for (int i = 0; i < a.length; i++) {
			a[i] = a[i].divide(max, 10, RoundingMode.HALF_UP); // Rundungspräzision einstellen (10 Nachkommastellen)
		}

		double[] d = new double[a.length];
		for (int i = 0; i < d.length; i++) {
			d[i] = a[i].doubleValue();
		}

		return d;
	}

	/**
	 * <pre>
	 * Berechnet den Arcussinushyperbolicus
	 * </pre>
	 * 
	 * @param x
	 * @return
	 */
	public static double asinh(double x) {
		return Math.log(x + Math.sqrt(x * x + 1.0));
	}

	/**
	 * <pre>
	 * berechnet den Arcuscosinushyperbolicus
	 * </pre>
	 * 
	 * @param x
	 * @return
	 */
	public static double acosh(double x) {
		return Math.log(x + Math.sqrt(x * x - 1.0));
	}

	/**
	 * <pre>
	 * Berechnet den Arcus-Tangens-Hyperbolicus
	 * </pre>
	 * 
	 * @param x
	 * @return
	 */
	public static double atanh(double x) {
		return 0.5 * Math.log((x + 1.0) / (x - 1.0));
	}

	public static final double[] linspace(double begin, double end, int cnt) {
		double[] res = new double[cnt];
		double delta = (end - begin) / (cnt - 1);

		res[0] = begin;
		for (int i = 1; i < res.length - 1; i++) {
			res[i] = begin + i * delta;
		}
		res[res.length - 1] = end;

		return res;
	}

	public static final double[] multiply(double[] a, double b) {

		for (int i = 0; i < a.length; i++) {
			a[i] *= b;
		}

		return a;
	}

	public static final double[] ones(int i) {
		double[] p = new double[i];
		for (int j = 0; j < p.length; j++) {
			p[j] = 1.0;
		}

		return p;
	}

	public static double[] real(Complex[] c) {
		double[] res = new double[c.length];

		for (int i = 0; i < res.length; i++) {
			res[i] = c[i].getReal();
		}

		return res;
	}

	public static final double[] zeros(int i) {
		double[] p = new double[i];

		return p;
	}

	public static final Complex[] zerosC(int i) {
		Complex[] p = new Complex[i];
		for (int j = 0; j < p.length; j++) {
			p[j] = new Complex(0.0, 0.0);
		}

		return p;
	}

	/**
	 * <pre>
	 * Berechnet Fakultät für grosse Zahlen (num > 19)
	 * </pre>
	 * 
	 * @param num
	 * @return
	 */
	public static BigDecimal factorialBig(int num) {
		BigDecimal fact = BigDecimal.valueOf(1);
		for (int i = 1; i <= num; i++)
			fact = fact.multiply(BigDecimal.valueOf(i));
		return fact;
	}

	/**
	 * <pre>
	 * Berechnet Fakultät für kleine Zahlen (num < 19)
	 * </pre>
	 * 
	 * @param num
	 * @return
	 */
	public static int factorial(int num) {
		int fact = 1;
		for (int i = 1; i <= num; i++)
			fact = fact * ((i));
		return fact;
	}

}
