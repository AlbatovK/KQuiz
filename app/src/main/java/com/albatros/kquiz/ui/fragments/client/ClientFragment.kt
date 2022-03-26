package com.albatros.kquiz.ui.fragments.client

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.ClientFragmentBinding
import com.albatros.kquiz.model.data.info.ClientInfo
import com.albatros.kquiz.ui.activity.MainActivity
import com.albatros.kquiz.ui.adapter.client.ClientAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClientFragment : Fragment(), MainActivity.IOnBackPressed {

    private lateinit var binding: ClientFragmentBinding
    private val viewModel: ClientViewModel by viewModel()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.custom_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            val direction = ClientFragmentDirections.actionClientFragmentToEnterFragment()
            findNavController().navigate(direction)
            true
        } else -> false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ClientFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val onStartedStateChanged = Observer<Boolean> {
        if (it) {
            val question = viewModel.getFirstQuestion()
            val direction = ClientFragmentDirections.actionClientFragmentToGameFragment(question)
            findNavController().navigate(direction)
        }
    }

    override fun onBackPressed(): Boolean {
        val direction = ClientFragmentDirections.actionClientFragmentToEnterFragment()
        findNavController().navigate(direction)
        return true
    }

    private val onUsersInfoChangedObserver = Observer<List<ClientInfo>> {
        binding.list.adapter = ClientAdapter(it.toMutableList())
        binding.list.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.usersInfo.observe(viewLifecycleOwner, onUsersInfoChangedObserver)
        viewModel.started.observe(viewLifecycleOwner, onStartedStateChanged)
    }
}