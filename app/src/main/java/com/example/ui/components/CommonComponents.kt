package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*
import kotlin.random.Random

@Composable
fun ChefPrimaryButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: String? = null,
  containerColor: Color = MaterialTheme.colorScheme.primary,
  contentColor: Color = Color.White,
  enabled: Boolean = true,
  testTag: String = "primary_button"
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.94f else 1.0f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
    label = "button_scale"
  )

  Button(
    onClick = onClick,
    modifier = modifier
      .scale(scale)
      .heightIn(min = 54.dp)
      .fillMaxWidth()
      .testTag(testTag),
    enabled = enabled,
    shape = RoundedCornerShape(26.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = containerColor,
      contentColor = contentColor
    ),
    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp),
    interactionSource = interactionSource,
    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      if (icon != null) {
        Text(text = icon, fontSize = 22.sp)
        Spacer(modifier = Modifier.width(10.dp))
      }
      Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
      )
    }
  }
}

@Composable
fun ChefSecondaryButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: String? = null,
  testTag: String = "secondary_button"
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier
      .heightIn(min = 48.dp)
      .fillMaxWidth()
      .testTag(testTag),
    shape = RoundedCornerShape(24.dp),
    border = ButtonDefaults.outlinedButtonBorder.copy(
      width = 2.dp,
      brush = Brush.horizontalGradient(listOf(ChefCoral, ChefGold))
    ),
    colors = ButtonDefaults.outlinedButtonColors(
      contentColor = MaterialTheme.colorScheme.primary
    ),
    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      if (icon != null) {
        Text(text = icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(8.dp))
      }
      Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun ChefCard(
  modifier: Modifier = Modifier,
  backgroundColor: Color = MaterialTheme.colorScheme.surface,
  cornerRadius: Dp = 22.dp,
  elevation: Dp = 4.dp,
  borderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
  onClick: (() -> Unit)? = null,
  content: @Composable ColumnScope.() -> Unit
) {
  val shape = RoundedCornerShape(cornerRadius)
  val cardModifier = if (onClick != null) {
    modifier
      .clip(shape)
      .clickable { onClick() }
  } else modifier

  Surface(
    modifier = cardModifier
      .shadow(elevation, shape)
      .border(1.dp, borderColor, shape),
    shape = shape,
    color = backgroundColor
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      content = content
    )
  }
}

@Composable
fun StarBadge(stars: Int, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = ChefGoldContainer,
    modifier = modifier.border(1.dp, ChefGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
      Text(text = "⭐", fontSize = 16.sp)
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "$stars",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        color = Color(0xFFE65100)
      )
    }
  }
}

@Composable
fun LevelChip(levelTitle: String, badgeEmoji: String, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = ChefCoralContainer,
    modifier = modifier.border(1.dp, ChefCoral.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
      Text(text = badgeEmoji, fontSize = 16.sp)
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = levelTitle,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = ChefCoralDark
      )
    }
  }
}

@Composable
fun SectionHeader(
  title: String,
  subtitle: String? = null,
  modifier: Modifier = Modifier,
  actionText: String? = null,
  onActionClick: (() -> Unit)? = null
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
      )
      if (subtitle != null) {
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
    if (actionText != null && onActionClick != null) {
      TextButton(onClick = onActionClick) {
        Text(
          text = actionText,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
          fontSize = 14.sp
        )
      }
    }
  }
}

@Composable
fun ConfettiCelebration(modifier: Modifier = Modifier) {
  val confettiColors = listOf(ChefCoral, ChefGold, ChefGreen, ChefSkyBlue, ChefPink, ChefPurple)
  val infiniteTransition = rememberInfiniteTransition(label = "confetti")
  
  Box(modifier = modifier.fillMaxSize()) {
    repeat(25) { index ->
      val initialX = remember { Random.nextFloat() }
      val delay = remember { Random.nextInt(0, 500) }
      val duration = remember { Random.nextInt(1200, 2200) }
      val color = remember { confettiColors[index % confettiColors.size] }
      val size = remember { Random.nextInt(8, 16).dp }

      val yPos by infiniteTransition.animateFloat(
        initialValue = -0.1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
          animation = tween(duration, delayMillis = delay, easing = LinearEasing),
          repeatMode = RepeatMode.Restart
        ),
        label = "confetti_y_$index"
      )

      val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
          animation = tween(duration, easing = LinearEasing),
          repeatMode = RepeatMode.Restart
        ),
        label = "confetti_rot_$index"
      )

      Box(
        modifier = Modifier
          .fillMaxSize()
          .align(Alignment.TopStart)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .offset(
              x = (initialX * 360).dp,
              y = (yPos * 600).dp
            )
            .size(size)
            .scale(if (index % 2 == 0) 1f else 0.7f)
            .clip(if (index % 3 == 0) CircleShape else RoundedCornerShape(3.dp))
            .background(color)
        )
      }
    }
  }
}

@Composable
fun RewardCompletionDialog(
  visible: Boolean,
  title: String,
  stars: Int,
  xp: Int,
  badgeName: String?,
  badgeEmoji: String?,
  onPlayAgain: () -> Unit,
  onBackToGames: () -> Unit,
  onDismiss: () -> Unit
) {
  if (!visible) return

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(32.dp))
        .background(MaterialTheme.colorScheme.surface)
        .border(2.dp, ChefGold.copy(alpha = 0.5f), RoundedCornerShape(32.dp))
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      ConfettiCelebration(modifier = Modifier.matchParentSize())

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        // Big Trophy / Celebration Emoji
        Text(text = "🎉", fontSize = 56.sp)

        Text(
          text = title,
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center
        )

        Text(
          text = "You earned awesome rewards!",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Rewards Row
        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Stars pill
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = ChefGoldContainer,
            modifier = Modifier.border(1.dp, ChefGold, RoundedCornerShape(20.dp))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "⭐", fontSize = 24.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "+$stars Stars",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = Color(0xFFE65100)
              )
            }
          }

          // XP pill
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = ChefCoralContainer,
            modifier = Modifier.border(1.dp, ChefCoral, RoundedCornerShape(20.dp))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "🔥", fontSize = 24.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "+$xp XP",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = ChefCoralDark
              )
            }
          }
        }

        if (badgeName != null && badgeEmoji != null) {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = ChefGreenContainer,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Text(text = badgeEmoji, fontSize = 24.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "New Badge: $badgeName",
                fontWeight = FontWeight.Bold,
                color = ChefGreenDark,
                fontSize = 14.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Buttons
        ChefPrimaryButton(
          text = "PLAY AGAIN",
          onClick = onPlayAgain,
          icon = "🔄",
          containerColor = MaterialTheme.colorScheme.primary,
          testTag = "reward_play_again"
        )

        ChefSecondaryButton(
          text = "BACK TO GAMES",
          onClick = onBackToGames,
          icon = "🎮",
          testTag = "reward_back_to_games"
        )
      }
    }
  }
}
