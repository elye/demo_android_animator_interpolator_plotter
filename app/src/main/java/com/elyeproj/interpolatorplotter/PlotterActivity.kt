package com.elyeproj.interpolatorplotter

import android.animation.TimeAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.CycleInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.elyeproj.interpolatorplotter.PlotterView.Companion.DURATION
import com.elyeproj.interpolatorplotter.PlotterView.Companion.MAX
import kotlinx.android.synthetic.main.activity_plotter.*

class PlotterActivity : AppCompatActivity() {

    private val buttonHeight by lazy {
        button_fly.height/2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plotter)
        overridePendingTransition(0, 0)

        val valueAnimator = ValueAnimator.ofFloat(0f, MAX)
        valueAnimator.interpolator = AnticipateOvershootInterpolator()
        valueAnimator.duration = DURATION.toLong()
        view_plotter.setHeightRange(-0.2f, 1.2f)
        valueAnimator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            val y = view_plotter.drawPath(animation.currentPlayTime.toFloat(), progress)
            y?.let {
                button_fly.y = it - buttonHeight
            }
        }
        valueAnimator.start()
    }
}
