package com.albatros.kquiz.ui.fragments.pedestal

import android.animation.*
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.albatros.kquiz.R
import com.albatros.kquiz.databinding.PedestalFragmentBinding
import com.albatros.kquiz.model.data.info.ClientInfo
import com.albatros.kquiz.ui.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PedestalFragment : Fragment(), MainActivity.IOnBackPressed {

    private val viewModel: PedestalViewModel by viewModel()
    private lateinit var binding: PedestalFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PedestalFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val onPassiveStateEndedObserver = Observer<Boolean> { value ->
        if (value) {
            endGame()
        }
    }

    private fun increaseViewSize(view: View, increaseValue: Int) {
        ValueAnimator.ofInt(view.measuredHeight, view.measuredHeight + increaseValue).apply {
            duration = 1000
            interpolator = BounceInterpolator()
            addUpdateListener {
                val animatedValue = it.animatedValue as Int
                val layoutParams = view.layoutParams
                layoutParams.height = animatedValue
                view.layoutParams = layoutParams
            }
        }.start()
    }

    private val onInfoLoaded = Observer<List<ClientInfo>> {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            with(binding) {
                val mList = listOf("\uD83E\uDD47", "\uD83E\uDD48", "\uD83E\uDD49")
                val pList = listOf(first, second, third)
                    .zip(listOf(firstTxt, secondTxt, thirdTxt))
                    .zip(listOf(1200, 800, 400))

                it.take(3).forEachIndexed { index, clientInfo ->
                    delay(1000)

                    repeat(8) {
                        if (index == 0) {

                            /* Idk why */
                            /* I just felt like it */
                            val newStar = AppCompatImageView(requireContext())
                            newStar.setImageResource(R.drawable.star)
                            newStar.layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT
                            )
                            root.addView(newStar)

                            var starW = newStar.width.toFloat()
                            var starH = newStar.height.toFloat()

                            newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
                            newStar.scaleY = newStar.scaleX
                            starW *= newStar.scaleX
                            starH *= newStar.scaleY

                            newStar.translationX = Math.random().toFloat() *
                                    root.width - starW / 2
                            val mover = ObjectAnimator.ofFloat(
                                newStar, View.TRANSLATION_Y,
                                1000f, root.height + starH
                            )

                            mover.interpolator = AccelerateInterpolator(1f)
                            val rotator = ObjectAnimator.ofFloat(
                                newStar, View.ROTATION,
                                (Math.random() * 1080).toFloat()
                            )
                            rotator.interpolator = LinearInterpolator()

                            val set = AnimatorSet()
                            set.playTogether(mover, rotator)
                            set.duration = (Math.random() * 1500 + 1000).toLong()

                            val listener = object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    root.removeView(newStar)
                                }
                            }

                            set.addListener(listener)
                            set.start()
                        }
                    }

                    pList[index].let { (views, size) ->
                        val (card, txt) = views
                        increaseViewSize(card, size)
                        val hint = clientInfo.name + "\n" + clientInfo.score + " " + mList[index]
                        txt.text = hint
                    }
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        endGame()
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)

        (activity as MainActivity).setDefaultColor()

        startPostponedEnterTransition()

        viewModel.onPassiveStateEnded.observe(viewLifecycleOwner, onPassiveStateEndedObserver)
        viewModel.info.observe(viewLifecycleOwner, onInfoLoaded)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.empty_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun endGame() {
        viewModel.stopSession()
        val direction = PedestalFragmentDirections.actionPedestalFragmentToEnterFragment()
        findNavController().navigate(direction)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            endGame()
            true
        }
        else -> false
    }
}