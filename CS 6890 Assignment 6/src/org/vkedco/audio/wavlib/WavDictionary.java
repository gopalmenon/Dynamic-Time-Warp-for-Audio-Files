/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vkedco.audio.wavlib;

/**
 *
 * @author vladimir kulyukin
 * 
 * bugs to vladimir dot kulyukin at gmail dot com
 */
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/*
 * buildAudioPacketDictionary method works as follows: each wav file is split into non-silence chunks.
 * Each non-silence chunk is represented as an array of segments. A segment is
 * a double[] of even length. The algorithm should accept zcr_threshold and
 * amp_thresh.
 * When an input file is given, the algorithm splits it into non-silence chunks.
 * Then it goes through the dictionary and does the DTW match on the 1st chunks 
 * of each entry. Those entries that pass the threshold, continue to the 2nd round.
 * The algorithm can be parameterized by how many chunks need to match before
 * the final match is returned.
 * Another modification is to keep the entry in even when a certain number of
 * mismatches has taken place.
 * Another interesting modification is to select a random sample of packets
 * from the input and match it against the same selection of packets from a
 * dictionary entry. 
 */

public class WavDictionary {

    static TreeMap<String, ArrayList<Double>> mLists = null;
    static TreeMap<String, double[]> mArrays = null;
    static TreeMap<String, ArrayList<double[]>> mSegments = null;

    public static void addAudioAmpList(String word, ArrayList<Double> amp_list) {
        mLists.put(word, amp_list);
    }

    public static void addAudioAmpArray(String word, double[] amp_ary) {
        mArrays.put(word, amp_ary);
    }
    
    public static void addAudioArrayOfSegments(String word, ArrayList<double[]> arrayOfSegments) {
        mSegments.put(word, arrayOfSegments);
    }
    
    public static void displayAudioArraySegments() {
        for(Entry<String, ArrayList<double[]>> e : mSegments.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue().size());
        }
    }

    public static ArrayList<Double> getAmpList(String word) {
        return mLists.get(word);
    }

    public static double[] getAmpArray(String word) {
        return mArrays.get(word);
    }
    
    public static ArrayList<double[]> getSegmentList(String word) {
        return mSegments.get(word);
    }

    public static int getArrayDictionarySize() {
        return mArrays.size();
    }

    public static int getListDictionarySize() {
        return mLists.size();
    }
    
    public TreeMap<String, ArrayList<double[]>> getSegments() {
        return mSegments;
    }
    
    public TreeMap<String, ArrayList<Double>> getLists() {
        return mLists;
    }
    
    public TreeMap<String, double[]> getArrays() {
        return mArrays;
    }
    
    public static void buildSegmentDictionary(String dir_path, int ch_num, int frame_sample_size,
            double zcr_thresh, double amp_thresh) {
        mSegments = new TreeMap<>();
        File dir = new File(dir_path);

        if (dir.isDirectory()) {
            for (String f : dir.list(new FilenameFilter() {
                public boolean accept(File directory, String fileName) {
                    return fileName.endsWith(".wav");
                }
            })) {
                System.out.println(f);
                WavDictionary.addAudioArrayOfSegments(f.substring(0, f.indexOf(".")),
                        WavSpeech.wavFileToListOfNonSilenceSegments(dir_path + f, ch_num, 
                                                    frame_sample_size, zcr_thresh, amp_thresh));
            }
        }
        
    }
    
    public static void buildListDictionary(String dir_path, int ch_num, int frame_sample_size,
            double zcr_thresh, double amp_thresh) {
        mLists = new TreeMap<>();
        File dir = new File(dir_path);

        if (dir.isDirectory()) {
            for (String f : dir.list(new FilenameFilter() {
                public boolean accept(File directory, String fileName) {
                    return fileName.endsWith(".wav");
                }
            })) {
                System.out.println(f);
                WavDictionary.addAudioAmpList(f.substring(0, f.indexOf(".")),
                        WavSpeech.wavFileToNonSilenceAmpArrayList(dir_path + f, ch_num, frame_sample_size, zcr_thresh, amp_thresh));
            }
        }
    }

    public static void buildArrayDictionary(String dir_path, int ch_num, int frame_sample_size,
            double zcr_thresh, double amp_thresh) {
        mArrays = new TreeMap<>();
        File dir = new File(dir_path);

        if (dir.isDirectory()) {
            for (String f : dir.list(new FilenameFilter() {
                public boolean accept(File directory, String fileName) {
                    return fileName.endsWith(".wav");
                }
            })) {
                System.out.println(f);
                WavDictionary.addAudioAmpArray(f.substring(0, f.indexOf(".")),
                        WavSpeech.wavFileToNonSilenceAmpArray(dir_path + f, ch_num, frame_sample_size, zcr_thresh, amp_thresh));
            }
        }
    }
    
    public static Set<String> getListKeys() {
        return mLists.keySet();
    }

    public static Set<String> getArrayKeys() {
        return mArrays.keySet();
    }
    
    public static Set<String> getSegmentKeys() {
        return mSegments.keySet();
    } 
}