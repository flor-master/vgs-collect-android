package com.verygoodsecurity.vgscollect

import com.verygoodsecurity.vgscollect.core.*
import com.verygoodsecurity.vgscollect.core.storage.VgsStore
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import java.util.regex.Pattern

class VGSCollectTest {

    @Test
    fun testEnvironmentByDefault() {
        val c = VGSCollect("abc")
        assertTrue(c.baseURL.contains("sandbox"))
    }

    @Test
    fun testEnvironmentSandboxByDefault() {
        val c = VGSCollect("abc", Environment.SANDBOX)
        assertTrue(c.baseURL.contains("sandbox"))
    }

    @Test
    fun testEnvironmentLiveByDefault() {
        val c = VGSCollect("abc", Environment.LIVE)
        assertTrue(c.baseURL.contains("live"))
    }

    @Test
    fun testUrl() {
        val regex = "^(https:\\/\\/)+[a-zA-Z0-9 ,]+[.]+[live, sandbox]+[.](verygoodproxy.com)\$"
        val c = VGSCollect("abc")

        assertTrue(Pattern.compile(regex).matcher(c.baseURL).matches())
    }

    @Test
    fun onDestroyTest() {
        val store = Mockito.mock(VgsStore::class.java)

        val collect = VGSCollect("s")
        collect.setStorage(store)

        collect.onDestroy()
        Mockito.verify(store).clear()
    }


    @Test
    fun getAllStates() {
        val store = Mockito.mock(VgsStore::class.java)

        val collect = VGSCollect("s")
        collect.setStorage(store)

        collect.getAllStates()

        Mockito.verify(store).getStates()
    }
}