package com.example.ui.games

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
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

data class PizzaTopping(val id: String, val name: String, val emoji: String, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaMakerGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()

  // Steps: 1: Crust, 2: Sauce, 3: Cheese, 4: Toppings, 5: Bake, 6: Slices
  var currentStep by remember { mutableIntStateOf(1) }
  var selectedCrust by remember { mutableStateOf("Classic Dough") }
  var selectedSauce by remember { mutableStateOf("Tomato Marinara") }
  var hasCheese by remember { mutableStateOf(false) }
  var activeToppings by remember { mutableStateOf(setOf<String>()) }
  var isBaking by remember { mutableStateOf(false) }
  var bakeProgress by remember { mutableFloatStateOf(0f) }
  var isBaked by remember { mutableStateOf(false) }
  var slicedCount by remember { mutableIntStateOf(0) }

  val crusts = listOf("Classic Dough" to "🫓", "Cheesy Crust" to "🧀", "Whole Wheat" to "🌾", "Star Crust" to "⭐")
  val sauces = listOf("Tomato Marinara" to Color(0xFFD32F2F), "Pesto Green" to Color(0xFF388E3C), "Creamy Garlic" to Color(0xFFFFF9C4), "Sweet BBQ" to Color(0xFF5D4037))
  val toppingsList = listOf(
    PizzaTopping("pepperoni", "Pepperoni", "🍕", Color(0xFFC62828)),
    PizzaTopping("mushrooms", "Mushrooms", "🍄", Color(0xFF8D6E63)),
    PizzaTopping("peppers", "Bell Peppers", "🫑", Color(0xFF2E7D32)),
    PizzaTopping("olives", "Olives", "🫒", Color(0xFF212121)),
    PizzaTopping("pineapple", "Pineapple", "🍍", Color(0xFFFBC02D)),
    PizzaTopping("tomatoes", "Tomatoes", "🍅", Color(0xFFE53935)),
    PizzaTopping("corn", "Sweet Corn", "🌽", Color(0xFFFFD54F))
  )

  fun startBake() {
    isBaking = true
    viewModel.playSizzle()
    scope.launch {
      for (i in 1..100) {
        delay(35)
        bakeProgress = i / 100f
      }
      isBaking = false
      isBaked = true
      currentStep = 6
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(text = "Pizza Maker 🍕", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(text = "Step $currentStep of 6", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("pizza_back_button")) {
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
      // Step Title & Instruction
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChefCoralContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = when (currentStep) {
            1 -> "Step 1: Choose your delicious pizza crust!"
            2 -> "Step 2: Spread your favorite pizza sauce!"
            3 -> "Step 3: Sprinkle rich mozzarella cheese!"
            4 -> "Step 4: Tap toppings to decorate your pizza!"
            5 -> "Step 5: Put in the stone oven and bake!"
            else -> "Step 6: Tap pizza to slice and serve!"
          },
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = ChefCoralDark,
          modifier = Modifier.padding(14.dp)
        )
      }

      // Visual Pizza Display Area
      Box(
        modifier = Modifier
          .size(260.dp)
          .shadow(8.dp, CircleShape)
          .clip(CircleShape)
          .background(if (isBaked) Color(0xFFD78839) else Color(0xFFFFD180))
          .border(8.dp, if (isBaked) Color(0xFFB0621A) else Color(0xFFFFA726), CircleShape)
          .clickable(enabled = currentStep == 6 && slicedCount < 6) {
            viewModel.playClick()
            slicedCount++
          },
        contentAlignment = Alignment.Center
      ) {
        // Sauce Layer
        if (currentStep >= 2) {
          Box(
            modifier = Modifier
              .size(220.dp)
              .clip(CircleShape)
              .background(
                when (selectedSauce) {
                  "Pesto Green" -> Color(0xFF4CAF50)
                  "Creamy Garlic" -> Color(0xFFFFF9C4)
                  "Sweet BBQ" -> Color(0xFF795548)
                  else -> Color(0xFFE53935)
                }
              )
          )
        }

        // Cheese Layer
        if (hasCheese || currentStep >= 4) {
          Box(
            modifier = Modifier
              .size(205.dp)
              .clip(CircleShape)
              .background(if (isBaked) Color(0xFFFFE082) else Color(0xFFFFFDE7))
          )
        }

        // Toppings Render
        if (activeToppings.isNotEmpty()) {
          Box(modifier = Modifier.size(190.dp)) {
            val toppings = toppingsList.filter { activeToppings.contains(it.id) }
            val positions = listOf(
              Offset(35f, 35f), Offset(105f, 25f), Offset(65f, 95f),
              Offset(25f, 105f), Offset(105f, 105f), Offset(70f, 55f),
              Offset(40f, 75f), Offset(95f, 70f)
            )
            positions.forEachIndexed { idx, offset ->
              if (idx < toppings.size * 2) {
                val item = toppings[idx % toppings.size]
                Text(
                  text = item.emoji,
                  fontSize = 24.sp,
                  modifier = Modifier
                    .offset(x = offset.x.dp, y = offset.y.dp)
                )
              }
            }
          }
        }

        // Slices lines if step 6
        if (currentStep == 6) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            if (slicedCount >= 1) drawLine(Color.DarkGray.copy(alpha = 0.6f), Offset(center.x, 0f), Offset(center.x, size.height), strokeWidth = 6f)
            if (slicedCount >= 3) drawLine(Color.DarkGray.copy(alpha = 0.6f), Offset(0f, center.y), Offset(size.width, center.y), strokeWidth = 6f)
            if (slicedCount >= 5) drawLine(Color.DarkGray.copy(alpha = 0.6f), Offset(0f, 0f), Offset(size.width, size.height), strokeWidth = 6f)
          }
        }

        // Baking smoke / oven overlay
        if (isBaking) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "🔥 Stone Oven Baking... 🔥", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
              Spacer(modifier = Modifier.height(10.dp))
              CircularProgressIndicator(
                progress = { bakeProgress },
                color = ChefGold,
                strokeWidth = 6.dp
              )
            }
          }
        }
      }

      // Step Controls / Options Bar
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 24.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        when (currentStep) {
          1 -> {
            Text(text = "Select Crust Style:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              items(crusts) { (name, emoji) ->
                FilterChip(
                  selected = selectedCrust == name,
                  onClick = {
                    viewModel.playClick()
                    selectedCrust = name
                  },
                  label = { Text("$emoji $name") },
                  shape = RoundedCornerShape(16.dp)
                )
              }
            }
          }
          2 -> {
            Text(text = "Select Pizza Sauce:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              items(sauces) { (name, color) ->
                FilterChip(
                  selected = selectedSauce == name,
                  onClick = {
                    viewModel.playClick()
                    selectedSauce = name
                  },
                  label = { Text(name) },
                  shape = RoundedCornerShape(16.dp)
                )
              }
            }
          }
          3 -> {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "Add Mozzarella Cheese?", fontWeight = FontWeight.Bold)
              Switch(
                checked = hasCheese,
                onCheckedChange = {
                  viewModel.playClick()
                  hasCheese = it
                }
              )
            }
          }
          4 -> {
            Text(text = "Tap to Add Toppings:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(toppingsList) { topping ->
                val isSelected = activeToppings.contains(topping.id)
                FilterChip(
                  selected = isSelected,
                  onClick = {
                    viewModel.playClick()
                    activeToppings = if (isSelected) activeToppings - topping.id else activeToppings + topping.id
                  },
                  label = { Text("${topping.emoji} ${topping.name}") },
                  shape = RoundedCornerShape(16.dp)
                )
              }
            }
          }
          5 -> {
            Text(
              text = if (isBaking) "Baking in progress..." else "Ready to bake in the hot stone oven!",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
          6 -> {
            Text(
              text = if (slicedCount >= 6) "Pizza is sliced into 6 yummy pieces!" else "Tap pizza to slice: $slicedCount / 6 cuts",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }

      // Next / Action Button
      Box(modifier = Modifier.fillMaxWidth()) {
        if (currentStep < 5) {
          ChefPrimaryButton(
            text = "NEXT STEP ➡️",
            onClick = {
              viewModel.playClick()
              currentStep++
            },
            testTag = "pizza_next_step"
          )
        } else if (currentStep == 5) {
          ChefPrimaryButton(
            text = "BAKE PIZZA! 🔥",
            onClick = { startBake() },
            enabled = !isBaking,
            containerColor = ChefCoral,
            testTag = "pizza_bake_button"
          )
        } else {
          ChefPrimaryButton(
            text = "SERVE PIZZA! ⭐",
            onClick = {
              viewModel.completeGame(
                stars = 20,
                xp = 35,
                gameName = "Pizza Maker",
                playAgainAction = {
                  currentStep = 1
                  hasCheese = false
                  activeToppings = emptySet()
                  isBaked = false
                  bakeProgress = 0f
                  slicedCount = 0
                }
              )
            },
            containerColor = ChefGreen,
            testTag = "pizza_serve_button"
          )
        }
      }
    }
  }
}
