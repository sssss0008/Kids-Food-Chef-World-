package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChefProfile
import com.example.data.model.GameItem
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

@Composable
fun HomeScreen(
  viewModel: ChefViewModel,
  profile: ChefProfile,
  onStartCooking: () -> Unit,
  onNavigateToGame: (String) -> Unit,
  onNavigateToRoute: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 1. HERO SECTION
    item {
      HeroSection(
        profile = profile,
        onStartCooking = onStartCooking,
        onProfileClick = { onNavigateToRoute("progress_screen") }
      )
    }

    // 2. CONTINUE PLAYING CARD
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader(title = "Continue Playing", subtitle = "Pick up right where you left off!")
        Spacer(modifier = Modifier.height(4.dp))
        ContinuePlayingCard(
          gameName = profile.lastPlayedGameName,
          progress = profile.lastPlayedGameProgress,
          onContinue = {
            when (profile.lastPlayedGameName) {
              "Burger Builder" -> onNavigateToGame("game_burger")
              "Cake Decorating" -> onNavigateToGame("game_cake")
              "Fruit Juice Maker" -> onNavigateToGame("game_juice")
              "Lunch Box Creator" -> onNavigateToGame("game_lunchbox")
              else -> onNavigateToGame("game_pizza")
            }
          }
        )
      }
    }

    // 3. TODAY'S CHALLENGE CARD
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader(title = "Today's Challenge", subtitle = "Complete it to earn bonus stars!")
        Spacer(modifier = Modifier.height(4.dp))
        TodaysChallengeCard(
          dailyChallenge = viewModel.repository.dailyChallenge,
          isCompleted = profile.dailyChallengeCompleted,
          onPlayNow = { onNavigateToGame("game_lunchbox") }
        )
      }
    }

    // 4. YOUR CHEF LEVEL PROGRESS
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader(
          title = "Your Chef Level",
          subtitle = "Cook and practice to become a Super Chef!",
          actionText = "See Ranks",
          onActionClick = { onNavigateToRoute("progress_screen") }
        )
        Spacer(modifier = Modifier.height(4.dp))
        ChefLevelCard(profile = profile)
      }
    }

    // 5. QUICK GAMES (Horizontal Scroll)
    item {
      Column {
        SectionHeader(
          title = "Quick Games",
          subtitle = "Jump into popular kid recipes!",
          actionText = "View All (11)",
          onActionClick = onStartCooking,
          modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(viewModel.repository.allGames.take(6)) { game ->
            QuickGameCard(game = game, onClick = { onNavigateToGame(game.route) })
          }
        }
      }
    }

    // 6. LEARN SOMETHING NEW
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader(
          title = "Learn Something New",
          subtitle = "Interactive food exploration & kitchen safety!",
          actionText = "All Lessons",
          onActionClick = { onNavigateToRoute("learning_academy") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          LearningMiniCard(
            title = "Fruits",
            emoji = "🍎",
            color = ChefCoralContainer,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToRoute("explore_fruits") }
          )
          LearningMiniCard(
            title = "Vegetables",
            emoji = "🥦",
            color = ChefGreenContainer,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToRoute("explore_veggies") }
          )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          LearningMiniCard(
            title = "Kitchen Tools",
            emoji = "🥣",
            color = ChefGoldContainer,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToRoute("explore_tools") }
          )
          LearningMiniCard(
            title = "Safety Rules",
            emoji = "🧤",
            color = ChefSkyBlueContainer,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateToRoute("learning_safety") }
          )
        }
      }
    }

    // 7. ACHIEVEMENTS BADGE GALLERY PREVIEW
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader(
          title = "Achievements",
          subtitle = "Collect all 9 master chef badges!",
          actionText = "Badges",
          onActionClick = { onNavigateToRoute("progress_screen") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(viewModel.repository.achievementsList) { achievement ->
            val isUnlocked = profile.unlockedAchievementIds.contains(achievement.id)
            BadgeItem(achievement = achievement, isUnlocked = isUnlocked)
          }
        }
      }
    }
  }
}

@Composable
private fun HeroSection(
  profile: ChefProfile,
  onStartCooking: () -> Unit,
  onProfileClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    Surface(
      shape = RoundedCornerShape(30.dp),
      color = ChefCoral,
      shadowElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .border(2.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(30.dp))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(listOf(ChefCoral, Color(0xFFE64A19)))
          )
          .padding(22.dp)
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Mascot / Avatar
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(CircleShape)
              .background(Color.White)
              .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
          ) {
            Text(text = "👨‍🍳", fontSize = 44.sp)
          }

          Text(
            text = if (profile.completedGamesCount > 0) "Welcome Back, Chef!" else "Welcome, Little Chef!",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )

          Text(
            text = "Ready to cook something amazing today?",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.95f)
          )

          Spacer(modifier = Modifier.height(4.dp))

          // START COOKING CTA BUTTON
          ChefPrimaryButton(
            text = "START COOKING",
            onClick = onStartCooking,
            icon = "🍳",
            containerColor = ChefGold,
            contentColor = Color(0xFF3E2723),
            testTag = "home_start_cooking_button"
          )
        }
      }
    }
  }
}

