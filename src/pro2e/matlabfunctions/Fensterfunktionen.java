package pro2e.matlabfunctions;

import java.math.BigDecimal;

/**
 * @brief Klasse mit statischen Methoden zur Berechnung von Fensterfunktionen
 * @author simon
 * @see https://en.wikipedia.org/wiki/Window_function
 * @see http://practicalcryptography.com/miscellaneous/machine-learning/implementing-dolph-chebyshev-window/
 *
 */
public class Fensterfunktionen {
	/**
	 * @brief enumerations for the different window functions
	 */
	public static final int RECTANGULAR = 0;
	public static final int CHEBYSHEV = 1;
	public static final int BINOMIAL = 2;
	public static final int COSINE = 3;
	public static final int COSINESQUARE = 4;
	public static final int VONHANN = 5;
	public static final int HANNING = 6;
	public static final int BLACKMAN = 7;
	public static final int WELCH = 8;
	public static final int BARTLETT = 9;
	public static final int BARTLETTHANN = 10;
	public static final int TRIANGULAR = 11;
	public static final int EXPONENTIAL = 12;

	/**
	 * @brief enumerations for the rounding function
	 */
	public static final int ROUND_FLOOR = 0;
	public static final int ROUND_CEIL = 1;
	public static final int ROUND_NEAREST = 2;

	// public static enum Window {
	// RECTANGULAR, CHEBYSHEV, BINOMIAL, COSINE, COSINESQUARE, VONHANN, HANNING,
	// BLACKMAN, WELCH, BARTLETT, BARTLETTHANN, TRIANGULAR, EXPONENTIAL
	// };

	/**
	 * @brief Quantisierungsfunktion nach Binärwerten - Methode: Floor / abrunden
	 * @param val
	 *            array of normalized values (range: 0-1)
	 * @param bits
	 *            the number of bits
	 * @return
	 */
	public static double[] Quantize(double[] val, int bits) {
		double[] out = new double[val.length];
		for (int i = 0; i < val.length; i++) {
			// out[i]=quantize(val[i], bits);
			out[i] = RoundToBinary(val[i], bits, ROUND_NEAREST);
		}
		return out;
	}

	/**
	 * @brief Quantisierungsfunktion nach Binärwerten - Methode: Ceil / aufrunden
	 * @param val
	 * @param bits
	 * @return
	 */
	public static double Quantize(double val, int bits) {
		int resolution = (int) Math.pow(2, bits);
		double step = 1.0 / resolution;
		double ret = 0;
		while (ret < val) {
			ret += step;
		}
		return ret;
	}

	/**
	 * 
	 * @param val
	 * @param bits
	 * @param method
	 * @return
	 */
	public static double RoundToBinary(double val, int bits, int method) {
		int resolution = (int) Math.pow(2, bits);
		double step = 1.0 / resolution;
		double ret = 0;
		if (method == ROUND_CEIL) {
			ret = 0;
			while (ret < val) {
				ret += step;
			}
		}
		if (method == ROUND_FLOOR) {
			ret = Math.pow(2, bits);
			while (ret > val) {
				ret -= step;
			}
		}
		if (method == ROUND_NEAREST) {
			double stepn = 1.0 / (resolution-1);
			for (int round = 0; round < resolution; round++) {
				if (Math.abs(val - (round * stepn)) <= (stepn / 2)) {
					ret = stepn * round;
				}
			}
		}
		return ret;
	}

	/**
	 * @brief Rechteckfenster / entspricht dem Matlab-Befehl ones(1, N)
	 * @param N
	 * @return
	 */
	public static double[] Rectangular(int N) {
		double[] out = new double[N];
		for (int i = 0; i < out.length; i++) {
			out[i] = 1;
		}
		return out;
	}

	/**
	 * @brief Dreiecksfenster (ohne Anheben)
	 * @param N
	 * @return
	 */
	public static double[] Triangular(int N) {
		int gerade;
		double[] FensterfunktionDreieck = new double[N];
		for (int i = 0; i < ((N / 2) + 1); i++) {
			FensterfunktionDreieck[i] = 1.0 - Math.abs(((i) - (N) / 2.0) / ((N) / 2.0));
		}
		if ((N) % 2 == 0) {
			gerade = 1;
		} else {
			gerade = 0;
		}
		for (int i = 0; i < ((N) / 2) + gerade; i++) {
			FensterfunktionDreieck[N - 1 - i] = FensterfunktionDreieck[i];
		}
		return FensterfunktionDreieck;

	}

	/**
	 * @brief Exponentialfunktion mit anpassbarer Steigung
	 * @param N
	 * @param steigung
	 * @return
	 */
	public static double[] Exponential(int N, double steigung) {
		double[] FensterExponential = new double[N];
		for (int i = 0; i < N; i++) {
			FensterExponential[i] = Math
					.exp(-Math.abs((i) - ((N - 1.0) / 2.0)) * (1 / ((N * 8.69) / (2.0 * steigung))));
		}
		return FensterExponential;
	}

	/**
	 * @brief Binominal-Fenster
	 * @details Das Binomial-Fenster unterdrückt sämtliche Nebenkeulen in einer
	 *          Arrayantennen-Anwendung
	 * @note Dependency: MiniMatlab Funktion
	 * 
	 *       <pre>
	 * BigDecimal factorial(int num)
	 *       </pre>
	 * 
	 * @param N
	 * @return
	 */
	public static double[] Binominal(int N) {
		double[] d = new double[N];
		/**
		 * @NOTE Calculating using double numbers only works up to N~20 --> using
		 *       BigDecimal for greater values
		 */
		if (N > 18) { // Slower Algorithm but works with greater numbers
			BigDecimal[] out = new BigDecimal[N];
			for (int k = 0; k < out.length; k++) {
				BigDecimal big = MiniMatlab.nchoosekBig(N - 1, k);
				out[k] = big;
			}
			d = MiniMatlab.normalize(out);
		} else { // Quicker Algorithm with double numbers
			double[] out = new double[N];
			for (int k = 0; k < out.length; k++) {
				double big = MiniMatlab.nchoosek(N - 1, k);
				out[k] = big;
			}
			d = MiniMatlab.normalize(out);
		}
		return d;
	}

