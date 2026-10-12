package com.americanbusiness.directory

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * Local application draft only. Identity documents must be submitted to a secure
 * verified backend, never emailed, logged, committed, or placed in public storage.
 */
class BusinessApplicationActivity : Activity() {
    private val navy = Color.rgb(18, 42, 77)
    private val muted = Color.rgb(95, 109, 127)
    private val red = Color.rgb(180, 48, 59)
    private val fields = linkedMapOf<String, EditText>()
    private val documents = linkedMapOf<String, Uri>()
    private lateinit var declaration: CheckBox

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun label(value: String, size: Float = 15f, bold: Boolean = false) =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(navy)
            if (bold) setTypeface(null, Typeface.BOLD)
        }
    private fun cardBackground(): GradientDrawable = GradientDrawable().apply {
        setColor(Color.WHITE)
        cornerRadius = dp(16).toFloat()
    }
    private fun space(parent: LinearLayout, v: Int = 12) {
        parent.addView(View(this), LinearLayout.LayoutParams(1, dp(v)))
    }
    private fun section(parent: LinearLayout, title: String, detail: String) {
        space(parent, 24)
        parent.addView(label(title, 20f, true))
        space(parent, 5)
        parent.addView(label(detail, 13f).apply { setTextColor(muted) })
        space(parent)
    }
    private fun field(parent: LinearLayout, key: String, hint: String, required: Boolean = true) {
        parent.addView(label(key + if (required) " *" else "", 14f, true))
        val input = EditText(this).apply {
            this.hint = hint
            textSize = 15f
            setSingleLine(true)
            setPadding(dp(12), dp(8), dp(12), dp(10))
            setBackgroundColor(Color.WHITE)
        }
        parent.addView(input)
        fields[key] = input
        space(parent, 12)
    }
    private fun pick(parent: LinearLayout, title: String, note: String, requestCode: Int) {
        val button = Button(this).apply {
            text = "＋  " + title
            setTextColor(navy)
            setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "image/jpeg", "image/png"))
                }
                startActivityForResult(intent, requestCode)
            }
        }
        parent.addView(button)
        parent.addView(label(note, 12f).apply { setTextColor(muted) })
        space(parent, 8)
    }
    @Deprecated("Used for document picker compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data?.data != null) {
            val type = when (requestCode) {
                201 -> "Business registration"
                202 -> "Ownership documentation"
                203 -> "EIN confirmation"
                else -> "Supporting document"
            }
            documents[type] = data.data!!
            Toast.makeText(this, "$type selected for draft (not uploaded)", Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = navy
        val root = ScrollView(this).apply { setBackgroundColor(Color.rgb(246, 248, 251)) }
        val form = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(24), dp(22), dp(32))
        }
        root.addView(form)
        form.addView(label("🇺🇸 Business Verification", 25f, true))
        space(form, 8)
        form.addView(label("Apply for review before joining the American Small Business Directory.", 15f))
        section(form, "1. Business details", "Enter the business's registered information.")
        field(form, "Legal business name / LLC", "Registered legal name")
        field(form, "DBA / trading name", "Optional public business name", false)
        field(form, "Street address", "Business street address")
        field(form, "City", "City")
        field(form, "State", "State")
        field(form, "ZIP code", "ZIP")
        field(form, "Business email", "Contact email")
        field(form, "Business phone", "Contact telephone")
        field(form, "Website", "Optional", false)
        field(form, "Category", "Restaurant, retail, service...")
        field(form, "State of registration", "State where business is registered")
        section(form, "2. Ownership", "Disclose all owners and any parent company.")
        field(form, "Primary owner legal name", "Full legal name")
        field(form, "Primary owner's percentage", "Example: 100%")
        field(form, "Additional owners and percentages", "Names and ownership percentages, if any", false)
        field(form, "Parent company", "None, or legal company name")
        field(form, "Business description", "What does your business offer?")
        section(form, "3. Supporting documents", "Choose PDF, JPG, or PNG files. This preview keeps only document references on this screen; it does not upload or save them.")
        pick(form, "Business registration", "State registration or equivalent", 201)
        pick(form, "Ownership documentation", "Operating agreement, ownership ledger, or equivalent", 202)
        pick(form, "EIN confirmation", "IRS EIN assignment letter; do not upload tax returns", 203)
        form.addView(label("Government ID verification will be provided through a secure identity-verification provider, not direct document storage in the app.", 13f))
        section(form, "4. Declaration", "Applications are reviewed manually. No listing or charge is created from this preview.")
        declaration = CheckBox(this).apply {
            text = "I certify that the information supplied is accurate and I am authorized to apply for this business."
            setTextColor(navy)
        }
        form.addView(declaration)
        space(form)
        val review = Button(this).apply {
            text = "Review application"
            setOnClickListener {
                val required = listOf("Legal business name / LLC", "Street address", "City", "State", "ZIP code", "Business email", "Business phone", "Category", "State of registration", "Primary owner legal name", "Primary owner's percentage", "Parent company", "Business description")
                val missing = required.filter { fields[it]?.text.toString().isBlank() }
                when {
                    missing.isNotEmpty() -> AlertDialog.Builder(this@BusinessApplicationActivity)
                        .setTitle("Complete required fields")
                        .setMessage(missing.joinToString("\n")).setPositiveButton("OK", null).show()
                    !declaration.isChecked -> Toast.makeText(this@BusinessApplicationActivity, "Please accept the declaration.", Toast.LENGTH_LONG).show()
                    else -> AlertDialog.Builder(this@BusinessApplicationActivity)
                        .setTitle("Application preview complete")
                        .setMessage("Your information is ready for a secure submission system. Nothing has been sent, stored remotely, verified, or charged. Do not assume an application has been submitted.")
                        .setPositiveButton("OK", null).show()
                }
            }
        }
        form.addView(review)
        setContentView(root)
    }
}
