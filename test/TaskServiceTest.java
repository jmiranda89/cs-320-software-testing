import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskServiceTest {

    private TaskService service;
    private Task task;

    @BeforeEach
    void setUp() {
        service = new TaskService();
        task = new Task(
                "TASK1001",
                "Confirm visit",
                "Verify customer details before the appointment."
        );
    }

    @Test
    void testAddTask() {
        assertTrue(service.addTask(task));
        assertEquals(1, service.getTaskCount());
        assertSame(task, service.getTask("TASK1001"));
    }

    @Test
    void testAddMultipleTasksWithUniqueIds() {
        Task secondTask = new Task("TASK1002", "Send reminder", "Remind the customer about tomorrow's appointment.");

        assertTrue(service.addTask(task));
        assertTrue(service.addTask(secondTask));
        assertEquals(2, service.getTaskCount());
        assertSame(task, service.getTask("TASK1001"));
        assertSame(secondTask, service.getTask("TASK1002"));
    }

    @Test
    void testDuplicateTaskIdIsRejectedWithoutReplacingOriginal() {
        Task duplicate = new Task("TASK1001", "Schedule visit", "Book a second visit for the customer.");

        assertTrue(service.addTask(task));
        assertFalse(service.addTask(duplicate));
        assertEquals(1, service.getTaskCount());
        assertSame(task, service.getTask("TASK1001"));
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
    }

    @Test
    void testDuplicateTaskIdIgnoresCaseBeyondFirstTask() {
        Task secondTask = new Task("TASK1002", "Send reminder", "Remind the customer about tomorrow's appointment.");
        Task duplicate = new Task("task1002", "Schedule visit", "Book a visit for another customer.");
        service.addTask(task);
        service.addTask(secondTask);

        assertFalse(service.addTask(duplicate));
        assertEquals(2, service.getTaskCount());
        assertSame(task, service.getTask("TASK1001"));
        assertSame(secondTask, service.getTask("TASK1002"));
        assertEquals("Send reminder", secondTask.getName());
    }

    @Test
    void testAddingSameTaskTwiceIsRejected() {
        assertTrue(service.addTask(task));
        assertFalse(service.addTask(task));
        assertEquals(1, service.getTaskCount());
        assertSame(task, service.getTask("TASK1001"));
    }

    @Test
    void testAddNullTaskThrowsWithoutChangingService() {
        service.addTask(task);

        assertThrows(IllegalArgumentException.class, () -> service.addTask(null));

        assertEquals(1, service.getTaskCount());
        assertSame(task, service.getTask("TASK1001"));
    }

    @Test
    void testDeleteTask() {
        service.addTask(task);

        assertTrue(service.deleteTask("TASK1001"));
        assertEquals(0, service.getTaskCount());
        assertNull(service.getTask("TASK1001"));
        assertFalse(service.deleteTask("TASK1001"));
    }

    @Test
    void testDeleteTargetsCorrectIdBeyondFirstTaskIgnoringCase() {
        Task secondTask = new Task("TASK1002", "Send reminder", "Remind the customer about tomorrow's appointment.");
        Task thirdTask = new Task("TASK1003", "Record follow-up", "Save notes after the completed customer visit.");
        service.addTask(task);
        service.addTask(secondTask);
        service.addTask(thirdTask);

        assertTrue(service.deleteTask("task1002"));
        assertEquals(2, service.getTaskCount());
        assertNull(service.getTask("TASK1002"));
        assertSame(task, service.getTask("TASK1001"));
        assertSame(thirdTask, service.getTask("TASK1003"));
    }

    @Test
    void testDeleteMissingTaskReturnsFalse() {
        service.addTask(task);

        assertFalse(service.deleteTask("TASK9999"));
        assertEquals(1, service.getTaskCount());
        assertSame(task, service.getTask("TASK1001"));
    }

    @Test
    void testDeletedIdCanBeReused() {
        Task replacement = new Task("task1001", "Schedule follow-up", "Arrange the customer's next visit.");
        service.addTask(task);

        assertTrue(service.deleteTask("TASK1001"));
        assertTrue(service.addTask(replacement));
        assertEquals(1, service.getTaskCount());
        assertSame(replacement, service.getTask("TASK1001"));
    }

    @Test
    void testUpdateNamePreservesDescriptionAndId() {
        service.addTask(task);

        assertTrue(service.updateName("TASK1001", "Confirm check-in"));
        assertEquals("Confirm check-in", service.getTask("TASK1001").getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());
        assertEquals(1, service.getTaskCount());
    }

    @Test
    void testUpdateDescriptionPreservesNameAndId() {
        service.addTask(task);

        assertTrue(service.updateDescription("TASK1001", "Confirm the visit time with the customer."));
        assertEquals("Confirm the visit time with the customer.", service.getTask("TASK1001").getDescription());
        assertEquals("Confirm visit", task.getName());
        assertEquals("TASK1001", task.getTaskId());
        assertEquals(1, service.getTaskCount());
    }

    @Test
    void testUpdateBeyondFirstTaskIgnoringCasePreservesOtherTasks() {
        Task secondTask = new Task("TASK1002", "Send reminder", "Remind the customer about tomorrow's appointment.");
        Task thirdTask = new Task("TASK1003", "Record follow-up", "Save notes after the completed customer visit.");
        service.addTask(task);
        service.addTask(secondTask);
        service.addTask(thirdTask);

        assertTrue(service.updateName("task1003", "Schedule follow-up"));
        assertTrue(service.updateDescription("task1003", "Arrange the customer's next visit."));

        assertSame(thirdTask, service.getTask("task1003"));
        assertEquals("Schedule follow-up", thirdTask.getName());
        assertEquals("Arrange the customer's next visit.", thirdTask.getDescription());
        assertEquals("TASK1003", thirdTask.getTaskId());
        assertSame(task, service.getTask("TASK1001"));
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
        assertSame(secondTask, service.getTask("TASK1002"));
        assertEquals("Send reminder", secondTask.getName());
        assertEquals("Remind the customer about tomorrow's appointment.", secondTask.getDescription());
        assertEquals(3, service.getTaskCount());
    }

    @Test
    void testUpdateAllowsMaximumLengths() {
        Task boundaryTask = new Task("1234567890", "Send reminder", "Remind the customer about tomorrow's appointment.");
        String twentyCharacters = "12345678901234567890";
        String fiftyCharacters = "12345678901234567890123456789012345678901234567890";
        service.addTask(boundaryTask);

        assertTrue(service.updateName("1234567890", twentyCharacters));
        assertTrue(service.updateDescription("1234567890", fiftyCharacters));
        assertEquals(twentyCharacters, service.getTask("1234567890").getName());
        assertEquals(fiftyCharacters, service.getTask("1234567890").getDescription());
        assertEquals("1234567890", boundaryTask.getTaskId());
    }

    @Test
    void testInvalidNameUpdatesPreserveState() {
        service.addTask(task);

        assertThrows(IllegalArgumentException.class, () ->
                service.updateName("TASK1001", null)
        );
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());

        assertThrows(IllegalArgumentException.class, () ->
                service.updateName("TASK1001", "123456789012345678901")
        );
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());
        assertSame(task, service.getTask("TASK1001"));
        assertEquals(1, service.getTaskCount());
    }

    @Test
    void testInvalidDescriptionUpdatesPreserveState() {
        String fiftyCharacters = "12345678901234567890123456789012345678901234567890";
        service.addTask(task);

        assertThrows(IllegalArgumentException.class, () ->
                service.updateDescription("TASK1001", null)
        );
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());

        assertThrows(IllegalArgumentException.class, () ->
                service.updateDescription("TASK1001", fiftyCharacters + "1")
        );
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());
        assertSame(task, service.getTask("TASK1001"));
        assertEquals(1, service.getTaskCount());
    }

    @Test
    void testUpdateMissingTaskReturnsFalse() {
        service.addTask(task);

        assertFalse(service.updateName("TASK9999", "Send reminder"));
        assertFalse(service.updateDescription("TASK9999", "Remind the customer about tomorrow's appointment."));
        assertNull(service.getTask("TASK9999"));
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());
        assertSame(task, service.getTask("TASK1001"));
        assertEquals(1, service.getTaskCount());
    }

    @Test
    void testMissingTaskReturnsFalseBeforeValidatingUpdate() {
        service.addTask(task);

        assertFalse(service.updateName("TASK9999", null));
        assertFalse(service.updateDescription("TASK9999", null));
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
        assertSame(task, service.getTask("TASK1001"));
        assertEquals(1, service.getTaskCount());
    }

    @Test
    void testNullIdMatchesNoTask() {
        service.addTask(task);

        assertNull(service.getTask(null));
        assertFalse(service.deleteTask(null));
        assertFalse(service.updateName(null, "Send reminder"));
        assertFalse(service.updateDescription(null, "Remind the customer about tomorrow's appointment."));
        assertSame(task, service.getTask("TASK1001"));
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());
        assertEquals(1, service.getTaskCount());
    }

    @Test
    void testEmptyServiceHasNoMatchingTask() {
        assertEquals(0, service.getTaskCount());
        assertNull(service.getTask("TASK1001"));
        assertFalse(service.deleteTask("TASK1001"));
        assertFalse(service.updateName("TASK1001", "Send reminder"));
        assertFalse(service.updateDescription("TASK1001", "Remind the customer about tomorrow's appointment."));
        assertEquals(0, service.getTaskCount());
    }

    @Test
    void testEmptyIdStillEnforcesUniquenessAndSupportsOperations() {
        Task emptyIdTask = new Task("", "Review request", "Check the customer's requested visit time.");
        Task duplicate = new Task("", "Send reminder", "Remind the customer about tomorrow's appointment.");

        assertTrue(service.addTask(emptyIdTask));
        assertFalse(service.addTask(duplicate));
        assertSame(emptyIdTask, service.getTask(""));
        assertTrue(service.updateName("", ""));
        assertTrue(service.updateDescription("", ""));
        assertEquals("", emptyIdTask.getName());
        assertEquals("", emptyIdTask.getDescription());
        assertEquals("", emptyIdTask.getTaskId());
        assertEquals(1, service.getTaskCount());
        assertTrue(service.deleteTask(""));
        assertNull(service.getTask(""));
        assertEquals(0, service.getTaskCount());
    }
}
