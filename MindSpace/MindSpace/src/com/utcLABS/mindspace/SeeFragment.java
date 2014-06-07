package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.TextView;

import com.utcLABS.mindspace.model.ConceptModel;

public class SeeFragment extends Fragment {

	private View rootView;
	private ConceptModel currentConcept;
	
	public SeeFragment(){
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.see_fragment, container,false);

		
		return rootView;
	}
	
	public void initFragment(ConceptModel currentConcept){
		this.currentConcept = currentConcept;
		TextView title = (TextView) rootView.findViewById(R.id.see_title);
		TextView desc = (TextView) rootView.findViewById(R.id.see_desc);
		if(currentConcept!=null){
			if(currentConcept.getName()!=null){
				title.setText(currentConcept.getName());
			}
			if(currentConcept.getDescription()!=null){
				desc.setText(currentConcept.getDescription());

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
		return currentConcept;
	}

	public void setConceptModel(ConceptModel conceptModel) {
		this.currentConcept = conceptModel;
	}
}
