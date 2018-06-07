package br.com.psoa.megasena.megasenaapp.view

import android.content.Intent
import android.os.Bundle
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

        fab.setOnClickListener { onPlayLottery() }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_menu_layout, HomeFragment(), "content_menu_layout")
                    .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_menu_layout, BetListFragment(), "content_menu_layout")
                .commit()

    }

    private fun onPlayLottery() {
        val intent = Intent(this@MenuActivity,
                PlayLotteryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        intent.putExtra("isEdit", false)
        startActivityForResult(intent, 1)
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

        when (item.itemId) {
            R.id.nav_exit -> {
                onExit()
                return true
            }
        }
        // Handle navigation view item clicks here.
        val fragment = when (item.itemId) {
            R.id.nav_info -> {
                HomeFragment()
            }
            R.id.nav_my_bets -> {
                BetListFragment()
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

    private fun onExit() {
        val sp = getSharedPreferences("login", MODE_PRIVATE)
        sp.edit().remove("email").apply()
        sp.edit().remove("userId").apply()
        openLoginActivity()
    }


    private fun openLoginActivity() {
        val intent = Intent(this@MenuActivity,
                LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        finish()
    }



}
