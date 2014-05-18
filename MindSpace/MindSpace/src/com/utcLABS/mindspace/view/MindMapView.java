package com.utcLABS.mindspace.view;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.ConceptModel.MindSpaceShape;

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
		
		float coef = 1f;
		
		// Creation Test ConceptModel
 		// Root
 		root = new ConceptModel(500f*coef+600f, 250f*coef+300f, null);
 		root.setName("Music");
 		root.setShape(MindSpaceShape.roundedRectangle);
 		root.setSize(ConceptModel.DEFAULT_SIZE*coef);
 		
 		// Sociability
 		ConceptModel sociability = new ConceptModel(200f*coef+600f, 400f*coef+300f, root);
 		sociability.setName("Sociability");
 		sociability.setColor(Color.rgb(50, 50, 200));
 		
 		// Titi
 		ConceptModel titi = new ConceptModel(100f*coef+600f, 475f*coef+300f, sociability);
 		titi.setName("People");
 		
 		// Creativity
 		ConceptModel creativity = new ConceptModel(250f*coef+600f, 125f*coef+300f, root);
 		creativity.setName("Creativity");
 		creativity.setColor(Color.rgb(50, 200, 50));
 		
 		// Rigour
 		ConceptModel rigour = new ConceptModel(750f*coef+600f, 125f*coef+300f, root);
 		rigour.setName("Rigour");
 		rigour.setColor(Color.rgb(200, 50, 50));
 		rigour.setShape(MindSpaceShape.oval);
 		
 		// Theory
 		ConceptModel theory = new ConceptModel(950f*coef+600f, 25f*coef+300f, rigour);
 		theory.setName("Theory");
 		
 		this.conceptView = new ConceptView(this, root, null);
 		
 		// OnTouchTest
 		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					root.getChildren().getFirst().setPosition(event.getX(),event.getY());
				}
				return false;
			}
		});
	}

}
