import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores appointments in memory and matches IDs without regard to case.
 * Duplicate adds and missing delete targets leave the stored appointments unchanged.
 */
public class AppointmentService {

    private final List<Appointment> appointmentList = new ArrayList<>();

    /**
     * Starts with no appointments.
     */
    public AppointmentService() {
    }

    /**
     * Adds the supplied appointment if its ID is not already stored.
     *
     * @param appointment the appointment to store
     * @return true if added; false if the ID already exists, ignoring case
     * @throws IllegalArgumentException if the appointment is null
     */
    public boolean addAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null.");
        }

        for (Appointment existingAppointment : appointmentList) {
            if (existingAppointment.getAppointmentId()
                    .equalsIgnoreCase(appointment.getAppointmentId())) {
                return false;
            }
        }

        appointmentList.add(appointment);
        return true;
    }

    /**
     * Removes the appointment matching the supplied ID.
     *
     * @param appointmentId the ID to match, ignoring case; null matches nothing
     * @return true if removed; false if no appointment matches
     */
    public boolean deleteAppointment(String appointmentId) {
        Iterator<Appointment> iterator = appointmentList.iterator();

        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();

            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    /**
     * Finds the stored appointment matching the supplied ID.
     *
     * @param appointmentId the ID to match, ignoring case; null matches nothing
     * @return the stored instance, or null if no appointment matches
     */
    public Appointment getAppointment(String appointmentId) {
        return findAppointment(appointmentId);
    }

    /**
     * Returns the number of appointments currently stored.
     *
     * @return the appointment count
     */
    public int getAppointmentCount() {
        return appointmentList.size();
    }

    private Appointment findAppointment(String appointmentId) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                return appointment;
            }
        }

        return null;
    }
}
