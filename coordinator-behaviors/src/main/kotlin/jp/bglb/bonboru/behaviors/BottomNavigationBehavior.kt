package jp.bglb.bonboru.behaviors

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.concurrent.TimeUnit

/**
 * Created by tetsuya on 2017/01/12.
 */
class BottomNavigationBehavior<V : View>(context: Context?,
    attrs: AttributeSet?) : CoordinatorLayout.Behavior<V>(context, attrs) {

  companion object {
    private val SCROLL_UP = 1
    private val SCROLL_DOWN = -1
  }

  private var parentHeight: Int = 0
  private var animating = false
  private var animatingDirection = 0

  override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
    parentHeight = parent.height
    parent.onLayoutChild(child, layoutDirection)
    ViewCompat.offsetTopAndBottom(child, parentHeight - child.height)
    return true
  }

  override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout?, child: V,
      directTargetChild: View?, target: View?, nestedScrollAxes: Int): Boolean {
    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
  }

  override fun onNestedScroll(coordinatorLayout: CoordinatorLayout?, child: V, target: View?,
      dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
    if (animating || dyConsumed == 0) return

    val direction = if (dyConsumed > 0) SCROLL_DOWN else SCROLL_UP
    if (animatingDirection == direction) return

    animatingDirection = direction
    if (direction == SCROLL_DOWN) {
      scrollOutAnimation(child)
    } else {
      scrollInAnimation(child)
    }
  }

  private fun scrollOutAnimation(targetView: View) {
    val objectAnimator = ObjectAnimator.ofFloat(targetView, "translationY", 0f,
        targetView.height.toFloat())
    objectAnimator.duration = 300
    objectAnimator.interpolator = AccelerateDecelerateInterpolator()
    objectAnimator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator?) {
        animating = true
      }

      override fun onAnimationCancel(animation: Animator?) {
        animating = true
      }

      override fun onAnimationEnd(animation: Animator?) {
        animating = false
      }
    })
    objectAnimator.start()
  }

  private fun scrollInAnimation(targetView: View) {
    val objectAnimator = ObjectAnimator.ofFloat(targetView, "translationY",
        targetView.height.toFloat(), 0f)
    objectAnimator.duration = 300
    objectAnimator.interpolator = AccelerateDecelerateInterpolator()
    objectAnimator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator?) {
        animating = true
      }

      override fun onAnimationCancel(animation: Animator?) {
        animating = true
      }

      override fun onAnimationEnd(animation: Animator?) {
        animating = false
      }
    })
    objectAnimator.start()
  }
}
