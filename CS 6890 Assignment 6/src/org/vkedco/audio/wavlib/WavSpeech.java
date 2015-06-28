package org.vkedco.audio.wavlib;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.vkedco.nlp.audiotrials.WavFile;
import org.vkedco.nlp.audiotrials.WavFileException;
import org.vkedco.nlp.audiotrials.WavFileManip;
import org.vkedco.nlp.audiotrials.ZeroCrossingRate;

/**
 *
 * @author Vladimir Kulyukin
 * bugs to vladimir dot kulyukin at gmail dot com
 */
public class WavSpeech {
    
    public static ArrayList<Double> wavFileToNonSilenceAmpArrayList(String wav_in_path,
            int ch_num, int frame_sample_size,
            double zcr_thresh, double amp_thresh) {

        ArrayList<Double> amp_list = new ArrayList<>();

        try {
            int frames_read = 0;
            double currZCR = 0;
            double currAmp = 0.0;
            WavFile inWavFile = WavFile.openWavFile(new File(wav_in_path));
            long sample_rate = inWavFile.getSampleRate();
            double normalizer = WavFileManip.convertFrameSampleSizeToSeconds((int) sample_rate, frame_sample_size);

            // Get the number of audio channels in the wav file
            int num_channels = inWavFile.getNumChannels();

            System.out.println("WavFile's number of frames: " + inWavFile.getNumFrames());
            System.out.println("WavFile's sample rate: " + inWavFile.getSampleRate());

            // Create a buffer of frame_sample_size frames
            double[][] buffer = new double[num_channels][frame_sample_size];

            do {
                frames_read = inWavFile.readFrames(buffer, frame_sample_size);
                currZCR = ZeroCrossingRate.computeZCR01(buffer[ch_num], normalizer);
                currAmp = WavFileManip.computeAvrgAbsAmplitude(buffer[ch_num]);

                //if ( framesRead > 0 ) currFrameSampleNum++;

                if (currZCR > zcr_thresh || currAmp > amp_thresh) {
                    //System.out.println("frames_read == " + frames_read);
                    if (frames_read > 0) {
                        for (int i = 0; i < buffer[ch_num].length; i++) {
                            //System.out.println("adding " + buffer[ch_num][i]);
                            amp_list.add(new Double(buffer[ch_num][i]));
                        }
                    }
                }
            } while (frames_read != 0);

            inWavFile.close();

        } catch (IOException | WavFileException e) {
            // TODO Auto-generated catch block
            System.err.println(e.toString());
        }

        return amp_list;
    }

    public static double[] wavFileToNonSilenceAmpArray(String wav_in_path,
            int ch_num, int frame_sample_size,
            double zcr_thresh, double amp_thresh) {

        ArrayList<Double> amp_list = new ArrayList<>();

        try {
            int frames_read = 0;
            double currZCR = 0;
            double currAmp = 0.0;
            WavFile inWavFile = WavFile.openWavFile(new File(wav_in_path));
            long sample_rate = inWavFile.getSampleRate();
            double normalizer = WavFileManip.convertFrameSampleSizeToSeconds((int) sample_rate, frame_sample_size);

            // Get the number of audio channels in the wav file
            int num_channels = inWavFile.getNumChannels();

            System.out.println("WavFile's number of frames: " + inWavFile.getNumFrames());
            System.out.println("WavFile's sample rate: " + inWavFile.getSampleRate());

            // Create a buffer of frame_sample_size frames
            double[][] buffer = new double[num_channels][frame_sample_size];

            do {
                frames_read = inWavFile.readFrames(buffer, frame_sample_size);
                currZCR = ZeroCrossingRate.computeZCR01(buffer[ch_num], normalizer);
                currAmp = WavFileManip.computeAvrgAbsAmplitude(buffer[ch_num]);

                //if ( framesRead > 0 ) currFrameSampleNum++;

                if (currZCR > zcr_thresh || currAmp > amp_thresh) {
                    //System.out.println("frames_read == " + frames_read);
                    if (frames_read > 0) {
                        for (int i = 0; i < buffer[ch_num].length; i++) {
                            //System.out.println("adding " + buffer[ch_num][i]);
                            amp_list.add(new Double(buffer[ch_num][i]));
                        }
                    }
                }
            } while (frames_read != 0);

            inWavFile.close();

        } catch (IOException | WavFileException e) {
            // TODO Auto-generated catch block
            System.err.println(e.toString());
        }

        double[] amp_ary = new double[amp_list.size()];
        for (int i = 0; i < amp_list.size(); i++) {
            amp_ary[i] = amp_list.get(i).doubleValue();
        }
        amp_list = null;

        return amp_ary;
    }
    
