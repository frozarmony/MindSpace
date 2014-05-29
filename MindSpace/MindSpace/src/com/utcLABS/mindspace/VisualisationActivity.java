package com.utcLABS.mindspace;

import com.devadvance.circularseekbar.CircularSeekBar;
import com.utcLABS.mindspace.model.MindMapModel;
import com.utcLABS.mindspace.view.MindMapView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.EditionActivity;
import android.view.ext.R;


public class VisualisationActivity extends ActionBarActivity {

	protected MenuItem itemEdit;
	protected MenuItem itemSee;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualisation);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		itemEdit = menu.findItem(R.id.menu_edit); 
		itemEdit.setEnabled(true);
		itemEdit.setIcon(R.drawable.ic_action_edit);
		
		itemSee = menu.findItem(R.id.menu_see);
		itemSee.setEnabled(false);
		itemSee.setIcon(R.drawable.ic_action_see_selected);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_edit){
			item.setEnabled(false);
			item.setIcon(R.drawable.ic_action_edit_selected);
			itemSee.setIcon(R.drawable.ic_action_see);
			itemSee.setEnabled(true);
			Intent i0 = new Intent(this, EditionActivity.class);
			startActivity(i0);
			this.finish();
		}else if(id == android.R.id.home){
			Intent i0 = new Intent(this, HomeActivity.class);
			startActivity(i0);
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
               
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Intent i0 = new Intent(this, HomeActivity.class);
			startActivity(i0);
			this.finish();
        }
		return true;
           
     }

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private MindMapView viewMindMap;
		private MindMapModel model;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_visualisation,
					container, false);
			viewMindMap = (MindMapView)rootView.findViewById(R.id.surfaceView);
	        model = viewMindMap.getModel();
	        viewMindMap.setDensity(1f);
			
			CircularSeekBar slider = (CircularSeekBar) rootView.findViewById(R.id.circularSeekBar1);
			
			return rootView;
		}
	}

}

