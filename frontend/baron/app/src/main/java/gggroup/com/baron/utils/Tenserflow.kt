package gggroup.com.baron.utils

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import gggroup.com.baron.tensorflow.Classifier
import gggroup.com.baron.tensorflow.TensorFlowImageClassifier
import java.io.BufferedInputStream
import java.io.InputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


object Tenserflow {
    private val INPUT_SIZE = 224
    private val IMAGE_MEAN = 128
    private val IMAGE_STD = 128.0f
    private val INPUT_NAME = "input"
    private val OUTPUT_NAME = "final_result"

    private val MODEL_FILE = "file:///android_asset/graph.pb"
    private val LABEL_FILE = "file:///android_asset/labels.txt"
    private var topResult: String? = "none"
    var secondResult: String? = "none"
    private var topResultConfidence: Float? = 0.0f
    var secondResultConfidence: Float? = 0.0f
    private var classifier: Classifier? = null
    private var executor = Executors.newSingleThreadExecutor()

    fun initTensorFlowAndLoadModel(assetManager: AssetManager) {
        executor.execute {
            try {
                classifier = TensorFlowImageClassifier.create(
                        assetManager,
                        MODEL_FILE,
                        LABEL_FILE,
                        INPUT_SIZE,
                        IMAGE_MEAN,
                        IMAGE_STD,
                        INPUT_NAME,
                        OUTPUT_NAME)


            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

    fun checkImage(URL: String, top: String, second: String): Boolean {

        var bitmap: Bitmap? = null
        val ins: InputStream?
        val bis: BufferedInputStream?
        try {
            val conn = java.net.URL(URL).openConnection()
            conn.connect()
            ins = conn.getInputStream()
            bis = BufferedInputStream(ins, 8192)
            bitmap = BitmapFactory.decodeStream(bis)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val mExecutor = Executors.newFixedThreadPool(3)
        mExecutor.execute {
            bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
        }
        mExecutor.shutdown()
        mExecutor.awaitTermination(java.lang.Long.MAX_VALUE, TimeUnit.DAYS)

        val results: List<Classifier.Recognition>? = classifier?.recognizeImage(bitmap)


        if (results != null) {
            topResult = results[0].title
            topResultConfidence = results[0].confidence
            if (topResult == top || topResult == second) return true
            val size = results.size - 1
            if (size >= 1) {
                secondResult = results[1].title
                secondResultConfidence = results[1].confidence

                if (secondResultConfidence!! < 0.5) {
                    if (secondResult == top || secondResult == second) return true
                }
                if (topResultConfidence!! < 0.5) {
                    topResult = "none"
                }
            }

        }
        return false
    }
}