    public static ArrayList<double[]> wavFileToListOfNonSilenceSegments(String wav_in_path,
            int ch_num, int frame_sample_size, double zcr_thresh, double amp_thresh) {

        ArrayList<double[]> arrayOfSegments = new ArrayList<>();

        try {
            int frames_read = 0;
            double currZCR = 0;
            double currAmp = 0.0;
            WavFile inWavFile = WavFile.openWavFile(new File(wav_in_path));
            long sample_rate = inWavFile.getSampleRate();
            double normalizer = WavFileManip.convertFrameSampleSizeToSeconds((int) sample_rate, frame_sample_size);

            // Get the number of audio channels in the wav file
            int num_channels = inWavFile.getNumChannels();

            System.out.println("WavFile's number of frames: " + inWavFile.getNumFrames());
            System.out.println("WavFile's sample rate: " + inWavFile.getSampleRate());

            // Create a buffer of frame_sample_size frames
            double[][] buffer = new double[num_channels][frame_sample_size];

            do {
                frames_read = inWavFile.readFrames(buffer, frame_sample_size);
                currZCR = ZeroCrossingRate.computeZCR01(buffer[ch_num], normalizer);
                currAmp = WavFileManip.computeAvrgAbsAmplitude(buffer[ch_num]);

                //if ( framesRead > 0 ) currFrameSampleNum++;
                double[] segment = null;
                if (currZCR > zcr_thresh || currAmp > amp_thresh) {
                    //System.out.println("frames_read == " + frames_read);
                    if (frames_read > 0) {
                        segment = new double[buffer[ch_num].length];
                        System.arraycopy(buffer[ch_num], 0, segment, 0, buffer[ch_num].length);
                        arrayOfSegments.add(segment);
                    }
                }
            } while (frames_read != 0);

            inWavFile.close();

        } catch (IOException | WavFileException e) {
            // TODO Auto-generated catch block
            System.err.println(e.toString());
        }

        return arrayOfSegments;
    }
    
    public static void detectSilence(String in_path, String out_path, String out_txt_path,
            int frame_sample_size, int channel_num, double zcr_thresh, double amp_thresh) {
        try {
            // Open the wav file specified as the first argument
            WavFile inWavFile = WavFile.openWavFile(new File(in_path));
            WavFile outWavFile = WavFile.newWavFile(new File(out_path),
                    inWavFile.getNumChannels(), inWavFile.getNumFrames(),
                    inWavFile.getValidBits(), inWavFile.getSampleRate());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(out_txt_path)));

            // Display information about the wav file
            inWavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = inWavFile.getNumChannels();

            System.out.println("WavFile's number of frames: " + inWavFile.getNumFrames());
            System.out.println("WavFile's sample rate: " + inWavFile.getSampleRate());

            // Create a buffer of frame_sample_size frames
            double[][] buffer = new double[numChannels][frame_sample_size];

            int framesRead;
            int framesWritten;
            long sample_rate = inWavFile.getSampleRate();
            // normalizer is a number of seconds
            double normalizer = WavFileManip.convertFrameSampleSizeToSeconds((int) sample_rate, frame_sample_size);
            int currFrameSampleNum = 0;
            double currZCR = 0;
            double totalAvrgAmp = 0.0;
            double currAmp = 0.0;
            DecimalFormat df = new DecimalFormat("0.000");

            do {
                framesRead = inWavFile.readFrames(buffer, frame_sample_size);
                currZCR = ZeroCrossingRate.computeZCR01(buffer[channel_num], normalizer);
                currAmp = WavFileManip.computeAvrgAbsAmplitude(buffer[channel_num]);

                if (framesRead > 0) {
                    currFrameSampleNum++;
                }

                // in silence, zero crossing rate is lower && current amp is lower
                if (currZCR <= zcr_thresh && currAmp <= amp_thresh) {
                    String tabbed_output = "";
                    tabbed_output += currFrameSampleNum + "\t";
                    tabbed_output += df.format(currZCR) + "\t";
                    tabbed_output += df.format(currAmp) + "\n";
                    totalAvrgAmp += currAmp;
                    framesWritten = outWavFile.writeFrames(buffer, framesRead);
                    bos.write(tabbed_output.getBytes());
                }
            } while (framesRead != 0);

