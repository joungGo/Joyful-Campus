package com.example.joyfulcampus

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.joyfulcampus.data.Key.Companion.DB_URL
import com.example.joyfulcampus.data.Key.Companion.DB_USERS
import com.example.joyfulcampus.databinding.ActivityMainBinding
import com.example.joyfulcampus.ui.auth.AuthActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 앱 들어갔을 때 로그인 확인하고 로그인 안되어있으면 AuthActivity로 이동
        val currentUser = Firebase.auth.currentUser


        if (currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        } else {
            currentUser.reload().addOnCompleteListener {
                if (currentUser.isEmailVerified) {
                } else {
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                }
            }
        }

        setchatsidebar()

        askNotificationPermission()

        // activity_main의 BottomNavigationView와 FragmentContainerView 연결 구현
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)


// NavigationView의 메뉴 텍스트 색상 설정
        binding.mainSidebar.itemTextColor = ContextCompat.getColorStateList(this, R.color.black)

        // NavigationView 아이템 클릭 리스너 설정
        binding.mainSidebar.setNavigationItemSelectedListener { menuItem ->
            // 메뉴 아이템 클릭 시 사이드바를 닫기
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            // 클릭 이벤트 처리
            when (menuItem.itemId) {
                R.id.hompage -> {
                    openUrl("https://www.dju.ac.kr/dju/main.do")
                    true
                }

                R.id.portal -> {
                    openUrl("https://portal.dju.ac.kr/")
                    true
                }

                R.id.schedule -> {
                    openUrl("https://www.dju.ac.kr/dju/sv/schdulView/schdulCalendarView.do?mi=1166")
                    true
                }

                R.id.schedule_notice -> {
                    openUrl("https://www.dju.ac.kr/dju/na/ntt/selectNttList.do?mi=1165&bbsId=1861")
                    true
                }

                else -> false
            }
        }
    }

    // 챗봇 버튼 Click Event
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_toolbar, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 홈 버튼 클릭 시 사이드바 열기
                binding.drawerLayout.openDrawer(GravityCompat.START)
                return true
            }

            R.id.chat_bot -> {
                // 챗봇 아이템 클릭 시 동작 작성
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        // 뒤로가기 버튼을 눌렀을 때 사이드바가 열려 있다면 닫기
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Function to open URL in browser
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showPermissionRationalDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setchatsidebar() {
        binding.chatSidebarBackground.isVisible = false
        binding.chatSidebar.isVisible = false
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationalDialog() {
        AlertDialog.Builder(this)
            .setMessage("알림 권한이 없으면 권한을 받을 수 있습니다.")
            .setPositiveButton("권한 허용하기") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }.setNegativeButton("취소") { dialogInterface, _ -> dialogInterface.cancel() }
            .show()
    }

}