package org.vkedco.nlp.audiotrials;

// Bugs to vladimir dot kulyukin at gmail dot com.

public class LDSim01 implements IDTWSimilarity {
	
	public LDSim01() {}

	@Override
	public double compare(Double x, Double y) {
		if ( x.doubleValue() == y.doubleValue() )
			return 0;
		else
			return 1;
	}

}