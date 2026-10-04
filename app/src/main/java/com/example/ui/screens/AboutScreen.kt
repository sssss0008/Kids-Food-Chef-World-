package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChefCard
import com.example.ui.components.ChefPrimaryButton
import com.example.ui.theme.*

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Logo & Tagline
    item {
      Surface(
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 6.dp,
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, ChefCoral.copy(alpha = 0.3f), RoundedCornerShape(28.dp))
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.verticalGradient(listOf(ChefCoralContainer, Color.White))
            )
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(ChefCoral),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "👨‍🍳", fontSize = 46.sp)
            }

            Text(
              text = "Kids Food Chef World",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center
            )

            Surface(
              shape = RoundedCornerShape(14.dp),
              color = ChefGold
            ) {
              Text(
                text = "Learn • Cook • Play",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = Color(0xFF3E2723),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
              )
            }

            Text(
              text = "The educational culinary game designed to inspire healthy eating habits, kitchen safety, and creative cooking fun for young chefs everywhere!",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }

    // App Features
    item {
      AboutCard(
        title = "Key Features",
        emoji = "🌟"
      ) {
        FeatureBullet(emoji = "🍕", text = "11 Interactive Cooking Games: Pizza, Burgers, Smoothies, Bento Box & more!")
        FeatureBullet(emoji = "🎓", text = "Cooking Academy: 6 structured lessons on food prep, hygiene & tools.")
        FeatureBullet(emoji = "🥦", text = "Healthy Nutrition: Learn about colorful fruits, vegetables, and balanced plates.")
        FeatureBullet(emoji = "🧤", text = "Kitchen Safety: Essential habits like adult supervision & oven mitts.")
        FeatureBullet(emoji = "🧠", text = "Brain Games: Food memory matching, sorting, and encouraging quizzes.")
        FeatureBullet(emoji = "🏆", text = "Chef Progression: Climb from Junior Chef to legendary Super Chef!")
      }
    }

    // Learning Goals
    item {
      AboutCard(
        title = "Learning Goals",
        emoji = "🎯"
      ) {
        FeatureBullet(emoji = "🥗", text = "Positive association with wholesome vegetables and fresh fruits.")
        FeatureBullet(emoji = "🎨", text = "Creative expression through personalized cake decorating and smoothies.")
        FeatureBullet(emoji = "🖐️", text = "Hand-eye coordination and step-by-step sequencing.")
        FeatureBullet(emoji = "🧼", text = "Kitchen hygiene (washing hands for 20 seconds) and safety awareness.")
      }
    }

    // Child Safety & Privacy
    item {
      AboutCard(
        title = "Child Safety & Privacy",
        emoji = "🛡️"
      ) {
        FeatureBullet(emoji = "🔒", text = "100% Offline: No internet connection required to enjoy recipes and games.")
        FeatureBullet(emoji = "🚫", text = "Zero Ads & Zero In-App Purchases: Completely safe for independent play.")
        FeatureBullet(emoji = "👨‍👩‍👧", text = "Protected Parent Area: Secured with a math gate for parents.")
        FeatureBullet(emoji = "🛡️", text = "No personal data collected or shared outside the device.")
      }
    }

    // Creator & Sync Platform
    item {
      ChefCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        cornerRadius = 22.dp
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(text = "🌐", fontSize = 24.sp)
            Text(
              text = "Connect & Developer Platform",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "Kids Food Chef World is crafted by Awiskar Acharya. Connect on LinkedIn to view developer updates, platform synchronization, and new cooking expansions!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          ChefPrimaryButton(
            text = "VISIT LINKEDIN PROFILE",
            onClick = {
              try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/awiskaracharya/"))
                context.startActivity(intent)
              } catch (_: Exception) {}
            },
            icon = "🔗",
            containerColor = ChefSkyBlue,
            testTag = "about_linkedin_button"
          )
        }
      }
    }

    // App Version
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "Kids Food Chef World • Version 1.0.0",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Made with ❤️ for little chefs worldwide",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )
        }
      }
    }
  }
}

@Composable
private fun AboutCard(
  title: String,
  emoji: String,
  content: @Composable ColumnScope.() -> Unit
) {
  ChefCard(
    backgroundColor = MaterialTheme.colorScheme.surface,
    cornerRadius = 24.dp
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(text = emoji, fontSize = 24.sp)
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
    Spacer(modifier = Modifier.height(12.dp))
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      content = content
    )
  }
}

@Composable
private fun FeatureBullet(emoji: String, text: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Text(text = emoji, fontSize = 16.sp)
    Text(
      text = text,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}
