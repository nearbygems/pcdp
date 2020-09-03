package kz.pcdp.distributed.matrix;

import com.sun.jna.*;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

public final class MPI {
  private interface MPILib extends Library {
    MPILib INSTANCE = (MPILib) Native.loadLibrary("mpi", MPILib.class);
    NativeLibrary lib = NativeLibrary.getInstance("mpi");

    int MPI_Send(IntBuffer buf, int count, Pointer datatype,
                 int dest, int tag, Pointer comm);

    int MPI_Send(DoubleBuffer buf, int count, Pointer datatype,
                 int dest, int tag, Pointer comm);

    int MPI_Recv(IntBuffer buf, int count, Pointer datatype, int src,
                 int tag, Pointer comm, Pointer status);

    int MPI_Recv(DoubleBuffer buf, int count, Pointer datatype,
                 int src, int tag, Pointer comm, Pointer status);

    int MPI_Isend(Memory buf, int count, Pointer datatype,
                  int dest, int tag, Pointer comm, Memory request);

    int MPI_Irecv(Memory buf, int count, Pointer datatype,
                  int src, int tag, Pointer comm, Memory request);

    int MPI_Bcast(DoubleBuffer buf, int count, Pointer datatype,
                  int root, Pointer comm);

    int MPI_Wait(Memory request, Pointer status);

    int MPI_Barrier(Pointer comm);

    int MPI_Init(int[] argc, String[] argv);

    int MPI_Finalize();

    int MPI_Comm_size(Pointer comm, int[] size);

    int MPI_Comm_rank(Pointer comm, int[] rank);
  }

  public final MPI_Comm MPI_COMM_WORLD;

  public final MPI_Datatype MPI_INTEGER;

  public final MPI_Datatype MPI_FLOAT;

  public final MPI_Datatype MPI_DOUBLE;

  public final MPI_Status MPI_STATUS_IGNORE;

  public MPI() {
    Pointer ptr = MPILib.lib.getGlobalVariableAddress(
      "ompi_mpi_comm_world");
    MPI_COMM_WORLD = new MPI_Comm(ptr);

    ptr = MPILib.lib.getGlobalVariableAddress("ompi_mpi_integer");
    MPI_INTEGER = new MPI_Datatype(ptr);

    ptr = MPILib.lib.getGlobalVariableAddress("ompi_mpi_float");
    MPI_FLOAT = new MPI_Datatype(ptr);

    ptr = MPILib.lib.getGlobalVariableAddress("ompi_mpi_double");
    MPI_DOUBLE = new MPI_Datatype(ptr);

    MPI_STATUS_IGNORE = new MPI_Status(Pointer.NULL);
  }

  public void MPI_Init() throws MPIException {
    final int err = MPILib.INSTANCE.MPI_Init(new int[0], new String[0]);
    if (err != 0) {
      throw new MPIException(err);
    }
  }

  public void MPI_Finalize() throws MPIException {
    final int err = MPILib.INSTANCE.MPI_Finalize();
    if (err != 0) {
      throw new MPIException(err);
    }
  }

  public int MPI_Comm_size(final MPI_Comm comm) throws MPIException {
    int[] result = new int[1];
    final int err = MPILib.INSTANCE.MPI_Comm_size(comm.comm, result);
    if (err != 0) {
      throw new MPIException(err);
    }
    return result[0];
  }

  public int MPI_Comm_rank(final MPI_Comm comm) throws MPIException {
    int[] result = new int[1];
    final int err = MPILib.INSTANCE.MPI_Comm_rank(comm.comm, result);
    if (err != 0) {
      throw new MPIException(err);
    }
    return result[0];
  }

  public void MPI_Bcast(final double[] buf, final int offset, final int count,
                        final int root, final MPI_Comm comm) throws MPIException {
    final DoubleBuffer wrapper = DoubleBuffer.wrap(buf, offset, count);
    final int err = MPILib.INSTANCE.MPI_Bcast(wrapper, count,
      MPI_DOUBLE.datatype, root, comm.comm);
    if (err != 0) {
      throw new MPIException(err);
    }
  }

  public void MPI_Send(final int[] buf, final int offset, final int count,
                       final int dst, final int tag, final MPI_Comm comm)
    throws MPIException {
    final IntBuffer wrapper = IntBuffer.wrap(buf, offset, count);
    final int err = MPILib.INSTANCE.MPI_Send(wrapper, count,
      MPI_INTEGER.datatype, dst, tag, comm.comm);
    if (err != 0) {
      throw new MPIException(err);
    }
  }

