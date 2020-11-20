package com.example.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


class CustomDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), LifecycleObserver {
    //获取基于主题的颜色作为画笔颜色
    private var paintColor = TypedValue().apply {
        context.theme.resolveAttribute(R.attr.colorPrimary, this, true)
    }.data

    private var sineWaveSamplesPath = Path()
    private var rotatingJob: Job? = null
    private var mAngle = 10f
    private var mRadius = 0f
    private var mWidth = 0f
    private var mHeight = 0f
    private val filledCirclePaint = Paint().apply {
        style = Paint.Style.FILL
        color = paintColor
    }
    private val solidLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = paintColor
    }
    private val vectorLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = paintColor
    }
    private val textPaint = Paint().apply {
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD
        color = paintColor
    }
    private val dashedLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        strokeWidth = 5f
        color = paintColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = if (w < h / 2) w / 2.toFloat() else h / 4.toFloat()
        mRadius -= 20f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawAxises(this)
            drawLabel(this)
            drawDashedCircle(this)
            drawVector(this)
            drawProjections(this)
            drawSineWave(this)
        }

    }

    private fun drawAxises(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
            drawLine(0f, -mHeight / 2, 0f, mHeight / 2, solidLinePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
        }
    }

    private fun drawLabel(canvas: Canvas) {
        canvas.apply {
            drawRect(100f, 100f, 600f, 250f, solidLinePaint)
            drawText("指数函数与旋转矢量", 120f, 195f, textPaint)
        }
    }

    private fun drawDashedCircle(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(0f, 0f, mRadius, dashedLinePaint)
        }
    }

    private fun drawVector(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            withRotation(-mAngle) {
                drawLine(0f, 0f, mRadius, 0f, vectorLinePaint)
            }
        }
    }

    private fun drawProjections(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawCircle(mRadius * cos(mAngle.toRadians()), 0f, 20f, filledCirclePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(mRadius * cos(mAngle.toRadians()), 0f, 20f, filledCirclePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            val x = mRadius * cos(mAngle.toRadians())
            val y = mRadius * sin(mAngle.toRadians())
            withTranslation(x, -y) {
                drawLine(0f, 0f, 0f, y, solidLinePaint)
                drawLine(0f, 0f, 0f, -mHeight / 4 + y, dashedLinePaint)
            }
        }
    }

    private fun drawSineWave(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            val samplesCount = 150
            val dy = mHeight / 2 / samplesCount
            sineWaveSamplesPath.reset()
            sineWaveSamplesPath.moveTo(mRadius * cos(mAngle.toRadians()), 0f)
            repeat(samplesCount) {
                val x = mRadius * cos(it * -0.15 + mAngle.toRadians())
                val y = -dy * it
                sineWaveSamplesPath.quadTo(x.toFloat(), y, x.toFloat(), y)
            }
            drawPath(sineWaveSamplesPath, vectorLinePaint)
            drawTextOnPath("hello world", sineWaveSamplesPath, 1000f, -20f, textPaint)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startRotating() {
        rotatingJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(100)
                mAngle += 5f
                invalidate()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseRotating() {
        rotatingJob?.cancel()
    }

    private fun Float.toRadians() = this / 180 * PI.toFloat()
}















