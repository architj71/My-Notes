package com.example.mynotes.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.Data.Note
import com.example.mynotes.Data.NoteDao
import com.example.mynotes.ml.Classifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteViewModel (private var dao : NoteDao) : ViewModel() {
    private var isSortedByDateAdded = MutableStateFlow(true)
    private var notes = isSortedByDateAdded.flatMapLatest {
        if(it) dao.getOrderedByDateAddedBy()
        else dao.getOrderedByTitle()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    var _state = MutableStateFlow(NoteState())
    var state = combine(_state, isSortedByDateAdded, notes) {
        state, isSortedByDateAdded, notes ->
        state.copy (
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())
    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }
            is NotesEvent.SaveNote -> {
                val content = state.value.title.value + " " + state.value.desc.value
                val category = Classifier.classify(content)

                val note = Note(
                    title = state.value.title.value,
                    desc = state.value.desc.value,
                    dateAdded = System.currentTimeMillis(),
                    category = category
                )

                viewModelScope.launch {
                    dao.upsert(note)
                }
                _state.update{
                    it.copy(
                        title = mutableStateOf(""),
                        desc = mutableStateOf("")
                    )
                }
            }
            NotesEvent.sortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }
}