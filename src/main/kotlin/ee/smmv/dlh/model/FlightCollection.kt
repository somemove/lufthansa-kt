package ee.smmv.dlh.model

import com.fasterxml.jackson.annotation.JsonProperty

data class FlightCollection(
	@get:JsonProperty("Flight") var flights: List<Flight> = listOf()
)
