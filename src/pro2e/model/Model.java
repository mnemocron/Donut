package pro2e.model;

import java.util.Observable;

import pro2e.TraceV1;

public class Model extends Observable {
	private TraceV1 trace = new TraceV1(this);
	private Antenna antenna = new Antenna(Antenna.ISOTROP, 1);
	private LinearAntennaArray antArray = new LinearAntennaArray(antenna);
	private CircularAntennaArray cirleArray = new CircularAntennaArray(antenna);

	private Object activeArray = antArray;
	
	public boolean isPlotScaleDb() {
		return this.antArray.isPlotScaleDb();
	}

	public void setPlotScaleDb(boolean plotScaleDb) {
		this.cirleArray.setPlotScaleDb(plotScaleDb);
		this.antArray.setPlotScaleDb(plotScaleDb);
	}

	public boolean showLinearArrayOptions() {
		if (this.activeArray == antArray)
			return true;
		else
			return false;
	}

	public double[] getPlot() {
		if (this.activeArray == antArray) {
			return antArray.getLinearPhasedArrayPlot();
		} else {
			return cirleArray.getCircularArrayPlot();
		}
	}

	public Model() {
		trace.constructorCall();
	}

	public int getnMeasures() {
		trace.methodeCall();
		return this.antArray.getnMeasures();
	}

	public void setnMeasures(int nMeasures) {
		trace.methodeCall();
		this.antArray.setnMeasures(nMeasures);
		this.cirleArray.setnMeasures(nMeasures);
	}

	public int getnBits() {
		trace.methodeCall();
		return antArray.getnBits();
	}

	public void setnBits(int nBits) {
		trace.methodeCall();
		this.antArray.setnBits(nBits);
	}

	public boolean isDiskretisierung() {
		trace.methodeCall();
		return this.antArray.isDiskretisierung();
	}

	public void setDiskretisierung(boolean diskretisierung) {
		trace.methodeCall();
		this.antArray.setDiskretisierung(diskretisierung);
		notifyObservers();
	}

	public void setDiskretisierung(boolean diskretisierung, int nBits) {
		trace.methodeCall();
		this.antArray.setDiskretisierung(diskretisierung);
		this.antArray.setnBits(nBits);
		notifyObservers();
	}

	public void setAntenna(int geo, double dLam, int nAnt, int type, boolean reflektor, double dLamRef, int refPos) {
		trace.methodeCall();
		this.cirleArray.setnAnt(nAnt);
		this.cirleArray.getAntenne().setAbstrahlung(type);
		this.cirleArray.setdLamRadius(dLam);

		this.antArray.setdLam(dLam);
		this.antArray.setnAnt(nAnt);
		this.antArray.getAntenne().setAbstrahlung(type);
		this.antArray.setReflektor(reflektor);
		this.antArray.setdLamReflektor(dLamRef);
		this.antArray.setReflektorPosition(refPos);

		if (geo == 0) {
			this.activeArray = antArray;
		}
		if (geo == 1) {
			this.activeArray = cirleArray;
		}

		notifyObservers();
	}

	public boolean isReflektor() {
		trace.methodeCall();
		return antArray.isReflektor();
	}

	public void setReflektor(boolean reflektor) {
		trace.methodeCall();
		this.antArray.setReflektor(reflektor);
	}

	public double getdLamReflektor() {
		trace.methodeCall();
		return this.antArray.getdLamReflektor();
	}

	public void setdLamReflektor(double dLamReflektor) {
		trace.methodeCall();
		this.antArray.setdLamReflektor(dLamReflektor);
	}

	public int getWindow() {
		trace.methodeCall();
		return this.antArray.getWindow();
	}

	public void setWindow(int window) {
		trace.methodeCall();
		this.antArray.setWindow(window);
		notifyObservers();
	}

	public double getWindowParam() {
		trace.methodeCall();
		return this.antArray.getWindowParam();
	}

	public void setWindowParam(double windowParam) {
		trace.methodeCall();
		this.antArray.setWindowParam(windowParam);
		notifyObservers();
	}

	public void setWindowParam(int window, double windowParam) {
		trace.methodeCall();
		this.antArray.setWindow(window);
		this.antArray.setWindowParam(windowParam);
		// System.out.println("[model] " + windowParam);
		notifyObservers();
	}

	public double getPhase() {
		trace.methodeCall();
		return antArray.getPhase();
	}

	public void setPhase(double phase) {
		trace.methodeCall();
		this.antArray.setPhase(phase);
		notifyObservers();
	}

	public Antenna getAntenna() {
		trace.methodeCall();
		return antenna;
	}

	public void setAntenna(Antenna antenna) {
		trace.methodeCall();
		this.antenna = antenna;
		notifyObservers();
	}

	public LinearAntennaArray getAntArray() {
		trace.methodeCall();
		return antArray;
	}

	public void setAntArray(LinearAntennaArray antArray) {
		trace.methodeCall();
		this.antArray = antArray;
		notifyObservers();
	}

	public double getDLam() {
		trace.methodeCall();
		return antArray.getdLam();
	}

	public void setDLam(double dLam) {
		trace.methodeCall();
		this.antArray.setdLam(dLam);
		notifyObservers();
	}

	public void setAntennaType(int t) {
		trace.methodeCall();
		antenna.setAbstrahlung(t);
		notifyObservers();
	}

	public void notifyObservers() {
		trace.methodeCall();
		setChanged();
		super.notifyObservers();
	}

	public void setNAnt(int nAnt) {
		trace.methodeCall();
		this.antArray.setnAnt(nAnt);
		notifyObservers();
	}

	public int getNAnt() {
		trace.methodeCall();
		return this.antArray.getnAnt();
	}
}
