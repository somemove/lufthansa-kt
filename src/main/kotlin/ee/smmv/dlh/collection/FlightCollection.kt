package ee.smmv.dlh.collection

import com.fasterxml.jackson.annotation.JsonProperty
import ee.smmv.dlh.model.Flight

data class FlightCollection(
	@get:JsonProperty("Flight") var flights: List<Flight> = listOf()
)
