package com.utcLABS.mindspace.view;

import com.utcLABS.mindspace.model.ConceptModel;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MindMapView extends RelativeLayout  {

	// Member
	private ConceptView				conceptView;
	
	@SuppressLint("NewApi")
	public MindMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Creation Test ConceptModel
 		// Root
 		ConceptModel root = new ConceptModel(500f, 250f, null);
 		root.setName("Music");
 		
 		// Creativity
 		ConceptModel creativity = new ConceptModel(250f, 125f, null);
 		creativity.setName("Creativity");
 		root.addChildNode(creativity);
 		
 		// Rigour
 		ConceptModel rigour = new ConceptModel(750f, 125f, null);
 		rigour.setName("Rigour");
 		root.addChildNode(rigour);
 		
 		// Sociability
 		ConceptModel sociability = new ConceptModel(200f, 400f, null);
 		sociability.setName("Sociability");
 		root.addChildNode(sociability);
 		
 		// Titi
 		ConceptModel titi = new ConceptModel(100f, 475f, null);
 		titi.setName("People");
 		sociability.addChildNode(titi);
 		
 		this.conceptView = new ConceptView(this, root, null);
	}

}
