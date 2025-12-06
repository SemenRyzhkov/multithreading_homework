package module2.deadlocksimulator;

public interface LockableResourceProcessor {
    /**
     * Выполняет критическую секцию над двумя ресурсами r1 и r2.
     * Внутри реализуется выбранная стратегия захвата (зависит от реализации).
     */
    void processResources(LockableResource r1, LockableResource r2)
            throws InterruptedException;
}
