package kz.pcdp.distributed.system;

import java.util.HashMap;
import java.util.Map;

public final class PCDPFolder extends PCDPFilesystemComponent {

  private final Map<String, PCDPFilesystemComponent> children;

  public PCDPFolder(final String setName) {
    super(setName);
    children = new HashMap<String, PCDPFilesystemComponent>();
  }

  public void addChild(PCDPFilesystemComponent child) {
    assert !children.containsKey(child.getName());
    children.put(child.getName(), child);
  }

  public boolean hasChild(String name) {
    return children.containsKey(name);
  }

  public PCDPFilesystemComponent getChild(String name) {
    if (!hasChild(name)) {
      return null;
    }

    return children.get(name);
  }
}

