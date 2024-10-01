package com.example.mynotes.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.mynotes.Data.Note

data class NoteState (
    val notes: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val desc: MutableState<String> = mutableStateOf("")
)