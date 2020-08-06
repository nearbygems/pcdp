package kz.pcdp.parallel;

import static edu.rice.pcdp.PCDP.async;
import static edu.rice.pcdp.PCDP.finish;
import static kz.pcdp.util.Util.printResults;

public class ArraySumAsyncFinish {

  public static void main(String[] args) {

    System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "2");

    double[] x = new double[100000000];

    for (int i = 1; i < 100000001; i++) {
      x[i - 1] = Math.random() * i;
    }

    for (int i = 1; i < 6; i++) {
      System.out.println("Run " + i);
      seqArraySum(x);
      parArraySum(x);
    }
  }

  public static void seqArraySum(double[] x) {
    long startTime = System.nanoTime();
    double sum1 = 0;
    double sum2 = 0;

    for (int i = 0; i < x.length / 2; i++) {
      sum1 += 1 / x[i];
    }

    for (int i = x.length / 2; i < x.length; i++) {
      sum2 += 1 / x[i];
    }

    double sum = sum1 + sum2;

    long timeInNanos = System.nanoTime() - startTime;
    printResults("seqArraySum", timeInNanos, sum);
  }

  public static void parArraySum(double[] x) {
    long startTime = System.nanoTime();
    double sum1 = 0;
    double sum2 = 0;

    finish(() -> {
      async(() -> {
        for (int i = 0; i < x.length / 2; i++) {
          sum1 += 1 / x[i];
        }
      });

      for (int i = x.length / 2; i < x.length; i++) {
        sum2 += 1 / x[i];
      }
    });

    double sum = sum1 + sum2;

    long timeInNanos = System.nanoTime() - startTime;
    printResults("parArraySum", timeInNanos, sum);
  }
}
