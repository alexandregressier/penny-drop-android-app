package dev.gressier.pennydrop.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.gressier.pennydrop.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? =
        inflater.inflate(R.layout.fragment_about, container, false).apply {
            findViewById<TextView>(R.id.about_credits)?.apply {
                movementMethod = LinkMovementMethod.getInstance() // Makes the links clickable
            }
            findViewById<TextView>(R.id.about_icon_credits)?.apply {
                SpannableString(getString(R.string.penny_drop_icons)).apply {
                    setSpan("https://materialdesignicons.com/icon/star-circle-outline", 4, 8, 0)
                    setSpan("https://materialdesignicons.com/icon/dice-6", 13, 26, 0)
                    setSpan("https://materialdesignicons.com", 46, 67, 0)
                    text = this
                    movementMethod = LinkMovementMethod.getInstance() // Makes the links clickable
                }
            }
        }
}