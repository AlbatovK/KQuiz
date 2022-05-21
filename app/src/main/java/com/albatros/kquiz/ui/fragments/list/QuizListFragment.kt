package com.albatros.kquiz.ui.fragments.list

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.NameDialogBinding
import com.albatros.kquiz.databinding.QuizListFragmentBinding
import com.albatros.kquiz.model.data.pojo.Quiz
import com.albatros.kquiz.ui.activity.MainActivity
import com.albatros.kquiz.ui.adapter.quiz.QuizAdapter
import com.albatros.kquiz.ui.adapter.quiz.QuizAdapterListener
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
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
                Snackbar.make(binding.list, "Could not load quiz list. Try again.", BaseTransientBottomBar.LENGTH_SHORT).show()
                val direction = QuizListFragmentDirections.actionQuizListFragmentToEnterFragment()
                findNavController().navigate(direction)
            }
            return@Observer
        }
        binding.list.adapter = QuizAdapter(it.toMutableList(), listener)
        binding.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private val onRegisterResult = Observer<Long?> {
        if (it == null) {
            Toast.makeText(context, "Could not register session. Try again.", Toast.LENGTH_SHORT).show()
            return@Observer
        }
        showNameInputDialog(it)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quiz_list_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        val listener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.loadQuizList()
                return true
            }
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true
        }

        menu.findItem(R.id.action_search).setOnActionExpandListener(listener)

        searchView.setOnCloseListener {
            viewModel.loadQuizList()
            true
        }

        val queryListener = object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.trim()?.lowercase()?.let { viewModel.fetchByTopics(query) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }

        searchView.setOnQueryTextListener(queryListener)
        val manager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val info = manager.getSearchableInfo(activity?.componentName)
        searchView.setSearchableInfo(info)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showNameInputDialog(id: Long) {
        val dialogBinding = NameDialogBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(binding.root.context).apply {
            setView(dialogBinding.root)
            setPositiveButton("Готово") { it, _ ->
                val name = dialogBinding.nameEdit.text.toString().ifBlank { "NoName" }
                viewModel.enterSession(id, name)
                it.cancel()
                (activity as? MainActivity)?.onUserInteraction()
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
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.quizzes.observe(viewLifecycleOwner, onQuizzesLoadedObserver)
        viewModel.sessionId.observe(viewLifecycleOwner, onRegisterResult)
        viewModel.userId.observe(viewLifecycleOwner, onEnteredSessionResult)
    }
}