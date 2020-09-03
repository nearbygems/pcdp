package kz.pcdp.distributed.server;

public abstract class PCDPFilesystemComponent {

    private final String name;

    public PCDPFilesystemComponent(final String setName) {
        name = setName;
    }

    public String getName() {
        return name;
    }
}
