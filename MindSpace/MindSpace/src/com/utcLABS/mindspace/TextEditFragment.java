package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.EditText;




public class TextEditFragment extends Fragment {
		
	View rootView; 
	
	public TextEditFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.edit_slide, container,
				false);
		
		EditText title = (EditText) rootView.findViewById(R.id.new_title);
		title.setText("Relaxation");
		
		//EditText desc = (EditText) rootView.findViewById(R.id.editInput);
		//desc.setText("khfldkhgsldfhglsdfkhglsdkfhglskdfhglfdkhgdlfkhgsdlgh");
		
		
		return rootView;

	}

}
