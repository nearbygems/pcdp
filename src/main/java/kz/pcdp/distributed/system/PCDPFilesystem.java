package kz.pcdp.distributed.system;

public class PCDPFilesystem {

  private final PCDPFolder root = new PCDPFolder("static");

  public PCDPFilesystem() { }

  public void addFile(PCDPPath path, String contents) {
    assert path.getNComponents() > 0;

    assert path.getComponent(0).equals(root.getName());

    int componentIndex = 1;
    PCDPFolder curr = root;
    while (componentIndex < path.getNComponents()) {
      String component = path.getComponent(componentIndex++);
      PCDPFilesystemComponent next = curr.getChild(component);

      if (componentIndex < path.getNComponents()) {
        if (next == null) {
          PCDPFolder newFolder = new PCDPFolder(component);
          curr.addChild(newFolder);
          curr = newFolder;
        } else {
          assert next instanceof PCDPFolder;
          curr = (PCDPFolder) next;
        }
      } else {
        assert next == null;
        PCDPFile newFile = new PCDPFile(component, contents);
        curr.addChild(newFile);
      }
    }
  }

  public String readFile(PCDPPath path) {
    if (path.getNComponents() == 0) {
      return null;
    }

    if (!path.getComponent(0).equals(root.getName())) {
      return null;
    }

    int componentIndex = 1;
    PCDPFilesystemComponent curr = root;
    while (componentIndex < path.getNComponents()) {
      final String nextComponent = path.getComponent(componentIndex++);

      if (curr == null || !(curr instanceof PCDPFolder)) {
        return null;
      }

      PCDPFilesystemComponent next = ((PCDPFolder) curr).getChild(
        nextComponent);

      curr = next;
    }

    if (curr == null || !(curr instanceof PCDPFile)) {
      return null;
    } else {
      return ((PCDPFile) curr).read();
    }
  }
}
