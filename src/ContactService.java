import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores contacts in memory and matches IDs without regard to case.
 * Duplicate adds and missing delete/update targets leave stored contacts unchanged.
 * Field updates use Contact's validation and preserve each contact's fixed ID.
 */
public class ContactService {

    private final List<Contact> contactList = new ArrayList<>();

    /**
     * Adds the supplied contact if its ID is not already stored.
     *
     * @param contact the contact instance to store
     * @return true if added; false if the ID already exists, ignoring case
     * @throws IllegalArgumentException if the contact is null
     */
    public boolean addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null.");
        }

        for (Contact existingContact : contactList) {
            if (existingContact.getContactId()
                    .equalsIgnoreCase(contact.getContactId())) {
                return false;
            }
        }

        contactList.add(contact);
        return true;
    }

    /**
     * Removes the contact matching the supplied ID.
     *
     * @param contactId the ID to match, ignoring case; null matches nothing
     * @return true if removed; false if no contact matches
     */
    public boolean deleteContact(String contactId) {
        Iterator<Contact> iterator = contactList.iterator();

        while (iterator.hasNext()) {
            Contact contact = iterator.next();

            if (contact.getContactId().equalsIgnoreCase(contactId)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    /**
     * Updates a matching contact's first name; missing contacts skip validation.
     *
     * @param contactId the ID to match, ignoring case; null matches nothing
     * @param firstName non-null replacement first name of up to 10 characters
     * @return true if updated; false if no contact matches
     * @throws IllegalArgumentException if a contact matches and the name is null
     *         or exceeds 10 characters; the current value is retained
     */
    public boolean updateFirstName(String contactId, String firstName) {
        Contact contact = findContact(contactId);

        if (contact == null) {
            return false;
        }

        contact.setFirstName(firstName);
        return true;
    }

    /**
     * Updates a matching contact's last name; missing contacts skip validation.
     *
     * @param contactId the ID to match, ignoring case; null matches nothing
     * @param lastName non-null replacement last name of up to 10 characters
     * @return true if updated; false if no contact matches
     * @throws IllegalArgumentException if a contact matches and the name is null
     *         or exceeds 10 characters; the current value is retained
     */
    public boolean updateLastName(String contactId, String lastName) {
        Contact contact = findContact(contactId);

        if (contact == null) {
            return false;
        }

        contact.setLastName(lastName);
        return true;
    }

    /**
     * Updates a matching contact's phone number; missing contacts skip validation.
     *
     * @param contactId the ID to match, ignoring case; null matches nothing
     * @param phone non-null replacement phone number containing exactly 10 digits
     * @return true if updated; false if no contact matches
     * @throws IllegalArgumentException if a contact matches and the number is null
     *         or is not exactly 10 digits; the current value is retained
     */
    public boolean updatePhone(String contactId, String phone) {
        Contact contact = findContact(contactId);

        if (contact == null) {
            return false;
        }

        contact.setPhone(phone);
        return true;
    }

    /**
     * Updates a matching contact's address; missing contacts skip validation.
     *
     * @param contactId the ID to match, ignoring case; null matches nothing
     * @param address non-null replacement address of up to 30 characters
     * @return true if updated; false if no contact matches
     * @throws IllegalArgumentException if a contact matches and the address is null
     *         or exceeds 30 characters; the current value is retained
     */
    public boolean updateAddress(String contactId, String address) {
        Contact contact = findContact(contactId);

        if (contact == null) {
            return false;
        }

        contact.setAddress(address);
        return true;
    }

    /**
     * Finds the stored contact instance; its setters continue to validate changes.
     *
     * @param contactId the ID to match, ignoring case; null matches nothing
     * @return the stored instance, or null if no contact matches
     */
    public Contact getContact(String contactId) {
        return findContact(contactId);
    }

    /**
     * Returns the number of contacts currently stored.
     *
     * @return the contact count
     */
    public int getContactCount() {
        return contactList.size();
    }

    // Keep lookups consistent with the case-insensitive uniqueness check during adds.
    private Contact findContact(String contactId) {
        for (Contact contact : contactList) {
            if (contact.getContactId().equalsIgnoreCase(contactId)) {
                return contact;
            }
        }

        return null;
    }
}
