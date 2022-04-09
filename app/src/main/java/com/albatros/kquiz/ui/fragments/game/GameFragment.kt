package com.albatros.kquiz.ui.fragments.game

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.GameFragmentBinding
import com.albatros.kquiz.ui.activity.MainActivity
import com.albatros.kquiz.ui.adapter.answer.AnswerAdapter
import com.albatros.kquiz.ui.adapter.answer.AnswerAdapterListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class GameFragment : Fragment(), MainActivity.IOnBackPressed {

    private lateinit var binding: GameFragmentBinding
    private val args by navArgs<GameFragmentArgs>()
    private val viewModel: GameViewModel by viewModel { parametersOf(args.question) }
    private var answered = false
    private var currentAnswer = ""

    override fun onBackPressed(): Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.custom_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> true
        else -> false
    }

    private val onActionEndObserver = Observer<Boolean> {
        if (currentAnswer == "")
            viewModel.submitData(currentAnswer)
        lifecycleScope.launch(Dispatchers.Main) {
            delay(6_000)
            val direction = GameFragmentDirections.actionGameFragmentToResultFragment(args.question)
            findNavController().navigate(direction)
        }
    }


    private val onProgressChangedObserver = Observer<Int> {
        binding.progressBar.setProgress(it, false)
        if (it < 50) {
            binding.progressBar.progressTintList = ColorStateList.valueOf(Color.RED)
        }
    }

    private val listener = object : AnswerAdapterListener {
        override fun onAnswerSelected(answer: String, cardView: CardView) {
            if (answered) return
            currentAnswer = answer
            viewModel.submitData(currentAnswer)
            val color = if (answer == args.question.answer)
                context?.resources?.getColor(R.color.neon_green, context?.theme)
            else context?.resources?.getColor(R.color.red, context?.theme)
            val old = cardView.cardBackgroundColor.defaultColor
            ValueAnimator.ofObject(ArgbEvaluator(), old, color).apply {
                duration = 400
                addUpdateListener { anim -> color?.let { cardView.setCardBackgroundColor(anim.animatedValue as Int) } }
            }.start()
            answered = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)

        val gradIds = listOf(
            R.drawable.grad_1,
            R.drawable.grad_2,
            R.drawable.grad_3,
            R.drawable.grad_4
        )

        val gradient = gradIds.random()

        (activity as MainActivity).setBackground(gradient)

        startPostponedEnterTransition()

        with(binding) {
            questionDescr.text = args.question.description
            progressBar.progressTintList = ColorStateList.valueOf(Color.WHITE)

            list.adapter = AnswerAdapter(args.question.variants.toMutableList(), listener)
            list.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.onActionEnd.observe(viewLifecycleOwner, onActionEndObserver)
        viewModel.counter.observe(viewLifecycleOwner, onProgressChangedObserver)
    }
}