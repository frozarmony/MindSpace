package com.utcLABS.mindspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ext.R;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.utcLABS.mindspace.model.MindMapModel;
import com.utcLABS.mindspace.model.MindMapXmlParser;
import com.utcLABS.mindspace.utilities.MindmapAdapter;
import com.utcLABS.mindspace.view.CreateMindmapDialog;

public class HomeActivity extends ActionBarActivity {
	final Context context = HomeActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initStorageDirectory();
		setContentView(R.layout.activity_home);

//		/* TEST : SUPPRESSION DES XML*/
//		String[] files = context.fileList();
//		if (files.length == 0)
//			System.out.println("Vide");
//		else {
//			for(String file : files){
//				System.out.println(file);
//				deleteFile(file);
//				System.out.println("supprimé");
//			}
//		}
//		
//		/* INSERTION FICHIER TEST */	
//		FileOutputStream output = null;        	
//		String xml = "<?xml version='1.0' encoding='UTF-8'?>"
//				+ "<mindmap>"
//				+ "<head>"
//					+ "<title>Santé</title>"
//					+ "<lastModificationDate>8 Jun 2014</lastModificationDate>"
//				+ "</head>"
//				+ "<concepts>"
//					+ "<concept desc='' picture='content://media/external/images/media/13' name='Santé' x='950.0' y='475.0' size='0.7' color='-1' shape='oval'>"
//						+ "<concept desc='' picture='' name='Hygiène de vie' x='740.0' y='580.0' size='0.48999998' color='-16733697' shape='oval'>"
//						+ "<concept desc='' picture='' name='Sport' x='670.0' y='632.5' size='0.343' color='-16733697' shape='oval'>"
//							+ "</concept>"
//						+ "</concept>"
//						+ "<concept desc='' picture='' name='Rigour' x='1125.0' y='387.5' size='0.48999998' color='-65495' shape='oval'>"
//							+ "<concept desc='' picture='' name='Sommeil' x='1265.0' y='317.5' size='0.343' color='-65495' shape='oval'>"
//							+ "</concept>"
//						+ "</concept>"
//					+ "<concept desc='' picture='' name='Soins' x='775.0' y='387.5' size='0.48999998' color='-8651008' shape='oval'>"
//						+ "</concept>"
//					+ "</concept>"
//				+ "</concepts>"
//			+ "</mindmap>";
//		try {
//			output = context.openFileOutput("Santé", Context.MODE_PRIVATE);
//			output.write(xml.getBytes());
//			if(output != null)
//			    output.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//				try {
//					inputFile = openFileInput(file);
//					MindMapModel mindmap = xmlParser.parseFile(inputFile);
//					System.out.println(mindmap.getTitle());
//					System.out.println(mindmap.getLastModificationDate());
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (XmlPullParserException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}	
	}

	private void initStorageDirectory() { // initialise le dossier de stockage des fichier locaux

		File mindSpaceDir = new File(Environment.getExternalStorageDirectory() + File.separator + "MindSpace");
		
		if(!mindSpaceDir.exists() && !mindSpaceDir.mkdir())
			System.err.println("Impossible de créer le dossier de l'application");
		
		File mindMapsDir = new File(Environment.getExternalStorageDirectory() + File.separator + "MindSpace" + File.separator + "mindmaps");
		
		if(!mindMapsDir.exists() && !mindMapsDir.mkdir())
			System.err.println("Impossible de créer le dossier de mindmap");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
               
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	this.finish();
        }
		return true;
           
     }

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		ListView listMindmapsView;
		ArrayList<MindMapModel> listMindmaps = new ArrayList<MindMapModel>();
		
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
			listMindmapsView = (ListView) rootView.findViewById(R.id.list_mindmaps);
			
			/* Loading of the existing Mindmaps */
			try {
 				loadMindmapFiles(getActivity());
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			final MindmapAdapter mindmapListAdapter = new MindmapAdapter(getActivity(), listMindmaps, listMindmapsView);
			listMindmapsView.setAdapter(mindmapListAdapter);
	        
	        // Definition of the new MindMap button behavior
	        Button newMindMap = (Button)rootView.findViewById(R.id.nouveau_mindmap);
	        newMindMap.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Context context = getActivity();
					LayoutInflater inflater = getActivity().getLayoutInflater();
					View dialogView = inflater.inflate(R.layout.new_mindmap_dialog, null);
					
					new CreateMindmapDialog(context, dialogView, mindmapListAdapter);
				}
	        });
					
			return rootView;
		}
		 
		private void loadMindmapFiles(Context context) throws XmlPullParserException, IOException{
			String[] files = context.fileList();
			MindMapXmlParser parser = new MindMapXmlParser();
			FileInputStream input;
			MindMapModel mindmap = null;
			if (files.length == 0)
				System.out.println("Vide");
			else {
				for(String file : files){
					input = context.openFileInput(file);
					try {
						mindmap = parser.parse(input);
						listMindmaps.add(mindmap);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}	
	}
}
