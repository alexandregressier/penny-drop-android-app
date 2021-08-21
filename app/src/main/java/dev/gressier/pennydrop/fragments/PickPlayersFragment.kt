package dev.gressier.pennydrop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.gressier.pennydrop.databinding.FragmentPickPlayersBinding
import dev.gressier.pennydrop.viewmodels.PickPlayersViewModel

class PickPlayersFragment : Fragment() {

    private val pickPlayersViewModel by activityViewModels<PickPlayersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentPickPlayersBinding.inflate(inflater, container, false).apply {
            vm = pickPlayersViewModel
        }
        return binding.root
    }
}