package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChefCard
import com.example.ui.components.ChefPrimaryButton
import com.example.ui.components.ChefSecondaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentAreaScreen(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val profile by viewModel.profile.collectAsState()
  val context = LocalContext.current

  // Parental Gate Math Challenge State
  var isUnlocked by remember { mutableStateOf(false) }
  val num1 = remember { Random.nextInt(5, 12) }
  val num2 = remember { Random.nextInt(4, 9) }
  val correctAnswer = num1 + num2
  var answerInput by remember { mutableStateOf("") }
  var gateError by remember { mutableStateOf(false) }
  var showResetConfirm by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Parents Area & Settings",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 19.sp
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("parent_area_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
      )
    }
  ) { padding ->
    if (!isUnlocked) {
      // PARENTAL GATE SCREEN
      Box(
        modifier = modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background)
          .padding(padding)
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        ChefCard(
          backgroundColor = MaterialTheme.colorScheme.surface,
          cornerRadius = 28.dp,
          elevation = 6.dp
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(ChefCoralContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Lock, contentDescription = "Lock", tint = ChefCoralDark, modifier = Modifier.size(32.dp))
            }

            Text(
              text = "Parental Gate",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.ExtraBold
            )

            Text(
              text = "For adults only. Please solve the math problem to enter:",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center
            )

            Surface(
              shape = RoundedCornerShape(18.dp),
              color = ChefGoldContainer,
              modifier = Modifier.padding(vertical = 4.dp)
            ) {
              Text(
                text = "What is $num1 + $num2 = ?",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE65100),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
              )
            }

            OutlinedTextField(
              value = answerInput,
              onValueChange = {
                answerInput = it
                gateError = false
              },
              placeholder = { Text("Your answer") },
              singleLine = true,
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.fillMaxWidth()
            )

            if (gateError) {
              Text(
                text = "Incorrect answer, please try again.",
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }

            ChefPrimaryButton(
              text = "ENTER PARENT AREA",
              onClick = {
                if (answerInput.trim() == correctAnswer.toString()) {
                  isUnlocked = true
                  gateError = false
                } else {
                  gateError = true
                }
              },
              icon = "🔓",
              testTag = "unlock_parent_area"
            )
          }
        }
      }
    } else {
      // UNLOCKED PARENT AREA
      LazyColumn(
        modifier = modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background)
          .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Child's Learning Stats
        item {
          ChefCard(
            backgroundColor = MaterialTheme.colorScheme.surface,
            cornerRadius = 24.dp
          ) {
            Text(
              text = "Learning & Activity Overview",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              StatReportBox(title = "Dishes Cooked", value = "${profile.completedGamesCount}", emoji = "🍳", modifier = Modifier.weight(1f))
              StatReportBox(title = "Lessons Passed", value = "${profile.completedLessonsCount}", emoji = "🎓", modifier = Modifier.weight(1f))
              StatReportBox(title = "Stars Earned", value = "${profile.stars}", emoji = "⭐", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "Current Chef Rank: ${profile.level.badgeEmoji} ${profile.level.title} (${profile.xp} total XP points)",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Sound & Music Toggles
        item {
          ChefCard(
            backgroundColor = MaterialTheme.colorScheme.surface,
            cornerRadius = 24.dp
          ) {
            Text(
              text = "Audio & Haptic Controls",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(text = "Sound Effects", fontWeight = FontWeight.Bold)
                Text(text = "Pop sounds, sizzling, and blender chimes", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              Switch(
                checked = profile.soundEnabled,
                onCheckedChange = { viewModel.toggleSound(it) },
                modifier = Modifier.testTag("toggle_sound_switch")
              )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(text = "Background Music", fontWeight = FontWeight.Bold)
                Text(text = "Playful cooking melodies", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              Switch(
                checked = profile.musicEnabled,
                onCheckedChange = { viewModel.toggleMusic(it) },
                modifier = Modifier.testTag("toggle_music_switch")
              )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(text = "Haptic Vibration", fontWeight = FontWeight.Bold)
                Text(text = "Tactile feedback when tapping ingredients", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              Switch(
                checked = profile.hapticsEnabled,
                onCheckedChange = { viewModel.toggleHaptics(it) }
              )
            }
          }
        }

        // Cross-device sync & Developer Platform
        item {
          ChefCard(
            backgroundColor = MaterialTheme.colorScheme.surface,
            cornerRadius = 24.dp
          ) {
            Text(
              text = "Cloud Sync & Developer Info",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Kids Food Chef World is designed by Awiskar Acharya. Data is currently cached locally on your device for fast offline play. Connect with the developer platform on LinkedIn to register for cloud device synchronization across Android tablets and smartphones.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            ChefPrimaryButton(
              text = "VISIT DEVELOPER LINKEDIN",
              onClick = {
                try {
                  val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/awiskaracharya/"))
                  context.startActivity(intent)
                } catch (_: Exception) {}
              },
              icon = "🌐",
              containerColor = ChefSkyBlue,
              testTag = "parent_linkedin_sync_button"
            )
          }
        }

        // Reset Data
        item {
          ChefCard(
            backgroundColor = MaterialTheme.colorScheme.surface,
            cornerRadius = 24.dp
          ) {
            Text(
              text = "Reset Progress",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Reset all stars, completed lessons, and chef ranks back to beginner level.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            ChefSecondaryButton(
              text = "RESET ALL PROGRESS",
              onClick = { showResetConfirm = true },
              icon = "⚠️"
            )
          }
        }
      }
    }
  }

  // Confirmation Alert Dialog for Reset
  if (showResetConfirm) {
    AlertDialog(
      onDismissRequest = { showResetConfirm = false },
      title = { Text("Reset All Progress?", fontWeight = FontWeight.Bold) },
      text = { Text("Are you sure you want to start fresh? All earned stars and recipes will be reset.") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.resetAllProgress()
            showResetConfirm = false
            onBack()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Reset")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirm = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun StatReportBox(title: String, value: String, emoji: String, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = ChefCoralContainer,
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(text = emoji, fontSize = 22.sp)
      Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = ChefCoralDark)
      Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
  }
}
