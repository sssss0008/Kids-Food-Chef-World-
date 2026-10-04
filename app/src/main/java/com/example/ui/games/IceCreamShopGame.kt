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

data class IceCreamFlavor(val id: String, val name: String, val color: Color, val emoji: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IceCreamShopGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val flavors = listOf(
    IceCreamFlavor("vanilla", "Creamy Vanilla", Color(0xFFFFF9C4), "🍦"),
    IceCreamFlavor("strawberry", "Sweet Strawberry", Color(0xFFFF80AB), "🍓"),
    IceCreamFlavor("mint", "Mint Choc Chip", Color(0xFFA7FFEB), "🌿"),
    IceCreamFlavor("chocolate", "Rich Chocolate", Color(0xFF6D4C41), "🍫"),
    IceCreamFlavor("blueberry", "Blueberry Swirl", Color(0xFF82B1FF), "🫐"),
    IceCreamFlavor("mango", "Mango Sherbet", Color(0xFFFFD54F), "🥭")
  )

  var selectedCone by remember { mutableStateOf("Waffle Cone 🧇") }
  var stackedScoops by remember { mutableStateOf(listOf(flavors[0])) }
  var hasCherry by remember { mutableStateOf(true) }
  var hasSprinkles by remember { mutableStateOf(true) }
  var customerHappy by remember { mutableStateOf(true) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Ice Cream Shop 🍨", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("ice_cream_back_button")) {
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
      // Customer Banner
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = ChefSkyBlueContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(text = if (customerHappy) "🐰" else "🐱", fontSize = 34.sp)
          Column {
            Text(text = "Bunny Customer:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0277BD))
            Text(text = "\"I'd love a tall ice cream cone with colorful scoops and a cherry on top!\"", fontSize = 12.sp)
          }
        }
      }

      // Visual Ice Cream Cone Stack
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp),
        contentAlignment = Alignment.BottomCenter
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(bottom = 10.dp)
        ) {
          // Cherry on top
          if (hasCherry) {
            Text(text = "🍒", fontSize = 28.sp)
          }

          // Scoops Stack (Reverse order: top to bottom)
          stackedScoops.reversed().forEach { scoop ->
            Box(
              modifier = Modifier
                .size(width = 85.dp, height = 48.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
                .background(scoop.color)
                .border(2.dp, scoop.color.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
              contentAlignment = Alignment.Center
            ) {
              if (hasSprinkles) {
                Text(text = "✨", fontSize = 16.sp)
              }
            }
          }

          // Cone
          Surface(
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp, topStart = 6.dp, topEnd = 6.dp),
            color = Color(0xFFD78839),
            modifier = Modifier
              .size(width = 65.dp, height = 75.dp)
              .border(2.dp, Color(0xFFB0621A), RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
          ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Text(text = "🧇", fontSize = 20.sp)
            }
          }
        }
      }

      // Scoop Controls Bar
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 24.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(text = "Tap to Add Scoop (Max 4):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(flavors) { flavor ->
            FilterChip(
              selected = false,
              onClick = {
                viewModel.playClick()
                if (stackedScoops.size < 4) {
                  stackedScoops = stackedScoops + flavor
                }
              },
              label = { Text("${flavor.emoji} ${flavor.name}") }
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Toppings:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterChip(
              selected = hasCherry,
              onClick = { hasCherry = !hasCherry },
              label = { Text("🍒 Cherry") }
            )
            FilterChip(
              selected = hasSprinkles,
              onClick = { hasSprinkles = !hasSprinkles },
              label = { Text("✨ Sprinkles") }
            )
          }
        }
      }

      // Serve Customer Button
      ChefPrimaryButton(
        text = "SERVE TO CUSTOMER! ⭐",
        onClick = {
          viewModel.completeGame(
            stars = 15,
            xp = 25,
            gameName = "Ice Cream Shop",
            playAgainAction = {
              stackedScoops = listOf(flavors[0])
              hasCherry = true
              hasSprinkles = true
            }
          )
        },
        containerColor = ChefPink,
        testTag = "serve_ice_cream_button"
      )
    }
  }
}
