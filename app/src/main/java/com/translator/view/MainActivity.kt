package com.translator.view

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.translator.R
import com.translator.databinding.ActivityMainBinding
import com.translator.presenter.BackButtonListener
import com.translator.presenter.MainPresenter
import org.koin.android.ext.android.inject

private const val DURATION = 1000L
private const val COUNTDOWN_DURATION = 2000L
private const val COUNTDOWN_INTERVAL = 1000L

class MainActivity : AppCompatActivity() {

    private val navigatorHolder: NavigatorHolder by inject()


    val navigator = AppNavigator(this, R.id.container)

    private val presenter = MainPresenter()

    private var vb: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultSplashScreen()
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb?.root)
        presenter.mainFragmentStart()

    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backPressed()) {
                return
            }
        }
        presenter.backClicked()

    }

    private fun setDefaultSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setSplashScreenHideAnimation()
            setSplashScreenDuration()
        }
    }

    @RequiresApi(31)
    private fun setSplashScreenHideAnimation() {

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat()
            ).apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = DURATION
                doOnEnd { splashScreenView.remove() }
                start()
            }
        }

    }

    private fun setSplashScreenDuration() {
        var isHideSplashScreen = false
        object : CountDownTimer(COUNTDOWN_DURATION, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                isHideSplashScreen = true
            }
        }.start()
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isHideSplashScreen) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

    }
}