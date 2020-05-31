package com.elyeproj.interpolatorplotter

import android.animation.TimeAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.CycleInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.elyeproj.interpolatorplotter.PlotterView.Companion.DURATION
import com.elyeproj.interpolatorplotter.PlotterView.Companion.MAX
import kotlinx.android.synthetic.main.activity_plotter.*

class PlotterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plotter)

        val valueAnimator = TimeAnimator.ofFloat(0f, MAX)
        valueAnimator.interpolator = CycleInterpolator(2f)
        valueAnimator.duration = DURATION.toLong()
        view_plotter.setHeightRange(-1f, 1f)
        valueAnimator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            val y =view_plotter.drawPath(animation.currentPlayTime.toFloat(), progress)
            y?.let {
                button_fly.y = it
            }
        }
        valueAnimator.start()
    }
}
