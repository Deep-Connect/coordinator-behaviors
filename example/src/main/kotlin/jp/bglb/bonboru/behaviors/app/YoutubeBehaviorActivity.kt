package jp.bglb.bonboru.behaviors.app

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import butterknife.bindView
import jp.bglb.bonboru.behaviors.YoutubeLikeBehavior

/**
 * Created by tetsuya on 2017/01/05.
 */
class YoutubeBehaviorActivity() : AppCompatActivity(), YoutubeLikeBehavior.OnBehaviorStateListener {
  val root by bindView<CoordinatorLayout>(R.id.root)
  val show by bindView<Button>(R.id.show)
  val reset by bindView<Button>(R.id.reset)

  var media: ImageView? = null
  var description: View? = null
  var wipe: Button? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_youtube)

    show.setOnClickListener {
      media = layoutInflater.inflate(R.layout.include_media, root, false) as ImageView
      description = layoutInflater.inflate(R.layout.include_description, root, false)
      wipe = description?.findViewById(R.id.wipe) as Button
      val behavior = YoutubeLikeBehavior.from(media)
      behavior?.listener = this

      root.addView(media)
      root.addView(description)

      wipe?.setOnClickListener {
        val behavior = YoutubeLikeBehavior.from(media)
        behavior?.updateState(YoutubeLikeBehavior.STATE_SHRINK)
      }
    }

    reset.setOnClickListener {
      val behavior = YoutubeLikeBehavior.from(media)
      behavior?.updateState(YoutubeLikeBehavior.STATE_EXPANDED)
    }
  }

  override fun onBehaviorStateChanged(newState: Long) {
    if (newState == YoutubeLikeBehavior.STATE_TO_RIGHT || newState == YoutubeLikeBehavior.STATE_TO_LEFT) {
      root.removeView(media)
      root.removeView(description)
    }
  }
}
