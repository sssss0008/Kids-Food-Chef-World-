package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameCategory
import com.example.data.model.GameItem
import com.example.ui.components.ChefCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

@Composable
fun GamesScreen(
  viewModel: ChefViewModel,
  onNavigateToGame: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val filteredGames by viewModel.filteredGames.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Title & Subtitle
    item {
      Column {
        Text(
          text = "Choose Your Game",
          style = MaterialTheme.typography.displaySmall,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Pick any recipe or cooking challenge to play & earn stars!",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Search Bar
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { viewModel.setSearchQuery(it) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("games_search_input"),
        placeholder = { Text("Search delicious games...", fontSize = 15.sp) },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { viewModel.setSearchQuery("") }) {
              Icon(Icons.Default.Clear, contentDescription = "Clear")
            }
          }
        },
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedContainerColor = MaterialTheme.colorScheme.surface,
          focusedBorderColor = MaterialTheme.colorScheme.primary,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        ),
        singleLine = true
      )
    }

    // Category Filter Chips
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(GameCategory.entries) { category ->
          val isSelected = selectedCategory == category
          FilterChip(
            selected = isSelected,
            onClick = { viewModel.selectCategory(category) },
            label = {
              Text(
                text = "${category.iconEmoji} ${category.displayName}",
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            },
            shape = RoundedCornerShape(18.dp),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = ChefCoral,
              selectedLabelColor = Color.White,
              containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.testTag("filter_chip_${category.name.lowercase()}")
          )
        }
      }
    }

    // Empty state if nothing matches search
    if (filteredGames.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(text = "🔍", fontSize = 48.sp)
            Text(
              text = "No games found!",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Try searching for Pizza, Burger, Cake or Fruit!",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // Games List / Cards
    items(filteredGames) { game ->
      GameListItemCard(
        game = game,
        onPlay = { onNavigateToGame(game.route) }
      )
    }
  }
}

@Composable
fun GameListItemCard(
  game: GameItem,
  onPlay: () -> Unit
) {
  ChefCard(
    backgroundColor = MaterialTheme.colorScheme.surface,
    cornerRadius = 24.dp,
    elevation = 4.dp,
    onClick = onPlay
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Game Illustration / Icon Box
      Box(
        modifier = Modifier
          .size(74.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(Color(game.colorHex).copy(alpha = 0.2f))
          .border(1.5.dp, Color(game.colorHex).copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = game.iconEmoji, fontSize = 38.sp)
      }

      // Details
      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = game.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = when (game.difficulty) {
              "Easy" -> ChefGreenContainer
              "Medium" -> ChefGoldContainer
              else -> ChefCoralContainer
            }
          ) {
            Text(
              text = game.difficulty,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = when (game.difficulty) {
                "Easy" -> ChefGreenDark
                "Medium" -> Color(0xFFE65100)
                else -> ChefCoralDark
              },
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = game.description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Rewards indicator
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "⭐ +${game.starsReward} Stars",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE65100)
          )
          Text(
            text = "•",
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "🔥 +${game.xpReward} XP",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ChefCoralDark
          )
        }
      }

      // Play Button
      Button(
        onClick = onPlay,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.testTag("play_button_${game.id}")
      ) {
        Text(
          text = "PLAY",
          fontWeight = FontWeight.ExtraBold,
          fontSize = 14.sp
        )
      }
    }
  }
}
