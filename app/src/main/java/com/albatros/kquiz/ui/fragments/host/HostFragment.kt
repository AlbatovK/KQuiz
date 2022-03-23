package com.albatros.kquiz.ui.fragments.host

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.HostFragmentBinding
import com.albatros.kquiz.model.data.ClientInfo
import com.albatros.kquiz.ui.adapter.client.ClientAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HostFragment : Fragment() {
    private lateinit var binding: HostFragmentBinding
    private val viewModel: HostViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HostFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val onUsersInfoChangedObserver = Observer<List<ClientInfo>> {
        binding.list.adapter = ClientAdapter(it as MutableList<ClientInfo>)
        binding.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.host_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_refresh -> {
            viewModel.updateUserInfo()
            true
        } else -> false
    }

    private val onSessionStartedObserver = Observer<Boolean> {
        if (it) {
            /* start game from fragment */
            return@Observer
        }
        Toast.makeText(context, "Unable to start", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.usersInfo.observe(viewLifecycleOwner, onUsersInfoChangedObserver)
        viewModel.started.observe(viewLifecycleOwner, onSessionStartedObserver)

        binding.startBtn.setOnClickListener {
            viewModel.startSession()
        }
    }
}