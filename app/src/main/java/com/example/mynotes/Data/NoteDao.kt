package com.example.mynotes.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
@Dao
interface NoteDao {
    @Upsert
    suspend fun upsert(note:Note)
    @Delete
    suspend fun deleteNote(note:Note)
    @Query("Select * from note order by title asc")
    fun getOrderedByTitle():Flow<List<Note>>
    @Query("Select * from Note order by dateAdded")
    fun getOrderedByDateAddedBy():Flow<List<Note>>
}
// DAO:Data Access Object used in Room library to access objects
/*The suspend keyword in Kotlin allows functions to run asynchronously
 without blocking the main thread, making it ideal for handling time-consuming operations
 like database access while keeping the app responsive.*/
