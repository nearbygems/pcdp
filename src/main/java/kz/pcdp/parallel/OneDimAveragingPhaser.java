package kz.pcdp.parallel;

import java.util.concurrent.Phaser;

public final class OneDimAveragingPhaser {

  private OneDimAveragingPhaser() { }

  public static void runSequential(final int iterations, final double[] myNew,
                                   final double[] myVal, final int n) {
    double[] next = myNew;
    double[] curr = myVal;

    for (int iter = 0; iter < iterations; iter++) {
      for (int j = 1; j <= n; j++) {
        next[j] = (curr[j - 1] + curr[j + 1]) / 2.0;
      }
      double[] tmp = curr;
      curr = next;
      next = tmp;
    }
  }

  public static void runParallelBarrier(final int iterations,
                                        final double[] myNew, final double[] myVal, final int n,
                                        final int tasks) {
    Phaser ph = new Phaser(0);
    ph.bulkRegister(tasks);

    Thread[] threads = new Thread[tasks];

    for (int ii = 0; ii < tasks; ii++) {
      final int i = ii;

      threads[ii] = new Thread(() -> {
        double[] threadPrivateMyVal = myVal;
        double[] threadPrivateMyNew = myNew;

        for (int iter = 0; iter < iterations; iter++) {
          final int left = i * (n / tasks) + 1;
          final int right = (i + 1) * (n / tasks);

          for (int j = left; j <= right; j++) {
            threadPrivateMyNew[j] = (threadPrivateMyVal[j - 1]
              + threadPrivateMyVal[j + 1]) / 2.0;
          }
          ph.arriveAndAwaitAdvance();

          double[] temp = threadPrivateMyNew;
          threadPrivateMyNew = threadPrivateMyVal;
          threadPrivateMyVal = temp;
        }
      });
      threads[ii].start();
    }

    for (int ii = 0; ii < tasks; ii++) {
      try {
        threads[ii].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void runParallelFuzzyBarrier(final int iterations,
                                             final double[] myNew, final double[] myVal, final int n,
                                             final int tasks) {
    Phaser[] phs = new Phaser[tasks];
    for (int i = 0; i < phs.length; i++) {
      phs[i] = new Phaser(1);
    }

    Thread[] threads = new Thread[tasks];

    for (int ii = 0; ii < tasks; ii++) {
      final int i = ii;

      threads[ii] = new Thread(() -> {
        double[] threadPrivateMyVal = myVal;
        double[] threadPrivateMyNew = myNew;

        for (int iter = 0; iter < iterations; iter++) {
          final int left = i * (n / tasks) + 1;
          final int right = (i + 1) * (n / tasks);

          for (int j = left; j <= right; j++) {
            threadPrivateMyNew[j] = (threadPrivateMyVal[j - 1]
              + threadPrivateMyVal[j + 1]) / 2.0;
          }
          int currentPhase = phs[i].arrive();
          if (i - 1 >= 0) {
            phs[i - 1].awaitAdvance(currentPhase);
          }
          if (i + 1 < tasks) {
            phs[i + 1].awaitAdvance(currentPhase);
          }

          double[] temp = threadPrivateMyNew;
          threadPrivateMyNew = threadPrivateMyVal;
          threadPrivateMyVal = temp;
        }
      });
      threads[ii].start();
    }

    for (int ii = 0; ii < tasks; ii++) {
      try {
        threads[ii].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
