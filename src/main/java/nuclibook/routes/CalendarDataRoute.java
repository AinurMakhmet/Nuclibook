package nuclibook.routes;

import nuclibook.constants.P;
import nuclibook.entity_utils.ActionLogger;
import nuclibook.entity_utils.BookingUtils;
import nuclibook.entity_utils.CameraTypeUtils;
import nuclibook.entity_utils.SecurityUtils;
import nuclibook.models.Booking;
import nuclibook.models.BookingSection;
import org.joda.time.DateTime;
import spark.Request;
import spark.Response;

import java.util.List;

public class CalendarDataRoute extends DefaultRoute {

	@Override
	public Object handle(Request request, Response response) throws Exception {
		// necessary prelim routine
		prepareToHandle();

		// security check
		if (!SecurityUtils.requirePermission(P.VIEW_APPOINTMENTS, response)) {
			ActionLogger.logAction(ActionLogger.ATTEMPT_VIEW_BOOKING_CALENDAR, 0, "Failed as user does not have permissions for this action");
			return "no_permission";
		}

		// get start/end date
		String start = request.queryParams("start");
		String end = request.queryParams("end");
		DateTime startDate = new DateTime(start + "T00:00:00.000");
		DateTime endDate = new DateTime(end + "T23:59:59.999");

		// start json
		StringBuilder jsonOutput = new StringBuilder();
		jsonOutput.append("{");
		boolean commaNeeded;

		/*
		BOOKINGS SECTION
		 */

		if (request.queryParams("bookings") != null && request.queryParams("bookings").equals("1")) {
			// get bookings between start/end dates
			List<Booking> bookings = BookingUtils.getBookingsByDateRange(startDate, endDate);

			// include cancelled?
			boolean includeCancelledBookings = request.queryParams("cancelledBookings") != null && request.queryParams("cancelledBookings").equals("1");

			// open section
			jsonOutput.append("\"bookings\": [");

			// loop bookings
			commaNeeded = false;
			for (Booking booking : bookings) {
				// include cancelled?
				if (!includeCancelledBookings && booking.getStatus().equals("cancelled")) {
					continue;
				}

				// open booking object
				if (commaNeeded) {
					jsonOutput.append(",");
				}
				commaNeeded = true;
				jsonOutput.append("{");

				// append basic info
				jsonOutput.append("\"id\": \"").append(booking.getId()).append("\",");
				jsonOutput.append("\"patientName\": \"").append(booking.getPatient().getName()).append("\",");
				jsonOutput.append("\"therapyName\": \"").append(booking.getTherapy().getName().replace("\"", "\\\"")).append("\",");
				jsonOutput.append("\"cameraName\": \"")
						.append(CameraTypeUtils
										.getCameraType(
												booking
														.getCamera()
														.getType()
														.getId()
										).getLabel()
										.replace("\"", "\\\"")
						)
						.append(", ")
						.append(booking
								.getCamera()
								.getRoomNumber()
								.replace("\"", "\\\""))
						.append("\",");
				jsonOutput.append("\"status\": \"").append(booking.getStatus()).append("\",");

				// open booking section array
				jsonOutput.append("\"bookingSections\": [");

				// get and loop booking sections
				List<BookingSection> bookingSections = booking.getBookingSections();
				for (int j = 0; j < bookingSections.size(); j++) {
					// open object
					if (j != 0) jsonOutput.append(",");
					jsonOutput.append("{");

					// append times
					jsonOutput.append("\"startTime\": \"").append(bookingSections.get(j).getStart().toString("YYYY-MM-dd HH:mm")).append("\",");
					jsonOutput.append("\"endTime\": \"").append(bookingSections.get(j).getEnd().toString("YYYY-MM-dd HH:mm")).append("\"");

					// close object
					jsonOutput.append("}");
				}

				// close booking section array
				jsonOutput.append("]");

				// close booking object
				jsonOutput.append("}");
			}

			// close booking array
			jsonOutput.append("],");
		}

		// We did a thing!
		ActionLogger.logAction(ActionLogger.VIEW_BOOKING_CALENDAR, 0);

		return jsonOutput.substring(0, jsonOutput.length() == 1 ? 1 : jsonOutput.length() - 1) + "}";
	}
}
