package com.utcLABS.mindspace;


import com.utcLABS.mindspace.model.ConceptModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.Button;
import android.widget.ImageView;

public class PictureEditFragment extends Fragment {

	private int RESULT_LOAD_IMAGE = 1;
	View rootView;
	
	public PictureEditFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.edit_picture, container,
				false);
		
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && null != data) {
	         ImageView imgcover = (ImageView) rootView.findViewById(R.id.imgView);
	         imgcover.setImageURI(data.getData());
	     }
		
		//ConceptModel conceptToEdit = ((MainActivity)getActivity()).getCurrentConcept();
		
	}
}
