package menon.cs6890.assignment6;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

import org.vkedco.audio.wavlib.WavDictionary;
import org.vkedco.audio.wavlib.WavSpeech;

public class MainClass {
	
	private static final int DEFAULT_CHANNEL_NUMBER = 1;
	private static final int DEFAULT_FRAME_SAMPLE_SIZE = 2024;
	private static final int DEFAULT_MAXIMUM_SEGMENTS_TO_MATCH = 14;//Integer.MAX_VALUE;
	private static final int DEFAULT_MAXIMUM_ELEMENT_TO_MATCH = 2000;
	private static final double DEFAULT_ZERO_CROSSING_THRESHOLD = 1270.0;
	private static final double DEFAULT_AMPLITUDE_THRESHOLD = 0.005;
	private static final boolean COMBINE_SEGMENTS_DEFAULT = false;
	
	public static void main(String[] args) {
		
		MainClass mainClass = new MainClass();
		
		//Get the directory path and input file names from the user
		String directoryPath = mainClass.getDictionaryDirectoryName();
		String inputFileName = mainClass.getInputFileName();
		
		//Match the input file against each dictionary entry and print out the warp distance
		mainClass.matchAudioFileAgainstSegmentDictionary(directoryPath, inputFileName, DEFAULT_CHANNEL_NUMBER, DEFAULT_FRAME_SAMPLE_SIZE, DEFAULT_ZERO_CROSSING_THRESHOLD, DEFAULT_AMPLITUDE_THRESHOLD, DEFAULT_MAXIMUM_SEGMENTS_TO_MATCH);

	}
	
	/**
	 * @param dict_dir
	 * @param input_word
	 * @param channel_num
	 * @param frame_sample_size
	 * @param zcr_thresh
	 * @param amp_thresh
	 * @param num_segments
	 */
	private void matchAudioFileAgainstSegmentDictionary(String dict_dir, String input_word, int channel_num, int frame_sample_size, double zcr_thresh, double amp_thresh, int num_segments) {
		
		//Build the dictionary of audio files
		WavDictionary.buildSegmentDictionary(dict_dir, channel_num, frame_sample_size, zcr_thresh, amp_thresh);
		Set<String> segmentKeys = WavDictionary.getSegmentKeys();
		
		//Convert input file into non-silent segments
		ArrayList<double[]> inputAudioSegments = WavSpeech.wavFileToListOfNonSilenceSegments(input_word, channel_num, frame_sample_size, zcr_thresh, amp_thresh);			
		
		double warpDistance = 0.0;
		
		//Try and match the input file against each entry in the dictionary
		
		for (String segmentKey : segmentKeys) {
			ArrayList<double[]> directoryEntryAudioSegments= WavDictionary.getSegmentList(segmentKey);
			warpDistance = warpInputFileToDirectoryEntry(inputAudioSegments, directoryEntryAudioSegments, num_segments, COMBINE_SEGMENTS_DEFAULT);
			System.out.println(input_word + ", " + segmentKey + " --> " + warpDistance);
		}
		
	}
	
