package com.example.ui.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.ui.components.ChefPrimaryButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingAcademyLessonScreen(
  lesson: Lesson,
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedQuizOption by remember { mutableStateOf<Int?>(null) }
  var quizFeedback by remember { mutableStateOf<String?>(null) }
  var isQuizPassed by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(text = "Lesson ${lesson.lessonNumber}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            Text(text = lesson.title, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("lesson_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(padding),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Hero Card
      item {
        ChefCard(
          backgroundColor = MaterialTheme.colorScheme.surface,
          cornerRadius = 24.dp
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(ChefCoralContainer),
              contentAlignment = Alignment.Center
            ) {
              Text(text = lesson.iconEmoji, fontSize = 36.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(text = lesson.subtitle, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
              Spacer(modifier = Modifier.height(4.dp))
              Text(text = "⭐ Earn +${lesson.xpReward} Chef XP", fontWeight = FontWeight.Bold, color = ChefCoralDark, fontSize = 13.sp)
            }
          }
        }
      }

      // Step-by-Step Tips
      item {
        Text(text = "Chef Lesson Tips:", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
      }

      itemsIndexed(lesson.tips) { index, tip ->
        ChefCard(
          backgroundColor = MaterialTheme.colorScheme.surface,
          cornerRadius = 18.dp,
          elevation = 2.dp
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(ChefGoldContainer),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "${index + 1}", fontWeight = FontWeight.Bold, color = Color(0xFFE65100), fontSize = 13.sp)
            }
            Text(text = tip, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
          }
        }
      }

      // Quick Lesson Checkpoint Quiz
      item {
        ChefCard(
          backgroundColor = MaterialTheme.colorScheme.surface,
          cornerRadius = 22.dp
        ) {
          Text(text = "Checkpoint Quiz ❓", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.height(8.dp))
          Text(text = lesson.quizQuestion, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(12.dp))

          lesson.quizOptions.forEachIndexed { idx, option ->
            val isSelected = selectedQuizOption == idx
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = if (isSelected) ChefCoralContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable {
                  viewModel.playClick()
                  selectedQuizOption = idx
                  if (idx == lesson.correctOptionIndex) {
                    isQuizPassed = true
                    quizFeedback = "🌟 Correct! Fantastic job, Chef!"
                  } else {
                    quizFeedback = "Not quite! Try reading the tips above."
                  }
                }
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) ChefCoral else Color.Transparent,
                  shape = RoundedCornerShape(14.dp)
                )
            ) {
              Text(text = option, modifier = Modifier.padding(12.dp), fontWeight = FontWeight.SemiBold)
            }
          }

          if (quizFeedback != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = quizFeedback!!,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = if (isQuizPassed) ChefGreenDark else MaterialTheme.colorScheme.error
            )
          }
        }
      }

      // Finish Button
      item {
        ChefPrimaryButton(
          text = if (isQuizPassed) "COMPLETE LESSON (+${lesson.xpReward} XP) 🎓" else "ANSWER QUIZ TO COMPLETE",
          onClick = {
            viewModel.completeLesson(lesson)
          },
          enabled = isQuizPassed,
          containerColor = ChefGreen,
          testTag = "finish_lesson_button"
        )
      }
    }
  }
}
