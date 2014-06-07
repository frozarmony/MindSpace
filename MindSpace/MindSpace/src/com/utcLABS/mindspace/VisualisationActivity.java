package com.utcLABS.mindspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.EditionActivity;
import android.view.ext.R;

import com.devadvance.circularseekbar.CircularSeekBar;
import com.devadvance.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;
import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.CurrentMindMap;
import com.utcLABS.mindspace.model.MindMapModel;
import com.utcLABS.mindspace.view.MindMapView;


public class VisualisationActivity extends ActionBarActivity {

	protected MenuItem itemEdit;
	protected MenuItem itemSee;
	private String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualisation);

		title = this.getIntent().getExtras().getString("title");
		setTitle(title);
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
			i0.putExtra("title", title);
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
		private View rootView = null;
		private DrawerLayout drawer = null;
		private SeeFragment seeFg = new SeeFragment();
		private ConceptModel currentConcept = null;
		private CircularSeekBar slider = null;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_visualisation,
					container, false);
			
			drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout_visualisation);
			
			//initView
//			viewMindMap = (MindMapView)rootView.findViewById(R.id.surfaceView);
//	        model = viewMindMap.getModel();
//			viewMindMap.setCurrentFragment(this);
//	        viewMindMap.setEditMode(false);
//	        viewMindMap.setDensity(0f);
	        
	        viewMindMap = (MindMapView)rootView.findViewById(R.id.surfaceView);
			viewMindMap.setCurrentFragment(this);
			viewMindMap.setModel(((CurrentMindMap) getActivity().getApplication()).getCurrentMindMap());
			viewMindMap.setEditMode(false);
			viewMindMap.setDensity(0f);
			
	        getFragmentManager().beginTransaction().add(R.id.layout_visualisation, seeFg).commit();
	        			
			initSlider(); 
			return rootView;
		}

		private void initSlider() {
			slider = (CircularSeekBar) rootView.findViewById(R.id.circularSeekBar1);
			slider.setOnSeekBarChangeListener(new OnCircularSeekBarChangeListener(){

				@Override
				public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
					drawer.closeDrawer(rootView.findViewById(R.id.layout_visualisation));
					float density = (float) (progress / 100.0);
					viewMindMap.setDensity(density);

				}

				@Override
				public void onStopTrackingTouch(CircularSeekBar seekBar) {
					
				}

				@Override
				public void onStartTrackingTouch(CircularSeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}

		public void editConcept(ConceptModel model) {
			currentConcept = model;
			seeFg.initFragment(currentConcept);
			drawer.openDrawer(rootView.findViewById(R.id.layout_visualisation));			
		}
	}

}

