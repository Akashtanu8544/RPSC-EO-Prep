package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDao
import com.example.data.local.AppDatabase
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.Serializable

data class TestInfo(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val questionCount: Int,
    val totalMarks: Int,
    val durationMins: Int,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val testType: String, // "chapter_test" or "mock_test"
    val isFree: Boolean
) : Serializable

class ExamRepository(
    private val appDao: AppDao,
    private val context: Context
) {

    // Seed data into Room database on first initialization
    suspend fun seedInitialDataIfNeeded() {
        withContext(Dispatchers.IO) {
            // Check if flashcards are empty, and seed them
            val existing = appDao.getAllFlashcards().first()
            if (existing.isEmpty()) {
                appDao.insertFlashcards(CuratedExamData.flashcards)
            }
        }
    }

    // Static Book / Syllabus Accessors
    fun getBooks(): List<Book> = CuratedExamData.books

    fun getChapters(bookId: String): List<Chapter> =
        CuratedExamData.chapters.filter { it.bookId == bookId }

    suspend fun getChapterContent(chapterId: String): ChapterContent? {
        return withContext(Dispatchers.IO) {
            // 1. Check local download cache
            val downloaded = appDao.getDownloadedChapter(chapterId)
            if (downloaded != null) {
                // Parse content from stored JSON
                try {
                    val json = JSONObject(downloaded.contentJson)
                    val enArray = json.getJSONArray("pagesEn")
                    val hiArray = json.getJSONArray("pagesHi")
                    val enList = List(enArray.length()) { enArray.getString(it) }
                    val hiList = List(hiArray.length()) { hiArray.getString(it) }
                    return@withContext ChapterContent(chapterId, enList, hiList)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            // 2. Fallback to bundled curated content
            CuratedExamData.chapterContents[chapterId]
        }
    }

    // Download a chapter to local database for 100% offline access
    suspend fun downloadChapter(chapterId: String) {
        withContext(Dispatchers.IO) {
            val chapter = CuratedExamData.chapters.find { it.id == chapterId } ?: return@withContext
            val content = CuratedExamData.chapterContents[chapterId] ?: return@withContext
            
            // Serialize content to JSON
            val json = JSONObject()
            val enArray = org.json.JSONArray(content.pagesEn)
            val hiArray = org.json.JSONArray(content.pagesHi)
            json.put("pagesEn", enArray)
            json.put("pagesHi", hiArray)

            val entity = DownloadedChapterEntity(
                chapterId = chapterId,
                bookId = chapter.bookId,
                titleEn = chapter.titleEn,
                titleHi = chapter.titleHi,
                contentJson = json.toString()
            )
            appDao.insertDownloadedChapter(entity)
        }
    }

    suspend fun deleteDownloadedChapter(chapterId: String) {
        withContext(Dispatchers.IO) {
            appDao.deleteDownloadedChapter(chapterId)
        }
    }

    fun isChapterDownloaded(chapterId: String): Flow<Boolean> =
        appDao.isChapterDownloaded(chapterId)

    // Tests (Chapter / Mock) Accessors
    fun getTests(): List<TestInfo> = listOf(
        TestInfo("test_polity_1", "Polity Chapter Test 1", "राजव्यवस्था अध्याय परीक्षण 1", 2, 4, 5, "Easy", "chapter_test", true),
        TestInfo("test_municipal_1", "Municipal Act Core Test 1", "नगरपालिका अधिनियम मुख्य परीक्षण 1", 2, 4, 5, "Medium", "chapter_test", true),
        TestInfo("test_schemes_1", "Welfare Schemes & Rules Test", "कल्याणकारी योजनाएं एवं नियम परीक्षण", 2, 4, 5, "Medium", "chapter_test", true),
        TestInfo("test_mock_1", "Full Syllabus Mock Test 1", "पूर्ण पाठ्यक्रम मॉक टेस्ट 1", 6, 12, 10, "Hard", "mock_test", false),
        TestInfo("test_mock_2", "Full Syllabus Mock Test 2", "पूर्ण पाठ्यक्रम मॉक टेस्ट 2", 6, 12, 10, "Hard", "mock_test", false),
        TestInfo("test_mock_3", "Premium Mock Test 3", "प्रीमियम मॉक टेस्ट 3", 6, 12, 10, "Hard", "mock_test", false)
    )

    fun getQuestionsForTest(testId: String): List<Question> {
        return if (testId.startsWith("test_mock_")) {
            // For mock test, bundle all curated questions
            CuratedExamData.questions.mapIndexed { idx, q -> q.copy(id = "mock_${idx}", testId = testId) }
        } else {
            CuratedExamData.questions.filter { it.testId == testId }
        }
    }

    // Room DB Flow accessors for ViewModel

    val completedChapters: Flow<List<CompletedChapterEntity>> = appDao.getAllCompletedChapters()
    val unlockedChapters: Flow<List<UnlockedChapterEntity>> = appDao.getAllUnlockedChapters()
    val unlockedTests: Flow<List<UnlockedTestEntity>> = appDao.getAllUnlockedTests()
    val bookmarks: Flow<List<BookmarkEntity>> = appDao.getAllBookmarks()
    val flashcards: Flow<List<FlashcardEntity>> = appDao.getAllFlashcards()
    val scores: Flow<List<ScoreEntity>> = appDao.getAllScores()

    suspend fun insertCompletedChapter(chapterId: String) {
        appDao.insertCompletedChapter(CompletedChapterEntity(chapterId))
    }

    suspend fun removeCompletedChapter(chapterId: String) {
        appDao.removeCompletedChapter(chapterId)
    }

    suspend fun unlockChapter(chapterId: String) {
        appDao.insertUnlockedChapter(UnlockedChapterEntity(chapterId))
    }

    suspend fun unlockTest(testId: String) {
        appDao.insertUnlockedTest(UnlockedTestEntity(testId))
    }

    suspend fun saveBookmark(id: String, type: String, itemId: String, titleEn: String, titleHi: String, subtitleEn: String, subtitleHi: String) {
        appDao.insertBookmark(
            BookmarkEntity(
                id = id,
                itemType = type,
                itemId = itemId,
                titleEn = titleEn,
                titleHi = titleHi,
                subtitleEn = subtitleEn,
                subtitleHi = subtitleHi
            )
        )
    }

    suspend fun removeBookmark(id: String) {
        appDao.deleteBookmarkById(id)
    }

    suspend fun isBookmarked(id: String): Boolean = appDao.isBookmarked(id)

    fun getHighlights(chapterId: String): Flow<List<HighlightEntity>> = appDao.getHighlightsForChapter(chapterId)

    suspend fun insertHighlight(highlight: HighlightEntity) {
        appDao.insertHighlight(highlight)
    }

    suspend fun deleteHighlight(id: String) {
        appDao.deleteHighlightById(id)
    }

    suspend fun saveTestScore(score: ScoreEntity) {
        appDao.insertScore(score)
    }

    suspend fun getHighScoreForTest(testId: String): ScoreEntity? {
        return appDao.getHighScoreForTest(testId)
    }

    suspend fun addCustomFlashcard(frontEn: String, frontHi: String, backEn: String, backHi: String, category: String) {
        appDao.insertFlashcard(
            FlashcardEntity(
                id = "custom_${System.currentTimeMillis()}",
                frontEn = frontEn,
                frontHi = frontHi,
                backEn = backEn,
                backHi = backHi,
                category = category,
                isDaily = false
            )
        )
    }

    suspend fun deleteFlashcard(id: String) {
        appDao.deleteFlashcardById(id)
    }

    suspend fun toggleFlashcardFavorite(flashcard: FlashcardEntity) {
        appDao.insertFlashcard(flashcard.copy(isFavorite = !flashcard.isFavorite))
    }

    suspend fun clearExpiredFlashcards() {
        appDao.clearExpiredDailyFlashcards(System.currentTimeMillis())
    }
}
