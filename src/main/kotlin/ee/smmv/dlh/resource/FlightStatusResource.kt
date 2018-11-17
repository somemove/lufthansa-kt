package ee.smmv.dlh.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ee.smmv.dlh.model.FlightCollection

@JsonIgnoreProperties(*["Meta"])
data class FlightStatusResource(
	var flights: List<FlightCollection> = listOf()
)
