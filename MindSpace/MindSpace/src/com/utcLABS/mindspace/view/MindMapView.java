package com.utcLABS.mindspace.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.MindMapModel;

@SuppressLint("NewApi") public class MindMapView extends FrameLayout  {
	
	/*
	 * Constant
	 */
	public final static float						MIN_SCALE							= 0.7f;
	public final static float						CREATION_POS_RATIO					= 0.15f;
	
	/*
	 * Member
	 */
	
	// Model Reference
	private MindMapModel							mindMapModel;
	
	// Interface Reference
	private Fragment 								currentFragment;
	
	// View Members
	private HashMap<ConceptModel, ConceptView>		conceptIndex;
	private RelativeLayout							mapView;
	private ScaleObject								scale;
	private float									density;
	
	// Property Change Listeners
	private PropertyChangeListener					onConceptCreated;
	private PropertyChangeListener					onConceptDeleted;
	
	// Test Members
	//private ConceptModel							root;
	
	// Controller Members
	private boolean									editMode;
	
	// Controller Listeners
	private ScaleGestureDetector					scaleDetector;
	private OnTouchListener							onTouch;
	private OnDragListener							onDrag;

	/*
	 * Constructor
	 */
	public MindMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Init Model Reference
		this.mindMapModel = new MindMapModel();
		
		// Init View Member
		this.conceptIndex = new HashMap<ConceptModel, ConceptView>();
		
		this.mapView = new RelativeLayout(context);
		this.mapView.setBackgroundColor(0xffeeeeee);
		this.addView(this.mapView, 4000, 4000);
		
		// Init Scale & Density
		this.scale = new ScaleObject();
		this.density = 0.5f;
		this.mapView.setPivotX(0f);
		this.mapView.setPivotY(0f);
		this.mapView.setScaleX(scale.scaleFactor);
		this.mapView.setScaleY(scale.scaleFactor);
		
		// Init Controller Member
		this.editMode = true;		
		
		// Listeners
 		// Init Controller's Listeners
 		initControllerListeners();
 		
