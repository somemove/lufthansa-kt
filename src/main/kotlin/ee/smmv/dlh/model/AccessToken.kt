package ee.smmv.dlh.model

data class AccessToken(
	var accessToken: String? = null,
	var tokenType: String? = null,
	var expiresIn: Int? = null
)
