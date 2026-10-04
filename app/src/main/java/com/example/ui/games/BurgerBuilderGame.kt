package com.example.ui.games

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import com.example.ui.components.ChefCard
import com.example.ui.components.ChefPrimaryButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChefViewModel

data class BurgerLayer(
  val id: String,
  val name: String,
  val emoji: String,
  val color: Color,
  val heightDp: Int = 18,
  val widthDp: Int = 180
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BurgerBuilderGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val availableIngredients = listOf(
    BurgerLayer("sauce", "Special Sauce", "🥫", Color(0xFFE53935), heightDp = 10, widthDp = 160),
    BurgerLayer("patty", "Juicy Patty", "🥩", Color(0xFF4E342E), heightDp = 22, widthDp = 185),
    BurgerLayer("cheese", "Cheddar Cheese", "🧀", Color(0xFFFFD54F), heightDp = 12, widthDp = 180),
    BurgerLayer("lettuce", "Crisp Lettuce", "🥬", Color(0xFF43A047), heightDp = 14, widthDp = 190),
    BurgerLayer("tomato", "Ripe Tomato", "🍅", Color(0xFFD32F2F), heightDp = 14, widthDp = 175),
    BurgerLayer("onion", "Sweet Onion", "🧅", Color(0xFFCE93D8), heightDp = 12, widthDp = 165),
    BurgerLayer("pickles", "Crunchy Pickles", "🥒", Color(0xFF689F38), heightDp = 12, widthDp = 170)
  )

  // Burger stack starting with bottom bun
  var stackedLayers by remember {
    mutableStateOf(listOf(BurgerLayer("bottom_bun", "Bottom Bun", "🍞", Color(0xFFD78839), heightDp = 24, widthDp = 180)))
  }
  var hasTopBun by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "Burger Builder 🍔", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("burger_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(
            onClick = {
              viewModel.playClick()
              stackedLayers = listOf(BurgerLayer("bottom_bun", "Bottom Bun", "🍞", Color(0xFFD78839), heightDp = 24, widthDp = 180))
              hasTopBun = false
            }
          ) {
            Icon(Icons.Default.Delete, contentDescription = "Clear burger")
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
      // Guide Banner
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChefGoldContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = if (hasTopBun) "🍔 Your tall delicious burger is ready to serve!" else "Tap ingredients to stack your custom burger!",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = Color(0xFFE65100),
          modifier = Modifier.padding(14.dp)
        )
      }

      // Burger Plate & Stack Display
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(280.dp),
        contentAlignment = Alignment.BottomCenter
      ) {
        // Plate
        Box(
          modifier = Modifier
            .size(width = 240.dp, height = 30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFFE0E0E0))
            .border(2.dp, Color(0xFFBDBDBD), RoundedCornerShape(15.dp))
        )

        // Stacked ingredients column
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(bottom = 18.dp)
        ) {
          // Top Bun if added
          if (hasTopBun) {
            Surface(
              shape = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp, bottomStart = 8.dp, bottomEnd = 8.dp),
              color = Color(0xFFE08E45),
              modifier = Modifier
                .size(width = 185.dp, height = 36.dp)
                .border(2.dp, Color(0xFFB0621A), RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
            ) {
              Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "• • •", color = Color(0xFFFFF9C4), fontWeight = FontWeight.ExtraBold)
              }
            }
            Spacer(modifier = Modifier.height(2.dp))
          }

          // Middle Layers in reverse (top to bottom)
          stackedLayers.reversed().forEach { layer ->
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = layer.color,
              modifier = Modifier
                .size(width = layer.widthDp.dp, height = layer.heightDp.dp)
                .padding(vertical = 1.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = layer.emoji, fontSize = 12.sp)
              }
            }
          }
        }
      }

      // Ingredient Palette
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 24.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(text = "Tap to Add Ingredient:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          items(availableIngredients) { ing ->
            Surface(
              shape = RoundedCornerShape(18.dp),
              color = ing.color.copy(alpha = 0.2f),
              modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .clickable(enabled = !hasTopBun) {
                  viewModel.playClick()
                  stackedLayers = stackedLayers + ing
                }
                .border(1.dp, ing.color.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            ) {
              Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(text = ing.emoji, fontSize = 24.sp)
                Text(text = ing.name, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Top Bun Button
        if (!hasTopBun) {
          OutlinedButton(
            onClick = {
              viewModel.playClick()
              hasTopBun = true
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
          ) {
            Text("Add Top Sesame Bun 🍞", fontWeight = FontWeight.Bold)
          }
        }
      }

      // Action Button
      ChefPrimaryButton(
        text = if (hasTopBun) "SERVE BURGER! ⭐" else "ADD TOP BUN & FINISH",
        onClick = {
          if (!hasTopBun) {
            hasTopBun = true
            viewModel.playClick()
          } else {
            viewModel.completeGame(
              stars = 15,
              xp = 30,
              gameName = "Burger Builder",
              playAgainAction = {
                stackedLayers = listOf(BurgerLayer("bottom_bun", "Bottom Bun", "🍞", Color(0xFFD78839), heightDp = 24, widthDp = 180))
                hasTopBun = false
              }
            )
          }
        },
        containerColor = if (hasTopBun) ChefGreen else MaterialTheme.colorScheme.primary,
        testTag = "burger_action_button"
      )
    }
  }
}
