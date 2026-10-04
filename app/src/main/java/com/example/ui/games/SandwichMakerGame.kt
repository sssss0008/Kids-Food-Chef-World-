package com.example.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
fun SandwichMakerGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedBread by remember { mutableStateOf("Whole Grain Bread 🍞") }
  var isGrilled by remember { mutableStateOf(false) }
  var isCutInHalf by remember { mutableStateOf(false) }
  var selectedFillings by remember { mutableStateOf(setOf("🧀", "🥬", "🍅")) }

  val fillings = listOf(
    "🧀" to "Cheese",
    "🥬" to "Lettuce",
    "🍅" to "Tomato",
    "🥒" to "Cucumber",
    "🥚" to "Boiled Egg",
    "🥑" to "Avocado"
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Sandwich Maker 🥪", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("sandwich_back_button")) {
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
        color = ChefGoldContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = if (isCutInHalf) "🥪 Cut diagonally into two delicious halves!" else "Pick breads, layer veggies, grill golden & slice!",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = Color(0xFFE65100),
          modifier = Modifier.padding(14.dp)
        )
      }

      // Visual Sandwich Area
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp),
        contentAlignment = Alignment.Center
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = if (isGrilled) Color(0xFFD78839) else Color(0xFFFFE0B2),
          modifier = Modifier
            .size(width = if (isCutInHalf) 260.dp else 220.dp, height = 150.dp)
            .border(3.dp, if (isGrilled) Color(0xFF8D6E63) else Color(0xFFFFB74D), RoundedCornerShape(20.dp))
        ) {
          Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (isCutInHalf) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🥪 Left Half", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row { selectedFillings.toList().take(3).forEach { Text(text = it, fontSize = 24.sp) } }
              }
              Box(modifier = Modifier.size(width = 2.dp, height = 100.dp).background(Color.White))
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🥪 Right Half", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row { selectedFillings.toList().takeLast(3).forEach { Text(text = it, fontSize = 24.sp) } }
              }
            } else {
              Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                if (isGrilled) {
                  Text(text = "♨️ Grill Marks", fontSize = 11.sp, color = Color(0xFF5D4037), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  selectedFillings.forEach { Text(text = it, fontSize = 28.sp) }
                }
              }
            }
          }
        }
      }

      // Controls Bar
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 24.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(text = "Choose Fresh Fillings:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(fillings) { (emoji, name) ->
            val isSelected = selectedFillings.contains(emoji)
            FilterChip(
              selected = isSelected,
              onClick = {
                viewModel.playClick()
                selectedFillings = if (isSelected) selectedFillings - emoji else selectedFillings + emoji
              },
              label = { Text("$emoji $name") }
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Button(
            onClick = {
              viewModel.playSizzle()
              isGrilled = !isGrilled
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isGrilled) ChefCoral else MaterialTheme.colorScheme.surfaceVariant)
          ) {
            Text(text = if (isGrilled) "🔥 Grilled" else "Toast / Grill", color = if (isGrilled) Color.White else MaterialTheme.colorScheme.onSurface)
          }

          Button(
            onClick = {
              viewModel.playClick()
              isCutInHalf = !isCutInHalf
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isCutInHalf) ChefGreen else MaterialTheme.colorScheme.surfaceVariant)
          ) {
            Text(text = if (isCutInHalf) "✂️ Cut in 2" else "Cut Diagonally", color = if (isCutInHalf) Color.White else MaterialTheme.colorScheme.onSurface)
          }
        }
      }

      // Serve Button
      ChefPrimaryButton(
        text = "SERVE SANDWICH! ⭐",
        onClick = {
          viewModel.completeGame(
            stars = 15,
            xp = 25,
            gameName = "Sandwich Maker",
            playAgainAction = {
              isGrilled = false
              isCutInHalf = false
            }
          )
        },
        containerColor = ChefGreen,
        testTag = "serve_sandwich_button"
      )
    }
  }
}
