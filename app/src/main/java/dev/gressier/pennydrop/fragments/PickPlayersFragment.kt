package dev.gressier.pennydrop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.gressier.pennydrop.databinding.FragmentPickPlayersBinding

class PickPlayersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentPickPlayersBinding.inflate(inflater, container, false)
        return binding.root
    }
}