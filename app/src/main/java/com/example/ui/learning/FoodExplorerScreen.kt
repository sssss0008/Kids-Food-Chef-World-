package com.example.ui.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.FoodCardItem
import com.example.data.model.KitchenToolItem
import com.example.ui.components.ChefCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodExplorerScreen(
  mode: String, // "fruits", "veggies", "tools"
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val title = when (mode) {
    "fruits" -> "Fruit Learning 🍎"
    "veggies" -> "Vegetable Learning 🥦"
    else -> "Kitchen Tools Learning 🥣"
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("explorer_back_button")) {
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
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Surface(
          shape = RoundedCornerShape(18.dp),
          color = when (mode) {
            "fruits" -> ChefCoralContainer
            "veggies" -> ChefGreenContainer
            else -> ChefGoldContainer
          },
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = when (mode) {
              "fruits" -> "🍎 Tap any fruit to explore its vitamins, colors and fun nature facts!"
              "veggies" -> "🥦 Discover how crunchy vegetables give you super vision and energy!"
              else -> "🥣 Learn how master chefs use spoons, whisks and rolling pins safely!"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(14.dp),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      if (mode == "tools") {
        items(viewModel.repository.kitchenToolsList) { tool ->
          KitchenToolCard(tool = tool, onClick = { viewModel.playClick() })
        }
      } else {
        val list = if (mode == "fruits") viewModel.repository.fruitsList else viewModel.repository.veggiesList
        items(list) { food ->
          FoodCard(food = food, onClick = { viewModel.playClick() })
        }
      }
    }
  }
}

@Composable
private fun FoodCard(food: FoodCardItem, onClick: () -> Unit) {
  ChefCard(
    backgroundColor = MaterialTheme.colorScheme.surface,
    cornerRadius = 22.dp,
    elevation = 3.dp,
    onClick = onClick
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(ChefCoralContainer),
        contentAlignment = Alignment.Center
      ) {
        Text(text = food.emoji, fontSize = 38.sp)
      }

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = food.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
          Surface(shape = RoundedCornerShape(10.dp), color = ChefGoldContainer) {
            Text(text = food.color, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(text = food.funFact, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)

        Spacer(modifier = Modifier.height(6.dp))
        Surface(shape = RoundedCornerShape(8.dp), color = ChefGreenContainer) {
          Text(text = "💪 ${food.vitamins}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ChefGreenDark, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
        }
      }
    }
  }
}

@Composable
private fun KitchenToolCard(tool: KitchenToolItem, onClick: () -> Unit) {
  ChefCard(
    backgroundColor = MaterialTheme.colorScheme.surface,
    cornerRadius = 22.dp,
    elevation = 3.dp,
    onClick = onClick
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(ChefGoldContainer),
        contentAlignment = Alignment.Center
      ) {
        Text(text = tool.emoji, fontSize = 36.sp)
      }

      Column(modifier = Modifier.weight(1f)) {
        Text(text = tool.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "Purpose: ${tool.purpose}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Fun Fact: ${tool.funFact}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))
        Surface(shape = RoundedCornerShape(8.dp), color = ChefCoralContainer) {
          Text(text = "🧤 ${tool.safetyTip}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ChefCoralDark, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
        }
      }
    }
  }
}
