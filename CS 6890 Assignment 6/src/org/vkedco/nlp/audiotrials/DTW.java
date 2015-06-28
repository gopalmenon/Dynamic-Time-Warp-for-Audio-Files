package org.vkedco.nlp.audiotrials;

// An implementation of Classical Dynamic Time Warping
// as outlined in M. Muller. Information Retrieval for Music and 
// Motion, Ch.04. Springer, ISBN 978-3-540-74047-6
// Bugs to vladimir dot kulyukin at gmail dot com.

import java.util.ArrayList;

public class DTW {
 
    static ArrayList<Double> XSEQ_01 = new ArrayList<Double>();
    static ArrayList<Double> YSEQ_01 = new ArrayList<Double>();
    static ArrayList<Double> ZSEQ_01 = new ArrayList<Double>();
    final static Integer INTEGER_ONE = new Integer(1);
    final static double MAX_VAL = 100;

    // 0 -> None
    // a -> 1
    // b -> 2
    // c -> 3
    // g -> 4
    static {
        // XSEQ_01 == 'abg'
        XSEQ_01.add(new Double(1));
        XSEQ_01.add(new Double(2));
        XSEQ_01.add(new Double(4));

        // YSEQ_01 = 'abbg'
        YSEQ_01.add(new Double(1));
        YSEQ_01.add(new Double(2));
        YSEQ_01.add(new Double(2));
        YSEQ_01.add(new Double(4));

        // ZSEQ_01 = 'agg'
        ZSEQ_01.add(new Double(1));
        ZSEQ_01.add(new Double(4));
        ZSEQ_01.add(new Double(4));
    }
    
    // compute DTW cost matrix for the sequences xseq and yseq.
    // isim is a similarity metric.
    static double[][] dp_dtw_cost_matrix(ArrayList<Double> xseq, ArrayList<Double> yseq,
            IDTWSimilarity isim) {
        double[][] cost_matrix = allocate_cost_matrix(xseq.size(), yseq.size());
        dp_dtw_cost_matrix_aux(xseq, yseq, cost_matrix, isim);
        return cost_matrix;
    }

    static void dp_dtw_cost_matrix_aux(ArrayList<Double> xseq, ArrayList<Double> yseq,
            double[][] cost_matrix, IDTWSimilarity isim) {
        int ncols = xseq.size();
        int nrows = yseq.size();
        System.out.println("ncols=" + ncols);
        System.out.println("nrows=" + nrows);

        cost_matrix[0][0] = isim.compare(xseq.get(0), yseq.get(0));
    
        System.out.println("check 00");
        for (int r = 1; r < nrows; r++) {
            cost_matrix[r][0] = cost_matrix[r-1][0] + 
                                isim.compare(xseq.get(0), yseq.get(r));
        }
        System.out.println("check 01");
        System.out.println("Initial Cost Matrix 01:");
        display_cost_matrix(cost_matrix, xseq.size(), yseq.size());

        for (int c = 1; c < ncols; c++) {
            cost_matrix[0][c] = cost_matrix[0][c-1]+ 
                                isim.compare(xseq.get(c), yseq.get(0));
        }
        System.out.println("check 02");
        System.out.println("Initial Cost Matrix 02:");
        display_cost_matrix(cost_matrix, xseq.size(), yseq.size());
        System.out.println("check 03");
        for (int r = 1; r < nrows; r++) {
            for (int c = 1; c < ncols; c++) {
                System.out.println("r,c=" + r + "," + c);
                double min_cost = Math.min(cost_matrix[r - 1][c - 1], cost_matrix[r - 1][c]);
                min_cost = Math.min(cost_matrix[r][c - 1], min_cost);
                min_cost += isim.compare(xseq.get(c), yseq.get(r));
                cost_matrix[r][c] = min_cost;
            }
        }
    }

