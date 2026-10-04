package com.example.ui.learning

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MemoryCard(val id: Int, val emoji: String, var isFlipped: Boolean = false, var isMatched: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodMemoryScreen(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()
  var difficulty by remember { mutableStateOf("Easy") } // Easy (6), Medium (8), Challenge (12)

  val foodSymbols = listOf("🍎", "🍕", "🥕", "🍓", "🍔", "🍦", "🍇", "🥑")

  fun generateCards(diff: String): List<MemoryCard> {
    val pairCount = when (diff) {
      "Easy" -> 3 // 6 cards
      "Medium" -> 4 // 8 cards
      else -> 6 // 12 cards
    }
    val selected = foodSymbols.take(pairCount)
    val doubleList = (selected + selected).shuffled()
    return doubleList.mapIndexed { index, emoji ->
      MemoryCard(id = index, emoji = emoji)
    }
  }

  var cards by remember { mutableStateOf(generateCards(difficulty)) }
  var flippedIndices by remember { mutableStateOf(listOf<Int>()) }
  var moves by remember { mutableIntStateOf(0) }
  var isChecking by remember { mutableStateOf(false) }

  val allMatched = cards.isNotEmpty() && cards.all { it.isMatched }

  fun onCardClick(index: Int) {
    if (isChecking || cards[index].isFlipped || cards[index].isMatched) return
    viewModel.playClick()

    val newCards = cards.toMutableList()
    newCards[index] = newCards[index].copy(isFlipped = true)
    cards = newCards

    val currentFlipped = flippedIndices + index
    flippedIndices = currentFlipped

    if (currentFlipped.size == 2) {
      moves++
      isChecking = true
      scope.launch {
        delay(600)
        val first = currentFlipped[0]
        val second = currentFlipped[1]
        val updated = cards.toMutableList()

        if (updated[first].emoji == updated[second].emoji) {
          viewModel.playStar()
          updated[first] = updated[first].copy(isMatched = true)
          updated[second] = updated[second].copy(isMatched = true)
        } else {
          updated[first] = updated[first].copy(isFlipped = false)
          updated[second] = updated[second].copy(isFlipped = false)
        }
        cards = updated
        flippedIndices = emptyList()
        isChecking = false
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Food Memory Game 🧠", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("memory_back_button")) {
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
      // Difficulty Chips & Moves Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          listOf("Easy", "Medium", "Challenge").forEach { diff ->
            FilterChip(
              selected = difficulty == diff,
              onClick = {
                viewModel.playClick()
                difficulty = diff
                cards = generateCards(diff)
                flippedIndices = emptyList()
                moves = 0
              },
              label = { Text(diff, fontSize = 12.sp) }
            )
          }
        }

        Surface(shape = RoundedCornerShape(12.dp), color = ChefCoralContainer) {
          Text(
            text = "Moves: $moves",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = ChefCoralDark,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      // Memory Grid
      LazyVerticalGrid(
        columns = GridCells.Fixed(if (cards.size > 8) 4 else 3),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(vertical = 12.dp)
      ) {
        itemsIndexed(cards) { index, card ->
          val rotation by animateFloatAsState(
            targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
            animationSpec = tween(300),
            label = "card_rot"
          )

          Box(
            modifier = Modifier
              .aspectRatio(1f)
              .clip(RoundedCornerShape(18.dp))
              .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
              }
              .background(
                if (card.isMatched) ChefGreenContainer
                else if (card.isFlipped) Color.White
                else ChefCoral
              )
              .border(
                2.dp,
                if (card.isMatched) ChefGreen else ChefGold,
                RoundedCornerShape(18.dp)
              )
              .clickable { onCardClick(index) },
            contentAlignment = Alignment.Center
          ) {
            if (rotation > 90f) {
              Text(
                text = card.emoji,
                fontSize = 32.sp,
                modifier = Modifier.graphicsLayer { rotationY = 180f }
              )
            } else {
              Text(text = "👨‍🍳", fontSize = 28.sp)
            }
          }
        }
      }

      // Bottom Button
      if (allMatched) {
        ChefPrimaryButton(
          text = "CLAIM REWARDS! ⭐",
          onClick = {
            viewModel.completeGame(
              stars = 20,
              xp = 35,
              gameName = "Food Memory Game",
              playAgainAction = {
                cards = generateCards(difficulty)
                flippedIndices = emptyList()
                moves = 0
              }
            )
          },
          containerColor = ChefGreen,
          testTag = "memory_claim_button"
        )
      } else {
        ChefSecondaryButton(
          text = "RESTART CARDS 🔄",
          onClick = {
            viewModel.playClick()
            cards = generateCards(difficulty)
            flippedIndices = emptyList()
            moves = 0
          }
        )
      }
    }
  }
}
