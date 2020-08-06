package kz.pcdp.parallel;

import static edu.rice.pcdp.PCDP.*;

public class AveragingBarrier {

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

  public void runForAll(final int iterations) {
    for (int iter = 0; iter < iterations; iter++) {
      forall(1, n, (j) -> {
        myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
      });

      double[] temp = myNew;
      myNew = myVal;
      myVal = temp;
    }
  }

  public void runForAllGrouped(final int iterations, final int tasks) {
    for (int iter = 0; iter < iterations; iter++) {
      forall(1, tasks - 1, (i) -> {
        for (int j = i * (n / tasks) + 1; j <= (i + 1) * (n / tasks); i++) {
          myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
        }
      });

      double[] temp = myNew;
      myNew = myVal;
      myVal = temp;
    }
  }

  public void runForAllBarrier(final int iterations, final int tasks) {
    forallPhased(0, tasks - 1, (i) -> {
      double[] myVal = this.myVal;
      double[] myNew = this.myNew;

      for (int iter = 0; iter < iterations; iter++) {
        for (int j = i * (n / tasks) + 1; j <= (i + 1) * (n / tasks); i++) {
          myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
        }

        next();

        double[] temp = myNew;
        myNew = myVal;
        myVal = temp;
      }
    });
  }
}
