package ee.smmv.dlh.model

data class Compartment(
	var classCode: Char? = null,
	var classDesc: String? = null,
	var flyNet: Boolean = false,
	var seatPower: Boolean = false,
	var usb: Boolean = false,
	var liveTv: Boolean = false
)
