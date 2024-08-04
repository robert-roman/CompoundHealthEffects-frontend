package com.example.compoundhealtheffects

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Callback
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import android.util.Log

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

    private val fetchCompoundsHEUrl = "http://10.0.2.2:5000/upload"

    private val jsonIgnoreUnknown = Json { ignoreUnknownKeys = true }
    private val responseSerializer =
        MapSerializer(String.serializer(), CompoundHealthEffect.serializer())

    fun fetchCompoundsHealthEffects(view: View) {
        val displayTextView = findViewById<TextView>(R.id.textView)
        if (selectedImageUri != null) {
            val filePath: String = getPath(selectedImageUri!!)
            val file = File(filePath)
            val mediaType = "image/png".toMediaType() // Ensure correct MIME type
            val httpClient = OkHttpClient()

            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "filename",
                    file.readBytes().toRequestBody(mediaType))
                .build()

            val request: Request = Request.Builder()
                .url(fetchCompoundsHEUrl)
                .post(requestBody)
                .build()

            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    e.printStackTrace()
                    runOnUiThread {
                        displayTextView.text = "Failed to fetch compounds health effects: " +
                                "${e.message}, ${e.cause}"
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string() ?: "No response"
                        runOnUiThread {
                            val decodedResponse =
                                jsonIgnoreUnknown.decodeFromString(responseSerializer, responseBody)
                            val formattedResponse =
                                decodedResponse
                                    .map { e ->
                                        "Compound ${e.value.compound.name} has " +
                                                "${e.value.healthEffects.size} health effects."
                                    }
                                    .toString()
                            displayTextView.text = formattedResponse
                            renderResponseAsTable(decodedResponse)
                        }
                    } else {
                        runOnUiThread {
                            displayTextView.text = "Failed to fetch compounds health effects: " +
                                    "${response.message} - ${response.code} \n " +
                                    "${response.body.toString()}"
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

    private fun renderResponseAsTable(decodedResponse: Map<String, CompoundHealthEffect>) {
        val responseTableLayout = findViewById<TableLayout>(R.id.responseTableLayout)
        responseTableLayout.removeAllViews()

        // Map to keep track of effect details layouts by their unique IDs
        val effectDetailsLayouts = mutableMapOf<String, LinearLayout>()

        for (entry in decodedResponse) {
            val compound = entry.value.compound
            val healthEffects = entry.value.healthEffects

            val mainRow = createMainRow(compound)
            responseTableLayout.addView(mainRow)

            val dropdownContent = createDropdownContent(healthEffects, effectDetailsLayouts)
            responseTableLayout.addView(dropdownContent)

            mainRow.setOnClickListener {
                dropdownContent.visibility = if (dropdownContent.visibility == View.GONE)
                    View.VISIBLE else View.GONE
            }
        }
    }

    private fun createMainRow(compound: Compound): TableRow {
        return TableRow(this).apply {
            setBackgroundColor(Color.LTGRAY)
            layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )

            // Create and add TextViews for compound name and description
            val nameTextView = TextView(this@MainActivity).apply {
                text = compound.name
                setTextColor(Color.rgb(130, 175, 200))
                setPadding(8, 8, 8, 8)
                layoutParams = TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f)
            }

            val descriptionTextView = TextView(this@MainActivity).apply {
                text = compound.description
                setPadding(8, 8, 8, 8)
                layoutParams = TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 2f)
                setSingleLine(false)
                ellipsize = null
            }

            addView(nameTextView)
            addView(descriptionTextView)
        }
    }

    private fun createDropdownContent(
        healthEffects: Map<String, HealthEffect>,
        effectDetailsLayouts: MutableMap<String, LinearLayout>
    ): LinearLayout {
        val dropdownContent = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            visibility = View.GONE
            setBackgroundColor(Color.parseColor("#E0E0E0"))
            setPadding(8, 8, 8, 8)
        }

        val relatedHealthEffectsTextView = TextView(this).apply {
            text = "Related health effects: ${healthEffects.size}"
            setPadding(8, 8, 8, 8)
            setTextColor(Color.BLACK)
        }

        dropdownContent.addView(relatedHealthEffectsTextView)

        val healthEffectsContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        healthEffects.forEach { (effectId, effectInfo) ->
            val effectHeader = TextView(this).apply {
                text = effectInfo.name
                setPadding(8, 8, 8, 8)
                setTextColor(Color.rgb(130, 175, 200))

                val effectDetailsLayout = createEffectDetailsLayout(effectInfo).apply {
                    tag = effectId
                }
                effectDetailsLayouts[effectId] = effectDetailsLayout

                setOnClickListener {
                    val layout = effectDetailsLayouts[effectId]
                    layout?.visibility = if (layout?.visibility == View.GONE)
                        View.VISIBLE else View.GONE
                }
            }

            val effectDetailsLayout = effectDetailsLayouts[effectId]

            healthEffectsContainer.addView(effectHeader)
            effectDetailsLayout?.let { healthEffectsContainer.addView(it) }
        }

        dropdownContent.addView(healthEffectsContainer)

        return dropdownContent
    }

    private fun createEffectDetailsLayout(effectInfo: HealthEffect): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(8, 8, 8, 8)
            setBackgroundColor(Color.GREEN)
            visibility = View.GONE

            val effectTitle = TextView(this@MainActivity).apply {
                text = "${effectInfo.name}: ${effectInfo.description ?: "No description available"}"
                setPadding(8, 8, 8, 8)
                setTextColor(Color.BLACK)
            }

            addView(effectTitle)
        }
    }


}
