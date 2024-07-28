//package com.example.compoundhealtheffects
//
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import okhttp3.Call
//import okhttp3.Callback
//import okhttp3.MediaType
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.MultipartBody
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import okhttp3.Response
//import java.io.File
//import java.io.IOException
//import java.net.URLConnection
//
//
//class MainActivity : AppCompatActivity() {
//    private var selectedImageUri: Uri? = Uri.EMPTY
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//
//    private val selectImageResultLauncher = registerForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { result ->
//        val displayImage = findViewById<ImageView>(R.id.selectedImage)
//        selectedImageUri = result
//        displayImage.setImageURI(result)
//    }
//
//    fun selectImageFromGallery(view: View) {
//        val request = PickVisualMediaRequest(ImageOnly)
//        selectImageResultLauncher.launch(request)
//    }
//
////    private val fetchCompoundsHEUrl = "http://127.0.0.1:5000/upload"
//private val fetchCompoundsHEUrl = "http://10.0.2.2:5000/upload"
//
////    fun fetchCompoundsHealthEffects(view: View) {
////        val displayTextView = findViewById<TextView>(R.id.textView)
////        if (selectedImageUri != null) {
////            val filePath: String = getPath(selectedImageUri!!)
////            val file = File(filePath)
////
////            // Determine the MIME type based on the file extension
////            // Added dynamic MIME type detection
////            val mimeType = URLConnection.guessContentTypeFromName(file.name) ?: "application/octet-stream"
////            val mediaType = MediaType.parse(mimeType)
////            val requestBody = file.readBytes().toRequestBody(mediaType) // Added mediaType to requestBody
////
////            // Use the correct file name with the proper extension
////            val multipartBody = MultipartBody.Builder()
////                .setType(MultipartBody.FORM)
////                .addFormDataPart("image", file.name, requestBody) // Use file.name to match the image's actual name
////                .build()
////
////            val request = Request.Builder()
////                .url(fetchCompoundsHEUrl)
////                .post(multipartBody)
////                .build()
////
////            val httpClient = OkHttpClient()
////            httpClient.newCall(request).enqueue(object : Callback {
////                override fun onFailure(call: Call, e: IOException) {
////                    e.printStackTrace()
////                    runOnUiThread {
////                        displayTextView.text = "Failed to fetch compounds health effects: ${e.message}"
////                    }
////                }
////
////                override fun onResponse(call: Call, response: Response) {
////                    if (response.isSuccessful) {
////                        val responseBody = response.body?.string() ?: "No response"
////                        runOnUiThread {
////                            displayTextView.text = responseBody
////                        }
////                    } else {
////                        runOnUiThread {
////                            displayTextView.text = "Failed to fetch compounds health effects: ${response.message} - ${response.code}"
////                        }
////                    }
////                }
////            })
////        }
////    }
//
//
//    fun fetchCompoundsHealthEffects(view: View) {
//        val displayTextView = findViewById<TextView>(R.id.textView)
//        if(selectedImageUri != null) {
//            val filePath: String = getPath(selectedImageUri!!)
//            val file = File(filePath)
//            val mediaType = "image/jpg".toMediaType() // Ensure the correct MIME type
//            val httpClient = OkHttpClient()
//
//            val requestBody: MultipartBody =
//                MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("image", null, file.readBytes().toRequestBody(mediaType))
//                    .build()
//
//            val compoundsHERequest: Request =
//                Request.Builder()
//                    .url(fetchCompoundsHEUrl)
//                    .post(requestBody)
//                    .build()
//
//            httpClient.newCall(compoundsHERequest).enqueue(object: Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    e.printStackTrace()
//                    runOnUiThread {
//                        displayTextView.text = "Failed to fetch compounds health effects: ${e.message}"
//                    }
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body?.string() ?: "No response"
//                        runOnUiThread {
//                            displayTextView.text = responseBody
//                        }
//                    } else {
//                        runOnUiThread {
//                            displayTextView.text = "Failed to fetch compounds health effects: ${response.message}"
//                        }
//                    }
//                }
//
//            })
//        }
//    }
//
//    private fun getPath(uri: Uri): String {
//        var res: String? = null
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = contentResolver.query(uri, proj, null, null, null)
//        if (cursor!!.moveToFirst()) {
//            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            res = cursor.getString(columnIndex)
//        }
//        cursor.close()
//        return res!!
////        return res ?: throw IllegalStateException("File path not found")
//
//    }
//}


package com.example.compoundhealtheffects

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Call
import okhttp3.Callback
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private val selectImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { result ->
        val displayImage = findViewById<ImageView>(R.id.selectedImage)
        selectedImageUri = result
        displayImage.setImageURI(result)
    }

    fun selectImageFromGallery(view: View) {
        val request = PickVisualMediaRequest(ImageOnly)
        selectImageResultLauncher.launch(request)
    }

    private val fetchCompoundsHEUrl = "http://10.0.2.2:5000/upload" // Update URL if necessary

    fun fetchCompoundsHealthEffects(view: View) {
        val displayTextView = findViewById<TextView>(R.id.textView)
        if (selectedImageUri != null) {
            val filePath: String = getPath(selectedImageUri!!)
            val file = File(filePath)
            val mediaType = "image/png".toMediaType() // Ensure correct MIME type
            val httpClient = OkHttpClient()

            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "filename", file.readBytes().toRequestBody(mediaType))
                .build()

            val request: Request = Request.Builder()
                .url(fetchCompoundsHEUrl)
                .post(requestBody)
                .build()

            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    runOnUiThread {
                        displayTextView.text = "Failed to fetch compounds health effects: ${e.message}, ${e.cause}"
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string() ?: "No response"
                        runOnUiThread {
                            displayTextView.text = responseBody
                        }
                    } else {
                        runOnUiThread {
                            displayTextView.text = "Failed to fetch compounds health effects: ${response.message} - ${response.code} \n ${response.body.toString()}"
                        }
                    }
                }
            })
        }
    }

    private fun getPath(uri: Uri): String {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(columnIndex)
            cursor.close()
        }
        return res ?: throw IllegalStateException("File path not found")
    }
}
