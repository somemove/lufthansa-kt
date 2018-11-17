# Lufthansa.kt

[![Release](https://jitpack.io/v/somemove/lufthansa-kt.svg?style=flat-square)](https://jitpack.io/#somemove/lufthansa-kt)

Lufthansa Kotlin (Java) SDK based on [Public API](https://developer.lufthansa.com/docs).

**STATUS: INCOMPLETE**

## Install

```groovy
// Define the dependency version
buildscript {
	ext {
		lhVersion = 'THE_VERSION'
	}
}
// Add the Jitpack repository
repositories {
	maven { url 'https://jitpack.io' }
}
// Add the dependency
dependencies {
	compile "com.github.somemove:lufthansa-kt:$lhVersion"
}
```

## Example

```
String key = "%API_KEY%";
String secret = "%API_SECRET%";

Lufthansa lh = new Lufthansa(key, secret);

lh.flightStatusByDate("LH454", LocalDate.now());

lh.flightSchedules("FRA", "NYC", LocalDate.now());
lh.flightSchedules("FRA", "NYC", LocalDate.now(), true);
lh.flightSchedules("FRA", "NYC", LocalDateTime.now());
lh.flightSchedules("FRA", "NYC", LocalDateTime.now(), true);
```
