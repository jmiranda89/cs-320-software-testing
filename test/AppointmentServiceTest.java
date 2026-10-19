import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointmentServiceTest {

    private AppointmentService service;
    private Appointment appointment;
    private Appointment secondAppointment;
    private Appointment thirdAppointment;

    @BeforeEach
    void setUp() {
        service = new AppointmentService();
        long now = System.currentTimeMillis();
        appointment = new Appointment(
                "APT1001", new Date(now + 86_400_000L), "Customer service consultation."
        );
        secondAppointment = new Appointment(
                "APT1002", new Date(now + 172_800_000L), "Review requested service changes."
        );
        thirdAppointment = new Appointment(
                "APT1003", new Date(now + 259_200_000L), "Follow up on the customer's previous visit."
        );
    }

    @Test
    void testAddAppointment() {
        assertTrue(service.addAppointment(appointment));
        assertEquals(1, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
    }

    @Test
    void testAddMultipleAppointmentsWithUniqueIds() {
        assertTrue(service.addAppointment(appointment));
        assertTrue(service.addAppointment(secondAppointment));
        assertTrue(service.addAppointment(thirdAppointment));

        assertEquals(3, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
        assertSame(thirdAppointment, service.getAppointment("APT1003"));
    }

    @Test
    void testDuplicateIdIsRejectedWithoutReplacingOriginalOrNeighbors() {
        Date originalDate = secondAppointment.getAppointmentDate();
        Appointment duplicate = new Appointment(
                "APT1002", thirdAppointment.getAppointmentDate(),
                "Confirm details for the scheduled consultation."
        );
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);
        service.addAppointment(thirdAppointment);

        assertFalse(service.addAppointment(duplicate));

        assertEquals(3, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
        assertSame(thirdAppointment, service.getAppointment("APT1003"));
        assertEquals(originalDate, secondAppointment.getAppointmentDate());
        assertEquals("Review requested service changes.", secondAppointment.getDescription());
        assertEquals("APT1002", secondAppointment.getAppointmentId());
    }

    @Test
    void testDuplicateIdIgnoresCaseBeyondFirstAppointment() {
        Date originalDate = thirdAppointment.getAppointmentDate();
        Appointment duplicate = new Appointment(
                "apt1003", appointment.getAppointmentDate(),
                "Review account information with the customer."
        );
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);
        service.addAppointment(thirdAppointment);

        assertFalse(service.addAppointment(duplicate));

        assertEquals(3, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
        assertSame(thirdAppointment, service.getAppointment("apt1003"));
        assertEquals(originalDate, thirdAppointment.getAppointmentDate());
        assertEquals("Follow up on the customer's previous visit.", thirdAppointment.getDescription());
        assertEquals("APT1003", thirdAppointment.getAppointmentId());
    }

    @Test
    void testAddingSameAppointmentTwiceIsRejected() {
        assertTrue(service.addAppointment(appointment));
        assertFalse(service.addAppointment(appointment));
        assertEquals(1, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
    }

    @Test
    void testAddNullAppointmentThrowsWithoutChangingService() {
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);

        assertThrows(IllegalArgumentException.class, () -> service.addAppointment(null));

        assertEquals(2, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
    }

    @Test
    void testDeleteOnlyAppointmentAndRejectRepeatedDeletion() {
        service.addAppointment(appointment);

        assertTrue(service.deleteAppointment("APT1001"));
        assertEquals(0, service.getAppointmentCount());
        assertNull(service.getAppointment("APT1001"));
        assertFalse(service.deleteAppointment("APT1001"));
        assertEquals(0, service.getAppointmentCount());
    }

    @Test
    void testDeleteFirstAppointmentPreservesRemainingAppointments() {
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);
        service.addAppointment(thirdAppointment);

        assertTrue(service.deleteAppointment("APT1001"));

        assertEquals(2, service.getAppointmentCount());
        assertNull(service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
        assertSame(thirdAppointment, service.getAppointment("APT1003"));
    }

    @Test
    void testDeleteMiddleAppointmentIgnoringCasePreservesNeighbors() {
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);
        service.addAppointment(thirdAppointment);

        assertTrue(service.deleteAppointment("apt1002"));

        assertEquals(2, service.getAppointmentCount());
        assertNull(service.getAppointment("APT1002"));
        assertSame(appointment, service.getAppointment("APT1001"));
        assertSame(thirdAppointment, service.getAppointment("APT1003"));
    }

    @Test
    void testDeleteLastAppointmentPreservesEarlierAppointments() {
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);
        service.addAppointment(thirdAppointment);

        assertTrue(service.deleteAppointment("APT1003"));

        assertEquals(2, service.getAppointmentCount());
        assertNull(service.getAppointment("APT1003"));
        assertSame(appointment, service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
    }

    @Test
    void testDeleteUnknownIdPreservesAllAppointments() {
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);
        service.addAppointment(thirdAppointment);

        assertFalse(service.deleteAppointment("APT9999"));

        assertEquals(3, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
        assertSame(thirdAppointment, service.getAppointment("APT1003"));
    }

    @Test
    void testNullIdMatchesNothingAndPreservesAppointments() {
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);

        assertNull(service.getAppointment(null));
        assertFalse(service.deleteAppointment(null));
        assertEquals(2, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
    }

    @Test
    void testLookupFindsNonFirstAppointmentsIgnoringCase() {
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);
        service.addAppointment(thirdAppointment);

        assertSame(secondAppointment, service.getAppointment("apt1002"));
        assertSame(thirdAppointment, service.getAppointment("apt1003"));
        assertSame(appointment, service.getAppointment("APT1001"));
        assertEquals(3, service.getAppointmentCount());
    }

    @Test
    void testMissingLookupReturnsNullWithoutChangingService() {
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);

        assertNull(service.getAppointment("APT9999"));
        assertEquals(2, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
    }

    @Test
    void testEmptyServiceHasNoMatchingAppointment() {
        assertEquals(0, service.getAppointmentCount());
        assertNull(service.getAppointment("APT1001"));
        assertNull(service.getAppointment(null));
        assertFalse(service.deleteAppointment("APT1001"));
        assertFalse(service.deleteAppointment(null));
        assertEquals(0, service.getAppointmentCount());
    }

    @Test
    void testDeletedIdCanBeReusedWithoutAffectingOtherAppointments() {
        Appointment replacement = new Appointment(
                "apt1001", thirdAppointment.getAppointmentDate(),
                "Confirm details for the scheduled consultation."
        );
        service.addAppointment(appointment);
        service.addAppointment(secondAppointment);

        assertTrue(service.deleteAppointment("APT1001"));
        assertTrue(service.addAppointment(replacement));

        assertEquals(2, service.getAppointmentCount());
        assertSame(replacement, service.getAppointment("APT1001"));
        assertSame(secondAppointment, service.getAppointment("APT1002"));
    }

    @Test
    void testEmptyIdEnforcesUniquenessAndSupportsLookupAndDelete() {
        Appointment emptyIdAppointment = new Appointment(
                "", appointment.getAppointmentDate(), "Review account information with the customer."
        );
        Appointment duplicate = new Appointment(
                "", secondAppointment.getAppointmentDate(), "Review requested service changes."
        );
        service.addAppointment(appointment);

        assertTrue(service.addAppointment(emptyIdAppointment));
        assertFalse(service.addAppointment(duplicate));
        assertEquals(2, service.getAppointmentCount());
        assertSame(emptyIdAppointment, service.getAppointment(""));
        assertSame(appointment, service.getAppointment("APT1001"));

        assertTrue(service.deleteAppointment(""));
        assertNull(service.getAppointment(""));
        assertEquals(1, service.getAppointmentCount());
        assertSame(appointment, service.getAppointment("APT1001"));
    }
}
