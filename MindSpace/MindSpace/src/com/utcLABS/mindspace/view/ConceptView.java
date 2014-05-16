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
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
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
	
	// Branch
	private View						branchView;
	private Path						branchPath;
	
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
		if(parentView != null){
			// Geometry
			PointF relativeP = new PointF(parentView.model.getPosition().x-model.getPosition().x, parentView.model.getPosition().y-model.getPosition().y);
			float width = Math.abs(relativeP.x) * 2.0f;
			float height = Math.abs(relativeP.y) * 2.0f;
			
			// Prepare Shape
			this.branchPath = new Path();
			this.branchPath.addRect(0, 0, width, height, Direction.CCW);
			this.branchPath.moveTo(width*0.5f, height*0.5f);
			this.branchPath.lineTo(relativeP.x+width*0.5f, relativeP.y+height*0.5f);
			PathShape pathShape = new PathShape(branchPath, width, height);
			ShapeDrawable shapeDrawable = new ShapeDrawable(pathShape);
			shapeDrawable.getPaint().setColor(Color.BLACK);
			shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
			shapeDrawable.getPaint().setStrokeWidth(4);
			//shapeDrawable.setBounds(0, 0, , 1);
			
			this.branchView = new View(mainView.getContext());
			this.branchView.setBackground(shapeDrawable);
			this.branchView.setX(model.getPosition().x - width*0.5f );
			this.branchView.setY(model.getPosition().y - height*0.5f );
			mainView.addView(this.branchView, (int)width, (int)height);
			
			/*Paint p = new Paint();
			p.setColor(Color.BLACK);
			p.setStyle(Paint.Style.STROKE);
			p.setStrokeWidth(4);*/
		}
		
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

		// OnSizeChanged
		this.onSizeChanged = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				nodeView.setTextSize((float)event.getNewValue());
				setNodePosition(new PointF(getX(), getY()));
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
			
			// Init Background
			this.shapeView = new GradientDrawable();
			this.shapeView.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
			this.shapeView.setStroke(3, Color.BLACK);
			this.shapeView.setColors(new int[]{model.getColor(), Color.GRAY, model.getColor()});
			this.configureShape(model.getShape());
			
			this.setBackground(this.shapeView);
		}
		
		private void configureShape(ConceptModel.MindSpaceShape mindspaceShape){
			int padding;
			
			// Specific Config according to shape
			switch(mindspaceShape){
			case oval:
				padding = 20;
				this.shapeView.setShape(GradientDrawable.OVAL);
				this.setPadding(padding*2, padding/3, padding*2, padding/2);
				break;
			case rectangle:
				this.shapeView.setShape(GradientDrawable.RECTANGLE);
				padding = 10;
				this.setPadding(padding, padding, padding, padding);
				break;
			case roundedRectangle:
				this.shapeView.setShape(GradientDrawable.RECTANGLE);
				this.shapeView.setCornerRadius(20);
				padding = 10;
				this.setPadding(padding, padding, padding, padding);
				break;
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			setNodePosition(model.getPosition());
		}
		
		

	}
	
}
