package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // Completed Chapters
    @Query("SELECT * FROM completed_chapters")
    fun getAllCompletedChapters(): Flow<List<CompletedChapterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedChapter(entity: CompletedChapterEntity)

    @Query("DELETE FROM completed_chapters WHERE chapterId = :chapterId")
    suspend fun removeCompletedChapter(chapterId: String)

    // Unlocked Chapters
    @Query("SELECT * FROM unlocked_chapters")
    fun getAllUnlockedChapters(): Flow<List<UnlockedChapterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnlockedChapter(entity: UnlockedChapterEntity)

    // Unlocked Tests
    @Query("SELECT * FROM unlocked_tests")
    fun getAllUnlockedTests(): Flow<List<UnlockedTestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnlockedTest(entity: UnlockedTestEntity)

    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY savedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(entity: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    suspend fun isBookmarked(id: String): Boolean

    // Highlights
    @Query("SELECT * FROM highlights WHERE chapterId = :chapterId ORDER BY createdAt DESC")
    fun getHighlightsForChapter(chapterId: String): Flow<List<HighlightEntity>>

    @Query("SELECT * FROM highlights ORDER BY createdAt DESC")
    fun getAllHighlights(): Flow<List<HighlightEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(entity: HighlightEntity)

    @Query("DELETE FROM highlights WHERE id = :id")
    suspend fun deleteHighlightById(id: String)

    // Scores (Attempts)
    @Query("SELECT * FROM scores ORDER BY attemptedAt DESC")
    fun getAllScores(): Flow<List<ScoreEntity>>

    @Query("SELECT * FROM scores WHERE testId = :testId ORDER BY score DESC LIMIT 1")
    suspend fun getHighScoreForTest(testId: String): ScoreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(entity: ScoreEntity)

    @Query("SELECT COUNT(*) FROM scores")
    fun getAttemptCount(): Flow<Int>

    // Downloaded Chapters (Offline Cache)
    @Query("SELECT * FROM downloaded_chapters")
    fun getAllDownloadedChapters(): Flow<List<DownloadedChapterEntity>>

    @Query("SELECT * FROM downloaded_chapters WHERE chapterId = :chapterId")
    suspend fun getDownloadedChapter(chapterId: String): DownloadedChapterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadedChapter(entity: DownloadedChapterEntity)

    @Query("DELETE FROM downloaded_chapters WHERE chapterId = :chapterId")
    suspend fun deleteDownloadedChapter(chapterId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM downloaded_chapters WHERE chapterId = :chapterId)")
    fun isChapterDownloaded(chapterId: String): Flow<Boolean>

    // Flashcards
    @Query("SELECT * FROM flashcards ORDER BY savedAt DESC")
    fun getAllFlashcards(): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE isDaily = 1")
    fun getDailyFlashcards(): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE isFavorite = 1")
    fun getFavoriteFlashcards(): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(entity: FlashcardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(entities: List<FlashcardEntity>)

    @Query("DELETE FROM flashcards WHERE id = :id")
    suspend fun deleteFlashcardById(id: String)

    @Query("DELETE FROM flashcards WHERE isDaily = 1 AND expiryTime < :currentTime")
    suspend fun clearExpiredDailyFlashcards(currentTime: Long)
}
