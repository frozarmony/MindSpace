package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.ImageButton;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.ConceptModel.MindSpaceShape;

public class ColorFragment extends Fragment {

	private View rootView;
	private ColorPicker picker;
	private ConceptModel conceptModel;

	public ColorFragment() {

	}
	
	public static ColorFragment newInstance(ConceptModel currentConcept) {
		ColorFragment fragment = new ColorFragment();
		Bundle args = new Bundle();
		args.putParcelable("currentConcept", currentConcept);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_color, container,false);
		
		Bundle args = getArguments();
		conceptModel = args.getParcelable("currentConcept");
				
		ColorPicker picker = (ColorPicker)rootView.findViewById(R.id.picker);
		
		picker.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				System.out.println("ColorPicker " + color);
				conceptModel.setColor(color);

			}
		});

		ImageButton rectangle = (ImageButton) rootView
				.findViewById(R.id.rectangle);
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

		ImageButton rectangleArrondi = (ImageButton) rootView
				.findViewById(R.id.rectangle_arrondi);
		rectangleArrondi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				conceptModel.setShape(MindSpaceShape.roundedRectangle);
			}
		});
		
		initFragment(conceptModel);

		return rootView;
	}

	public ConceptModel getConceptModel() {
		return conceptModel;
	}

	public void setConceptModel(ConceptModel conceptModel) {
		this.conceptModel = conceptModel;
	}

	public void initFragment(ConceptModel currentConcept) {
		this.conceptModel = currentConcept;
		if(conceptModel!=null && picker!=null){
			picker.setOldCenterColor(conceptModel.getColor());
		}
	}

}
