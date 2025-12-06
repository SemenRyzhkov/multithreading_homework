package module2.deadlocksimulator;

import java.util.concurrent.locks.ReentrantLock;

public final class LockableResource {

    private final String name;
    private final int id;
    private final ReentrantLock lock = new ReentrantLock();
    private final Object monitor = new Object();

    public LockableResource(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Object getMonitor() {
        return monitor;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
