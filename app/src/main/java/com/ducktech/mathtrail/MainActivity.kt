package com.ducktech.mathtrail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Canvas = Color(0xFFFFF9F0)
private val Ink = Color(0xFF102D4A)
private val Teal = Color(0xFF078D92)
private val Coral = Color(0xFFE7655B)
private val SoftTeal = Color(0xFFE3F5F1)
private val SoftCoral = Color(0xFFFFECE7)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Canvas
                ) {
                    MathTrailHome()
                }
            }
        }
    }
}

/** Home screen foundation. Navigation and persisted profiles arrive in later milestones. */
@Composable
private fun MathTrailHome() {
    var selectedGrade by remember { mutableIntStateOf(5) }
    var gradeMenuExpanded by remember { mutableStateOf(false) }
    var selectedOperation by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Math Trail", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("⚙", color = Ink, fontSize = 24.sp)
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "🦊",
                    modifier = Modifier
                        .size(58.dp)
                        .background(SoftTeal, CircleShape)
                        .padding(8.dp),
                    fontSize = 34.sp
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text("Sam", color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    Text("Choose your grade", color = Ink.copy(alpha = 0.72f), fontSize = 14.sp)
                }
                Column {
                    TextButton(onClick = { gradeMenuExpanded = true }) {
                        Text("Grade $selectedGrade  ▾", color = Ink, fontWeight = FontWeight.SemiBold)
                    }
                    DropdownMenu(
                        expanded = gradeMenuExpanded,
                        onDismissRequest = { gradeMenuExpanded = false }
                    ) {
                        (3..9).forEach { grade ->
                            DropdownMenuItem(
                                text = { Text("Grade $grade") },
                                onClick = {
                                    selectedGrade = grade
                                    gradeMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Text("Choose a practice", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        OperationCard(
            title = "Multiplication",
            symbol = "×",
            background = SoftCoral,
            accent = Coral,
            onClick = { selectedOperation = "Multiplication" }
        )
        OperationCard(
            title = "Division",
            symbol = "÷",
            background = SoftTeal,
            accent = Teal,
            onClick = { selectedOperation = "Division" }
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedOperation = "Resume" },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4F5)),
            shape = RoundedCornerShape(22.dp)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("▤", color = Teal, fontSize = 28.sp)
                Column(modifier = Modifier.weight(1f)) {
                    Text("Resume practice", color = Ink, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text("Division · 8 questions left", color = Ink.copy(alpha = 0.8f))
                }
                Text("›", color = Ink, fontSize = 28.sp)
            }
        }

        selectedOperation?.let { operation ->
            Text(
                text = if (operation == "Resume") "Resume will open your saved session." else "$operation for Grade $selectedGrade selected.",
                color = Teal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            "Progress     ·     Badges     ·     Profile",
            modifier = Modifier.fillMaxWidth(),
            color = Ink.copy(alpha = 0.65f),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun OperationCard(
    title: String,
    symbol: String,
    background: Color,
    accent: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(54.dp),
                color = accent,
                shape = RoundedCornerShape(16.dp)
            ) {
                androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                    Text(symbol, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(title, modifier = Modifier.weight(1f), color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("›", color = Ink, fontSize = 28.sp)
        }
    }
}
