package jp.bglb.bonboru.behaviors

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.CoordinatorLayout.Behavior
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/**
 * Created by Tetsuya Masuda on 2016/09/14.
 */
class FollowBehavior<V : View>(context: Context?, attrs: AttributeSet?) : Behavior<V>(
    context, attrs) {

  var initialized = false
  var collapsedY = 0
  var peekHeight = 300
  var anchorPointY = 600
  var currentChildY = 0
  var anchorTopMargin = 0

  init {
    attrs?.let {
      val googleMapLikeBehaviorParam = context?.obtainStyledAttributes(it, R.styleable.GoogleMapLikeBehaviorParam)
      peekHeight = googleMapLikeBehaviorParam?.getDimensionPixelSize(
          R.styleable.GoogleMapLikeBehaviorParam_peekHeight, 0)!!
      anchorTopMargin = googleMapLikeBehaviorParam?.getDimensionPixelSize(
          R.styleable.GoogleMapLikeBehaviorParam_anchorPoint, 0)!!
      googleMapLikeBehaviorParam?.recycle()
    }
  }

  override fun layoutDependsOn(parent: CoordinatorLayout?, child: V, dependency: View?): Boolean {
    val behavior = GoogleMapLikeBehavior.from(dependency)
    return behavior != null
  }

  override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
    parent.onLayoutChild(child, layoutDirection)
    ViewCompat.offsetTopAndBottom(child, 0)
    anchorPointY = parent.height - anchorTopMargin
    return true
  }

  override fun onDependentViewChanged(parent: CoordinatorLayout, child: V,
      dependency: View): Boolean {
    super.onDependentViewChanged(parent, child, dependency)
    if (!initialized) {
      init(child, dependency)
      return false
    }
    val rate = (parent.height - dependency.y - peekHeight) / (anchorPointY - peekHeight)
    currentChildY = ((parent.height + child.height) * (1f - rate)).toInt()
    if (currentChildY <= 0) {
      child.y = 0F
      currentChildY = 0
    } else {
      child.y = currentChildY.toFloat()
    }
    return true
  }

  private fun init(child: View, dependency: View) {
    collapsedY = dependency.height - 2 * peekHeight
    currentChildY = dependency.y.toInt()
    if (currentChildY == anchorPointY || currentChildY == anchorPointY - 1 || currentChildY == anchorPointY + 1) child.y = 0f
    else child.y = currentChildY.toFloat()
    initialized = true
  }

}
