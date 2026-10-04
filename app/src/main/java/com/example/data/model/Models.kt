package com.example.data.model

enum class ChefLevel(
  val title: String,
  val minXp: Int,
  val maxXp: Int,
  val badgeEmoji: String,
  val description: String
) {
  JUNIOR_CHEF("Junior Chef", 0, 100, "🍳", "Just started the cooking journey!"),
  KITCHEN_HELPER("Kitchen Helper", 101, 300, "🥄", "Knows their way around the kitchen!"),
  FOOD_CREATOR("Food Creator", 301, 600, "🎨", "Inventing tasty & colorful dishes!"),
  MASTER_CHEF("Master Chef", 601, 1000, "⭐", "A true cooking champion!"),
  SUPER_CHEF("Super Chef", 1001, 2000, "👑", "Legendary world-class kid chef!");

  companion object {
    fun fromXp(xp: Int): ChefLevel {
      return entries.lastOrNull { xp >= it.minXp } ?: JUNIOR_CHEF
    }
  }
}

enum class GameCategory(val displayName: String, val iconEmoji: String) {
  ALL("All", "🌟"),
  COOKING("Cooking", "🍳"),
  CREATIVE("Creative", "🎨"),
  HEALTHY("Healthy", "🥗"),
  LEARNING("Learning", "📚"),
  MEMORY("Memory", "🧠"),
  MATCHING("Matching", "🃏")
}

data class GameItem(
  val id: String,
  val name: String,
  val description: String,
  val category: GameCategory,
  val difficulty: String,
  val starsReward: Int,
  val xpReward: Int,
  val iconEmoji: String,
  val colorHex: Long,
  val route: String
)

data class Lesson(
  val id: String,
  val lessonNumber: Int,
  val title: String,
  val subtitle: String,
  val category: String, // Academy, Healthy, Safety
  val iconEmoji: String,
  val xpReward: Int,
  val tips: List<String>,
  val quizQuestion: String,
  val quizOptions: List<String>,
  val correctOptionIndex: Int,
  val isCompleted: Boolean = false
)

data class Achievement(
  val id: String,
  val title: String,
  val description: String,
  val iconEmoji: String,
  val xpReward: Int,
  val starsReward: Int,
  val isUnlocked: Boolean = false
)

data class DailyChallenge(
  val id: String,
  val title: String,
  val description: String,
  val rewardStars: Int,
  val rewardXp: Int,
  val targetGameRoute: String,
  val isCompleted: Boolean = false
)

data class FoodCardItem(
  val id: String,
  val name: String,
  val category: String, // Fruit, Vegetable, Grain, Protein, Treat
  val color: String,
  val funFact: String,
  val emoji: String,
  val vitamins: String
)

data class KitchenToolItem(
  val id: String,
  val name: String,
  val purpose: String,
  val funFact: String,
  val emoji: String,
  val safetyTip: String
)

data class QuizQuestion(
  val id: Int,
  val question: String,
  val options: List<String>,
  val correctIndex: Int,
  val hint: String,
  val funFact: String,
  val emoji: String
)

data class ChefProfile(
  val name: String = "Little Chef Leo",
  val xp: Int = 85,
  val stars: Int = 45,
  val coins: Int = 120,
  val completedGamesCount: Int = 4,
  val completedLessonsCount: Int = 2,
  val lastPlayedGameName: String = "Pizza Maker",
  val lastPlayedGameProgress: String = "3 / 5 steps",
  val unlockedAchievementIds: Set<String> = setOf("first_recipe", "healthy_chef"),
  val completedLessonIds: Set<String> = setOf("lesson_1"),
  val dailyChallengeCompleted: Boolean = false,
  val soundEnabled: Boolean = true,
  val musicEnabled: Boolean = true,
  val hapticsEnabled: Boolean = true,
  val hasSeenOnboarding: Boolean = true
) {
  val level: ChefLevel
    get() = ChefLevel.fromXp(xp)

  val xpInCurrentLevel: Int
    get() = (xp - level.minXp).coerceAtLeast(0)

  val xpForNextLevel: Int
    get() = (level.maxXp - level.minXp)

  val progressPercent: Float
    get() = if (xpForNextLevel <= 0) 1f else (xpInCurrentLevel.toFloat() / xpForNextLevel).coerceIn(0f, 1f)
}
