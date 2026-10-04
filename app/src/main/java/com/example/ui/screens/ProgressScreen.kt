package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import com.example.data.model.ChefLevel
import com.example.data.model.ChefProfile
import com.example.ui.components.ChefCard
import com.example.ui.components.ChefPrimaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val profile by viewModel.profile.collectAsState()
  var showEditNameDialog by remember { mutableStateOf(false) }
  var nameInput by remember { mutableStateOf(profile.name) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Chef Progress & Badges",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 19.sp
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("progress_back_button")) {
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
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
      // 1. Chef Profile Card
      item {
        ChefCard(
          backgroundColor = MaterialTheme.colorScheme.surface,
          cornerRadius = 26.dp,
          elevation = 4.dp
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(ChefCoralContainer),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "👨‍🍳", fontSize = 40.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = profile.name,
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.ExtraBold
                )
                IconButton(
                  onClick = { showEditNameDialog = true },
                  modifier = Modifier.size(24.dp)
                ) {
                  Icon(Icons.Default.Edit, contentDescription = "Edit Name", modifier = Modifier.size(16.dp))
                }
              }

              Text(
                text = "${profile.level.badgeEmoji} ${profile.level.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // XP Progress
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "${profile.xp} / ${profile.level.maxXp} XP",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "${(profile.progressPercent * 100).toInt()}% Complete",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          LinearProgressIndicator(
            progress = { profile.progressPercent },
            modifier = Modifier
              .fillMaxWidth()
              .height(12.dp)
              .clip(RoundedCornerShape(6.dp)),
            color = ChefCoral,
            trackColor = ChefCoralContainer
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 3 Stats Pills: Stars, Games, Lessons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            StatPill(emoji = "⭐", label = "${profile.stars} Stars", color = ChefGoldContainer, modifier = Modifier.weight(1f))
            StatPill(emoji = "🍳", label = "${profile.completedGamesCount} Dishes", color = ChefCoralContainer, modifier = Modifier.weight(1f))
            StatPill(emoji = "🎓", label = "${profile.completedLessonsCount} Lessons", color = ChefGreenContainer, modifier = Modifier.weight(1f))
          }
        }
      }

      // 2. Chef Ranks System
      item {
        SectionHeader(
          title = "Chef Ranks Ladder",
          subtitle = "Cook your way to the top rank!"
        )
      }

      items(ChefLevel.entries) { level ->
        val isCurrent = profile.level == level
        val isAchieved = profile.xp >= level.minXp
        ChefRankItem(level = level, isCurrent = isCurrent, isAchieved = isAchieved)
      }

      // 3. Badges & Achievements
      item {
        Spacer(modifier = Modifier.height(6.dp))
        SectionHeader(
          title = "Collectible Badges",
          subtitle = "Earn badges by making recipes & learning!"
        )
      }

      items(viewModel.repository.achievementsList) { achievement ->
        val isUnlocked = profile.unlockedAchievementIds.contains(achievement.id)
        AchievementRow(achievement = achievement, isUnlocked = isUnlocked)
      }
    }
  }

  // Edit Name Dialog
  if (showEditNameDialog) {
    AlertDialog(
      onDismissRequest = { showEditNameDialog = false },
      title = { Text("What is your Chef Name?", fontWeight = FontWeight.Bold) },
      text = {
        OutlinedTextField(
          value = nameInput,
          onValueChange = { nameInput = it },
          label = { Text("Chef Name") },
          singleLine = true,
          shape = RoundedCornerShape(16.dp)
        )
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.updateChefName(nameInput)
            showEditNameDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditNameDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun StatPill(emoji: String, label: String, color: Color, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = color,
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Text(text = emoji, fontSize = 16.sp)
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = label,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}

@Composable
private fun ChefRankItem(level: ChefLevel, isCurrent: Boolean, isAchieved: Boolean) {
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = if (isCurrent) ChefCoralContainer else MaterialTheme.colorScheme.surface,
    modifier = Modifier
      .fillMaxWidth()
      .border(
        width = if (isCurrent) 2.dp else 1.dp,
        color = if (isCurrent) ChefCoral else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
        shape = RoundedCornerShape(20.dp)
      )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(if (isAchieved) ChefGoldContainer else Color.LightGray.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = if (isAchieved) level.badgeEmoji else "🔒", fontSize = 24.sp)
      }

      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = level.title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          if (isCurrent) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = ChefCoral
            ) {
              Text(
                text = "CURRENT",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
        Text(
          text = level.description,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Text(
        text = "${level.minXp} XP",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = if (isAchieved) ChefCoralDark else Color.Gray
      )
    }
  }
}

@Composable
private fun AchievementRow(achievement: com.example.data.model.Achievement, isUnlocked: Boolean) {
  ChefCard(
    backgroundColor = MaterialTheme.colorScheme.surface,
    cornerRadius = 20.dp,
    elevation = 2.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(if (isUnlocked) ChefGoldContainer else Color.LightGray.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = if (isUnlocked) achievement.iconEmoji else "🔒", fontSize = 28.sp)
      }

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = achievement.title,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = achievement.description,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Column(horizontalAlignment = Alignment.End) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (isUnlocked) ChefGreenContainer else Color.LightGray.copy(alpha = 0.3f)
        ) {
          Text(
            text = if (isUnlocked) "Unlocked" else "Locked",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) ChefGreenDark else Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "+${achievement.starsReward} ⭐",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFFE65100)
        )
      }
    }
  }
}
