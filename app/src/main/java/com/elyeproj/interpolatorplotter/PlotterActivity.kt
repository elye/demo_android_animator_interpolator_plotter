package com.elyeproj.interpolatorplotter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Path
import android.os.Bundle
import android.view.animation.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.elyeproj.interpolatorplotter.PlotterView.Companion.DURATION
import com.elyeproj.interpolatorplotter.PlotterView.Companion.MAX
import kotlinx.android.synthetic.main.activity_plotter.*


class PlotterActivity : AppCompatActivity() {

    private val buttonHeight by lazy {
        button_fly.height / 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plotter)

        ArrayAdapter.createFromResource(
            this,
            R.array.interpolator_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            interpolator_spinner.adapter = adapter
        }

        btn_plotter.setOnClickListener {
            startPlotting()
        }
    }

    private fun startPlotting() {
        val (selectedInterpolator, range) = getInterpolator()

        view_plotter.setHeightRange(range.first, range.second)
        ValueAnimator.ofFloat(0f, MAX).apply {
            interpolator = selectedInterpolator
            duration = DURATION.toLong()
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                val buttonY = view_plotter.drawPath(animation.currentPlayTime.toFloat(), progress)
                buttonY?.let {
                    button_fly.y = it - buttonHeight
                }
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    btn_plotter.isEnabled = false
                }
                override fun onAnimationEnd(animation: Animator) {
                    btn_plotter.isEnabled = true
                }
            })
        }.start()
    }

    private fun getInterpolator(): Pair<Interpolator, Pair<Float, Float>> {
        return when (interpolator_spinner.selectedItem) {
            "LinearInterpolator" -> Pair(LinearInterpolator(), Pair(0f, 1f))
            "LinearOutSlowInInterpolator" -> Pair(LinearOutSlowInInterpolator(), Pair(0f, 1f))
            "AccelerateInterpolator" -> Pair(AccelerateInterpolator(), Pair(0f, 1f))
            "DecelerateInterpolator" -> Pair(DecelerateInterpolator(), Pair(0f, 1f))
            "AccelerateDecelerateInterpolator" -> Pair(AccelerateDecelerateInterpolator(), Pair(0f, 1f))
            "OvershootInterpolator" -> Pair(OvershootInterpolator(), Pair(0f, 1.2f))
            "AnticipateInterpolator" -> Pair(AnticipateInterpolator(), Pair(-0.2f, 1f))
            "AnticipateOvershootInterpolator" -> Pair(AnticipateOvershootInterpolator(), Pair(-0.2f, 1.2f))
            "CycleInterpolator" -> Pair(CycleInterpolator(2f), Pair(-1f, 1f))
            "BounceInterpolator" -> Pair(BounceInterpolator(), Pair(0f, 1f))
            "PathInterpolator" -> {
                val path = Path()
                path.lineTo(0.1f, 0.25f)
                path.lineTo(0.2f, 0.25f)
                path.lineTo(0.3f, 0.75f)
                path.lineTo(0.4f, 0.75f)
                path.lineTo(0.5f, 0.5f)
                path.lineTo(0.6f, 0.5f)
                path.lineTo(0.7f, 0.2f)
                path.lineTo(0.8f, 0.3f)
                path.lineTo(0.9f, 0.2f)
                path.lineTo(1f, 1f)
                Pair(PathInterpolator(path), Pair(0f, 1f))
            }
            "HesitateInterpolator" -> Pair(HesitateInterpolator(), Pair(0f, 1f))
            "SpringInterpolator" -> Pair(SpringInterpolator(), Pair(0f, 1.4f))
            else -> Pair(LinearInterpolator(), Pair(0f, 1f))
        }
    }
}
