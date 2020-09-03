package kz.pcdp.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public final class ReciprocalArraySum {

  private ReciprocalArraySum() { }

  protected static double seqArraySum(final double[] input) {
    double sum = 0;
    for (double v : input) {
      sum += 1 / v;
    }
    return sum;
  }

  private static int getChunkSize(final int nChunks, final int nElements) {
    return (nElements + nChunks - 1) / nChunks;
  }

  private static int getChunkStartInclusive(final int chunk, final int nChunks, final int nElements) {
    final int chunkSize = getChunkSize(nChunks, nElements);
    return chunk * chunkSize;
  }

  private static int getChunkEndExclusive(final int chunk, final int nChunks, final int nElements) {
    final int chunkSize = getChunkSize(nChunks, nElements);
    final int end = (chunk + 1) * chunkSize;
    if (end > nElements) {
      return nElements;
    } else {
      return end;
    }
  }

  private static class ReciprocalArraySumTask
    extends RecursiveAction {

    private static final int SEQUENTIAL_THRESHOLD = 58000;

    private final int startIndexInclusive;

    private final int endIndexExclusive;

    private final double[] input;

    private double value;

    ReciprocalArraySumTask(final int setStartIndexInclusive, final int setEndIndexExclusive, final double[] setInput) {
      this.startIndexInclusive = setStartIndexInclusive;
      this.endIndexExclusive = setEndIndexExclusive;
      this.input = setInput;
    }

    public double getValue() {
      return value;
    }

    @Override
    protected void compute() {
      int range = this.endIndexExclusive - this.startIndexInclusive;
      if (range <= SEQUENTIAL_THRESHOLD) {
        for (int i = this.startIndexInclusive; i < this.endIndexExclusive; i++) {
          this.value += 1 / this.input[i];
        }
      } else {
        int middle = (this.startIndexInclusive + this.endIndexExclusive) / 2;
        ReciprocalArraySumTask leftSum = new ReciprocalArraySumTask(this.startIndexInclusive, middle, this.input);
        ReciprocalArraySumTask rightSum = new ReciprocalArraySumTask(middle, this.endIndexExclusive, this.input);
        leftSum.fork();
        rightSum.compute();
        leftSum.join();
        this.value = leftSum.getValue() + rightSum.getValue();
      }
    }
  }

  protected static double parArraySum(final double[] input) {
    assert input.length % 2 == 0;
    return parManyTaskArraySum(input, 2);
  }

  protected static double parManyTaskArraySum(final double[] input, final int numTasks) {
    assert input.length % 2 == 0;
    double sum = 0;
    List<ReciprocalArraySumTask> taskList = new ArrayList<>();

    for (int i = 0; i < numTasks; i++) {
      taskList.add(new ReciprocalArraySumTask(getChunkStartInclusive(i, numTasks, input.length),
        getChunkEndExclusive(i, numTasks, input.length), input));
    }

    ForkJoinTask.invokeAll(taskList);

    for (ReciprocalArraySumTask task : taskList) {
      sum += task.getValue();
    }

    return sum;
  }
}