    static double[][] dp_dtw_2col_cost_matrix(ArrayList<Double> xseq, ArrayList<Double> yseq,
            IDTWSimilarity isim) {
        final int NROWS = yseq.size();
        final int NCOLS = xseq.size();
        double cost_matrix[][] = new double[NROWS][2];
        
        // compute 0th column
        cost_matrix[0][0] = isim.compare(xseq.get(0), yseq.get(0));
        for(int r = 1; r < NROWS; r++) {
            cost_matrix[r][0] = cost_matrix[r-1][0] + 
                                isim.compare(xseq.get(0), yseq.get(1));
        }
        // compute 1th column
        cost_matrix[0][1] = isim.compare(xseq.get(1), yseq.get(0)) + 
                            cost_matrix[0][0];
        for(int r = 1; r < NROWS; r++) {
            double min_cost = Math.min(cost_matrix[r - 1][0], cost_matrix[r - 1][1]);
            min_cost = Math.min(cost_matrix[r][0], min_cost);
            min_cost += isim.compare(xseq.get(1), yseq.get(r));
            cost_matrix[r][1] = min_cost;
        }
        
        double min_cost;
        for (int c = 2; c < NCOLS; c++) {
            DTW.swap_cols_0_and_1(cost_matrix, NROWS);
            cost_matrix[0][1] = cost_matrix[0][0] + isim.compare(xseq.get(c), yseq.get(0));
            for (int r = 1; r < NROWS; r++) {
                min_cost = Math.min(cost_matrix[r - 1][0], cost_matrix[r - 1][1]);
                min_cost = Math.min(cost_matrix[r][0], min_cost);
                min_cost += isim.compare(xseq.get(c), yseq.get(r));
                cost_matrix[r][1] = min_cost;
            }
        }

        //System.out.println("End Cost Matrix 02:");
        //display_cost_matrix(cost_matrix, xseq.size(), TEMP_NCOLS);
        return cost_matrix;
    }
    
    static double[][] allocate_cost_matrix(int ncols, int nrows) {
        double[][] cost_matrix = new double[nrows][];
        for (int r = 0; r < nrows; r++) {
            cost_matrix[r] = new double[ncols];
            for (int c = 0; c < ncols; c++) {
                cost_matrix[r][c] = 0.0;
            }
        }
        return cost_matrix;
    }

    static void swap_cols_0_and_1(double[][] cost_matrix, int nrows) {
        for (int r = 0; r < nrows; r++) {
            cost_matrix[r][0] = cost_matrix[r][1];
            cost_matrix[r][1] = 0;
        }
    }

    static void display_cost_matrix(double[][] cm, int ncols, int nrows) {
        for (int r = 0; r < nrows; r++) {
            for (int c = 0; c < ncols; c++) {
                System.out.print(cm[r][c] + " ");
            }
            System.out.println();
        }
    }

    static void display_owp(ArrayList<Pair<Integer, Integer>> owp) {
        for (Pair<Integer, Integer> p : owp) {
            System.out.print(p.toString() + " ");
        }
        System.out.println();
    }

    // TESTS
    static void dtw_comp_xseq_01_yseq_01() {
        double[][] cost_matrix = DTW.dp_dtw_cost_matrix(XSEQ_01, YSEQ_01, new LDSim01());
        System.out.println("Cost Matrix:");
        display_cost_matrix(cost_matrix, XSEQ_01.size(), YSEQ_01.size());

    }

    static void dtw_comp_xseq_01_zseq_01() {
        double[][] cost_matrix = DTW.dp_dtw_cost_matrix(XSEQ_01, ZSEQ_01, new LDSim01());
        System.out.println("Cost Matrix:");
        display_cost_matrix(cost_matrix, XSEQ_01.size(), ZSEQ_01.size());
    }
    
    static void dtw_comp_yseq_01_zseq_01() {
        double[][] cost_matrix = DTW.dp_dtw_cost_matrix(YSEQ_01, ZSEQ_01, new LDSim01());
        System.out.println("Cost Matrix:");
        display_cost_matrix(cost_matrix, YSEQ_01.size(), ZSEQ_01.size());
    }

    static void dtw_owp_2col_cost_matrix_aux_xseq_01_yseq_01() {
        IDTWSimilarity isim = new LDSim01();
        double[][] cost_matrix = DTW.dp_dtw_2col_cost_matrix(XSEQ_01, YSEQ_01, isim);
        DTW.display_cost_matrix(cost_matrix, 2, YSEQ_01.size());
    }
    
    static void dtw_owp_2col_cost_matrix_aux_yseq_01_zseq_01() {
        IDTWSimilarity isim = new LDSim01();
        double[][] cost_matrix = DTW.dp_dtw_2col_cost_matrix(YSEQ_01, ZSEQ_01, isim);
        DTW.display_cost_matrix(cost_matrix, 2, ZSEQ_01.size());
    }

    public static void main(String[] args) {    
        DTW.dtw_comp_xseq_01_yseq_01();
        DTW.dtw_owp_2col_cost_matrix_aux_xseq_01_yseq_01();
    }
}