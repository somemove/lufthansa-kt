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

```java
String key = "%API_KEY%";
String secret = "%API_SECRET%";

Lufthansa lh = new Lufthansa(key, secret);

lh.flightStatusByDate("LH454", LocalDate.now());

lh.flightSchedules("FRA", "NYC", LocalDate.now());
lh.flightSchedules("FRA", "NYC", LocalDate.now(), true);
lh.flightSchedules("FRA", "NYC", LocalDateTime.now());
lh.flightSchedules("FRA", "NYC", LocalDateTime.now(), true);
```

##  Testing the SDK

To run the unit tests locally, a key and secret must be obtained from Lufthansa Group Developers website. When available, create a file `api-credentials.properties` at `src/test/resources/private` folder with given content:

```
key=YOUR_API_KEY
secret=YOUR_API_SECRET
```

**The file is required to run unit test case of SDK.** And it is ignored by Git.
