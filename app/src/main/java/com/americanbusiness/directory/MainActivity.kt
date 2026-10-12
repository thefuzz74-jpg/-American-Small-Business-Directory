package com.americanbusiness.directory

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*

data class Business(val name: String, val category: String, val address: String, val description: String)

class MainActivity : Activity() {
    private val navy = Color.rgb(18, 42, 77)
    private val blue = Color.rgb(35, 74, 124)
    private val red = Color.rgb(180, 48, 59)
    private val muted = Color.rgb(100, 116, 139)
    private val pageBackgroundColor = Color.rgb(246, 248, 251)
    private val businesses = emptyList<Business>() // Replace only with verified backend records.
    private lateinit var results: LinearLayout
    private lateinit var search: EditText

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
    private fun shape(color: Int, radius: Int = 16, stroke: Int? = null): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radius).toFloat()
            if (stroke != null) setStroke(dp(1), stroke)
        }

    private fun text(value: String, size: Float, color: Int = navy, bold: Boolean = false): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(null, Typeface.BOLD)
        }

    private fun vertical(): LinearLayout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    private fun addSpace(parent: LinearLayout, height: Int) {
        parent.addView(View(this), LinearLayout.LayoutParams(1, dp(height)))
    }
    private fun button(label: String, color: Int, action: () -> Unit): TextView =
        text(label, 15f, Color.WHITE, true).apply {
            gravity = Gravity.CENTER
            background = shape(color, 12)
            setPadding(dp(16), dp(15), dp(16), dp(15))
            setOnClickListener { action() }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = navy
        window.navigationBarColor = Color.rgb(236, 240, 245)

        val page = vertical().apply { setBackgroundColor(pageBackgroundColor) }
        val scroll = ScrollView(this).apply { isFillViewport = true }
        val content = vertical()
        scroll.addView(content)
        page.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val hero = vertical().apply {
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(navy, blue)
            )
            setPadding(dp(24), dp(28), dp(24), dp(30))
        }
        hero.addView(text("★  SUPPORT MAIN STREET AMERICA", 11f, Color.rgb(226, 235, 248), true))
        addSpace(hero, 18)
        hero.addView(text("Discover local.\nSupport American.", 30f, Color.WHITE, true))
        addSpace(hero, 12)
        hero.addView(text("Find independently owned American small businesses, reviewed before joining our directory.", 15f, Color.rgb(225, 233, 244)))
        addSpace(hero, 24)
        val searchPanel = vertical().apply {
            background = shape(Color.WHITE, 14)
            setPadding(dp(14), dp(3), dp(14), dp(3))
        }
        search = EditText(this).apply {
            hint = "Search businesses or locations"
            setHintTextColor(muted)
            setTextColor(navy)
            textSize = 15f
            setSingleLine(true)
            background = null
            compoundDrawablePadding = dp(8)
        }
        searchPanel.addView(search)
        hero.addView(searchPanel)
        content.addView(hero)

        val body = vertical().apply { setPadding(dp(20), dp(24), dp(20), dp(30)) }
        content.addView(body)
        val heading = text("Explore the directory", 22f, navy, true)
        val featured = vertical().apply {
            background = shape(Color.WHITE, 18, Color.rgb(224, 230, 238))
            setPadding(dp(20), dp(20), dp(20), dp(20))
        }
        featured.addView(text("★  FEATURED AMERICAN BUSINESSES", 13f, red, true))
        addSpace(featured, 10)
        featured.addView(text("LAUNCH SHOWCASE  •  BELVIDERE, TENNESSEE", 12f, blue, true))
        addSpace(featured, 9)
        featured.addView(text("Lexie Holler Market & Restaurant", 22f, navy, true))
        addSpace(featured, 8)
        featured.addView(text("A locally owned country store and restaurant serving daily plate lunches and a full menu, with groceries and general merchandise.", 14f, muted))
        addSpace(featured, 12)
        featured.addView(button("Find Lexie Holler on Maps ↗", blue) {
            val mapUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode("Lexie Holler Market and Restaurant, Belvidere, Tennessee"))
            startActivity(Intent(Intent.ACTION_VIEW, mapUri))
        })
        addSpace(featured, 20)
        featured.addView(text("FEATURED BUSINESS ADVERTISING", 12f, red, true))
        addSpace(featured, 8)
        featured.addView(text("Promote your business here", 19f, navy, true))
        addSpace(featured, 8)
        featured.addView(text("Sponsored placements: $4.99 for 30 days, after payment and administrator approval.", 14f, muted))
        addSpace(featured, 12)
        featured.addView(button("Learn about featured placement", blue) {
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Front-page advertising")
                .setMessage("Verified businesses will be able to purchase optional sponsored placement after the secure checkout and review system is connected. Front-page featured advertising costs $4.99 per 30 days, separate from the $1.99/month directory subscription. All advertisements require approval. No payment is collected in this preview.")
                .setPositiveButton("OK", null).show()
        })
        body.addView(featured)
        addSpace(body, 24)
        body.addView(heading)
        addSpace(body, 6)
        body.addView(text("Verified listings, real local businesses.", 14f, muted))
        addSpace(body, 20)

        val categories = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val names = listOf("All", "Food", "Retail", "Services")
        names.forEachIndexed { index, category ->
            val chip = text(category, 12f, if (index == 0) Color.WHITE else navy, true).apply {
                gravity = Gravity.CENTER
                background = shape(if (index == 0) navy else Color.WHITE, 22, if (index == 0) null else Color.rgb(222, 229, 238))
                setPadding(dp(10), dp(12), dp(10), dp(12))
                setOnClickListener {
                    render(search.text.toString(), if (category == "All") "" else category)
                    for (i in 0 until categories.childCount) {
                        val child = categories.getChildAt(i) as TextView
                        child.background = shape(if (i == index) navy else Color.WHITE, 22)
                        child.setTextColor(if (i == index) Color.WHITE else navy)
                    }
                }
            }
            categories.addView(chip, LinearLayout.LayoutParams(0, -2, 1f).apply {
                if (index > 0) leftMargin = dp(6)
            })
        }
        body.addView(categories)
        addSpace(body, 20)
        results = vertical()
        body.addView(results)
        addSpace(body, 22)

        val join = vertical().apply {
            background = shape(Color.WHITE, 18, Color.rgb(224, 230, 238))
            setPadding(dp(20), dp(22), dp(20), dp(22))
            elevation = dp(2).toFloat()
        }
        join.addView(text("Own an American small business?", 20f, navy, true))
        addSpace(join, 8)
        join.addView(text("Apply for review and reach customers looking to shop independent.", 14f, muted))
        addSpace(join, 16)
        join.addView(text("$1.99 / month after approval", 17f, red, true))
        addSpace(join, 16)
        join.addView(button("Apply to be listed  →", red) {
            startActivity(Intent(this@MainActivity, BusinessApplicationActivity::class.java))
        })
        body.addView(join)
        addSpace(body, 18)
        body.addView(text("Every listing must be independently owned, U.S.-owned and approved. Verification documents remain private.", 12f, muted).apply { gravity = Gravity.CENTER })

        val footer = LinearLayout(this).apply {
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
            setPadding(dp(12), dp(12), dp(12), dp(12))
        }
        listOf("⌂  Discover", "✓  Verified", "▣  Owner Center").forEachIndexed { i, label ->
            val item = text(label, 12f, if (i == 0) navy else muted, i == 0).apply {
                gravity = Gravity.CENTER
                setPadding(dp(5), dp(10), dp(5), dp(10))
                setOnClickListener {
                    if (i == 1) AlertDialog.Builder(this@MainActivity)
                        .setTitle("Verified listings")
                        .setMessage("Businesses must be reviewed and have an active subscription before appearing in search.")
                        .setPositiveButton("OK", null).show()
                    else if (i == 2) startActivity(Intent(this@MainActivity, OwnerDashboardActivity::class.java))
                    else scroll.smoothScrollTo(0, 0)
                }
            }
            footer.addView(item, LinearLayout.LayoutParams(0, -2, 1f))
        }
        page.addView(footer)
        setContentView(page)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { render(s.toString(), "") }
            override fun afterTextChanged(s: Editable?) {}
        })
        render("", "")
    }

    private fun render(query: String, category: String) {
        results.removeAllViews()
        val matching = businesses.filter {
            (category.isBlank() || it.category.contains(category, true)) &&
                listOf(it.name, it.category, it.address).any { field -> field.contains(query, true) }
        }
        if (matching.isEmpty()) {
            val empty = vertical().apply {
                background = shape(Color.WHITE, 18, Color.rgb(226, 232, 240))
                gravity = Gravity.CENTER
                setPadding(dp(20), dp(32), dp(20), dp(32))
            }
            empty.addView(text("⌕", 44f, blue).apply { gravity = Gravity.CENTER })
            addSpace(empty, 8)
            empty.addView(text("Growing our directory", 20f, navy, true).apply { gravity = Gravity.CENTER })
            addSpace(empty, 10)
            empty.addView(text("No approved listings are available yet. Check back as verified American businesses join.", 14f, muted).apply { gravity = Gravity.CENTER })
            results.addView(empty)
        }
        matching.forEach { business ->
            val card = vertical().apply {
                background = shape(Color.WHITE, 16)
                setPadding(dp(18), dp(18), dp(18), dp(18))
            }
            card.addView(text(business.name, 19f, navy, true))
            card.addView(text(business.category + "  •  Verified", 13f, red, true))
            addSpace(card, 8)
            card.addView(text(business.address, 14f, muted))
            addSpace(card, 14)
            card.addView(button("Directions ↗", blue) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(business.address))))
            })
            results.addView(card)
            addSpace(results, 12)
        }
    }
}
