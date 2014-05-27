package com.utcLABS.mindspace;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
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

import com.utcLABS.mindspace.model.Mindmap;
import com.utcLABS.mindspace.utilities.MindmapAdapter;

public class HomeActivity extends ActionBarActivity {
	final Context context = HomeActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_home);
	
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		ListView listeMindmaps;
		List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
		ListView lvDetail;
		ArrayList<Mindmap> myList = new ArrayList<Mindmap>();
		
		String[] title = new String[] { "Mindmap 1", "Mindmap 2", "Mindmap 3", "Mindmap 4",
				"Mindmap 5", "Mindmap 6", "Mindmap 7", "Mindmap 8" };
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
			listeMindmaps = (ListView) rootView.findViewById(R.id.list_mindmaps);
								
			getDataInList();
			
			final MindmapAdapter mindmapListAdapter = new MindmapAdapter(getActivity(), myList, listeMindmaps);
	        listeMindmaps.setAdapter(mindmapListAdapter);
	        
	        // Behavior of the newMindMap button
	        Button newMindMap = (Button)rootView.findViewById(R.id.nouveau_mindmap);
	        newMindMap.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					LayoutInflater inflater = getActivity().getLayoutInflater();
					View dialogView = inflater.inflate(R.layout.new_mindmap_dialog, null);
					final TextView input = (TextView) dialogView.findViewById(R.id.new_mindmap_title);
					
					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
					.setView(dialogView);
					dialogBuilder.setTitle("Nouveau")
					.setPositiveButton("Créer",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							Mindmap newRow = new Mindmap();
							newRow.setTitle(input.getText().toString());
							newRow.setLastModificationDate("Modifié le "+ new Date().toString());
		                    myList.add(newRow);
		                    mindmapListAdapter.notifyDataSetChanged();
						}
					})
					.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});	
					AlertDialog dialog = dialogBuilder.create();
					dialog.show();
					
				}
			});     
	        	          
			return rootView;
		}
		
		 private void getDataInList() { 
			 for(int i=0;i<8;i++) { 
		 		 // Create a new object for each list item 
				 Mindmap ld = new Mindmap(); 
				 ld.setTitle(title[i]); 
				 ld.setLastModificationDate(new Date().toString()); 
				 myList.add(ld); 
			 } 
		} 
	}
}
