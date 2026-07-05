package com.eoro.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDao
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.ContentVersionManager
import com.example.data.repository.ExamRepository
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
class ContentVersionAndRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var appDao: AppDao
    private lateinit var context: Context
    private lateinit var repository: ExamRepository
    private lateinit var contentVersionManager: ContentVersionManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        appDao = database.appDao()
        repository = ExamRepository(appDao, context)
        contentVersionManager = ContentVersionManager(appDao, context)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testContentVersionManagerSyncFlow() = runTest {
        // Initially no version is stored in Room
        val initialVersion = appDao.getContentVersion()
        assertNull(initialVersion)

        // Trigger sync
        val syncStatuses = mutableListOf<String>()
        contentVersionManager.checkAndSyncContent { status ->
            syncStatuses.add(status)
        }

        // Verify version updated
        val updatedVersion = appDao.getContentVersion()
        assertNotNull(updatedVersion)
        assertTrue((updatedVersion?.version ?: 0) >= 2)
        assertTrue(syncStatuses.any { it.contains("Sync completed") || it.contains("up to date") })
    }

    @Test
    fun testRepositoryCompletedAndUnlockedChapters() = runTest {
        // 1. Initial states should be empty
        var completed = repository.completedChapters.first()
        var unlocked = repository.unlockedChapters.first()
        assertTrue(completed.isEmpty())
        assertTrue(unlocked.isEmpty())

        // 2. Mark a chapter completed and unlock another
        repository.insertCompletedChapter("chapter_1_test")
        repository.unlockChapter("chapter_2_test")

        // 3. Verify changes
        completed = repository.completedChapters.first()
        unlocked = repository.unlockedChapters.first()
        assertEquals(1, completed.size)
        assertEquals("chapter_1_test", completed[0].chapterId)
        assertEquals(1, unlocked.size)
        assertEquals("chapter_2_test", unlocked[0].chapterId)

        // 4. Remove completed chapter and verify
        repository.removeCompletedChapter("chapter_1_test")
        completed = repository.completedChapters.first()
        assertTrue(completed.isEmpty())
    }

    @Test
    fun testRepositoryBookmarkManagement() = runTest {
        // 1. Start empty
        var bookmarks = repository.bookmarks.first()
        assertTrue(bookmarks.isEmpty())
        assertFalse(repository.isBookmarked("q_101"))

        // 2. Save bookmark via repository
        repository.saveBookmark(
            id = "q_101",
            type = "question",
            itemId = "101",
            titleEn = "Constitutional amendment question",
            titleHi = "संवैधानिक संशोधन प्रश्न",
            subtitleEn = "Polity Test",
            subtitleHi = "राजव्यवस्था टेस्ट"
        )

        // 3. Verify
        bookmarks = repository.bookmarks.first()
        assertEquals(1, bookmarks.size)
        assertEquals("Constitutional amendment question", bookmarks[0].titleEn)
        assertTrue(repository.isBookmarked("q_101"))

        // 4. Remove bookmark
        repository.removeBookmark("q_101")
        bookmarks = repository.bookmarks.first()
        assertTrue(bookmarks.isEmpty())
        assertFalse(repository.isBookmarked("q_101"))
    }

    @Test
    fun testRepositoryScoreStorageAndHighScore() = runTest {
        // 1. Start empty
        var allScores = repository.scores.first()
        assertTrue(allScores.isEmpty())

        val firstScore = ScoreEntity(
            id = "score_1",
            testId = "test_municipal_1",
            testNameEn = "Municipal Act Test 1",
            testNameHi = "नगरपालिका अधिनियम टेस्ट 1",
            testType = "chapter_test",
            score = 3,
            totalQuestions = 4,
            correctCount = 3,
            incorrectCount = 1,
            skippedCount = 0,
            timeTakenSec = 120,
            attemptedAt = System.currentTimeMillis()
        )

        // 2. Save score
        repository.saveTestScore(firstScore)

        // 3. Retrieve high score
        val highScore = repository.getHighScoreForTest("test_municipal_1")
        assertNotNull(highScore)
        assertEquals(3, highScore?.score)

        // 4. Save a higher score
        val secondScore = firstScore.copy(id = "score_2", score = 4, correctCount = 4, incorrectCount = 0)
        repository.saveTestScore(secondScore)

        // 5. High score should now be 4
        val updatedHighScore = repository.getHighScoreForTest("test_municipal_1")
        assertEquals(4, updatedHighScore?.score)
    }

    @Test
    fun testRepositoryFlashcardsCrud() = runTest {
        // 1. Initially empty
        var flashcards = repository.flashcards.first()
        assertTrue(flashcards.isEmpty())

        // 2. Insert custom flashcard
        repository.addCustomFlashcard(
            frontEn = "Sovereign",
            frontHi = "संप्रभु",
            backEn = "Supreme power over a body politic",
            backHi = "एक राजनीतिक निकाय पर सर्वोच्च शक्ति",
            category = "Vocab"
        )

        // 3. Verify inserted
        flashcards = repository.flashcards.first()
        assertEquals(1, flashcards.size)
        val card = flashcards[0]
        assertEquals("Sovereign", card.frontEn)
        assertFalse(card.isFavorite)

        // 4. Toggle Favorite
        repository.toggleFlashcardFavorite(card)
        flashcards = repository.flashcards.first()
        assertTrue(flashcards[0].isFavorite)

        // 5. Delete flashcard
        repository.deleteFlashcard(card.id)
        flashcards = repository.flashcards.first()
        assertTrue(flashcards.isEmpty())
    }

    @Test
    fun testRepositoryHighlights() = runTest {
        // 1. Start empty for chapter_1
        var highlights = repository.getHighlights("ch_polity_1").first()
        assertTrue(highlights.isEmpty())

        val highlight = HighlightEntity(
            id = "hl_1",
            chapterId = "ch_polity_1",
            text = "The state is sovereign.",
            colorHex = "#FFEB3B",
            noteText = "",
            pageIndex = 0,
            createdAt = System.currentTimeMillis()
        )

        // 2. Insert Highlight
        repository.insertHighlight(highlight)

        // 3. Verify
        highlights = repository.getHighlights("ch_polity_1").first()
        assertEquals(1, highlights.size)
        assertEquals("The state is sovereign.", highlights[0].text)

        // 4. Delete Highlight
        repository.deleteHighlight("hl_1")
        highlights = repository.getHighlights("ch_polity_1").first()
        assertTrue(highlights.isEmpty())
    }

    @Test
    fun testRepositoryChapterDownloads() = runTest {
        // 1. Initially not downloaded
        var isDownloaded = repository.isChapterDownloaded("ch_polity_1").first()
        assertFalse(isDownloaded)

        // 2. Download chapter
        repository.downloadChapter("ch_polity_1")

        // 3. Verify it is downloaded
        isDownloaded = repository.isChapterDownloaded("ch_polity_1").first()
        assertTrue(isDownloaded)

        // 4. Verify getting content
        val content = repository.getChapterContent("ch_polity_1")
        assertNotNull(content)
        assertEquals("ch_polity_1", content?.chapterId)

        // 5. Delete downloaded chapter
        repository.deleteDownloadedChapter("ch_polity_1")
        isDownloaded = repository.isChapterDownloaded("ch_polity_1").first()
        assertFalse(isDownloaded)
    }
}
