package com.example.smartsevak

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.webView)

        val url = intent.getStringExtra("url")

        webView.webViewClient = WebViewClient()

        // this will load the url of the website
        webView.loadUrl(url.toString())

        // this will enable the javascrip
        //   settings, it can also allow xss vulnerabilities
        webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)

//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        webView.settings.domStorageEnabled = true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }
        else
            onBackPressedDispatcher.onBackPressed()
    }
}