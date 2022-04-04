package com.albatros.kquiz.ui.fragments.enter

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.ClientInputDialogBinding
import com.albatros.kquiz.databinding.EnterFragmentBinding
import com.albatros.kquiz.databinding.NameDialogBinding
import com.albatros.kquiz.ui.activity.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class EnterFragment : Fragment(), MainActivity.IOnBackPressed {

    private val viewModel: EnterViewModel by viewModel()
    private lateinit var binding: EnterFragmentBinding

    private val onUserIdChangedObserver = Observer<Long?> {
        if (it == null) {
            Toast.makeText(context, "Unable to find session with this id. Try again.", Toast.LENGTH_SHORT).show()
            return@Observer
        }
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
        setHasOptionsMenu(false)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.userId.observe(viewLifecycleOwner, onUserIdChangedObserver)

        with(binding) {

            cardCreate.setOnClickListener {
                val direction = EnterFragmentDirections.actionEnterFragmentToQuizListFragment()
                findNavController().navigate(direction)
            }

            cardExit.setOnClickListener {
                activity?.let {
                    it.finish()
                    it.finishAffinity()
                }
            }

            cardEnter.setOnClickListener {
                showClientInputDialog()
            }
        }
    }

    private fun showClientInputDialog() {
        val dialogBinding = ClientInputDialogBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(binding.root.context).apply {
            setView(dialogBinding.root)
            setPositiveButton("Готово") { it, _ ->
                val name = dialogBinding.nameEdit.text.toString().ifBlank { "NoName" }
                val id = dialogBinding.idEdit.text.toString()
                viewModel.enterSession(id, name)
                it.cancel()
            }
        }.create()
        dialog.window?.let {
            it.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.dialog_shape, activity?.theme))
            it.attributes.verticalMargin = -0.1F
        }
        dialog.show()
    }

    override fun onBackPressed(): Boolean =
        activity?.let {
            it.finish()
            it.finishAffinity()
            true
        } ?: false
}