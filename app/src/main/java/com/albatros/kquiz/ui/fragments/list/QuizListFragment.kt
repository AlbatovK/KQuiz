package com.albatros.kquiz.ui.fragments.list

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kquiz.databinding.QuizListFragmentBinding
import com.albatros.kquiz.model.data.Quiz
import com.albatros.kquiz.ui.adapter.quiz.QuizAdapter
import com.albatros.kquiz.ui.adapter.quiz.QuizAdapterListener
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
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            val direction = QuizListFragmentDirections.actionQuizListFragmentToEnterFragment()
            findNavController().navigate(direction)
            return@Observer
        }
        binding.list.adapter = QuizAdapter(it.toMutableList(), listener)
        binding.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
    }

    private val onRegisterResult = Observer<Long?> {
        if (it == null) {
            Toast.makeText(context, "Could not register", Toast.LENGTH_SHORT).show()
            return@Observer
        }
        Toast.makeText(context, "session_id$it", Toast.LENGTH_LONG).show()
        /* TODO show dialog */
        viewModel.enterSession(it, "michael")
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