package com.utcLABS.mindspace;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.EditionActivity;
import android.view.ext.R;
import android.widget.Button;
import android.widget.ImageView;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.MindMapXmlParser;

public class PictureEditFragment extends Fragment {

	private int RESULT_LOAD_IMAGE = 1;
	View rootView;
	ConceptModel conceptModel;

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
		
		initFragment();
		
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
	
	public void initFragment(){
		if(conceptModel!=null){
			if(conceptModel.getOnlyPicture()!=null && !conceptModel.getOnlyPicture().equals("")){
				Uri path = Uri.parse(conceptModel.getOnlyPicture());
				ImageView imgcover = (ImageView) rootView.findViewById(R.id.imgView);
				imgcover.setImageURI(path);
			}	
		}	
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && null != data) {
	         ImageView imgcover = (ImageView) rootView.findViewById(R.id.imgView);
	         imgcover.setImageURI(data.getData());
	         conceptModel.setOnlyPicture(data.getData().toString());
	     }		
	}
	
	public ConceptModel getConceptModel() {
		return conceptModel;
	}

	public void setConceptModel(ConceptModel conceptModel) {
		this.conceptModel = conceptModel;
	}
}
