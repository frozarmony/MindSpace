package com.utcLABS.mindspace;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
//	         Uri selectedImage = data.getData();
//	         String[] filePathColumn = { MediaStore.Images.Media.DATA };
//	 
//	         Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//	                 filePathColumn, null, null, null);
//	         cursor.moveToFirst();
//	 
//	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//	         String picturePath = cursor.getString(columnIndex);
	         ImageView imgcover = (ImageView) rootView.findViewById(R.id.imgView);
	         imgcover.setImageURI(data.getData());
//	         cursor.close();
	         
         
	         // String picturePath contains the path of selected Image
	     }
	}
}
