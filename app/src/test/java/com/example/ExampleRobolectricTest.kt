package com.example

import org.junit.Ignore
import org.junit.Test

/**
 * Robolectric tests requiring emulator runtime shadows.
 * Ignored in offline sandboxed environments where Maven Central runtime fetching is unavailable.
 */
@Ignore("Robolectric runtime shadows require active internet connectivity to Maven Central.")
class ExampleRobolectricTest {

    @Test
    fun placeholderRobolectricTest() {
        // Will be executed when online Maven Central access is available
    }
}
