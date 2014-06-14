package com.utcLABS.mindspace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.utcLABS.mindspace.model.ConceptModel;

@SuppressLint("NewApi") public class GoogleFragment extends Fragment {
	private WebView webView;
	private ConceptModel conceptModel;

	private String JSToInclude = "document.getElementById('mfl').style.display = 'none';document.getElementById('sfcnt').style.display = 'none';document.getElementById('mhog_head').style.display = 'none';document.getElementsByTagName('body')[0].removeChild(document.getElementById('is_l'));function selectImage(imgElement){var index = imgElement.getAttribute('data-index');index = parseInt(index);MindSpace.selectImage(imgElement.src,pa[index].u);}var metas = document.getElementsByClassName('mimimg');for (var i = 0; i < metas.length; i++) {	metas[i].removeAttribute('jsaction');}var imgs = document.getElementsByClassName('rg_di');for (var i = 0; i < imgs.length; i++) {	imgs[i].setAttribute('onClick','selectImage(this.firstChild)');}";
	
	public GoogleFragment() {
		
	}

	
	public static GoogleFragment newInstance(ConceptModel currentConcept) {
		GoogleFragment fragment = new GoogleFragment();
		Bundle args = new Bundle();
		args.putParcelable("currentConcept", currentConcept);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_web, container,false);
		

		Bundle args = getArguments();
		conceptModel = args.getParcelable("currentConcept");
				
		webView = (WebView) rootView.findViewById(R.id.wikipediaView);
		
		webView.setWebViewClient(new MyWebView());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new WebAppInterface(rootView.getContext()), "MindSpace");
		
		initFragment();
		return rootView;
	}	
	
	private class MyWebView extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			System.out.println("finished =) =)");
			webView.evaluateJavascript(JSToInclude, null);
			//webView.documentHasImages(response);
		}

		@Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
			ArrayMap<String, String> header = new ArrayMap<String,String>();

			header.put("user-agent","Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5");
			view.loadUrl(url,header);
			return true;
	      }
	}
	
	public class WebAppInterface {
	    Context mContext;

	    /** Instantiate the interface and set the context */
	    WebAppInterface(Context c) {
	        mContext = c;
	    }

	    /** Show a toast from the web page */
	    @JavascriptInterface
	    public void selectImage(String thumbnailUrl,String url) {
	    	/*new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						System.out.println("Début téléchargement");
			    	    URL  imageUrl = new URL (imgUrl);
			    	    Bitmap img = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
			    	    imgView = new ImageView(dialogBuilder.getContext());
			    	    ((ImageView)imgView).setImageBitmap(img);
			    	    dialogBuilder.setView(imgView);
			    	    imgView.postInvalidate();
			    	    System.out.println("Fin téléchargement");
			    	  }
			    	  catch(Exception  ex) {
			    		  ex.printStackTrace();
			    		  Toast.makeText(mContext, "Impossible de récupérer l'image..", Toast.LENGTH_SHORT).show();
			    		  return;
			    	  }
				}
			}).start();*/
	    	final String fullUrl = url;
	    	URL imageUrl = null;
	    	Bitmap img;
			try {
				imageUrl = new URL(thumbnailUrl);
				img = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
			} catch (MalformedURLException e) {
				Toast.makeText(mContext, "Impossible de récupérer l'image..", Toast.LENGTH_SHORT).show();
	    		return;
			} catch (IOException e) {
				Toast.makeText(mContext, "Impossible de récupérer l'image..", Toast.LENGTH_SHORT).show();
	    		return;
			}
    	    
    	    	
	    	
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
	    	ImageView imgView = new ImageView(dialogBuilder.getContext());
	    	imgView.setImageBitmap(img);
	    	imgView.setMinimumHeight(600);
	    	imgView.setMinimumWidth(600);
	    	dialogBuilder.setView(imgView);
			dialogBuilder.setTitle("Associer cette image au concept "+conceptModel.getName())
			.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					Toast.makeText(mContext,"Téléchargement de l'image", Toast.LENGTH_SHORT).show();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							Looper.prepare();
							try {
								System.out.println("Début téléchargement");
					    	    URL  imageUrl = new URL (fullUrl);
					    	    Bitmap img = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
					    	    FileOutputStream out = null;
					    	    String imgLocalPath = Environment.getExternalStorageDirectory() + File.separator + "MindSpace" + File.separator + "images" + File.separator + fullUrl.hashCode()+".jpg";
					    	    try {
					    	           out = new FileOutputStream(imgLocalPath);
					    	           img.compress(Bitmap.CompressFormat.JPEG, 70, out);
					    	    } catch (Exception e) {
					    	        e.printStackTrace();
					    	    } finally {
					    	           try{
					    	               out.close();
					    	           } catch(Throwable ignore) {}
					    	    }
					    	    conceptModel.addPicture(imgLocalPath);
					    	    System.out.println("Fin téléchargement");
					    	    DisplayInToast("L'image "+fullUrl+"a été associée au concept "+conceptModel.getName());
					    	  }
					    	  catch(Exception  ex) {
					    		  ex.printStackTrace();
					    		  DisplayInToast("Impossible de récupérer l'image..");
					    		  return;
					    	  }
						}
					}).start();
					Toast.makeText(mContext, "L'image "+fullUrl+"a été associée au concept "+conceptModel.getName(), Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton("Non",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});	
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();   
	    }
	    
	    private void DisplayInToast(String message){
	    	Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	    }
	}

	public void setConceptModel(ConceptModel currentConcept) {
		this.conceptModel = currentConcept;
	}
	
	public void initFragment() {
		if(conceptModel!=null){
			ArrayMap<String, String> header = new ArrayMap<String,String>();
			header.put("user-agent","Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5");
			webView.loadUrl("https://www.google.fr/search?hl=fr&biw=1366&bih=643&site=imghp&tbm=isch&source=hp&biw=1366&bih=643&q="+conceptModel.getName(),header);
		}else {
			webView.loadUrl("https://www.google.fr/search?hl=fr&biw=1366&bih=643&site=imghp&tbm=isch&source=hp&biw=1366&bih=643");
		}
	}
}
