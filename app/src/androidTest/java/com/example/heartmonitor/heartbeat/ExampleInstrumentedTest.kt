package com.example.heartmonitor.heartbeat

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class) // Use fully qualified name
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.heartmonitor.heartbeat", appContext.packageName)
    }
}
