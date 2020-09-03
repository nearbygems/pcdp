package kz.pcdp.distributed.system;

public final class PCDPPath {

  private final String[] components;

  public PCDPPath(final String path) {
    if (!path.startsWith("/")) {
      throw new RuntimeException("Only absolute paths supported, " +
        "received path \"" + path + "\"");
    }

    components = path.substring(1).split("/");
  }

  public int getNComponents() { return components.length; }

  public String getComponent(final int index) { return components[index]; }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (String component : components) {
      sb.append("/");
      sb.append(component);
    }
    return sb.toString();
  }
}
