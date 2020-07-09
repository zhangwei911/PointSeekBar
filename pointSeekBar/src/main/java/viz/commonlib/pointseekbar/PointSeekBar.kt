package viz.commonlib.pointseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @title: PointSeekBar
 * @projectName PointSeekBar
 * @description:
 * @author zhangwei
 * @date 2020/7/8 14:01
 */
class PointSeekBar : View {
    private val paint = Paint()

    /**
     * 当前进度
     */
    var progress: Int = 0
        set(value) {
            if (value > max) {
                throw Exception("progress must be less than or equal to max")
            } else if (value < 0) {
                throw Exception("progress must be greater than 0")
            }
            field = value
            progressChangedListener?.invoke(value)
            invalidate()
        }

    /**
     * 最大进度
     */
    var max = 100
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 进度条背景颜色
     */
    var defaultColor = Color.GRAY
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 进度条颜色
     */
    var progressColor = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 当前位置圆形颜色
     */
    var seekBarColor = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 当前位置圆形半径
     */
    var seekBarRadius = 25
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 打点圆形半径
     */
    var pointRadius = 10
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 进度条高度
     */
    var progressHeight = 10
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 当前位置圆形圆心x坐标
     */
    private var seekBarCenterX = 0f

    /**
     * 当前位置圆形圆心y坐标
     */
    private var seekBarCenterY = 0f

    /**
     * 是否正在拖动
     */
    private var isSeekBarDragging = false

    /**
     * 进度条左偏移
     */
    private var progressOffsetLeft = 0

    /**
     * 进度条右偏移
     */
    private var progressOffsetRight = 0

    /**
     * 进度条上偏移
     */
    private var progressOffsetTop = 0

    /**
     * 进度条下偏移
     */
    private var progressOffsetBottom = 0

    /**
     * 点击打点后是否改变当前位置当打点处
     */
    private var isJumpWhenPointClick = true

    /**
     * 当前位置大于打点位置时是否改变打点圆形颜色为进度条颜色
     * 此时 pointRadius 必须大于 progressHeight/2 , 否则看不到打点
     */
    private var isChangePointColor = true

    /**
     * 打点列表
     */

    var pointList = mutableListOf<PointInfo>()
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 进度变化事件
     */
    var progressChangedListener: ((progress: Int) -> Unit)? = null

    /**
     * 打点点击事件
     */
    var pointClickListener: ((index: Int, pointInfo: PointInfo) -> Unit)? = null

    /**
     * 开始拖拽事件
     */
    var dragStartListener: ((progress: Int) -> Unit)? = null

    /**
     * 正在拖拽事件
     */
    var draggingListener: ((progress: Int) -> Unit)? = null

    /**
     * 拖拽结束事件
     */
    var draggedListener: ((progress: Int) -> Unit)? = null

    /**
     * 当前位置圆形点击范围与半径的比例
     */
    var seekBarClickRangeRatio = 1.5

