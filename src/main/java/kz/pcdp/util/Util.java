package kz.pcdp.util;

public class Util {

  public static void seqArraySum(double[] x) {
    long startTime = System.nanoTime();
    double sum = 0;

    for (double v : x) {
      sum += 1 / v;
    }

    long timeInNanos = System.nanoTime() - startTime;
    Util.printResults("seqArraySum", timeInNanos, sum);
  }

  public static void printResults(String name, long timeInNanos, double sum) {
    System.out.printf("  %s completed in %8.3f milliseconds, with sum = %8.5f \n", name, timeInNanos / 1e6, sum);
  }
}
