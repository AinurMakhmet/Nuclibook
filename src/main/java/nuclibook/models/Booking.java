package nuclibook.models;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import nuclibook.server.Renderable;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Model representing a booking.
 */
@DatabaseTable(tableName = "bookings")
public class Booking implements Renderable {

	@DatabaseField(generatedId = true)
	private Integer id;

	@DatabaseField(columnName = "patient_id", foreign = true, foreignAutoRefresh = true)
	private Patient patient;

	@DatabaseField(columnName = "therapy_id", foreign = true, foreignAutoRefresh = true)
	private Therapy therapy;

	@DatabaseField(columnName = "camera_id", foreign = true, foreignAutoRefresh = true)
	private Camera camera;

	@DatabaseField(columnName = "tracer_id", foreign = true, foreignAutoRefresh = true)
	private Tracer tracer;

	@DatabaseField(width = 32, columnName = "tracer_dose")
	private String tracerDose;

	@ForeignCollectionField(eager = true)
	private ForeignCollection<BookingSection> bookingSections;

	@ForeignCollectionField(eager = true)
	private ForeignCollection<BookingStaff> bookingStaff;

	@DatabaseField(width = 16)
	private String status;

	@DatabaseField(dataType = DataType.LONG_STRING)
	private String notes;

	/**
	 * Blank constructor for ORM.
	 */
	public Booking() {
	}

	/**
	 * Get the ID of the booking.
	 *
	 * @return The ID of the booking.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set the ID of the booking.
	 *
	 * @param id The ID of the booking.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Get the patient.
	 *
	 * @return The patient.
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * Set the patient.
	 *
	 * @param patient The patient.
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * Get the therapy.
	 *
	 * @return The therapy.
	 */
	public Therapy getTherapy() {
		return therapy;
	}

	/**
	 * Set the therapy.
	 *
	 * @param therapy The therapy.
	 */
	public void setTherapy(Therapy therapy) {
		this.therapy = therapy;
	}

	/**
	 * Get the camera.
	 *
	 * @return The camera.
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Set the camera
	 *
	 * @param camera The camera.
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	/**
	 * Get the tracer
	 *
	 * @return The tracer
	 */
	public Tracer getTracer() {
		return tracer;
	}

	/**
	 * Set the tracer
	 *
	 * @param tracer The tracer
	 */
	public void setTracer(Tracer tracer) {
		this.tracer = tracer;
	}

	/**
	 * Get the tracer dose.
	 *
	 * @return The tracer dose.
	 */
	public String getTracerDose() {
		return tracerDose;
	}

	/**
	 * Set the tracer dose
	 *
	 * @param tracerDose The tracer dose.
	 */
	public void setTracerDose(String tracerDose) {
		this.tracerDose = tracerDose;
	}

	/**
	 * Get the list of booking sections for this booking.
	 *
	 * @return The list of booking sections for this booking.
	 */
	public List<BookingSection> getBookingSections() {
		ArrayList<BookingSection> output = new ArrayList<>();
		try {
			bookingSections.refreshCollection();
		} catch (SQLException | NullPointerException e) {
			return output;
		}
		CloseableIterator<BookingSection> iterator = bookingSections.closeableIterator();
		try {
			BookingSection bs;
			while (iterator.hasNext()) {
				bs = iterator.next();
				if (bs != null) output.add(bs);
			}
		} finally {
			iterator.closeQuietly();
		}

		// sort by date
		output.sort(new Comparator<BookingSection>() {
			@Override
			public int compare(BookingSection o1, BookingSection o2) {
				return o1.getStart().compareTo(o2.getStart());
			}
		});

		return output;
	}

	/**
	 * Get the list of staff for this booking.
	 *
	 * @return The list of staff for this booking.
	 */
	public List<Staff> getStaff() {
		ArrayList<Staff> output = new ArrayList<>();
		try {
			bookingStaff.refreshCollection();
		} catch (SQLException | NullPointerException e) {
			return output;
		}
		CloseableIterator<BookingStaff> iterator = bookingStaff.closeableIterator();
		try {
			BookingStaff bs;
			while (iterator.hasNext()) {
				bs = iterator.next();
				if (bs != null) output.add(bs.getStaff());
			}
		} finally {
			iterator.closeQuietly();
		}
		return output;
	}

