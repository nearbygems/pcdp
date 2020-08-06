package kz.pcdp.parallel;

import static edu.rice.pcdp.PCDP.*;
import static kz.pcdp.util.Util.printResults;

public class MatrixMultiply {

  public static void main(String[] args) {

    double[][] A = new double[10000][10000];

    for (int i = 1; i < 10001; i++) {
      for (int j = 1; j < 10001; j++) {
        A[i - 1][j - 1] = Math.random() * i;
      }
    }

    double[][] B = new double[10000][10000];

    for (int i = 1; i < 10001; i++) {
      for (int j = 1; j < 10001; j++) {
        B[i - 1][j - 1] = Math.random() * i;
      }
    }

    double[][] C = new double[10000][10000];

    for (int i = 1; i < 6; i++) {
      System.out.println("Run " + i);
      seqMatrixMultiply(A, B, C, 50);
      parMatrixMultiply(A, B, C, 50);
    }
  }

  public static void seqMatrixMultiply(double[][] A, double[][] B, double[][] C, int n) {
    long startTime = System.nanoTime();

    forseq2d(0, n - 1, 0, n - 1, (i, j) -> {
      C[i][j] = 0;
      for (int k = 0; k < n; k++) {
        C[i][j] += A[i][j] * B[i][j];
      }
    });

    long timeInNanos = System.nanoTime() - startTime;
    printResults("seqMatrixMultiply", timeInNanos, C[n - 1][n - 1]);
  }

  public static void parMatrixMultiply(double[][] A, double[][] B, double[][] C, int n) {
    long startTime = System.nanoTime();

    forall2dChunked(0, n - 1, 0, n - 1, (i, j) -> {
      C[i][j] = 0;
      for (int k = 0; k < n; k++) {
        C[i][j] += A[i][j] * B[i][j];
      }
    });

    long timeInNanos = System.nanoTime() - startTime;
    printResults("parMatrixMultiply", timeInNanos, C[n - 1][n - 1]);
  }
}
