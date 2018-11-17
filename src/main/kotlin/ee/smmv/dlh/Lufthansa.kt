package ee.smmv.dlh

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UPPER_CAMEL_CASE
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ee.smmv.dlh.collection.ScheduleCollection
import ee.smmv.dlh.model.AccessToken
import ee.smmv.dlh.resource.FlightStatusResource
import ee.smmv.dlh.response.FlightStatusResponse
import ee.smmv.dlh.response.ScheduleResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.lang.RuntimeException
import java.net.URI
import java.net.URL
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

class Lufthansa {

	companion object {
		const val API_BASE = "https://api.lufthansa.com"

		@JvmStatic private val TYPE_ACCESS_TOKEN = object: TypeReference<AccessToken>() {}
		@JvmStatic private val TYPE_FLIGHT_STATUS_RESPONSE = object: TypeReference<FlightStatusResponse>() {}
		@JvmStatic private val TYPE_SCHEDULE_RESPONSE = object: TypeReference<ScheduleResponse>() {}

		@JvmStatic private val DATE_FORMATTER: DateTimeFormatter = ISO_LOCAL_DATE
		@JvmStatic private val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

		@JvmStatic private val LOG: Logger = LoggerFactory.getLogger(Lufthansa::class.java)
	}

	private val key: String
	private val secret: String
	private val restTemplate: RestTemplate

	private val authMapper: ObjectMapper = jacksonObjectMapper()
	private val apiMapper: ObjectMapper = jacksonObjectMapper()

	init {
		authMapper.propertyNamingStrategy = SNAKE_CASE

		apiMapper.propertyNamingStrategy = UPPER_CAMEL_CASE
		apiMapper.enable(ACCEPT_SINGLE_VALUE_AS_ARRAY)
		apiMapper.registerModule(JavaTimeModule())
	}

	constructor(key: String, secret: String) : this(
		key,
		secret,
		RestTemplate(
			mutableListOf<HttpMessageConverter<*>>(
				FormHttpMessageConverter(),
				StringHttpMessageConverter(StandardCharsets.UTF_8)
			)
		)
	)

	constructor(key: String, secret: String, restTemplate: RestTemplate) {
		restTemplate.errorHandler = LufthansaClientErrorHandler()

		this.key = key
		this.secret = secret
		this.restTemplate = restTemplate
	}

	fun flightSchedules(origin: String, destination: String, date: LocalDate): ScheduleResponse {
		LOG.trace("Lookup for schedules from $origin to $destination at $date")

		origin.validateIataCode()
		destination.validateIataCode()

		return flightSchedules(urlFor("v1/operations/schedules/$origin/$destination/${DATE_FORMATTER.format(date)}"))
	}

	fun flightSchedules(origin: String, destination: String, date: LocalDate, directFlights: Boolean): ScheduleResponse {
		LOG.trace("Lookup for schedules from $origin to $destination at $date")

		origin.validateIataCode()
		destination.validateIataCode()

		return flightSchedules(
			urlFor("v1/operations/schedules/$origin/$destination/${DATE_FORMATTER.format(date)}",
				mapOf(
					"directFlights" to directFlights.toString()
				)
			)
		)
	}

	fun flightSchedules(origin: String, destination: String, dateTime: LocalDateTime): ScheduleResponse {
		LOG.trace("Lookup for schedules from $origin to $destination at $dateTime")

		origin.validateIataCode()
		destination.validateIataCode()

		return flightSchedules(urlFor("v1/operations/schedules/$origin/$destination/${DATETIME_FORMATTER.format(dateTime)}"))
	}

	fun flightSchedules(origin: String, destination: String, dateTime: LocalDateTime, directFlights: Boolean): ScheduleResponse {
		LOG.trace("Lookup for schedules from $origin to $destination at $dateTime")

		origin.validateIataCode()
		destination.validateIataCode()

		return flightSchedules(
			urlFor("v1/operations/schedules/$origin/$destination/${DATETIME_FORMATTER.format(dateTime)}",
				mapOf(
					"directFlights" to directFlights.toString()
				)
			)
		)
	}

	private fun flightSchedules(url: URI): ScheduleResponse {
		var token = getAccessToken()

		val headers = HttpHeaders()
		headers.accept = mutableListOf(APPLICATION_JSON)
		headers.setBearerAuth(token.accessToken!!)

		val requestEntity = HttpEntity<String>(headers)
		val responseEntity = restTemplate.exchange(url, GET, requestEntity, String::class.java)

		return when (responseEntity.statusCode) {
			OK -> {
				apiMapper
					.readValue<ScheduleResponse>(responseEntity.body, TYPE_SCHEDULE_RESPONSE)
			}
			NOT_FOUND -> {
				ScheduleResponse(ScheduleCollection())
			}
			else -> {
				throw RuntimeException("Could not retrieve flight status")
			}
		}
	}

	fun flightStatusByDate(flightNumber: String, date: LocalDate): FlightStatusResource {
		LOG.trace("Lookup for flight status of $flightNumber")

		val token = getAccessToken()
		val url = urlFor("v1/operations/flightstatus/$flightNumber/${DATE_FORMATTER.format(date)}")

		val headers = HttpHeaders()
		headers.accept = mutableListOf(APPLICATION_JSON)
		headers.setBearerAuth(token.accessToken!!)

		val requestEntity = HttpEntity<String>(headers)
		val responseEntity = restTemplate.exchange(url, GET, requestEntity, String::class.java)

		return when (responseEntity.statusCode) {
			OK -> {
				apiMapper
					.readValue<FlightStatusResponse>(responseEntity.body, TYPE_FLIGHT_STATUS_RESPONSE)
					.flightStatusResource!!
			}
			NOT_FOUND -> {
				FlightStatusResource()
			}
			else -> {
				throw RuntimeException("Could not retrieve flight status")
			}
		}
	}

	protected fun getAccessToken(): AccessToken {
		LOG.trace("Requesting access token from API")

		val url = urlFor("v1/oauth/token")
		val headers = HttpHeaders()
		headers.contentType = APPLICATION_FORM_URLENCODED
		val keys: MultiValueMap<String, String> = with (LinkedMultiValueMap<String, String>()) {
			setAll(kotlin.collections.mapOf(
				"client_id" to key,
				"client_secret" to secret,
				"grant_type" to "client_credentials"
			))

			this
		}

		val requestEntity = HttpEntity(keys, headers)
		val responseEntity = restTemplate.exchange(url, POST, requestEntity, String::class.java)

		return when (responseEntity.statusCode) {
			OK -> {
				authMapper.readValue(responseEntity.body, TYPE_ACCESS_TOKEN)
			}
			else -> {
				throw RuntimeException("Could not retrieve access token to access Lufthansa Public API")
			}
		}
	}

	private fun urlFor(localPath: String): URI = URL("$API_BASE/$localPath").toURI()

	private fun urlFor(localPath: String, params: Map<String, String>): URI =
		with (UriComponentsBuilder.fromHttpUrl("$API_BASE/$localPath")) {
			params.forEach { name, value -> queryParam(name, value) }

			build().encode().toUri()
		}

}
