package com.utcLABS.mindspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.view.ext.R;

public class WikipediaFragment extends Fragment {

	private WebView webView;
	
	public WikipediaFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_web, container,
				false);
		
		webView = (WebView) rootView.findViewById(R.id.wikipediaView);
		webView.setWebViewClient(new MyWebView());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://fr.wikipedia.org/");
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
