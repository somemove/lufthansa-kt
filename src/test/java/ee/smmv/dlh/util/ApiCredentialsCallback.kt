package ee.smmv.dlh.util

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Properties

class ApiCredentialsCallback : BeforeAllCallback {

	override fun beforeAll(context: ExtensionContext) {
		LOG.trace("Loading API credentials")
		val properties = Properties()
		properties.load(javaClass.classLoader.getResourceAsStream("private/api-credentials.properties"))

		LOG.trace("Loaded properties: ${properties.entries.joinToString(";") { "${it.key},${it.value}" }}")

		val store = context.getStore(NAMESPACE)
		store.put(KEY, properties[KEY])
		store.put(SECRET, properties[SECRET])
	}

	companion object {
		const val KEY = "key"
		const val SECRET = "secret"

		@JvmStatic val NAMESPACE: ExtensionContext.Namespace
			get() = ExtensionContext.Namespace.create(ApiCredentialsCallback::class.java)

		@JvmStatic private val LOG: Logger = LoggerFactory.getLogger(ApiCredentialsCallback::class.java)
	}

}
