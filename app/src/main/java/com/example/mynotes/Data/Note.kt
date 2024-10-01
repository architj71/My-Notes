package com.example.mynotes.Data

import androidx.compose.runtime.Composable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val title: String,
    val desc: String,
    var dateAdded:Long
)