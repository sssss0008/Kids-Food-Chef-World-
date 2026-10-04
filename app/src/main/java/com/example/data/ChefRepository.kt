package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChefRepository(context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("kids_food_chef_prefs", Context.MODE_PRIVATE)

  private val _profile = MutableStateFlow(loadProfile())
  val profile: StateFlow<ChefProfile> = _profile.asStateFlow()

  private fun loadProfile(): ChefProfile {
    val name = prefs.getString("chef_name", "Little Chef Leo") ?: "Little Chef Leo"
    val xp = prefs.getInt("chef_xp", 85)
    val stars = prefs.getInt("chef_stars", 45)
    val coins = prefs.getInt("chef_coins", 120)
    val completedGames = prefs.getInt("completed_games", 4)
    val completedLessons = prefs.getInt("completed_lessons", 2)
    val lastGameName = prefs.getString("last_game_name", "Pizza Maker") ?: "Pizza Maker"
    val lastGameProgress = prefs.getString("last_game_progress", "3 / 5 steps") ?: "3 / 5 steps"
    val achievements = prefs.getStringSet("unlocked_achievements", setOf("first_recipe", "healthy_chef")) ?: setOf("first_recipe", "healthy_chef")
    val lessons = prefs.getStringSet("completed_lessons", setOf("lesson_1")) ?: setOf("lesson_1")
    val dailyChallengeCompleted = prefs.getBoolean("daily_completed", false)
    val sound = prefs.getBoolean("sound_enabled", true)
    val music = prefs.getBoolean("music_enabled", true)
    val haptics = prefs.getBoolean("haptics_enabled", true)
    val onboarding = prefs.getBoolean("seen_onboarding", false)

    return ChefProfile(
      name = name,
      xp = xp,
      stars = stars,
      coins = coins,
      completedGamesCount = completedGames,
      completedLessonsCount = completedLessons,
      lastPlayedGameName = lastGameName,
      lastPlayedGameProgress = lastGameProgress,
      unlockedAchievementIds = achievements,
      completedLessonIds = lessons,
      dailyChallengeCompleted = dailyChallengeCompleted,
      soundEnabled = sound,
      musicEnabled = music,
      hapticsEnabled = haptics,
      hasSeenOnboarding = onboarding
    )
  }

  private fun saveProfile(profile: ChefProfile) {
    prefs.edit()
      .putString("chef_name", profile.name)
      .putInt("chef_xp", profile.xp)
      .putInt("chef_stars", profile.stars)
      .putInt("chef_coins", profile.coins)
      .putInt("completed_games", profile.completedGamesCount)
      .putInt("completed_lessons", profile.completedLessonsCount)
      .putString("last_game_name", profile.lastPlayedGameName)
      .putString("last_game_progress", profile.lastPlayedGameProgress)
      .putStringSet("unlocked_achievements", profile.unlockedAchievementIds)
      .putStringSet("completed_lessons", profile.completedLessonIds)
      .putBoolean("daily_completed", profile.dailyChallengeCompleted)
      .putBoolean("sound_enabled", profile.soundEnabled)
      .putBoolean("music_enabled", profile.musicEnabled)
      .putBoolean("haptics_enabled", profile.hapticsEnabled)
      .putBoolean("seen_onboarding", profile.hasSeenOnboarding)
      .apply()
  }

  fun addReward(stars: Int, xp: Int, gameName: String? = null) {
    _profile.update { current ->
      val newXp = current.xp + xp
      val newStars = current.stars + stars
      val newGames = current.completedGamesCount + 1
      val newAchievements = current.unlockedAchievementIds.toMutableSet()

      if (newGames >= 1) newAchievements.add("first_recipe")
      if (newGames >= 5) newAchievements.add("cooking_pro")
      if (newXp >= 300) newAchievements.add("kitchen_helper_star")
      if (newXp >= 600) newAchievements.add("food_creator_master")
      if (newXp >= 1000) newAchievements.add("super_chef")

      val updated = current.copy(
        xp = newXp,
        stars = newStars,
        completedGamesCount = newGames,
        lastPlayedGameName = gameName ?: current.lastPlayedGameName,
        lastPlayedGameProgress = "Completed!",
        unlockedAchievementIds = newAchievements
      )
      saveProfile(updated)
      updated
    }
  }

  fun completeLesson(lessonId: String, xpReward: Int) {
    _profile.update { current ->
      val newLessons = current.completedLessonIds + lessonId
      val newXp = current.xp + xpReward
      val updated = current.copy(
        xp = newXp,
        completedLessonsCount = newLessons.size,
        completedLessonIds = newLessons
      )
      saveProfile(updated)
      updated
    }
  }

  fun completeDailyChallenge() {
    _profile.update { current ->
      if (!current.dailyChallengeCompleted) {
        val updated = current.copy(
          dailyChallengeCompleted = true,
          stars = current.stars + 20,
          xp = current.xp + 35
        )
        saveProfile(updated)
        updated
      } else current
    }
  }

  fun updateSettings(sound: Boolean? = null, music: Boolean? = null, haptics: Boolean? = null) {
    _profile.update { current ->
      val updated = current.copy(
        soundEnabled = sound ?: current.soundEnabled,
        musicEnabled = music ?: current.musicEnabled,
        hapticsEnabled = haptics ?: current.hapticsEnabled
      )
      saveProfile(updated)
      updated
    }
  }

  fun markOnboardingCompleted() {
    _profile.update { current ->
      val updated = current.copy(hasSeenOnboarding = true)
      saveProfile(updated)
      updated
    }
  }

  fun updateChefName(newName: String) {
    _profile.update { current ->
      val updated = current.copy(name = newName.ifBlank { "Little Chef" })
      saveProfile(updated)
      updated
    }
  }

  fun resetProgress() {
    val reset = ChefProfile(hasSeenOnboarding = true)
    saveProfile(reset)
    _profile.value = reset
  }

  // Pre-configured static database items
  val allGames: List<GameItem> = listOf(
    GameItem("pizza_maker", "Pizza Maker", "Roll dough, add sauce, cheese & yummy toppings!", GameCategory.COOKING, "Easy", 15, 30, "🍕", 0xFFFF7043, "game_pizza"),
    GameItem("burger_builder", "Burger Builder", "Stack juicy patties, lettuce, tomato & melted cheese!", GameCategory.COOKING, "Medium", 15, 25, "🍔", 0xFFFFA726, "game_burger"),
    GameItem("cake_decorating", "Cake Decorating", "Design fancy celebration cakes with frosting & candles!", GameCategory.CREATIVE, "Easy", 20, 35, "🎂", 0xFFEC407A, "game_cake"),
    GameItem("fruit_juice_maker", "Fruit Juice Maker", "Slice fresh fruits and blend yummy smoothies!", GameCategory.HEALTHY, "Easy", 15, 25, "🍹", 0xFF26A69A, "game_juice"),
    GameItem("lunch_box_creator", "Lunch Box Creator", "Pack a balanced colorful bento lunch with fruits & veggies!", GameCategory.HEALTHY, "Easy", 20, 30, "🍱", 0xFF66BB6A, "game_lunchbox"),
    GameItem("ice_cream_shop", "Ice Cream Shop", "Scoop delicious ice cream, add sprinkles & cherry!", GameCategory.CREATIVE, "Easy", 15, 25, "🍨", 0xFF42A5F5, "game_icecream"),
    GameItem("sandwich_maker", "Sandwich Maker", "Layer fresh bread, crisp greens, cheese & grill it!", GameCategory.COOKING, "Easy", 15, 25, "🥪", 0xFFFFB74D, "game_sandwich"),
    GameItem("cookie_bakery", "Cookie Bakery", "Cut fun shapes, add chocolate chips and bake!", GameCategory.CREATIVE, "Medium", 20, 30, "🍪", 0xFF8D6E63, "game_cookie"),
    GameItem("food_sorting", "Food Sorting", "Sort fruits, veggies, treats, and kitchen tools!", GameCategory.LEARNING, "Easy", 25, 40, "🧺", 0xFF7E57C2, "game_sorting"),
    GameItem("healthy_meal_builder", "Healthy Meal Builder", "Build a rainbow plate with proteins, grains & vitamins!", GameCategory.HEALTHY, "Medium", 20, 35, "🥗", 0xFF2E7D32, "game_healthy_meal"),
    GameItem("restaurant_manager", "Restaurant Manager", "Seat animal friends, cook special orders and earn stars!", GameCategory.COOKING, "Challenge", 30, 50, "🏪", 0xFFFF9800, "game_restaurant")
  )

  val academyLessons: List<Lesson> = listOf(
    Lesson(
      id = "lesson_1",
      lessonNumber = 1,
      title = "Kitchen Basics & Clean Hands",
      subtitle = "Why washing hands and wearing an apron is chef rule #1!",
      category = "Cooking Academy",
      iconEmoji = "🧼",
      xpReward = 30,
      tips = listOf(
        "Wash your hands with warm water and soap for 20 seconds before touching food!",
        "Sing the 'Happy Birthday' song twice while washing your hands.",
        "Put on a clean apron to protect your clothes from splashes.",
        "Tie long hair back so it stays clean and out of the food."
      ),
      quizQuestion = "How long should you wash your hands before cooking?",
      quizOptions = listOf("2 seconds", "20 seconds with soap", "No need to wash"),
      correctOptionIndex = 1
    ),
    Lesson(
      id = "lesson_2",
      lessonNumber = 2,
      title = "Colorful Food Ingredients",
      subtitle = "Discovering the five tasty food groups!",
      category = "Cooking Academy",
      iconEmoji = "🥦",
      xpReward = 30,
      tips = listOf(
        "Foods come from nature in all the colors of the rainbow!",
        "Red foods like tomatoes and strawberries protect your heart.",
        "Green veggies like spinach and broccoli give you super energy!",
        "Orange foods like carrots help your eyes see brightly at night."
      ),
      quizQuestion = "Which food helps protect your eyesight?",
      quizOptions = listOf("Sweet candy", "Crunchy carrots", "Soda"),
      correctOptionIndex = 1
    ),
    Lesson(
      id = "lesson_3",
      lessonNumber = 3,
      title = "Everyday Kitchen Tools",
      subtitle = "Spoons, whisks, rolling pins, and measuring cups!",
      category = "Cooking Academy",
      iconEmoji = "🥣",
      xpReward = 35,
      tips = listOf(
        "A whisk is great for beating eggs and mixing pancake batter with air!",
        "A rolling pin flattens pizza dough into a big round crust.",
        "Measuring cups make sure we add the exact right amount of flour and milk.",
        "Spatulas help scoop every last drop of batter from the bowl."
      ),
      quizQuestion = "What tool is best for flattening pizza dough?",
      quizOptions = listOf("A rolling pin", "A spoon", "A cup"),
      correctOptionIndex = 0
    ),
    Lesson(
      id = "lesson_4",
      lessonNumber = 4,
      title = "Food Preparation Fun",
      subtitle = "Washing, peeling, mashing, and stirring!",
      category = "Cooking Academy",
      iconEmoji = "🥕",
      xpReward = 35,
      tips = listOf(
        "Always rinse fresh fruits and vegetables under cool running water.",
        "Peel carrots and potatoes over a compost bowl to keep the counter clean.",
        "Stir gently from bottom to top so the soup doesn't spill over.",
        "Let food cool down before taking a big bite!"
      ),
      quizQuestion = "What should you do before eating fresh apples or berries?",
      quizOptions = listOf("Wash them with water", "Put salt on them", "Leave them dirty"),
      correctOptionIndex = 0
    ),
    Lesson(
      id = "lesson_5",
      lessonNumber = 5,
      title = "Building a Rainbow Plate",
      subtitle = "How to mix grains, proteins, fruits and greens!",
      category = "Healthy Food",
      iconEmoji = "🌈",
      xpReward = 40,
      tips = listOf(
        "Fill half of your plate with bright fruits and crunchy vegetables!",
        "Add healthy grains like brown rice, oats, or whole wheat bread for energy.",
        "Proteins like eggs, beans, fish, and chicken build strong muscles.",
        "Drink pure water instead of sugary drinks to stay hydrated and alert."
      ),
      quizQuestion = "How much of your plate should be colorful fruits and veggies?",
      quizOptions = listOf("None at all", "About half the plate", "Only candy"),
      correctOptionIndex = 1
    ),
    Lesson(
      id = "lesson_6",
      lessonNumber = 6,
      title = "Kitchen Safety Champion",
      subtitle = "Hot ovens, sharp tools, and adult helpers!",
      category = "Kitchen Safety",
      iconEmoji = "🧤",
      xpReward = 45,
      tips = listOf(
        "Always ask an adult helper when using the stove, oven, or microwave!",
        "Never touch hot pans or baking sheets without thick oven mitts.",
        "If something spills on the floor, clean it up right away so nobody slips.",
        "Only adults handle sharp chef knives - kids can use safe plastic kid-cutters."
      ),
      quizQuestion = "What should you use when touching a hot baking pan?",
      quizOptions = listOf("Bare hands", "Thick oven mitts", "A paper towel"),
      correctOptionIndex = 1
    )
  )

  val fruitsList: List<FoodCardItem> = listOf(
    FoodCardItem("f_apple", "Crispy Apple", "Fruit", "Red / Green", "Apples float in water because 25% of their volume is air!", "🍎", "Vitamin C & Fiber"),
    FoodCardItem("f_banana", "Sweet Banana", "Fruit", "Yellow", "Bananas naturally grow curved pointing up toward the warm sun!", "🍌", "Potassium for energy"),
    FoodCardItem("f_orange", "Juicy Orange", "Fruit", "Orange", "One juicy orange gives you all the Vitamin C you need for the whole day!", "🍊", "Vitamin C power"),
    FoodCardItem("f_strawberry", "Ruby Strawberry", "Fruit", "Red", "Strawberries are the only fruit that wear their tiny seeds on the outside!", "🍓", "Antioxidants"),
    FoodCardItem("f_mango", "Golden Mango", "Fruit", "Golden Yellow", "Known as the king of tropical fruits, mangoes are super sweet and creamy!", "🥭", "Vitamin A & C"),
    FoodCardItem("f_watermelon", "Refreshing Watermelon", "Fruit", "Green & Pink", "Watermelon is 92% pure water - the ultimate sunny picnic treat!", "🍉", "Hydration hero"),
    FoodCardItem("f_grapes", "Juicy Grapes", "Fruit", "Purple / Green", "Grapes grow in cheerful clusters on climbing vines in sunny vineyards!", "🍇", "Energy boost"),
    FoodCardItem("f_pineapple", "Crown Pineapple", "Fruit", "Golden Brown", "Pineapples take almost 2 whole years to grow their golden crown!", "🍍", "Digestive enzymes")
  )

  val veggiesList: List<FoodCardItem> = listOf(
    FoodCardItem("v_carrot", "Crunchy Carrot", "Vegetable", "Bright Orange", "Carrots were originally purple before orange carrots became popular!", "🥕", "Vitamin A for eyes"),
    FoodCardItem("v_broccoli", "Tiny Tree Broccoli", "Vegetable", "Deep Green", "Broccoli looks just like miniature fairy forest trees!", "🥦", "Calcium & Vitamin K"),
    FoodCardItem("v_tomato", "Plump Tomato", "Vegetable", "Bright Red", "Tomatoes are technically fruits botanically, but we cook them like vegetables!", "🍅", "Lycopene boost"),
    FoodCardItem("v_potato", "Golden Potato", "Vegetable", "Golden Brown", "Potatoes grow safely underground beneath cheerful green leaves!", "🥔", "Vitamin B6 & Carbs"),
    FoodCardItem("v_cucumber", "Cool Cucumber", "Vegetable", "Crisp Green", "The inside of a cucumber can be up to 20 degrees cooler than the air!", "🥒", "Refreshing water"),
    FoodCardItem("v_spinach", "Super Spinach", "Vegetable", "Dark Emerald", "Spinach is packed with iron that gives your muscles extra strength!", "🥬", "Iron & Folate"),
    FoodCardItem("v_corn", "Sweet Corn", "Vegetable", "Golden Yellow", "Each ear of corn has an average of 800 sweet juicy kernels!", "🌽", "Fiber & Lutein"),
    FoodCardItem("v_peas", "Sweet Green Peas", "Vegetable", "Spring Green", "Peas grow cozy and safe inside natural green sleeping pods!", "🫛", "Plant protein")
  )

  val kitchenToolsList: List<KitchenToolItem> = listOf(
    KitchenToolItem("t_whisk", "Balloon Whisk", "Whisks air into pancake batter and scrambled eggs", "Creates fluffy clouds in bowls!", "🥣", "Safe for kids to hold and mix!"),
    KitchenToolItem("t_rolling_pin", "Wooden Rolling Pin", "Rolls pizza dough and cookie sheets flat and even", "Made from smooth maple wood", "🪵", "Keep fingers clear of the roll path!"),
    KitchenToolItem("t_spatula", "Silicone Spatula", "Flips golden pancakes and scrapes mixing bowls clean", "Heat-proof and bendy!", "🥞", "Great for gentle folding."),
    KitchenToolItem("t_measuring_cups", "Measuring Cups & Spoons", "Measures flour, milk, sugar, and spices accurately", "Science of cooking!", "🥄", "Level the top with a butter knife."),
    KitchenToolItem("t_oven_mitts", "Thick Oven Mitts", "Protects chef hands from hot pans and oven racks", "Padded with heat insulation", "🧤", "Always wear both hands when carrying hot trays!"),
    KitchenToolItem("t_blender", "Smoothie Blender", "Whirls whole fruits, ice, and yogurt into smoothies", "Spins at super high speed!", "🍹", "Always keep the blender lid securely on before pressing start!"),
    KitchenToolItem("t_cutting_board", "Rainbow Cutting Board", "Provides a safe, clean surface for chopping veggies", "Non-slip rubber feet", "🪵", "Use separate boards for meat and fresh fruits!"),
    KitchenToolItem("t_chef_hat", "Puffy Chef Toque", "Keeps kitchen hair tidy and marks the master chef", "Traditional 100-pleat hat!", "👨‍🍳", "Wear with pride in every recipe!")
  )

  val achievementsList: List<Achievement> = listOf(
    Achievement("first_recipe", "First Recipe", "Completed your very first cooking recipe!", "🍳", 30, 15, true),
    Achievement("pizza_pro", "Pizza Pro", "Crafted a delicious customized pizza!", "🍕", 35, 20),
    Achievement("healthy_chef", "Healthy Chef", "Packed a balanced rainbow meal plate!", "🥗", 40, 20, true),
    Achievement("fruit_expert", "Fruit Expert", "Explored all the colorful fruits in the academy!", "🍓", 35, 15),
    Achievement("vegetable_explorer", "Vegetable Explorer", "Learned all about green and orange vegetables!", "🥦", 35, 15),
    Achievement("quiz_master", "Quiz Master", "Answered 5 cooking quiz questions correctly!", "🏆", 50, 25),
    Achievement("memory_master", "Memory Master", "Matched all the food memory cards!", "🧠", 45, 25),
    Achievement("kitchen_safety_star", "Kitchen Safety Star", "Mastered kitchen safety rules and oven mitts!", "🧤", 50, 30),
    Achievement("super_chef", "Super Chef", "Reached the legendary rank of Super Chef!", "👑", 100, 50)
  )

  val dailyChallenge = DailyChallenge(
    id = "daily_1",
    title = "Build a Healthy Lunch Box",
    description = "Pack a delicious bento box with 1 fruit, 1 veggie, 1 grain, and cold water!",
    rewardStars = 20,
    rewardXp = 35,
    targetGameRoute = "game_lunchbox"
  )

  val quizQuestions: List<QuizQuestion> = listOf(
    QuizQuestion(1, "Which one of these is a sweet, crunchy fruit?", listOf("Apple", "Spoon", "Frying Pan"), 0, "It grows on apple trees!", "Apples come in green, red, and yellow colors!", "🍎"),
    QuizQuestion(2, "What should you wear on your hands when touching a hot baking tray?", listOf("Paper napkins", "Thick Oven Mitts", "Nothing"), 1, "They protect against heat!", "Oven mitts keep your fingers cool and safe.", "🧤"),
    QuizQuestion(3, "Which food group gives you long-lasting energy for running and playing?", listOf("Whole Grains", "Sugary Candy", "Ice Cubes"), 0, "Like brown rice, oats, and whole grain bread!", "Grains are complex carbohydrates that fuel your brain.", "🌾"),
    QuizQuestion(4, "Which kitchen tool is used to mix pancake batter and beat eggs?", listOf("A Fork", "A Balloon Whisk", "A Scissors"), 1, "It has curved wire loops!", "Whisking traps air bubbles to make fluffy pancakes.", "🥣"),
    QuizQuestion(5, "What color vegetable is famous for helping your eyes see in the dark?", listOf("Orange Carrots", "White Rice", "Black Pepper"), 0, "Crunchy and beloved by bunnies!", "Carrots have Beta-Carotene which converts to Vitamin A.", "🥕"),
    QuizQuestion(6, "Why do we wash our hands before cooking food?", listOf("To get wet", "To wash away germs and stay healthy", "Just for fun"), 1, "Keep food clean and safe!", "Clean hands prevent kitchen germs from spreading.", "🧼")
  )
}
