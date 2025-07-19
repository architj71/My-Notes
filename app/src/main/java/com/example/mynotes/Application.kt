package com.example.mynotes

import android.app.Application
import com.example.mynotes.ml.Classifier

class MyNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Classifier.init(this)
    }
}
