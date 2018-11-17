package ee.smmv.dlh.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Schedule(
	@get:JsonProperty("TotalJourney") var totalJourney: Duration? = null,
	@get:JsonProperty("Flight") var flights: List<Flight> = listOf()
)
