package ee.smmv.dlh.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ee.smmv.dlh.collection.FlightCollection

@JsonIgnoreProperties(*["Meta"])
data class FlightStatusResource(
	var flights: List<FlightCollection> = listOf()
)
