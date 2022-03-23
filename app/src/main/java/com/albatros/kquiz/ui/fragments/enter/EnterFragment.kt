package com.albatros.kquiz.ui.fragments.enter

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.albatros.kquiz.databinding.EnterFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EnterFragment : Fragment() {

    private val viewModel: EnterViewModel by viewModel()
    private lateinit var binding: EnterFragmentBinding

    private val onUserIdChangedObserver = Observer<Long?> {
        if (it == null) {
            Toast.makeText(context, "Some mistake", Toast.LENGTH_SHORT).show()
            return@Observer
        }
        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        val direction = EnterFragmentDirections.actionEnterFragmentToClientFragment()
        findNavController().navigate(direction)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EnterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.userId.observe(viewLifecycleOwner, onUserIdChangedObserver)

        binding.createBtn.setOnClickListener {
            val direction = EnterFragmentDirections.actionEnterFragmentToQuizListFragment()
            findNavController().navigate(direction)
        }

        binding.enterBtn.setOnClickListener {
            val id = binding.edit.text.toString()
            viewModel.enterSession(id, "another")
        }
    }
}