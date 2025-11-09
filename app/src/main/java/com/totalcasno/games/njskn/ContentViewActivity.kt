package com.totalcasno.games.njskn

import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebView as PageViewer
import android.webkit.WebViewClient as PageClient
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class ContentViewActivity : ComponentActivity() {
    
    private lateinit var contentDisplay: PageViewer
    private lateinit var loadingIndicator: ProgressBar
    private var isFirstLoad = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        hideSystemUI()
        
        val rootLayout = ConstraintLayout(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(android.graphics.Color.BLACK)
        }
        
        contentDisplay = PageViewer(this).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(android.graphics.Color.BLACK)
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                setSupportZoom(true)
                builtInZoomControls = false
                loadWithOverviewMode = true
                useWideViewPort = true
            }
            
            setWebViewClient(object : PageClient() {
                override fun onPageStarted(view: PageViewer?, link: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, link, favicon)
                    if (isFirstLoad) {
                        loadingIndicator.visibility = View.VISIBLE
                    }
                }
                
                override fun onPageFinished(view: PageViewer?, link: String?) {
                    super.onPageFinished(view, link)
                    if (isFirstLoad) {
                        loadingIndicator.visibility = View.GONE
                        isFirstLoad = false
                    }
                }
                
                override fun shouldOverrideUrlLoading(
                    view: PageViewer?,
                    request: WebResourceRequest?
                ): Boolean {
                    return false
                }
            })
        }
        
        loadingIndicator = ProgressBar(this).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            }
            indeterminateTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#4A90E2")
            )
        }
        
        rootLayout.addView(contentDisplay)
        rootLayout.addView(loadingIndicator)
        
        setContentView(rootLayout)
        
        val targetLink = intent.getStringExtra("TARGET_LINK")
        if (!targetLink.isNullOrEmpty()) {
            contentDisplay.loadUrl(targetLink)
        }
        
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (contentDisplay.canGoBack()) {
                    contentDisplay.goBack()
                }
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
    
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        hideSystemUI()
    }
    
    override fun onDestroy() {
        contentDisplay.destroy()
        super.onDestroy()
    }
}

