package com.elyeproj.interpolatorplotter

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class PlotterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyle, defStyleRes) {

    companion object {
        const val MAX = 300f
        const val DURATION =5000f
    }

    private var minHeight = 0f
    private var maxHeight = 1f
    private val marginLeft: Float = resources.dpToPx(40f)
    private val marginVertical: Float = resources.dpToPx(10f)
    private val textVerticalAlign: Float = resources.dpToPx(7f)
    private val initial = Pair(marginLeft, marginVertical)
    private val mutablePath by lazy {
        Path().apply {
            moveTo(getXPos(0f), getYPos(0f))
        }
    }

    private fun getXPos(posX: Float): Float {
        return initial.first + ((posX / DURATION) * (width - 2 * initial.first))
    }

    private fun getYPos(posY: Float): Float {
        return height - initial.second - (((posY / MAX - minHeight) / (maxHeight - minHeight))
                * (height - 2 * initial.second))
    }

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = resources.dpToPx(2f)
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = resources.dpToPx(2f)
        style = Paint.Style.FILL
        textSize = resources.dpToPx(14f)
        textAlign = Paint.Align.RIGHT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (height == 0 || width == 0) return
        canvas.drawRect(marginLeft, marginVertical, width - marginLeft, height - marginVertical, paint)
        canvas.drawPath(mutablePath, paint)
        drawText(canvas)

    }

    private fun drawText(canvas: Canvas) {
        if (minHeight <= 0 && maxHeight >= 0) {
            drawYAxisText(canvas, 0f)
        }
        if (minHeight < 0) {
            drawYAxisText(canvas, minHeight)
            var current = -1f
            while (current > minHeight) {
                drawYAxisText(canvas, current--)
            }
        }
        if (maxHeight > 0 ) {
            drawYAxisText(canvas, maxHeight)
            var current = 1f
            while (current < maxHeight) {
                drawYAxisText(canvas, current++)
            }
        }
    }

    private fun drawYAxisText(canvas: Canvas, yPos: Float) {
        canvas.drawText("$yPos",
            getXPos(-DURATION / 50f),
            getYPos(yPos * MAX) + textVerticalAlign, textPaint)
    }

    fun drawPath(first: Float, second: Float): Float? {
        if (height == 0 || width == 0) return null
        val x = getXPos(if (first > DURATION) DURATION else first)
        val y = getYPos(second)
        mutablePath.lineTo(x, y)
        invalidate()
        return y
    }

    private inline fun <reified T> Resources.dpToPx(value: Float): T {
        val result = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value, displayMetrics
        )

        return when (T::class) {
            Float::class -> result as T
            Int::class -> result.toInt() as T
            else -> throw IllegalStateException("Type not supported")
        }
    }

    fun setHeightRange(min: Float, max: Float) {
        minHeight = min
        maxHeight = max
    }
}
