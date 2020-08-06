package kz.pcdp.parallel;

import java.util.concurrent.Phaser;

public class AveragingPhaser {

  double[] myVal;
  double[] myNew;
  int n;

  public void runSequential(final int iterations) {
    for (int iter = 0; iter < iterations; iter++) {
      for (int j = 1; j <= n; j++) {
        myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
      }

      double[] temp = myNew;
      myNew = myVal;
      myVal = temp;
    }
  }

  public void runForAllBarrier(final int iterations, final int tasks) {
    Phaser ph = new Phaser(0);
    ph.bulkRegister(tasks);

    Thread[] threads = new Thread[tasks];

    for (int ii = 0; ii < tasks; ii++) {
      int i = ii;
      threads[ii] = new Thread(() -> {
        double[] myVal = this.myVal;
        double[] myNew = this.myNew;

        for (int iter = 0; iter < iterations; iter++) {
          int left = i * (n / tasks) + 1;
          int right = (i + 1) * (n / tasks);

          for (int j = left; j <= right; j++) {
            myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
          }

          ph.arriveAndAwaitAdvance();

          double[] temp = myNew;
          myNew = myVal;
          myVal = temp;
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

  public void runForAllFuzzyBarrier(final int iterations, final int tasks) {
    Phaser ph = new Phaser(0);
    ph.bulkRegister(tasks);

    Thread[] threads = new Thread[tasks];

    for (int ii = 0; ii < tasks; ii++) {
      int i = ii;
      threads[ii] = new Thread(() -> {
        double[] myVal = this.myVal;
        double[] myNew = this.myNew;

        for (int iter = 0; iter < iterations; iter++) {
          int left = i * (n / tasks) + 1;
          myNew[left] = (myVal[left - 1] + myVal[left + 1]) / 2.0;

          int right = (i + 1) * (n / tasks);
          myNew[right] = (myVal[right - 1] + myVal[right + 1]) / 2.0;

          int currentPhase = ph.arrive();

          for (int j = left + 1; j <= right - 1; j++) {
            myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
          }

          ph.awaitAdvance(currentPhase);

          double[] temp = myNew;
          myNew = myVal;
          myVal = temp;
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
