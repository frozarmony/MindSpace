package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.EditText;

import com.utcLABS.mindspace.model.ConceptModel;




public class TextEditFragment extends Fragment {
		
	private View rootView; 
	private ConceptModel conceptModel;
	private EditText desc;
	private EditText title;
	private Boolean createdListener = false;
	
	public TextEditFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.edit_slide, container,false);
		title = (EditText) rootView.findViewById(R.id.new_title);
		desc = (EditText) rootView.findViewById(R.id.editInput);
//
//		title.addTextChangedListener(new TextWatcher() {
//			 
//			   public void afterTextChanged(Editable s) {
//				   	System.out.println(title.getText().toString());
//					conceptModel.setName(s.toString());
//			   }
//			 
//			   public void beforeTextChanged(CharSequence s, int start, 
//			     int count, int after) {
//			   }
//			 
//			   public void onTextChanged(CharSequence s, int start, 
//			     int before, int count) {
//			   
//			   }
//		});
		initFragment(conceptModel);

		return rootView;
		

	}
	
	public void initFragment(ConceptModel currentConcept){
		this.conceptModel = currentConcept;
		if(conceptModel!=null){
			if(conceptModel.getName()!=null){
				title.setText(conceptModel.getName());
			}else {
				title.setText(null);
			}
			if(conceptModel.getDescription()!=null){
				desc.setText(conceptModel.getDescription());
			}else {
				desc.setText(null);
			}
		}else {
			title.setText(null);
			desc.setText(null);
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
		initFragment(conceptModel);
	}

}
