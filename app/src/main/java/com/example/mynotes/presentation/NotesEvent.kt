package com.example.mynotes.presentation

import com.example.mynotes.Data.Note

sealed interface NotesEvent {
    object sortNotes : NotesEvent
    data class DeleteNote(var note: Note) : NotesEvent
    data class SaveNote(
        var title: String,
        var desc: String
    ) : NotesEvent
}
