package org.vkedco.nlp.audiotrials;

// bugs to vladimir dot kulyukin at gmail dot com

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

public class WavFileManip {

    public static void readWavFileDouble(String in_path) {
        try {
            // Open the wav file specified as the first argument
            WavFile wavFile = WavFile.openWavFile(new File(in_path));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();

            // Create a buffer of 100 frames
            double[] buffer = new double[100 * numChannels];

            int framesRead;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            do {
                // Read frames into buffer
                framesRead = wavFile.readFrames(buffer, 100);

                // Loop through frames and look for minimum and maximum value
                for (int s = 0; s < framesRead * numChannels; s++) {
                    if (buffer[s] > max) {
                        max = buffer[s];
                    }
                    if (buffer[s] < min) {
                        min = buffer[s];
                    }
                }
            } while (framesRead != 0);

            // Close the wavFile
            wavFile.close();

            // Output the minimum and maximum value
            System.out.printf("Min: %f, Max: %f\n", min, max);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void readWavFileDouble2(String in_path) {
        try {
            // Open the wav file specified as the first argument
            WavFile wavFile = WavFile.openWavFile(new File(in_path));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();

            // Create a buffer of 100 frames
            double[][] buffer = new double[numChannels][100];

            int framesRead;
            int frameNum = 0;

            do {
                // Read frames into buffer
                framesRead = wavFile.readFrames(buffer, 100);
                System.out.println(framesRead);

                for (int fn = 0; fn < framesRead; fn++) {
                    for (int ch = 0; ch < numChannels; ch++) {
                        System.out.print(ch + ", " + frameNum + ": "
                                + buffer[ch][fn] + " ");
                        // if ( buffer[ch][fn] != 0 ) { wavFile.close(); return;
                        // }
                    }
                    frameNum++;
                    System.out.println();
                }
            } while (framesRead != 0);

            // Close the wavFile
            wavFile.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static Hashtable<Integer, ArrayList<Double>> readWavFileDouble3(
            String in_path) {
        try {
            // Open the wav file specified as the first argument
            WavFile wavFile = WavFile.openWavFile(new File(in_path));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();

            System.out.println("WavFile's number of frames: "
                    + wavFile.getNumFrames());
            System.out.println("WavFile's sample rate: "
                    + wavFile.getSampleRate());

            // Create a buffer of 100 frames
            double[][] buffer = new double[numChannels][100];

            Hashtable<Integer, ArrayList<Double>> vectorTable = new Hashtable<Integer, ArrayList<Double>>();
            ArrayList<Integer> channelKeys = new ArrayList<Integer>();

            for (int ch = 0; ch < numChannels; ch++) {
                channelKeys.add(new Integer(ch));
            }

            for (Integer ch : channelKeys) {
                vectorTable.put(ch, new ArrayList<Double>());
            }

            int framesRead;
            int frameNum = 0;

            do {
                // Read frames into buffer
                framesRead = wavFile.readFrames(buffer, 100);
                // System.out.println(framesRead);

                for (int fn = 0; fn < framesRead; fn++) {
                    for (Integer ch : channelKeys) {
                        // System.out.print(ch + ", " + frameNum + ": " +
                        // buffer[ch][fn] + " ");
                        // if ( buffer[ch][fn] != 0 ) { wavFile.close(); return;
                        // }
                        vectorTable.get(ch).add(
                                new Double(buffer[ch.intValue()][fn]));
                    }
                    // frameNum++;
                    // System.out.println();
                }
            } while (framesRead != 0);

            // Close the wavFile
            wavFile.close();

            return vectorTable;

        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public static void readWavFileDouble4(String in_path, int frame_sample_size) {
        try {
            // Open the wav file specified as the first argument
            WavFile wavFile = WavFile.openWavFile(new File(in_path));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();

            System.out.println("WavFile's number of frames: "
                    + wavFile.getNumFrames());
            System.out.println("WavFile's sample rate: "
                    + wavFile.getSampleRate());

            // Create a buffer of frame_sample_size frames
            double[][] buffer = new double[numChannels][frame_sample_size];

            // Hashtable<Integer, ArrayList<Double>> vectorTable = new
            // Hashtable<Integer, ArrayList<Double>>();
            // ArrayList<Integer> channelKeys = new ArrayList<Integer>();

            // for(int ch = 0; ch < numChannels; ch++) {
            // channelKeys.add(new Integer(ch));
            // }

            // for(Integer ch: channelKeys) {
            // vectorTable.put(ch, new ArrayList<Double>());
            // }

            int framesRead;
            long sample_rate = wavFile.getSampleRate();
            double normalizer = WavFileManip.convertFrameSampleSizeToSeconds(
                    (int) sample_rate, frame_sample_size);
            double zcrTotal = 0;
            long totalFrameSamples = 0;
            double currZCR = 0;

            do {
                // Read frames into buffer
                framesRead = wavFile.readFrames(buffer, frame_sample_size);
                if (framesRead != 0) {
                    totalFrameSamples += 1;
                }
                // System.out.println(framesRead);

                for (int ch = 0; ch < numChannels; ch++) {
                    currZCR = ZeroCrossingRate.computeZCR01(buffer[ch],
                            normalizer);
                    System.out.println("ZCR(" + frame_sample_size + ", " + ch
                            + ")=" + currZCR);
                    zcrTotal += currZCR;
                }
            } while (framesRead != 0);

            // Close the wavFile
            wavFile.close();

            System.out.println("Mean ZCR = " + zcrTotal / totalFrameSamples);

            // return vectorTable;

        } catch (Exception e) {
            System.err.println(e);
            // return null;
        }
    }

    public static ArrayList<Integer> segmentWordsByZCR(String in_path, int frame_sample_size, int channel_num,
            double threshold) {
        ArrayList<Integer> wordFrameSamples = new ArrayList<Integer>();

        try {
            // Open the wav file specified as the first argument
            WavFile wavFile = WavFile.openWavFile(new File(in_path));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();

            System.out.println("WavFile's number of frames: " + wavFile.getNumFrames());
            System.out.println("WavFile's sample rate: " + wavFile.getSampleRate());

            // Create a buffer of frame_sample_size frames
            double[][] buffer = new double[numChannels][frame_sample_size];

            int framesRead;
            long sample_rate = wavFile.getSampleRate();
            double normalizer = WavFileManip.convertFrameSampleSizeToSeconds((int) sample_rate, frame_sample_size);
            int currFrameSample = 0;
            double currZCR = 0;

            do {
                // Read frames into buffer
                framesRead = wavFile.readFrames(buffer, frame_sample_size);

                for (int ch = 0; ch < numChannels; ch++) {
                    currZCR = ZeroCrossingRate.computeZCR01(buffer[ch], normalizer);
                    System.out.println("ZCR(" + frame_sample_size + ", " + ch + ")=" + currZCR);
                    if (currZCR > threshold && ch == channel_num) {
                        wordFrameSamples.add(new Integer(currFrameSample));
                    }
                }
                if (framesRead != 0) {
                    currFrameSample += 1;
                }
            } while (framesRead != 0);

            // Close the wavFile
            wavFile.close();

            return wordFrameSamples;

        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public static void segmentWordsByZCR02(String in_path, String out_path,
            int frame_sample_size, int channel_num, double threshold) {
        try {
            // Open the wav file specified as the first argument
            WavFile inWavFile = WavFile.openWavFile(new File(in_path));
            WavFile outWavFile = WavFile.newWavFile(new File(out_path),
                    inWavFile.getNumChannels(), inWavFile.getNumFrames(),
                    inWavFile.getValidBits(), inWavFile.getSampleRate());

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

            do {
                // Read frames into buffer
                framesRead = inWavFile.readFrames(buffer, frame_sample_size);
                currZCR = ZeroCrossingRate.computeZCR01(buffer[channel_num], normalizer);
                if (framesRead > 0) {
                    currFrameSampleNum++;
                }

                if (currZCR > threshold) {
                    System.out.println("ZCR(" + frame_sample_size + ", " + channel_num + ", " + currFrameSampleNum + ")=" + currZCR);
                    framesWritten = outWavFile.writeFrames(buffer, framesRead);
                    System.out.println("framesWritten " + framesWritten);
                    System.out.println("buffer len 0 = " + buffer[0].length);
                    System.out.println("buffer len 1 = " + buffer[1].length);
                    System.out.println("frames remaining = " + outWavFile.getFramesRemaining());
                }
                //System.out.println("offset = " + offset);
            } while (framesRead != 0);

            System.out.println(335 * WavFileManip.convertFrameSampleSizeToSeconds(44100, frame_sample_size));
            System.out.println(500 * WavFileManip.convertFrameSampleSizeToSeconds(44100, frame_sample_size));
            inWavFile.close();
            outWavFile.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void writeFrameSamples(String in_path, String out_path,
            int frame_sample_size, int channel_num, int from, int upto) {
        try {
            // Open the wav file specified as the first argument
            WavFile inWavFile = WavFile.openWavFile(new File(in_path));
            WavFile outWavFile = WavFile.newWavFile(new File(out_path),
                    inWavFile.getNumChannels(), inWavFile.getNumFrames(),
                    inWavFile.getValidBits(), inWavFile.getSampleRate());

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
            double zcrTotal = 0.0;
            double totalAvrgAmp = 0.0;
            double currAmp = 0.0;
            DecimalFormat df = new DecimalFormat("0.000");

            do {
                // Read frames into buffer
                framesRead = inWavFile.readFrames(buffer, frame_sample_size);
                currZCR = ZeroCrossingRate.computeZCR01(buffer[channel_num], normalizer);
                zcrTotal += currZCR;

                if (framesRead > 0) {
                    currFrameSampleNum++;
                }

                if (currFrameSampleNum >= from && currFrameSampleNum <= upto) {
                    System.out.println("ZCR(" + frame_sample_size + ", " + channel_num + ", " + currFrameSampleNum + ")=" + currZCR);
                    currAmp = WavFileManip.computeAvrgAbsAmplitude(buffer[channel_num]);
                    totalAvrgAmp += currAmp;
                    System.out.println("AvrgAmp = " + df.format(currAmp));
                    framesWritten = outWavFile.writeFrames(buffer, framesRead);
                    //System.out.println("framesWritten " + framesWritten);
                    //System.out.println("buffer len 0 = " + buffer[0].length);
                    //System.out.println("buffer len 1 = " + buffer[1].length);
                    //System.out.println("frames remaining = " + outWavFile.getFramesRemaining());

                }

                //System.out.println("offset = " + offset);
            } while (framesRead != 0);

            System.out.println(280 * WavFileManip.convertFrameSampleSizeToSeconds(44100, frame_sample_size));
            System.out.println(500 * WavFileManip.convertFrameSampleSizeToSeconds(44100, frame_sample_size));
            System.out.println("Mean ZCR = " + zcrTotal / currFrameSampleNum);
            System.out.println("Mean AMP = " + totalAvrgAmp / currFrameSampleNum);

            inWavFile.close();
            outWavFile.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void writeFrameSamples02(String in_path, String out_path, String out_txt_path,
            int frame_sample_size, int channel_num, int from, int upto) {
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
            double zcrTotal = 0.0;
            double totalAvrgAmp = 0.0;
            double currAmp = 0.0;
            DecimalFormat df = new DecimalFormat("0.000");

            do {
                // Read frames into buffer
                framesRead = inWavFile.readFrames(buffer, frame_sample_size);
                currZCR = ZeroCrossingRate.computeZCR01(buffer[channel_num], normalizer);
                zcrTotal += currZCR;

                if (framesRead > 0) {
                    currFrameSampleNum++;
                }

                if (currFrameSampleNum >= from && currFrameSampleNum <= upto) {
                    String tabbed_output = "";
                    tabbed_output += currFrameSampleNum + "\t";
                    tabbed_output += df.format(currZCR) + "\t";
                    System.out.println("ZCR(" + frame_sample_size + ", " + channel_num + ", " + currFrameSampleNum + ")=" + currZCR);
                    currAmp = WavFileManip.computeAvrgAbsAmplitude(buffer[channel_num]);
                    tabbed_output += df.format(currAmp) + "\n";
                    totalAvrgAmp += currAmp;
                    System.out.println("AvrgAmp = " + df.format(currAmp));
                    framesWritten = outWavFile.writeFrames(buffer, framesRead);
                    bos.write(tabbed_output.getBytes());

                    //System.out.println("framesWritten " + framesWritten);
                    //System.out.println("buffer len 0 = " + buffer[0].length);
                    //System.out.println("buffer len 1 = " + buffer[1].length);
                    //System.out.println("frames remaining = " + outWavFile.getFramesRemaining());

                }

                //System.out.println("offset = " + offset);
            } while (framesRead != 0);

            System.out.println(280 * WavFileManip.convertFrameSampleSizeToSeconds(44100, frame_sample_size));
            System.out.println(500 * WavFileManip.convertFrameSampleSizeToSeconds(44100, frame_sample_size));
            System.out.println("Mean ZCR = " + zcrTotal / currFrameSampleNum);
            System.out.println("Mean AMP = " + totalAvrgAmp / currFrameSampleNum);

            inWavFile.close();
            outWavFile.close();
            bos.flush();
            bos.close();

        } catch (Exception e) {
            System.err.println(e);
        }
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
                // in silence, zero crossing rate is lower && current amp is low
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

        } catch (Exception e) {
            System.err.println(e);
        }
    }

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
                    framesWritten = outWavFile.writeFrames(buffer, framesRead);
                    bos.write(tabbed_output.getBytes());
                }
            } while (framesRead != 0);

            inWavFile.close();
            outWavFile.close();
            bos.flush();
            bos.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static int convertFrameSampleSizeToMilliSeconds(int sample_rate,
            int frame_sample_size) {
        return (int) ((frame_sample_size * 1000.0) / sample_rate);
    }

    public static double convertFrameSampleSizeToSeconds(int sample_rate,
            int frame_sample_size) {
        return convertFrameSampleSizeToMilliSeconds(sample_rate,
                frame_sample_size) / 1000.0;
    }

    public static int convertMilliSecondsToFrameSampleSize(int sample_rate,
            int num_ms) {
        return (int) ((num_ms * sample_rate) / 1000.0);
    }

    public static int convertSecondsToFrameSampleSize(int sample_rate,
            double num_secs) {
        return convertMilliSecondsToFrameSampleSize(sample_rate,
                (int) (num_secs * 1000));
    }

    public static double computeAvrgAbsAmplitude(double[] signals) {
        double total = 0.0;
        for (int i = 0; i < signals.length; i++) {
            total += Math.abs(signals[i]);
        }
        return total / signals.length;
    }
}