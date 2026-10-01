package com.attor.app.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SessionManagerTest {

    private lateinit var context: Context
    private lateinit var sessionManager: SessionManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        sessionManager = SessionManager(context)
        sessionManager.logout()
    }

    @Test
    fun isGuest_defaultState_returnsTrue() {
        assertTrue(sessionManager.isGuest())
    }

    @Test
    fun isLoggedIn_defaultState_returnsFalse() {
        assertFalse(sessionManager.isLoggedIn())
    }

    @Test
    fun getUserName_defaultState_returnsInvitado() {
        assertEquals("Invitado", sessionManager.getUserName())
    }

    @Test
    fun getUserEmail_defaultState_returnsEmpty() {
        assertEquals("", sessionManager.getUserEmail())
    }

    @Test
    fun login_setsUserLoggedIn() {
        sessionManager.login("Test User", "test@example.com")
        assertFalse(sessionManager.isGuest())
        assertTrue(sessionManager.isLoggedIn())
    }

    @Test
    fun login_setsUserName() {
        sessionManager.login("Test User", "test@example.com")
        assertEquals("Test User", sessionManager.getUserName())
    }

    @Test
    fun login_setsUserEmail() {
        sessionManager.login("Test User", "test@example.com")
        assertEquals("test@example.com", sessionManager.getUserEmail())
    }

    @Test
    fun loginAsGuest_setsGuestState() {
        sessionManager.login("Test User", "test@example.com")
        sessionManager.loginAsGuest()
        assertTrue(sessionManager.isGuest())
        assertFalse(sessionManager.isLoggedIn())
    }

    @Test
    fun logout_clearsSession() {
        sessionManager.login("Test User", "test@example.com")
        sessionManager.logout()
        assertTrue(sessionManager.isGuest())
        assertFalse(sessionManager.isLoggedIn())
    }

    @Test
    fun logout_resetsUserName() {
        sessionManager.login("Test User", "test@example.com")
        sessionManager.logout()
        assertEquals("Invitado", sessionManager.getUserName())
    }
}
