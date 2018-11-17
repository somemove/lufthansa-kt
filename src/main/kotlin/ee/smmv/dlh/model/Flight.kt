package ee.smmv.dlh.model

data class Flight(
	var departure: FlightStopTime? = null,
	var arrival: FlightStopTime? = null,
	var marketingCarrier: Carrier? = null,
	var operatingCarrier: Carrier? = null,
	var equipment: Equipment? = null,
	var details: Details? = null,
	var flightStatus: StatusCode? = null,
	var serviceType: String? = null
)