 		// Update Concepts Visibility
 		updateConceptsVisibility();
	}
	
	/*
	 * Model's Listeners
	 */
	private void initPropertyChangeListeners(MindMapModel model){
		
		// OnConceptCreated
		this.onConceptCreated = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// Create Concept View
				ConceptModel model = (ConceptModel)event.getNewValue();
				ConceptModel parent = model.getParent();
				ConceptView parentView = null;
				
				if( parent != null )
					parentView = conceptIndex.get(model.getParent());
				
				ConceptView view = new ConceptView(MindMapView.this, model, parentView, scale);
				
				// Index Concept View
				conceptIndex.put(model, view);
			}
		};
		model.addPropertyChangeListener(MindMapModel.NP_CONCEPT_CREATED, this.onConceptCreated);
		
		// OnConceptDeleted
		this.onConceptDeleted = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// Delete Concept View if exist
				ConceptModel model = (ConceptModel)event.getNewValue();
				ConceptView view = conceptIndex.get(model);
				
				if( view != null ){
					view.detachView();
					conceptIndex.remove(model);
				}
			}
		};
		model.addPropertyChangeListener(MindMapModel.NP_CONCEPT_DELETED, this.onConceptDeleted);
	}
		
	/*
	 *	Controller's Listeners
	 */
	private void initControllerListeners(){
		
		// OnTouchListener
		this.onTouch = new View.OnTouchListener() {
			
			private boolean scaled = false;
			private float lastX = 0f;
			private float lastY = 0f;
			
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				float _x = ev.getX();
				float _y = ev.getY();
				
				/*if(ev.getPointerCount() == 2)
					ev.setLocation((ev.getX(0)+ev.getX(1))/2, (ev.getY(0)+ev.getY(1))/2);*/

				scaleDetector.onTouchEvent(ev);
				if(scaleDetector.isInProgress()){
					scaled = true;
				}
				else{
					switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN:
						lastX = _x;
						lastY = _y;
						break;
					case MotionEvent.ACTION_MOVE:
						if(!scaled){
							mapView.scrollBy((int)(-(_x-lastX)/scale.scaleFactor), (int)(-(_y-lastY)/scale.scaleFactor));
							lastX = _x;
							lastY = _y;
						}
						break;
					case MotionEvent.ACTION_UP:					
						scaled = false;
						break;
					}
				}
				
				return true;
			}
		};
		this.setOnTouchListener(this.onTouch);
		
		// OnScaleListener
		this.scaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
		    @SuppressLint("NewApi")
		    @Override
		    public boolean onScale(ScaleGestureDetector detector) {
		    	float oldScale = scale.scaleFactor;
				scale.scaleFactor *= detector.getScaleFactor();
				scale.scaleFactor = Math.max(MIN_SCALE, Math.min(scale.scaleFactor, 5.0f));
				
				mapView.setPivotX(0f);
				mapView.setPivotY(0f);
				mapView.setScaleX(scale.scaleFactor);
				mapView.setScaleY(scale.scaleFactor);
				
				float targetX = mapView.getScrollX() + 0.5f*getWidth() /oldScale;
				float targetY = mapView.getScrollY() + 0.5f*getHeight()/oldScale;
							
				// Target V3 
				//float targetX = mapView.getScrollX() + 0.5f*getWidth() /oldScale - (1f-detector.getScaleFactor())*(detector.getFocusX()-0.5f*getWidth() /oldScale);
				//float targetY = mapView.getScrollY() + 0.5f*getHeight()/oldScale - (1f-detector.getScaleFactor())*(detector.getFocusY()-0.5f*getHeight() /oldScale);
				
				mapView.scrollTo(	(int)(targetX - 0.5f*getWidth() /scale.scaleFactor),
									(int)(targetY - 0.5f*getHeight()/scale.scaleFactor));
				
				updateConceptsVisibility();
				invalidate();
				return true;
		    }
		});
		
		// OnDragListener
		this.onDrag = new View.OnDragListener() {
			
			@Override
 			public boolean onDrag(View v, DragEvent event) {
 				// Init
 				ConceptView conceptView;
 				
 				if(editMode){
	 				switch (event.getAction()) {
	 				case DragEvent.ACTION_DRAG_STARTED:
	 					if( !event.getClipDescription().hasMimeType(ConceptView.MIMETYPE_CONCEPTVIEW) )
	 						return false;
					break;
	 				case DragEvent.ACTION_DROP:
	 					conceptView = (ConceptView) event.getLocalState();
	 					conceptView.getModel().moveTo(null);
	 					conceptView.getModel().setPosition(	mapView.getScrollX()+event.getX()/scale.getScale(),
	 														mapView.getScrollY()+event.getY()/scale.getScale());
	 					break;
	 				case DragEvent.ACTION_DRAG_ENDED:
	 					Log.d("MindMapView", "Action Ended");
	 					break;
	 				}
	 				return true;
 				}
 				return false;
 			}
		};
		this.setOnDragListener(this.onDrag);
		
	}
	
	/*
	 * View Method
	 */
	
	// Getter
	public MindMapModel	getModel(){				return mindMapModel;	}
	public boolean		isEditMode(){			return editMode;		}
	public float		getDensity(){			return density;			}
	public Fragment		getCurrentFragment() {	return currentFragment;	}

	// Setter
	public void setModel(MindMapModel model){
		// Clear Old Model if exist
		if( this.mindMapModel != null ){
			// Detach all ConceptViews and Clear Index
			for( ConceptView v : this.conceptIndex.values() )
				v.detachView();
			this.conceptIndex.clear();
			
			// Detach from Model
			model.removePropertyChangeListener(MindMapModel.NP_CONCEPT_CREATED, this.onConceptCreated);
			model.removePropertyChangeListener(MindMapModel.NP_CONCEPT_DELETED, this.onConceptDeleted);
			
			// Remove Old Model Ref
			this.mindMapModel = null;
		}
		
		// Load New Model if exist
		if( model != null ){
			this.mindMapModel = model;
			
			// Init New Model Listeners
			this.initPropertyChangeListeners(model);
			
			// Init Concepts List
			List<ConceptModel> list = model.copyOfConceptsList();

			while( !list.isEmpty() ){
				// Init Iterator
				Iterator<ConceptModel> it = list.iterator();
				
				while( it.hasNext() ){
					ConceptModel concept = it.next();
					ConceptModel parent = concept.getParent();
					ConceptView parentView = null;
					
					if( parent != null )
						parentView = this.conceptIndex.get(parent);
					
					if( parent == null || parentView != null ){
						ConceptView view = new ConceptView(this, concept, parentView, this.scale);
						
						// Index Concept View
						this.conceptIndex.put(concept, view);
						
						it.remove();
					}
					// Else do nothing
				}
			}
		}
	}
	
	public void setEditMode(boolean editMode){
		this.editMode = editMode;
	}

	public void setCurrentFragment(Fragment currentFragment) {
		this.currentFragment = currentFragment;
	}

	public void setDensity(float newDensity){
		if( this.density != newDensity ){
			this.density = newDensity;
			updateConceptsVisibility();
		}
	}
	
	protected ConceptView searchViewOfModel(ConceptModel model){
		return conceptIndex.get(model);
	}
	
	protected int getConceptsCount(){
		return this.conceptIndex.size();
	}
	
	protected void addViewToMap(View v){
		this.mapView.addView(v);
	}
	
	protected void addViewToMap(View v, int index, LayoutParams params){
		this.mapView.addView(v, index, params);
	}
	
	protected void removeViewFromMap(View v){
		this.mapView.removeView(v);
	}
	
	private void updateConceptsVisibility(){
		for( ConceptView v : conceptIndex.values() )
			v.updateVisibility();
	}
	
	public PointF getDefaultPosition(){
		return new PointF(	this.mapView.getScrollX()+this.getWidth() *0.2f/this.scale.scaleFactor,
							this.mapView.getScrollY()+this.getHeight()*0.2f/this.scale.scaleFactor);
	}
	
	public float getDefaultSize(){
		return Math.min(1f, 1f/scale.scaleFactor);
	}
	
	/*
	 * Class Declaration
	 */
	
	// ScaleObject which is shared with this MindMapView's ConceptView
	public class ScaleObject {
		public float scaleFactor = MIN_SCALE;
		public float getScale(){
			return scaleFactor;
		}
	};
}
