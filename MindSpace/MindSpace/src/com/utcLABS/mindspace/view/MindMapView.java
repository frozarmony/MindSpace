package com.utcLABS.mindspace.view;

import com.utcLABS.mindspace.model.ConceptModel;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MindMapView extends RelativeLayout  {

	// Member
	private ConceptModel			root;
	private ConceptView				conceptView;
	
	@SuppressLint("NewApi")
	public MindMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Creation Test ConceptModel
 		// Root
 		root = new ConceptModel(500f, 250f, null);
 		root.setName("Music");
 		
 		// Creativity
 		ConceptModel creativity = new ConceptModel(250f, 125f, root);
 		creativity.setName("Creativity");
 		creativity.setColor(Color.rgb(50, 200, 50));
 		
 		// Rigour
 		ConceptModel rigour = new ConceptModel(750f, 125f, root);
 		rigour.setName("Rigour");
 		rigour.setColor(Color.rgb(200, 50, 50));
 		
 		// Theory
 		ConceptModel theory = new ConceptModel(950f, 25f, rigour);
 		theory.setName("Theory");
 		
 		// Sociability
 		ConceptModel sociability = new ConceptModel(200f, 400f, root);
 		sociability.setName("Sociability");
 		sociability.setColor(Color.rgb(50, 50, 200));
 		
 		// Titi
 		ConceptModel titi = new ConceptModel(100f, 475f, sociability);
 		titi.setName("People");
 		
 		this.conceptView = new ConceptView(this, root, null);
 		
 		// OnTouchTest
 		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					root.setPosition(event.getX(),event.getY());
				}
				return false;
			}
		});
	}

}
