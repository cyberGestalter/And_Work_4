package com.example.and_work_4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import java.util.*

class AnalogWatchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val ARROW_PART_COUNT = 3
    private val ARROW_RATE = 2f / ARROW_PART_COUNT
    private val CLOCK_MARK_X = 0f
    private val DEGREES_COUNT = 360f
    private val TIME_PART_COUNT = 60
    private val HOUR_STEP = 30f
    private val SECOND_STEP = 6f
    private val paint = Paint()

    private var width = 0f
    private var height = 0f
    private var radius = 0f
    private var clockMarkLength = 0f
    private var hourArrowLength = 0f
    private var minuteArrowLength = 0f
    private var secondArrowLength = 0f
    private var calendar: Calendar? = null

    var clockColor = Color.BLACK
    var secondArrowColor = Color.RED
    var minuteArrowColor = Color.BLUE
    var hourArrowColor = Color.BLACK
    var clockStroke = 10f
    var hourStroke = 10f
    var minuteStroke = hourStroke * ARROW_RATE
    var secondStroke = minuteStroke * ARROW_RATE

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.AnalogWatchView, 0, 0)
            .apply {
                try {
                    secondArrowColor =
                        getColor(R.styleable.AnalogWatchView_secondArrowColor, secondArrowColor)
                    minuteArrowColor =
                        getColor(R.styleable.AnalogWatchView_minuteArrowColor, minuteArrowColor)
                    hourArrowColor =
                        getColor(R.styleable.AnalogWatchView_hourArrowColor, hourArrowColor)
                    clockColor = getColor(R.styleable.AnalogWatchView_clockColor, clockColor)

                    hourStroke =
                        getDimension(R.styleable.AnalogWatchView_hourArrowStroke, hourStroke)
                    minuteStroke =
                        getDimension(R.styleable.AnalogWatchView_minuteArrowStroke, minuteStroke)
                    secondStroke =
                        getDimension(R.styleable.AnalogWatchView_secondArrowStroke, secondStroke)
                    clockStroke = getDimension(R.styleable.AnalogWatchView_clockStroke, clockStroke)
                } finally {
                    recycle()
                }
            }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val minValue = (MeasureSpec.getSize(widthMeasureSpec))
            .coerceAtMost(MeasureSpec.getSize(heightMeasureSpec))

        width = minValue.toFloat()
        height = minValue.toFloat()

        radius = minValue / 2.2f
        clockMarkLength = radius / 10
        hourArrowLength = 2 * radius * ARROW_RATE
        minuteArrowLength = hourArrowLength * ARROW_RATE
        secondArrowLength = minuteArrowLength * ARROW_RATE

        setMeasuredDimension(minValue, minValue)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeWidth = clockStroke
        paint.color = clockColor

        val centerX = width / 2
        val centerY = height / 2
        canvas?.drawCircle(centerX, centerY, radius, paint)

        paint.style = Paint.Style.FILL
        canvas?.translate(centerX, centerY)
        val clockMarkY = radius
        for (i in 1..12) {
            canvas?.drawLine(
                CLOCK_MARK_X,
                clockMarkY,
                CLOCK_MARK_X,
                clockMarkY - clockMarkLength,
                paint
            )
            canvas?.rotate(HOUR_STEP)
        }

        calendar = Calendar.getInstance()
        val currentHour = calendar?.get(Calendar.HOUR)
        val currentMinute = calendar?.get(Calendar.MINUTE)
        val currentSecond = calendar?.get(Calendar.SECOND)

        if (currentHour != null && currentMinute != null && currentSecond != null) {
            val hourAngle = currentHour * HOUR_STEP + HOUR_STEP / TIME_PART_COUNT * currentMinute
            drawArrow(canvas, hourArrowColor, hourStroke, hourArrowLength, hourAngle)

            val minuteAngle =
                currentMinute * SECOND_STEP + SECOND_STEP / TIME_PART_COUNT * currentSecond
            drawArrow(canvas, minuteArrowColor, minuteStroke, minuteArrowLength, minuteAngle)

            val secondAngle = currentSecond * SECOND_STEP
            drawArrow(canvas, secondArrowColor, secondStroke, secondArrowLength, secondAngle)
        }

        invalidate()
    }

    private fun drawArrow(canvas: Canvas?, color: Int, width: Float, length: Float, angle: Float) {
        paint.color = color
        paint.strokeWidth = width
        drawCurrentTime(canvas, length, angle)
    }

    private fun drawCurrentTime(canvas: Canvas?, arrowLength: Float, angle: Float) {
        canvas?.rotate(angle)
        canvas?.drawLine(
            CLOCK_MARK_X,
            arrowLength / ARROW_PART_COUNT, CLOCK_MARK_X,
            arrowLength / ARROW_PART_COUNT - arrowLength,
            paint
        )
        canvas?.rotate(DEGREES_COUNT - angle)
    }
}