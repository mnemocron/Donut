package pro2e.model;

import org.apache.commons.math3.complex.Complex;

import pro2e.matlabfunctions.Fensterfunktionen;
import pro2e.matlabfunctions.MiniMatlab;

public class LinearAntennaArray {
	public static final int POSITION_BROADSIDE = 0;
	public static final int POSITION_ENDFIRE = 1;

	private int nAnt = 8;
	private double dLam = 0.5;
	private double phase = 90;
	private int window = Fensterfunktionen.RECTANGULAR;
	private double windowParam = 0.9;
	private boolean reflektor = false;
	private double dLamReflektor = 0.5;
	private int nMeasures = 361;
	private int nBits = 3;
	private boolean diskretisierung = false;
	private int reflektorPosition = POSITION_BROADSIDE;
	private boolean plotScaleDb = true;

	/**
	 * <pre>
	 *  berechnet den 3db Öffnungswinkel
 	 * </pre>
	 * @return
	 */
	public double get3dbAngle() {

		int resolution = 1000;
		double degreesPerStep = 360.0 / (double) resolution;

		int nMeas = this.nMeasures; // zwischenspeichern
		this.nMeasures = resolution; // und stattdessen mit 1000 Punkten berechnen
		double[] pattern = this.getLinearPhasedArrayPlot();
		this.nMeasures = nMeas;

		double db3 = 3;
		int indexOfMainLobe = MiniMatlab.getIndexOfMax(pattern);
		int indexOfRight3db = indexOfMainLobe;
		int indexOfLeft3db = indexOfMainLobe;
		double max = pattern[indexOfMainLobe];

		for (int i = indexOfMainLobe; i < pattern.length; i++) {
			if (MiniMatlab.abs(max - pattern[i]) >= db3) {
				indexOfRight3db = i;
				i = pattern.length;
			}
		}
		for (int i = indexOfMainLobe; i > 0; i--) {
			if (MiniMatlab.abs(max - pattern[i]) >= db3) {
				indexOfLeft3db = i;
				i = 0;
			}
		}

		return MiniMatlab.abs(degreesPerStep * indexOfLeft3db - degreesPerStep * indexOfRight3db);
	}

