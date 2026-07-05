package com.example.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.*
import com.example.data.repository.ExamRepository
import com.example.data.repository.TestInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.Calendar

data class CountdownState(
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val appDao = database.appDao()
    val repository = ExamRepository(appDao, application)
    private val prefs = UserPreferencesRepository(application)
    private val contentVersionManager = com.example.data.repository.ContentVersionManager(appDao, application)

    // Sync status Flow
    private val _syncStatus = MutableStateFlow("Idle")
    val syncStatus = _syncStatus.asStateFlow()

    // User preferences
    val userName = prefs.userNameFlow.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val preferredLanguage = prefs.preferredLanguageFlow.stateIn(viewModelScope, SharingStarted.Eagerly, "en")
    val darkModeFlow = prefs.darkModeFlow.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val onboardingCompleted = prefs.onboardingCompletedFlow.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val studyGoalHours = prefs.studyGoalHoursFlow.stateIn(viewModelScope, SharingStarted.Eagerly, 2.0f)
    val lifetimeAdSkips = prefs.lifetimeAdSkipsFlow.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    // Exam countdown ticker
    private val _countdown = MutableStateFlow(CountdownState())
    val countdown: StateFlow<CountdownState> = _countdown.asStateFlow()

    // Database Reactive Flows
    val completedChapters = repository.completedChapters.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val unlockedChapters = repository.unlockedChapters.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val unlockedTests = repository.unlockedTests.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val bookmarks = repository.bookmarks.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val flashcards = repository.flashcards.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val scores = repository.scores.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Kindle Reader Preferences
    private val _readerFontSize = MutableStateFlow(16f)
    val readerFontSize = _readerFontSize.asStateFlow()

    private val _readerLineHeight = MutableStateFlow(1.5f)
    val readerLineHeight = _readerLineHeight.asStateFlow()

    private val _readerTheme = MutableStateFlow("Light") // "Light", "Dark", "Sepia", "Night"
    val readerTheme = _readerTheme.asStateFlow()

    private val _readerFontFamily = MutableStateFlow("Sans") // "Sans", "Serif", "Monospace"
    val readerFontFamily = _readerFontFamily.asStateFlow()

    // Test series / Chapter test active states
    private val _activeQuestions = MutableStateFlow<List<Question>>(emptyList())
    val activeQuestions = _activeQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex = _currentQuestionIndex.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<Map<Int, List<Int>>>(emptyMap()) // Index -> Option indices
    val selectedAnswers = _selectedAnswers.asStateFlow()

    private val _flaggedForReview = MutableStateFlow<Set<Int>>(emptySet())
    val flaggedForReview = _flaggedForReview.asStateFlow()

    private val _testTimerSec = MutableStateFlow(0)
    val testTimerSec = _testTimerSec.asStateFlow()

    private val _activeTestInfo = MutableStateFlow<TestInfo?>(null)
    val activeTestInfo = _activeTestInfo.asStateFlow()

    private val _isPreparingResult = MutableStateFlow(false)
    val isPreparingResult = _isPreparingResult.asStateFlow()

    private val _lastTestResult = MutableStateFlow<ScoreEntity?>(null)
    val lastTestResult = _lastTestResult.asStateFlow()

    // Active reading chapter highlight flows
    private val _activeChapterHighlights = MutableStateFlow<List<HighlightEntity>>(emptyList())
    val activeChapterHighlights = _activeChapterHighlights.asStateFlow()

    private var tickerJob: Job? = null
    private var testTimerJob: Job? = null

    init {
        // Seed initial data (Curated flashcards, etc.)
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
            repository.clearExpiredFlashcards()
            triggerContentSync()
        }
        startCountdownTicker()
    }

    fun triggerContentSync() {
        viewModelScope.launch {
            contentVersionManager.checkAndSyncContent { status ->
                _syncStatus.value = status
            }
        }
    }

    // Onboarding handlers
    fun completeOnboarding(name: String, language: String, goal: Float) {
        viewModelScope.launch {
            prefs.saveUserName(name)
            prefs.savePreferredLanguage(language)
            prefs.saveStudyGoalHours(goal)
            prefs.saveOnboardingCompleted(true)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            prefs.savePreferredLanguage(lang)
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            prefs.saveDarkMode(enabled)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            prefs.resetAllProgress()
            withContext(Dispatchers.IO) {
                database.clearAllTables()
                repository.seedInitialDataIfNeeded()
            }
        }
    }

    // Countdown logic: Target RPSC EO RO Exam Date set to November 15, 2026
    private fun startCountdownTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch(Dispatchers.Default) {
            val targetCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2026)
                set(Calendar.MONTH, Calendar.NOVEMBER) // 10 is November in Calendar class
                set(Calendar.DAY_OF_MONTH, 15)
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val targetTimeMillis = targetCalendar.timeInMillis

            while (isActive) {
                val diff = targetTimeMillis - System.currentTimeMillis()
                if (diff > 0) {
                    val sec = diff / 1000
                    val days = sec / (24 * 3600)
                    var rem = sec % (24 * 3600)
                    val hours = rem / 3600
                    rem %= 3600
                    val mins = rem / 60
                    val seconds = rem % 60
                    _countdown.value = CountdownState(days, hours, mins, seconds)
                } else {
                    _countdown.value = CountdownState(0, 0, 0, 0)
                }
                delay(1000)
            }
        }
    }

    // Kindle Reader Preferences Setters
    fun updateReaderFontSize(size: Float) {
        _readerFontSize.value = size.coerceIn(12f, 32f)
    }

    fun updateReaderLineHeight(height: Float) {
        _readerLineHeight.value = height.coerceIn(1.0f, 2.5f)
    }

    fun updateReaderTheme(theme: String) {
        _readerTheme.value = theme
    }

    fun updateReaderFontFamily(family: String) {
        _readerFontFamily.value = family
    }

    // Bookmark & Highlight Logic
    fun toggleChapterBookmark(chapter: Chapter) {
        viewModelScope.launch {
            val id = "chapter_${chapter.id}"
            if (repository.isBookmarked(id)) {
                repository.removeBookmark(id)
            } else {
                repository.saveBookmark(
                    id = id,
                    type = "chapter",
                    itemId = chapter.id,
                    titleEn = chapter.titleEn,
                    titleHi = chapter.titleHi,
                    subtitleEn = "Chapter in book: ${chapter.bookId}",
                    subtitleHi = "किताब का अध्याय: ${chapter.bookId}"
                )
            }
        }
    }

    fun loadHighlightsForChapter(chapterId: String) {
        viewModelScope.launch {
            repository.getHighlights(chapterId).collect {
                _activeChapterHighlights.value = it
            }
        }
    }

    fun addHighlight(chapterId: String, text: String, colorHex: String, noteText: String, pageIndex: Int) {
        viewModelScope.launch {
            val entity = HighlightEntity(
                id = "hl_${System.currentTimeMillis()}",
                chapterId = chapterId,
                text = text,
                colorHex = colorHex,
                noteText = noteText,
                pageIndex = pageIndex
            )
            repository.insertHighlight(entity)
        }
    }

    fun deleteHighlight(highlightId: String) {
        viewModelScope.launch {
            repository.deleteHighlight(highlightId)
        }
    }

    // Offline downloads management
    fun toggleChapterDownload(chapterId: String) {
        viewModelScope.launch {
            val isDownloaded = repository.isChapterDownloaded(chapterId).first()
            if (isDownloaded) {
                repository.deleteDownloadedChapter(chapterId)
            } else {
                repository.downloadChapter(chapterId)
            }
        }
    }

    // Chapter completion logic
    fun toggleChapterCompletion(chapterId: String) {
        viewModelScope.launch {
            val completedList = completedChapters.value
            val isCompleted = completedList.any { it.chapterId == chapterId }
            if (isCompleted) {
                repository.removeCompletedChapter(chapterId)
            } else {
                repository.insertCompletedChapter(chapterId)
            }
        }
    }

    // Simulated Rewarded Ad Unlocks
    fun unlockChapterByReward(chapterId: String) {
        viewModelScope.launch {
            repository.unlockChapter(chapterId)
        }
    }

    fun unlockTestByReward(testId: String) {
        viewModelScope.launch {
            repository.unlockTest(testId)
        }
    }

    suspend fun trySkipAdWithLifetimeSkip(): Boolean {
        return prefs.incrementAdSkips()
    }

    // Active Test Engine
    fun startTest(testInfo: TestInfo) {
        _activeTestInfo.value = testInfo
        _activeQuestions.value = repository.getQuestionsForTest(testInfo.id)
        _currentQuestionIndex.value = 0
        _selectedAnswers.value = emptyMap()
        _flaggedForReview.value = emptySet()
        _testTimerSec.value = testInfo.durationMins * 60
        _lastTestResult.value = null

        testTimerJob?.cancel()
        testTimerJob = viewModelScope.launch {
            while (_testTimerSec.value > 0) {
                delay(1000)
                _testTimerSec.value -= 1
            }
            // Auto submit when timer ticks down
            submitTest()
        }
    }

    fun selectAnswer(questionIndex: Int, optionIndex: Int, isMultipleChoice: Boolean = false) {
        val currentAnswers = _selectedAnswers.value.toMutableMap()
        val selectedOptions = currentAnswers[questionIndex]?.toMutableList() ?: mutableListOf()

        if (isMultipleChoice) {
            if (selectedOptions.contains(optionIndex)) {
                selectedOptions.remove(optionIndex)
            } else {
                selectedOptions.add(optionIndex)
            }
        } else {
            selectedOptions.clear()
            selectedOptions.add(optionIndex)
        }

        currentAnswers[questionIndex] = selectedOptions
        _selectedAnswers.value = currentAnswers
    }

    fun toggleFlagForReview(questionIndex: Int) {
        val currentFlags = _flaggedForReview.value.toMutableSet()
        if (currentFlags.contains(questionIndex)) {
            currentFlags.remove(questionIndex)
        } else {
            currentFlags.add(questionIndex)
        }
        _flaggedForReview.value = currentFlags
    }

    fun navigateToQuestion(index: Int) {
        if (index in 0 until _activeQuestions.value.size) {
            _currentQuestionIndex.value = index
        }
    }

    fun submitTest() {
        testTimerJob?.cancel()
        val info = _activeTestInfo.value ?: return
        val questions = _activeQuestions.value
        val userAnswers = _selectedAnswers.value

        _isPreparingResult.value = true

        viewModelScope.launch {
            // Simulate preparing performance assessment
            delay(1500)

            var correctCount = 0
            var incorrectCount = 0
            var skippedCount = 0

            questions.forEachIndexed { index, question ->
                val answers = userAnswers[index]
                if (answers.isNullOrEmpty()) {
                    skippedCount++
                } else {
                    // For single choice, evaluate answers[0]
                    if (answers[0] == question.correctAnswerIndex) {
                        correctCount++
                    } else {
                        incorrectCount++
                    }
                }
            }

            // Calculation values
            val marksPerQuestion = info.totalMarks / questions.size
            val finalScore = correctCount * marksPerQuestion
            val timeTaken = (info.durationMins * 60) - _testTimerSec.value

            val scoreEntity = ScoreEntity(
                id = "score_${System.currentTimeMillis()}",
                testId = info.id,
                testNameEn = info.titleEn,
                testNameHi = info.titleHi,
                testType = info.testType,
                score = finalScore,
                totalQuestions = questions.size,
                correctCount = correctCount,
                incorrectCount = incorrectCount,
                skippedCount = skippedCount,
                timeTakenSec = timeTaken
            )

            repository.saveTestScore(scoreEntity)
            _lastTestResult.value = scoreEntity
            _isPreparingResult.value = false
        }
    }

    // Flashcards Operations
    fun toggleFlashcardFavorite(card: FlashcardEntity) {
        viewModelScope.launch {
            repository.toggleFlashcardFavorite(card)
        }
    }

    fun addCustomFlashcard(frontEn: String, frontHi: String, backEn: String, backHi: String, category: String) {
        viewModelScope.launch {
            repository.addCustomFlashcard(frontEn, frontHi, backEn, backHi, category)
        }
    }

    fun deleteFlashcard(id: String) {
        viewModelScope.launch {
            repository.deleteFlashcard(id)
        }
    }

    // Stats calculations for beautiful dashboard indicators
    val studyStats: Flow<StudyStats> = combine(
        scores,
        completedChapters
    ) { scoreList, completed ->
        val avgScore = if (scoreList.isNotEmpty()) {
            val percentages = scoreList.map { (it.score.toFloat() / (it.totalQuestions * 2)) * 100 }
            percentages.average().toInt().coerceIn(0, 100)
        } else {
            0
        }

        val wrongCount = scoreList.sumOf { it.incorrectCount }
        val mockCount = scoreList.count { it.testType == "mock_test" }

        // Dynamic streak generation based on score logs or standard 3-day baseline
        val streak = if (scoreList.isNotEmpty()) {
            val uniqueDays = scoreList.map { 
                val cal = Calendar.getInstance().apply { timeInMillis = it.attemptedAt }
                cal.get(Calendar.DAY_OF_YEAR)
            }.toSet().size
            uniqueDays.coerceAtLeast(1)
        } else {
            0
        }

        StudyStats(
            dailyStreak = streak,
            studyGoalHours = studyGoalHours.value,
            averageScore = avgScore,
            completedChapterCount = completed.size,
            mockTestsAttempted = mockCount,
            wrongQuestionsCount = wrongCount
        )
    }.flowOn(Dispatchers.Default)

    override fun onCleared() {
        super.onCleared()
        tickerJob?.cancel()
        testTimerJob?.cancel()
    }
}
