package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.EditText;
import android.widget.TextView;



public class TextEditFragment extends Fragment {
		
	View rootView; 
	
	public TextEditFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.edit_slide, container,
				false);
		
//		EditText title = (EditText) rootView.findViewById(R.id.title);
//		title.addTextChangedListener(new TextWatcher() {
//		 
//		   public void afterTextChanged(Editable s) {
//				((MainActivity)getActivity()).getCurrentConcept().setName(s.toString());
//		   }
//		 
//		   public void beforeTextChanged(CharSequence s, int start, 
//		     int count, int after) {
//		   }
//		 
//		   public void onTextChanged(CharSequence s, int start, 
//		     int before, int count) {
//		   
//		   }
//		  });
		return rootView;

	}

}
