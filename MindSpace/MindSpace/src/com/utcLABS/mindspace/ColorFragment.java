package com.utcLABS.mindspace;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.utcLABS.mindspace.model.ConceptModel;

public class ColorFragment extends Fragment {

	private ConceptModel conceptModel;
	
	public ColorFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_color, container,
				false);
		
		
		return rootView;
	}

	public ConceptModel getConceptModel() {
		return conceptModel;
	}

	public void setConceptModel(ConceptModel conceptModel) {
		this.conceptModel = conceptModel;
	}
}
