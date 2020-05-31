package com.elyeproj.interpolatorplotter

import android.view.animation.Interpolator
import kotlin.math.pow
import kotlin.math.sin


class SpringInterpolator(private val factor: Float = 0.3f) : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return (2.0.pow((-10 * input).toDouble()) * sin(2 * Math.PI * (input - factor / 4) / factor) + 1).toFloat()
    }
}
