package com.example.joyfulcampus

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.joyfulcampus.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // activity_main의 BottomNavigationView와 FragmentContainerView 연결 구현
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)

        // Toolbar
        val toolbarBodyTemplate = binding.appBarMain.toolbar
        setSupportActionBar(toolbarBodyTemplate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_apps_24)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarBodyTemplate.title = "편안한 캠퍼스"


    }

    // 챗봇 버튼 구현
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                // TODO : 챗봇 icon 클릭 후 발생하는 이벤트 구현하기
            }
            R.id.chat_bot -> {
                // TODO : 챗봇 icon 클릭 후 발생하는 이벤트 구현하기
            }
        }
        return super.onOptionsItemSelected(item)
    }
}