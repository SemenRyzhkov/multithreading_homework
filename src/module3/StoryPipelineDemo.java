package module3;

import utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoryPipelineDemo {

    public static final int POOL_SIZE = 100;
    // I/O-задержка на fetch/finalize/save
    public static final int IO_MILLIS = 100;
    // CPU‐нагрузка на edit
    public static final int CPU_MILLIS = 100;
    // вероятность ошибки на editText (0 < chance ≤ 1)
    public static final double CHANCE_OF_ERROR = 0.2;

    public void runPipeline(List<StoryTask> tasks,
                            ExecutorService executorService,
                            StoryStorage storage,
                            StoryService service
    ) {
        List<CompletableFuture<StoryResult>> futures = new ArrayList<>();
        tasks.forEach(task -> {
            var future = CompletableFuture.supplyAsync(() -> service.fetchDraft(task), executorService)
                    .thenApplyAsync(service::editText)
                    .thenApplyAsync(story -> service.finalizeStory(task, story), executorService)
                    .thenApplyAsync(story -> {
                        storage.save(story);
                        return story;
                    })
                    .exceptionally(ex -> {
//                        Logger.log("Task failed: taskId=%s", task.id(), ex.getMessage());
                        return null;
                    });
            futures.add(future);
        });
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();
    }

    public static void main(String[] args) {
        int[] taskCounts = {10, 100, 1000};
        int[] ioMillisValues = {10, 100, 500};
        int[] cpuMillisValues = {1, 10, 100};
        double[] errorChances = {0.1, 0.2, 0.5};

        for (int n : taskCounts) {
            for (int io : ioMillisValues) {
                for (int cpu : cpuMillisValues) {
                    for (double chance : errorChances) {
                        System.out.printf("\n--- ТЕСТ: n=%d, IO=%dms, CPU=%dms, Error=%.1f ---\n", n, io, cpu, chance);
                        runTest(n, io, cpu, chance);
                    }
                }
            }
        }
    }

    private static void runTest(int n, int io, int cpu, double chance) {
        List<StoryTask> tasks = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            tasks.add(StoryTask.of(i, "Author " + i, "Chapter" + i));
        }

        StoryStorage storage = new StoryStorage(io);
        StoryService service = new StoryService(io, cpu, io, chance);

        long start1 = System.nanoTime();
        try (ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE)) {
            new StoryPipelineDemo().runPipeline(tasks, executor, storage, service);
        }
        long end1 = System.nanoTime();
        double time1 = (end1 - start1) / 1_000_000.0; // мс
        int saved1 = storage.snapshot().size();

        storage.clear();

        long start2 = System.nanoTime();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            new StoryPipelineDemo().runPipeline(tasks, executor, storage, service);
        }
        long end2 = System.nanoTime();
        double time2 = (end2 - start2) / 1_000_000.0; // мс
        int saved2 = storage.snapshot().size();

        System.out.printf("Fixed Pool     : %.2f ms | saved: %d/%d%n", time1, saved1, n);
        System.out.printf("Virtual Threads: %.2f ms | saved: %d/%d%n", time2, saved2, n);

        if (time1 < time2) {
            double diff = (time2 - time1) / time2 * 100;
            System.out.printf("Fixed быстрее на %.1f%%\n", diff);
        } else {
            double diff = (time1 - time2) / time1 * 100;
            System.out.printf("VT быстрее на %.1f%%\n", diff);
        }
    }
}
