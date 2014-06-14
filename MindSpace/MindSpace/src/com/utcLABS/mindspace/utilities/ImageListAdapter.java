package com.utcLABS.mindspace.utilities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.utcLABS.mindspace.PictureEditFragment;
import com.utcLABS.mindspace.SeeFragment;
import com.utcLABS.mindspace.model.ConceptModel;

public class ImageListAdapter extends BaseAdapter {

	private ArrayList<ImageView> pictures;
	private Context context;
	private boolean isInEditMode;
	private ConceptModel concept;
	private PictureEditFragment editfragment;
	private SeeFragment seeFragment;
	
	public ImageListAdapter(Context _context,ConceptModel _concept, PictureEditFragment pictureEditFragment){
		context = _context;
		concept = _concept;
		editfragment = pictureEditFragment;
		seeFragment = null;
		isInEditMode = true;
		pictures = new ArrayList<ImageView>();
	}
	
	public ImageListAdapter(Context _context,ConceptModel _concept, SeeFragment _seeFragment){
		context = _context;
		concept = _concept;
		editfragment = null;
		seeFragment = _seeFragment;
		isInEditMode = false;
		pictures = new ArrayList<ImageView>();
	}
	
	public List<ImageView> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> _pictures) {
		pictures.clear();
		System.out.println(_pictures);
		for (int i = 0; i < _pictures.size(); i++) {
			Uri path = Uri.parse(_pictures.get(i));
			ImageView imgV;
			if(editfragment != null)
				imgV = new ImageView(editfragment.getActivity());
			else
				imgV = new ImageView(seeFragment.getActivity());
			imgV.setPadding(10, 10, 10, 10);
			imgV.setAdjustViewBounds(true);
			if(isInEditMode)
				imgV.setOnClickListener(new EditClickListener(_pictures.get(i)));
			else
				imgV.setOnClickListener(new SeeClickListener(_pictures.get(i)));
			imgV.setImageURI(path);
			imgV.setMaxHeight(300);
			imgV.setMaxWidth(300);
			pictures.add(imgV);
		}
	}
	
	public class EditClickListener implements OnClickListener {
		private String path;
		public EditClickListener(String string) {
			path = string;
		}

		@Override
		public void onClick(View v) {
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);//editfragment.getActivity()
	    	ImageView imgView = new ImageView(dialogBuilder.getContext());
	    	imgView.setImageURI(Uri.parse(path));
	    	imgView.setMinimumHeight(600);
	    	imgView.setMinimumWidth(600);
	    	dialogBuilder.setView(imgView);
			dialogBuilder.setTitle("Dissocier cette image du concept ?")
			.setPositiveButton("Dissocier",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					concept.removePicture(path);
					pictures.remove(path);
					editfragment.initImageList();
					Toast.makeText(context,"L'image a été dissociée du concept "+concept.getName(), Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton("Fermer",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});	
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();  
		}
		
	}
	
	public class SeeClickListener implements OnClickListener {
		private String path;
		public SeeClickListener(String string) {
			path = string;
		}

		@Override
		public void onClick(View v) {
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
	    	ImageView imgView = new ImageView(dialogBuilder.getContext());
	    	imgView.setImageURI(Uri.parse(path));
	    	imgView.setMinimumHeight(600);
	    	imgView.setMinimumWidth(600);
	    	dialogBuilder.setView(imgView);
			dialogBuilder.setTitle("Image")
			.setNegativeButton("Fermer",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();  
		}
		
	}

	@Override
	public int getCount() {
		return pictures.size();
	}

	@Override
	public Object getItem(int position) {
		return pictures.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return pictures.get(position);
	}

	@Override
	public boolean isEmpty() {
		return pictures.isEmpty();
	}


	@Override
	public boolean isEnabled(int position) {
		return pictures.get(position).isEnabled();
	}

	@Override
	public long getItemId(int position) {
		return pictures.get(position).getId();
	}

}
