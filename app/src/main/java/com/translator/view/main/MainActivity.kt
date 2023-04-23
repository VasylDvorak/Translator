package com.translator.view.main

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.translator.R
import com.translator.application.App
import com.translator.databinding.ActivityMainBinding
import com.translator.presenter.BackButtonListener
import com.translator.presenter.MainPresenter
import javax.inject.Inject
import kotlin.properties.Delegates

private const val THEME_KEY = "theme_key"
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    var setTheme by Delegates.notNull<Boolean>()

    val navigator = AppNavigator(this, R.id.container)

    private val presenter = MainPresenter().apply {
            App.instance.appComponent.inject(this)
        }


    private var vb: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme = this.getPreferences(Context.MODE_PRIVATE).getBoolean(THEME_KEY, false)
        if(setTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb?.root)

        App.instance.appComponent.inject(this)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var updateStatusTheme = menu?.findItem(R.id.change_theme)
        updateStatusTheme?.setChecked(setTheme)
        return super.onCreateOptionsMenu(menu)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.change_theme -> {
                item.isChecked = !item.isChecked
                saveStatusTheme(item.isChecked)

                this.recreate()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveStatusTheme(nightTheme: Boolean) {
            with(this.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(THEME_KEY, nightTheme)
                apply()
        }
    }


}