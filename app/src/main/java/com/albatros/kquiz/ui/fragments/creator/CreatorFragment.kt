package com.albatros.kquiz.ui.fragments.creator

import android.app.Activity
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.CreatorFragmentBinding
import com.albatros.kquiz.databinding.TopicInputDialogBinding
import com.albatros.kquiz.model.data.pojo.Question
import com.albatros.kquiz.ui.activity.MainActivity
import com.albatros.kquiz.ui.adapter.question.QuestionAdapter
import com.albatros.kquiz.ui.adapter.question.QuestionAdapterListener
import com.albatros.kquiz.ui.adapter.topic.TopicAdapter
import com.albatros.kquiz.ui.adapter.topic.TopicAdapterListener
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatorFragment : Fragment(), MainActivity.IOnBackPressed {

    private val viewModel: CreatorViewModel by viewModel()
    private lateinit var binding: CreatorFragmentBinding

    override fun onBackPressed(): Boolean {
        MainActivity.set_state = true
        (activity as? MainActivity)?.onUserInteraction()
        val direction = CreatorFragmentDirections.actionCreatorFragmentToEnterFragment()
        findNavController().navigate(direction)
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreatorFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val onTopicChangedObserver = Observer<List<String>> {
        (binding.list.adapter as TopicAdapter).setSource(it)
    }

    private val onQuestionChangedObserver = Observer<List<Question>> {
        (binding.qList.adapter as QuestionAdapter).setSource(it)
    }

    private val onQuestionValidStateChangedObserver = Observer<Boolean> {
        with(binding) {
            if (it) {
                listOf(answer1, answer2, answer3, answer4, answerEdit, descriptionEdit).forEach {
                    it.setText("")
                    it.clearFocus()
                }
                hideKeyboard()
                return@Observer
            }

            Snackbar.make(root, "Необходимо ввести все данные", BaseTransientBottomBar.LENGTH_SHORT).show()

        }
    }

    private val onValidStateChangedObserver = Observer<Boolean> {
        with(binding) {
            titleField.helperText = ""
            hideKeyboard()

            if (it) {
                nameEdit.clearFocus()
                MainActivity.set_state = true
                (activity as? MainActivity)?.onUserInteraction()
                val direction = CreatorFragmentDirections.actionCreatorFragmentToEnterFragment()
                findNavController().navigate(direction)
                return@Observer
            }

            if (nameEdit.text.isNullOrEmpty() || nameEdit.text!!.isBlank()) {
                titleField.helperText = "Необходимо ввести название"
                return@Observer
            }

            if (!viewModel.areTopicsSelected()) {
                Snackbar.make(root, "Необходимо ввести тему", BaseTransientBottomBar.LENGTH_SHORT).show()
                return@Observer
            }

            Snackbar.make(root, "Необходимо ввести вопросы", BaseTransientBottomBar.LENGTH_SHORT).show()
        }
    }

    private val touchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            rcv: RecyclerView,
            vh: RecyclerView.ViewHolder,
            trg: RecyclerView.ViewHolder
        ): Boolean = true

        override fun getSwipeDirs (recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            if (viewHolder.adapterPosition == 0) return 0
            return super.getSwipeDirs(recyclerView, viewHolder)
        }

        override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
            if (dir == ItemTouchHelper.LEFT || dir == ItemTouchHelper.RIGHT) {
                viewModel.deleteTopic(vh.adapterPosition)
            }
        }
    }

    private fun hideKeyboard() {
        val manager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.creator_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            MainActivity.set_state = true
            (activity as? MainActivity)?.onUserInteraction()
            val direction = CreatorFragmentDirections.actionCreatorFragmentToEnterFragment()
            findNavController().navigate(direction)
            true
        }
        R.id.action_create -> {
            viewModel.processQuiz(binding.nameEdit.text.toString())
            true
        } else -> false
    }

    private fun showClientInputDialog() {
        val dialogBinding = TopicInputDialogBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(binding.root.context).apply {
            setView(dialogBinding.root)
            setPositiveButton("Готово") { it, _ ->
                val topic = dialogBinding.nameEdit.text.toString()
                viewModel.addTopic(topic)
                it.cancel()
                hideKeyboard()
            }
        }.create()
        dialog.window?.let {
            it.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.dialog_shape, activity?.theme))
            it.attributes.verticalMargin = -0.08F
        }
        dialog.show()
    }

    @Suppress("deprecation")
    private fun showSystemBars() {
        val flag = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
        activity?.window?.decorView?.systemUiVisibility = flag
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        showSystemBars()
        MainActivity.set_state = false
        viewModel.enable()

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.topics.observe(viewLifecycleOwner, onTopicChangedObserver)
        viewModel.inputValid.observe(viewLifecycleOwner, onValidStateChangedObserver)
        viewModel.questions.observe(viewLifecycleOwner, onQuestionChangedObserver)
        viewModel.qInputValid.observe(viewLifecycleOwner, onQuestionValidStateChangedObserver)

        val topicAdapterListener = object : TopicAdapterListener {
            override fun onLastCardSelected(view: CardView) {
                showClientInputDialog()
            }
        }

        binding.addQuestion.setOnClickListener {
            with(binding) {
                viewModel.addQuestion(
                    descriptionEdit.text.toString(),
                    listOf(answer1, answer2, answer3, answer4).map { it.text.toString() },
                    answerEdit.text.toString()
                )
            }
        }

        binding.qList.adapter = QuestionAdapter(mutableListOf(), object: QuestionAdapterListener {
            override fun onDelete(question: Question, cardView: CardView) {
            }
        })

        binding.qList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.list.adapter = TopicAdapter(mutableListOf(), topicAdapterListener)
        ItemTouchHelper(touchCallback).attachToRecyclerView(binding.list)
        binding.list.layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
    }
}