    /**
     * 打点位置圆形点击范围与半径的比例
     */
    var pointClickRangeRatio = 3

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            max(progressHeight, seekBarRadius * 2) + paddingBottom + paddingTop
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = defaultColor
        paint.style = Paint.Style.FILL
        progressOffsetTop = seekBarRadius - progressHeight / 2 + paddingTop
        drawBackground(canvas, progressOffsetTop, progressOffsetRight)
        val progressLen =
            (width.toFloat() - paddingRight - paddingLeft) * progress / max - progressOffsetRight
        drawProgress(canvas, progressOffsetTop, progressLen)
        drawPoint(canvas)
        drawSeekBar(progressLen, progressOffsetLeft, progressOffsetTop, canvas)
    }

    /**
     * 绘制打点圆形
     */
    private fun drawPoint(canvas: Canvas?) {
        pointList.forEach {
            paint.color = if (isChangePointColor && it.progress <= progress) {
                progressColor
            } else {
                it.color
            }
            val pointProgressLen =
                (width.toFloat() - paddingRight - paddingLeft) * it.progress / max - progressOffsetRight
            val pointCenterX = paddingLeft + pointProgressLen + progressOffsetLeft
            val pointCenterY = progressOffsetTop.toFloat() + progressHeight / 2
            canvas?.drawCircle(
                pointCenterX,
                pointCenterY,
                pointRadius.toFloat(),
                paint
            )
        }
    }

    /**
     * 绘制进度条背景
     */
    private fun drawBackground(
        canvas: Canvas?,
        progressOffsetTop: Int,
        progressOffsetRight: Int
    ) {
        canvas?.drawRect(
            0f + paddingLeft,
            progressOffsetTop.toFloat(),
            width.toFloat() - paddingRight - progressOffsetRight,
            progressOffsetTop.toFloat() + progressHeight,
            paint
        )
    }

    /**
     * 绘制进度条
     */
    private fun drawProgress(
        canvas: Canvas?,
        progressOffsetTop: Int,
        progressLen: Float
    ) {
        paint.color = progressColor
        canvas?.drawRect(
            0f + paddingLeft,
            progressOffsetTop.toFloat(),
            progressLen + paddingLeft,
            progressOffsetTop.toFloat() + progressHeight,
            paint
        )
    }

    /**
     * 绘制当前位置圆形
     */
    private fun drawSeekBar(
        progressLen: Float,
        progressOffsetLeft: Int,
        progressOffsetTop: Int,
        canvas: Canvas?
    ) {
        paint.color = seekBarColor
        seekBarCenterX = paddingLeft + progressLen + progressOffsetLeft
        seekBarCenterY = progressOffsetTop.toFloat() + progressHeight / 2
        if (seekBarCenterX < seekBarRadius) {
            seekBarCenterX = seekBarRadius.toFloat()
        } else if (seekBarCenterX > width.toFloat() - paddingRight - seekBarRadius) {
            seekBarCenterX = width.toFloat() - paddingRight - seekBarRadius
        }
        canvas?.drawCircle(
            seekBarCenterX,
            seekBarCenterY,
            seekBarRadius.toFloat(),
            paint
        )
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    /**
     * 判断是否在当前位置圆形内
     */
    private fun isInCircle(event: MotionEvent): Boolean {
        val distance = calcDistance(event.x, event.y, seekBarCenterX, seekBarCenterY)
        return distance <= seekBarRadius * seekBarClickRangeRatio
    }

    /**
     * 判断是否在打点圆形内
     */
    private fun isInPointCircle(event: MotionEvent): Boolean {
        pointList.forEachIndexed { index, pointInfo ->
            val pointProgressLen =
                (width.toFloat() - paddingRight - paddingLeft) * pointInfo.progress / max - progressOffsetRight
            val pointCenterX = paddingLeft + pointProgressLen + progressOffsetLeft
            val pointCenterY = progressOffsetTop.toFloat() + progressHeight / 2
            val distance = calcDistance(event.x, event.y, pointCenterX, pointCenterY)
            if (distance <= pointRadius * pointClickRangeRatio) {
                pointClickListener?.invoke(index, pointInfo)
                if (isJumpWhenPointClick) {
                    progress = pointInfo.progress
                }
                return true
            }
        }
        return false
    }

    private fun calcDistance(x1: Float, y1: Float, x2: Float, y2: Float): Double {
        return sqrt(
            (x1 - x2).toDouble().pow(2.0)
                    + (y1 - y2).toDouble().pow(2.0)
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    performClick()
                    parent.requestDisallowInterceptTouchEvent(true)
                    isSeekBarDragging = isInCircle(this)
                    if (isSeekBarDragging) {
                        dragStartListener?.invoke(progress)
                    }
                    if (!isSeekBarDragging && !isInPointCircle(event)) {
                        calcProgress(x)
                        invalidate()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isSeekBarDragging) {
                        calcProgress(x)
                        draggingListener?.invoke(progress)
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (isSeekBarDragging) {
                        draggedListener?.invoke(progress)
                    }
                }
                else -> {
                }
            }
        }
        return isSeekBarDragging || super.onTouchEvent(event)
    }

    /**
     * 计算当前进度
     */
    private fun calcProgress(x: Float) {
        val progressTmp = String.format(
            "%.0f",
            ((x - paddingLeft) / (width.toFloat() - paddingRight - paddingLeft) * max)
        ).toInt()
        progress = when {
            progressTmp > max -> {
                max
            }
            progressTmp < 0 -> {
                0
            }
            else -> {
                progressTmp
            }
        }
    }
}