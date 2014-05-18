package com.utcLABS.mindspace.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.utcLABS.mindspace.model.ConceptModel;

public class ConceptView {
	
	// Constants
	private static final float			BRANCH_BASE_WIDTH = 500f;
	private static final float			BRANCH_BASE_HEIGHT = 50f;

	// Model Member
	private ConceptModel				model;
	
	// View Member
	private ConceptView					parentView;
	private LinkedList<ConceptView>		childrenViews;
	
	// Node
	private NodeView					nodeView;
	
	// Branch
	private View						branchView;
	private Paint						branchPaint;
	
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
	@SuppressLint("NewApi")
	public ConceptView(MindMapView mainView, ConceptModel model, ConceptView parentView) {
		super();
		
		// Init Model
		this.model = model;
		
		// Init View Member
		this.parentView = parentView;
		
		// Init NodeView
		initNodeView(mainView);
		
		// Init BranchView
		initBranchView(mainView);
		
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
		mainView.addViewToMap(nodeView);
	}
	
	@SuppressLint("NewApi")
	private void initBranchView(MindMapView mainView){
		if(parentView != null){
			// Geometry
			PointF relativeP = new PointF(parentView.model.getPosition().x-model.getPosition().x, parentView.model.getPosition().y-model.getPosition().y);
			
			Path branchPath = new Path();
			branchPath.moveTo(BRANCH_BASE_WIDTH, BRANCH_BASE_HEIGHT/2f);
			branchPath.lineTo(BRANCH_BASE_WIDTH*2f, BRANCH_BASE_HEIGHT/2f);
			
			PathShape pathShape = new PathShape(branchPath, BRANCH_BASE_WIDTH*2f, BRANCH_BASE_HEIGHT);
			ShapeDrawable branchDrawable = new ShapeDrawable(pathShape);
			
			this.branchPaint = branchDrawable.getPaint();
			this.branchPaint.setColor(model.getColor());
			this.branchPaint.setStyle(Paint.Style.STROKE);
			this.branchPaint.setStrokeWidth(BRANCH_BASE_HEIGHT/2f);
			
			this.branchView = new View(mainView.getContext());
			this.branchView.setBackground(branchDrawable);
			this.branchView.setScaleX(relativeP.length()/BRANCH_BASE_WIDTH);
			this.branchView.setScaleY(model.getSize()/ConceptModel.DEFAULT_SIZE);
			this.branchView.setRotation(getAngle(relativeP));
			this.branchView.setX(model.getPosition().x - BRANCH_BASE_WIDTH);
			this.branchView.setY(model.getPosition().y - BRANCH_BASE_HEIGHT/2f);
			
			mainView.addViewToMap(this.branchView, 0, new android.widget.FrameLayout.LayoutParams((int)(BRANCH_BASE_WIDTH*2f), (int)(BRANCH_BASE_HEIGHT)));
		}
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
				updateBranch((PointF) event.getNewValue());
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_POSITION, this.onPositionChanged);

		// OnSizeChanged
		this.onSizeChanged = new PropertyChangeListener() {
			@SuppressLint("NewApi")
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				float newSize = (float)event.getNewValue();
				nodeView.setTextSize(newSize);
				setNodePosition(new PointF(getX(), getY()));
				
				branchView.setScaleY(newSize/ConceptModel.DEFAULT_SIZE);
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_SIZE, this.onSizeChanged);
		
		// OnColorChanged
		this.onColorChanged = new PropertyChangeListener() {
			@SuppressLint("NewApi")
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				nodeView.shapeView.setColors(new int[]{(int)event.getNewValue(), Color.GRAY, (int)event.getNewValue()});
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_COLOR, this.onColorChanged);
		
		// TO DO Shape
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
	
	// Update Branch
	@SuppressLint("NewApi")
	private void updateBranch(PointF modelPos){
		if(parentView != null){
			// Geometry
			PointF relativeP = new PointF(parentView.model.getPosition().x-modelPos.x, parentView.model.getPosition().y-modelPos.y);
			
			// Update scale and rotation
			this.branchView.setScaleX(relativeP.length()/BRANCH_BASE_WIDTH);
			this.branchView.setRotation(getAngle(relativeP));
			this.branchView.setX(modelPos.x - BRANCH_BASE_WIDTH);
			this.branchView.setY(modelPos.y - BRANCH_BASE_HEIGHT/2f);
		}
	}
	
	/*
	 * Tool
	 */
	private static float getAngle(PointF vector) {
		float angle = (float) Math.toDegrees(Math.atan2(vector.y, vector.x));
		if(angle < 0){angle += 360;}
	    return angle;
	}
	
	/*
	 *  Node View
	 */
	public class NodeView extends TextView {
		
		// Member
		GradientDrawable	shapeView;

		// Constructor
		@SuppressLint("NewApi")
		public NodeView(Context context, ConceptModel model) {
			super(context);
			
			// Init TextView
			this.setText(model.getName());
			this.setTextSize(model.getSize());
			
			// Init Background
			this.shapeView = new GradientDrawable();
			this.shapeView.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
			this.shapeView.setStroke(1, Color.BLACK);
			this.shapeView.setColors(new int[]{model.getColor(), Color.GRAY, model.getColor()});
			this.configureShape(model.getShape());
			
			this.setBackground(this.shapeView);
		}

		// For First Update
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			setNodePosition(model.getPosition());
		}
		
		// Shape
		private void configureShape(ConceptModel.MindSpaceShape mindspaceShape){
			int padding;
			
			// Specific Config according to shape
			switch(mindspaceShape){
			case oval:
				padding = (int)(20f*model.getSize()/ConceptModel.DEFAULT_SIZE);
				this.shapeView.setShape(GradientDrawable.OVAL);
				this.setPadding(padding*2, padding/3, padding*2, padding/2);
				break;
			case rectangle:
				this.shapeView.setShape(GradientDrawable.RECTANGLE);
				padding = (int)(10f*model.getSize()/ConceptModel.DEFAULT_SIZE);
				this.setPadding(padding, padding, padding, padding);
				break;
			case roundedRectangle:
				this.shapeView.setShape(GradientDrawable.RECTANGLE);
				this.shapeView.setCornerRadius(20);
				padding = (int)(10f*model.getSize()/ConceptModel.DEFAULT_SIZE);
				this.setPadding(padding, padding, padding, padding);
				break;
			}
		}

	}
	
}
