package com.example.ui.games

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class JuiceFruit(val id: String, val name: String, val emoji: String, val juiceColor: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitJuiceMakerGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()
  val availableFruits = listOf(
    JuiceFruit("strawberry", "Strawberry", "🍓", Color(0xFFE91E63)),
    JuiceFruit("orange", "Orange", "🍊", Color(0xFFFF9800)),
    JuiceFruit("banana", "Banana", "🍌", Color(0xFFFFEB3B)),
    JuiceFruit("apple", "Green Apple", "🍏", Color(0xFF8BC34A)),
    JuiceFruit("watermelon", "Watermelon", "🍉", Color(0xFFF06292)),
    JuiceFruit("mango", "Mango", "🥭", Color(0xFFFFB300)),
    JuiceFruit("blueberry", "Blueberry", "🫐", Color(0xFF3F51B5))
  )

  var step by remember { mutableIntStateOf(1) } // 1: Pick, 2: Slice, 3: Blend, 4: Pour & Garnish
  var selectedFruits by remember { mutableStateOf(listOf<JuiceFruit>()) }
  var slicedCount by remember { mutableIntStateOf(0) }
  var isBlending by remember { mutableStateOf(false) }
  var blendProgress by remember { mutableFloatStateOf(0f) }
  var hasStraw by remember { mutableStateOf(true) }
  var hasGarnish by remember { mutableStateOf(true) }

  val infiniteTransition = rememberInfiniteTransition(label = "blender_spin")
  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(tween(400, easing = LinearEasing), RepeatMode.Restart),
    label = "spin_rot"
  )

  fun startBlend() {
    isBlending = true
    viewModel.playBlend()
    scope.launch {
      for (i in 1..100) {
        delay(30)
        blendProgress = i / 100f
      }
      isBlending = false
      step = 4
    }
  }

  val finalJuiceColor = remember(selectedFruits) {
    if (selectedFruits.isEmpty()) Color(0xFFFF8A65)
    else selectedFruits.first().juiceColor
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Fruit Juice Maker 🍹", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("juice_back_button")) {
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
        color = ChefSkyBlueContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = when (step) {
            1 -> "Step 1: Pick 2 or 3 fruits for your smoothie!"
            2 -> "Step 2: Tap the chopping board to slice your fruits!"
            3 -> "Step 3: Close the lid and blend the smoothie!"
            else -> "Step 4: Served ice cold in a glass with a straw!"
          },
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = Color(0xFF0277BD),
          modifier = Modifier.padding(14.dp)
        )
      }

      // Visual Interactive Area
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp),
        contentAlignment = Alignment.Center
      ) {
        when (step) {
          1, 2 -> {
            // Chopping Board Area
            Surface(
              shape = RoundedCornerShape(24.dp),
              color = Color(0xFFFFE0B2),
              modifier = Modifier
                .size(width = 240.dp, height = 200.dp)
                .border(3.dp, Color(0xFFFFA726), RoundedCornerShape(24.dp))
                .clickable(enabled = step == 2 && slicedCount < 6) {
                  viewModel.playClick()
                  slicedCount++
                }
            ) {
              Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                if (selectedFruits.isEmpty()) {
                  Text(text = "🧺 Pick fruits below to start!", color = Color(0xFF8D6E63), fontWeight = FontWeight.Bold)
                } else {
                  Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    selectedFruits.forEach { fruit ->
                      Text(text = fruit.emoji, fontSize = if (slicedCount > 3) 26.sp else 38.sp)
                    }
                  }
                  Spacer(modifier = Modifier.height(10.dp))
                  if (step == 2) {
                    Text(
                      text = if (slicedCount >= 6) "All chopped & ready!" else "Tap to slice: $slicedCount / 6 cuts",
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFE65100),
                      fontSize = 13.sp
                    )
                  }
                }
              }
            }
          }
          3 -> {
            // Blender Pitcher
            Surface(
              shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
              color = Color.White.copy(alpha = 0.85f),
              modifier = Modifier
                .size(width = 150.dp, height = 220.dp)
                .border(3.dp, ChefSkyBlue, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 24.dp, bottomEnd = 24.dp))
            ) {
              Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (isBlending) {
                  Box(
                    modifier = Modifier
                      .size(110.dp)
                      .rotate(rotation)
                      .clip(CircleShape)
                      .background(finalJuiceColor)
                  )
                  Text(text = "🌪️", fontSize = 42.sp)
                } else {
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                      selectedFruits.forEach { Text(text = it.emoji, fontSize = 24.sp) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Ready to blend!", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  }
                }
              }
            }
          }
          4 -> {
            // Fresh Smoothie Glass
            Surface(
              shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 22.dp, bottomEnd = 22.dp),
              color = finalJuiceColor,
              modifier = Modifier
                .size(width = 130.dp, height = 200.dp)
                .shadow(6.dp, RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 22.dp, bottomEnd = 22.dp))
                .border(2.dp, Color.White, RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 22.dp, bottomEnd = 22.dp))
            ) {
              Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                if (hasStraw) {
                  Box(
                    modifier = Modifier
                      .size(width = 8.dp, height = 80.dp)
                      .offset(y = (-40).dp, x = 20.dp)
                      .background(ChefCoral)
                  )
                }
                if (hasGarnish) {
                  Text(text = "🍋", fontSize = 28.sp, modifier = Modifier.offset(y = (-14).dp, x = (-30).dp))
                }
                Column(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                  verticalArrangement = Arrangement.Center,
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text(text = "🧊", fontSize = 22.sp)
                  Text(text = "Delicious!", color = Color.White, fontWeight = FontWeight.ExtraBold)
                }
              }
            }
          }
        }
      }

      // Step Controls Bar
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 24.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        when (step) {
          1 -> {
            Text(text = "Select Fresh Fruits (${selectedFruits.size} / 3):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(availableFruits) { fruit ->
                val isSelected = selectedFruits.any { it.id == fruit.id }
                FilterChip(
                  selected = isSelected,
                  onClick = {
                    viewModel.playClick()
                    selectedFruits = if (isSelected) {
                      selectedFruits.filter { it.id != fruit.id }
                    } else {
                      if (selectedFruits.size < 3) selectedFruits + fruit else selectedFruits
                    }
                  },
                  label = { Text("${fruit.emoji} ${fruit.name}") },
                  shape = RoundedCornerShape(16.dp)
                )
              }
            }
          }
          2 -> {
            Text(text = "Tap board to slice fruits into pieces!", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }
          3 -> {
            Text(text = "High speed blender with healthy ice cubes!", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }
          4 -> {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "Drink Garnish & Straw", fontWeight = FontWeight.Bold)
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = hasStraw, onClick = { hasStraw = !hasStraw }, label = { Text("🥤 Straw") })
                FilterChip(selected = hasGarnish, onClick = { hasGarnish = !hasGarnish }, label = { Text("🍋 Lemon") })
              }
            }
          }
        }
      }

      // Bottom Primary Action
      Box(modifier = Modifier.fillMaxWidth()) {
        when (step) {
          1 -> {
            ChefPrimaryButton(
              text = "SLICE FRUITS 🔪",
              onClick = {
                viewModel.playClick()
                step = 2
              },
              enabled = selectedFruits.isNotEmpty(),
              testTag = "juice_slice_fruits"
            )
          }
          2 -> {
            ChefPrimaryButton(
              text = "PUT IN BLENDER 🌪️",
              onClick = {
                viewModel.playClick()
                step = 3
              },
              enabled = slicedCount >= 3,
              testTag = "juice_to_blender"
            )
          }
          3 -> {
            ChefPrimaryButton(
              text = "BLEND SMOOTHIE! 🍹",
              onClick = { startBlend() },
              enabled = !isBlending,
              containerColor = ChefSkyBlue,
              testTag = "juice_blend_button"
            )
          }
          4 -> {
            ChefPrimaryButton(
              text = "SERVE JUICE! ⭐",
              onClick = {
                viewModel.completeGame(
                  stars = 15,
                  xp = 30,
                  gameName = "Fruit Juice Maker",
                  playAgainAction = {
                    step = 1
                    selectedFruits = emptyList()
                    slicedCount = 0
                  }
                )
              },
              containerColor = ChefGreen,
              testTag = "juice_serve_button"
            )
          }
        }
      }
    }
  }
}
