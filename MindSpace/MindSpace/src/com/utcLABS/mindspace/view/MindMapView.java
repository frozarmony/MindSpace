package com.utcLABS.mindspace.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

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
	
	// Property Change Listener
	private PropertyChangeListener					onConceptCreated;
	private PropertyChangeListener					onConceptDeleted;
	
	// Test Member
	private ConceptModel							root;
	
	//controler member
	private ScaleGestureDetector mScaleDetector;
	public final ScaleObject scale = new ScaleObject();
	// pour partager le scaleFactor avec les NodeView
	public class ScaleObject extends Object {
		public float scaleFactor = 1f;
		public float getScale(){
			return scaleFactor;
		}
	};
	
	protected float centerX = 0;
	protected float offsetX = 0;
	protected float centerY = 0;
	protected float offsetY = 0;
	

	/*
	 * Constructor
	 */
	public MindMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Init View Member
		this.conceptIndex = new HashMap<ConceptModel, ConceptView>();
		
		this.mapView = new RelativeLayout(context);
		this.addView(this.mapView, 4000,4000);
		this.mapView.setBackgroundColor(0xffeeeeee);
		
		
		float coef = 1f;
		
		// Creation Test ConceptModel
		this.mindMapModel = new MindMapModel();
		
		// Listeners
		initPropertyChangeListeners(this.mindMapModel);
		
 		// Root
 		root = this.mindMapModel.createNewConcept(new PointF(500f*coef+600f, 250f*coef+300f));
 		root.setName("Music");
 		root.setSize(ConceptModel.DEFAULT_SIZE*coef);
 		
 		// Sociability
 		ConceptModel sociability = this.mindMapModel.createNewConcept(root);
 		sociability.setPosition(200f*coef+600f, 400f*coef+300f);
 		sociability.setName("Sociability");
 		sociability.setColor(Color.rgb(50, 50, 200));
 		
 		// Titi
 		ConceptModel titi = this.mindMapModel.createNewConcept(sociability);
 		titi.setPosition(100f*coef+600f, 475f*coef+300f);
 		titi.setName("People");
 		
 		// Rigour
 		ConceptModel rigour = this.mindMapModel.createNewConcept(root);
 		rigour.setPosition(750f*coef+600f, 125f*coef+300f);
 		rigour.setName("Rigour");
 		rigour.setColor(Color.rgb(200, 50, 50));
 		rigour.setShape(MindSpaceShape.oval);
 		
 		// Theory
 		ConceptModel theory = this.mindMapModel.createNewConcept(rigour);
 		theory.setPosition(950f*coef+600f, 25f*coef+300f);
 		theory.setName("Theory");
 		
 		// Creativity
 		ConceptModel creativity = this.mindMapModel.createNewConcept(root);
 		creativity.setPosition(250f*coef+600f, 125f*coef+300f);
 		creativity.setName("Creativity");
 		creativity.setColor(Color.rgb(50, 200, 50));
 		
 		//this.rootConceptView = new ConceptView(this, root, null);
 		
 		mScaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
 		// OnTouchTest
 		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				float _x = ev.getX();
				float _y = ev.getY();
				
				if(ev.getPointerCount() == 2)
					ev.setLocation((ev.getX(0)+ev.getX(1))/2, (ev.getY(0)+ev.getY(1))/2);
				
				mScaleDetector.onTouchEvent(ev);
				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					offsetX = _x-centerX;
					offsetY = _y-centerY;
					break;
				case MotionEvent.ACTION_MOVE:
					if(offsetX != 0 && offsetY != 0){
						centerX = _x - offsetX;
						centerY = _y - offsetY;
						//mapView.setPaddingRelative((int)centerX, (int)centerY, (int)centerX, (int)centerY);
						//mapView.setX(centerX);
						//mapView.setY(centerY);
						mapView.scrollTo((int)-centerX, (int)-centerY);
						/*mapView.setTranslationX(centerX);
						mapView.setTranslationY(centerY);*/
					}
					break;
				case MotionEvent.ACTION_UP:					
					offsetX = 0;
					offsetY = 0;
					break;
				}
				return true;
			}
		});
 		
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
 					conceptView = (ConceptView) event.getLocalState();
 					conceptView.endDropAction();
 					break;
 				}
 				return true;
 			}
 		});
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @SuppressLint("NewApi") @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        scale.scaleFactor *= detector.getScaleFactor();
	        //scaleFactor = Math.max(1f, Math.min(scaleFactor, 5.0f));
	        //System.out.println(scaleFactor.hashCode());
	        setScaleX(scale.scaleFactor);
	        setScaleY(scale.scaleFactor);
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
				// Create Concept View
				ConceptModel model = (ConceptModel)event.getNewValue();
				ConceptView view = conceptIndex.get(model);
				
				if( view != null ){
					view.removeSubViews();
					conceptIndex.remove(model);
				}
			}
		};
		model.addPropertyChangeListener(MindMapModel.NP_CONCEPT_DELETED, this.onConceptDeleted);
	}
		
	/*
	 * View Method
	 */
	protected ConceptView searchViewOfModel(ConceptModel model){
		return conceptIndex.get(model);
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

}
