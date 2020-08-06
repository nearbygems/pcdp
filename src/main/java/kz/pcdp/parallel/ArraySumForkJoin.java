package kz.pcdp.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import static kz.pcdp.util.Util.printResults;
import static kz.pcdp.util.Util.seqArraySum;

public class ArraySumForkJoin {

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

  public static void parArraySum(double[] x) {
    long startTime = System.nanoTime();

    SumArray t = new SumArray(x, 0, x.length);
    ForkJoinPool.commonPool().invoke(t);

    double sum = t.ans;

    long timeInNanos = System.nanoTime() - startTime;
    printResults("parArraySum", timeInNanos, sum);
  }

  private static class SumArray extends RecursiveAction {
    static int SEQUENTIAL_THRESHOLD = 1000;
    int lo;
    int hi;
    double[] arr;
    double ans = 0;

    SumArray(double[] a, int l, int h) {
      lo = l;
      hi = h;
      arr = a;
    }

    protected void compute() {
      if (hi - lo <= SEQUENTIAL_THRESHOLD) {
        for (int i = lo; i < hi; ++i) {
          ans += 1 / arr[i];
        }
      } else {
        SumArray left = new SumArray(arr, lo, (hi + lo) / 2);
        SumArray right = new SumArray(arr, (hi + lo) / 2, hi);
        left.fork();
        right.compute();
        left.join();
        ans = left.ans + right.ans;
      }
    }
  }
}
