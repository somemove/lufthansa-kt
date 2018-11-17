package ee.smmv.dlh;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("String Extension for Lufthansa Public API Client tests")
public class DLHStringTests {

	@Test
	public void validateIataCodeTests() {
		assertThrows(IllegalArgumentException.class, () -> DLHString.validateIataCode("a"));

		assertThrows(IllegalArgumentException.class, () -> DLHString.validateIataCode("LKPR"));

		try {
			DLHString.validateIataCode("FRA");
		} catch (IllegalArgumentException e) {
			throw new Error("FRA is a valid IATA airport code");
		}
	}

}
