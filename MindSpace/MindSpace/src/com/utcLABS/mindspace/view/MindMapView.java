package com.utcLABS.mindspace.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.ConceptModel.MindSpaceShape;
import com.utcLABS.mindspace.model.MindMapModel;

@SuppressLint("NewApi") public class MindMapView extends FrameLayout  {

	/*
	 * Member
	 */
	
	// Model Member
	private MindMapModel							mindMapModel;
	
	// View Member
	private HashMap<ConceptModel, ConceptView>		conceptIndex;
	private RelativeLayout							mapView;
	private float									density;
	
	// Property Change Listener
	private PropertyChangeListener					onConceptCreated;
	private PropertyChangeListener					onConceptDeleted;
	
	// Test Member
	private ConceptModel							root;
	
	//controler member
	private ScaleGestureDetector mScaleDetector;
	public final ScaleObject scale = new ScaleObject();
	// pour partager le scaleFactor avec les NodeView
	public class ScaleObject {
		public float scaleFactor = 1f;
		public float getScale(){
			return scaleFactor;
		}
	};
	
	protected float centerX = 0;
	protected float offsetX = 0;
	protected float centerY = 0;
	protected float offsetY = 0;
	
	private MyTouchListener touchListener;

	/*
	 * Constructor
	 */
	public MindMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Init Model Member
		this.mindMapModel = null;
		
		// Init View Member
		this.conceptIndex = new HashMap<ConceptModel, ConceptView>();
		
		this.mapView = new RelativeLayout(context);
		this.mapView.setBackgroundColor(0xffeeeeee);
		this.addView(this.mapView, 4000,4000);	// TODO compute proper mapView Size
		
		this.density = 0.5f;
		
		/*
		 * Model Test
		 */
		MindMapModel myTestModel = new MindMapModel();
		float coef = 0.7f;
		
		// Listeners
		//initPropertyChangeListeners(this.mindMapModel);
		
 		// Root
 		root = myTestModel.createNewConcept(new PointF(500f*coef+600f, 250f*coef+300f));
 		root.setName("Music");
 		root.setSize(coef);
 		
 		// Sociability
 		ConceptModel sociability = myTestModel.createNewConcept(root);
 		sociability.setPosition(200f*coef+600f, 400f*coef+300f);
 		sociability.setName("Sociability");
 		sociability.setColor(Color.rgb(50, 50, 200));
 		
 		// Titi
 		ConceptModel titi = myTestModel.createNewConcept(sociability);
 		titi.setPosition(100f*coef+600f, 475f*coef+300f);
 		titi.setName("People");
 		
 		// Rigour
 		ConceptModel rigour = myTestModel.createNewConcept(root);
 		rigour.setPosition(750f*coef+600f, 125f*coef+300f);
 		rigour.setName("Rigour");
 		rigour.setColor(Color.rgb(200, 50, 50));
 		rigour.setShape(MindSpaceShape.oval);
 		
 		// Theory
 		ConceptModel theory = myTestModel.createNewConcept(rigour);
 		theory.setPosition(950f*coef+600f, 25f*coef+300f);
 		theory.setName("Theory");
 		
 		// Creativity
 		ConceptModel creativity = myTestModel.createNewConcept(root);
 		creativity.setPosition(250f*coef+600f, 125f*coef+300f);
 		creativity.setName("Creativity");
 		creativity.setColor(Color.rgb(50, 200, 50));
 		
 		this.setModel(myTestModel);
 		
 		/*
 		 * Controller Test
 		 */
 		
 		mScaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
 		// OnTouchTest
 		this.touchListener = new MyTouchListener();
 		this.setOnTouchListener(this.touchListener);
 		
 		this.setOnDragListener(new OnDragListener() {

 			@Override
 			public boolean onDrag(View v, DragEvent event) {
 				// Init
 				ConceptView conceptView;
 				
 				switch (event.getAction()) {
 				case DragEvent.ACTION_DRAG_STARTED:
				if( !event.getClipDescription().hasMimeType(ConceptView.MIMETYPE_CONCEPTVIEW) )
					return false;
				break;
 				case DragEvent.ACTION_DROP:
 					Log.d("MindMapView", "Action Drop");
 					conceptView = (ConceptView) event.getLocalState();
 					conceptView.getModel().moveTo(null);
 					conceptView.getModel().setPosition(event.getX(), event.getY());	// TODO get Relative X Y
 					break;
 				case DragEvent.ACTION_DRAG_ENDED:
 					Log.d("MindMapView", "Action Ended");
 					break;
 				}
 				return true;
 			}
 		});
 		
 		// Update Concepts Visibility
 		updateConceptsVisibility();
	}
	
	class MyTouchListener implements OnTouchListener{

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				float _x = ev.getX();
				float _y = ev.getY();
				
				/*if(ev.getPointerCount() == 2)
					ev.setLocation((ev.getX(0)+ev.getX(1))/2, (ev.getY(0)+ev.getY(1))/2);*/
				

				mScaleDetector.onTouchEvent(ev);
				//mDoubleTapDetector.onDoubleTap(ev);
				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					offsetX = _x+centerX;
					offsetY = _y+centerY;
					break;
				case MotionEvent.ACTION_MOVE:
					if(offsetX != 0 && offsetY != 0){
						centerX = offsetX-_x;
						centerY = offsetY-_y;
						mapView.scrollTo((int)(centerX/scale.scaleFactor), (int)(centerY/scale.scaleFactor));
					}
					break;
				case MotionEvent.ACTION_UP:					
					offsetX = 0;
					offsetY = 0;
					break;
				}
				return true;
			}
		}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @SuppressLint("NewApi") @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        scale.scaleFactor *= detector.getScaleFactor();
	        scale.scaleFactor = Math.max(1f, Math.min(scale.scaleFactor, 5.0f));
	        mapView.setPivotX(detector.getFocusX()*scale.scaleFactor);
	        mapView.setPivotY(detector.getFocusY()*scale.scaleFactor);
	        mapView.setScaleX(scale.scaleFactor);
	        mapView.setScaleY(scale.scaleFactor);
	        updateConceptsVisibility();
	        invalidate();
	        return true;
	    }
	}
	
	/*
	 * Listeners
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
	 * View Method
	 */
	
	// Getter
	public float		getDensity(){		return density;			}
	public MindMapModel	getModel(){			return mindMapModel;	}
	
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

}
