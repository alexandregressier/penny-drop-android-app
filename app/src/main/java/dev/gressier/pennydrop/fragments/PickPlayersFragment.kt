package dev.gressier.pennydrop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dev.gressier.pennydrop.R
import dev.gressier.pennydrop.databinding.FragmentPickPlayersBinding
import dev.gressier.pennydrop.types.NewPlayer
import dev.gressier.pennydrop.viewmodels.GameViewModel
import dev.gressier.pennydrop.viewmodels.PickPlayersViewModel
import kotlinx.coroutines.launch

class PickPlayersFragment : Fragment() {

    private val pickPlayersViewModel by activityViewModels<PickPlayersViewModel>()
    private val gameViewModel by activityViewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentPickPlayersBinding.inflate(inflater, container, false).apply {
            vm = pickPlayersViewModel
            buttonPlayGame.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    gameViewModel.startGame(
                        pickPlayersViewModel.players.value
                            ?.filter { it.isIncluded.get() }
                            ?.map(NewPlayer::toPlayer)
                            ?: emptyList()
                    )
                    findNavController().navigate(R.id.gameFragment)
                }
            }
        }
        return binding.root
    }
}