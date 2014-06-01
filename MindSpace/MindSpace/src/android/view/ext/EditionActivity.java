package android.view.ext;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
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
import com.utcLABS.mindspace.view.MindMapView;

public class EditionActivity extends ActionBarActivity {

	protected MenuItem itemEdit;
	protected MenuItem itemSee;
	private ConceptModel currentConcept;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edition);
		
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
			i0.putExtra("title", title);
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

		private MindMapView viewMindMap;
		private MindMapModel model;
		View rootView = null;
		TextEditFragment editFg = new TextEditFragment();
		PictureEditFragment pictureFg = new PictureEditFragment();
		ColorFragment colorFg = new ColorFragment();
		WikipediaFragment wikiFg = new WikipediaFragment();
		GoogleFragment googleFg = new GoogleFragment();
		private ConceptModel currentConcept = null;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_edition, container,false);
			 viewMindMap = (MindMapView)rootView.findViewById(R.id.surfaceView);
			 viewMindMap.setCurrentFragment(this);
			 viewMindMap.setMode(true);
	         model = viewMindMap.getMindMapModel();
			
			final SatelliteMenu menu = (SatelliteMenu) rootView.findViewById(R.id.menu);
            List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
            items.add(new SatelliteMenuItem(4, R.drawable.duplicate_button));
            items.add(new SatelliteMenuItem(4, R.drawable.redo_button));
            items.add(new SatelliteMenuItem(4, R.drawable.undo_button));
            items.add(new SatelliteMenuItem(1, R.drawable.add_button));;
            menu.addItems(items);
           
            
            
            menu.setOnItemClickedListener(new SateliteClickedListener() {
            	  public void eventOccured(int id) {
            		  if(id == 1){
                		  currentConcept = model.createNewConcept(new PointF(300,300));
                		  
                		  editFg.setConceptModel(currentConcept);
                		  pictureFg.setConceptModel(currentConcept);
                		  colorFg.setConceptModel(currentConcept);
                		  wikiFg.setConceptModel(currentConcept);
                		  googleFg.setConceptModel(currentConcept);
                		  
                		  viewMindMap.setModel(model);
                		  DrawerLayout drawerLayout = (DrawerLayout)rootView.findViewById(R.id.drawer_layout);
                		  drawerLayout.openDrawer(rootView.findViewById(R.id.layout_fragment));
            		  }	  
            	  }
            	});
			
	        getFragmentManager().beginTransaction().add(R.id.container_fragment, editFg).commit();

	        ImageButton editConcept = (ImageButton)rootView.findViewById(R.id.edit_concept);
	        editConcept.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					editFg.initFragment(currentConcept);
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, editFg);
			        transaction.addToBackStack(null).commit();
				}
			});
	        
	        ImageButton editPicture = (ImageButton)rootView.findViewById(R.id.edit_picture);
	        editPicture.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					pictureFg.setConceptModel(currentConcept);					
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, pictureFg);
			        transaction.addToBackStack(null).commit();
				}
			});
	        
	        ImageButton wikipedia = (ImageButton)rootView.findViewById(R.id.wikipedia);
	        wikipedia.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					wikiFg.setConceptModel(currentConcept);					
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, wikiFg);
			        transaction.addToBackStack(null).commit();
				}
			});
	        
	        ImageButton google = (ImageButton)rootView.findViewById(R.id.google);
	        google.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					googleFg.setConceptModel(currentConcept);					
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, googleFg);
			        transaction.addToBackStack(null).commit();
				}
			});
	        
	        
	        ImageButton color = (ImageButton)rootView.findViewById(R.id.edit_shape);
	        color.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					colorFg.setConceptModel(currentConcept);					
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container_fragment, colorFg);
			        transaction.addToBackStack(null).commit();
				}
			});
	   		return rootView;
		}
		
		public void editConcept(ConceptModel model) {
			currentConcept = model;
			editFg.initFragment(currentConcept);
			DrawerLayout drawerLayout = (DrawerLayout)rootView.findViewById(R.id.drawer_layout);
			drawerLayout.openDrawer(rootView.findViewById(R.id.layout_fragment));
		}
	}

}
