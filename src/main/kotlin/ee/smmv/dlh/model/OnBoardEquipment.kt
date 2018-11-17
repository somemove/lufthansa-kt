package ee.smmv.dlh.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OnBoardEquipment(
	var inflightEntertainment: Boolean = false,
	@get:JsonProperty("Compartment") var compartments: List<Compartment> = listOf()
)
