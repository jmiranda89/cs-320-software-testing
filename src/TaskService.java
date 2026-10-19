import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// IDs ignore case, matching ContactService. Null IDs never match a task.
// Duplicate adds and missing delete/update targets return false.
public class TaskService {

    private final List<Task> taskList = new ArrayList<>();

    public TaskService() {
    }

    // Duplicate IDs leave the existing task in place.
    public boolean addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }

        for (Task existingTask : taskList) {
            if (existingTask.getTaskId()
                    .equalsIgnoreCase(task.getTaskId())) {
                return false;
            }
        }

        // Keep the caller's instance, matching ContactService.
        taskList.add(task);
        return true;
    }

    public boolean deleteTask(String taskId) {
        Iterator<Task> iterator = taskList.iterator();

        while (iterator.hasNext()) {
            Task task = iterator.next();

            if (task.getTaskId().equalsIgnoreCase(taskId)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    public boolean updateName(String taskId, String name) {
        Task task = findTask(taskId);

        // Missing tasks are a no-op, even if the new value is invalid.
        if (task == null) {
            return false;
        }

        task.setName(name);
        return true;
    }

    public boolean updateDescription(String taskId, String description) {
        Task task = findTask(taskId);

        // Missing tasks are a no-op, even if the new value is invalid.
        if (task == null) {
            return false;
        }

        task.setDescription(description);
        return true;
    }

    // Callers get the stored instance; setters still validate their changes.
    public Task getTask(String taskId) {
        return findTask(taskId);
    }

    public int getTaskCount() {
        return taskList.size();
    }

    private Task findTask(String taskId) {
        for (Task task : taskList) {
            if (task.getTaskId().equalsIgnoreCase(taskId)) {
                return task;
            }
        }

        return null;
    }
}
