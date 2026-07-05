package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDao
import com.example.data.local.AppDatabase
import com.example.data.model.BookmarkEntity
import com.example.data.model.ContentVersionEntity
import com.example.data.repository.ContentVersionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AppDatabaseAndSyncTest {

    private lateinit var database: AppDatabase
    private lateinit var appDao: AppDao
    private lateinit var context: Context
    private lateinit var contentVersionManager: ContentVersionManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        // Initialize an in-memory version of the database for fast hermetic testing
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        appDao = database.appDao()
        contentVersionManager = ContentVersionManager(appDao, context)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testRoomDatabaseBookmarksCrud() = runTest {
        // 1. Initial list should be empty
        var bookmarks = appDao.getAllBookmarks().first()
        assertTrue(bookmarks.isEmpty())

        // 2. Insert a bookmark
        val bookmark = BookmarkEntity(
            id = "test_chapter_bookmark",
            itemType = "chapter",
            itemId = "chapter_1",
            titleEn = "Chapter 1: Duties",
            titleHi = "अध्याय 1: कर्तव्य",
            subtitleEn = "Executive Officer Syllabus",
            subtitleHi = "अधिशाषी अधिकारी पाठ्यक्रम",
            savedAt = System.currentTimeMillis()
        )
        appDao.insertBookmark(bookmark)

        // 3. Verify it is inserted and check bookmark status
        bookmarks = appDao.getAllBookmarks().first()
        assertEquals(1, bookmarks.size)
        assertEquals("Chapter 1: Duties", bookmarks[0].titleEn)
        assertTrue(appDao.isBookmarked("test_chapter_bookmark"))

        // 4. Delete bookmark and verify it's gone
        appDao.deleteBookmarkById("test_chapter_bookmark")
        bookmarks = appDao.getAllBookmarks().first()
        assertTrue(bookmarks.isEmpty())
        assertFalse(appDao.isBookmarked("test_chapter_bookmark"))
    }

    @Test
    fun testContentVersionCrud() = runTest {
        // 1. Initially, no content version should exist
        var versionEntity = appDao.getContentVersion()
        assertNull(versionEntity)

        // 2. Insert a content version
        val entity = ContentVersionEntity(
            version = 3,
            lastChecked = System.currentTimeMillis()
        )
        appDao.insertContentVersion(entity)

        // 3. Retrieve and verify
        versionEntity = appDao.getContentVersion()
        assertNotNull(versionEntity)
        assertEquals(3, versionEntity?.version)
    }

    @Test
    fun testContentVersionManagerSyncFlow() = runTest {
        // 1. Initial local version is null (defaults to 0 inside manager)
        assertNull(appDao.getContentVersion())

        // 2. Trigger check and sync content.
        // It will either successfully download the manifest or trigger the simulated fallback (v2),
        // download files/mock files, and update the database version.
        val syncStatuses = mutableListOf<String>()
        contentVersionManager.checkAndSyncContent { status ->
            syncStatuses.add(status)
        }

        // 3. Verify that sync completed and database was updated
        val updatedVersion = appDao.getContentVersion()
        assertNotNull("The content version should have been saved to the database", updatedVersion)
        
        // Either v2 (simulated) or higher version (real URL if network succeeds)
        assertTrue("Saved content version should be >= 2", (updatedVersion?.version ?: 0) >= 2)
        
        // Check that statuses were reported correctly
        assertTrue(syncStatuses.any { it.contains("Sync completed") || it.contains("up to date") })
    }
}
