package com.utcLABS.mindspace.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
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
import android.widget.TextView;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.view.MindMapView.ScaleObject;
import com.utcSABB.mindspace.R;

@SuppressLint("NewApi")
public class ConceptView {
	
	// Constants
	public static final String			MIMETYPE_CONCEPTVIEW		= "application/conceptview";
	
	private static final float			BRANCH_BASE_WIDTH			= 500f;
	private static final float			BRANCH_BASE_HEIGHT			= 50f;
	

	// Model Member
	private ConceptModel				model;
	
	// View Member
	private MindMapView					mainView;
	private ConceptView					parentView;
	private ScaleObject					scaleFactor;
	
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
	//private PropertyChangeListener		onShapeChanged; TODO
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
	public ConceptView(MindMapView mainView, ConceptModel model, ConceptView parentView, ScaleObject _scaleFactor) {
		super();
		
		// Init Model
		this.model = model;
		
		// Init View Member
		this.mainView = mainView;
		this.parentView = parentView;
		this.scaleFactor = _scaleFactor;
		
		// Init NodeView
		initNodeView(mainView);
		
		// Init BranchView
		initBranchView(mainView);
		
		// Init Model's Listeners
		initPropertyChangeListeners(model);
		
		// Init Controller's Listener's
		initControllerListener();
		
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
		this.branchView.setX(model.getPosition().x - BRANCH_BASE_WIDTH);
		this.branchView.setY(model.getPosition().y - BRANCH_BASE_HEIGHT/2f);
				
		mainView.addViewToMap(this.branchView, 0, new android.widget.FrameLayout.LayoutParams((int)(BRANCH_BASE_WIDTH*2f), (int)(BRANCH_BASE_HEIGHT)));
		
		if(parentView == null)
			this.branchView.setVisibility(View.INVISIBLE);
		else
			updateBranch(parentView.model.getPosition());
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
				float newSize = (Float)event.getNewValue();
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
				int color = (Integer)event.getNewValue();
				nodeView.shapeView.setColors(new int[]{color, Color.LTGRAY, color});
				branchPaint.setColor(color);
			}
		};
		model.addPropertyChangeListener(ConceptModel.NP_COLOR, this.onColorChanged);
		
		// TO DO Shape
		
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
					updateBranch(newParent.getPosition());
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
 					//((ConceptView) event.getLocalState()).endDropAction();
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
	public float			getX(){			return model.getPosition().x;	}
	public float			getY(){			return model.getPosition().y;	}
	public ConceptModel		getModel(){		return model;					}
	
	// Visual Effects
	protected void setSelectMode(boolean select){
		this.nodeView.setSelectMode(select);
	}
	
	/*protected void endDropAction(){
		if( this.nodeView.getVisibility() != View.VISIBLE )
			this.nodeView.setVisibility(View.VISIBLE);
	}*/
	
	// Move Node
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
			
			// Show BranchView
			this.branchView.setVisibility(View.VISIBLE);
			Log.d("ConceptView("+model.getName()+")", "Updated BranchView");
		}
	}
	
	// Remove all ConceptView's subview from MindMapView
	protected void removeSubViews(){
		mainView.removeViewFromMap(this.branchView);
		mainView.removeViewFromMap(this.nodeView);
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
			this.setTextSize(model.getSize());
			
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