	/**
	 * Get the list of staff for this booking.
	 *
	 * @return The list of staff for this booking.
	 */
	public List<BookingStaff> getBookingStaff() {
		ArrayList<BookingStaff> output = new ArrayList<>();
		try {
			bookingStaff.refreshCollection();
		} catch (SQLException | NullPointerException e) {
			return output;
		}
		CloseableIterator<BookingStaff> iterator = bookingStaff.closeableIterator();
		try {
			BookingStaff bs;
			while (iterator.hasNext()) {
				bs = iterator.next();
				if (bs != null) output.add(bs);
			}
		} finally {
			iterator.closeQuietly();
		}
		return output;
	}

	/**
	 * Get the status of the booking.
	 *
	 * @return The status of the booking.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Set the status of the booking
	 *
	 * @param status The status of the booking.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Get the notes associated with the booking.
	 *
	 * @return The notes associated with the booking.
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Set the notes associated with the booking.
	 *
	 * @param notes The notes associated with the booking.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public HashMap<String, String> getHashMap() {
		return new HashMap<String, String>() {{
			put("booking-id", getId().toString());
			put("patient-name", getPatient().getName());
			put("therapy-name", getTherapy().getName());
			put("camera-id", getCamera().getId().toString());
			put("camera-type-label", getCamera().getType().getLabel());
			put("camera-room-number", getCamera().getRoomNumber());
			put("tracer-name", getTracer().getName());
			put("tracer-id", getTracer().getId().toString());
			put("tracer-dose", getTracerDose());
			put("status", getStatus());

			// get status label
			String statusLabel = "default";
			if (getStatus().equals("unconfirmed")) statusLabel = "warning";
			if (getStatus().equals("confirmed")) statusLabel = "success";
			if (getStatus().equals("rebooked")) statusLabel = "info";
			put("status-with-label", "<span class=\"label label-as-badge label-" + statusLabel + "\">" + getStatus() + "</span>");

			// get date
			List<BookingSection> bookingSections = getBookingSections();
			if (bookingSections.isEmpty()) {
				put("date", "?");
			} else {
				put("date", bookingSections.get(0).getStart().toString("YYYY-MM-dd"));
			}

			// set up booking sections as string for day summary
			String bookingSectionsAsString = "";
			String bookingSectionsAsStringTimeOnly = "";
			if (!bookingSections.isEmpty()) {

				for (BookingSection b : bookingSections) {
                    String date = b.getStart().toString().substring(0,10);
					bookingSectionsAsString += "<li class=\"list-group-item\">\n";
					String startTime = b.getStart().toString("HH:mm");
					String endTime = b.getEnd().toString("HH:mm");
					bookingSectionsAsString += (startTime + " to " + endTime + " \n");
					bookingSectionsAsString += "</li>";
					bookingSectionsAsStringTimeOnly += (date + startTime  + endTime + ", ");
				}
                bookingSectionsAsStringTimeOnly = bookingSectionsAsStringTimeOnly.substring(0, bookingSectionsAsStringTimeOnly.length() - 2);
            } else {
				bookingSectionsAsString = "<em>None</em>\n";
			}
			put("booking-sections-as-string", bookingSectionsAsString);
			put("booking-sections-as-string-time-only", bookingSectionsAsStringTimeOnly);

			// get days until
			if (bookingSections.isEmpty()) {
				put("days-until", "?");
			} else {
				int daysUntil = Days.daysBetween(new LocalDate(), bookingSections.get(0).getStart().toLocalDate()).getDays();
				if (daysUntil == 0) {
					put("days-until", "today");
				} else if (daysUntil < 0) {
					put("days-until", (daysUntil * -1) + " day" + (daysUntil == -1 ? "" : "s") + " ago");
				} else {
					put("days-until", "in " + daysUntil + " day" + (daysUntil == -1 ? "" : "s"));
				}
			}

			// get staff
			String staff = "";
			if (getStaff().isEmpty()) {
				staff = "<em>None</em>";
			} else {
				List<Staff> assignedStaff = getStaff();
				for (Staff s : assignedStaff) {
					staff += s.getName() + ", ";
				}

				staff = staff.substring(0, staff.length() - 2);
			}
			put("staff", staff);
			//get staffID
			String staffId = "";
			if (getStaff().isEmpty()) {
				staffId = "<em>None</em>";
			} else {
				List<Staff> assignedStaff = getStaff();
				for (Staff s : assignedStaff) {
					staffId += s.getId() + ", ";
				}

				staffId = staffId.substring(0, staffId.length() - 2);
			}
			put("staff-id-list", staffId);

			// get notes
			String notes = getNotes();
			if (notes == null || notes.length() == 0) {
				notes = "<em>None</em>";
			} else {
				notes = notes.replace("\n", "<br />");
			}
			put("notes", notes);
			put("notes-unformatted", getNotes());
		}};
	}
}