package com.example.mynotes.ml

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object Classifier {

    private lateinit var interpreter: Interpreter
    private val labels = listOf("happy", "sad", "angry", "fear", "surprise", "neutral")
    private var isInitialized = false

    private val vocabSize = 10000 // set this to your actual vocab size

    fun init(context: Context) {
        if (isInitialized) {
            Log.d("Classifier", "Already initialized, skipping.")
            return
        }
        try {
            Log.d("Classifier", "Initializing Classifier...")
            val model = loadModelFile(context, "text_classification.tflite")
            Log.d("Classifier", "Model file loaded successfully.")
            interpreter = Interpreter(model)
            isInitialized = true
            Log.d("Classifier", "Interpreter created successfully.")
        } catch (e: Exception) {
            Log.e("Classifier", "Initialization failed: ${e.message}", e)
        }
    }

    fun classify(text: String): String {
        if (!isInitialized) {
            Log.e("Classifier", "classify() called before initialization!")
            throw IllegalStateException("Classifier is not initialized. Call init() first.")
        }

        Log.d("Classifier", "classify() called with text: \"$text\"")

        val input = preprocess(text) // <-- new preprocessing
        Log.d("Classifier", "Preprocessed input: ${input[0].joinToString(", ", "[", "]")}")

        val output = Array(1) { FloatArray(labels.size) }
        try {
            interpreter.run(input, output)
            Log.d("Classifier", "Model inference completed. Raw output: ${output[0].joinToString(", ", "[", "]")}")
        } catch (e: Exception) {
            Log.e("Classifier", "Error during model inference: ${e.message}", e)
            throw e
        }

        val maxIdx = output[0].indices.maxByOrNull { output[0][it] } ?: 0
        val predictedLabel = labels[maxIdx]
        Log.d("Classifier", "Predicted label: $predictedLabel")
        return predictedLabel
    }

    private fun loadModelFile(context: Context, filename: String): MappedByteBuffer {
        Log.d("Classifier", "Loading model file: $filename")
        val fileDescriptor = context.assets.openFd(filename)
        FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }
    }

    private fun preprocess(text: String): Array<FloatArray> {
        Log.d("Classifier", "Preprocessing text...")
        val tokens = tokenize(text)
        val clampedTokens = tokens.map { id ->
            if (id >= vocabSize) 0 else id // replace OOV with <UNK> (id=0)
        }
        // pad/truncate to 100 tokens
        val padded = FloatArray(100) { 0f }
        for (i in clampedTokens.indices.take(100)) {
            padded[i] = clampedTokens[i].toFloat()
        }
        return arrayOf(padded)
    }

    private fun tokenize(text: String): List<Int> {
        // Very naive tokenizer for now (replace with actual tokenizer used in training)
        return text.split(" ").map { kotlin.math.abs(it.hashCode()) % vocabSize }
    }
}
