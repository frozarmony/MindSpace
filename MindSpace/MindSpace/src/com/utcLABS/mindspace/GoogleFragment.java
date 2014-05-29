package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.ext.R;

public class GoogleFragment extends Fragment {
	private WebView webView;
	
	public GoogleFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_web, container,
				false);
		
		webView = (WebView) rootView.findViewById(R.id.wikipediaView);
		webView.setWebViewClient(new MyWebView());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("https://www.google.fr/imghp?hl=fr&tab=wi&ei=30J_U8isMs-yhAfbwICABA&ved=0CAQQqi4oAg");
		return rootView;
	}
	
	private class MyWebView extends WebViewClient {
		 @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         view.loadUrl(url);
	         return true;
	      }
	}
}
