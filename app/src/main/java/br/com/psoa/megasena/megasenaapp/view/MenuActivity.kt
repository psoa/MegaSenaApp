package br.com.psoa.megasena.megasenaapp.view

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import br.com.psoa.megasena.megasenaapp.R
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*

class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Add a new bet", Snackbar.LENGTH_LONG)
                    .setAction("bet", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_menu_layout, HomeFragment() , "content_menu_layout")
                    .commit()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        val fragment = when (item.itemId) {
            R.id.nav_camera -> {
                HomeFragment()
            }
            R.id.nav_gallery -> {
                HomeFragment()
            }
            R.id.nav_slideshow -> {
                HomeFragment()
            }
            R.id.nav_manage -> {
                HomeFragment()
            }
            R.id.nav_share -> {
                HomeFragment()
            }
            R.id.nav_send -> {
                HomeFragment()
            }
            R.id.nav_about -> {
                AboutFragment()
            }
            R.id.nav_exit -> {
                HomeFragment()
            }
            else -> {
                HomeFragment()
            }
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_menu_layout, fragment)
                .commit()
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
