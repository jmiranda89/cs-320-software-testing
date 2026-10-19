import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactServiceTest {

    private ContactService service;
    private Contact contact;

    @BeforeEach
    void setUp() {
        service = new ContactService();
        contact = new Contact(
                "ORBIT01",
                "Marisol",
                "Kepler",
                "4235550147",
                "17 Lantern Way"
        );
    }

    @Test
    void testAddContact() {
        assertTrue(service.addContact(contact));
        assertEquals(1, service.getContactCount());
        assertSame(contact, service.getContact("ORBIT01"));
    }

    @Test
    void testAddMultipleContactsWithUniqueIds() {
        Contact secondContact = new Contact(
                "NOVA02",
                "Ronan",
                "Vale",
                "6155550126",
                "42 Juniper Loop"
        );

        assertTrue(service.addContact(contact));
        assertTrue(service.addContact(secondContact));
        assertEquals(2, service.getContactCount());
    }

    @Test
    void testDuplicateContactIdIsRejected() {
        Contact duplicate = new Contact(
                "ORBIT01",
                "Elara",
                "Stone",
                "8655550188",
                "8 Copper Ridge"
        );

        assertTrue(service.addContact(contact));
        assertFalse(service.addContact(duplicate));
        assertEquals(1, service.getContactCount());
        assertSame(contact, service.getContact("ORBIT01"));
    }

    @Test
    void testDuplicateContactIdIgnoresCase() {
        Contact first = new Contact(
                "CASEKEY7",
                "Sable",
                "North",
                "4235550162",
                "91 Summit Path"
        );

        Contact duplicate = new Contact(
                "casekey7",
                "Orion",
                "Drake",
                "6155550174",
                "6 River Bend"
        );

        assertTrue(service.addContact(first));
        assertFalse(service.addContact(duplicate));
        assertEquals(1, service.getContactCount());
    }

    @Test
    void testAddNullContactThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addContact(null)
        );
    }

    @Test
    void testDeleteContact() {
        service.addContact(contact);

        assertTrue(service.deleteContact("ORBIT01"));
        assertEquals(0, service.getContactCount());
        assertNull(service.getContact("ORBIT01"));
    }

    @Test
    void testDeleteMissingContactReturnsFalse() {
        service.addContact(contact);

        assertFalse(service.deleteContact("GHOST99"));
        assertEquals(1, service.getContactCount());
    }

    @Test
    void testUpdateFirstName() {
        service.addContact(contact);

        assertTrue(service.updateFirstName("ORBIT01", "Lyra"));
        assertEquals("Lyra", service.getContact("ORBIT01").getFirstName());
    }

    @Test
    void testUpdateLastName() {
        service.addContact(contact);

        assertTrue(service.updateLastName("ORBIT01", "Harbor"));
        assertEquals("Harbor", service.getContact("ORBIT01").getLastName());
    }

    @Test
    void testUpdatePhone() {
        service.addContact(contact);

        assertTrue(service.updatePhone("ORBIT01", "4235550199"));
        assertEquals("4235550199", service.getContact("ORBIT01").getPhone());
    }

    @Test
    void testUpdateAddress() {
        service.addContact(contact);

        assertTrue(service.updateAddress("ORBIT01", "33 Ember Hollow"));
        assertEquals(
                "33 Ember Hollow",
                service.getContact("ORBIT01").getAddress()
        );
    }

    @Test
    void testUpdateMissingContactReturnsFalse() {
        service.addContact(contact);

        assertFalse(service.updateFirstName("GHOST99", "Lyra"));
        assertFalse(service.updateLastName("GHOST99", "Harbor"));
        assertFalse(service.updatePhone("GHOST99", "4235550199"));
        assertFalse(service.updateAddress("GHOST99", "33 Ember Hollow"));

        assertEquals(1, service.getContactCount());
        assertEquals("Marisol", service.getContact("ORBIT01").getFirstName());
        assertEquals("Kepler", service.getContact("ORBIT01").getLastName());
        assertEquals("4235550147", service.getContact("ORBIT01").getPhone());
        assertEquals("17 Lantern Way", service.getContact("ORBIT01").getAddress());
    }

    @Test
    void testUpdateAndDeleteBeyondFirstContact() {
        Contact secondContact = new Contact(
                "NOVA02",
                "Ronan",
                "Vale",
                "6155550126",
                "42 Juniper Loop"
        );

        Contact thirdContact = new Contact(
                "PINE03",
                "Elara",
                "Stone",
                "8655550188",
                "8 Copper Ridge"
        );

        service.addContact(contact);
        service.addContact(secondContact);
        service.addContact(thirdContact);

        assertTrue(service.updatePhone("PINE03", "8655550111"));
        assertEquals("8655550111", service.getContact("PINE03").getPhone());

        assertTrue(service.deleteContact("NOVA02"));
        assertEquals(2, service.getContactCount());
        assertNull(service.getContact("NOVA02"));

        assertSame(contact, service.getContact("ORBIT01"));
        assertSame(thirdContact, service.getContact("PINE03"));
    }

    @Test
    void testInvalidUpdateIsRejected() {
        service.addContact(contact);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updatePhone("ORBIT01", "12345")
        );

        assertEquals("4235550147", service.getContact("ORBIT01").getPhone());
    }

    @Test
    void testContactIdDoesNotChangeDuringUpdates() {
        service.addContact(contact);

        service.updateFirstName("ORBIT01", "Lyra");
        service.updateLastName("ORBIT01", "Harbor");
        service.updatePhone("ORBIT01", "4235550199");
        service.updateAddress("ORBIT01", "33 Ember Hollow");

        assertEquals("ORBIT01", service.getContact("ORBIT01").getContactId());
    }
}