package ee.smmv.dlh.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Carrier(
	@get:JsonProperty("AirlineID") var airlineId: String? = null,
	var flightNumber: Int? = null
)
