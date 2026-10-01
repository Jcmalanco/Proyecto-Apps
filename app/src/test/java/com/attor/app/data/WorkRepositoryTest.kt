package com.attor.app.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
class WorkRepositoryTest {

    private lateinit var context: Context
    private lateinit var repository: WorkRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = WorkRepository(context)
    }

    @Test
    fun seedIfEmpty_emptyDatabase_insertsSampleData() = runBlocking {
        repository.seedIfEmpty()
        val works = repository.getAll()
        assertTrue(works.isNotEmpty())
        assertEquals(12, works.size)
    }

    @Test
    fun seedIfEmpty_alreadySeeded_doesNotDuplicate() = runBlocking {
        repository.seedIfEmpty()
        val firstCount = repository.getAll().size
        repository.seedIfEmpty()
        val secondCount = repository.getAll().size
        assertEquals(firstCount, secondCount)
    }

    @Test
    fun getAll_returnsAllWorks() = runBlocking {
        repository.seedIfEmpty()
        val works = repository.getAll()
        assertEquals(12, works.size)
    }

    @Test
    fun getById_existingId_returnsWork() = runBlocking {
        repository.seedIfEmpty()
        val work = repository.getById("w1")
        assertNotNull(work)
        assertEquals("El Herrero de Almas", work?.title)
    }

    @Test
    fun getById_nonExistentId_returnsNull() = runBlocking {
        repository.seedIfEmpty()
        val work = repository.getById("nonexistent")
        assertNull(work)
    }

    @Test
    fun publish_newWork_insertsSuccessfully() = runBlocking {
        repository.seedIfEmpty()
        val newWork = Work(
            id = "w99",
            title = "Test Work",
            author = "Test Author",
            rating = 4.5f,
            format = WorkFormat.NOVEL
        )
        repository.publish(newWork)
        val retrieved = repository.getById("w99")
        assertNotNull(retrieved)
        assertEquals("Test Work", retrieved?.title)
    }

    @Test
    fun getAll_containsCorrectFormats() = runBlocking {
        repository.seedIfEmpty()
        val works = repository.getAll()
        val formats = works.map { it.format }.toSet()
        assertTrue(formats.contains(WorkFormat.NOVEL))
        assertTrue(formats.contains(WorkFormat.MANGA))
        assertTrue(formats.contains(WorkFormat.COMIC))
    }
}
