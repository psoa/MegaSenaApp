package br.com.psoa.megasena.megasenaapp.view

import android.content.Intent
import android.net.Uri
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

class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private val _fragmentTag = "content_menu_layout"

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
                    .add(R.id.content_menu_layout, HomeFragment(), _fragmentTag)
                    .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_menu_layout, BetListFragment(), _fragmentTag)
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
//    FIXME: Add a settings editing functionality (like change password)
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Handle navigation view item clicks here.
        // Open new activities
        when (item.itemId) {
            R.id.nav_exit -> {
                onExit()
                return true
            }
            R.id.nav_map -> {
                showMap()
                return true
            }
            R.id.nav_share -> {
                shareNumbers()
                return true
            }
            R.id.nav_send -> {
                makePhoneCall("")
                return true
            }
        }

        //Open new fragments
        val fragment = when (item.itemId) {
            R.id.nav_info -> {
                title = getString(R.string.title_activity_menu)
                HomeFragment()
            }
            R.id.nav_my_bets -> {
                title = "Minhas Apostas"
                BetListFragment()
            }
            R.id.nav_about -> {
                title = "Sobre"
                AboutFragment()
            }
            else -> {
                title = getString(R.string.title_activity_menu)
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


    private fun shareNumbers() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.caixa_lottery_result_uri))
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_you_win)))
    }

    private fun makePhoneCall(number: String) : Boolean {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
            startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun showMap() {

            val intent = Intent(this@MenuActivity,
                    LotteryLocationMapsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            finish()
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
