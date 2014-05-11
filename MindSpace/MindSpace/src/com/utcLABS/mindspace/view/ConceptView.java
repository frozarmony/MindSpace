package com.utcLABS.mindspace.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
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
	
	// Property Change Listener
	private PropertyChangeListener		onNameChanged;
	private PropertyChangeListener		onPositionChanged;
	private PropertyChangeListener		onSizeChanged;
	private PropertyChangeListener		onColorChanged;
	private PropertyChangeListener		onShapeChanged;

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
		
		// Init Listeners
		initPropertyChangeListeners(model);
    	
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
		nodeView.setX(this.getX() - nodeView.getWidth()/2);
		nodeView.setY(this.getY() - nodeView.getHeight()/2);
		mainView.addView(nodeView);
	}
	
	/*
	 * Listeners
	 */
	private void initPropertyChangeListeners(ConceptModel model){
		// OnNameChanged
		this.onNameChanged = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				nodeView.setText((String)event.getNewValue());
				setNodePosition(new PointF(getX(), getY()));
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_NAME, this.onNameChanged);
		
		// OnPositionChanged
		this.onPositionChanged = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				setNodePosition((PointF) event.getNewValue());
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_POSITION, this.onPositionChanged);

		// TO DO Size, Color, Shape
	}
	
	/*
	 * Geometry Method
	 */
	
	// Getters
	public float getX(){	return model.getPosition().x;	}
	public float getY(){	return model.getPosition().y;	}
	
	// Setters
	@SuppressLint("NewApi")
	private void setNodePosition(PointF p){
		nodeView.setX(p.x - nodeView.getWidth()/2);
		nodeView.setY(p.y - nodeView.getHeight()/2);
	}
	
	/*
	 *  Node View
	 */
	public class NodeView extends TextView {
		
		// Member
		GradientDrawable	shapeView;

		@SuppressLint("NewApi")
		public NodeView(Context context, ConceptModel model) {
			super(context);
			
			// Init TextView
			this.setText(model.getName());
			this.setTextSize(model.getSize());
			int padding = 20;
			this.setPadding(padding*2, padding/3, padding*2, padding/2);
			
			// Init Background
			this.shapeView = new GradientDrawable();
			this.shapeView.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
			this.shapeView.setStroke(3, Color.BLACK);
			this.shapeView.setShape(getShape(model.getShape()));
			this.shapeView.setColors(new int[]{model.getColor(), Color.LTGRAY, model.getColor()});
			
			this.setBackground(this.shapeView);
		}
		
		private int getShape(ConceptModel.MindSpaceShape mindspaceShape){
			switch(mindspaceShape){
			case oval:			return GradientDrawable.OVAL;
			case rectangle: 	return GradientDrawable.RECTANGLE;
			default:			return GradientDrawable.OVAL;
			}
		}

	}
	
}
