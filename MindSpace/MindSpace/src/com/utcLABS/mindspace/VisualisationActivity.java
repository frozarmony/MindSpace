package com.utcLABS.mindspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.MenuActivity;
import android.view.ext.R;


public class VisualisationActivity extends MenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualisation);

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
		}
		return super.onOptionsItemSelected(item);
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
			View rootView = inflater.inflate(R.layout.fragment_visualisation,
					container, false);
			return rootView;
		}
	}

}

