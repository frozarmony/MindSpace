package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.ListView;
import android.widget.TextView;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.utilities.ImageListAdapter;

public class SeeFragment extends Fragment {

	private View rootView;
	private ConceptModel currentConcept;
	private ListView imgList;
	
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
		
		imgList = (ListView) rootView.findViewById(R.id.image_list);
		imgList.setAdapter(new ImageListAdapter(rootView.getContext(),currentConcept, this));
		
		if(currentConcept!=null){
			if(currentConcept.getName()!=null){
				title.setText(currentConcept.getName());
			}
			if(currentConcept.getDescription()!=null){
				desc.setText(currentConcept.getDescription());
			}
			if(!currentConcept.getPictures().isEmpty()){
				((ImageListAdapter)imgList.getAdapter()).setPictures(currentConcept.getPictures());
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
