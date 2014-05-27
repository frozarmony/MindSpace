package com.utcLABS.mindspace.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ext.R;
import android.widget.TextView;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.view.MindMapView.ScaleObject;


@SuppressLint("NewApi")
public class ConceptView {
	
	// Constants
	public static final String			MIMETYPE_CONCEPTVIEW		= "application/conceptview";
	
	private static final float			TEXT_BASE_SIZE				= 70f;
	private static final float			BRANCH_BASE_WIDTH			= 500f;
	private static final float			BRANCH_BASE_HEIGHT			= 50f;
	private static final int			CLOUD_BASE_SIZE				= 1000;
	private static final float			CLOUD_RATIO_HEIGHT			= 0.7f;
	

	// Model Member
	private ConceptModel				model;
	
	// View Member
	private boolean						isVisible;
	private MindMapView					mainView;
	private ConceptView					parentView;
	private ScaleObject					scaleFactor;
	
	// Node
	private NodeView					nodeView;
	
	// Branch
	private View						branchView;
	private Paint						branchPaint;
	
	// Cloud
	private View						cloudView;
	private GradientDrawable			cloudGrad;
	
	// Property Change Listener
	private PropertyChangeListener		onNameChanged;
	private PropertyChangeListener		onPositionChanged;
	private PropertyChangeListener		onSizeChanged;
	private PropertyChangeListener		onColorChanged;
	private PropertyChangeListener		onShapeChanged;
	private PropertyChangeListener		onMoved;
	
	// Controller Member
	private boolean						isMoving;
	
	// Controller Listener
	private View.OnTouchListener		onTouch;
	private View.OnLongClickListener	onLongClick;
	private View.OnDragListener			onDrag;
	
	/*
	 * Inititialisation
	 */
	
	// Constructor
	@SuppressLint("NewApi")
	public ConceptView(MindMapView mainView, ConceptModel model, ConceptView parentView, ScaleObject scaleFactor) {
		super();
		
		// Init Model
		this.model = model;
		
		// Init View Member
		this.isVisible = true;
		this.mainView = mainView;
		this.parentView = parentView;
		this.scaleFactor = scaleFactor;
		
		// Init NodeView
		initNodeView(mainView);
		
		// Init BranchView
		initBranchView(mainView);
		
		// Init CloudView
		initCloudView(mainView);
		
		// Init Model's Listeners
		initPropertyChangeListeners(model);
		
		// Init Controller's Listener's
		initControllerListener();
		
		// Update Visibility
		updateVisibility();
	}

	@SuppressLint("NewApi")
	private void initNodeView(MindMapView mainView){
		nodeView = new NodeView(mainView.getContext(), model);
		PointF pos = this.model.getPosition();
		nodeView.setX(pos.x - nodeView.getWidth()/2);
		nodeView.setY(pos.y - nodeView.getHeight()/2);
		mainView.addViewToMap(nodeView);
	}
	
	@SuppressLint("NewApi")
	private void initBranchView(MindMapView mainView){
			
		Path branchPath = new Path();
		branchPath.moveTo(BRANCH_BASE_WIDTH, BRANCH_BASE_HEIGHT/2f);
		branchPath.lineTo(BRANCH_BASE_WIDTH*2f, 0);
		branchPath.lineTo(BRANCH_BASE_WIDTH*2f, BRANCH_BASE_HEIGHT);
		branchPath.lineTo(BRANCH_BASE_WIDTH, BRANCH_BASE_HEIGHT/2f);
		
		PathShape pathShape = new PathShape(branchPath, BRANCH_BASE_WIDTH*2f, BRANCH_BASE_HEIGHT);
		ShapeDrawable branchDrawable = new ShapeDrawable(pathShape);
		
		this.branchPaint = branchDrawable.getPaint();
		this.branchPaint.setColor(model.getColor());
		this.branchPaint.setStyle(Paint.Style.FILL);
		//this.branchPaint.setStrokeWidth(BRANCH_BASE_HEIGHT/2f);
		
		this.branchView = new View(mainView.getContext());
		this.branchView.setBackground(branchDrawable);
		this.branchView.setX(model.getPosition().x - BRANCH_BASE_WIDTH);
		this.branchView.setY(model.getPosition().y - BRANCH_BASE_HEIGHT/2f);
		this.branchView.setScaleY(model.getSize());
				
		mainView.addViewToMap(this.branchView, mainView.getConceptsCount(), new android.widget.FrameLayout.LayoutParams((int)(BRANCH_BASE_WIDTH*2f), (int)(BRANCH_BASE_HEIGHT)));
		
		if(parentView == null)
			this.branchView.setVisibility(View.INVISIBLE);
		else
			updateBranch(this.model.getPosition());
	}
	
