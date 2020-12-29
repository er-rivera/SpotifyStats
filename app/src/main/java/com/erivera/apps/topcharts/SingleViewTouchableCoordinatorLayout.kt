package com.erivera.apps.topcharts

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

class SingleViewTouchableCoordinatorLayout(context: Context, attributeSet: AttributeSet? = null) : CoordinatorLayout(context, attributeSet) {

    private val gridTouchArea by lazy {
        findViewById<View>(R.id.songGridView)
    }

    private val centerIconLayout by lazy {
        findViewById<View>(R.id.centerIconLayout)
    }

    private val prevButton by lazy {
        findViewById<View>(R.id.prevButton)
    }

    private val nextButton by lazy {
        findViewById<View>(R.id.nextButton)
    }

    private val viewRect1 = Rect()
    private val viewRect2 = Rect()
    private val viewRect3 = Rect()
    private val viewRect4 = Rect()

    var motionProgress = 0.0F

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        gridTouchArea.getHitRect(viewRect1)
        centerIconLayout.getHitRect(viewRect2)
        prevButton.getHitRect(viewRect3)
        nextButton.getHitRect(viewRect4)
        val isValidSurface = viewRect1.contains(event.x.toInt(), event.y.toInt()) || viewRect2.contains(event.x.toInt(), event.y.toInt()) ||
                viewRect3.contains(event.x.toInt(), event.y.toInt()) || viewRect4.contains(event.x.toInt(), event.y.toInt())

        val result = if(isValidSurface.not() && motionProgress == 0F) false else super.dispatchTouchEvent(event)
        Log.d("TestScroll", "CoordinatorLayout: result $result, motionProgress $motionProgress")
        return result
    }
}