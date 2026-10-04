package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChefCoral
import com.example.ui.theme.ChefCoralContainer
import com.example.ui.theme.ChefCoralDark
import com.example.ui.viewmodel.AppDestination

@Composable
fun ChefBottomNav(
  currentDestination: AppDestination,
  onDestinationSelected: (AppDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .shadow(12.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .windowInsetsPadding(WindowInsets.navigationBars)
        .padding(horizontal = 8.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      val items = listOf(
        AppDestination.HOME to (Icons.Filled.Home to Icons.Outlined.Home),
        AppDestination.GAMES to (Icons.Filled.SportsEsports to Icons.Outlined.SportsEsports),
        AppDestination.PRACTICE to (Icons.Filled.School to Icons.Outlined.School),
        AppDestination.ABOUT to (Icons.Filled.Info to Icons.Outlined.Info)
      )

      items.forEach { (destination, iconPair) ->
        val isSelected = currentDestination == destination
        val (selectedIcon, unselectedIcon) = iconPair

        val bgColor by animateColorAsState(
          targetValue = if (isSelected) ChefCoralContainer else MaterialTheme.colorScheme.surface,
          animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
          label = "nav_bg_${destination.name}"
        )

        val iconColor by animateColorAsState(
          targetValue = if (isSelected) ChefCoralDark else MaterialTheme.colorScheme.onSurfaceVariant,
          label = "nav_icon_${destination.name}"
        )

        Box(
          modifier = Modifier
            .weight(1f)
            .height(56.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple()
            ) { onDestinationSelected(destination) }
            .testTag("nav_tab_${destination.name.lowercase()}"),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = if (isSelected) selectedIcon else unselectedIcon,
              contentDescription = destination.label,
              tint = iconColor,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = destination.label,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
              color = iconColor
            )
          }
        }
      }
    }
  }
}
