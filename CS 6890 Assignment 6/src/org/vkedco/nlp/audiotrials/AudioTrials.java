package org.vkedco.nlp.audiotrials;

// Manipulation of AudioInputStream and detection of silence and
// non-silence in audio files.
// Bugs to vladimir dot kulyukin at gmail dot com

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class AudioTrials {
    
    // change this to the directory where you place the audio files
    static final String AUDIO_DIR = 
            "C:/Users/Vladimir/Dropbox/MyShare/TEACHING/CS6890_NLP/support_materials/audio_files/";
    
    static final String AUDIO_DICTIONARY = AUDIO_DIR + "wav_audio_dictionary/";

    // take a path to an audio file and print various statistics of
    // that file such as total number of frames read, format encoding,
    // sample size, number of channels, etc.
    static void readSoundBytes(String audioPath, String format) {
        int totalFramesRead = 0;
        File fileIn = new File(audioPath);
        try {
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(fileIn);

            AudioFormat aufrmt = audioInputStream.getFormat();

            System.out.println("Audio Format: " + audioInputStream.getFormat());
            System.out.println("Audio Format Encoding: " + aufrmt.getEncoding());
            System.out.println("Audio Format Sample Size Bits: "
                    + aufrmt.getSampleSizeInBits());
            System.out.println("Audio Format Num Channels: " + aufrmt.getChannels());
            System.out.println("Audio Format Frame Size in Bytes: " + aufrmt.getFrameSize());

            int bytesPerFrame =
                    audioInputStream.getFormat().getFrameSize();

            if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
                bytesPerFrame = 1;
            }

            System.out.println("bytesPerFrame == " + bytesPerFrame);

            int numBytes = 1024 * bytesPerFrame;

            byte[] audioBytes = new byte[numBytes];

            //displayBytes(audioInputStream);

            try {
                int numBytesRead = 0;
                int numFramesRead = 0;
                while ((numBytesRead = audioInputStream.read(audioBytes))
                        != -1) {
                    numFramesRead = numBytesRead / bytesPerFrame;
                    totalFramesRead += numFramesRead;
                    //System.out.println("numFramesRead == " + numFramesRead);
                }
            } catch (Exception ex) {
                System.err.println(ex.toString());
            }

            System.out.print(format + ": ");
            int b;
            for (int i = 0; i < audioBytes.length; i++) {
                b = audioBytes[i];
                System.out.print(b + " ");
            }

        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
    
    static void audioTrial_01() {
        System.out.println("MS=" + WavFileManip.convertFrameSampleSizeToMilliSeconds(44100, 200));
        System.out.println("SEC=" + WavFileManip.convertFrameSampleSizeToSeconds(44100, 200));
        System.out.println("Frames=" + WavFileManip.convertMilliSecondsToFrameSampleSize(44100, 4));
        System.out.println("Frames=" + WavFileManip.convertSecondsToFrameSampleSize(44100, 0.25));
    }
    
    static void audioTrial_02() {
        WavFileManip.writeFrameSamples02(AUDIO_DIR + "calcium.wav",
                                         AUDIO_DIR + "calcium_ca.wav",
                                         AUDIO_DIR + "calcium_ca.txt",
                                         200, 0, 290, 312);
    }
    
    static void audioTrial_03() {
        WavFileManip.writeFrameSamples02(AUDIO_DIR + "calcium.wav",
                AUDIO_DIR + "calcium_a.wav",
                AUDIO_DIR + "calcium_a.txt",
                200, 0, 313, 338);
    }
    
    // detect silence and non-silence in file calcium.wav
    static void audioTrial_04() {
        // CALCIUM
        WavFileManip.detectSilence(AUDIO_DIR + "calcium.wav",
                AUDIO_DIR + "calcium_silence.wav",
                AUDIO_DIR + "calcium_silence2.txt",
                200, 0, 1270.0, 0.005);
        
        WavFileManip.detectNonSilence(AUDIO_DIR + "calcium.wav",
                AUDIO_DIR + "calcium_non_silence.wav",
                AUDIO_DIR + "calcium_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
     // detect silence and non-silence in file calorie.wav
    static void audioTrial_05() {
       // CALORIE
        WavFileManip.detectSilence(AUDIO_DIR + "calorie.wav",
                AUDIO_DIR + "calorie_silence.wav",
                AUDIO_DIR + "calorie_silence2.txt",
                200, 0, 1270.0, 0.005);
        
        WavFileManip.detectNonSilence(AUDIO_DIR + "calorie.wav",
                AUDIO_DIR + "calorie_non_silence.wav",
                AUDIO_DIR + "calorie_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file calories.wav
    static void audioTrial_06() {
        // CALORIES
        WavFileManip.detectSilence(AUDIO_DIR + "calories.wav",
                AUDIO_DIR + "calories_silence.wav",
                AUDIO_DIR + "calories_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "calories.wav",
                AUDIO_DIR + "calories_non_silence.wav",
                AUDIO_DIR + "calories_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file carbohydrates.wav
    static void audioTrial_07() {
        // CARBOHYDRATES
        WavFileManip.detectSilence(AUDIO_DIR + "carbohydrates.wav",
                AUDIO_DIR + "carbohydrates_silence.wav",
                AUDIO_DIR + "carbohydrates_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "carbohydrates.wav",
                AUDIO_DIR + "carbohydrates_non_silence.wav",
                AUDIO_DIR + "carbohydrates_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file cholesterol.wav
    static void audioTrial_08() {
        // CHOLESTEROL
        WavFileManip.detectSilence(AUDIO_DIR + "cholesterol.wav",
                AUDIO_DIR + "cholesterol_silence.wav",
                AUDIO_DIR + "cholesterol_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "cholesterol.wav",
                AUDIO_DIR + "cholesterol_non_silence.wav",
                AUDIO_DIR + "cholesterol_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file dietary_fiber.wav
    static void audioTrial_09() {
         // DIETARY FIBER
        WavFileManip.detectSilence(AUDIO_DIR + "dietary_fiber.wav",
                AUDIO_DIR + "dietary_fiber_silence.wav",
                AUDIO_DIR + "dietary_fiber_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "dietary_fiber.wav",
                AUDIO_DIR + "dietary_fiber_non_silence.wav",
                AUDIO_DIR + "dietary_fiber_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file fact.wav
    static void audioTrial_10() {
        // FACT
        WavFileManip.detectSilence(AUDIO_DIR + "fact.wav",
                AUDIO_DIR + "fact_silence.wav",
                AUDIO_DIR + "fact_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "fact.wav",
                AUDIO_DIR + "fact_non_silence.wav",
                AUDIO_DIR + "fact_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file facts.wav
    static void audioTrial_11() {
        // FACTS
        WavFileManip.detectSilence(AUDIO_DIR + "facts.wav",
                AUDIO_DIR + "facts_silence.wav",
                AUDIO_DIR + "facts_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "facts.wav",
                AUDIO_DIR + "facts_non_silence.wav",
                AUDIO_DIR + "facts_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file ingredient.wav
    static void audioTrial_12() {
        // INGREDIENT
        WavFileManip.detectSilence(AUDIO_DIR + "ingredient.wav",
                AUDIO_DIR + "ingredient_silence.wav",
                AUDIO_DIR + "ingredient_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "ingredient.wav",
                AUDIO_DIR + "ingredient_non_silence.wav",
                AUDIO_DIR + "ingredient_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file ingredients.wav
    static void audioTrial_13() {
        // INGREDIENTS
        WavFileManip.detectSilence(AUDIO_DIR + "ingredients.wav",
                AUDIO_DIR + "ingredients_silence.wav",
                AUDIO_DIR + "ingredients_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "ingredients.wav",
                AUDIO_DIR + "ingredients_non_silence.wav",
                AUDIO_DIR + "ingredients_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file nutrition.wav
    static void audioTrial_14() {
        // NUTRITION
        WavFileManip.detectSilence(AUDIO_DIR + "nutrition.wav",
                AUDIO_DIR + "nutrition_silence.wav",
                AUDIO_DIR + "nutrition_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "nutrition.wav",
                AUDIO_DIR + "nutrition_non_silence.wav",
                AUDIO_DIR + "nutrition_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file nutrition_facts.wav
    static void audioTrial_15() {
        // NUTRITION FACTS
        WavFileManip.detectSilence(AUDIO_DIR + "nutrition_facts.wav",
                AUDIO_DIR + "nutrition_facts_silence.wav",
                AUDIO_DIR + "nutrition_facts_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "nutrition_facts.wav",
                AUDIO_DIR + "nutrition_facts_non_silence.wav",
                AUDIO_DIR + "nutrition_facts_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file saturated_fat.wav
    static void audioTrial_16() {
       // SATURATED FAT
        WavFileManip.detectSilence(AUDIO_DIR + "saturated_fat.wav",
                AUDIO_DIR + "saturated_fat_silence.wav",
                AUDIO_DIR + "saturated_fat_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "saturated_fat.wav",
                AUDIO_DIR + "saturated_fat_non_silence.wav",
                AUDIO_DIR + "saturated_fat_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in file serving_size.wav
    static void audioTrial_17() {
       // SERVING SIZE
        WavFileManip.detectSilence(AUDIO_DIR + "serving_size.wav",
                AUDIO_DIR + "serving_size_silence.wav",
                AUDIO_DIR + "serving_size_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "serving_size.wav",
                AUDIO_DIR + "serving_size_non_silence.wav",
                AUDIO_DIR + "serving_size_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in total_carbohydrate.wav
    static void audioTrial_18() {
        // TOTAL CARBOHYDRATE
        WavFileManip.detectSilence(AUDIO_DIR + "total_carbohydrate.wav",
                AUDIO_DIR + "total_carbohydrate_silence.wav",
                AUDIO_DIR + "total_carbohydrate_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "total_carbohydrate.wav",
                AUDIO_DIR + "total_carbohydrate_non_silence.wav",
                AUDIO_DIR + "total_carbohydrate_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in total_fat.wav
    static void audioTrial_19() {
        // TOTAL FAT
        WavFileManip.detectSilence(AUDIO_DIR + "total_fat.wav",
                AUDIO_DIR + "total_fat_silence.wav",
                AUDIO_DIR + "total_fat_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "total_fat.wav",
                AUDIO_DIR + "total_fat_non_silence.wav",
                AUDIO_DIR + "total_fat_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in trans_fat.wav
    static void audioTrial_20() {
        // TOTAL FAT
        WavFileManip.detectSilence(AUDIO_DIR + "trans_fat.wav",
                AUDIO_DIR + "trans_fat_silence.wav",
                AUDIO_DIR + "trans_fat_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "trans_fat.wav",
                AUDIO_DIR + "trans_fat_non_silence.wav",
                AUDIO_DIR + "trans_fat_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    
    // detect silence and non-silence in protein.wav
    static void audioTrial_21() {
         // PROTEIN
        WavFileManip.detectSilence(AUDIO_DIR + "protein.wav",
                AUDIO_DIR + "protein_silence.wav",
                AUDIO_DIR + "protein_silence2.txt",
                200, 0, 1270.0, 0.005);

        WavFileManip.detectNonSilence(AUDIO_DIR + "protein.wav",
                AUDIO_DIR + "protein_non_silence.wav",
                AUDIO_DIR + "protein_non_silence2.txt",
                200, 0, 1270.0, 0.005);
    }
    

    public static void main(String[] args) {
        audioTrial_09();
    }

    final static void displayBytes(AudioInputStream ais) {
        int b;
        System.out.println("displayBytes");
        try {
            while ((b = ais.read()) != -1) {
                System.out.println("amplitude == " + b);
            }
            System.out.println("done");
        } catch (IOException ex) {
        }
        System.out.println("done");
    }

    final static ArrayList<Integer> audioFileToArrayList(String inputPath) {
        int b;
        ArrayList<Integer> vector = new ArrayList<Integer>();
        try {
            File inFile;
            try {
                inFile = new File(inputPath);
            } catch (NullPointerException ex) {
                System.out.println("Error: on of the convertFileToAIFF" + " parameters is null!");
                return null;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(inFile);
            while ((b = ais.read()) != -1) {
                vector.add(new Integer(b));
            }
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } catch (UnsupportedAudioFileException ex) {
            System.err.println(ex.toString());
        }
        return vector;
    }

    final static void convertFileToAIFF(String inputPath, String outputPath) {
        AudioFileFormat inFileFormat;
        File inFile;
        File outFile;
        try {
            inFile = new File(inputPath);
            outFile = new File(outputPath);
        } catch (NullPointerException ex) {
            System.out.println("Error: on of the convertFileToAIFF" + " parameters is null!");
            return;
        }
        try {
            inFileFormat = AudioSystem.getAudioFileFormat(inFile);
            System.out.println(inFileFormat + "\n");
            if (inFileFormat.getType() != AudioFileFormat.Type.AIFF) {
                System.out.println("Check 00");
                AudioInputStream inFileAIS = AudioSystem.getAudioInputStream(inFile);
                AudioFileFormat.Type[] types = AudioSystem.getAudioFileTypes(inFileAIS);
                for (AudioFileFormat.Type t : types) {
                    System.out.println(t);
                }
                System.out.println("Check 01");
                //inFileAIS.reset();
                System.out.println("Check 02");
                if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.AIFF, inFileAIS)) {
                    System.out.println("Conversion is possible!");
                    AudioSystem.write(inFileAIS, AudioFileFormat.Type.AIFF, outFile);
                    System.out.println("Successfully made AIFF file, "
                            + outFile.getPath() + ", from "
                            + inFileFormat.getType() + " file, "
                            + inFile.getPath() + ".");
                    inFileAIS.close();
                    return;
                } else {
                    System.out.println("Warning: AIFF conversion of "
                            + inFile.getPath()
                            + " is not currently supported by AudioSystem.");
                }
            } else {
                System.out.println("Input file " + inFile.getPath()
                        + " is AIFF." + " Conversion is unnecessary.");
            }
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("Error: " + inFile.getPath()
                    + " is not a supported audio file type!");
            return;
        } catch (IOException ex) {
            System.out.println("Error: failure attempting to read " + inFile.getPath() + "!");
            return;
        }
    }

    public static int convertFrameSampleSizeToMilliSeconds(int sample_rate, int frame_sample_size) {
        return (int) (frame_sample_size * 1000.0) / sample_rate;
    }
}