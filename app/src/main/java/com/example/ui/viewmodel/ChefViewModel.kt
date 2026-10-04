package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundPlayer
import com.example.data.ChefRepository
import com.example.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppDestination(val label: String, val icon: String) {
  HOME("Home", "🏠"),
  GAMES("Games", "🎮"),
  PRACTICE("Practice", "📚"),
  ABOUT("About", "ℹ️")
}

data class RewardState(
  val visible: Boolean = false,
  val title: String = "Great Job, Chef!",
  val stars: Int = 15,
  val xp: Int = 30,
  val badgeName: String? = null,
  val badgeEmoji: String? = null,
  val onPlayAgain: () -> Unit = {},
  val onBackToGames: () -> Unit = {}
)

class ChefViewModel(application: Application) : AndroidViewModel(application) {

  val repository = ChefRepository(application)
  val soundPlayer = SoundPlayer(application)

  val profile: StateFlow<ChefProfile> = repository.profile

  private val _currentDestination = MutableStateFlow(AppDestination.HOME)
  val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

  // Sub-routes (games, lessons, parent area, etc.)
  private val _activeRoute = MutableStateFlow<String?>(null)
  val activeRoute: StateFlow<String?> = _activeRoute.asStateFlow()

  // Selected game category filter in Games screen
  private val _selectedCategory = MutableStateFlow(GameCategory.ALL)
  val selectedCategory: StateFlow<GameCategory> = _selectedCategory.asStateFlow()

  // Search query in Games screen
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  // Reward completion popup state
  private val _rewardState = MutableStateFlow(RewardState())
  val rewardState: StateFlow<RewardState> = _rewardState.asStateFlow()

  // Selected Lesson for detail view
  private val _selectedLesson = MutableStateFlow<Lesson?>(null)
  val selectedLesson: StateFlow<Lesson?> = _selectedLesson.asStateFlow()

  // Filtered games
  val filteredGames: StateFlow<List<GameItem>> = combine(
    _selectedCategory,
    _searchQuery
  ) { category, query ->
    repository.allGames.filter { game ->
      val matchesCategory = (category == GameCategory.ALL) || (game.category == category)
      val matchesQuery = query.isBlank() ||
        game.name.contains(query, ignoreCase = true) ||
        game.description.contains(query, ignoreCase = true)
      matchesCategory && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.allGames)

  fun setDestination(dest: AppDestination) {
    playClick()
    _currentDestination.value = dest
    _activeRoute.value = null
  }

  fun navigateToRoute(route: String) {
    playClick()
    _activeRoute.value = route
  }

  fun navigateBack() {
    playClick()
    _activeRoute.value = null
    _selectedLesson.value = null
  }

  fun selectCategory(category: GameCategory) {
    playClick()
    _selectedCategory.value = category
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun selectLesson(lesson: Lesson) {
    playClick()
    _selectedLesson.value = lesson
    _activeRoute.value = "lesson_detail"
  }

  fun completeGame(stars: Int, xp: Int, gameName: String, playAgainAction: () -> Unit) {
    soundPlayer.playSuccess(profile.value.soundEnabled)
    soundPlayer.triggerHaptic(profile.value.hapticsEnabled)
    repository.addReward(stars, xp, gameName)

    _rewardState.value = RewardState(
      visible = true,
      title = "Delicious, Chef!",
      stars = stars,
      xp = xp,
      badgeName = if (profile.value.completedGamesCount % 3 == 0) "Culinary Star!" else null,
      badgeEmoji = "🎖",
      onPlayAgain = {
        _rewardState.value = RewardState(visible = false)
        playAgainAction()
      },
      onBackToGames = {
        _rewardState.value = RewardState(visible = false)
        _activeRoute.value = null
        _currentDestination.value = AppDestination.GAMES
      }
    )
  }

  fun dismissReward() {
    _rewardState.value = RewardState(visible = false)
  }

  fun completeLesson(lesson: Lesson) {
    soundPlayer.playSuccess(profile.value.soundEnabled)
    repository.completeLesson(lesson.id, lesson.xpReward)
    _selectedLesson.value = null
    _activeRoute.value = null
  }

  fun completeDailyChallenge() {
    soundPlayer.playStar(profile.value.soundEnabled)
    repository.completeDailyChallenge()
  }

  fun toggleSound(enabled: Boolean) {
    repository.updateSettings(sound = enabled)
  }

  fun toggleMusic(enabled: Boolean) {
    repository.updateSettings(music = enabled)
  }

  fun toggleHaptics(enabled: Boolean) {
    repository.updateSettings(haptics = enabled)
  }

  fun completeOnboarding() {
    playClick()
    repository.markOnboardingCompleted()
  }

  fun updateChefName(name: String) {
    playClick()
    repository.updateChefName(name)
  }

  fun resetAllProgress() {
    repository.resetProgress()
    _activeRoute.value = null
    _currentDestination.value = AppDestination.HOME
  }

  fun playClick() {
    soundPlayer.playClick(profile.value.soundEnabled)
    soundPlayer.triggerHaptic(profile.value.hapticsEnabled)
  }

  fun playSizzle() {
    soundPlayer.playCookSizzle(profile.value.soundEnabled)
  }

  fun playBlend() {
    soundPlayer.playBlend(profile.value.soundEnabled)
  }

  fun playStar() {
    soundPlayer.playStar(profile.value.soundEnabled)
  }
}
