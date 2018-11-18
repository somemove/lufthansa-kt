package ee.smmv.dlh;

import ee.smmv.dlh.model.AccessToken;

import ee.smmv.dlh.resource.FlightStatusResource;
import ee.smmv.dlh.response.ScheduleResponse;
import ee.smmv.dlh.util.ApiCredentialsCallback;
import ee.smmv.dlh.util.ApiCredentialsResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.util.StringUtils.hasText;

@DisplayName("Lufthansa Public API Client tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ApiCredentialsCallback.class)
@ExtendWith(ApiCredentialsResolver.class)
public class LufthansaLiveApiTests {

	private static Logger LOG = LoggerFactory.getLogger(LufthansaLiveApiTests.class);

	private Lufthansa client;

	@BeforeAll
	public  void setupClient(String key, String secret) {
		LOG.trace("Creating a Lufthansa client");

		this.client = new Lufthansa(key, secret);
	}

	@Test
	@DisplayName("Test for obtaining access token into Public API")
	public void testGetAccessToken() {
		AccessToken token = this.client.getAccessToken();

		assertNotNull("Token not received", token);
		assertTrue("Access token not read", hasText(token.getAccessToken()));
		assertTrue("Access token expiry period not read", token.getExpiresIn() > 0);
	}

	@Test
	@DisplayName("Test for obtaining of flight status at given date")
	public void testGetFlightStatusByDate() {
		FlightStatusResource fsr = this.client.flightStatusByDate("LH1403", getTomorrow());

		assertNotNull(fsr);
		assertEquals(1, fsr.getFlights().size());
	}

	@Test
	@DisplayName("Test for obtaining flight schedules for given route at given date")
	public void testGetScheduleForDate() {
		ScheduleResponse sr = this.client.flightSchedules("PRG", "NYC", getTomorrow());

		assertNotNull("No response for schedules received", sr);
		assertTrue("No schedules found for PRG-NYC", sr.getScheduleResource().getSchedules().size() > 0);

		// fails if there is something missing
		Period firstSegmentValidity = sr.getScheduleResource().getSchedules().get(0).getFlights().get(0).getDetails().getDatePeriod().toPeriod();

		sr = this.client.flightSchedules("PRG", "NYC", getTomorrow(), true);

		assertNotNull("No response for schedules received", sr);
		assertTrue(sr.getScheduleResource().getSchedules().size() == 0);

		sr = this.client.flightSchedules("PRG", "NYC", LocalDateTime.now());

		assertNotNull("No response for schedules received", sr);
		assertTrue("No schedules found for PRG-NYC", sr.getScheduleResource().getSchedules().size() > 0);

		// fails if there is something missing
		firstSegmentValidity = sr.getScheduleResource().getSchedules().get(0).getFlights().get(0).getDetails().getDatePeriod().toPeriod();

		sr = this.client.flightSchedules("PRG", "NYC", LocalDateTime.now(), true);

		assertNotNull("No response for schedules received", sr);
		assertTrue(sr.getScheduleResource().getSchedules().size() == 0);
	}

	@Test
	@DisplayName("Test for schedule search input checks")
	public void testGetScheduleForDateChecks() {
		assertThrows(IllegalArgumentException.class, () -> this.client.flightSchedules("LKPR", "FRA", LocalDate.now()));

		assertThrows(IllegalArgumentException.class, () -> this.client.flightSchedules("PRG", "F", LocalDate.now()));

		assertThrows(IllegalArgumentException.class, () -> this.client.flightSchedules("LKPR", "FRA", LocalDate.now(), true));

		assertThrows(IllegalArgumentException.class, () -> this.client.flightSchedules("PRG", "F", LocalDate.now(), true));

		assertThrows(IllegalArgumentException.class, () -> this.client.flightSchedules("LKPR", "FRA", LocalDateTime.now()));

		assertThrows(IllegalArgumentException.class, () -> this.client.flightSchedules("PRG", "F", LocalDateTime.now()));

		assertThrows(IllegalArgumentException.class, () -> this.client.flightSchedules("LKPR", "FRA", LocalDateTime.now(), true));

		assertThrows(IllegalArgumentException.class, () -> this.client.flightSchedules("PRG", "F", LocalDateTime.now(), true));
	}

	private static LocalDate getTomorrow() {
		LocalDate ld = LocalDate.now();
		ld = ld.plusDays(1);
		return ld;
	}

}
