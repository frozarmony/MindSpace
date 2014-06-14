package com.utcLABS.mindspace;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.Button;
import android.widget.ListView;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.utilities.ImageListAdapter;

public class PictureEditFragment extends Fragment {

	private int RESULT_LOAD_IMAGE = 1;
	View rootView;
	ConceptModel conceptModel;
	private ListView imgList;

	public PictureEditFragment(){
		
	}
	
	public static PictureEditFragment newInstance(ConceptModel currentConcept) {
		PictureEditFragment fragment = new PictureEditFragment();
		Bundle args = new Bundle();
		args.putParcelable("currentConcept", currentConcept);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.edit_picture, container,
				false);
		
		Bundle args = getArguments();
		conceptModel = args.getParcelable("currentConcept");
		
		initImageList();
//		initFragment();
		
		Button loadPicture = (Button)rootView.findViewById(R.id.buttonLoadPicture);
		loadPicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);	
			}
		});
		
		return rootView;
	}
	
	public void initImageList() {
		if(conceptModel!=null){
			imgList = (ListView) rootView.findViewById(R.id.image_list);
			imgList.setAdapter(new ImageListAdapter(rootView.getContext(),conceptModel, this));
			((ImageListAdapter)imgList.getAdapter()).setPictures(conceptModel.getPictures());
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && null != data) {
			conceptModel.addPicture(data.getData().toString());
			((ImageListAdapter)imgList.getAdapter()).setPictures(conceptModel.getPictures());
			initImageList();
	     }		
	}
	
	public ConceptModel getConceptModel() {
		return conceptModel;
	}

	public void setConceptModel(ConceptModel conceptModel) {
		this.conceptModel = conceptModel;
	}
}
