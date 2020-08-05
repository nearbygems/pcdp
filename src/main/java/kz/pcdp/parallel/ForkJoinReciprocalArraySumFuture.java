package kz.pcdp.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static kz.pcdp.util.Util.printResults;
import static kz.pcdp.util.Util.seqArraySum;

public class ForkJoinReciprocalArraySumFuture {

  public static void main(String[] args) {

    System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "4");

    double[] x = new double[100000000];

    for (int i = 1; i < 100000001; i++) {
      x[i - 1] = Math.random() * i;
    }

    for (int i = 1; i < 6; i++) {
      System.out.println("Run " + i);
      seqArraySum(x);
      parArraySumFuture(x);
    }
  }

  public static void parArraySumFuture(double[] x) {
    long startTime = System.nanoTime();

    SumArray t = new SumArray(x, 0, x.length);
    double sum = ForkJoinPool.commonPool().invoke(t);

    long timeInNanos = System.nanoTime() - startTime;
    printResults("parArraySumFuture", timeInNanos, sum);
  }

  private static class SumArray extends RecursiveTask<Double> {
    static int SEQUENTIAL_THRESHOLD = 1000;
    int lo;
    int hi;
    double[] arr;

    SumArray(double[] a, int l, int h) {
      lo = l;
      hi = h;
      arr = a;
    }

    protected Double compute() {
      if (hi - lo <= SEQUENTIAL_THRESHOLD) {
        double sum = 0;
        for (int i = lo; i < hi; ++i) {
          sum += 1 / arr[i];
        }
        return sum;
      } else {
        SumArray left = new SumArray(arr, lo, (hi + lo) / 2);
        SumArray right = new SumArray(arr, (hi + lo) / 2, hi);
        left.fork();
        double rightSum = right.compute();
        double leftSum = left.join();
        return leftSum + rightSum;

      }
    }
  }

}
