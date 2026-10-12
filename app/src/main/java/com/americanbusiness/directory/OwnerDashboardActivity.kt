package com.americanbusiness.directory

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*

/** Owner dashboard preview. Real account authentication and server permissions are required. */
class OwnerDashboardActivity : Activity() {
    private val navy = Color.rgb(18, 42, 77)
    private val red = Color.rgb(180, 48, 59)
    private fun dp(n: Int) = (n * resources.displayMetrics.density).toInt()
    private fun text(value: String, size: Float, bold: Boolean = false) = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(navy)
        if (bold) setTypeface(null, Typeface.BOLD)
    }
    private fun action(container: LinearLayout, name: String, message: String) {
        val button = Button(this).apply {
            text = name
            setOnClickListener {
                AlertDialog.Builder(this@OwnerDashboardActivity)
                    .setTitle(name)
                    .setMessage(message)
                    .setPositiveButton("OK", null).show()
            }
        }
        container.addView(button)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = navy
        val scroll = ScrollView(this).apply { setBackgroundColor(Color.rgb(246,248,251)) }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(28), dp(22), dp(28))
        }
        scroll.addView(content)
        content.addView(text("Business Owner Center", 26f, true))
        content.addView(text("Manage your American small business listing", 15f))
        val notice = text("PREVIEW MODE — Account sign-in and secure backend are not connected. Changes are not saved.", 14f, true).apply {
            setTextColor(red)
            setPadding(0, dp(22), 0, dp(18))
        }
        content.addView(notice)
        content.addView(text("Listing status", 19f, true))
        content.addView(text("Not submitted • Not verified • No active subscription", 14f))
        val heading = text("Business tools", 19f, true).apply { setPadding(0, dp(26), 0, dp(12)) }
        content.addView(heading)
        action(content, "Edit business profile", "Profile editing will become available after secure account sign-in is connected.")
        action(content, "Upload storefront photos", "Photos will be uploaded to protected storage after the backend is connected.")
        action(content, "Post a promotion", "Approved businesses will be able to publish offers and events.")
        action(content, "View subscription", "The listing price is $1.99/month after verification. No payment is collected in this preview.")
        action(content, "Application status", "Applications will show pending, needs information, approved, or rejected once secure submissions are enabled.")
        content.addView(text("Apply for verification", 19f, true).apply { setPadding(0, dp(24), 0, dp(12)) })
        content.addView(Button(this).apply {
            text = "Open business application"
            setOnClickListener {
                startActivity(Intent(this@OwnerDashboardActivity, BusinessApplicationActivity::class.java))
            }
        })
        setContentView(scroll)
    }
}