@Composable
private fun ContinuePlayingCard(
  gameName: String,
  progress: String,
  onContinue: () -> Unit
) {
  ChefCard(
    backgroundColor = MaterialTheme.colorScheme.surface,
    cornerRadius = 22.dp,
    onClick = onContinue
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(ChefCoralContainer),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "🍕", fontSize = 28.sp)
      }

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = gameName,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 17.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Continue your recipe: $progress",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Button(
        onClick = onContinue,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
        modifier = Modifier.testTag("continue_play_button")
      ) {
        Text(text = "Play", fontWeight = FontWeight.Bold, fontSize = 13.sp)
      }
    }
  }
}

@Composable
private fun TodaysChallengeCard(
  dailyChallenge: com.example.data.model.DailyChallenge,
  isCompleted: Boolean,
  onPlayNow: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(24.dp),
    color = ChefGreenContainer,
    modifier = Modifier
      .fillMaxWidth()
      .border(1.5.dp, ChefGreen.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
      .shadow(4.dp, RoundedCornerShape(24.dp))
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(text = "🍱", fontSize = 24.sp)
          Text(
            text = dailyChallenge.title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp,
            color = ChefGreenDark
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.White
        ) {
          Text(
            text = "⭐ +${dailyChallenge.rewardStars} Stars",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = Color(0xFFE65100),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = dailyChallenge.description,
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF2E7D32)
      )

      Spacer(modifier = Modifier.height(14.dp))

      if (isCompleted) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFFC8E6C9),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "✅ Completed Today! You earned 20 Stars!", fontWeight = FontWeight.Bold, color = ChefGreenDark)
          }
        }
      } else {
        ChefPrimaryButton(
          text = "PLAY NOW",
          onClick = onPlayNow,
          icon = "⭐",
          containerColor = ChefGreen,
          testTag = "play_daily_challenge"
        )
      }
    }
  }
}

@Composable
private fun ChefLevelCard(profile: ChefProfile) {
  ChefCard(
    backgroundColor = MaterialTheme.colorScheme.surface,
    cornerRadius = 24.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(text = profile.level.badgeEmoji, fontSize = 32.sp)
        Column {
          Text(
            text = profile.level.title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = profile.level.description,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Text(
        text = "${profile.xp} / ${profile.level.maxXp} XP",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.primary
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    LinearProgressIndicator(
      progress = { profile.progressPercent },
      modifier = Modifier
        .fillMaxWidth()
        .height(12.dp)
        .clip(RoundedCornerShape(6.dp)),
      color = ChefCoral,
      trackColor = ChefCoralContainer
    )

    Spacer(modifier = Modifier.height(8.dp))

    val remainingXp = (profile.level.maxXp - profile.xp).coerceAtLeast(0)
    Text(
      text = "Only $remainingXp XP to reach next chef rank!",
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
private fun QuickGameCard(
  game: GameItem,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(22.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 3.dp,
    modifier = Modifier
      .width(150.dp)
      .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(22.dp))
      .clickable { onClick() }
      .testTag("quick_game_${game.id}")
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Box(
        modifier = Modifier
          .size(56.dp)
          .clip(CircleShape)
          .background(Color(game.colorHex).copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = game.iconEmoji, fontSize = 30.sp)
      }

      Text(
        text = game.name,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = "⭐ +${game.starsReward} Stars",
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFFE65100)
      )
    }
  }
}

@Composable
private fun LearningMiniCard(
  title: String,
  emoji: String,
  color: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = color,
    modifier = modifier
      .height(68.dp)
      .clickable { onClick() }
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(text = emoji, fontSize = 28.sp)
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}

@Composable
private fun BadgeItem(
  achievement: com.example.data.model.Achievement,
  isUnlocked: Boolean
) {
  Surface(
    shape = RoundedCornerShape(18.dp),
    color = if (isUnlocked) ChefGoldContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    modifier = Modifier
      .width(100.dp)
      .border(
        1.dp,
        if (isUnlocked) ChefGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
        RoundedCornerShape(18.dp)
      )
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
        text = if (isUnlocked) achievement.iconEmoji else "🔒",
        fontSize = 24.sp
      )
      Text(
        text = achievement.title,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = if (isUnlocked) Color(0xFF5D4037) else Color.Gray,
        maxLines = 1
      )
    }
  }
}
