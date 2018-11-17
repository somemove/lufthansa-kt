package ee.smmv.dlh.model

import java.time.LocalDate
import java.time.Period

data class DatePeriod(
	var effective: LocalDate? = null,
	var expiration: LocalDate? = null
) {

	/**
	 * Converts to JSR-310 {@link Period} object.
	 *
	 * @return Period between effective and expiration date
	 */
	fun toPeriod(): Period = if (effective == null || expiration == null) {
		throw IllegalStateException("Effective or expiration date is not provided")
	} else {
		Period.between(effective, expiration)
	}

}
