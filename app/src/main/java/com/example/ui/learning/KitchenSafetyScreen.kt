package com.example.ui.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

data class SafetyScenario(
  val id: Int,
  val scenario: String,
  val question: String,
  val options: List<String>,
  val correctIndex: Int,
  val explanation: String,
  val emoji: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenSafetyScreen(
  viewModel: ChefViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scenarios = listOf(
    SafetyScenario(
      id = 1,
      scenario = "You are ready to start mixing cookie dough!",
      question = "What is the very first thing you must do before touching ingredients?",
      options = listOf("Start eating chocolate chips", "Wash hands with warm water and soap for 20 seconds", "Turn on the oven"),
      correctIndex = 1,
      explanation = "Washing hands keeps germs away so our food is completely clean and safe!",
      emoji = "🧼"
    ),
    SafetyScenario(
      id = 2,
      scenario = "The oven bell rings and the hot pizza is ready!",
      question = "How should you take the hot baking tray out of the oven?",
      options = listOf("Ask an adult helper to use thick oven mitts", "Grab the metal edges quickly with bare fingers", "Blow on it with your breath"),
      correctIndex = 0,
      explanation = "Always have an adult help with hot ovens and always protect hands with thick mitts!",
      emoji = "🧤"
    ),
    SafetyScenario(
      id = 3,
      scenario = "A glass of water accidentally spilled on the kitchen tile floor!",
      question = "What should you do right away?",
      options = listOf("Walk away and ignore it", "Wipe up the water immediately with a towel so no one slips", "Put a rug over it"),
      correctIndex = 1,
      explanation = "Wet floors are slippery! Wiping up spills prevents slips and kitchen bumps.",
      emoji = "🧽"
    ),
    SafetyScenario(
      id = 4,
      scenario = "You want to slice an apple in half for your lunchbox!",
      question = "Which tool should kids use for cutting?",
      options = listOf("A sharp chef knife alone", "A kid-safe plastic wavy cutter with an adult nearby", "A pair of scissors"),
      correctIndex = 1,
      explanation = "Only grown-ups handle sharp metal knives. Kids can use safe plastic wavy cutters with adult supervision!",
      emoji = "🍎"
    )
  )

  var completedScenarios by remember { mutableStateOf(setOf<Int>()) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Kitchen Safety Rules 🧤", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("safety_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(padding),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = ChefSkyBlueContainer,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(text = "🛡️", fontSize = 32.sp)
            Column {
              Text(text = "Safety First in the Kitchen!", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color(0xFF0277BD))
              Text(text = "Answer the safety scenarios to earn the Kitchen Safety Star badge!", fontSize = 12.sp)
            }
          }
        }
      }

      items(scenarios) { sc ->
        val isDone = completedScenarios.contains(sc.id)
        var selectedOpt by remember { mutableStateOf<Int?>(null) }
        var isCorrect by remember { mutableStateOf(false) }

        ChefCard(
          backgroundColor = MaterialTheme.colorScheme.surface,
          cornerRadius = 22.dp,
          elevation = 3.dp
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(text = sc.emoji, fontSize = 28.sp)
            Column(modifier = Modifier.weight(1f)) {
              Text(text = "Scenario #${sc.id}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
              Text(text = sc.scenario, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(text = sc.question, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          Spacer(modifier = Modifier.height(10.dp))

          sc.options.forEachIndexed { idx, opt ->
            val isSelected = selectedOpt == idx
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = if (isSelected) {
                if (idx == sc.correctIndex) ChefGreenContainer else ChefCoralContainer
              } else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable {
                  viewModel.playClick()
                  selectedOpt = idx
                  if (idx == sc.correctIndex) {
                    isCorrect = true
                    completedScenarios = completedScenarios + sc.id
                  } else {
                    isCorrect = false
                  }
                }
            ) {
              Text(text = opt, modifier = Modifier.padding(10.dp), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
          }

          if (isCorrect) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(10.dp), color = ChefGreenContainer) {
              Text(
                text = "✅ ${sc.explanation}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = ChefGreenDark,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
        }
      }

      if (completedScenarios.size >= 4) {
        item {
          ChefPrimaryButton(
            text = "CLAIM SAFETY CHAMPION BADGE! 🎖",
            onClick = {
              viewModel.completeGame(
                stars = 25,
                xp = 40,
                gameName = "Kitchen Safety Lessons",
                playAgainAction = { completedScenarios = emptySet() }
              )
            },
            containerColor = ChefGreen,
            testTag = "claim_safety_badge"
          )
        }
      }
    }
  }
}
