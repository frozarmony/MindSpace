package android.view.ext;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.utcLABS.mindspace.ColorFragment;
import com.utcLABS.mindspace.GoogleFragment;
import com.utcLABS.mindspace.PictureEditFragment;
import com.utcLABS.mindspace.TextEditFragment;
import com.utcLABS.mindspace.VisualisationActivity;
import com.utcLABS.mindspace.WikipediaFragment;
import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.MindMapModel;
import com.utcLABS.mindspace.utilities.MindmapAdapter;
import com.utcLABS.mindspace.view.CircleView;
import com.utcLABS.mindspace.view.ConceptView;

public class EditionActivity extends ActionBarActivity {

	protected MenuItem itemEdit;
	protected MenuItem itemSee;
	private ConceptModel currentConcept;
	private MindMapModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edition);
				
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
			this.onPause();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			final View rootView = inflater.inflate(R.layout.fragment_edition, container,
					false);
			
			SatelliteMenu menu = (SatelliteMenu) rootView.findViewById(R.id.menu);
            List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
            items.add(new SatelliteMenuItem(4, R.drawable.duplicate_button));
            items.add(new SatelliteMenuItem(4, R.drawable.redo_button));
            items.add(new SatelliteMenuItem(4, R.drawable.undo_button));
            items.add(new SatelliteMenuItem(3, R.drawable.add_button));;
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
	        
	        
	        /* Definition of the bin events */
	        final View binDrag = (View)rootView.findViewById(R.id.binDrag);
	        binDrag.setOnDragListener(new OnDragListener() {

	     			@Override
	     			public boolean onDrag(View v, final DragEvent event) {
	     				// Init
	     				final ConceptView conceptView;
	     				
	     				switch (event.getAction()) {
	     				case DragEvent.ACTION_DRAG_STARTED:
		    				if( !event.getClipDescription().hasMimeType(ConceptView.MIMETYPE_CONCEPTVIEW) )
		    					return false;
		    				binDrag.setBackgroundResource(R.drawable.bin_drag);
	     					Log.d("Bin", "Action Drag Start");
		    				break;
	     				case DragEvent.ACTION_DROP:
	     					Log.d("Bin", "Action Drop");
	     					conceptView = (ConceptView) event.getLocalState();
	     					/*Dialog Window to confirm the concept deletion */
	     					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(rootView.getContext())
							.setTitle("Supprimer le concept")
							.setMessage("Voulez-vous supprimer le concept \""+ conceptView.getModel().getName()+ "\" ?");
				
							dialogBuilder.setPositiveButton("Supprimer",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {	
			     					conceptView.getModel().delete();
								}
							});
							
							dialogBuilder.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});	
							
							AlertDialog dialog = dialogBuilder.create();
							dialog.show();   

	     					break;
	     				case DragEvent.ACTION_DRAG_ENDED:
		    				binDrag.setBackground(null);
	     					Log.d("Bin", "Action Drag Ended");
	     					break;
	     				case DragEvent.ACTION_DRAG_ENTERED:
	     					Log.d("Bin", "Action Drag Entered");
		    				binDrag.setBackgroundResource(R.drawable.bin_drag_hover);
		    				break;
	     				case DragEvent.ACTION_DRAG_EXITED:
	     					binDrag.setBackgroundResource(R.drawable.bin_drag);
	     					break;
	     				}
	     				return true;
	     			}
	     		});
	        
	        
	        
	   		return rootView;
		}
		

	}

}
