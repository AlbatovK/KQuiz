package com.albatros.kquiz.ui.fragments.list

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.NameDialogBinding
import com.albatros.kquiz.databinding.QuizListFragmentBinding
import com.albatros.kquiz.model.data.pojo.Quiz
import com.albatros.kquiz.ui.adapter.quiz.QuizAdapter
import com.albatros.kquiz.ui.adapter.quiz.QuizAdapterListener
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuizListFragment : Fragment() {

    private lateinit var binding: QuizListFragmentBinding
    private val viewModel: ListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuizListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val listener = object: QuizAdapterListener {
        override fun onQuizSelected(quiz: Quiz, view: View) {
            viewModel.registerSession(quiz)
        }
    }

    private val onQuizzesLoadedObserver = Observer<List<Quiz>?> {
        if (it == null) {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(1_000)
                Toast.makeText(context, "Could not load quiz list. Try again.", Toast.LENGTH_SHORT).show()
                val direction = QuizListFragmentDirections.actionQuizListFragmentToEnterFragment()
                findNavController().navigate(direction)
            }
            return@Observer
        }
        binding.list.adapter = QuizAdapter(it.toMutableList(), listener)
        binding.list.layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
        }
    }

    private val onRegisterResult = Observer<Long?> {
        if (it == null) {
            Toast.makeText(context, "Could not register session. Try again.", Toast.LENGTH_SHORT).show()
            return@Observer
        }
        showNameInputDialog(it)
    }

    private fun showNameInputDialog(id: Long) {
        val dialogBinding = NameDialogBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(binding.root.context).apply {
            setView(dialogBinding.root)
            setPositiveButton("Готово") { it, _ ->
                val name = dialogBinding.nameEdit.text.toString().ifBlank { "NoName" }
                viewModel.enterSession(id, name)
                it.cancel()
            }
        }.create()
        dialog.window?.let {
            it.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.dialog_shape, activity?.theme))
            it.attributes.verticalMargin = -0.08F
        }
        dialog.show()
    }

    private val onEnteredSessionResult = Observer<Long?> {
        if (it == null) {
            Toast.makeText(context, "Could not enter", Toast.LENGTH_SHORT).show()
            return@Observer
        }
        val direction = QuizListFragmentDirections.actionQuizListFragmentToHostFragment()
        findNavController().navigate(direction)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.quizzes.observe(viewLifecycleOwner, onQuizzesLoadedObserver)
        viewModel.sessionId.observe(viewLifecycleOwner, onRegisterResult)
        viewModel.userId.observe(viewLifecycleOwner, onEnteredSessionResult)
    }
}