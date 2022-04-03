package com.albatros.kquiz.ui.fragments.host

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.HostFragmentBinding
import com.albatros.kquiz.databinding.InfoDialogBinding
import com.albatros.kquiz.model.data.info.ClientInfo
import com.albatros.kquiz.ui.activity.MainActivity
import com.albatros.kquiz.ui.adapter.client.ClientAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class HostFragment : Fragment(), MainActivity.IOnBackPressed {
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
        binding.list.adapter = ClientAdapter(it.toMutableList())
        binding.list.layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.host_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showInfoDialog() {
        val dialogBinding = InfoDialogBinding.inflate(layoutInflater)
        dialogBinding.sessionIdTxt.text = viewModel.getSessionId().toString()
        val dialog = MaterialAlertDialogBuilder(binding.root.context).apply {
            setView(dialogBinding.root)
            setNegativeButton("Закрыть") { it, _ -> it.cancel() }
            setPositiveButton("Поделиться") { it, _ ->
                val intent = Intent(Intent.ACTION_SEND)
                val shareBody = dialogBinding.sessionIdTxt.text.toString()
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(intent, "Invitation to the game"))
                it.cancel()
            }
        }.create()
        dialog.window?.let {
            it.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.dialog_shape, activity?.theme))
            it.attributes.verticalMargin = -0.08F
        }
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            val direction = HostFragmentDirections.actionHostFragmentToEnterFragment()
            findNavController().navigate(direction)
            viewModel.stopSession()
            true
        }
        R.id.action_info -> {
            showInfoDialog()
            true
        } else -> false
    }

    override fun onBackPressed(): Boolean {
        val direction = HostFragmentDirections.actionHostFragmentToEnterFragment()
        findNavController().navigate(direction)
        viewModel.stopSession()
        return true
    }

    private val onSessionStartedObserver = Observer<Boolean> {
        if (it) {
            val question = viewModel.getFirstQuestion()
            val direction = HostFragmentDirections.actionHostFragmentToGameFragment(question)
            findNavController().navigate(direction)
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

        with(binding) {
            cardLaunch.setOnClickListener {
                viewModel.startSession()
            }
        }
    }
}