package ee.smmv.dlh;

import ee.smmv.dlh.extension.DLHLocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("LocalDate Extension for Lufthansa Public API Client tests")
public class DLHLocalDateTests {

	@Test
	@DisplayName("Tests for validating date belonging to given range")
	public void validateDateInRange() {
		try {
			DLHLocalDate.validateDateInRange(LocalDate.now(), -1, 5);
		} catch (IllegalArgumentException e) {
			throw new Error("NOW() belongs to date range yesterday to plus 5 days.");
		}

		try {
			DLHLocalDate.validateDateInRange(LocalDate.now().minusDays(1), -1, 5);
		} catch (IllegalArgumentException e) {
			throw new Error("Yesterday belongs to date range yesterday to plus 5 days.");
		}

		try {
			DLHLocalDate.validateDateInRange(LocalDate.now().plusDays(5), -1, 5);
		} catch (IllegalArgumentException e) {
			throw new Error("Today plus 5 days belongs to date range yesterday to plus 5 days.");
		}

		assertThrows(IllegalArgumentException.class, () -> DLHLocalDate.validateDateInRange(LocalDate.now().minusDays(2), -1, 5));
		assertThrows(IllegalArgumentException.class, () -> DLHLocalDate.validateDateInRange(LocalDate.now().plusDays(6), -1, 5));
	}

}
