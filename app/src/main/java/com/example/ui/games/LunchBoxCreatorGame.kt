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

data class BentoItem(val id: String, val name: String, val emoji: String, val tip: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchBoxCreatorGame(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedFruit by remember { mutableStateOf<BentoItem?>(null) }
  var selectedVeggie by remember { mutableStateOf<BentoItem?>(null) }
  var selectedProtein by remember { mutableStateOf<BentoItem?>(null) }
  var selectedGrain by remember { mutableStateOf<BentoItem?>(null) }
  var activeTip by remember { mutableStateOf("Pick a fruit, veggie, protein and grain to make a balanced lunch box!") }

  val fruits = listOf(
    BentoItem("apple", "Apple Slices", "🍎", "Apples give your body fiber and keep your teeth clean!"),
    BentoItem("berries", "Sweet Berries", "🍓", "Berries are full of antioxidants that protect your body!"),
    BentoItem("grapes", "Crispy Grapes", "🍇", "Grapes give you quick healthy energy for recess!"),
    BentoItem("banana", "Banana Wheels", "🍌", "Bananas have potassium that keeps your muscles working well!")
  )

  val veggies = listOf(
    BentoItem("carrots", "Baby Carrots", "🥕", "Carrots have Vitamin A to help you see in dim light!"),
    BentoItem("cucumber", "Cool Cucumbers", "🥒", "Cucumbers are super hydrating and crunch with every bite!"),
    BentoItem("tomatoes", "Cherry Tomatoes", "🍅", "Tomatoes have lycopene to keep your heart healthy!"),
    BentoItem("broccoli", "Tiny Trees", "🥦", "Broccoli gives you calcium for strong growing bones!")
  )

  val proteins = listOf(
    BentoItem("egg", "Boiled Egg", "🥚", "Eggs build strong muscles so you can run faster!"),
    BentoItem("cheese", "Cheese Cubes", "🧀", "Cheese has calcium and protein to make your bones tough!"),
    BentoItem("chicken", "Grilled Chicken", "🍗", "Chicken gives you long-lasting energy for the afternoon!"),
    BentoItem("edamame", "Green Edamame", "🫛", "Soybeans are fun plant proteins you pop from the pod!")
  )

  val grains = listOf(
    BentoItem("rice_ball", "Cute Rice Ball", "🍙", "Rice gives you complex carbohydrates for brain power!"),
    BentoItem("roll", "Wheat Roll", "🥖", "Whole grains keep your tummy happy all day!"),
    BentoItem("crackers", "Gold Crackers", "🍘", "Crunchy whole grain snack for quick energy!")
  )

  val packedCount = listOfNotNull(selectedFruit, selectedVeggie, selectedProtein, selectedGrain).size

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Lunch Box Creator 🍱", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("lunchbox_back_button")) {
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
      // Educational Tip Card
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = ChefGreenContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(text = "💡", fontSize = 24.sp)
          Text(
            text = activeTip,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = ChefGreenDark
          )
        }
      }

      // Bento Box Container Visual
      Surface(
        shape = RoundedCornerShape(26.dp),
        color = Color(0xFFEF5350),
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .border(4.dp, Color(0xFFC62828), RoundedCornerShape(26.dp))
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Top row: Fruit & Veggie
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            BentoCompartment(
              title = "Fruit",
              item = selectedFruit,
              modifier = Modifier.weight(1f),
              bg = Color(0xFFFFF9C4)
            )
            BentoCompartment(
              title = "Vegetable",
              item = selectedVeggie,
              modifier = Modifier.weight(1f),
              bg = Color(0xFFE8F5E9)
            )
          }

          // Bottom row: Protein & Grain
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            BentoCompartment(
              title = "Protein",
              item = selectedProtein,
              modifier = Modifier.weight(1f),
              bg = Color(0xFFFFE0B2)
            )
            BentoCompartment(
              title = "Grain",
              item = selectedGrain,
              modifier = Modifier.weight(1f),
              bg = Color(0xFFEDE7F6)
            )
          }
        }
      }

      // Ingredients Selector
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 24.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(text = "Select Items to Pack ($packedCount / 4):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          // Fruits
          items(fruits) { fruit ->
            FilterChip(
              selected = selectedFruit?.id == fruit.id,
              onClick = {
                viewModel.playClick()
                selectedFruit = fruit
                activeTip = fruit.tip
              },
              label = { Text("${fruit.emoji} ${fruit.name}") }
            )
          }
          // Veggies
          items(veggies) { veggie ->
            FilterChip(
              selected = selectedVeggie?.id == veggie.id,
              onClick = {
                viewModel.playClick()
                selectedVeggie = veggie
                activeTip = veggie.tip
              },
              label = { Text("${veggie.emoji} ${veggie.name}") }
            )
          }
          // Proteins
          items(proteins) { protein ->
            FilterChip(
              selected = selectedProtein?.id == protein.id,
              onClick = {
                viewModel.playClick()
                selectedProtein = protein
                activeTip = protein.tip
              },
              label = { Text("${protein.emoji} ${protein.name}") }
            )
          }
          // Grains
          items(grains) { grain ->
            FilterChip(
              selected = selectedGrain?.id == grain.id,
              onClick = {
                viewModel.playClick()
                selectedGrain = grain
                activeTip = grain.tip
              },
              label = { Text("${grain.emoji} ${grain.name}") }
            )
          }
        }
      }

      // Finish Button
      ChefPrimaryButton(
        text = if (packedCount >= 4) "PACK LUNCH BOX! 🎒" else "PACK AT LEAST 3 ITEMS ($packedCount/4)",
        onClick = {
          viewModel.completeDailyChallenge()
          viewModel.completeGame(
            stars = 20,
            xp = 35,
            gameName = "Lunch Box Creator",
            playAgainAction = {
              selectedFruit = null
              selectedVeggie = null
              selectedProtein = null
              selectedGrain = null
              activeTip = "Great job! Let's pack another healthy rainbow meal!"
            }
          )
        },
        enabled = packedCount >= 3,
        containerColor = ChefGreen,
        testTag = "pack_lunchbox_button"
      )
    }
  }
}

@Composable
private fun BentoCompartment(
  title: String,
  item: BentoItem?,
  modifier: Modifier = Modifier,
  bg: Color
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = bg,
    modifier = modifier.fillMaxHeight()
  ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      if (item != null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = item.emoji, fontSize = 32.sp)
          Text(text = item.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
      } else {
        Text(text = "+ $title", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
      }
    }
  }
}