  public void MPI_Send(final double[] buf, final int offset, final int count,
                       final int dst, final int tag, final MPI_Comm comm)
    throws MPIException {
    final DoubleBuffer wrapper = DoubleBuffer.wrap(buf, offset, count);
    final int err = MPILib.INSTANCE.MPI_Send(wrapper, count,
      MPI_DOUBLE.datatype, dst, tag, comm.comm);
    if (err != 0) {
      throw new MPIException(err);
    }
  }

  public void MPI_Recv(final int[] buf, final int offset, final int count,
                       final int src, final int tag, final MPI_Comm comm)
    throws MPIException {
    final IntBuffer wrapper = IntBuffer.wrap(buf, offset, count);
    final int err = MPILib.INSTANCE.MPI_Recv(wrapper, count,
      MPI_INTEGER.datatype, src, tag, comm.comm,
      MPI_STATUS_IGNORE.status);
    if (err != 0) {
      throw new MPIException(err);
    }
  }

  public void MPI_Recv(final double[] buf, final int offset, final int count,
                       final int src, final int tag, final MPI_Comm comm)
    throws MPIException {
    final DoubleBuffer wrapper = DoubleBuffer.wrap(buf, offset, count);
    final int err = MPILib.INSTANCE.MPI_Recv(wrapper, count,
      MPI_DOUBLE.datatype, src, tag, comm.comm,
      MPI_STATUS_IGNORE.status);
    if (err != 0) {
      throw new MPIException(err);
    }
  }

  public MPI_Request MPI_Irecv(final double[] buf, final int offset,
                               final int count, final int src, final int tag, final MPI_Comm comm)
    throws MPIException {
    Recv_MPI_Request request = new Recv_MPI_Request(count * 8, buf, offset,
      count);
    final int err = MPILib.INSTANCE.MPI_Irecv(request.buf, count,
      MPI_DOUBLE.datatype, src, tag, comm.comm,
      request.request);
    if (err != 0) {
      throw new MPIException(err);
    }
    return request;
  }

  public MPI_Request MPI_Isend(final double[] buf, final int offset,
                               final int count, final int dst, final int tag, final MPI_Comm comm)
    throws MPIException {
    Send_MPI_Request request = new Send_MPI_Request(count * 8);
    request.buf.write(0, buf, offset, count);
    final int err = MPILib.INSTANCE.MPI_Isend(request.buf, count,
      MPI_DOUBLE.datatype, dst, tag, comm.comm,
      request.request);
    if (err != 0) {
      throw new MPIException(err);
    }
    return request;
  }

  public void MPI_Wait(final MPI_Request request) throws MPIException {
    final int err = MPILib.INSTANCE.MPI_Wait(request.request,
      MPI_STATUS_IGNORE.status);
    if (err != 0) {
      throw new MPIException(err);
    }
    request.complete();
  }

  public void MPI_Waitall(final MPI_Request[] requests) throws MPIException {
    for (int i = 0; i < requests.length; i++) {
      MPI_Wait(requests[i]);
    }
  }

  public void MPI_Barrier(final MPI_Comm comm) throws MPIException {
    final int err = MPILib.INSTANCE.MPI_Barrier(comm.comm);
    if (err != 0) {
      throw new MPIException(err);
    }
  }

  public static class MPI_Comm {

    protected final Pointer comm;

    public MPI_Comm(final Pointer setComm) {
      this.comm = setComm;
    }
  }

  public static final class MPI_Datatype {

    protected final Pointer datatype;

    public MPI_Datatype(final Pointer setDatatype) {
      this.datatype = setDatatype;
    }
  }

  public abstract static class MPI_Request {

    protected final Memory request;

    protected final Memory buf;

    public MPI_Request(final long size) {
      request = new Memory(8);
      buf = new Memory(size);
    }

    protected abstract void complete();
  }

  public final class Recv_MPI_Request extends MPI_Request {

    private final double[] dst;

    private final int offset;

    private final int count;

    public Recv_MPI_Request(final long setSize, final double[] setDst,
                            final int setOffset, final int setCount) {
      super(setSize);
      this.dst = setDst;
      this.offset = setOffset;
      this.count = setCount;
    }

    @Override
    protected void complete() {
      buf.read(0, dst, offset, count);
    }
  }

  public final class Send_MPI_Request extends MPI_Request {

    public Send_MPI_Request(final long size) {
      super(size);
    }

    @Override
    protected void complete() {
    }
  }

  public static final class MPI_Status {

    protected final Pointer status;

    public MPI_Status(final Pointer setStatus) {
      this.status = setStatus;
    }
  }

  public static final class MPIException extends Exception {

    private final int code;

    public MPIException(final int setCode) {
      this.code = setCode;
    }

    public int getErrCode() {
      return code;
    }
  }
}
