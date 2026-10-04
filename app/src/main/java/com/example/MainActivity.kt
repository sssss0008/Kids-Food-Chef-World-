package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.components.*
import com.example.ui.games.*
import com.example.ui.learning.*
import com.example.ui.screens.*
import com.example.ui.theme.KidsFoodChefTheme
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.ChefViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val viewModel: ChefViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      KidsFoodChefTheme {
        KidsFoodChefApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun KidsFoodChefApp(viewModel: ChefViewModel) {
  val profile by viewModel.profile.collectAsState()
  val currentDestination by viewModel.currentDestination.collectAsState()
  val activeRoute by viewModel.activeRoute.collectAsState()
  val rewardState by viewModel.rewardState.collectAsState()
  val selectedLesson by viewModel.selectedLesson.collectAsState()

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  // Onboarding overlay for first-time launch
  if (!profile.hasSeenOnboarding) {
    OnboardingScreen(onComplete = { viewModel.completeOnboarding() })
    return
  }

  // Handle system back when in subroute or drawer
  BackHandler(enabled = drawerState.isOpen || activeRoute != null) {
    if (drawerState.isOpen) {
      scope.launch { drawerState.close() }
    } else if (activeRoute != null) {
      viewModel.navigateBack()
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ChefDrawerContent(
        profile = profile,
        onNavigateToRoute = { route ->
          when (route) {
            "games_tab" -> viewModel.setDestination(AppDestination.GAMES)
            "about_tab" -> viewModel.setDestination(AppDestination.ABOUT)
            else -> viewModel.navigateToRoute(route)
          }
        },
        onSelectCategory = { cat ->
          viewModel.setDestination(AppDestination.GAMES)
          viewModel.selectCategory(cat)
        },
        onCloseDrawer = {
          scope.launch { drawerState.close() }
        }
      )
    }
  ) {
    Scaffold(
      topBar = {
        if (activeRoute == null) {
          ChefTopBar(
            profile = profile,
            onMenuClick = {
              scope.launch { drawerState.open() }
            },
            onProfileClick = {
              viewModel.navigateToRoute("progress_screen")
            },
            title = when (currentDestination) {
              AppDestination.HOME -> "Kids Food Chef"
              AppDestination.GAMES -> "Cooking Games"
              AppDestination.PRACTICE -> "Academy & Practice"
              AppDestination.ABOUT -> "About Kids Chef"
            }
          )
        }
      },
      bottomBar = {
        // Bottom nav is shown on the 4 primary tabs
        if (activeRoute == null) {
          ChefBottomNav(
            currentDestination = currentDestination,
            onDestinationSelected = { dest ->
              viewModel.setDestination(dest)
            }
          )
        }
      },
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(
            top = if (activeRoute == null) innerPadding.calculateTopPadding() else androidx.compose.ui.unit.Dp(0f),
            bottom = if (activeRoute == null) innerPadding.calculateBottomPadding() else androidx.compose.ui.unit.Dp(0f)
          )
      ) {
        // Active Sub-Route Content
        if (activeRoute != null) {
          when (activeRoute) {
            "game_pizza" -> PizzaMakerGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_burger" -> BurgerBuilderGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_cake" -> CakeDecoratingGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_juice" -> FruitJuiceMakerGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_lunchbox" -> LunchBoxCreatorGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_icecream" -> IceCreamShopGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_sandwich" -> SandwichMakerGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_cookie" -> CookieBakeryGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_sorting" -> FoodSortingGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_healthy_meal" -> HealthyMealBuilderGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_restaurant" -> RestaurantManagerGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_memory" -> FoodMemoryScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "game_matching" -> FoodMatchingScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "explore_fruits" -> FoodExplorerScreen(mode = "fruits", viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "explore_veggies" -> FoodExplorerScreen(mode = "veggies", viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "explore_tools" -> FoodExplorerScreen(mode = "tools", viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "learning_safety" -> KitchenSafetyScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "learning_academy" -> {
              // Redirect to Practice tab with Academy
              LaunchedEffect(Unit) {
                viewModel.setDestination(AppDestination.PRACTICE)
                viewModel.navigateBack()
              }
            }
            "lesson_detail" -> {
              if (selectedLesson != null) {
                CookingAcademyLessonScreen(
                  lesson = selectedLesson!!,
                  viewModel = viewModel,
                  onBack = { viewModel.navigateBack() }
                )
              }
            }
            "cooking_quiz" -> CookingQuizScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "daily_challenge" -> LunchBoxCreatorGame(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "progress_screen" -> ProgressScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            "parent_area", "settings_screen" -> ParentAreaScreen(viewModel = viewModel, onBack = { viewModel.navigateBack() })
            else -> {
              LaunchedEffect(Unit) { viewModel.navigateBack() }
            }
          }
        } else {
          // Primary 4 Tabs
          when (currentDestination) {
            AppDestination.HOME -> {
              HomeScreen(
                viewModel = viewModel,
                profile = profile,
                onStartCooking = { viewModel.setDestination(AppDestination.GAMES) },
                onNavigateToGame = { route -> viewModel.navigateToRoute(route) },
                onNavigateToRoute = { route -> viewModel.navigateToRoute(route) }
              )
            }
            AppDestination.GAMES -> {
              GamesScreen(
                viewModel = viewModel,
                onNavigateToGame = { route -> viewModel.navigateToRoute(route) }
              )
            }
            AppDestination.PRACTICE -> {
              PracticeScreen(
                viewModel = viewModel,
                onNavigateToRoute = { route -> viewModel.navigateToRoute(route) }
              )
            }
            AppDestination.ABOUT -> {
              AboutScreen()
            }
          }
        }

        // Global Reward Celebration Dialog
        RewardCompletionDialog(
          visible = rewardState.visible,
          title = rewardState.title,
          stars = rewardState.stars,
          xp = rewardState.xp,
          badgeName = rewardState.badgeName,
          badgeEmoji = rewardState.badgeEmoji,
          onPlayAgain = rewardState.onPlayAgain,
          onBackToGames = rewardState.onBackToGames,
          onDismiss = { viewModel.dismissReward() }
        )
      }
    }
  }
}
