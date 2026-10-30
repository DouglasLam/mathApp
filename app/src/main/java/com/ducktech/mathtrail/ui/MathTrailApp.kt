package com.ducktech.mathtrail.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ducktech.mathtrail.data.SharedPreferencesStudentProfileRepository
import com.ducktech.mathtrail.domain.StudentProfile
import com.ducktech.mathtrail.domain.StudentProfileRules

private val Canvas = Color(0xFFFFF9F0)
private val Ink = Color(0xFF102D4A)
private val Teal = Color(0xFF078D92)
private val Coral = Color(0xFFE7655B)
private val SoftTeal = Color(0xFFE3F5F1)
private val SoftCoral = Color(0xFFFFECE7)

@Composable
fun MathTrailApp(context: Context) {
    val repository = remember(context) { SharedPreferencesStudentProfileRepository(context) }
    val profiles = remember { mutableStateListOf<StudentProfile>().apply { addAll(repository.getProfiles()) } }
    val savedId = remember { repository.getActiveProfileId() }
    var activeId by remember {
        mutableStateOf(savedId?.takeIf { id -> profiles.any { it.id == id } } ?: profiles.firstOrNull()?.id)
    }
    var showProfiles by remember { mutableStateOf(activeId == null) }
    var creatingProfile by remember { mutableStateOf(false) }

    if (showProfiles) {
        ProfilePicker(
            profiles = profiles,
            activeId = activeId,
            creatingProfile = creatingProfile,
            onSelect = { profile ->
                activeId = profile.id
                repository.setActiveProfileId(profile.id)
                showProfiles = false
                creatingProfile = false
            },
            onCreate = { profile ->
                profiles.add(profile)
                repository.saveProfiles(profiles)
                activeId = profile.id
                repository.setActiveProfileId(profile.id)
                showProfiles = false
                creatingProfile = false
            },
            onAdd = { creatingProfile = true },
            onCancel = { creatingProfile = false },
            onDone = { showProfiles = false }
        )
    } else {
        val profile = profiles.firstOrNull { it.id == activeId }
        if (profile == null) {
            showProfiles = true
        } else {
            HomeScreen(profile, onSwitchProfile = { showProfiles = true })
        }
    }
}

@Composable
private fun ProfilePicker(
    profiles: List<StudentProfile>,
    activeId: String?,
    creatingProfile: Boolean,
    onSelect: (StudentProfile) -> Unit,
    onCreate: (StudentProfile) -> Unit,
    onAdd: () -> Unit,
    onCancel: () -> Unit,
    onDone: () -> Unit
) {
    var displayName by remember(creatingProfile) { mutableStateOf("") }
    var avatar by remember(creatingProfile) { mutableStateOf(StudentProfileRules.availableAvatars.first()) }
    var nameError by remember(creatingProfile) { mutableStateOf<String?>(null) }

    Column(
        Modifier.fillMaxSize().background(Canvas).padding(horizontal = 20.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Who's practicing?", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Choose a student profile or add a new one.", color = Ink.copy(alpha = 0.72f), fontSize = 15.sp)

        profiles.forEach { profile ->
            ProfileCard(profile, profile.id == activeId) { onSelect(profile) }
        }

        if (creatingProfile) {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Create a profile", color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = displayName,
                        onValueChange = { displayName = it; nameError = null },
                        label = { Text("Student name") },
                        singleLine = true,
                        isError = nameError != null,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth()
                    )
                    nameError?.let { Text(it, color = Coral, fontSize = 13.sp) }
                    Text("Choose an avatar", color = Ink, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        StudentProfileRules.availableAvatars.forEach { option ->
                            TextButton(onClick = { avatar = option }) {
                                Text(if (avatar == option) "$option ✓" else option, fontSize = 22.sp)
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            val error = StudentProfileRules.validateName(displayName)
                            if (error == null) onCreate(StudentProfileRules.create(displayName, avatar))
                            else nameError = error
                        }) { Text("Create profile") }
                        TextButton(onClick = onCancel) { Text("Cancel") }
                    }
                }
            }
        } else {
            TextButton(onClick = onAdd) { Text("+ Add a student", color = Teal, fontWeight = FontWeight.Bold) }
        }

        Spacer(Modifier.weight(1f))
        if (profiles.isNotEmpty()) {
            TextButton(onClick = onDone, modifier = Modifier.align(Alignment.End)) {
                Text("Done", color = Teal, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProfileCard(profile: StudentProfile, active: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (active) SoftTeal else Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(profile.avatar, Modifier.size(54.dp).background(Color.White, CircleShape).padding(8.dp), fontSize = 30.sp)
            Column(Modifier.weight(1f)) {
                Text(profile.displayName, color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text(if (active) "Currently selected" else "Tap to switch", color = Ink.copy(alpha = 0.68f))
            }
            Text("›", color = Teal, fontSize = 28.sp)
        }
    }
}

@Composable
private fun HomeScreen(profile: StudentProfile, onSwitchProfile: () -> Unit) {
    var grade by remember { mutableIntStateOf(5) }
    var gradeMenuExpanded by remember { mutableStateOf(false) }
    var selectedOperation by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier.fillMaxSize().background(Canvas).padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Math Trail", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onSwitchProfile) { Text("Switch student", color = Teal) }
        }
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(24.dp)) {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(profile.avatar, Modifier.size(58.dp).background(SoftTeal, CircleShape).padding(8.dp), fontSize = 34.sp)
                Column(Modifier.weight(1f)) {
                    Text(profile.displayName, color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    Text("Choose your grade", color = Ink.copy(alpha = 0.72f), fontSize = 14.sp)
                }
                Box {
                    TextButton(onClick = { gradeMenuExpanded = true }) { Text("Grade $grade  ▾", color = Ink, fontWeight = FontWeight.SemiBold) }
                    DropdownMenu(expanded = gradeMenuExpanded, onDismissRequest = { gradeMenuExpanded = false }) {
                        (3..9).forEach { option ->
                            DropdownMenuItem(text = { Text("Grade $option") }, onClick = { grade = option; gradeMenuExpanded = false })
                        }
                    }
                }
            }
        }

        Text("Choose a practice", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        OperationCard("Multiplication", "×", SoftCoral, Coral) { selectedOperation = "Multiplication" }
        OperationCard("Division", "÷", SoftTeal, Teal) { selectedOperation = "Division" }

        Card(
            Modifier.fillMaxWidth().clickable { selectedOperation = "Resume" },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4F5)),
            shape = RoundedCornerShape(22.dp)
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("▤", color = Teal, fontSize = 28.sp)
                Column(Modifier.weight(1f)) {
                    Text("Resume practice", color = Ink, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text("Division · 8 questions left", color = Ink.copy(alpha = 0.8f))
                }
                Text("›", color = Ink, fontSize = 28.sp)
            }
        }
        selectedOperation?.let { operation ->
            Text(
                if (operation == "Resume") "Resume will open your saved session." else "$operation for Grade $grade selected.",
                color = Teal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.weight(1f))
        Text("Progress     ·     Badges     ·     Profile", Modifier.fillMaxWidth(), color = Ink.copy(alpha = 0.65f), fontSize = 14.sp)
    }
}

@Composable
private fun OperationCard(title: String, symbol: String, background: Color, accent: Color, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().height(82.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(Modifier.fillMaxSize().padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Surface(Modifier.size(54.dp), color = accent, shape = RoundedCornerShape(16.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(symbol, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(title, Modifier.weight(1f), color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("›", color = Ink, fontSize = 28.sp)
        }
    }
}
