package android.view.ext;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.utcLABS.mindspace.ColorFragment;
import com.utcLABS.mindspace.GoogleFragment;
import com.utcLABS.mindspace.HomeActivity;
import com.utcLABS.mindspace.PictureEditFragment;
import com.utcLABS.mindspace.TextEditFragment;
import com.utcLABS.mindspace.VisualisationActivity;
import com.utcLABS.mindspace.WikipediaFragment;
import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.MindMapModel;

public class MenuActivity extends ActionBarActivity {

	protected MenuItem itemEdit;
	protected MenuItem itemSee;
	private ConceptModel currentConcept;
	private MindMapModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		itemEdit = menu.findItem(R.id.menu_edit); 
		itemEdit.setEnabled(false);
		itemEdit.setIcon(R.drawable.ic_action_edit_selected);
		
		itemSee = menu.findItem(R.id.menu_see);
		itemSee.setEnabled(true);
		itemSee.setIcon(R.drawable.ic_action_see);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_settings) {
			return true;
		}else if(id == R.id.menu_see){
			item.setEnabled(false);
			item.setIcon(R.drawable.ic_action_see_selected);
			itemEdit.setIcon(R.drawable.ic_action_edit);
			itemEdit.setEnabled(true);
			Intent i0 = new Intent(this, VisualisationActivity.class);
			startActivity(i0);
			this.finish();
			return true;
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


	public ConceptModel getCurrentConcept() {
		return currentConcept;
	}

	public void setCurrentConcept(ConceptModel currentConcept) {
		this.currentConcept = currentConcept;
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_menu, container,
					false);
			
			SatelliteMenu menu = (SatelliteMenu) rootView.findViewById(R.id.menu);
            List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
            items.add(new SatelliteMenuItem(4, R.drawable.ic_action_undo));
            items.add(new SatelliteMenuItem(4, R.drawable.ic_action_undo));
            items.add(new SatelliteMenuItem(4, R.drawable.ic_action_new));
            items.add(new SatelliteMenuItem(3, R.drawable.ic_action_new));;
            menu.addItems(items);
			
			Fragment fg = new TextEditFragment();
	        getFragmentManager().beginTransaction().add(R.id.container_fragment, fg).commit();

	        ImageButton editConcept = (ImageButton)rootView.findViewById(R.id.edit_concept);
	        editConcept.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Fragment fg = new TextEditFragment();
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, fg);
			        transaction.addToBackStack(null).commit();
				}
			});
	        
	        ImageButton editPicture = (ImageButton)rootView.findViewById(R.id.edit_picture);
	        editPicture.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Fragment fg = new PictureEditFragment();
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, fg);
			        transaction.addToBackStack(null).commit();
				}
			});
	        
	        ImageButton wikipedia = (ImageButton)rootView.findViewById(R.id.wikipedia);
	        wikipedia.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Fragment fg = new WikipediaFragment();
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, fg);
			        transaction.addToBackStack(null).commit();
				}
			});
	        
	        ImageButton google = (ImageButton)rootView.findViewById(R.id.google);
	        google.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Fragment fg = new GoogleFragment();
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, fg);
			        transaction.addToBackStack(null).commit();
				}
			});
	        
	        
	        ImageButton color = (ImageButton)rootView.findViewById(R.id.edit_shape);
	        color.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Fragment fg = new ColorFragment();
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, fg);
			        transaction.addToBackStack(null).commit();
				}
			});
	   		return rootView;
		}

	}

}
