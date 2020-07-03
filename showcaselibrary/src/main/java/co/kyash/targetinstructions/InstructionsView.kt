package co.kyash.targetinstructions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import co.kyash.targetinstructions.targets.Target

class InstructionsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var currentTarget: Target? = null

    internal var overlayColor: Int = ContextCompat.getColor(context, R.color.default_cover)
    internal var listener: OnStateChangedListener? = null

    init {
        id = R.id.instructions_view
        bringToFront()
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        setOnClickListener({
            if (currentTarget != null && currentTarget!!.canClick()) {
                listener?.onTargetClicked()
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = overlayColor
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        if (currentTarget != null && canvas != null) {
            currentTarget!!.drawHighlight(canvas)
        }
    }

    internal fun showTarget(target: Target) {
        removeAllViews()
        addView(target.messageView)

        currentTarget = target
        currentTarget?.show()
    }

    internal fun hideTarget(target: Target) {
        target.hideImmediately()
        listener?.onTargetClosed()
    }

    interface OnStateChangedListener {
        fun onTargetClosed()

        fun onTargetClicked()
    }

}