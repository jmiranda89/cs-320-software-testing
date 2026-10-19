import java.util.Date;

/**
 * A scheduled customer visit with a fixed ID, date, and description.
 * AppointmentService enforces ID uniqueness.
 */
public class Appointment {

    private final String appointmentId;
    private final Date appointmentDate;
    private final String description;

    /**
     * Creates an appointment after validating its ID, date, and description.
     *
     * @param appointmentId non-null ID of up to 10 characters
     * @param appointmentDate non-null date that is not in the past
     * @param description non-null description of up to 50 characters
     * @throws IllegalArgumentException if any value is invalid
     */
    public Appointment(String appointmentId, Date appointmentDate, String description) {
        validateAppointmentId(appointmentId);

        // Validate a copy so later changes to the caller's Date cannot affect this visit.
        Date dateCopy = appointmentDate == null ? null : new Date(appointmentDate.getTime());
        validateAppointmentDate(dateCopy);
        validateDescription(description);

        this.appointmentId = appointmentId;
        this.appointmentDate = dateCopy;
        this.description = description;
    }

    /**
     * Returns the ID assigned at construction.
     *
     * @return the appointment's fixed ID
     */
    public String getAppointmentId() {
        return appointmentId;
    }

    /**
     * Returns a copy of the date to protect the stored value from outside changes.
     *
     * @return a date with the appointment's scheduled time
     */
    public Date getAppointmentDate() {
        return new Date(appointmentDate.getTime());
    }

    /**
     * Returns the details supplied when the appointment was created.
     *
     * @return the appointment description
     */
    public String getDescription() {
        return description;
    }

    private void validateAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.length() > 10) {
            throw new IllegalArgumentException(
                    "Appointment ID cannot be null or longer than 10 characters."
            );
        }
    }

    private void validateAppointmentDate(Date appointmentDate) {
        if (appointmentDate == null || appointmentDate.before(new Date())) {
            throw new IllegalArgumentException(
                    "Appointment date cannot be null or in the past."
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
