package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.R;

import com.larswerkman.holocolorpicker.ColorPicker;

public class ColorFragment extends Fragment {

	public ColorFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_color, container,
				false);
		
		ColorPicker picker = (ColorPicker)rootView.findViewById(R.id.picker);
		return rootView;
	}
}
