package pro2e.model;

import org.apache.commons.math3.complex.Complex;

import pro2e.matlabfunctions.MiniMatlab;

public class CircularAntennaArray {

	private int nAnt = 8;
	private double dLamRadius = 0.5;
	private int nMeasures = 361;
	private boolean plotScaleDb = true;

	/**
	 * @brief berechnet den 3db Öffnungswinkel
	 * @return
	 */
	public double get3dbAngle() {

		int resolution = 1000;
		double degreesPerStep = 360.0 / (double) resolution;

		int nMeas = this.nMeasures; // zwischenspeichern
		this.nMeasures = resolution; // und stattdessen mit 1000 Punkten berechnen
		double[] pattern = this.getCircularArrayPlot();
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
	 * @brief berechnet die 2D Abstrahlcharakteristik von 0 bis 360° mit den
	 *        gegebenen Attributen mit und ohne Reflektor
	 * @return
	 */
	public double[] getCircularArrayPlot() {
		double[] out = new double[this.nMeasures];
		double[] psi_r = MiniMatlab.linspace(0, 2 * Math.PI, nMeasures);

		double[] amplitude = antenne.getIntensity(nMeasures); // Abstrahlamplituden eines einzelnen Strahlers

		Complex[] s = new Complex[nMeasures]; // leeres Complex Array
		for (int i = 0; i < s.length; i++) {
			s[i] = new Complex(0, 0);
		}

		double phi = 0;
		Complex addend;

		double alp = 2 * Math.PI / nAnt;

		for (int ii = 0; ii < nMeasures; ii++) {
			for (int k = 0; k < nAnt; k++) {
				phi = -(dLamRadius * 4 * Math.PI * Math.cos(psi_r[ii] - k * (alp / 2)) * Math.sin(k * (alp / 2)));
				addend = new Complex(Math.cos(phi), Math.sin(phi));
				addend = addend.multiply(amplitude[ii]);
				s[(ii + (int) nMeasures * 25) % nMeasures] = s[ii].add(addend);
				// rotate by an angle of 90° to match the linear array plot
				// nMeasures/4*3 --> 90° rotation
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

	public double getdLamRadius() {
		return dLamRadius;
	}

	public void setdLamRadius(double dLamRadius) {
		this.dLamRadius = dLamRadius;
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

	public Antenna getAntenne() {
		return antenne;
	}

	public void setAntenne(Antenna antenne) {
		this.antenne = antenne;
	}

	public CircularAntennaArray(Antenna ant) {
		this.antenne = ant;
	}

}
