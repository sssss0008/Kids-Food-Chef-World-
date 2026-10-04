package com.example.ui.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

data class MatchTile(val id: Int, val name: String, val emoji: String, val pairId: Int, var isMatched: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodMatchingScreen(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val basePairs = listOf(
    "Apple" to "🍎",
    "Carrot" to "🥕",
    "Pizza" to "🍕",
    "Cookie" to "🍪",
    "Watermelon" to "🍉",
    "Ice Cream" to "🍨"
  )

  fun buildTiles(): List<MatchTile> {
    val list = mutableListOf<MatchTile>()
    var idCounter = 0
    basePairs.forEachIndexed { pairIndex, (name, emoji) ->
      list.add(MatchTile(id = idCounter++, name = name, emoji = emoji, pairId = pairIndex))
      list.add(MatchTile(id = idCounter++, name = name, emoji = emoji, pairId = pairIndex))
    }
    return list.shuffled()
  }

  var tiles by remember { mutableStateOf(buildTiles()) }
  var selectedTile by remember { mutableStateOf<MatchTile?>(null) }
  var matchesFound by remember { mutableIntStateOf(0) }

  val isAllMatched = matchesFound >= basePairs.size

  fun onTileSelect(tile: MatchTile) {
    if (tile.isMatched) return
    viewModel.playClick()

    if (selectedTile == null) {
      selectedTile = tile
    } else {
      if (selectedTile!!.id != tile.id && selectedTile!!.pairId == tile.pairId) {
        viewModel.playStar()
        matchesFound++
        tiles = tiles.map {
          if (it.pairId == tile.pairId) it.copy(isMatched = true) else it
        }
        selectedTile = null
      } else {
        selectedTile = null
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Food Matching Game 🃏", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("matching_back_button")) {
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
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChefCoralContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Tap matching foods to pair them up!", fontWeight = FontWeight.Bold, color = ChefCoralDark, fontSize = 13.sp)
          Text(text = "Matches: $matchesFound / ${basePairs.size}", fontWeight = FontWeight.ExtraBold, color = ChefCoralDark, fontSize = 13.sp)
        }
      }

      // Tiles Grid
      LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(vertical = 12.dp)
      ) {
        items(tiles) { tile ->
          val isSelected = selectedTile?.id == tile.id
          Surface(
            shape = RoundedCornerShape(18.dp),
            color = when {
              tile.isMatched -> ChefGreenContainer
              isSelected -> ChefGoldContainer
              else -> MaterialTheme.colorScheme.surface
            },
            modifier = Modifier
              .aspectRatio(1f)
              .clip(RoundedCornerShape(18.dp))
              .clickable(enabled = !tile.isMatched) { onTileSelect(tile) }
              .border(
                width = if (isSelected) 3.dp else 1.5.dp,
                color = when {
                  tile.isMatched -> ChefGreen
                  isSelected -> ChefGold
                  else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                },
                shape = RoundedCornerShape(18.dp)
              )
          ) {
            Column(
              modifier = Modifier.fillMaxSize(),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Text(text = tile.emoji, fontSize = 34.sp)
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = tile.name,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = if (tile.isMatched) ChefGreenDark else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      if (isAllMatched) {
        ChefPrimaryButton(
          text = "CLAIM REWARDS! ⭐",
          onClick = {
            viewModel.completeGame(
              stars = 20,
              xp = 35,
              gameName = "Food Matching",
              playAgainAction = {
                tiles = buildTiles()
                matchesFound = 0
                selectedTile = null
              }
            )
          },
          containerColor = ChefGreen,
          testTag = "matching_claim_button"
        )
      } else {
        ChefSecondaryButton(
          text = "RESET TILES 🔄",
          onClick = {
            viewModel.playClick()
            tiles = buildTiles()
            matchesFound = 0
            selectedTile = null
          }
        )
      }
    }
  }
}
