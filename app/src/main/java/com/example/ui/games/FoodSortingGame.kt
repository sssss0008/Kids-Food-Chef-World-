package com.example.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.ui.components.ChefCard
import com.example.ui.components.ChefPrimaryButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

data class SortableItem(val id: String, val name: String, val emoji: String, val correctCategory: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSortingGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val allItems = remember {
    listOf(
      SortableItem("apple", "Apple", "🍎", "Fruit"),
      SortableItem("carrot", "Carrot", "🥕", "Veggie"),
      SortableItem("banana", "Banana", "🍌", "Fruit"),
      SortableItem("broccoli", "Broccoli", "🥦", "Veggie"),
      SortableItem("cupcake", "Cupcake", "🧁", "Treat"),
      SortableItem("cookie", "Cookie", "🍪", "Treat"),
      SortableItem("whisk", "Whisk", "🥣", "Tool"),
      SortableItem("rolling_pin", "Rolling Pin", "🪵", "Tool")
    )
  }

  var currentIndex by remember { mutableIntStateOf(0) }
  var sortedCount by remember { mutableIntStateOf(0) }
  var feedbackMessage by remember { mutableStateOf("Sort each item into its matching basket!") }

  val currentItem = allItems.getOrNull(currentIndex)
  val isFinished = currentIndex >= allItems.size

  fun handleSort(selectedCategory: String) {
    if (currentItem == null) return
    if (currentItem.correctCategory == selectedCategory) {
      viewModel.playClick()
      sortedCount++
      feedbackMessage = "🌟 Correct! That is a ${currentItem.name}!"
      currentIndex++
    } else {
      feedbackMessage = "Almost! ${currentItem.name} belongs to ${currentItem.correctCategory}."
      currentIndex++
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Food Sorting Game 🧺", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("sort_back_button")) {
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
      // Top Feedback
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChefPurpleContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = feedbackMessage,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = Color(0xFF4A148C),
          modifier = Modifier.padding(14.dp)
        )
      }

      // Center Item Display
      if (!isFinished && currentItem != null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier
              .size(130.dp)
              .clip(CircleShape)
              .background(ChefCoralContainer)
              .border(3.dp, ChefCoral, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(text = currentItem.emoji, fontSize = 64.sp)
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = currentItem.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold
          )
          Text(
            text = "Item ${currentIndex + 1} of ${allItems.size}",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "🎉", fontSize = 64.sp)
          Text(text = "Great Sorting!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
          Text(text = "You sorted $sortedCount of ${allItems.size} items accurately!", fontSize = 14.sp)
        }
      }

      // 4 Baskets to tap
      if (!isFinished) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            BasketButton(
              title = "Fruits 🍎",
              color = ChefCoralContainer,
              modifier = Modifier.weight(1f),
              onClick = { handleSort("Fruit") }
            )
            BasketButton(
              title = "Veggies 🥦",
              color = ChefGreenContainer,
              modifier = Modifier.weight(1f),
              onClick = { handleSort("Veggie") }
            )
          }
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            BasketButton(
              title = "Treats 🧁",
              color = ChefPinkContainer,
              modifier = Modifier.weight(1f),
              onClick = { handleSort("Treat") }
            )
            BasketButton(
              title = "Kitchen Tools 🥣",
              color = ChefGoldContainer,
              modifier = Modifier.weight(1f),
              onClick = { handleSort("Tool") }
            )
          }
        }
      } else {
        ChefPrimaryButton(
          text = "CLAIM REWARDS! ⭐",
          onClick = {
            viewModel.completeGame(
              stars = 20,
              xp = 35,
              gameName = "Food Sorting",
              playAgainAction = {
                currentIndex = 0
                sortedCount = 0
                feedbackMessage = "Sort each item into its matching basket!"
              }
            )
          },
          containerColor = ChefGreen
        )
      }
    }
  }
}

@Composable
private fun BasketButton(title: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = color,
    modifier = modifier
      .height(72.dp)
      .clickable { onClick() }
      .border(1.5.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
  ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
    }
  }
}
