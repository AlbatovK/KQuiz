package com.albatros.kquiz.ui.fragments.result

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.ResultFragmentBinding
import com.albatros.kquiz.model.data.info.ClientInfo
import com.albatros.kquiz.ui.activity.MainActivity
import com.albatros.kquiz.ui.adapter.client.ResultAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ResultFragment : Fragment(), MainActivity.IOnBackPressed {

    private lateinit var binding: ResultFragmentBinding
    private val args by navArgs<ResultFragmentArgs>()
    private val viewModel: ResultViewModel by viewModel { parametersOf(args.question) }

    override fun onBackPressed(): Boolean = true

    private val onPassiveStateEndedObserver = Observer<Boolean> { value ->
        if (value) {
            viewModel.getNextQuestion()?.let {
                val direction = ResultFragmentDirections.actionResultFragmentToGameFragment(it)
                findNavController().navigate(direction)
                return@Observer
            }
            viewModel.stopSession()
            val direction = ResultFragmentDirections.actionResultFragmentToEnterFragment()
            findNavController().navigate(direction)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.custom_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> true
        else -> false
    }

    private val onInfoLoaded = Observer<List<ClientInfo>> {
        binding.list.adapter = ResultAdapter(it.toMutableList())
        binding.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ResultFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.onPassiveStateEnded.observe(viewLifecycleOwner, onPassiveStateEndedObserver)
        viewModel.usersInfo.observe(viewLifecycleOwner, onInfoLoaded)
    }
}