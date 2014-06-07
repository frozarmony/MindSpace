package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.utcLABS.mindspace.model.ConceptModel;

public class WikipediaFragment extends Fragment {

	private WebView webView;
	private ConceptModel conceptModel;
	
	public WikipediaFragment(){
		
	}
	
	public static WikipediaFragment newInstance(ConceptModel currentConcept) {
		WikipediaFragment fragment = new WikipediaFragment();
		Bundle args = new Bundle();
		args.putParcelable("currentConcept", currentConcept);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_web, container,
				false);
		
		Bundle args = getArguments();
		conceptModel = args.getParcelable("currentConcept");
			
		webView = (WebView) rootView.findViewById(R.id.wikipediaView);
		webView.setWebViewClient(new MyWebView());
		webView.getSettings().setJavaScriptEnabled(true);
		
		initFragment();

		return rootView;
	}
	
	private class MyWebView extends WebViewClient {
		 @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         view.loadUrl(url);
	         return true;
	      }
	}

	public void setConceptModel(ConceptModel currentConcept) {
		this.conceptModel = currentConcept;
	}

	public void initFragment() {
		if(conceptModel!=null){
			webView.loadUrl("http://fr.wikipedia.org/wiki/"+conceptModel.getName());
		}else {
			webView.loadUrl("http://fr.wikipedia.org/wiki/");
		}
	}
}
