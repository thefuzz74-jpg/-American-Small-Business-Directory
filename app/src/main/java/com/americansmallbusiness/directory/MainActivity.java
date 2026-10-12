package com.americansmallbusiness.directory;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class MainActivity extends Activity {
 private WebView webView;
 @Override public void onCreate(Bundle state) { super.onCreate(state); webView = new WebView(this); webView.setWebViewClient(new WebViewClient()); webView.getSettings().setJavaScriptEnabled(true); setContentView(webView); webView.loadUrl("file:///android_asset/index.html"); }
 @Override public void onBackPressed() { if(webView!=null && webView.canGoBack()) webView.goBack(); else super.onBackPressed(); }
 @Override protected void onDestroy() { if(webView!=null) webView.destroy(); super.onDestroy(); }
}
