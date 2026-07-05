package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// Domain Models

data class Book(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val part: String, // "Part A" or "Part B"
    val iconName: String
) : Serializable

data class Chapter(
    val id: String,
    val bookId: String,
    val titleEn: String,
    val titleHi: String,
    val estimatedReadingTime: String,
    val orderIndex: Int,
    val subtopicsEn: List<String>,
    val subtopicsHi: List<String>
) : Serializable

data class ChapterContent(
    val chapterId: String,
    val pagesEn: List<String>,
    val pagesHi: List<String>
) : Serializable

data class Question(
    val id: String,
    val testId: String,
    val textEn: String,
    val textHi: String,
    val optionsEn: List<String>,
    val optionsHi: List<String>,
    val correctAnswerIndex: Int,
    val type: QuestionType,
    val explanationEn: String,
    val explanationHi: String
) : Serializable

enum class QuestionType {
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    ASSERTION_REASON,
    STATEMENT_BASED,
    MATCH_THE_FOLLOWING
}

// Room Database Entities

@Entity(tableName = "completed_chapters")
data class CompletedChapterEntity(
    @PrimaryKey val chapterId: String,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "unlocked_chapters")
data class UnlockedChapterEntity(
    @PrimaryKey val chapterId: String,
    val unlockedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "unlocked_tests")
data class UnlockedTestEntity(
    @PrimaryKey val testId: String,
    val unlockedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String, // Combination of itemType + itemId
    val itemType: String, // "chapter" or "question"
    val itemId: String,
    val titleEn: String,
    val titleHi: String,
    val subtitleEn: String,
    val subtitleHi: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "highlights")
data class HighlightEntity(
    @PrimaryKey val id: String,
    val chapterId: String,
    val text: String,
    val colorHex: String,
    val noteText: String = "",
    val pageIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey val id: String, // auto-generated or composite
    val testId: String,
    val testNameEn: String,
    val testNameHi: String,
    val testType: String, // "chapter_test" or "mock_test"
    val score: Int,
    val totalQuestions: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val skippedCount: Int,
    val timeTakenSec: Int,
    val attemptedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "downloaded_chapters")
data class DownloadedChapterEntity(
    @PrimaryKey val chapterId: String,
    val bookId: String,
    val titleEn: String,
    val titleHi: String,
    val contentJson: String, // JSON serialization of ChapterContent
    val downloadedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val frontEn: String,
    val frontHi: String,
    val backEn: String,
    val backHi: String,
    val category: String, // e.g. "Municipal Revenue", "Rajasthan History"
    val isDaily: Boolean,
    val expiryTime: Long = 0L, // 0 for saved flashcards, currentMillis + 24hrs for daily
    val isFavorite: Boolean = false,
    val savedAt: Long = System.currentTimeMillis()
)

data class StudyStats(
    val dailyStreak: Int = 0,
    val studyGoalHours: Float = 2.0f,
    val averageScore: Int = 0,
    val completedChapterCount: Int = 0,
    val mockTestsAttempted: Int = 0,
    val wrongQuestionsCount: Int = 0
)

@Entity(tableName = "content_version")
data class ContentVersionEntity(
    @PrimaryKey val id: String = "manifest_version",
    val version: Int,
    val lastChecked: Long = System.currentTimeMillis()
)

