package dev.gressier.pennydrop.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.gressier.pennydrop.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentGameBinding.inflate(inflater, container, false).apply {
            textCurrentTurnInfo.movementMethod = ScrollingMovementMethod()
        }
        return binding.root
    }
}