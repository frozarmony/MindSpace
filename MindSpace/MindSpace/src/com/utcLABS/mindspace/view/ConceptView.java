package com.utcLABS.mindspace.view;

import java.util.LinkedList;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;
import com.utcLABS.mindspace.model.ConceptModel;
import com.utcSABB.mindspace.R;

public class ConceptView {

	// Model Member
	private ConceptModel				model;
	
	// View Member
	private ConceptView					parentView;
	private LinkedList<ConceptView>		childrenViews;
	
	// Node
	private NodeView					nodeView;	

	/*
	 * Inititialisation
	 */
	
	// Constructor
	public ConceptView(MindMapView mainView, ConceptModel model, ConceptView parentView) {
		super();
		
		// Init Model
		this.model = model;
		
		// Init View Member
		this.parentView = parentView;
		
		// Init NodeView
		initNodeView(mainView);		
    	
    	// Children Views
    	this.childrenViews = new LinkedList<ConceptView>();
    	for( ConceptModel m : model.getChildren() ){
    		ConceptView childView = new ConceptView(mainView, m, this);
    		this.childrenViews.add(childView);
    	}
	}

	@SuppressLint("NewApi")
	private void initNodeView(MindMapView mainView){
		nodeView = new NodeView(mainView.getContext(), model);
		nodeView.setText(model.getName());
		nodeView.setX(this.getX() - nodeView.getWidth()/2);
		nodeView.setY(this.getY() - nodeView.getHeight()/2);
		mainView.addView(nodeView);
	}
	
	/*
	 * Geometry Info
	 */
	
	// Getters
	public float getX(){	return model.getPosition().x;	}
	public float getY(){	return model.getPosition().y;	}
	
	// Node View without link
	public class NodeView extends TextView {
		
		// Member
		Drawable shape;

		@SuppressLint("NewApi")
		public NodeView(Context context, ConceptModel model) {
			super(context);
			
			Resources res = getResources();
			shape = res.getDrawable(R.drawable.nodeshape);
			this.setTextSize(64);
			this.setBackground(shape);
		}

	}
	
}
