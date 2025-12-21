package module3;

import utils.Logger;
import utils.ThreadSleepUtil;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class StoryStorage {
    Map<Long, StoryResult> storyResults = new ConcurrentHashMap<>();
    //задержка I/O при сохранении
    int saveTimeMillis;

    public StoryStorage(int saveTimeMillis) {
        if (saveTimeMillis <= 0) {
            throw new IllegalArgumentException("saveTimeMillis < 0");
        }
        this.saveTimeMillis = saveTimeMillis;
    }

    // upsert по taskId, с I/O‐задержкой
    public void save(StoryResult result) {
//        Logger.log("Saving result for task %d on thread: %s",
//                result.taskId(), Thread.currentThread().getName());
        ThreadSleepUtil.safeSleepWithJitter(saveTimeMillis);
        storyResults.put(result.taskId(), result);
    }

    public Optional<StoryResult> get(long taskId) {
        return Optional.ofNullable(storyResults.get(taskId));
    }

    public int size() {
        return storyResults.size();
    }

    // немодифицируемая копия
    public Map<Long, StoryResult> snapshot() {
        return Collections.unmodifiableMap(new ConcurrentHashMap<>(storyResults));
    }


    public void clear() {
        Logger.log("Clearing storage on thread: %s", Thread.currentThread().getName());
        ThreadSleepUtil.safeSleepWithJitter(saveTimeMillis);
        storyResults.clear();
    }
}