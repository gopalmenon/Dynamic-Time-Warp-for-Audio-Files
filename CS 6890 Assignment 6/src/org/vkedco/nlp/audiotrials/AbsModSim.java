package org.vkedco.nlp.audiotrials;

public class AbsModSim implements IDTWSimilarity {

	public AbsModSim() {}
	
	@Override
	public double compare(Double x, Double y) {
		return Math.abs(x.doubleValue() - y.doubleValue());
	}

}