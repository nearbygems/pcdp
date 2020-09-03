package kz.pcdp.distributed.matrix;

import static kz.pcdp.distributed.matrix.MPI.MPIException;
import static kz.pcdp.distributed.matrix.MPI.MPI_Request;

public class MatrixMult {

  public static void parallelMatrixMultiply(Matrix firstMatrix, Matrix secondMatrix,
                                            Matrix resultMatrix, final MPI mpi) throws MPIException {

    final int myrank = mpi.MPI_Comm_rank(mpi.MPI_COMM_WORLD);
    final int size = mpi.MPI_Comm_size(mpi.MPI_COMM_WORLD);

    final int nrows = resultMatrix.getNRows();
    final int rowChunk = (nrows + size - 1) / size;
    final int startRow = myrank * rowChunk;
    int endRow = (myrank + 1) * rowChunk;
    if (endRow > nrows) {
      endRow = nrows;
    }

    mpi.MPI_Bcast(firstMatrix.getValues(), 0, firstMatrix.getNRows() * firstMatrix.getNCols(), 0, mpi.MPI_COMM_WORLD);
    mpi.MPI_Bcast(secondMatrix.getValues(), 0, secondMatrix.getNRows() * secondMatrix.getNCols(), 0, mpi.MPI_COMM_WORLD);

    for (int row = startRow; row < endRow; row++) {
      for (int col = 0; col < resultMatrix.getNCols(); col++) {
        resultMatrix.set(row, col, 0.0);
        for (int k = 0; k < secondMatrix.getNRows(); k++) {
          resultMatrix.incr(row, col, firstMatrix.get(row, k) * secondMatrix.get(k, col));
        }
      }
    }

    if (myrank == 0) {
      MPI_Request[] requests = new MPI_Request[size - 1];
      for (int i = 1; i < size; i++) {
        int rankStartRow = i * rowChunk;
        int rankEndRow = (i + 1) * rowChunk;
        if (rankEndRow > nrows) rankEndRow = nrows;

        final int rowOffset = rankStartRow * resultMatrix.getNCols();
        final int nElements = (rankEndRow - rankStartRow) * resultMatrix.getNCols();

        requests[i - 1] = mpi.MPI_Irecv(resultMatrix.getValues(), rowOffset, nElements, i, i, mpi.MPI_COMM_WORLD);
      }
      mpi.MPI_Waitall(requests);

    } else {
      mpi.MPI_Send(resultMatrix.getValues(), startRow * resultMatrix.getNCols(),
        (endRow - startRow) * resultMatrix.getNCols(), 0, myrank,
        mpi.MPI_COMM_WORLD);
    }
  }
}
