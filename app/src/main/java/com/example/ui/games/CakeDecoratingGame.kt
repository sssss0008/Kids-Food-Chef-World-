package com.example.ui.games

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CakeDecoratingGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedFlavor by remember { mutableStateOf("Vanilla Dream") }
  var frostingColor by remember { mutableStateOf(Color(0xFFF8BBD0)) } // Pastel Pink
  var selectedToppings by remember { mutableStateOf(setOf("🍓", "✨")) }
  var candleCount by remember { mutableIntStateOf(3) }
  var candlesLit by remember { mutableStateOf(true) }

  val flavors = listOf(
    "Vanilla Dream" to Color(0xFFFFF9C4),
    "Chocolate Rich" to Color(0xFF5D4037),
    "Strawberry Sweet" to Color(0xFFFFCDD2),
    "Matcha Green" to Color(0xFFC8E6C9)
  )

  val frostingPalette = listOf(
    "Pastel Pink" to Color(0xFFF8BBD0),
    "Sky Mint" to Color(0xFFB2EBF2),
    "Sunny Lemon" to Color(0xFFFFF59D),
    "Lavender" to Color(0xFFE1BEE7),
    "Sweet Cream" to Color(0xFFFFFFFF),
    "Dark Choco" to Color(0xFF3E2723)
  )

  val toppings = listOf("🍓", "🍒", "🫐", "✨", "🍫", "🌈", "⭐")

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Cake Decorating 🎂", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("cake_back_button")) {
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
        color = ChefPinkContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "🎨 Design your dream celebration cake! Tap flavors, frosting & candies.",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = ChefPink,
          modifier = Modifier.padding(14.dp)
        )
      }

      // Visual Tiered Cake Display
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp),
        contentAlignment = Alignment.BottomCenter
      ) {
        // Cake Stand
        Box(
          modifier = Modifier
            .size(width = 240.dp, height = 24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE0E0E0))
            .border(2.dp, Color(0xFFBDBDBD), RoundedCornerShape(12.dp))
        )

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(bottom = 20.dp)
        ) {
          // Candles Row
          if (candleCount > 0) {
            Row(
              horizontalArrangement = Arrangement.spacedBy(14.dp),
              modifier = Modifier.padding(bottom = 2.dp)
            ) {
              repeat(candleCount) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = if (candlesLit) "🔥" else "💨",
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                      viewModel.playClick()
                      candlesLit = !candlesLit
                    }
                  )
                  Box(
                    modifier = Modifier
                      .size(width = 6.dp, height = 24.dp)
                      .clip(RoundedCornerShape(3.dp))
                      .background(ChefSkyBlue)
                  )
                }
              }
            }
          }

          // Top Tier
          Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = frostingColor,
            modifier = Modifier
              .size(width = 130.dp, height = 55.dp)
              .border(2.dp, frostingColor.copy(alpha = 0.8f), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
          ) {
            Row(
              modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically
            ) {
              selectedToppings.take(3).forEach { top ->
                Text(text = top, fontSize = 16.sp, modifier = Modifier.padding(2.dp))
              }
            }
          }

          // Bottom Tier
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = frostingColor,
            modifier = Modifier
              .size(width = 200.dp, height = 75.dp)
              .border(2.dp, frostingColor.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
          ) {
            Row(
              modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
              horizontalArrangement = Arrangement.SpaceAround,
              verticalAlignment = Alignment.CenterVertically
            ) {
              selectedToppings.forEach { top ->
                Text(text = top, fontSize = 20.sp)
              }
            }
          }
        }
      }

      // Customization Controls
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 24.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        // Frosting Colors
        Text(text = "Frosting Color:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(frostingPalette) { (name, color) ->
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                  width = if (frostingColor == color) 3.dp else 1.dp,
                  color = if (frostingColor == color) ChefCoral else Color.Gray.copy(alpha = 0.4f),
                  shape = CircleShape
                )
                .clickable {
                  viewModel.playClick()
                  frostingColor = color
                }
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Toppings Selection
        Text(text = "Add Yummy Toppings:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(toppings) { emoji ->
            val isSelected = selectedToppings.contains(emoji)
            FilterChip(
              selected = isSelected,
              onClick = {
                viewModel.playClick()
                selectedToppings = if (isSelected) selectedToppings - emoji else selectedToppings + emoji
              },
              label = { Text(text = emoji, fontSize = 18.sp) },
              shape = RoundedCornerShape(16.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Candles count stepper
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Birthday Candles: $candleCount", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
              onClick = { if (candleCount > 0) candleCount-- },
              shape = CircleShape,
              contentPadding = PaddingValues(0.dp),
              modifier = Modifier.size(34.dp)
            ) { Text("-", fontSize = 16.sp) }
            Button(
              onClick = { if (candleCount < 6) candleCount++ },
              shape = CircleShape,
              contentPadding = PaddingValues(0.dp),
              modifier = Modifier.size(34.dp)
            ) { Text("+", fontSize = 16.sp) }
          }
        }
      }

      // Serve & Complete Button
      ChefPrimaryButton(
        text = "CELEBRATE CAKE! 🎉",
        onClick = {
          viewModel.completeGame(
            stars = 20,
            xp = 35,
            gameName = "Cake Decorating",
            playAgainAction = {
              selectedToppings = setOf("🍓", "✨")
              candleCount = 3
              candlesLit = true
            }
          )
        },
        containerColor = ChefPink,
        testTag = "cake_celebrate_button"
      )
    }
  }
}
