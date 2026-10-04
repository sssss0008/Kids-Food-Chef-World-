package com.example.ui.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.ChefCard
import com.example.ui.components.ChefPrimaryButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingQuizScreen(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val questions = viewModel.repository.quizQuestions
  var questionIndex by remember { mutableIntStateOf(0) }
  var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
  var feedbackText by remember { mutableStateOf<String?>(null) }
  var isCorrectAnswer by remember { mutableStateOf(false) }
  var score by remember { mutableIntStateOf(0) }

  val q = questions.getOrNull(questionIndex)
  val isComplete = questionIndex >= questions.size

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Cooking & Food Quiz ❓", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("quiz_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
      )
    }
  ) { padding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(padding)
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      if (!isComplete && q != null) {
        // Question Header
        Surface(
          shape = RoundedCornerShape(18.dp),
          color = ChefGoldContainer,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Question ${questionIndex + 1} of ${questions.size}", fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
            Text(text = "Score: $score ⭐", fontWeight = FontWeight.ExtraBold, color = Color(0xFFE65100))
          }
        }

        // Question Card
        ChefCard(
          backgroundColor = MaterialTheme.colorScheme.surface,
          cornerRadius = 24.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .size(60.dp)
              .clip(CircleShape)
              .background(ChefCoralContainer)
              .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
          ) {
            Text(text = q.emoji, fontSize = 34.sp)
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = q.question,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(14.dp))

          q.options.forEachIndexed { idx, option ->
            val isSelected = selectedAnswerIndex == idx
            val optBg = when {
              isSelected && idx == q.correctIndex -> ChefGreenContainer
              isSelected && idx != q.correctIndex -> ChefCoralContainer
              else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }

            Surface(
              shape = RoundedCornerShape(16.dp),
              color = optBg,
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                  viewModel.playClick()
                  selectedAnswerIndex = idx
                  if (idx == q.correctIndex) {
                    isCorrectAnswer = true
                    score++
                    feedbackText = "🌟 Great job! ${q.funFact}"
                  } else {
                    isCorrectAnswer = false
                    feedbackText = "Not quite! Try again: ${q.hint}"
                  }
                }
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) (if (idx == q.correctIndex) ChefGreen else ChefCoral) else Color.Transparent,
                  shape = RoundedCornerShape(16.dp)
                )
            ) {
              Text(
                text = option,
                modifier = Modifier.padding(14.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
          }

          if (feedbackText != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isCorrectAnswer) ChefGreenContainer else ChefCoralContainer
            ) {
              Text(
                text = feedbackText!!,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isCorrectAnswer) ChefGreenDark else ChefCoralDark,
                modifier = Modifier.padding(10.dp)
              )
            }
          }
        }

        // Next Question Button
        ChefPrimaryButton(
          text = if (isCorrectAnswer) "NEXT QUESTION ➡️" else "SELECT AN ANSWER",
          onClick = {
            viewModel.playClick()
            questionIndex++
            selectedAnswerIndex = null
            feedbackText = null
            isCorrectAnswer = false
          },
          enabled = isCorrectAnswer,
          containerColor = MaterialTheme.colorScheme.primary,
          testTag = "quiz_next_button"
        )
      } else {
        // Quiz Complete
        ChefCard(
          backgroundColor = MaterialTheme.colorScheme.surface,
          cornerRadius = 28.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(text = "🏆", fontSize = 64.sp)
            Text(text = "Quiz Master Complete!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Text(text = "You scored $score out of ${questions.size} correct answers!", style = MaterialTheme.typography.bodyLarge)

            Surface(shape = RoundedCornerShape(16.dp), color = ChefGoldContainer) {
              Text(
                text = "⭐ +25 Stars • 🔥 +40 XP",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color(0xFFE65100),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
              )
            }
          }
        }

        ChefPrimaryButton(
          text = "CLAIM REWARDS! ⭐",
          onClick = {
            viewModel.completeGame(
              stars = 25,
              xp = 40,
              gameName = "Cooking Quiz",
              playAgainAction = {
                questionIndex = 0
                selectedAnswerIndex = null
                feedbackText = null
                score = 0
              }
            )
          },
          containerColor = ChefGreen,
          testTag = "quiz_claim_rewards"
        )
      }
    }
  }
}
