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

data class RestaurantCustomer(val name: String, val emoji: String, val order: String, val orderEmoji: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantManagerGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val customers = listOf(
    RestaurantCustomer("Bunny Hop", "🐰", "Crispy Pizza", "🍕"),
    RestaurantCustomer("Barnaby Bear", "🐻", "Juicy Burger", "🍔"),
    RestaurantCustomer("Fiona Fox", "🦊", "Berry Smoothie", "🍹"),
    RestaurantCustomer("Pip Panda", "🐼", "Celebration Cake", "🎂")
  )

  var currentCustomerIndex by remember { mutableIntStateOf(0) }
  var isCooking by remember { mutableStateOf(false) }
  var isFoodReady by remember { mutableStateOf(false) }
  var coinsEarned by remember { mutableIntStateOf(0) }
  var unlockedDecorations by remember { mutableStateOf(setOf<String>()) }

  val customer = customers[currentCustomerIndex % customers.size]

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Little Chef Bistro 🏪", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("restaurant_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = ChefGoldContainer,
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Text(
              text = "🪙 $coinsEarned Coins",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFFE65100),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
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
      // Bistro Atmosphere Banner
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = ChefCoralContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(text = "🏪", fontSize = 28.sp)
            Column {
              Text(text = "Little Chef Bistro", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = ChefCoralDark)
              Text(text = "Table 1 Guest: ${customer.name}", fontSize = 12.sp)
            }
          }
          Row {
            if (unlockedDecorations.contains("lights")) Text(text = "💡", fontSize = 20.sp)
            if (unlockedDecorations.contains("flowers")) Text(text = "💐", fontSize = 20.sp)
            if (unlockedDecorations.contains("trophy")) Text(text = "🏆", fontSize = 20.sp)
          }
        }
      }

      // Guest Table Visual Area
      Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFFFF3E0),
        modifier = Modifier
          .fillMaxWidth()
          .height(220.dp)
          .border(2.dp, Color(0xFFFFB74D), RoundedCornerShape(24.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text = customer.emoji, fontSize = 54.sp)
          Spacer(modifier = Modifier.height(6.dp))

          // Thought Bubble with Order
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 3.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(text = "Order: ${customer.orderEmoji} ${customer.order}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
          }
        }
      }

      // Kitchen Work Station
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 24.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(text = "Kitchen Cooking Station:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (!isFoodReady) {
            Button(
              onClick = {
                viewModel.playSizzle()
                isFoodReady = true
              },
              shape = RoundedCornerShape(16.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ChefCoral)
            ) {
              Text("🍳 Cook ${customer.order}!")
            }
          } else {
            Surface(shape = RoundedCornerShape(12.dp), color = ChefGreenContainer) {
              Text(
                text = "✅ ${customer.orderEmoji} Ready to serve!",
                color = ChefGreenDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Decorations Shop
        Text(text = "Bistro Upgrades (Costs 30 Coins):", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          FilterChip(
            selected = unlockedDecorations.contains("lights"),
            onClick = {
              if (coinsEarned >= 30 && !unlockedDecorations.contains("lights")) {
                coinsEarned -= 30
                unlockedDecorations = unlockedDecorations + "lights"
              }
            },
            label = { Text("💡 Cozy Lights") }
          )
          FilterChip(
            selected = unlockedDecorations.contains("flowers"),
            onClick = {
              if (coinsEarned >= 30 && !unlockedDecorations.contains("flowers")) {
                coinsEarned -= 30
                unlockedDecorations = unlockedDecorations + "flowers"
              }
            },
            label = { Text("💐 Flower Tables") }
          )
        }
      }

      // Serve / Next Guest Action
      ChefPrimaryButton(
        text = if (isFoodReady) "SERVE TO ${customer.name.uppercase()}! ⭐" else "COOK DISH FIRST",
        onClick = {
          if (isFoodReady) {
            viewModel.playClick()
            coinsEarned += 20
            isFoodReady = false
            currentCustomerIndex++
            if (currentCustomerIndex >= 4) {
              viewModel.completeGame(
                stars = 25,
                xp = 45,
                gameName = "Restaurant Manager",
                playAgainAction = {
                  currentCustomerIndex = 0
                }
              )
            }
          }
        },
        enabled = isFoodReady,
        containerColor = ChefGreen,
        testTag = "restaurant_serve_button"
      )
    }
  }
}
