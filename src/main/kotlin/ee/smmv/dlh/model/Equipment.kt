package ee.smmv.dlh.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Equipment(
	var aircraftCode: String? = null,
	@get:JsonProperty("OnBoardEquipment") var onBoardEquipment: OnBoardEquipment? = null
)
