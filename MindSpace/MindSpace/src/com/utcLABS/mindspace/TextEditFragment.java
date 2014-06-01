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

import com.utcLABS.mindspace.model.ConceptModel;

import com.utcLABS.mindspace.model.ConceptModel;




public class TextEditFragment extends Fragment {
		
	private View rootView; 
	private ConceptModel conceptModel;
	private EditText desc;
	private EditText title;
	
	public TextEditFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.edit_slide, container,false);
		title = (EditText) rootView.findViewById(R.id.new_title);
		desc = (EditText) rootView.findViewById(R.id.editInput);

		title.addTextChangedListener(new TextWatcher() {
		 
		   public void afterTextChanged(Editable s) {
				conceptModel.setName(s.toString());
		   }
		 
		   public void beforeTextChanged(CharSequence s, int start, 
		     int count, int after) {
		   }
		 
		   public void onTextChanged(CharSequence s, int start, 
		     int before, int count) {
		   
		   }
		  });

		return rootView;

	}
	
	public void initFragment(ConceptModel currentConcept){
		this.conceptModel = currentConcept;
		if(conceptModel!=null){
			if(conceptModel.getName()!=null){
				title.setText(conceptModel.getName());
			}
			if(conceptModel.getDescription()!=null){
				desc.setText(conceptModel.getDescription());

			}
		}
		
	}

	public View getRootView() {
		return rootView;
	}

	public void setRootView(View rootView) {
		this.rootView = rootView;
	}

	public ConceptModel getConceptModel() {
		return conceptModel;
	}

	public void setConceptModel(ConceptModel conceptModel) {
		this.conceptModel = conceptModel;
	}

}
