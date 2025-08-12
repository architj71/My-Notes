package com.example.mynotes.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteScreen(
    state: NoteState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(Color(0xFF9A9CEA))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "MyNotes",
                    fontSize = 23.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 3.dp)
                )
                IconButton(onClick = { onEvent(NotesEvent.sortNotes) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Sort,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = Color.White
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFF9A9CEA),
                onClick = {
                    state.title.value = ""
                    state.desc.value = ""
                    navController.navigate(route = "AddNoteScreen")
                }
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        }
    ) {
        LazyColumn(
            contentPadding = it,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.notes.size) { i ->
                NoteItem(
                    state = state,
                    index = i,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun NoteItem(state: NoteState, index: Int, onEvent: (NotesEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(getColorForCategory(state.notes[index].category))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = state.notes[index].title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.notes[index].desc,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        IconButton(onClick = { onEvent(NotesEvent.DeleteNote(state.notes[index])) }) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                modifier = Modifier.size(35.dp),
                contentDescription = null
            )
        }
    }
}

// Updated color mapping for emotion labels
fun getColorForCategory(category: String): Color {
    Log.d("CategoryCheck", "Category: '$category'")
    return when (category.lowercase()) {
        "happy" -> Color.Yellow
        "sad" -> Color.Blue
        "angry" -> Color.Red
        "fear" -> Color.Gray
        "surprise" -> Color.Magenta
        "neutral" -> Color.LightGray
        else -> Color.LightGray
    }
}
