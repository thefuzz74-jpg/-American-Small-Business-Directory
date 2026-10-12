package com.americanbusiness.directory

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*

/** Spotlight draft preview. No network upload or submission is performed. */
class SpotlightActivity : Activity() {
    private var selectedPhoto: Uri? = null
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun input(hintText: String): EditText = EditText(this).apply {
        hint = hintText
        textSize = 15f
        setSingleLine(hintText != "Describe the product, service or offer")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(18,42,77)
        val scroll = ScrollView(this).apply { setBackgroundColor(Color.rgb(246,248,251)) }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(26), dp(22), dp(26))
        }
        scroll.addView(root)
        root.addView(TextView(this).apply {
            text = "★ Business Spotlight"
            textSize = 26f
            setTextColor(Color.rgb(18,42,77))
        })
        root.addView(TextView(this).apply {
            text = "Showcase products, services, sales and events. Every submission must be approved before publication."
            textSize = 15f
            setPadding(0, dp(12), 0, dp(20))
        })
        val type = Spinner(this)
        type.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            listOf("Product", "Service", "Sale", "Event"))
        root.addView(type)
        val title = input("Spotlight title *")
        val description = input("Describe the product, service or offer")
        description.minLines = 4
        description.gravity = android.view.Gravity.TOP
        val price = input("Price or special offer (optional)")
        root.addView(title)
        root.addView(description)
        root.addView(price)
        val photoLabel = TextView(this).apply { text = "No photo selected" }
        root.addView(Button(this).apply {
            text = "Choose spotlight photo"
            setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }
                startActivityForResult(intent, 401)
            }
        })
        root.addView(photoLabel)
        root.addView(Button(this).apply {
            text = "Review spotlight draft"
            setOnClickListener {
                if (title.text.isBlank() || description.text.isBlank()) {
                    Toast.makeText(this@SpotlightActivity, "Enter a title and description.", Toast.LENGTH_LONG).show()
                } else {
                    AlertDialog.Builder(this@SpotlightActivity)
                        .setTitle("Spotlight preview")
                        .setMessage("Type: ${type.selectedItem}\nTitle: ${title.text}\nPrice: ${price.text}\nPhoto selected: ${selectedPhoto != null}\n\nThis is a local preview only. It has NOT been uploaded, submitted for approval, or published. Secure owner login and administrator review must be connected first.")
                        .setPositiveButton("OK", null).show()
                }
            }
        })
        setContentView(scroll)
    }
    @Deprecated("Legacy activity result API")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 401 && resultCode == RESULT_OK) {
            selectedPhoto = data?.data
            Toast.makeText(this, if (selectedPhoto != null) "Photo selected for draft" else "No photo selected", Toast.LENGTH_SHORT).show()
        }
    }
}
