package ee.smmv.dlh.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Duration(
	@get:JsonProperty("Duration") var value: String? = null
)
