package com.erivera.apps.topcharts

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

class SingleViewTouchableCoordinatorLayout(context: Context, attributeSet: AttributeSet? = null) : CoordinatorLayout(context, attributeSet) {

    private val viewRect1 = Rect()
    private val viewRect2 = Rect()
    private val viewRect3 = Rect()
    private val viewRect4 = Rect()
    private val viewRect5 = Rect()

    var motionProgress = 0.0F

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        findViewById<View>(R.id.songGridView)?.getHitRect(viewRect1)
        findViewById<View>(R.id.centerIconLayout)?.getHitRect(viewRect2)
        findViewById<View>(R.id.prevButton)?.getHitRect(viewRect3)
        findViewById<View>(R.id.nextButton)?.getHitRect(viewRect4)
        findViewById<View>(R.id.arrowDown)?.getHitRect(viewRect5)
        val isValidSurface = viewRect1.contains(event.x.toInt(), event.y.toInt()) || viewRect2.contains(event.x.toInt(), event.y.toInt()) ||
                viewRect3.contains(event.x.toInt(), event.y.toInt()) || viewRect4.contains(event.x.toInt(), event.y.toInt()) ||
                viewRect5.contains(event.x.toInt(), event.y.toInt())

        val result = if(isValidSurface.not() && motionProgress == 0F) false else super.dispatchTouchEvent(event)
        Log.d("TestScroll", "CoordinatorLayout: result $result, motionProgress $motionProgress")
        return result
    }
}