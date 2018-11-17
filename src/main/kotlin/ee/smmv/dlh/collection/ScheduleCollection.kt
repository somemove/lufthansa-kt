package ee.smmv.dlh.collection

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import ee.smmv.dlh.model.Schedule

@JsonIgnoreProperties(*["Meta"])
data class ScheduleCollection(
	@get:JsonProperty("Schedule") var schedules: List<Schedule> = listOf()
)
