package com.attor.app.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UserRepositoryTest {

    private lateinit var context: Context
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = UserRepository(context)
    }

    @Test
    fun register_newUser_returnsTrue() = runBlocking {
        val result = repository.register("Test User", "test@example.com", "password123")
        assertTrue(result)
    }

    @Test
    fun register_duplicateEmail_returnsFalse() = runBlocking {
        repository.register("Test User", "test@example.com", "password123")
        val result = repository.register("Another User", "test@example.com", "different")
        assertFalse(result)
    }

    @Test
    fun login_correctCredentials_returnsUser() = runBlocking {
        repository.register("Test User", "test@example.com", "password123")
        val user = repository.login("test@example.com", "password123")
        assertNotNull(user)
        assertEquals("Test User", user?.name)
        assertEquals("test@example.com", user?.email)
    }

    @Test
    fun login_wrongPassword_returnsNull() = runBlocking {
        repository.register("Test User", "test@example.com", "password123")
        val user = repository.login("test@example.com", "wrongpassword")
        assertNull(user)
    }

    @Test
    fun login_nonExistentEmail_returnsNull() = runBlocking {
        val user = repository.login("nonexistent@example.com", "password123")
        assertNull(user)
    }

    @Test
    fun register_passwordIsHashed() = runBlocking {
        repository.register("Test User", "test@example.com", "password123")
        val user = repository.login("test@example.com", "password123")
        assertNotNull(user)
        assertFalse(user?.passwordHash?.contains("password123") ?: true)
        assertEquals(64, user?.passwordHash?.length)
    }
}
