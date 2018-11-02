package ee.smmv.dlh.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Departure or arrival of flight
 */
data class FlightStopTime(
	var airportCode: String? = null,
	var scheduledTimeLocal: ScheduledLocalDateTime? = null,
	@get:JsonProperty("ScheduledTimeUTC") var scheduledTimeUtc: ScheduledGlobalDateTime? = null,
	var actualTimeLocal: ScheduledLocalDateTime? = null,
	@get:JsonProperty("ActualTimeUTC") var actualTimeUtc: ScheduledGlobalDateTime? = null,
	var timeStatus: StatusCode? = null,
	var terminal: Terminal? = null
)
