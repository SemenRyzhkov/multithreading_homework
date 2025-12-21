package module3;

import utils.BusyCpuUtil;
import utils.Logger;
import utils.ThreadSleepUtil;

public final class StoryService {
    private int fetchDraftMs, editTextMs, finalizeMs;
    private double chanceOfEditingError;

    // все ms ≥ 0; 0 < chanceOfEditingError ≤ 1 → иначе IllegalArgumentException
    public StoryService(int fetchDraftMs, int editTextMs, int finalizeMs, double chanceOfEditingError) {
        if (
                fetchDraftMs < 0
                        || editTextMs < 0
                        || finalizeMs < 0
                        || chanceOfEditingError <= 0
                        || chanceOfEditingError > 1
        ) {
            throw new IllegalArgumentException();
        }
        this.fetchDraftMs = fetchDraftMs;
        this.editTextMs = editTextMs;
        this.finalizeMs = finalizeMs;
        this.chanceOfEditingError = chanceOfEditingError;
    }

    // Имитация внешнего I/O (блокирующая задержка): вернёт "draft"
    public String fetchDraft(StoryTask task) {
//        Logger.log("Fetching draft for story %d on thread: %s", task.id(), Thread.currentThread().getName());
        ThreadSleepUtil.safeSleepWithJitter(fetchDraftMs);
        return String.format(
                "Черновик истории № %d: \"%s\".\n" +
                        "Автор: %s.\n" +
                         "%s",
                task.id(),
                task.title(),
                task.author(),
                "Начинается с неожиданного стука в дверь старого дома."
        );
    }

    // Имитация CPU (busy‐loop); с заданной вероятностью бросает исключение
    public String editText(String draft) {
//        Logger.log("Editing text on thread: %s", Thread.currentThread().getName());

        BusyCpuUtil.spinOnCpuMillis(editTextMs);
        if (Math.random() < chanceOfEditingError) {
            throw new IllegalStateException("Ошибка редактирования текста");
        }
        return draft + " \n Хозяин дома подходит к двери, смотрит в замачную скважину и видит...";
    }

    // Имитация I/O + формирование StoryResult
    public StoryResult finalizeStory(StoryTask task, String editedText) {
//        Logger.log("Finalizing story %d on thread: %s", task.id(), Thread.currentThread().getName());

        ThreadSleepUtil.safeSleepWithJitter(finalizeMs);
        if (editedText == null || editedText.isBlank()) {
            throw new IllegalArgumentException("Отредактированный текст пуст");
        }
        String finalText = editedText.trim() + "\n огромного медведя с дубиной в руках";
        return StoryResult.of(task.id(), finalText);    }

}

