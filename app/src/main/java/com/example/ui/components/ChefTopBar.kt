package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChefProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefTopBar(
  profile: ChefProfile,
  onMenuClick: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier,
  title: String = "Kids Food Chef"
) {
  TopAppBar(
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "👨‍🍳",
          fontSize = 24.sp
        )
        Text(
          text = title,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 19.sp,
          color = MaterialTheme.colorScheme.onBackground
        )
      }
    },
    navigationIcon = {
      IconButton(
        onClick = onMenuClick,
        modifier = Modifier.testTag("top_menu_button")
      ) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "Open Menu",
          tint = MaterialTheme.colorScheme.onBackground
        )
      }
    },
    actions = {
      // Stars counter pill
      StarBadge(stars = profile.stars)
      Spacer(modifier = Modifier.width(6.dp))
      // Level Chip
      LevelChip(
        levelTitle = profile.level.title.replace(" Chef", ""),
        badgeEmoji = profile.level.badgeEmoji,
        modifier = Modifier.testTag("top_level_badge")
      )
      Spacer(modifier = Modifier.width(8.dp))
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.background,
      titleContentColor = MaterialTheme.colorScheme.onBackground
    ),
    modifier = modifier
  )
}