            inWavFile.close();
            outWavFile.close();
            bos.flush();
            bos.close();

        } catch (IOException | WavFileException e) {
            System.err.println(e.toString());
        }
    }

    // modify this to create a database of ArrayList<Double> words.
    // AudioDictionary
    // String --> ArrayList<Double>
    // It takes a file of a word and returns ArrayList<Double>
    // - ArrayList<Double> nonSilenceWavFileToAmpArray(String in_path, int channel_num)
    // - Write a file that takes a Directory and returns a Hash of words mapped to 
    // ArrayList<Double>.
    // - Write a file that takes an ArrayList<Double> and returns the best match
    // in the table of ArrayList<Double>
    public static void detectNonSilence(String in_path, String out_path, String out_txt_path,
            int frame_sample_size, int channel_num, double zcr_thresh, double amp_thresh) {
        try {
            // Open the wav file specified as the first argument
            WavFile inWavFile = WavFile.openWavFile(new File(in_path));
            WavFile outWavFile = WavFile.newWavFile(new File(out_path),
                    inWavFile.getNumChannels(), inWavFile.getNumFrames(),
                    inWavFile.getValidBits(), inWavFile.getSampleRate());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(out_txt_path)));

            // Display information about the wav file
            inWavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = inWavFile.getNumChannels();

            System.out.println("WavFile's number of frames: " + inWavFile.getNumFrames());
            System.out.println("WavFile's sample rate: " + inWavFile.getSampleRate());

            // Create a buffer of frame_sample_size frames
            double[][] buffer = new double[numChannels][frame_sample_size];

            int framesRead;
            int framesWritten;
            long sample_rate = inWavFile.getSampleRate();
            double normalizer = WavFileManip.convertFrameSampleSizeToSeconds((int) sample_rate, frame_sample_size);
            int currFrameSampleNum = 0;
            double currZCR = 0;
            double totalAvrgAmp = 0.0;
            double currAmp = 0.0;
            DecimalFormat df = new DecimalFormat("0.000");

            // - buffer[channel_num] is a double array of length frame_sample_size.
            // Here is an algorithm. Have an ArrayList<Double> that keeps adding Doubles
            // from buffer[channel_num] so long as buffer is non-silence.
            // Once you have ArrayList<Double>, use DTW on ArrayList<Double> to match it
            // with a pre-stored word patterns, each of which is ArrayList<Double>.
            // Here is what we need to implement:
            // - dp_dtw_cost_matrix(int n, int m, ArrayList<Double> xseq, ArrayList<Double> yseq, 
            //                      ISimilarity isim, max_cost)
            // - get_dp_owp(int n, int m, double[][] cost_matrix)
            // - get_owp(int n, int m, double[][] cost_matrix)
            // - dp_dtw_cost_matrix_subseq(ArrayList<Double> xseq, ArrayList<Double> yseq, ISimilarity isim,
            //   							int max_cost)
            // - find_b_star(int n, int m, double[][] cost_matrix, double max_cost)
            // - find_a_star(optimal warping path)
            // - dp_dtw_cost_matrix_subseq(ArrayList<Double> xseq, ArrayList<Double> yseq, ISimilarity isim, 
            //							double max_cost)
            // - dp_dtw_subseq(ArrayList<Double> xseq, ArrayList<Double> yseq, double max_cost=1000)

            do {
                framesRead = inWavFile.readFrames(buffer, frame_sample_size);
                currZCR = ZeroCrossingRate.computeZCR01(buffer[channel_num], normalizer);
                currAmp = WavFileManip.computeAvrgAbsAmplitude(buffer[channel_num]);

                if (framesRead > 0) {
                    currFrameSampleNum++;
                }

                if (currZCR > zcr_thresh || currAmp > amp_thresh) {
                    String tabbed_output = "";
                    tabbed_output += currFrameSampleNum + "\t";
                    tabbed_output += df.format(currZCR) + "\t";
                    tabbed_output += df.format(currAmp) + "\n";
                    totalAvrgAmp += currAmp;
                    // This is where we can get buffer[num_channels] and compute 
                    // its Haar Coefficients
                    framesWritten = outWavFile.writeFrames(buffer, framesRead);
                    bos.write(tabbed_output.getBytes());
                }
            } while (framesRead != 0);

            inWavFile.close();
            outWavFile.close();
            bos.flush();
            bos.close();

        } catch (IOException | WavFileException e) {
            System.err.println(e.toString());
        }
    }
    
}