package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChefProfile
import com.example.data.model.GameCategory
import com.example.ui.theme.*

@Composable
fun ChefDrawerContent(
  profile: ChefProfile,
  onNavigateToRoute: (String) -> Unit,
  onSelectCategory: (GameCategory) -> Unit,
  onCloseDrawer: () -> Unit,
  modifier: Modifier = Modifier
) {
  ModalDrawerSheet(
    modifier = modifier.widthIn(max = 340.dp),
    drawerContainerColor = MaterialTheme.colorScheme.surface,
    drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
      // Friendly Chef Header Card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(listOf(ChefCoral, ChefCoralDark))
          )
          .statusBarsPadding()
          .padding(20.dp)
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "👨‍🍳", fontSize = 34.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = profile.name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.White
              )
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "${profile.level.badgeEmoji} ${profile.level.title}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = ChefGoldContainer
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // XP progress bar
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "${profile.xp} XP",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f)
              )
              Text(
                text = "Next: ${profile.level.maxXp} XP",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
              progress = { profile.progressPercent },
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
              color = ChefGold,
              trackColor = Color.White.copy(alpha = 0.35f)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Stats badges row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color.White.copy(alpha = 0.2f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "⭐ ${profile.stars} Stars", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            }
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color.White.copy(alpha = 0.2f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "🍳 ${profile.completedGamesCount} Dishes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            }
          }
        }
      }

      // Drawer Menu Body
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // PLAY Section
        DrawerSection(title = "PLAY") {
          DrawerItem(emoji = "🎮", label = "All Games") {
            onSelectCategory(GameCategory.ALL)
            onNavigateToRoute("games_tab")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🍳", label = "Cooking Games") {
            onSelectCategory(GameCategory.COOKING)
            onNavigateToRoute("games_tab")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🎨", label = "Creative Games") {
            onSelectCategory(GameCategory.CREATIVE)
            onNavigateToRoute("games_tab")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🥗", label = "Healthy Games") {
            onSelectCategory(GameCategory.HEALTHY)
            onNavigateToRoute("games_tab")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🧠", label = "Memory Games") {
            onNavigateToRoute("game_memory")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🃏", label = "Matching Games") {
            onNavigateToRoute("game_matching")
            onCloseDrawer()
          }
        }

        // LEARN Section
        DrawerSection(title = "LEARN") {
          DrawerItem(emoji = "🎓", label = "Cooking Academy") {
            onNavigateToRoute("learning_academy")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🍎", label = "Fruit Learning") {
            onNavigateToRoute("explore_fruits")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🥦", label = "Vegetable Learning") {
            onNavigateToRoute("explore_veggies")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🥣", label = "Kitchen Tools Learning") {
            onNavigateToRoute("explore_tools")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🧤", label = "Kitchen Safety Lessons") {
            onNavigateToRoute("learning_safety")
            onCloseDrawer()
          }
        }

        // CHALLENGES Section
        DrawerSection(title = "CHALLENGES") {
          DrawerItem(emoji = "⭐", label = "Daily Challenge") {
            onNavigateToRoute("daily_challenge")
            onCloseDrawer()
          }
          DrawerItem(emoji = "❓", label = "Cooking Quiz") {
            onNavigateToRoute("cooking_quiz")
            onCloseDrawer()
          }
          DrawerItem(emoji = "🏪", label = "Restaurant Manager") {
            onNavigateToRoute("game_restaurant")
            onCloseDrawer()
          }
        }

        // PROGRESS Section
        DrawerSection(title = "PROGRESS") {
          DrawerItem(emoji = "🏆", label = "My Progress & Badges") {
            onNavigateToRoute("progress_screen")
            onCloseDrawer()
          }
          DrawerItem(emoji = "👑", label = "Chef Levels") {
            onNavigateToRoute("progress_screen")
            onCloseDrawer()
          }
        }

        // MORE Section
        DrawerSection(title = "MORE") {
          DrawerItem(emoji = "👨‍👩‍👧", label = "Parent Area") {
            onNavigateToRoute("parent_area")
            onCloseDrawer()
          }
          DrawerItem(emoji = "⚙️", label = "Settings & Sound") {
            onNavigateToRoute("settings_screen")
            onCloseDrawer()
          }
          DrawerItem(emoji = "ℹ️", label = "About Kids Food Chef") {
            onNavigateToRoute("about_tab")
            onCloseDrawer()
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
private fun DrawerSection(
  title: String,
  content: @Composable ColumnScope.() -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(vertical = 4.dp),
        content = content
      )
    }
  }
}

@Composable
private fun DrawerItem(
  emoji: String,
  label: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(horizontal = 14.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text(text = emoji, fontSize = 20.sp)
    Text(
      text = label,
      fontSize = 15.sp,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}
