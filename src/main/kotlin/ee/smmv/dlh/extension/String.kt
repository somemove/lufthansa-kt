@file:JvmName("DLHString")
package ee.smmv.dlh

fun String.validateIataCode() {
	if (this.length != 3) {
		throw IllegalArgumentException("Given value $this is not an IATA airport code")
	}
}
