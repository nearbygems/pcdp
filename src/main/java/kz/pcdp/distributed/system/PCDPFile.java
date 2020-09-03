package kz.pcdp.distributed.system;

public final class PCDPFile extends PCDPFilesystemComponent {

  private final String contents;

  public PCDPFile(final String setName, final String setContents) {
    super(setName);
    contents = setContents;
  }

  public String read() {
    return contents;
  }
}
