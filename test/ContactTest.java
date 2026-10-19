import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContactTest {

    @Test
    void testCreateValidContact() {
        Contact contact = new Contact(
                "JMIRANDA",
                "Jonathan",
                "Miranda",
                "4235550147",
                "17 Lantern Way"
        );

        assertEquals("JMIRANDA", contact.getContactId());
        assertEquals("Jonathan", contact.getFirstName());
        assertEquals("Miranda", contact.getLastName());
        assertEquals("4235550147", contact.getPhone());
        assertEquals("17 Lantern Way", contact.getAddress());
    }

    @Test
    void testContactIdAllowsTenCharacters() {
        Contact contact = new Contact(
                "1234567890",
                "Marisol",
                "Kepler",
                "6155550126",
                "42 Juniper Loop"
        );

        assertEquals("1234567890", contact.getContactId());
    }

    @Test
    void testContactIdCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        null,
                        "Ronan",
                        "Vale",
                        "8655550188",
                        "8 Copper Ridge"
                )
        );
    }

    @Test
    void testContactIdCannotExceedTenCharacters() {
        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "12345678901",
                        "Elara",
                        "Stone",
                        "4235550162",
                        "91 Summit Path"
                )
        );
    }

    @Test
    void testFirstNameValidation() {
        assertDoesNotThrow(() ->
                new Contact(
                        "ORBIT01",
                        "1234567890",
                        "Kepler",
                        "4235550147",
                        "17 Lantern Way"
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "ORBIT01",
                        null,
                        "Kepler",
                        "4235550147",
                        "17 Lantern Way"
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "ORBIT01",
                        "12345678901",
                        "Kepler",
                        "4235550147",
                        "17 Lantern Way"
                )
        );
    }

    @Test
    void testLastNameValidation() {
        assertDoesNotThrow(() ->
                new Contact(
                        "NOVA02",
                        "Marisol",
                        "1234567890",
                        "6155550126",
                        "42 Juniper Loop"
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "NOVA02",
                        "Marisol",
                        null,
                        "6155550126",
                        "42 Juniper Loop"
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "NOVA02",
                        "Marisol",
                        "12345678901",
                        "6155550126",
                        "42 Juniper Loop"
                )
        );
    }

    @Test
    void testPhoneValidation() {
        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "PINE03",
                        "Ronan",
                        "Vale",
                        null,
                        "6 River Bend"
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "PINE03",
                        "Ronan",
                        "Vale",
                        "423555123",
                        "6 River Bend"
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "PINE03",
                        "Ronan",
                        "Vale",
                        "42355512345",
                        "6 River Bend"
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "PINE03",
                        "Ronan",
                        "Vale",
                        "42355A1234",
                        "6 River Bend"
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "PINE03",
                        "Ronan",
                        "Vale",
                        "423-555-12",
                        "6 River Bend"
                )
        );
    }

    @Test
    void testAddressValidation() {
        String thirtyCharacters = "123456789012345678901234567890";

        assertDoesNotThrow(() ->
                new Contact(
                        "EMBER04",
                        "Elara",
                        "Stone",
                        "8655550188",
                        thirtyCharacters
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "EMBER04",
                        "Elara",
                        "Stone",
                        "8655550188",
                        null
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Contact(
                        "EMBER04",
                        "Elara",
                        "Stone",
                        "8655550188",
                        thirtyCharacters + "1"
                )
        );
    }

    @Test
    void testUpdateMutableFields() {
        Contact contact = new Contact(
                "TRAIL05",
                "Sable",
                "North",
                "4235550162",
                "91 Summit Path"
        );

        contact.setFirstName("Lyra");
        contact.setLastName("Harbor");
        contact.setPhone("4235550199");
        contact.setAddress("33 Ember Hollow");

        assertEquals("Lyra", contact.getFirstName());
        assertEquals("Harbor", contact.getLastName());
        assertEquals("4235550199", contact.getPhone());
        assertEquals("33 Ember Hollow", contact.getAddress());
        assertEquals("TRAIL05", contact.getContactId());
    }

    @Test
    void testSettersRejectInvalidValues() {
        Contact contact = new Contact(
                "RIDGE06",
                "Orion",
                "Drake",
                "6155550174",
                "6 River Bend"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> contact.setFirstName(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> contact.setLastName("12345678901")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> contact.setPhone("12345")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> contact.setAddress(
                        "1234567890123456789012345678901"
                )
        );

        assertEquals("Orion", contact.getFirstName());
        assertEquals("Drake", contact.getLastName());
        assertEquals("6155550174", contact.getPhone());
        assertEquals("6 River Bend", contact.getAddress());
    }
}