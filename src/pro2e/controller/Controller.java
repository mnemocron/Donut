package pro2e.controller;

import pro2e.DonutFramework;
import pro2e.matlabfunctions.Fensterfunktionen;
import pro2e.model.Model;
import pro2e.TraceV1;

public class Controller {
	private Model model;
	private TraceV1 trace = new TraceV1(this);
	// private P2Framework mvcFramework;

	public boolean isPlotScaleDb() {
		return model.isPlotScaleDb();
	}

	public void setPlotScaleDb(boolean plotScaleDb) {
		model.setPlotScaleDb(plotScaleDb);
	}

	public Controller(Model model, DonutFramework mvcFramework) {
		trace.constructorCall();
		this.model = model;
		// this.mvcFramework = mvcFramework;
	}

	public void setDiskretisierung(boolean on, int bits) {
		trace.methodeCall();
		model.setDiskretisierung(on, bits);
	}

	public void setDiskretisierung(boolean on) {
		trace.methodeCall();
		model.setDiskretisierung(on);
	}

	/**
	 * @brief überprüft die Parameter für die gegebene Fensterfunktion und setzt
	 *        beide Attribute ins Model
	 * @param window
	 * @param windowParam
	 */
	public void setWindowParam(int window, double windowParam) {
		trace.methodeCall();
		if (windowParam < 0) // darf nie kleiner 0 sein
			windowParam = 0;
		if (window == Fensterfunktionen.CHEBYSHEV || window == Fensterfunktionen.EXPONENTIAL) {
			if (windowParam > 60) {
				windowParam = 60;
			}
		}
		if (window == Fensterfunktionen.COSINE || window == Fensterfunktionen.COSINESQUARE) {
			if (windowParam > 1)
				windowParam = 1;
		}

		model.setWindowParam(window, windowParam);
	}

	/**
	 * @brief überprüft die Parameter auf ungültige Werte und setzt alle Attribute
	 *        ins Model. Anzahl Antennen muss zwischen 1 und 100 liegen. Verhältnis
	 *        d/Lamda muss zwischen 0 und 10 sein
	 * @param dLam
	 * @param nAnt
	 * @param type
	 * @param reflektor
	 * @param dLamRef
	 * @param refPos
	 */
	public void setAntenna(int geo, double dLam, int nAnt, int type, boolean reflektor, double dLamRef, int refPos) {
		trace.methodeCall();
		if (geo > 1)
			geo = 1;
		if (geo < 0)
			geo = 0;
		if (nAnt > 100)
			nAnt = 100;
		if (nAnt < 1)
			nAnt = 1;
		if (dLam > 10)
			dLam = 10;
		if (dLamRef > 10)
			dLamRef = 10;
		if (dLamRef < 0.001)
			dLamRef = 0.001;
		model.setAntenna(geo, dLam, nAnt, type, reflektor, dLamRef, refPos);
	}

	public void setWindow(int window) {
		trace.methodeCall();
		model.setWindow(window);
	}

	public void setAntennaType(int t) {
		trace.methodeCall();
		model.setAntennaType(t);
	}

	/**
	 * @brief überprüft ob phase innerhalb von 0 bis 180 Grad liegt und setzt das
	 *        Attribut im Model
	 * @param phase
	 */
	public void setPhase(double phase) {
		trace.methodeCall();
		if (phase < 0)
			phase = 0;
		if (phase > 180)
			phase = 180;
		model.setPhase(phase);
	}
}