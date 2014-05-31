package com.utcLABS.mindspace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ext.R;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;

public class ColorFragment extends Fragment {

	public ColorFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_color, container,
				false);
		
		ColorPicker picker = (ColorPicker)rootView.findViewById(R.id.picker);
		
		picker.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				System.out.println(color);	
				String hexColor = String.format("#%06X", (0xFFFFFF & color));
				System.out.println(hexColor);	

			}
			
		});
		return rootView;
	}
	
}
	
