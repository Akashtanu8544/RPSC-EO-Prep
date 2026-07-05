package com.example.presentation.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.*
import com.example.data.repository.*
import com.example.presentation.utils.Localization
import com.example.presentation.viewmodel.AppViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// Helper component for Premium Background Gradients
@Composable
fun PremiumGradientBrush(): Brush {
    return Brush.linearGradient(
        colors = listOf(PurpleGradStart, PurpleGradEnd),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )
}

// ----------------- SPLASH & ONBOARDING SCREEN -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: AppViewModel,
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var selectedLang by remember { mutableStateOf("en") }
    var studyGoal by remember { mutableFloatStateOf(3f) }

    // Floating logo animation
    val infiniteTransition = rememberInfiniteTransition(label = "logo")
    val logoOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Animated Header / Logo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .offset(y = logoOffset.dp)
            ) {
                // Premium Styled Logo Card with Gradient
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(16.dp, shape = RoundedCornerShape(24.dp))
                        .background(PremiumGradientBrush(), shape = RoundedCornerShape(24.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Logo icon",
                        tint = PureWhite,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = Localization.getString("welcome_officer", selectedLang),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = Localization.getString("rpsc_subtitle", selectedLang),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            // Onboarding Form Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Language Selection Title
                    Text(
                        text = Localization.getString("select_lang", selectedLang),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Language Toggle Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf("en" to "English", "hi" to "हिन्दी").forEach { (code, label) ->
                            val isSelected = selectedLang == code
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) PurpleGradStart else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .background(
                                        color = if (isSelected) PurpleGradStart.copy(alpha = 0.1f) else Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { selectedLang = code }
                                    .testTag("lang_toggle_$code"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "selected",
                                            tint = PurpleGradStart,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Text(
                                        text = label,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) PurpleGradStart else MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }

                    // User Name Field
                    Text(
                        text = Localization.getString("enter_name", selectedLang),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("E.g., Aditya Sharma") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("name_input"),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurpleGradStart,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
                        )
                    )

                    // Initial Study Goal Slider
                    Text(
                        text = "${Localization.getString("set_goal", selectedLang)}: ${studyGoal.roundToInt()} hrs",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = studyGoal,
                        onValueChange = { studyGoal = it },
                        valueRange = 1f..12f,
                        steps = 10,
                        colors = SliderDefaults.colors(
                            activeTrackColor = PurpleGradStart,
                            thumbColor = PurpleGradStart
                        ),
                        modifier = Modifier.testTag("goal_slider")
                    )
                    Text(
                        text = Localization.getString("goal_hint", selectedLang),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }

            // Submit Button
            Button(
                onClick = {
                    if (name.trim().isEmpty()) {
                        Toast.makeText(context, "Please enter your name / कृपया अपना नाम दर्ज करें", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.completeOnboarding(name, selectedLang, studyGoal)
                        onNavigateToHome()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(28.dp))
                    .testTag("get_started_button"),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Localization.getString("get_started", selectedLang),
                        style = MaterialTheme.typography.titleMedium,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Arrow next",
                        tint = PureWhite
                    )
                }
            }
        }
    }
}

// ----------------- HOME SCREEN (DASHBOARD) -----------------

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onNavigateToRead: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToFlashcards: () -> Unit,
    onNavigateToCMS: () -> Unit
) {
    val context = LocalContext.current
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val lang by viewModel.preferredLanguage.collectAsStateWithLifecycle()
    val countdown by viewModel.countdown.collectAsStateWithLifecycle()
    val stats by viewModel.studyStats.collectAsStateWithLifecycle(initialValue = StudyStats())
    val darkModeVal by viewModel.darkModeFlow.collectAsStateWithLifecycle()
    val isSystemDark = isSystemInDarkTheme()
    val activeDark = darkModeVal ?: isSystemDark

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 1. Premium Toolbar / Welcome Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${Localization.getString("welcome", lang)}$userName",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = Localization.getString("rpsc_subtitle", lang),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }

                // Interactive control toggles
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Language switcher button
                    IconButton(
                        onClick = { viewModel.setLanguage(if (lang == "en") "hi" else "en") },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
                            .testTag("home_lang_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Switch language",
                            tint = PurpleGradStart
                        )
                    }

                    // Dark/Light mode toggle
                    IconButton(
                        onClick = { viewModel.toggleDarkMode(!activeDark) },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
                            .testTag("home_dark_toggle")
                    ) {
                        Icon(
                            imageVector = if (activeDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = PurpleGradStart
                        )
                    }

                    // CMS Panel Info button
                    IconButton(
                        onClick = onNavigateToCMS,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
                            .testTag("home_cms_info")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "CMS Manager",
                            tint = PurpleGradStart
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Exam Live Countdown Ticker Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(12.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SlateGrey)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = Localization.getString("exam_countdown", lang),
                        style = MaterialTheme.typography.titleMedium,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        CountdownCircle(value = countdown.days, label = Localization.getString("days", lang))
                        CountdownCircle(value = countdown.hours, label = Localization.getString("hours", lang))
                        CountdownCircle(value = countdown.minutes, label = Localization.getString("minutes", lang))
                        CountdownCircle(value = countdown.seconds, label = Localization.getString("seconds", lang))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Quick stats cards (Flashcards, test series, study progress)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HomeDashboardRingCard(
                    title = Localization.getString("flashcards", lang),
                    progress = 1.0f,
                    pctText = "5 Deck",
                    icon = Icons.Default.Style,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToFlashcards
                )
                HomeDashboardRingCard(
                    title = Localization.getString("test_series", lang),
                    progress = (stats.averageScore.toFloat() / 100f).coerceIn(0f, 1f),
                    pctText = "${stats.averageScore}%",
                    icon = Icons.Default.Assignment,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToPractice
                )
                HomeDashboardRingCard(
                    title = Localization.getString("study_progress", lang),
                    progress = (stats.completedChapterCount.toFloat() / 8f).coerceIn(0f, 1f),
                    pctText = "${((stats.completedChapterCount.toFloat() / 8f) * 100).roundToInt()}%",
                    icon = Icons.Default.TrendingUp,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToRead
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Two Primary Navigation Buttons (Read Mode & Practice Mode)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Read Mode Navigation
                Button(
                    onClick = onNavigateToRead,
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                        .shadow(12.dp, shape = RoundedCornerShape(20.dp))
                        .testTag("home_read_btn"),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.MenuBook, contentDescription = "read mode", tint = PureWhite, modifier = Modifier.size(24.dp))
                        Text(
                            text = Localization.getString("read_mode", lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }
                }

                // Practice Mode Navigation
                Button(
                    onClick = onNavigateToPractice,
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                        .shadow(12.dp, shape = RoundedCornerShape(20.dp))
                        .testTag("home_practice_btn"),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleGradEnd)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Quiz, contentDescription = "practice mode", tint = PureWhite, modifier = Modifier.size(24.dp))
                        Text(
                            text = Localization.getString("practice_mode", lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Daily Streak Metrics
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Officer Performance Indexes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(
                            label = Localization.getString("streak", lang),
                            value = "${stats.dailyStreak} 🔥",
                            modifier = Modifier.weight(1f)
                        )
                        StatItem(
                            label = Localization.getString("target", lang),
                            value = "${stats.completedChapterCount}/8 ${Localization.getString("chapters", lang)}",
                            modifier = Modifier.weight(1f)
                        )
                        StatItem(
                            label = Localization.getString("avg_score", lang),
                            value = "${stats.averageScore}% 🎯",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 6. AdMob Native Ad Container (Integrated beautifully and unobtrusively)
            AdMobNativeAdPlaceholder(lang = lang)
        }
    }
}

@Composable
fun CountdownCircle(value: Long, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(PureWhite.copy(alpha = 0.15f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = String.format("%02d", value),
                style = MaterialTheme.typography.titleLarge,
                color = PureWhite,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = PureWhite.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun HomeDashboardRingCard(
    title: String,
    progress: Float,
    pctText: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(4.dp, shape = RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = PurpleGradStart,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Progress Ring
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(45.dp)) {
                    drawCircle(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        style = Stroke(width = 8f)
                    )
                    drawArc(
                        color = PurpleGradStart,
                        startAngle = -90f,
                        sweepAngle = progress * 360f,
                        useCenter = false,
                        style = Stroke(width = 8f)
                    )
                }
                Text(
                    text = pctText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PurpleGradStart
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AdMobNativeAdPlaceholder(lang: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PurpleGradStart.copy(alpha = 0.2f)),
        colors = CardDefaults.cardColors(containerColor = PurpleGradStart.copy(alpha = 0.04f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(PurpleGradStart, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AD",
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "AdMob Premium Sponsor",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = PurpleGradStart
                )
                Text(
                    text = if (lang == "hi") "आरपीएससी अधिकारी परीक्षा में अपनी गति 2 गुना बढ़ाएं!" else "Boost your study efficiency with smart mocks!",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Install", fontSize = 12.sp, color = PureWhite, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ----------------- READ MODE SCREEN (LIBRARY) -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadModeScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReader: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lang by viewModel.preferredLanguage.collectAsStateWithLifecycle()
    val completedChapters by viewModel.completedChapters.collectAsStateWithLifecycle()
    val unlockedChapters by viewModel.unlockedChapters.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") } // "All", "Part A", "Part B"
    var expandedBookId by remember { mutableStateOf<String?>(null) }

    // Dialog state for premium rewarded ad simulation
    var showUnlockDialogByChapterId by remember { mutableStateOf<String?>(null) }
    var isSimulatingAdVideo by remember { mutableStateOf(false) }

    val books = viewModel.repository.getBooks().filter {
        val matchesQuery = it.titleEn.contains(searchQuery, ignoreCase = true) ||
                it.titleHi.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == "All" || it.part == selectedFilter
        matchesQuery && matchesFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.getString("read_mode", lang), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("read_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(Localization.getString("search_hint", lang)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleGradStart,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                    )
                )

                // Filtering Chips row
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All" to "filter_all", "Part A" to "filter_part_a", "Part B" to "filter_part_b").forEach { (filterValue, locKey) ->
                        val isSelected = selectedFilter == filterValue
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filterValue },
                            label = { Text(Localization.getString(locKey, lang)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurpleGradStart,
                                selectedLabelColor = PureWhite
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                // Books & Chapters List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(books) { book ->
                        val isExpanded = expandedBookId == book.id
                        val chapters = viewModel.repository.getChapters(book.id)
                        val totalChaptersCount = chapters.size
                        val completedInBook = chapters.count { ch -> completedChapters.any { it.chapterId == ch.id } }
                        val bookProgressPct = if (totalChaptersCount > 0) completedInBook.toFloat() / totalChaptersCount else 0f

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, shape = RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column {
                                // Book Summary Header
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { expandedBookId = if (isExpanded) null else book.id }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    // Icon Circle
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(PremiumGradientBrush(), shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when(book.iconName) {
                                                "balance" -> Icons.Default.Balance
                                                "museum" -> Icons.Default.Museum
                                                "domain" -> Icons.Default.Domain
                                                else -> Icons.Default.Verified
                                            },
                                            contentDescription = "book icon",
                                            tint = PureWhite,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Box(
                                            modifier = Modifier
                                                .background(PurpleGradStart.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = book.part,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PurpleGradStart
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = if (lang == "hi") book.titleHi else book.titleEn,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (lang == "hi") book.descriptionHi else book.descriptionEn,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    // Completion Progress Indicator
                                    Box(contentAlignment = Alignment.Center) {
                                        Canvas(modifier = Modifier.size(40.dp)) {
                                            drawArc(
                                                color = Color.LightGray.copy(alpha = 0.3f),
                                                startAngle = 0f,
                                                sweepAngle = 360f,
                                                useCenter = false,
                                                style = Stroke(width = 6f)
                                            )
                                            drawArc(
                                                color = SuccessColor,
                                                startAngle = -90f,
                                                sweepAngle = bookProgressPct * 360f,
                                                useCenter = false,
                                                style = Stroke(width = 6f)
                                            )
                                        }
                                        Text(
                                            text = "${(bookProgressPct * 100).roundToInt()}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    }
                                }

                                // Chapter list (Expanded)
                                AnimatedVisibility(visible = isExpanded) {
                                    Column(
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.4f))
                                            .padding(vertical = 8.dp)
                                    ) {
                                        HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                                        chapters.forEach { chapter ->
                                            val isCompleted = completedChapters.any { it.chapterId == chapter.id }
                                            val isChapterFree = chapter.orderIndex <= 2 // First 2 chapters are free
                                            val isUnlocked = isChapterFree || unlockedChapters.any { it.chapterId == chapter.id }
                                            val isDownloaded by viewModel.repository.isChapterDownloaded(chapter.id).collectAsStateWithLifecycle(initialValue = false)

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        if (isUnlocked) {
                                                            onNavigateToReader(chapter.id)
                                                        } else {
                                                            showUnlockDialogByChapterId = chapter.id
                                                        }
                                                    }
                                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        if (isCompleted) {
                                                            Icon(
                                                                imageVector = Icons.Default.CheckCircle,
                                                                contentDescription = "completed",
                                                                tint = SuccessColor,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }
                                                        Text(
                                                            text = if (lang == "hi") chapter.titleHi else chapter.titleEn,
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (isUnlocked) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = "${Localization.getString("reading_time", lang)} ${chapter.estimatedReadingTime}",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                                    )
                                                }

                                                // Action buttons
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    // Download offline icon
                                                    IconButton(
                                                        onClick = { viewModel.toggleChapterDownload(chapter.id) },
                                                        modifier = Modifier.size(36.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = if (isDownloaded) Icons.Default.CloudDone else Icons.Default.CloudDownload,
                                                            contentDescription = "download",
                                                            tint = if (isDownloaded) SuccessColor else PurpleGradStart.copy(alpha = 0.6f),
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    }

                                                    // Unlock status Badge
                                                    Box(
                                                        modifier = Modifier
                                                            .background(
                                                                color = if (isUnlocked) SuccessColor.copy(alpha = 0.1f) else WarningColor.copy(alpha = 0.1f),
                                                                shape = RoundedCornerShape(6.dp)
                                                            )
                                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                                    ) {
                                                        Text(
                                                            text = if (isUnlocked) Localization.getString("free_badge", lang) else Localization.getString("pro_badge", lang),
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (isUnlocked) SuccessColor else WarningColor
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Reward Ad Unlock Modal
            showUnlockDialogByChapterId?.let { chapterId ->
                val targetChapter = viewModel.repository.getChapters("").find { it.id == chapterId } ?: CuratedExamData.chapters.find { it.id == chapterId }
                if (targetChapter != null) {
                    AlertDialog(
                        onDismissRequest = { if (!isSimulatingAdVideo) showUnlockDialogByChapterId = null },
                        title = { Text(Localization.getString("unlock_reward_title", lang)) },
                        text = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(Localization.getString("unlock_reward_desc", lang))
                                Spacer(modifier = Modifier.height(16.dp))
                                if (isSimulatingAdVideo) {
                                    CircularProgressIndicator(color = PurpleGradStart)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Simulating Video Ad playback...", fontSize = 12.sp, color = PurpleGradStart)
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    isSimulatingAdVideo = true
                                    coroutineScope.launch {
                                        delay(2500) // Watch 2.5s ad simulation
                                        viewModel.unlockChapterByReward(chapterId)
                                        isSimulatingAdVideo = false
                                        showUnlockDialogByChapterId = null
                                        Toast.makeText(context, Localization.getString("unlock_success", lang), Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart),
                                enabled = !isSimulatingAdVideo
                            ) {
                                Text(Localization.getString("unlock_btn", lang), color = PureWhite)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showUnlockDialogByChapterId = null },
                                enabled = !isSimulatingAdVideo
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

// ----------------- READER SCREEN (KINDLE INSPIRED) -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    chapterId: String,
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lang by viewModel.preferredLanguage.collectAsStateWithLifecycle()
    val completedChapters by viewModel.completedChapters.collectAsStateWithLifecycle()

    // Kindle reader configurations
    val fontSize by viewModel.readerFontSize.collectAsStateWithLifecycle()
    val lineHeight by viewModel.readerLineHeight.collectAsStateWithLifecycle()
    val readerThemeVal by viewModel.readerTheme.collectAsStateWithLifecycle()
    val fontFamilyName by viewModel.readerFontFamily.collectAsStateWithLifecycle()

    var chapterContent by remember { mutableStateOf<ChapterContent?>(null) }
    var activePageIdx by remember { mutableIntStateOf(0) }
    var showSettingsPanel by remember { mutableStateOf(false) }

    // Highlight / Notes state
    val highlights by viewModel.activeChapterHighlights.collectAsStateWithLifecycle()
    var selectedHighlightColor by remember { mutableStateOf("#FEF08A") } // Yellow hex
    var enteredNoteText by remember { mutableStateOf("") }
    var isAddingNoteForText by remember { mutableStateOf<String?>(null) }

    // Dictionary state
    var selectedWordForDictionary by remember { mutableStateOf<String?>(null) }

    val chapter = CuratedExamData.chapters.find { it.id == chapterId }

    // Load content and highlights
    LaunchedEffect(chapterId) {
        chapterContent = viewModel.repository.getChapterContent(chapterId)
        viewModel.loadHighlightsForChapter(chapterId)
    }

    // Determine reader palette
    val readerBg = when(readerThemeVal) {
        "Dark" -> DarkGrey
        "Sepia" -> SepiaBackground
        "Night" -> NightBackground
        else -> PureWhite
    }
    val readerTextCol = when(readerThemeVal) {
        "Dark" -> PureWhite
        "Sepia" -> SepiaTextColor
        "Night" -> NightTextColor
        else -> SlateGrey
    }

    val pageList = if (lang == "hi") chapterContent?.pagesHi ?: emptyList() else chapterContent?.pagesEn ?: emptyList()
    val pageCount = pageList.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (lang == "hi") chapter?.titleHi ?: "" else chapter?.titleEn ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = readerTextCol
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = readerTextCol)
                    }
                },
                actions = {
                    // Settings panel toggle
                    IconButton(onClick = { showSettingsPanel = !showSettingsPanel }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Font settings", tint = readerTextCol)
                    }

                    // Bookmark chapter
                    IconButton(onClick = {
                        chapter?.let {
                            viewModel.toggleChapterBookmark(it)
                            Toast.makeText(context, "Chapter Bookmarked!", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = "Bookmark", tint = readerTextCol)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = readerBg)
            )
        },
        bottomBar = {
            // Immersive Kindle stats reading footer
            Surface(
                color = readerBg,
                border = BorderStroke(1.dp, readerTextCol.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Page ${if (pageCount > 0) activePageIdx + 1 else 0} of $pageCount",
                        color = readerTextCol.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Mark as complete checkbox button
                    val isCompleted = completedChapters.any { it.chapterId == chapterId }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.clickable {
                            viewModel.toggleChapterCompletion(chapterId)
                            Toast.makeText(context, if (isCompleted) "Completed Flag Removed" else "Chapter Completed! Streak updated!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "complete",
                            tint = if (isCompleted) SuccessColor else readerTextCol.copy(alpha = 0.5f),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Mark Completed",
                            color = readerTextCol.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(readerBg)
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Kindle Reading Top Linear Progress bar
                val progressVal = if (pageCount > 0) (activePageIdx + 1).toFloat() / pageCount.toFloat() else 0f
                LinearProgressIndicator(
                    progress = { progressVal },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = PurpleGradStart,
                    trackColor = readerTextCol.copy(alpha = 0.05f)
                )

                // Page Reader View
                if (pageCount > 0) {
                    val pageContent = pageList[activePageIdx]
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
                    ) {
                        // Highlight-sensitive interactive page content
                        // In clean kindle styling we let users select any sentence to highlight
                        val paragraphs = pageContent.split("\n\n")
                        paragraphs.forEach { para ->
                            Text(
                                text = para,
                                fontSize = fontSize.sp,
                                lineHeight = (fontSize * lineHeight).sp,
                                fontFamily = when(fontFamilyName) {
                                    "Serif" -> FontFamily.Serif
                                    "Monospace" -> FontFamily.Monospace
                                    else -> FontFamily.Default
                                },
                                color = readerTextCol,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    // Clicking a paragraph triggers highlight / dict options
                                    .clickable {
                                        isAddingNoteForText = para
                                    }
                            )
                        }

                        // Render existing highlights on page
                        val pageHighlights = highlights.filter { it.pageIndex == activePageIdx }
                        if (pageHighlights.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            HorizontalDivider(color = readerTextCol.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Active Highlights & Marginalia:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = readerTextCol)
                            Spacer(modifier = Modifier.height(10.dp))
                            pageHighlights.forEach { hl ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(android.graphics.Color.parseColor(hl.colorHex)).copy(alpha = 0.15f))
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("\"${hl.text}\"", fontSize = 12.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = readerTextCol)
                                        if (hl.noteText.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("Note: ${hl.noteText}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PurpleGradStart)
                                        }
                                        IconButton(onClick = { viewModel.deleteHighlight(hl.id) }, modifier = Modifier.size(24.dp).align(Alignment.End)) {
                                            Icon(Icons.Default.Delete, contentDescription = "delete", tint = ErrorColor, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Simple page nav row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { if (activePageIdx > 0) activePageIdx-- },
                            enabled = activePageIdx > 0
                        ) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "prev")
                            Text("Prev Page")
                        }

                        TextButton(
                            onClick = { if (activePageIdx < pageCount - 1) activePageIdx++ },
                            enabled = activePageIdx < pageCount - 1
                        ) {
                            Text("Next Page")
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "next")
                        }
                    }
                }
            }

            // Kindle Font & Formatting Settings drawer overlay
            if (showSettingsPanel) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .shadow(24.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    color = readerBg,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    border = BorderStroke(1.dp, readerTextCol.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Formatting & Typography", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = readerTextCol)
                            IconButton(onClick = { showSettingsPanel = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = readerTextCol)
                            }
                        }

                        // Font size control
                        Text("${Localization.getString("font_size", lang)}: ${fontSize.roundToInt()}sp", color = readerTextCol, fontSize = 14.sp)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("A-", fontSize = 12.sp, color = readerTextCol)
                            Slider(
                                value = fontSize,
                                onValueChange = { viewModel.updateReaderFontSize(it) },
                                valueRange = 12f..32f,
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(thumbColor = PurpleGradStart, activeTrackColor = PurpleGradStart)
                            )
                            Text("A+", fontSize = 18.sp, color = readerTextCol)
                        }

                        // Line Height Control
                        Text("${Localization.getString("line_height", lang)}: $lineHeight", color = readerTextCol, fontSize = 14.sp)
                        Slider(
                            value = lineHeight,
                            onValueChange = { viewModel.updateReaderLineHeight(it) },
                            valueRange = 1.0f..2.5f,
                            colors = SliderDefaults.colors(thumbColor = PurpleGradStart, activeTrackColor = PurpleGradStart)
                        )

                        // Theme Mode selection
                        Text("Reading Space Background", color = readerTextCol, fontSize = 14.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Light", "Dark", "Sepia", "Night").forEach { themeName ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .background(
                                            color = when (themeName) {
                                                "Dark" -> DarkGrey
                                                "Sepia" -> SepiaBackground
                                                "Night" -> NightBackground
                                                else -> PureWhite
                                            },
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .border(
                                            width = if (readerThemeVal == themeName) 2.dp else 1.dp,
                                            color = if (readerThemeVal == themeName) PurpleGradStart else Color.LightGray,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { viewModel.updateReaderTheme(themeName) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = themeName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (themeName) {
                                            "Dark" -> PureWhite
                                            "Sepia" -> SepiaTextColor
                                            "Night" -> NightTextColor
                                            else -> SlateGrey
                                        }
                                    )
                                }
                            }
                        }

                        // Font Family choice
                        Text("Font Family", color = readerTextCol, fontSize = 14.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Sans", "Serif", "Monospace").forEach { fam ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .border(
                                            width = if (fontFamilyName == fam) 2.dp else 1.dp,
                                            color = if (fontFamilyName == fam) PurpleGradStart else Color.LightGray,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { viewModel.updateReaderFontFamily(fam) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = fam,
                                        fontSize = 12.sp,
                                        fontFamily = when(fam) {
                                            "Serif" -> FontFamily.Serif
                                            "Monospace" -> FontFamily.Monospace
                                            else -> FontFamily.Default
                                        },
                                        color = readerTextCol
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Note Addition and Highlighting Modal Dialogue
            isAddingNoteForText?.let { selectedText ->
                AlertDialog(
                    onDismissRequest = { isAddingNoteForText = null },
                    title = { Text("Highlight text & Add Marginalia") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("\"$selectedText\"", style = MaterialTheme.typography.bodyMedium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)

                            // Select Highlight Color row
                            Text("Highlight Color:", fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("#FEF08A" to AccentYellow, "#E9D5FF" to AccentPurple, "#BFDBFE" to AccentBlue, "#BCF0DA" to AccentGreen, "#FBCFE8" to AccentPink).forEach { (hex, col) ->
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(col, shape = CircleShape)
                                            .border(
                                                width = if (selectedHighlightColor == hex) 3.dp else 0.dp,
                                                color = PurpleGradStart,
                                                shape = CircleShape
                                            )
                                            .clickable { selectedHighlightColor = hex }
                                    )
                                }
                            }

                            // Dictionary Translation toggle
                            Button(
                                onClick = {
                                    // Simulated translation
                                    val englishTrans = "This relates to state municipal administrative codes and local urban bodies regulatory structure."
                                    val hindiTrans = "यह राज्य नगरपालिका प्रशासनिक कोड और स्थानीय शहरी निकायों की नियामक संरचना से संबंधित है।"
                                    selectedWordForDictionary = if (lang == "hi") englishTrans else hindiTrans
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart.copy(alpha = 0.1f), contentColor = PurpleGradStart)
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Translate, contentDescription = "trans", modifier = Modifier.size(16.dp))
                                    Text("Simulate Scholar Translation")
                                }
                            }

                            selectedWordForDictionary?.let { translationStr ->
                                Card(colors = CardDefaults.cardColors(containerColor = PurpleGradStart.copy(alpha = 0.05f))) {
                                    Text(translationStr, modifier = Modifier.padding(10.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Enter note text
                            OutlinedTextField(
                                value = enteredNoteText,
                                onValueChange = { enteredNoteText = it },
                                placeholder = { Text("Add study notes here...") },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.addHighlight(
                                    chapterId = chapterId,
                                    text = selectedText.take(100) + "...",
                                    colorHex = selectedHighlightColor,
                                    noteText = enteredNoteText,
                                    pageIndex = activePageIdx
                                )
                                isAddingNoteForText = null
                                enteredNoteText = ""
                                selectedWordForDictionary = null
                                Toast.makeText(context, "Saved Margin Highlight!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart)
                        ) {
                            Text("Save Highlight", color = PureWhite)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            isAddingNoteForText = null
                            enteredNoteText = ""
                            selectedWordForDictionary = null
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

// ----------------- PRACTICE MODE SCREEN (EXAMS) -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeModeScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToTest: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lang by viewModel.preferredLanguage.collectAsStateWithLifecycle()
    val unlockedTests by viewModel.unlockedTests.collectAsStateWithLifecycle()
    val scores by viewModel.scores.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("chapter_test") } // "chapter_test" or "mock_test"
    var showUnlockDialogByTestId by remember { mutableStateOf<String?>(null) }
    var isSimulatingAdPlayback by remember { mutableStateOf(false) }

    // Skips state for ad bypass
    val remainingSkips by viewModel.lifetimeAdSkips.collectAsStateWithLifecycle()

    val testsList = viewModel.repository.getTests().filter { it.testType == activeTab }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.getString("practice_mode", lang), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("practice_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                // Tab Selection Layout
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("chapter_test" to "chapter_tests", "mock_test" to "mock_tests").forEach { (tabCode, locKey) ->
                        val isSelected = activeTab == tabCode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .background(
                                    color = if (isSelected) PurpleGradStart else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { activeTab = tabCode }
                                .testTag("practice_tab_$tabCode"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = Localization.getString(locKey, lang),
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) PureWhite else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                // Tests List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(testsList) { test ->
                        val isUnlocked = test.isFree || unlockedTests.any { it.testId == test.id }
                        val attemptHistory = scores.filter { it.testId == test.id }
                        val isAttempted = attemptHistory.isNotEmpty()
                        val maxScore = attemptHistory.maxOfOrNull { it.score } ?: 0

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, shape = RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = when (test.difficulty) {
                                                    "Easy" -> SuccessColor.copy(alpha = 0.1f)
                                                    "Medium" -> WarningColor.copy(alpha = 0.1f)
                                                    else -> ErrorColor.copy(alpha = 0.1f)
                                                },
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = test.difficulty,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (test.difficulty) {
                                                "Easy" -> SuccessColor
                                                "Medium" -> WarningColor
                                                else -> ErrorColor
                                            }
                                        )
                                    }

                                    // Attempt status badge
                                    if (isAttempted) {
                                        Box(
                                            modifier = Modifier
                                                .background(SuccessColor.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = Localization.getString("attempted", lang),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SuccessColor
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (lang == "hi") test.titleHi else test.titleEn,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        text = "${test.questionCount} Questions",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = "${test.totalMarks} Marks",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = "${test.durationMins} Mins",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                }

                                if (isAttempted) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${Localization.getString("high_score", lang)} $maxScore / ${test.totalMarks}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = PurpleGradStart
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Main attempt trigger button
                                Button(
                                    onClick = {
                                        if (isUnlocked) {
                                            viewModel.startTest(test)
                                            onNavigateToTest(test.id)
                                        } else {
                                            showUnlockDialogByTestId = test.id
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("attempt_test_btn_${test.id}"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = if (isUnlocked) PurpleGradStart else WarningColor)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isUnlocked) Icons.Default.PlayArrow else Icons.Default.Lock,
                                            contentDescription = "start",
                                            tint = PureWhite
                                        )
                                        Text(
                                            text = if (isUnlocked) {
                                                if (isAttempted) Localization.getString("reattempt", lang) else "Start Test"
                                            } else {
                                                "Unlock Test"
                                            },
                                            color = PureWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Reward Test Unlocking dialog
            showUnlockDialogByTestId?.let { testId ->
                AlertDialog(
                    onDismissRequest = { if (!isSimulatingAdPlayback) showUnlockDialogByTestId = null },
                    title = { Text("Unlock Premium Officer Exam") },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(Localization.getString("locked_test_desc", lang))
                            Text(
                                text = "${Localization.getString("skips_remaining", lang)}${2 - remainingSkips}",
                                fontSize = 11.sp,
                                color = PurpleGradStart,
                                fontWeight = FontWeight.Bold
                            )
                            if (isSimulatingAdPlayback) {
                                CircularProgressIndicator(color = PurpleGradStart)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Playing interactive Ad segment...", fontSize = 12.sp, color = PurpleGradStart)
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                isSimulatingAdPlayback = true
                                coroutineScope.launch {
                                    delay(2000) // Sim ad wait
                                    viewModel.unlockTestByReward(testId)
                                    isSimulatingAdPlayback = false
                                    showUnlockDialogByTestId = null
                                    Toast.makeText(context, "Test Unlocked Permanently!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart),
                            enabled = !isSimulatingAdPlayback
                        ) {
                            Text(Localization.getString("unlock_btn", lang), color = PureWhite)
                        }
                    },
                    dismissButton = {
                        Row {
                            // Local Bypass Ad skip trigger (User can skip max twice lifetime)
                            TextButton(
                                onClick = {
                                    coroutineScope.launch {
                                        val skipped = viewModel.trySkipAdWithLifetimeSkip()
                                        if (skipped) {
                                            viewModel.unlockTestByReward(testId)
                                            showUnlockDialogByTestId = null
                                            Toast.makeText(context, "Ad Skipped! (Remaining skips updated)", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, Localization.getString("no_skips_left", lang), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                enabled = !isSimulatingAdPlayback
                            ) {
                                Text("Bypass (Skip)", color = SuccessColor)
                            }

                            TextButton(
                                onClick = { showUnlockDialogByTestId = null },
                                enabled = !isSimulatingAdPlayback
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                )
            }
        }
    }
}

// ----------------- TEST SERIES INTERFACE SCREEN -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestInterfaceScreen(
    testId: String,
    viewModel: AppViewModel,
    onNavigateToResult: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lang by viewModel.preferredLanguage.collectAsStateWithLifecycle()
    val testInfo by viewModel.activeTestInfo.collectAsStateWithLifecycle()
    val questions by viewModel.activeQuestions.collectAsStateWithLifecycle()
    val activeQuestionIdx by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
    val selectedAnswers by viewModel.selectedAnswers.collectAsStateWithLifecycle()
    val flaggedForReview by viewModel.flaggedForReview.collectAsStateWithLifecycle()
    val timerSec by viewModel.testTimerSec.collectAsStateWithLifecycle()

    var showSubmitConfirmation by remember { mutableStateOf(false) }

    val questionCount = questions.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (lang == "hi") testInfo?.titleHi ?: "" else testInfo?.titleEn ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
                    }
                },
                actions = {
                    // Test Series Countdown Timer
                    val mins = timerSec / 60
                    val secs = timerSec % 60
                    Row(
                        modifier = Modifier
                            .background(ErrorColor.copy(alpha = 0.12f), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.HourglassEmpty, contentDescription = "Timer", tint = ErrorColor, modifier = Modifier.size(16.dp))
                        Text(
                            text = String.format("%02d:%02d", mins, secs),
                            color = ErrorColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Bookmark question
                    IconButton(onClick = {
                        if (questions.isNotEmpty()) {
                            val activeQ = questions[activeQuestionIdx]
                            coroutineScope.launch {
                                viewModel.repository.saveBookmark(
                                    id = "q_${activeQ.id}",
                                    type = "question",
                                    itemId = activeQ.id,
                                    titleEn = activeQ.textEn,
                                    titleHi = activeQ.textHi,
                                    subtitleEn = "Question in Test ${testInfo?.titleEn}",
                                    subtitleHi = "टेस्ट का प्रश्न ${testInfo?.titleHi}"
                                )
                                Toast.makeText(context, "Question Bookmarked!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = "Bookmark")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            // Palette panel & navigation footer
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Slidable horizontal Question palette circles
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(questionCount) { idx ->
                            val isSelected = activeQuestionIdx == idx
                            val answersSelected = selectedAnswers[idx] ?: emptyList()
                            val isFlagged = flaggedForReview.contains(idx)

                            val bgCircle = when {
                                isSelected -> PurpleGradStart
                                isFlagged -> WarningColor
                                answersSelected.isNotEmpty() -> SuccessColor
                                else -> Color.LightGray.copy(alpha = 0.5f)
                            }

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(bgCircle, shape = CircleShape)
                                    .clickable { viewModel.navigateToQuestion(idx) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    color = if (isSelected || answersSelected.isNotEmpty() || isFlagged) PureWhite else SlateGrey,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Lower Nav Actions (Prev, Flag Review, Next/Submit)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { viewModel.navigateToQuestion(activeQuestionIdx - 1) },
                            enabled = activeQuestionIdx > 0
                        ) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "prev")
                            Text("Prev")
                        }

                        // Flag for Review toggle
                        IconButton(
                            onClick = { viewModel.toggleFlagForReview(activeQuestionIdx) },
                            modifier = Modifier.background(
                                color = if (flaggedForReview.contains(activeQuestionIdx)) WarningColor.copy(alpha = 0.2f) else Color.Transparent,
                                shape = CircleShape
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.OutlinedFlag,
                                contentDescription = "Review Flag",
                                tint = if (flaggedForReview.contains(activeQuestionIdx)) WarningColor else MaterialTheme.colorScheme.onBackground
                            )
                        }

                        if (activeQuestionIdx == questionCount - 1) {
                            Button(
                                onClick = { showSubmitConfirmation = true },
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessColor),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("submit_test_btn")
                            ) {
                                Text("Submit", color = PureWhite, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = { viewModel.navigateToQuestion(activeQuestionIdx + 1) },
                                colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Next", color = PureWhite, fontWeight = FontWeight.Bold)
                                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "next", tint = PureWhite)
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            if (questions.isNotEmpty()) {
                val currentQuestion = questions[activeQuestionIdx]
                val selectedList = selectedAnswers[activeQuestionIdx] ?: emptyList()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Question text Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Question ${activeQuestionIdx + 1} of $questionCount",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PurpleGradStart,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Type: ${currentQuestion.type.name.replace("_", " ")}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (lang == "hi") currentQuestion.textHi else currentQuestion.textEn,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Options list
                    val options = if (lang == "hi") currentQuestion.optionsHi else currentQuestion.optionsEn
                    options.forEachIndexed { optIdx, optionText ->
                        val isSelected = selectedList.contains(optIdx)
                        val isMultiChoice = currentQuestion.type == QuestionType.MULTIPLE_CHOICE

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) PurpleGradStart else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .background(
                                    color = if (isSelected) PurpleGradStart.copy(alpha = 0.08f) else Color.Transparent,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable {
                                    viewModel.selectAnswer(activeQuestionIdx, optIdx, isMultiChoice)
                                }
                                .padding(16.dp)
                                .testTag("option_$optIdx"),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            color = if (isSelected) PurpleGradStart else Color.Transparent,
                                            shape = if (isMultiChoice) RoundedCornerShape(4.dp) else CircleShape
                                        )
                                        .border(
                                            width = if (isSelected) 0.dp else 1.dp,
                                            color = Color.LightGray,
                                            shape = if (isMultiChoice) RoundedCornerShape(4.dp) else CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "checked",
                                            tint = PureWhite,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // Submit Exam validation warning dialog
            if (showSubmitConfirmation) {
                AlertDialog(
                    onDismissRequest = { showSubmitConfirmation = false },
                    title = { Text("Submit Officer Examination?") },
                    text = { Text("All your selected answers will be sealed and submitted for instant performance estimation analytics.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showSubmitConfirmation = false
                                viewModel.submitTest()
                                onNavigateToResult()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessColor)
                        ) {
                            Text("Confirm Submit", color = PureWhite)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showSubmitConfirmation = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

// ----------------- RESULT ANALYSIS SCREEN -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: AppViewModel,
    onNavigateHome: () -> Unit,
    onRetryTest: (String) -> Unit
) {
    val lang by viewModel.preferredLanguage.collectAsStateWithLifecycle()
    val isPreparing by viewModel.isPreparingResult.collectAsStateWithLifecycle()
    val result by viewModel.lastTestResult.collectAsStateWithLifecycle()

    var showExplsPanel by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Officer Score Assessment", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateHome) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            if (isPreparing) {
                // Assessment loader screen with premium micro animation
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = PurpleGradStart, strokeWidth = 5.dp, modifier = Modifier.size(60.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = Localization.getString("preparing_result", lang),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PurpleGradStart
                    )
                }
            } else {
                result?.let { r ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Summary Hero Ring Banner
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = PurpleGradStart)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (lang == "hi") r.testNameHi else r.testNameEn,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // Ring score metric
                                Box(contentAlignment = Alignment.Center) {
                                    val pct = if (r.totalQuestions > 0) r.correctCount.toFloat() / r.totalQuestions.toFloat() else 0f
                                    Canvas(modifier = Modifier.size(100.dp)) {
                                        drawCircle(color = PureWhite.copy(alpha = 0.15f), style = Stroke(width = 12f))
                                        drawArc(
                                            color = PureWhite,
                                            startAngle = -90f,
                                            sweepAngle = pct * 360f,
                                            useCenter = false,
                                            style = Stroke(width = 12f)
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${r.score}",
                                            style = MaterialTheme.typography.displayLarge,
                                            color = PureWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Marks",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = PureWhite.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }

                        // Grid metrics (Correct, Wrong, Skipped, Accuracy)
                        val accPct = if (r.correctCount + r.incorrectCount > 0) {
                            ((r.correctCount.toFloat() / (r.correctCount + r.incorrectCount).toFloat()) * 100).roundToInt()
                        } else {
                            0
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricBox(
                                title = Localization.getString("correct", lang),
                                valText = "${r.correctCount} ✅",
                                col = SuccessColor,
                                modifier = Modifier.weight(1f)
                            )
                            MetricBox(
                                title = Localization.getString("incorrect", lang),
                                valText = "${r.incorrectCount} ❌",
                                col = ErrorColor,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricBox(
                                title = Localization.getString("skipped", lang),
                                valText = "${r.skippedCount} ⚪",
                                col = Color.Gray,
                                modifier = Modifier.weight(1f)
                            )
                            MetricBox(
                                title = Localization.getString("accuracy", lang),
                                valText = "$accPct% 🎯",
                                col = PurpleGradStart,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Detailed Performance Index list
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Performance Analytics Index", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Est. RPSC Percentile", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                                    Text("${(accPct * 0.95 + 5).roundToInt()}th Percentile", fontWeight = FontWeight.Bold)
                                }

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Average Speed", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                                    Text("${r.timeTakenSec / r.totalQuestions} sec / Q", fontWeight = FontWeight.Bold)
                                }

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Recommended Path", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                                    Text(
                                        text = if (accPct > 70) "Review Hard Topics" else "Re-read Chapters A1, B1",
                                        fontWeight = FontWeight.Bold,
                                        color = if (accPct > 70) SuccessColor else WarningColor
                                    )
                                }
                            }
                        }

                        // Recommendations & Explanations Panel
                        Button(
                            onClick = { showExplsPanel = !showExplsPanel },
                            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("review_ans_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Menu, contentDescription = "menu")
                                Text(Localization.getString("review_answers", lang), color = PureWhite, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (showExplsPanel) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                val testQs = viewModel.repository.getQuestionsForTest(r.testId)
                                testQs.forEachIndexed { index, q ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text("Q${index + 1}: ${if (lang == "hi") q.textHi else q.textEn}", fontWeight = FontWeight.Bold)
                                            val correctOpt = if (lang == "hi") q.optionsHi[q.correctAnswerIndex] else q.optionsEn[q.correctAnswerIndex]
                                            Text("Correct Answer: $correctOpt", color = SuccessColor, fontWeight = FontWeight.Bold)
                                            Text("Explanations:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            Text(if (lang == "hi") q.explanationHi else q.explanationEn, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom Actions row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { onRetryTest(r.testId) },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart)
                            ) {
                                Text(Localization.getString("retry", lang), color = PureWhite, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = onNavigateHome,
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onBackground)
                            ) {
                                Text("Home Dashboard", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricBox(title: String, valText: String, col: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.shadow(2.dp, shape = RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(valText, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = col)
        }
    }
}

// ----------------- FLASHCARDS INTERFACE SCREEN (SWIPE) -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lang by viewModel.preferredLanguage.collectAsStateWithLifecycle()
    val flashcards by viewModel.flashcards.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("all") } // "all", "favorite"
    var activeCardIdx by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    // Dialog state for adding custom cards
    var showAddCustomCardDialog by remember { mutableStateOf(false) }
    var customFrontEn by remember { mutableStateOf("") }
    var customFrontHi by remember { mutableStateOf("") }
    var customBackEn by remember { mutableStateOf("") }
    var customBackHi by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("Custom") }

    val filteredCards = flashcards.filter {
        activeTab == "all" || it.isFavorite
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.getString("flashcards", lang), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("flashcard_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Quick add custom card
                    IconButton(onClick = { showAddCustomCardDialog = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add custom card")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tab select
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("all" to "recent", "favorite" to "favorites").forEach { (tabCode, locKey) ->
                        val isSelected = activeTab == tabCode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(
                                    color = if (isSelected) PurpleGradStart else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    activeTab = tabCode
                                    activeCardIdx = 0
                                    isFlipped = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = Localization.getString(locKey, lang),
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) PureWhite else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                if (filteredCards.isNotEmpty()) {
                    val card = filteredCards[activeCardIdx.coerceIn(0, filteredCards.size - 1)]

                    // Swipe Card UI Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 12.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .shadow(6.dp, shape = RoundedCornerShape(24.dp))
                                .clickable { isFlipped = !isFlipped }
                                .testTag("flashcard_item"),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isFlipped) SuccessColor.copy(alpha = 0.05f) else PurpleGradStart.copy(alpha = 0.05f)
                            ),
                            border = BorderStroke(1.5.dp, if (isFlipped) SuccessColor else PurpleGradStart)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Category Header & favorite flag button
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(PurpleGradStart.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(card.category, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PurpleGradStart)
                                    }

                                    IconButton(onClick = { viewModel.toggleFlashcardFavorite(card) }) {
                                        Icon(
                                            imageVector = if (card.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "fav",
                                            tint = if (card.isFavorite) ErrorColor else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                        )
                                    }
                                }

                                // Interactive Text Core (Front / Back)
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = if (isFlipped) "ANSWER / उत्तर" else "QUESTION / प्रश्न",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isFlipped) SuccessColor else PurpleGradStart,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = if (isFlipped) {
                                            if (lang == "hi") card.backHi else card.backEn
                                        } else {
                                            if (lang == "hi") card.frontHi else card.frontEn
                                        },
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                // Action tip
                                Text(
                                    text = if (isFlipped) "Tap card to see the question again" else Localization.getString("show_answer", lang),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Swipe / Next Controllers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                isFlipped = false
                                activeCardIdx = (activeCardIdx + 1) % filteredCards.size
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Text(Localization.getString("next_card", lang), color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = "Card ${activeCardIdx + 1} of ${filteredCards.size}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Style, contentDescription = "empty", modifier = Modifier.size(60.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(Localization.getString("flashcard_empty", lang), textAlign = TextAlign.Center, color = Color.Gray)
                    }
                }
            }

            // Dialog for adding custom cards
            if (showAddCustomCardDialog) {
                AlertDialog(
                    onDismissRequest = { showAddCustomCardDialog = false },
                    title = { Text("Build Custom Study Flashcard") },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            OutlinedTextField(
                                value = customFrontEn,
                                onValueChange = { customFrontEn = it },
                                label = { Text("Front Text (English)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = customFrontHi,
                                onValueChange = { customFrontHi = it },
                                label = { Text("Front Text (Hindi)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = customBackEn,
                                onValueChange = { customBackEn = it },
                                label = { Text("Back Text (English)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = customBackHi,
                                onValueChange = { customBackHi = it },
                                label = { Text("Back Text (Hindi)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = customCategory,
                                onValueChange = { customCategory = it },
                                label = { Text("Topic Category") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (customFrontEn.isEmpty() || customBackEn.isEmpty()) {
                                    Toast.makeText(context, "English Front/Back are mandatory!", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.addCustomFlashcard(
                                        frontEn = customFrontEn,
                                        frontHi = if (customFrontHi.isEmpty()) customFrontEn else customFrontHi,
                                        backEn = customBackEn,
                                        backHi = if (customBackHi.isEmpty()) customBackEn else customBackHi,
                                        category = customCategory
                                    )
                                    showAddCustomCardDialog = false
                                    customFrontEn = ""
                                    customFrontHi = ""
                                    customBackEn = ""
                                    customBackHi = ""
                                    Toast.makeText(context, "Custom Flashcard Saved!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart)
                        ) {
                            Text("Save Card", color = PureWhite)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddCustomCardDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

// ----------------- CMS MANAGER SCREEN -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CMSManagerScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val lang by viewModel.preferredLanguage.collectAsStateWithLifecycle()
    var isSyncing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.getString("cms_title", lang), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("cms_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(PurpleGradStart.copy(alpha = 0.1f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "sync logo",
                            tint = PurpleGradStart,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    Text(
                        text = "RPSC CMS Update Center",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = Localization.getString("cms_info", lang),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Database Sync State Details", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Active Engine", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                                Text("Room Cache + Firestore", fontWeight = FontWeight.Bold)
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Curated Books Cached", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                                Text("4 Volumes", fontWeight = FontWeight.Bold)
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Practice Tests Synced", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                                Text("6 Papers", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = PurpleGradStart)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            isSyncing = true
                            coroutineScope.launch {
                                delay(3000) // Simulate fast network sync
                                isSyncing = false
                                Toast.makeText(viewModel.getApplication(), "Firestore content synced. 0 updates found.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleGradStart),
                        enabled = !isSyncing
                    ) {
                        Text(if (isSyncing) "Synchronizing Materials..." else "Force Database Refresh", fontWeight = FontWeight.Bold, color = PureWhite)
                    }

                    Button(
                        onClick = {
                            viewModel.resetProgress()
                            Toast.makeText(viewModel.getApplication(), "All progress reset locally", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
                    ) {
                        Text("Reset All Cache & Progress", fontWeight = FontWeight.Bold, color = PureWhite)
                    }
                }
            }
        }
    }
}
