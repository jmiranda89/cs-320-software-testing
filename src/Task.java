// Task fields cannot be null. ID, name, and description allow up to 10, 20,
// and 50 characters. Empty strings are allowed, matching Contact.
// IDs stay fixed after creation; TaskService checks uniqueness.
public class Task {

    private final String taskId;
    private String name;
    private String description;

    public Task(String taskId, String name, String description) {
        validateTaskId(taskId);
        validateName(name);
        validateDescription(description);

        this.taskId = taskId;
        this.name = name;
        this.description = description;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        // Validate first so a rejected update keeps the current value.
        validateName(name);
        this.name = name;
    }

    public void setDescription(String description) {
        // Validate first so a rejected update keeps the current value.
        validateDescription(description);
        this.description = description;
    }

    private void validateTaskId(String taskId) {
        if (taskId == null || taskId.length() > 10) {
            throw new IllegalArgumentException(
                    "Task ID cannot be null or longer than 10 characters."
            );
        }
    }

    private void validateName(String name) {
        if (name == null || name.length() > 20) {
            throw new IllegalArgumentException(
                    "Name cannot be null or longer than 20 characters."
            );
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException(
                    "Description cannot be null or longer than 50 characters."
            );
        }
    }
}
