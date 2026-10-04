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
fun HealthyMealBuilderGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var fruitChoice by remember { mutableStateOf<String?>("🍎 Apple") }
  var veggieChoice by remember { mutableStateOf<String?>("🥦 Broccoli") }
  var grainChoice by remember { mutableStateOf<String?>("🍚 Brown Rice") }
  var proteinChoice by remember { mutableStateOf<String?>("🍗 Chicken") }

  val fruits = listOf("🍎 Apple", "🍌 Banana", "🍓 Strawberries", "🍇 Grapes")
  val veggies = listOf("🥦 Broccoli", "🥕 Carrots", "🥬 Spinach", "🌽 Sweet Corn")
  val grains = listOf("🍚 Brown Rice", "🍞 Whole Wheat", "🍝 Pasta", "🥔 Potato")
  val proteins = listOf("🍗 Chicken", "🥚 Boiled Egg", "🐟 Fish", "🫛 Tofu / Beans")

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Healthy Meal Builder 🥗", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("healthy_meal_back_button")) {
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
        color = ChefGreenContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "🌈 Build a balanced rainbow plate: half fruits & veggies, plus grains & protein!",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = ChefGreenDark,
          modifier = Modifier.padding(14.dp)
        )
      }

      // Visual Plate (Divided into 4 quadrants)
      Surface(
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = Modifier
          .size(240.dp)
          .border(6.dp, Color(0xFFE0E0E0), CircleShape)
      ) {
        Column(modifier = Modifier.fillMaxSize()) {
          // Top half: Fruit & Veggie
          Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Box(
              modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(ChefCoralContainer.copy(alpha = 0.6f)),
              contentAlignment = Alignment.Center
            ) {
              Text(text = fruitChoice ?: "+ Fruit", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Box(
              modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(ChefGreenContainer.copy(alpha = 0.6f)),
              contentAlignment = Alignment.Center
            ) {
              Text(text = veggieChoice ?: "+ Veggie", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }
          // Bottom half: Grains & Protein
          Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Box(
              modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(ChefGoldContainer.copy(alpha = 0.6f)),
              contentAlignment = Alignment.Center
            ) {
              Text(text = grainChoice ?: "+ Grain", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Box(
              modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(Color(0xFFFFCCBC).copy(alpha = 0.6f)),
              contentAlignment = Alignment.Center
            ) {
              Text(text = proteinChoice ?: "+ Protein", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }
        }
      }

      // Ingredients Selector
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 24.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(text = "Tap to Switch Foods on Your Plate:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(fruits) { f ->
            FilterChip(selected = fruitChoice == f, onClick = { fruitChoice = f }, label = { Text(f) })
          }
          items(veggies) { v ->
            FilterChip(selected = veggieChoice == v, onClick = { veggieChoice = v }, label = { Text(v) })
          }
          items(grains) { g ->
            FilterChip(selected = grainChoice == g, onClick = { grainChoice = g }, label = { Text(g) })
          }
          items(proteins) { p ->
            FilterChip(selected = proteinChoice == p, onClick = { proteinChoice = p }, label = { Text(p) })
          }
        }
      }

      // Finish Button
      ChefPrimaryButton(
        text = "SERVE BALANCED PLATE! ⭐",
        onClick = {
          viewModel.completeGame(
            stars = 20,
            xp = 35,
            gameName = "Healthy Meal Builder",
            playAgainAction = {
              fruitChoice = "🍎 Apple"
              veggieChoice = "🥦 Broccoli"
            }
          )
        },
        containerColor = ChefGreen,
        testTag = "serve_healthy_meal_button"
      )
    }
  }
}