	@SuppressLint("NewApi")
	private void initCloudView(MindMapView mainView){
		
		// Init Gradient
		this.cloudGrad = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{model.getColor() & 0x88ffffff, model.getColor() & 0x00ffffff});
		this.cloudGrad.setShape(GradientDrawable.OVAL);
		this.cloudGrad.setSize(CLOUD_BASE_SIZE, CLOUD_BASE_SIZE);
		this.cloudGrad.setGradientType(GradientDrawable.RADIAL_GRADIENT);
		this.cloudGrad.setGradientRadius(CLOUD_BASE_SIZE/2f);
		this.cloudGrad.setGradientCenter(0.5f, 0.5f);
		
		// Init Cloud View
		this.cloudView = new View(this.mainView.getContext());
		this.cloudView.setBackground(this.cloudGrad);
		this.cloudView.setScaleX(model.getSize());
		this.cloudView.setScaleY(model.getSize()*CLOUD_RATIO_HEIGHT);

		// Add to mainView
		mainView.addViewToMap(this.cloudView, mainView.getConceptsCount(), new android.widget.FrameLayout.LayoutParams(CLOUD_BASE_SIZE,CLOUD_BASE_SIZE));
		
		// Set Position
		this.cloudView.setX(this.model.getPosition().x - CLOUD_BASE_SIZE/2f);
		this.cloudView.setY(this.model.getPosition().y - CLOUD_BASE_SIZE/2f);
	}
	
	/*
	 * Model's Listeners
	 */
	private void initPropertyChangeListeners(ConceptModel model){
		// OnNameChanged
		this.onNameChanged = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				nodeView.setText((String)event.getNewValue());
				setNodePosition(ConceptView.this.model.getPosition());
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
				float newSize = (Float)event.getNewValue();
				nodeView.setTextSize(newSize*TEXT_BASE_SIZE);
				setNodePosition(ConceptView.this.model.getPosition());
				
				Log.d("ConceptView("+ ConceptView.this.model.getName()+")", "OnSizeChanged : " + newSize);
				
				branchView.setScaleY(newSize);
				
				cloudView.setScaleX(newSize);
				cloudView.setScaleY(newSize*CLOUD_RATIO_HEIGHT);
				
				updateVisibility();
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_SIZE, this.onSizeChanged);
		
		// OnColorChanged
		this.onColorChanged = new PropertyChangeListener() {
			@SuppressLint("NewApi")
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				int color = (Integer)event.getNewValue();
				nodeView.shapeView.setColors(new int[]{color, Color.LTGRAY, color});
				
				branchPaint.setColor(color);
				branchView.invalidate();
				
				cloudGrad.setColors(new int[]{color & 0x88ffffff, color & 0x00ffffff});
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_COLOR, this.onColorChanged);
		
		// OnShapeChanged
		this.onShapeChanged = new PropertyChangeListener() {
			@SuppressLint("NewApi")
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				ConceptModel.MindSpaceShape newShape = (ConceptModel.MindSpaceShape)event.getNewValue();
				ConceptView.this.nodeView.configureShape(newShape);
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_SHAPE, this.onShapeChanged);
		
		// OnMoved
		this.onMoved = new PropertyChangeListener() {
			@SuppressLint("NewApi")
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				ConceptModel newParent = (ConceptModel)event.getNewValue();
				
				if( newParent != null ){
 					Log.d("ConceptView("+ ConceptView.this.model.getName()+")", "Moved to " + newParent.getName());
					ConceptView newParentView = mainView.searchViewOfModel(newParent);
					parentView = newParentView;
					updateBranch(ConceptView.this.model.getPosition());
				}
				else{
					parentView = null;
					branchView.setVisibility(View.INVISIBLE);
				}
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_MOVE, this.onMoved);
	}

	
	/*
	 *	Controller's Listeners
	 */
	private void initControllerListener(){
		
		// OnClickListener
		this.onTouch = new View.OnTouchListener() {
			
			// Member
			private PointF startPos;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN :
					startPos = new PointF(event.getX(),event.getY());
					break;
				case MotionEvent.ACTION_MOVE :
					// Compute Offset
					PointF offset = new PointF(event.getX()-startPos.x, event.getY()-startPos.y);
					
					if(offset.length()>10f){	// TODO Enhance this
						isMoving = true;
						PointF currPosition = model.getPosition();
						model.setPosition(currPosition.x+event.getX()-startPos.x, currPosition.y+event.getY()-startPos.y);
						return true;
					}
					break;
				case MotionEvent.ACTION_UP :
					startPos = null;
					isMoving = false;
					break;
				}
				
				return false;
			}
		};
		this.nodeView.setOnTouchListener(this.onTouch);
				
		// OnLongClickListener
		this.onLongClick = new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if( !isMoving ){
					// Create ClipData
					ClipData.Item item =  new ClipData.Item( model.getName() );
					ClipData dragData = new ClipData(model.getName(), new String[]{ConceptView.MIMETYPE_CONCEPTVIEW},item);
					
					// Create DragShadow
					View.DragShadowBuilder myShadow = new MyDragShadowBuilder(v);
					   
					// Modify Current View
					//nodeView.setVisibility(View.INVISIBLE);
					//branchView.setVisibility(View.INVISIBLE);
					
					return v.startDrag(dragData, myShadow, ConceptView.this, 0);
				}
				return false;
			}
		};
		this.nodeView.setOnLongClickListener(this.onLongClick);
		
		// OnDragListener
		this.onDrag = new View.OnDragListener() {
			
			@Override
			public boolean onDrag(View v, DragEvent event) {
				
				switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					if( !event.getClipDescription().hasMimeType(MIMETYPE_CONCEPTVIEW) )
						return false;
					break;
 				case DragEvent.ACTION_DRAG_ENTERED:
 					setSelectMode(true);
 					break;
 				case DragEvent.ACTION_DRAG_EXITED:
 					setSelectMode(false);
 					break;
 				case DragEvent.ACTION_DROP:
 					Log.d("ConceptView("+model.getName()+")", "Action Drop");
 					ConceptView conceptView = (ConceptView) event.getLocalState();
 					conceptView.getModel().moveTo(getModel());
 					setSelectMode(false);
 					break;
 				case DragEvent.ACTION_DRAG_ENDED:
 					Log.d("ConceptView("+model.getName()+")", "Action Ended");
 					break;
 				}
				
	 			return true;
			}
		};
		this.nodeView.setOnDragListener(this.onDrag);
		
	}
	
	/*
	 * View Method
	 */
	
	// Getters
	public ConceptModel		getModel(){		return model;					}
	
	// Visual Effects
	protected void setSelectMode(boolean select){
		this.nodeView.setSelectMode(select);
	}

	protected void updateVisibility(){
		// Init
		float relativeSize		= model.getSize() * scaleFactor.getScale();
		float minRelativeSize	= 1f - mainView.getDensity();
		
		Log.d("ConceptView("+ model.getName()+")", "Check Visibility " + relativeSize + " < " + minRelativeSize);
		
		if( model.getParent() != null && relativeSize < minRelativeSize ){
			if( this.isVisible ){
				this.isVisible = false;
				this.nodeView.setVisibility(View.INVISIBLE);
				this.cloudView.setVisibility(View.INVISIBLE);
			}
			if( model.getParent().getSize() * scaleFactor.getScale() < minRelativeSize )
				this.branchView.setVisibility(View.INVISIBLE);
		}
		else{
			if( !this.isVisible ){
				this.isVisible = true;
				this.nodeView.setVisibility(View.VISIBLE);
				this.updateBranch(this.model.getPosition());
				this.cloudView.setVisibility(View.VISIBLE);
			}
		}
	}
	
	// Move Node
	@SuppressLint("NewApi")
	private void setNodePosition(PointF p){
		this.nodeView.setX(p.x - nodeView.getWidth()/2);
		this.nodeView.setY(p.y - nodeView.getHeight()/2);
		this.cloudView.setX(p.x - CLOUD_BASE_SIZE/2f);
		this.cloudView.setY(p.y - CLOUD_BASE_SIZE/2f);
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
			
			// Show BranchView
			if( isVisible )
				this.branchView.setVisibility(View.VISIBLE);
			Log.d("ConceptView("+model.getName()+")", "Updated BranchView");
		}
	}
	
	// Remove all ConceptView's subview from MindMapView
	protected void detachView(){
		Log.d("ConceptView("+ model.getName()+")", "Detach View");
		
		// Detach from mainView
		mainView.removeViewFromMap(this.branchView);
		mainView.removeViewFromMap(this.nodeView);
		mainView.removeViewFromMap(this.cloudView);
		
		// Detach from model
		model.removePropertyChangeListener(ConceptModel.NP_NAME, this.onNameChanged);
		model.removePropertyChangeListener(ConceptModel.NP_POSITION, this.onPositionChanged);
		model.removePropertyChangeListener(ConceptModel.NP_SIZE, this.onSizeChanged);
		model.removePropertyChangeListener(ConceptModel.NP_COLOR, this.onColorChanged);
		model.removePropertyChangeListener(ConceptModel.NP_MOVE, this.onMoved);
	}
	
	/*
	 *  Node View
	 */
	private class NodeView extends TextView {
		
		/*
		 *  Member
		 */
		private GradientDrawable			shapeView;
		
		/*
		 *	Constructor
		 */
		@SuppressLint("NewApi")
		public NodeView(Context context, ConceptModel model) {
			super(context);
			
			// Init TextView
			this.setText(model.getName());
			this.setTextSize(model.getSize()*TEXT_BASE_SIZE);
			
			// Init Background
			this.shapeView = new GradientDrawable();
			this.shapeView.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
			this.shapeView.setStroke(1, Color.BLACK);
			this.shapeView.setColors(new int[]{model.getColor(), Color.LTGRAY, model.getColor()});
			this.configureShape(model.getShape());
			
			this.setBackground(this.shapeView);
		}
		
		public void setSelectMode(boolean select){
			if(select)
				this.shapeView.setColorFilter(0x77ffffff, PorterDuff.Mode.SRC_ATOP);
			else
				this.shapeView.clearColorFilter();
		}
		
		// For First Update
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			setNodePosition(model.getPosition());
		}
		
		// Shape Configuration
		private void configureShape(ConceptModel.MindSpaceShape mindspaceShape){
			int padding;
			
			// Specific Config according to shape
			switch(mindspaceShape){
			case oval:
				padding = (int)(20f*model.getSize());
				this.shapeView.setShape(GradientDrawable.OVAL);
				this.setPadding(padding*2, padding/3, padding*2, padding/2);
				break;
			case rectangle:
				this.shapeView.setShape(GradientDrawable.RECTANGLE);
				padding = (int)(10f*model.getSize());
				this.setPadding(padding, padding, padding, padding);
				break;
			case roundedRectangle:
				this.shapeView.setShape(GradientDrawable.RECTANGLE);
				this.shapeView.setCornerRadius(20);
				padding = (int)(10f*model.getSize());
				this.setPadding(padding, padding, padding, padding);
				break;
			}
		}

	}
	
	/*
	 *  Drag Shadow Builder
	 */
	private class MyDragShadowBuilder extends View.DragShadowBuilder {

	    // The drag shadow image, defined as a drawable thing
	    private GradientDrawable shadow =  new GradientDrawable();
	    private Drawable enterShape = nodeView.getResources().getDrawable(R.drawable.drag_shadow);
	    private int marginShadow = 10;
	    private int width, height;
	    
        public MyDragShadowBuilder(View v) {
            super(v);
            this.shadow = new GradientDrawable();
			this.shadow.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
			this.shadow.setStroke(1, Color.BLACK);
			this.shadow.setColors(new int[]{ Color.TRANSPARENT, Color.GRAY, Color.TRANSPARENT});
			//v.setBackground(enterShape);
			
        }

        @Override
        public void onProvideShadowMetrics (Point size, Point touch){
            width = getView().getWidth() +2*marginShadow;
            height = getView().getHeight() +2*marginShadow;
            width*=scaleFactor.scaleFactor;
            height*=scaleFactor.scaleFactor;
            shadow.setBounds(0, 0, width, height);
            size.set(width, height);
            touch.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) { // TODO
        	//shadow.draw(canvas);
        	
        	//enterShape.draw(canvas);
        	//System.out.println(enterShape.getIntrinsicHeight());
        	canvas.save();
        	canvas.translate(marginShadow*scaleFactor.scaleFactor, marginShadow*scaleFactor.scaleFactor);
        	canvas.scale(scaleFactor.scaleFactor, scaleFactor.scaleFactor);
        	
        	nodeView.draw(canvas);
            canvas.restore();
            
        }
	}
	
	
	/*
	 *  Tools
	 */
	
	private static float getAngle(PointF vector) {
		float angle = (float) Math.toDegrees(Math.atan2(vector.y, vector.x));
		if(angle < 0){angle += 360;}
	    return angle;
	}
	
}
