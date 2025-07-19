package com.example.mynotes.ml

import android.content.Context
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier
import java.util.concurrent.Executors

object Classifier {
    private var classifier: NLClassifier? = null

    fun init(context: Context) {
        val model = "text_classifier.tflite"
        classifier = NLClassifier.createFromFile(context, model)
    }

    fun classify(text: String): String {
        val results = classifier?.classify(text) ?: return "none"
        return results.maxByOrNull { it.score }?.label ?: "none"
    }
}
