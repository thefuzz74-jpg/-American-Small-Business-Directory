package com.americanbusiness.directory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

data class Business(val name: String, val category: String, val address: String, val description: String)

class MainActivity : Activity() {
    // Only administrator-approved, paid records should be supplied by a future trusted backend.
    // Never interpret this local demo dataset as verified listings.
    private val businesses = emptyList<Business>()
    private lateinit var results: LinearLayout
    private lateinit var search: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            setPadding(22, 32, 22, 16)
        }
        val title = TextView(this).apply {
            text = "🇺🇸 American Small Business Directory"
            textSize = 25f
            setTextColor(Color.rgb(14, 44, 90))
            gravity = Gravity.CENTER
        }
        root.addView(title)
        root.addView(TextView(this).apply {
            text = "Discover independently owned American businesses"
            textSize = 15f
            gravity = Gravity.CENTER
            setPadding(0, 14, 0, 20)
        })
        search = EditText(this).apply {
            hint = "Search name, city, state, category"
            setSingleLine(true)
        }
        root.addView(search)
        val searchButton = Button(this).apply {
            text = "Search businesses"
            setOnClickListener { render(search.text.toString()) }
        }
        root.addView(searchButton)
        val applyButton = Button(this).apply {
            text = "List your business — $1.99/month"
            setOnClickListener {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Business applications")
                    .setMessage("Applications and secure payment checkout are coming soon. No payments are collected in this preview. Listings require administrator verification before publication.")
                    .setPositiveButton("OK", null).show()
            }
        }
        root.addView(applyButton)
        val scroll = ScrollView(this)
        results = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(results)
        root.addView(scroll, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)
        render("")
    }

    private fun render(query: String) {
        results.removeAllViews()
        val matching = businesses.filter {
            listOf(it.name, it.category, it.address).any { field -> field.contains(query, ignoreCase = true) }
        }
        if (matching.isEmpty()) {
            results.addView(TextView(this).apply {
                text = "No verified businesses listed yet. Applications will open after the secure verification and payment system is ready."
                textSize = 17f
                setPadding(8, 40, 8, 8)
            })
        }
        matching.forEach { business ->
            results.addView(TextView(this).apply {
                text = "✓ ${business.name}\n${business.category}\n${business.address}\n${business.description}"
                textSize = 17f
                setPadding(8, 24, 8, 6)
            })
            results.addView(Button(this).apply {
                text = "Open in Maps"
                setOnClickListener {
                    val uri = Uri.parse("geo:0,0?q=" + Uri.encode(business.address))
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
            })
        }
    }
}
