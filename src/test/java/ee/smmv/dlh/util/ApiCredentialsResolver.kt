package ee.smmv.dlh.util

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * JUnit 5 parameters resolve that handles <code>setupClient</code> method
 * only, and expets it to have two String arguments - key, and secret.
 *
 * Data to populate those arguments are obtained from extension context.
 * {@link ApiCredentialsCallback} loads those keys from properties file
 * into context store.
 */
class ApiCredentialsResolver : ParameterResolver {

	var argumentMapping: Map<String, String> = mapOf(
		"arg0" to ApiCredentialsCallback.KEY,
		"arg1" to ApiCredentialsCallback.SECRET
	)

	override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
		LOG.trace("Resolving parameter ${parameterContext.parameter.name}")

		val store = extensionContext.getStore(ApiCredentialsCallback.NAMESPACE)
		return store[argumentMapping[parameterContext.parameter.name]]
	}

	override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
		LOG.trace("Checking support for parameter ${parameterContext.parameter.name} of method ${parameterContext.declaringExecutable.name}")

		return parameterContext.declaringExecutable.name == "setupClient"
	}

	companion object {
		@JvmStatic private val LOG: Logger = LoggerFactory.getLogger(ApiCredentialsResolver::class.java)
	}

}
