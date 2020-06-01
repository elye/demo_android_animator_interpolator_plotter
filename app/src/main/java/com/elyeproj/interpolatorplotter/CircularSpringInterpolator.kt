package com.elyeproj.interpolatorplotter

import android.view.animation.Interpolator
import kotlin.math.pow
import kotlin.math.sin


class CircularSpringInterpolator(private val tension: Float = 50f) : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return (sin(tension * input) * sin(Math.PI * input)).toFloat()
    }
}
