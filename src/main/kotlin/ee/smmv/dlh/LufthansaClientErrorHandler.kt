package ee.smmv.dlh

import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler

class LufthansaClientErrorHandler : DefaultResponseErrorHandler() {

	override fun hasError(response: ClientHttpResponse): Boolean {
		return if (response.statusCode == HttpStatus.NOT_FOUND) {
			false
		} else {
			super.hasError(response)
		}
	}

}
