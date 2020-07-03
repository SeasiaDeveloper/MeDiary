package com.mediary.activities.calenderview.gesture

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

 open class OnSwipeTouchListener(ctx: Context) : View.OnTouchListener {

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

     inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

         override fun onDown(e: MotionEvent): Boolean {
             return true
         }

         override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
             var result = false
             try {
                 val diffY = e2.getY() - e1.getY()
                 val diffX = e2.getX() - e1.getX()
                 if (Math.abs(diffX) > Math.abs(diffY)) {
                     if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                         if (diffX > 0) {
                             onSwipeRight()
                         } else {
                             onSwipeLeft()
                         }
                         result = true
                     }
                 } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                     if (diffY > 0) {
                         onSwipeBottom()
                     } else {
                         onSwipeTop()
                     }
                     result = true
                 }
             } catch (exception: Exception) {
                 exception.printStackTrace()
             }

             return result
         }

         private val SWIPE_THRESHOLD = 100
         private val SWIPE_VELOCITY_THRESHOLD = 100
     }

     fun onSwipeRight() {}

     fun onSwipeLeft() {}

     fun onSwipeTop() {}

     fun onSwipeBottom() {}
}