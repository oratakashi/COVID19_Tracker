package com.oratakashi.covid19.ui.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import butterknife.ButterKnife
import butterknife.OnClick
import com.oratakashi.covid19.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    var current_url = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        ButterKnife.bind(this)

        var url = intent.getStringExtra("url")
        url = url!!.replace("https://", "")
        val fullDomain = url.split(".com")
        val domain = fullDomain[0].split(".")

        tvLink.text = domain[1]+".com"

        wvPage.settings.javaScriptEnabled = true
        wvPage.settings.builtInZoomControls = true
        wvPage.settings.displayZoomControls = false
        wvPage.isHorizontalScrollBarEnabled = false
        wvPage.isVerticalFadingEdgeEnabled = false
        wvPage.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                var urls = url
                current_url = url!!
                urls = urls!!.replace("https://", "")
                val fullDomain = urls.split(".com")
                val domain = fullDomain[0].split(".")
                tvLink.text = domain[1]+".com"
                shWebView.visibility = View.VISIBLE
                shWebView.startShimmerAnimation()
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                shWebView.stopShimmerAnimation()
                shWebView.visibility = View.GONE
            }
        }

        wvPage.loadUrl(intent.getStringExtra("url"))
    }

    override fun onBackPressed() {
        if(wvPage.canGoBack()){
            wvPage.goBack()
        }else{
            super.onBackPressed()
        }
    }

    @OnClick(R.id.ivBack) fun onBack(){
        if(wvPage.canGoBack()){
            wvPage.goBack()
        }else{
            finish()
        }
    }

    @OnClick(R.id.tvButton) fun onBrowser(){
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(current_url)
        startActivity(i)
    }
}
