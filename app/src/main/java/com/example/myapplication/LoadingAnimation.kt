import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.myapplication.R


class LoadingAnimation constructor(private val context: Activity, private val animationName: String = "loading.json") {

    // 現有視圖中最底層的視圖
    private val rootView: ViewGroup = context.window.decorView.findViewById<ViewGroup>(android.R.id.content)

    // 畫面的layout與設定layout的參數
    private val rLayout: LinearLayout = LinearLayout(context)
    private val rLayoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
    )

    // loading動畫container
    private val loadingContainer: RelativeLayout = RelativeLayout(context)

    // loading動畫與參數
    private val lottieAnimationView: LottieAnimationView = LottieAnimationView(context)
    private val lLayoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    )

    // loading文字
    private val loadingTextTV: TextView = TextView(context)

    init {
        initLayout()
        initLoadingImage()
        initLoadingText()
        rLayout.addView(loadingTextTV)
        loadingContainer.addView(lottieAnimationView)
    }

    private fun initLayout() {
        rLayout.orientation = LinearLayout.VERTICAL

        // 設定背景為黑色，alpha值0.8
        rLayout.setBackgroundColor(Color.BLACK)
        rLayout.alpha = 0.8F
    }

    private fun initLoadingImage() {
        // 包住loading的container，主要是用來做定位
        val layoutParams = lLayoutParams
        layoutParams.setMargins(0, 150, 0, 0)
        loadingContainer.layoutParams = layoutParams
        rLayout.addView(loadingContainer)

        // loading動畫主體
        lottieAnimationView.setAnimation(animationName)
        lottieAnimationView.layoutParams = lLayoutParams
    }

    private fun initLoadingText() {
        // 加載自訂義字體
        val customTypeface = ResourcesCompat.getFont(context, R.font.yujiboku_regular)
        // 載入中文字
        loadingTextTV.text = "努力加載中..."
        loadingTextTV.setTextColor(ContextCompat.getColor(context, R.color.white))
        loadingTextTV.gravity = Gravity.CENTER
        loadingTextTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21F)

        // 設置自訂義字體
        customTypeface?.let {
            loadingTextTV.typeface = it
        }
    }

    fun start() {
        rootView.addView(rLayout)
        lottieAnimationView.repeatCount = LottieDrawable.INFINITE
        lottieAnimationView.playAnimation()
    }

    fun stop() {
        rootView.removeView(rLayout)
        lottieAnimationView.cancelAnimation()
    }
}