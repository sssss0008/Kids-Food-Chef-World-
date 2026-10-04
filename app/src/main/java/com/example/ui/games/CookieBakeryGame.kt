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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookieBakeryGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()
  val cutters = listOf("⭐" to "Star", "❤️" to "Heart", "🧸" to "Teddy", "👨‍🍳" to "Chef Hat", "🌸" to "Flower")
  var selectedShape by remember { mutableStateOf("⭐") }
  var hasChocChips by remember { mutableStateOf(true) }
  var hasSprinkles by remember { mutableStateOf(true) }
  var isBaking by remember { mutableStateOf(false) }
  var bakeProgress by remember { mutableFloatStateOf(0f) }
  var isBaked by remember { mutableStateOf(false) }
  var isBoxed by remember { mutableStateOf(false) }

  fun bakeCookies() {
    isBaking = true
    viewModel.playSizzle()
    scope.launch {
      for (i in 1..100) {
        delay(30)
        bakeProgress = i / 100f
      }
      isBaking = false
      isBaked = true
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Cookie Bakery 🍪", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("cookie_back_button")) {
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
        Text(
          text = if (isBoxed) "🎁 Baked golden and packaged in a gift box!" else if (isBaked) "Baking complete! Tap Package into Box!" else "Pick a cutter shape, add chocolate chips, and bake!",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = ChefCoralDark,
          modifier = Modifier.padding(14.dp)
        )
      }

      // Visual Baking Tray
      Surface(
        shape = RoundedCornerShape(22.dp),
        color = if (isBoxed) Color(0xFFFFCCBC) else Color(0xFF90A4AE),
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .border(4.dp, if (isBoxed) ChefCoral else Color(0xFF607D8B), RoundedCornerShape(22.dp))
      ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          if (isBaking) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "🔥 Baking in Bakery Oven...", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
              Spacer(modifier = Modifier.height(10.dp))
              CircularProgressIndicator(progress = { bakeProgress }, color = ChefGold)
            }
          } else if (isBoxed) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "🎁", fontSize = 54.sp)
              Text(text = "Chef Cookie Gift Box!", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = ChefCoralDark)
            }
          } else {
            // 4 Fresh Cookies on Tray
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
              repeat(3) {
                Surface(
                  shape = RoundedCornerShape(18.dp),
                  color = if (isBaked) Color(0xFFD78839) else Color(0xFFFFE0B2),
                  modifier = Modifier
                    .size(76.dp)
                    .border(2.dp, if (isBaked) Color(0xFF8D6E63) else Color(0xFFFFB74D), RoundedCornerShape(18.dp))
                ) {
                  Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                  ) {
                    Text(text = selectedShape, fontSize = 26.sp)
                    Row {
                      if (hasChocChips) Text(text = "🍫", fontSize = 10.sp)
                      if (hasSprinkles) Text(text = "✨", fontSize = 10.sp)
                    }
                  }
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
        Text(text = "Choose Cookie Cutter Shape:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(cutters) { (shape, name) ->
            FilterChip(
              selected = selectedShape == shape,
              onClick = {
                viewModel.playClick()
                selectedShape = shape
              },
              label = { Text("$shape $name") }
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Toppings:", fontWeight = FontWeight.Bold)
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = hasChocChips, onClick = { hasChocChips = !hasChocChips }, label = { Text("🍫 Choco Chips") })
            FilterChip(selected = hasSprinkles, onClick = { hasSprinkles = !hasSprinkles }, label = { Text("✨ Sprinkles") })
          }
        }
      }

      // Action Button
      Box(modifier = Modifier.fillMaxWidth()) {
        if (!isBaked) {
          ChefPrimaryButton(
            text = "BAKE COOKIES! 🔥",
            onClick = { bakeCookies() },
            enabled = !isBaking,
            containerColor = ChefCoral,
            testTag = "bake_cookies_button"
          )
        } else if (!isBoxed) {
          ChefPrimaryButton(
            text = "PACKAGE IN GIFT BOX 🎁",
            onClick = {
              viewModel.playClick()
              isBoxed = true
            },
            containerColor = ChefGold,
            testTag = "box_cookies_button"
          )
        } else {
          ChefPrimaryButton(
            text = "SERVE COOKIES! ⭐",
            onClick = {
              viewModel.completeGame(
                stars = 20,
                xp = 30,
                gameName = "Cookie Bakery",
                playAgainAction = {
                  isBaked = false
                  isBoxed = false
                  bakeProgress = 0f
                }
              )
            },
            containerColor = ChefGreen,
            testTag = "serve_cookies_button"
          )
        }
      }
    }
  }
}
