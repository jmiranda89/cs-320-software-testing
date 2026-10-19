import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void testCreateValidTask() {
        Task task = new Task(
                "TASK1001",
                "Confirm visit",
                "Verify customer details before the appointment."
        );

        assertEquals("TASK1001", task.getTaskId());
        assertEquals("Confirm visit", task.getName());
        assertEquals("Verify customer details before the appointment.", task.getDescription());
    }

    @Test
    void testTaskIdAllowsTenCharacters() {
        Task task = new Task("1234567890", "Review request", "Check the customer's requested visit time.");

        assertEquals("1234567890", task.getTaskId());
    }

    @Test
    void testTaskIdCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(null, "Send reminder", "Remind the customer about tomorrow's appointment.")
        );
    }

    @Test
    void testTaskIdCannotExceedTenCharacters() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task("12345678901", "Verify details", "Review the customer's updated contact information.")
        );
    }

    @Test
    void testTaskIdIsPrivateAndFinalWithNoSetter() throws NoSuchFieldException {
        int modifiers = Task.class.getDeclaredField("taskId").getModifiers();

        assertTrue(Modifier.isPrivate(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
        assertThrows(NoSuchMethodException.class, () ->
                Task.class.getMethod("setTaskId", String.class)
        );
    }

    @Test
    void testNameAllowsTwentyCharacters() {
        String twentyCharacters = "12345678901234567890";
        Task task = new Task("TASK1001", twentyCharacters, "Confirm the customer's appointment details.");

        assertEquals(twentyCharacters, task.getName());
    }

    @Test
    void testNameCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task("TASK1001", null, "Confirm the customer's appointment details.")
        );
    }

    @Test
    void testNameCannotExceedTwentyCharacters() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task("TASK1001", "123456789012345678901", "Confirm the customer's appointment details.")
        );
    }

    @Test
    void testDescriptionAllowsFiftyCharacters() {
        String fiftyCharacters = "12345678901234567890123456789012345678901234567890";
        Task task = new Task("TASK1001", "Confirm visit", fiftyCharacters);

        assertEquals(fiftyCharacters, task.getDescription());
    }

    @Test
    void testDescriptionCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task("TASK1001", "Confirm visit", null)
        );
    }

    @Test
    void testDescriptionCannotExceedFiftyCharacters() {
        String fiftyCharacters = "12345678901234567890123456789012345678901234567890";

        assertThrows(IllegalArgumentException.class, () ->
                new Task("TASK1001", "Confirm visit", fiftyCharacters + "1")
        );
    }

    @Test
    void testUpdateNamePreservesOtherFields() {
        Task task = new Task("TASK1001", "Confirm visit", "Confirm the customer's appointment details.");

        task.setName("Confirm check-in");

        assertEquals("Confirm check-in", task.getName());
        assertEquals("Confirm the customer's appointment details.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());
    }

    @Test
    void testUpdateDescriptionPreservesOtherFields() {
        Task task = new Task("TASK1003", "Record follow-up", "Save notes after the completed customer visit.");

        task.setDescription("Record the customer's follow-up request.");

        assertEquals("Record the customer's follow-up request.", task.getDescription());
        assertEquals("Record follow-up", task.getName());
        assertEquals("TASK1003", task.getTaskId());
    }

    @Test
    void testSettersAllowMaximumLengths() {
        Task task = new Task("TASK1001", "Confirm visit", "Confirm the customer's appointment details.");
        String twentyCharacters = "12345678901234567890";
        String fiftyCharacters = "12345678901234567890123456789012345678901234567890";

        task.setName(twentyCharacters);
        task.setDescription(fiftyCharacters);

        assertEquals(twentyCharacters, task.getName());
        assertEquals(fiftyCharacters, task.getDescription());
        assertEquals("TASK1001", task.getTaskId());
    }

    @Test
    void testSetNameRejectsNullWithoutChangingState() {
        Task task = new Task("TASK1001", "Confirm visit", "Confirm the customer's appointment details.");

        assertThrows(IllegalArgumentException.class, () -> task.setName(null));

        assertEquals("Confirm visit", task.getName());
        assertEquals("Confirm the customer's appointment details.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());
    }

    @Test
    void testSetNameRejectsTooLongValueWithoutChangingState() {
        Task task = new Task("TASK1001", "Confirm visit", "Confirm the customer's appointment details.");

        assertThrows(IllegalArgumentException.class, () ->
                task.setName("123456789012345678901")
        );

        assertEquals("Confirm visit", task.getName());
        assertEquals("Confirm the customer's appointment details.", task.getDescription());
        assertEquals("TASK1001", task.getTaskId());
    }

    @Test
    void testSetDescriptionRejectsNullWithoutChangingState() {
        Task task = new Task("TASK1003", "Record follow-up", "Save notes after the completed customer visit.");

        assertThrows(IllegalArgumentException.class, () -> task.setDescription(null));

        assertEquals("Record follow-up", task.getName());
        assertEquals("Save notes after the completed customer visit.", task.getDescription());
        assertEquals("TASK1003", task.getTaskId());
    }

    @Test
    void testSetDescriptionRejectsTooLongValueWithoutChangingState() {
        Task task = new Task("TASK1003", "Record follow-up", "Save notes after the completed customer visit.");
        String fiftyCharacters = "12345678901234567890123456789012345678901234567890";

        assertThrows(IllegalArgumentException.class, () ->
                task.setDescription(fiftyCharacters + "1")
        );

        assertEquals("Record follow-up", task.getName());
        assertEquals("Save notes after the completed customer visit.", task.getDescription());
        assertEquals("TASK1003", task.getTaskId());
    }

    @Test
    void testEmptyStringsAreAllowedLikeContact() {
        Task task = new Task("", "", "");

        assertEquals("", task.getTaskId());
        assertEquals("", task.getName());
        assertEquals("", task.getDescription());

        task.setName("Review request");
        task.setDescription("Check the customer's requested visit time.");
        task.setName("");
        task.setDescription("");

        assertEquals("", task.getName());
        assertEquals("", task.getDescription());
        assertEquals("", task.getTaskId());
    }
}
