@file:JvmName("DLHLocalDate")
package ee.smmv.dlh.extension

import java.time.LocalDate
import kotlin.math.abs

fun LocalDate.validateDateInRange(from: Int, to: Int) {
	val fromDate = LocalDate.now().minusDays(abs(from.toLong()))
	val toDate = LocalDate.now().plusDays(to.toLong())

	if (fromDate.isAfter(this)) throw IllegalArgumentException("Given date is more than ${abs(from)} days in past")
	if (toDate.isBefore(this)) throw IllegalArgumentException("Given date is more than ${abs(to)} days in future")
}
