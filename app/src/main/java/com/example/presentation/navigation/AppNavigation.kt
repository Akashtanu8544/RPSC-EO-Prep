package com.example.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.presentation.viewmodel.AppViewModel
import com.example.presentation.ui.*

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val READ_MODE = "read_mode"
    const val READER = "reader/{chapterId}"
    const val PRACTICE_MODE = "practice_mode"
    const val TEST_INTERFACE = "test_interface/{testId}"
    const val RESULT_SCREEN = "result_screen"
    const val FLASHCARDS = "flashcards"
    const val CMS_MANAGER = "cms_manager"

    fun readerRoute(chapterId: String) = "reader/$chapterId"
    fun testInterfaceRoute(testId: String) = "test_interface/$testId"
}

@Composable
fun AppNavigation(
    viewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
) {
    val startDestination = if (viewModel.onboardingCompleted.value) Routes.HOME else Routes.ONBOARDING

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.95f, animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(250)) + scaleOut(targetScale = 0.95f, animationSpec = tween(250)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.95f, animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(250)) + scaleOut(targetScale = 0.95f, animationSpec = tween(250)) }
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(viewModel = viewModel, onNavigateToHome = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            })
        }

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToRead = { navController.navigate(Routes.READ_MODE) },
                onNavigateToPractice = { navController.navigate(Routes.PRACTICE_MODE) },
                onNavigateToFlashcards = { navController.navigate(Routes.FLASHCARDS) },
                onNavigateToCMS = { navController.navigate(Routes.CMS_MANAGER) }
            )
        }

        composable(Routes.READ_MODE) {
            ReadModeScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToReader = { chapterId ->
                    navController.navigate(Routes.readerRoute(chapterId))
                }
            )
        }

        composable(
            route = Routes.READER,
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: ""
            ReaderScreen(
                chapterId = chapterId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PRACTICE_MODE) {
            PracticeModeScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTest = { testId ->
                    navController.navigate(Routes.testInterfaceRoute(testId))
                }
            )
        }

        composable(
            route = Routes.TEST_INTERFACE,
            arguments = listOf(navArgument("testId") { type = NavType.StringType })
        ) { backStackEntry ->
            val testId = backStackEntry.arguments?.getString("testId") ?: ""
            TestInterfaceScreen(
                testId = testId,
                viewModel = viewModel,
                onNavigateToResult = {
                    navController.navigate(Routes.RESULT_SCREEN) {
                        popUpTo(Routes.TEST_INTERFACE) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.RESULT_SCREEN) {
            ResultScreen(
                viewModel = viewModel,
                onNavigateHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                onRetryTest = { testId ->
                    navController.navigate(Routes.testInterfaceRoute(testId)) {
                        popUpTo(Routes.RESULT_SCREEN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.FLASHCARDS) {
            FlashcardsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CMS_MANAGER) {
            CMSManagerScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
