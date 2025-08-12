package com.example.mynotes

import android.app.Application
import android.util.Log
import com.example.mynotes.ml.Classifier

class MyNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("MyNotesApp", "Application started, initializing Classifier...")
        try {
            Classifier.init(this)
            Log.d("MyNotesApp", "Classifier initialized successfully.")
        } catch (e: Exception) {
            Log.e("MyNotesApp", "Classifier initialization failed: ${e.message}", e)
        }
    }

}
