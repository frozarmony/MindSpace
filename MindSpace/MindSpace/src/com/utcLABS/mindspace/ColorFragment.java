package com.utcLABS.mindspace;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.ImageButton;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.ConceptModel.MindSpaceShape;

public class ColorFragment extends Fragment {

	private ConceptModel conceptModel;
	
	public ColorFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_color, container,
				false);
		
		ImageButton rectangle = (ImageButton) rootView.findViewById(R.id.rectangle);
		rectangle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				conceptModel.setShape(MindSpaceShape.rectangle);		
			}
		});
		
		ImageButton ellipse = (ImageButton) rootView.findViewById(R.id.ellipse);
		ellipse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				conceptModel.setShape(MindSpaceShape.oval);		
			}
		});
		
		ImageButton rectangleArrondi = (ImageButton) rootView.findViewById(R.id.rectangle_arrondi);
		rectangleArrondi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				conceptModel.setShape(MindSpaceShape.roundedRectangle);		
			}
		});
		
		
		return rootView;
	}

	public ConceptModel getConceptModel() {
		return conceptModel;
	}

	public void setConceptModel(ConceptModel conceptModel) {
		this.conceptModel = conceptModel;
	}
}
