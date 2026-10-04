package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChefPrimaryButton
import com.example.ui.theme.*

data class OnboardingStep(
  val title: String,
  val subtitle: String,
  val emoji: String,
  val gradient: List<Color>
)

@Composable
fun OnboardingScreen(
  onComplete: () -> Unit,
  modifier: Modifier = Modifier
) {
  val steps = listOf(
    OnboardingStep(
      title = "Kids Food Chef World",
      subtitle = "Learn. Cook. Play.",
      emoji = "👨‍🍳",
      gradient = listOf(ChefCoral, ChefCoralDark)
    ),
    OnboardingStep(
      title = "Create Delicious Food",
      subtitle = "Bake crispy pizzas, stack tall burgers, decorate sweet cakes, and blend fruit smoothies!",
      emoji = "🍕",
      gradient = listOf(Color(0xFFFFA000), Color(0xFFF57C00))
    ),
    OnboardingStep(
      title = "Learn Healthy Nutrition",
      subtitle = "Discover colorful rainbow fruits, crunchy vegetables, and master kitchen safety rules!",
      emoji = "🥦",
      gradient = listOf(ChefGreen, ChefGreenDark)
    ),
    OnboardingStep(
      title = "Become a Super Chef",
      subtitle = "Earn shiny stars, level up from Junior Chef, and collect 9 legendary chef badges!",
      emoji = "👑",
      gradient = listOf(ChefPurple, Color(0xFF6A1B9A))
    )
  )

  var currentStepIndex by remember { mutableIntStateOf(0) }
  val currentStep = steps[currentStepIndex]
  val isLastStep = currentStepIndex == steps.size - 1

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Brush.verticalGradient(currentStep.gradient))
      .padding(24.dp)
  ) {
    // Top Skip Button
    if (!isLastStep) {
      TextButton(
        onClick = onComplete,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .statusBarsPadding()
          .testTag("skip_onboarding")
      ) {
        Text(
          text = "Skip",
          color = Color.White.copy(alpha = 0.85f),
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      }
    }

    // Center Illustration & Content
    Column(
      modifier = Modifier
        .align(Alignment.Center)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
      AnimatedContent(targetState = currentStep.emoji, label = "emoji_anim") { emoji ->
        Box(
          modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.25f)),
          contentAlignment = Alignment.Center
        ) {
          Text(text = emoji, fontSize = 76.sp)
        }
      }

      Text(
        text = currentStep.title,
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White,
        textAlign = TextAlign.Center
      )

      Text(
        text = currentStep.subtitle,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        color = Color.White.copy(alpha = 0.95f),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      // Step indicator dots
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 12.dp)
      ) {
        steps.indices.forEach { index ->
          Box(
            modifier = Modifier
              .size(if (index == currentStepIndex) 24.dp else 10.dp, 10.dp)
              .clip(RoundedCornerShape(5.dp))
              .background(if (index == currentStepIndex) Color.White else Color.White.copy(alpha = 0.4f))
          )
        }
      }
    }

    // Bottom Action Button
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(bottom = 16.dp)
    ) {
      if (isLastStep) {
        ChefPrimaryButton(
          text = "LET'S COOK!",
          onClick = onComplete,
          icon = "🍳",
          containerColor = ChefGold,
          contentColor = Color(0xFF3E2723),
          testTag = "onboarding_lets_cook"
        )
      } else {
        ChefPrimaryButton(
          text = "NEXT",
          onClick = { currentStepIndex++ },
          icon = "➡️",
          containerColor = Color.White,
          contentColor = MaterialTheme.colorScheme.onSurface,
          testTag = "onboarding_next"
        )
      }
    }
  }
}