	/**
	 * This function computes the chebyshev polyomial T_n(x)
	 * 
	 * @param n
	 * @param x
	 * @return
	 * @see http://practicalcryptography.com/miscellaneous/machine-learning/implementing-dolph-chebyshev-window/
	 */
	private static double Cheby_poly(int n, double x) {
		double res;
		if (Math.abs(x) <= 1)
			res = Math.cos(n * Math.acos(x));
		else
			res = Math.cosh(n * MiniMatlab.acosh(x));
		return res;
	}

	/**
	 * @brief Dolph Chebyshev Fenster - entspricht dem Matlab commad chebwin()
	 *        chebwin(N,R) returns the N-point Chebyshev window with R decibels of
	 *        relative sidelobe attenuation.
	 * @param N
	 *            size of output array
	 * @param atten
	 *            attenuation
	 * @return
	 * @see http://practicalcryptography.com/miscellaneous/machine-learning/implementing-dolph-chebyshev-window/
	 */
	public static double[] Chebwin(int size, double atten) {
		double[] out = new double[size];
		int nn, i;
		double M, n, sum = 0, max = 0;
		double tg = Math.pow(10, atten / 20); /* 1/r term [2], 10^gamma [2] */
		double x0 = Math.cosh((1.0 / (size - 1)) * MiniMatlab.acosh(tg));
		M = (size - 1) / 2;
		if (size % 2 == 0)
			M = M + 0.5; /* handle even length windows */
		for (nn = 0; nn < (size / 2 + 1); nn++) {
			n = nn - M;
			sum = 0;
			for (i = 1; i <= M; i++) {
				sum += Cheby_poly(size - 1, x0 * Math.cos(Math.PI * i / size)) * Math.cos(2.0 * n * Math.PI * i / size);
			}
			out[nn] = tg + 2 * sum;
			out[size - nn - 1] = out[nn];
			if (out[nn] > max)
				max = out[nn];
		}
		for (nn = 0; nn < size; nn++)
			out[nn] /= max; /* normalise everything */
		return out;
	}

	/**
	 * @brief Cosinusfenster (anhebbar)
	 * @param N
	 * @param x
	 * @return
	 */
	public static double[] Cosin(int size, double x) {
		double[] out = new double[size];
		for (int i = 0; i < size; i++) {
			out[i] = x * Math.cos(((Math.PI * i) / (size - 1)) - (Math.PI / 2)) + (1.0 - x);
		}
		return out;
	}

	public static double[] CosinSquare(int size, double x) {
		double[] out = new double[size];
		for (int i = 0; i < size; i++) {
			out[i] = Math.pow(x * Math.cos(((Math.PI * i) / (size - 1)) - (Math.PI / 2)) + (1.0 - x), 2);
		}
		return out;
	}

	/**
	 * @brief von Hann Fenster
	 * @note Tested: working
	 * @param size
	 * @return
	 */
	public static double[] VonHann(int size) {
		double[] samples = new double[size];
		for (int i = 0; i < size; i++) {
			samples[i] = 0.5 * (1 - Math.cos((2 * Math.PI * i) / (double) (size - 1)));
		}
		return samples;
	}

	/**
	 * @brief Hanning Fenster
	 * @note Tested: working
	 * @param size
	 * @return
	 */
	public static double[] Hanning(int size) {
		double[] samples = new double[size];
		for (int i = 0; i < size; ++i) {
			samples[i] = (1 * 0.5 * (1.0 - Math.cos(2 * Math.PI * i / (size - 1))));
		}
		return samples;
	}

	/**
	 * @brief Blackman Fenster
	 * @note Tested: working
	 * @param size
	 * @return
	 */
	public static double[] Blackman(int size) {
		double[] samples = new double[size];
		double alpha = 0.16, a0 = (1.0 - alpha) / 2.0, a1 = 0.5, a2 = alpha / 2.0;
		for (int i = 0; i < size; ++i) {
			samples[i] = a0 - a1 * Math.cos((2 * Math.PI * i) / (double) (size - 1))
					+ a2 * Math.cos((4 * Math.PI * i) / (double) (size - 1));
		}
		return samples;
	}

	/**
	 * @brief Welch Fenster
	 * @note Tested: working
	 * @param size
	 * @return
	 */
	public static double[] Welch(int size) {
		double[] samples = new double[size];
		for (int i = 0; i < size; ++i) {
			samples[i] = (1 * (1 - Math.pow((double) (i - ((size - 1) / 2)) / (double) ((size - 1) / 2), 2)));
		}
		return samples;
	}

	/**
	 * @brief Bartlett Hann Fenster
	 * @note Tested: working
	 * @param size
	 * @return
	 */
	public static double[] BartlettHann(int size) {
		double[] samples = new double[size];
		double a0 = 0.62, a1 = 0.48, a2 = 0.38;
		for (int i = 0; i < size; ++i) {
			samples[i] = a0 - a1 * Math.abs((((double) i) / (double) (size - 1)) - 0.5)
					- a2 * Math.cos((2 * Math.PI * i) / (double) (size - 1));
		}
		return samples;
	}

}