	private double warpInputFileToDirectoryEntry(ArrayList<double[]> inputAudioSegments, ArrayList<double[]> directoryEntryAudioSegments, int maximumSegmentsToMatch, boolean combineSegments) {
		
		double returnValue = 0.0;
		
		//Find maximum number of segments to process based on minimum value of input sizes
		int maximumSegmentsToProcess = Math.min(Math.min(inputAudioSegments.size(), directoryEntryAudioSegments.size()), maximumSegmentsToMatch);
		
		if (combineSegments) {
			
			//Find number of values in all input segments
			int inputValues = 0;
			for (double[] inputSegment : inputAudioSegments) {
				inputValues += inputSegment.length;
			}			
			int maxInputValues = Math.min(inputValues, DEFAULT_MAXIMUM_ELEMENT_TO_MATCH);

			//Combine all the input segments
			double[] inputArray = new double[maxInputValues];
			int inputIndex = 0;
			
			outerFor1:
			for (double[] inputSegment : inputAudioSegments) {
				for (int inputSegmentIndex = 0; inputSegmentIndex < inputSegment.length; ++inputSegmentIndex) {
					if (++inputIndex >= maxInputValues) {
						break outerFor1;
					}
					inputArray[inputIndex] = inputSegment[inputSegmentIndex];
				}
			}
			
			//Find number of values in all directory entry segments
			int directoryEntryValues = 0;
			for (double[] directoryEntrySegment : directoryEntryAudioSegments) {
				directoryEntryValues += directoryEntrySegment.length;
			}
			int maxDirectoryEntryValues = Math.min(directoryEntryValues, DEFAULT_MAXIMUM_ELEMENT_TO_MATCH);
			
			//Combine all the directory entry segments	
			double[] directoryEntryArray = new double[maxDirectoryEntryValues];
			int directoryEntryIndex = 0;
			
			outerFor2:
			for (double[] directoryEntrySegment : directoryEntryAudioSegments) {
				for (int directoryEntrySegmentIndex = 0; directoryEntrySegmentIndex < directoryEntrySegment.length; ++directoryEntrySegmentIndex) {
					if (++directoryEntryIndex >= maxInputValues) {
						break outerFor2;
					}
					directoryEntryArray[directoryEntryIndex] = directoryEntrySegment[directoryEntrySegmentIndex];
				}
			}			
			
			//Process combined segments and find the warp distance
			DynamicTimeWarpEditDistanceTable dynamicTimeWarpEditDistanceTable = new DynamicTimeWarpEditDistanceTable(directoryEntryArray, inputArray);
			dynamicTimeWarpEditDistanceTable.warpSourceToTarget();
			returnValue += dynamicTimeWarpEditDistanceTable.getWarpDistance();
			
		} else {
			
			//Process each segment and find the warp distance
			for (int segmentIndex = 0; segmentIndex < maximumSegmentsToProcess; ++segmentIndex) {
				DynamicTimeWarpEditDistanceTable dynamicTimeWarpEditDistanceTable = new DynamicTimeWarpEditDistanceTable(directoryEntryAudioSegments.get(segmentIndex), inputAudioSegments.get(segmentIndex));
				dynamicTimeWarpEditDistanceTable.warpSourceToTarget();
				returnValue += dynamicTimeWarpEditDistanceTable.getWarpDistance();
			}
		}
		return returnValue;
		
	}
	
	//Get the dictionary directory from the user
	private String getDictionaryDirectoryName() {
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String userInput = null, returnValue = null;
		File dictionaryDirectory = null;
		
		System.out.println("Enter the directory containing the dictionary: ");
		try {
			userInput = bufferedReader.readLine();
			if (userInput == null || userInput.trim().length() == 0) {
				System.err.println("Error: directory containing the dictionary needs to be specified.");
				System.exit(0);
			} else {
				dictionaryDirectory = new File(userInput.trim());
				if (!dictionaryDirectory.isDirectory()) {
					System.err.println("Error: directory " + userInput.trim() + " not found.");
					System.exit(0);
				}
			}
			returnValue = dictionaryDirectory.getCanonicalPath();
		} catch (IOException e) {
			System.err.println("IOException while getting user input. " + e);
			System.exit(0);
		}	
		
		return returnValue + "/";
	}
	
	//Get name of input audio file
	private String getInputFileName() {
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String userInput = null, returnValue = null;
		File inputFile = null;
		
		
		System.out.println("Enter the input file name along with path: ");
		try {
			userInput = bufferedReader.readLine();
			if (userInput == null || userInput.trim().length() == 0) {
				System.err.println("Error: input file needs to be specified.");
				System.exit(0);
			} else {
				inputFile = new File(userInput.trim());
				if (!inputFile.isFile()) {
					System.err.println("Error: input file " + userInput.trim() + " not found.");
					System.exit(0);
				}
			}
			returnValue = inputFile.getCanonicalPath();
		} catch (IOException e) {
			System.err.println("IOException while getting user input. " + e);
		}
		
		return returnValue;
	}

}
