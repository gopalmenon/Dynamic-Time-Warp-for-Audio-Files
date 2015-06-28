package org.vkedco.nlp.audiotrials;

// zero crossing rate
// bugs to vladimir dot kulyukin at gmail dot com

public class ZeroCrossingRate {
	
	public static double computeZCR01(double[] signals, double normalizer) 
        {
		long numZC = 0;
		
		for(int i = 1; i < signals.length; i++) {
			if ( (signals[i] >= 0 && signals[i-1] < 0) ||
				 (signals[i] < 0 && signals[i-1] >= 0) ) {
				numZC++;
			}
		}
		return numZC/normalizer;
	}
}