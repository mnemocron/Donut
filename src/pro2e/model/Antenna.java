package pro2e.model;

public class Antenna {
	public static final int ISOTROP = 0;
	public static final int LAMBERT = 1;
	public static final int HERTZDIPOL = 2;
	public static final int HALBWELLENDIPOL = 3;

	private int abstrahlung = ISOTROP;
	private int amplitude = 1;

	public Antenna(int abstrahlung) {
		this.abstrahlung = abstrahlung;
		this.amplitude = 1;
	}

	public Antenna(int abstrahlung, int amplitude) {
		this.abstrahlung = abstrahlung;
		this.amplitude = amplitude;
	}

	public int getAbstrahlung() {
		return this.abstrahlung;
	}

	public void setAbstrahlung(int abstrahlung) {
		this.abstrahlung = abstrahlung;
	}

	public int getAmplitude() {
		return this.amplitude;
	}

	public void setAmplitudeg(int amplitude) {
		this.amplitude = amplitude;
	}

	/**
	 * @brief gibt ein Array mit der Amplitude in Abhängigkeit des Winkels aus (von
	 *        0 bis 360°)
	 * @param N
	 * @return
	 */
	public double[] getIntensity(int N) {
		double[] out = new double[N];
		switch (this.abstrahlung) {
		case LAMBERT:
			for (int i = 0; i < out.length; i++) {
				double phi = i * 2 * Math.PI / N;
				out[i] = this.amplitude * Math.sin(phi);
			}
			break;
		case HERTZDIPOL:
			for (int i = 0; i < out.length; i++) {
				double phi = i * 2 * Math.PI / N;
				out[i] = this.amplitude * Math.pow(Math.sin(phi), 2);
			}
			break;
		case HALBWELLENDIPOL:
			for (int i = 0; i < out.length; i++) {
				double phi = i * 2 * Math.PI / N;
				if (Math.abs(Math.sin(phi)) > 0.00001) {
					out[i] = Math.abs(Math.cos((Math.PI / 2.0) * Math.cos(phi)) / Math.sin(phi));
				} else {
					out[i] = 0;
				}
			}
			break;
		default: // ISOTROP
			for (int i = 0; i < out.length; i++) {
				out[i] = this.amplitude;
			}
			break;
		}
		return out;
	}
}
