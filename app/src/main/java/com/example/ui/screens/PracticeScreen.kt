package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Lesson
import com.example.ui.components.ChefCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

@Composable
fun PracticeScreen(
  viewModel: ChefViewModel,
  onNavigateToRoute: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val profile by viewModel.profile.collectAsState()
  val lessons = viewModel.repository.academyLessons

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Learn & Practice",
          style = MaterialTheme.typography.displaySmall,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Become a smarter little chef with fun interactive lessons!",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Academy Lessons Section
    item {
      SectionHeader(
        title = "Cooking Academy",
        subtitle = "Master the essential kitchen skills one step at a time."
      )
    }

    items(lessons) { lesson ->
      val isCompleted = profile.completedLessonIds.contains(lesson.id)
      LessonItemCard(
        lesson = lesson,
        isCompleted = isCompleted,
        onStart = { viewModel.selectLesson(lesson) }
      )
    }

    // Healthy Food Exploration Section
    item {
      Spacer(modifier = Modifier.height(6.dp))
      SectionHeader(
        title = "Healthy Food Exploration",
        subtitle = "Discover yummy fruits, vegetables, and kitchen tools!"
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        ExploreCategoryCard(
          title = "Fruit Explorer",
          subtitle = "8 Sweet Fruits",
          emoji = "🍎",
          containerColor = ChefCoralContainer,
          modifier = Modifier.weight(1f),
          onClick = { onNavigateToRoute("explore_fruits") }
        )
        ExploreCategoryCard(
          title = "Veggie Explorer",
          subtitle = "8 Green Veggies",
          emoji = "🥦",
          containerColor = ChefGreenContainer,
          modifier = Modifier.weight(1f),
          onClick = { onNavigateToRoute("explore_veggies") }
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        ExploreCategoryCard(
          title = "Kitchen Tools",
          subtitle = "8 Tools to Know",
          emoji = "🥣",
          containerColor = ChefGoldContainer,
          modifier = Modifier.weight(1f),
          onClick = { onNavigateToRoute("explore_tools") }
        )
        ExploreCategoryCard(
          title = "Kitchen Safety",
          subtitle = "Safety Rules",
          emoji = "🧤",
          containerColor = ChefSkyBlueContainer,
          modifier = Modifier.weight(1f),
          onClick = { onNavigateToRoute("learning_safety") }
        )
      }
    }

    // Brain Games Section
    item {
      Spacer(modifier = Modifier.height(6.dp))
      SectionHeader(
        title = "Brain Games",
        subtitle = "Train your chef memory & test your food knowledge!"
      )
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        BrainGameCard(
          title = "Food Memory Game",
          description = "Flip cards and find pairs of matching foods!",
          emoji = "🧠",
          rewardXp = 35,
          onClick = { onNavigateToRoute("game_memory") }
        )
        BrainGameCard(
          title = "Food Matching Game",
          description = "Connect matching foods before time runs out!",
          emoji = "🃏",
          rewardXp = 30,
          onClick = { onNavigateToRoute("game_matching") }
        )
        BrainGameCard(
          title = "Food Sorting Mini-Game",
          description = "Sort foods into healthy categories and lunch boxes!",
          emoji = "🧺",
          rewardXp = 40,
          onClick = { onNavigateToRoute("game_sorting") }
        )
        BrainGameCard(
          title = "Cooking Quiz Challenge",
          description = "Answer fun multiple-choice food & safety trivia!",
          emoji = "❓",
          rewardXp = 35,
          onClick = { onNavigateToRoute("cooking_quiz") }
        )
      }
    }
  }
}

@Composable
fun LessonItemCard(
  lesson: Lesson,
  isCompleted: Boolean,
  onStart: () -> Unit
) {
  ChefCard(
    backgroundColor = MaterialTheme.colorScheme.surface,
    cornerRadius = 22.dp,
    elevation = 3.dp,
    onClick = onStart
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(60.dp)
          .clip(RoundedCornerShape(18.dp))
          .background(if (isCompleted) ChefGreenContainer else ChefCoralContainer),
        contentAlignment = Alignment.Center
      ) {
        Text(text = lesson.iconEmoji, fontSize = 32.sp)
      }

      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "Lesson ${lesson.lessonNumber}",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
          )
          if (isCompleted) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = ChefGreenContainer
            ) {
              Text(
                text = "Completed ✅",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = ChefGreenDark,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Text(
          text = lesson.title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = lesson.subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "🔥 +${lesson.xpReward} XP",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = ChefCoralDark
        )
      }

      Button(
        onClick = onStart,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isCompleted) ChefGreen else MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
        modifier = Modifier.testTag("start_lesson_${lesson.id}")
      ) {
        Text(
          text = if (isCompleted) "Review" else "Start",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
    }
  }
}

@Composable
fun ExploreCategoryCard(
  title: String,
  subtitle: String,
  emoji: String,
  containerColor: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(22.dp),
    color = containerColor,
    modifier = modifier
      .height(100.dp)
      .clickable { onClick() }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(14.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(text = emoji, fontSize = 28.sp)
      Column {
        Text(
          text = title,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
fun BrainGameCard(
  title: String,
  description: String,
  emoji: String,
  rewardXp: Int,
  onClick: () -> Unit
) {
  ChefCard(
    backgroundColor = MaterialTheme.colorScheme.surface,
    cornerRadius = 20.dp,
    onClick = onClick
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(CircleShape)
          .background(ChefPurpleContainer),
        contentAlignment = Alignment.Center
      ) {
        Text(text = emoji, fontSize = 26.sp)
      }

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Surface(
        shape = RoundedCornerShape(12.dp),
        color = ChefCoralContainer
      ) {
        Text(
          text = "+$rewardXp XP",
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          color = ChefCoralDark,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
      }
    }
  }
}
