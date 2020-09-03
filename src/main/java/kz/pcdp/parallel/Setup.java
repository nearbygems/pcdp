package kz.pcdp.parallel;

import static edu.rice.pcdp.PCDP.async;
import static edu.rice.pcdp.PCDP.finish;

public final class Setup {

  private Setup() { }

  public static int setup(final int val) {
    final int[] result = new int[1];
    finish(() -> {
      async(() -> {
        result[0] = val;
      });
    });
    return result[0];
  }
}
