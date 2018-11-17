package ee.smmv.dlh.response

import ee.smmv.dlh.collection.ScheduleCollection

data class ScheduleResponse(
	var scheduleResource: ScheduleCollection? = null
)
