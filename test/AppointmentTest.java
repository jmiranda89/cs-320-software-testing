import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;
import java.util.Date;

import org.junit.jupiter.api.Test;

class AppointmentTest {

    @Test
    void testCreateValidAppointmentWithFutureDate() {
        Date scheduledDate = futureDate();
        Appointment appointment = new Appointment(
                "APT1001", scheduledDate, "Customer service consultation."
        );

        assertEquals("APT1001", appointment.getAppointmentId());
        assertEquals(scheduledDate, appointment.getAppointmentDate());
        assertEquals("Customer service consultation.", appointment.getDescription());
    }

    @Test
    void testAppointmentIdAllowsTenCharacters() {
        Appointment appointment = new Appointment(
                "1234567890", futureDate(), "Review requested service changes."
        );

        assertEquals("1234567890", appointment.getAppointmentId());
    }

    @Test
    void testAppointmentIdCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Appointment(null, futureDate(), "Customer service consultation.")
        );
    }

    @Test
    void testAppointmentIdCannotExceedTenCharacters() {
        assertThrows(IllegalArgumentException.class, () ->
                new Appointment("12345678901", futureDate(), "Customer service consultation.")
        );
    }

    @Test
    void testAppointmentIdIsPrivateAndFinalWithNoSetter() throws NoSuchFieldException {
        int modifiers = Appointment.class.getDeclaredField("appointmentId").getModifiers();

        assertTrue(Modifier.isPrivate(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
        assertThrows(NoSuchMethodException.class, () ->
                Appointment.class.getMethod("setAppointmentId", String.class)
        );
    }

    @Test
    void testAppointmentDateCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Appointment("APT1001", null, "Customer service consultation.")
        );
    }

    @Test
    void testAppointmentDateCannotBeInThePast() {
        Date yesterday = new Date(System.currentTimeMillis() - 86_400_000L);

        assertThrows(IllegalArgumentException.class, () ->
                new Appointment("APT1001", yesterday, "Customer service consultation.")
        );
    }

    @Test
    void testChangingOriginalDateDoesNotChangeAppointment() {
        Date scheduledDate = futureDate();
        long scheduledTime = scheduledDate.getTime();
        Appointment appointment = new Appointment(
                "APT1002", scheduledDate, "Review requested service changes."
        );

        scheduledDate.setTime(System.currentTimeMillis() - 86_400_000L);

        assertEquals(scheduledTime, appointment.getAppointmentDate().getTime());
        assertEquals("APT1002", appointment.getAppointmentId());
        assertEquals("Review requested service changes.", appointment.getDescription());
    }

    @Test
    void testChangingReturnedDateDoesNotChangeAppointment() {
        Date scheduledDate = futureDate();
        Appointment appointment = new Appointment(
                "APT1003", scheduledDate, "Follow up on the customer's previous visit."
        );
        Date returnedDate = appointment.getAppointmentDate();

        returnedDate.setTime(System.currentTimeMillis() - 86_400_000L);

        assertEquals(scheduledDate, appointment.getAppointmentDate());
        assertEquals("APT1003", appointment.getAppointmentId());
        assertEquals("Follow up on the customer's previous visit.", appointment.getDescription());
    }

    @Test
    void testDescriptionAllowsFiftyCharacters() {
        String fiftyCharacters = "12345678901234567890123456789012345678901234567890";
        Appointment appointment = new Appointment("APT1001", futureDate(), fiftyCharacters);

        assertEquals(fiftyCharacters, appointment.getDescription());
    }

    @Test
    void testDescriptionCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Appointment("APT1001", futureDate(), null)
        );
    }

    @Test
    void testDescriptionCannotExceedFiftyCharacters() {
        String fiftyCharacters = "12345678901234567890123456789012345678901234567890";

        assertThrows(IllegalArgumentException.class, () ->
                new Appointment("APT1001", futureDate(), fiftyCharacters + "1")
        );
    }

    @Test
    void testEmptyIdAndDescriptionAreAllowedLikeContactAndTask() {
        Date scheduledDate = futureDate();
        Appointment appointment = new Appointment("", scheduledDate, "");

        assertEquals("", appointment.getAppointmentId());
        assertEquals(scheduledDate, appointment.getAppointmentDate());
        assertEquals("", appointment.getDescription());
    }

    private Date futureDate() {
        return new Date(System.currentTimeMillis() + 86_400_000L);
    }
}
