/**
 * A person's contact information with validated names, phone number, and address.
 * The contact ID is immutable after construction; ContactService enforces uniqueness.
 */
public class Contact {

    private final String contactId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    /**
     * Creates a contact after validating every supplied value.
     *
     * @param contactId non-null ID of up to 10 characters
     * @param firstName non-null first name of up to 10 characters
     * @param lastName non-null last name of up to 10 characters
     * @param phone non-null phone number containing exactly 10 digits
     * @param address non-null address of up to 30 characters
     * @throws IllegalArgumentException if any value is null or violates its constraint
     */
    public Contact(String contactId, String firstName, String lastName,
                   String phone, String address) {
        validateContactId(contactId);
        validateFirstName(firstName);
        validateLastName(lastName);
        validatePhone(phone);
        validateAddress(address);

        this.contactId = contactId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Returns the ID assigned at construction.
     *
     * @return the contact's fixed ID
     */
    public String getContactId() {
        return contactId;
    }

    /**
     * Returns the contact's current first name.
     *
     * @return the validated first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the contact's current last name.
     *
     * @return the validated last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the phone number as digits without separators.
     *
     * @return the contact's 10-digit phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the contact's current address.
     *
     * @return the validated address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Updates the first name after validation, preserving the current value on rejection.
     *
     * @param firstName non-null replacement first name of up to 10 characters
     * @throws IllegalArgumentException if the name is null or exceeds 10 characters
     */
    public void setFirstName(String firstName) {
        validateFirstName(firstName);
        this.firstName = firstName;
    }

    /**
     * Updates the last name after validation, preserving the current value on rejection.
     *
     * @param lastName non-null replacement last name of up to 10 characters
     * @throws IllegalArgumentException if the name is null or exceeds 10 characters
     */
    public void setLastName(String lastName) {
        validateLastName(lastName);
        this.lastName = lastName;
    }

    /**
     * Updates the phone number after validation, preserving the current value on rejection.
     *
     * @param phone non-null replacement phone number containing exactly 10 digits
     * @throws IllegalArgumentException if the number is null or is not exactly 10 digits
     */
    public void setPhone(String phone) {
        validatePhone(phone);
        this.phone = phone;
    }

    /**
     * Updates the address after validation, preserving the current value on rejection.
     *
     * @param address non-null replacement address of up to 30 characters
     * @throws IllegalArgumentException if the address is null or exceeds 30 characters
     */
    public void setAddress(String address) {
        validateAddress(address);
        this.address = address;
    }

    private void validateContactId(String contactId) {
        if (contactId == null || contactId.length() > 10) {
            throw new IllegalArgumentException(
                    "Contact ID cannot be null or longer than 10 characters."
            );
        }
    }

    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.length() > 10) {
            throw new IllegalArgumentException(
                    "First name cannot be null or longer than 10 characters."
            );
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.length() > 10) {
            throw new IllegalArgumentException(
                    "Last name cannot be null or longer than 10 characters."
            );
        }
    }

    private void validatePhone(String phone) {
        // Match the entire value so spaces, separators, and partial numbers are rejected.
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException(
                    "Phone number must contain exactly 10 digits."
            );
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.length() > 30) {
            throw new IllegalArgumentException(
                    "Address cannot be null or longer than 30 characters."
            );
        }
    }
}