	/**
	 * <pre>
	 * berechnet die 2D Abstrahlcharakteristik von 0 bis 360° mit den
	 * gegebenen Attributen mit und ohne Reflektor
 	 * </pre>
	 * @return
	 */
	public double[] getLinearPhasedArrayPlot() {
		double[] out = new double[this.nMeasures];
		double[] psi_r = MiniMatlab.linspace(0, 2 * Math.PI, nMeasures);
		phase = 0 - phase;
		double dd_pp = 2 * Math.PI * Math.cos(Math.toRadians(phase)) * dLam; // Laufzeitunterschied zwischen 2 Strahlern
																				// bei der Phased Array Anwendung

		double[] ak = new double[nMeasures];
		switch (window) {
		case Fensterfunktionen.CHEBYSHEV:
			ak = Fensterfunktionen.Chebwin(nAnt, windowParam);
			break;
		case Fensterfunktionen.TRIANGULAR:
			ak = Fensterfunktionen.Triangular(nAnt);
			break;
		case Fensterfunktionen.BINOMIAL:
			ak = Fensterfunktionen.Binominal(nAnt);
			break;
		case Fensterfunktionen.COSINE:
			ak = Fensterfunktionen.Cosin(nAnt, windowParam);
			break;
		case Fensterfunktionen.COSINESQUARE:
			ak = Fensterfunktionen.CosinSquare(nAnt, windowParam);
			break;
		case Fensterfunktionen.EXPONENTIAL:
			ak = Fensterfunktionen.Exponential(nAnt, windowParam);
			break;
		default:
			ak = Fensterfunktionen.Rectangular(nAnt);
		}

		if (isDiskretisierung()) {
			ak = Fensterfunktionen.Quantize(ak, getnBits()); // Fensterfunktionsamplituden diskretisieren
		}

		double[] amplitude = antenne.getIntensity(nMeasures); // Abstrahlamplituden eines einzelnen Strahlers

		double[] pk = new double[nAnt]; // Laufzeitunterschied für alle Strahler
		for (int i = 0; i < pk.length; i++) {
			pk[i] = (i + 1) * dd_pp;
		}

		Complex[] s = new Complex[nMeasures]; // leeres Complex Array
		for (int i = 0; i < s.length; i++) {
			s[i] = new Complex(0, 0);
		}

		double phi = 0;
		Complex addend;

		if (this.isReflektor()) {
			/* Reflektor senkrecht zum Antennenarray (links) */
			if (this.reflektorPosition == POSITION_ENDFIRE) {
				for (int ii = 0; ii < nMeasures; ii++) {
					if (psi_r[ii] >= Math.PI / 2 && psi_r[ii] <= 3 * Math.PI / 2) { // Fallunterscheidung
						for (int k = 0; k < nAnt; k++) {
							// s=s+C.*exp(-j*dL*2*pi.*cos(phi_r).*(k-1)) +
							// C.*exp(-j.*((2*dR+dL*(k-1))*2*pi.*cos(phi_r)+dL*2*pi.*cos(phi_r).*(k-1)+pi));
							phi = -(dLam * 2 * Math.PI * Math.cos(psi_r[ii]) * (k) + pk[k]);
							addend = new Complex(Math.cos(phi), Math.sin(phi));
							addend = addend.multiply(ak[k]).multiply(amplitude[ii]);
							s[ii] = s[ii].add(addend);
							phi = -(((2 * dLamReflektor + dLam * (k)) * 2 * Math.PI * Math.cos(psi_r[ii])
									+ dLam * 2 * Math.PI * Math.cos(psi_r[ii]) * (k)) + pk[k]);
							addend = new Complex(Math.cos(phi), Math.sin(phi));
							addend = addend.multiply(ak[k]).multiply(amplitude[ii]);
							s[ii] = s[ii].add(addend);
						}
					} else {
						s[ii] = new Complex(0, 0); // auf der anderen Seite des Reflektors ist die abstrahlung = 0
					}
				}
			} else {
				/* Reflektor parallel zum Antennenarray (unten) */
				/* POSITION_BROADSIDE // PARALLEL */
				for (int ii = 0; ii < nMeasures; ii++) {
					if (psi_r[ii] >= 0.0 && psi_r[ii] <= Math.PI) { // Fallunterscheidung
						for (int k = 0; k < nAnt; k++) {
							// s=s+C.*exp(-j*dL*2*pi.*cos(phi_r).*(k-1)) +
							// C.*exp(-j.*(2*dR*2*pi.*sin(phi_r)+dL*2*pi.*cos(phi_r).*(k-1)+pi));
							phi = -(dLam * 2 * Math.PI * Math.cos(psi_r[ii]) * (k) + pk[k]);
							addend = new Complex(Math.cos(phi), Math.sin(phi));
							addend = addend.multiply(ak[k]).multiply(amplitude[ii]);
							s[ii] = s[ii].add(addend);
							phi = -(2 * dLamReflektor * 2 * Math.PI * Math.sin(psi_r[ii])
									+ dLam * 2 * Math.PI * Math.cos(psi_r[ii]) * (k) + Math.PI + pk[k]);
							addend = new Complex(Math.cos(phi), Math.sin(phi));
							addend = addend.multiply(ak[k]).multiply(amplitude[ii]);
							s[ii] = s[ii].add(addend);
						}
					} else {
						s[ii] = new Complex(0, 0); // auf der anderen Seite des Reflektors ist die abstrahlung = 0
					}
				}
			}
		} else {
			/* Ohne Reflektor */
			for (int ii = 0; ii < nMeasures; ii++) {
				for (int k = 0; k < nAnt; k++) {
					phi = -(dLam * 2 * Math.PI * Math.cos(psi_r[ii]) * (k) + pk[k]);
					addend = new Complex(Math.cos(phi), Math.sin(phi));
					addend = addend.multiply(ak[k]);
					addend = addend.multiply(amplitude[ii]);
					s[ii] = s[ii].add(addend);
				}
			}
		}

		if (plotScaleDb) {
			out = MiniMatlab.db20(MiniMatlab.normalize(MiniMatlab.abs(s))); // normieren und auf dB Skala umrechnen
		} else {
			out = (MiniMatlab.normalize(MiniMatlab.abs(s)));
		}

		return out;
	}

	public boolean isPlotScaleDb() {
		return plotScaleDb;
	}

	public void setPlotScaleDb(boolean plotScaleDb) {
		this.plotScaleDb = plotScaleDb;
	}

	public int getReflektorPosition() {
		return reflektorPosition;
	}

	public void setReflektorPosition(int reflektorPosition) {
		this.reflektorPosition = reflektorPosition;
	}

	public int getnBits() {
		return nBits;
	}

	public void setnBits(int nBits) {
		this.nBits = nBits;
	}

	public boolean isDiskretisierung() {
		return diskretisierung;
	}

	public void setDiskretisierung(boolean diskretisierung) {
		this.diskretisierung = diskretisierung;
	}

	public int getnMeasures() {
		return nMeasures;
	}

	public void setnMeasures(int nMeasures) {
		this.nMeasures = nMeasures;
	}

	private Antenna antenne;

	public int getnAnt() {
		return nAnt;
	}

	public void setnAnt(int nAnt) {
		this.nAnt = nAnt;
	}

	public double getdLam() {
		return dLam;
	}

	public void setdLam(double dLam) {
		this.dLam = dLam;
	}

	public double getPhase() {
		return phase;
	}

	public void setPhase(double phase) {
		this.phase = phase;
	}

	public int getWindow() {
		return window;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	public double getWindowParam() {
		return windowParam;
	}

	public void setWindowParam(double windowParam) {
		this.windowParam = windowParam;
	}

	public boolean isReflektor() {
		return reflektor;
	}

	public void setReflektor(boolean reflektor) {
		this.reflektor = reflektor;
	}

	public double getdLamReflektor() {
		return dLamReflektor;
	}

	public void setdLamReflektor(double dLamReflektor) {
		this.dLamReflektor = dLamReflektor;
	}

	public Antenna getAntenne() {
		return antenne;
	}

	public void setAntenne(Antenna antenne) {
		this.antenne = antenne;
	}

	public LinearAntennaArray(Antenna ant) {
		this.antenne = ant;
	}

}
