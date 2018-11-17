package ee.smmv.dlh.response

import ee.smmv.dlh.resource.FlightStatusResource

data class FlightStatusResponse(
	var flightStatusResource: FlightStatusResource? = null
)
