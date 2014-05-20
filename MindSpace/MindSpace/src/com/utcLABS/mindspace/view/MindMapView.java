package com.utcLABS.mindspace.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.TreeMap;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.model.MindMapModel;
import com.utcLABS.mindspace.model.ConceptModel.MindSpaceShape;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MindMapView extends ScrollView  {

	/*
	 * Member
	 */
	
	// Model Member
	private MindMapModel							mindMapModel;
	
	// View Member
	private RelativeLayout							mapView;
	private TreeMap<ConceptModel, ConceptView>		conceptIndex;
	private ConceptView								rootConceptView;
	
	// Property Change Listener
	private PropertyChangeListener					onConceptCreated;
	
	// Test Member
	private ConceptModel							root;

	/*
	 * Constructor
	 */
	@SuppressLint("NewApi")
	public MindMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Init View Member
		this.mapView = new RelativeLayout(context);
		this.addView(this.mapView, 4000,4000);
		
		this.conceptIndex = new TreeMap<ConceptModel, ConceptView>();
		
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
 		
 		// OnTouchTest
 		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if( event.getAction() == MotionEvent.ACTION_DOWN ){
					//root.getChildren().getFirst().setPosition(event.getX(),event.getY());
					//setScrollX(getScrollX()+50);
					//setScrollY(getScrollY()+50);
					
					//root.getChildren().getFirst().moveTo(root.getChildren().getLast());
					//mindMapModel.deleteConcept(root.getChildren().getFirst());
				}
				return false;
			}
		});
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
				
				ConceptView view = new ConceptView(MindMapView.this, model, parentView);
				
				// Index Concept View
				conceptIndex.put(model, view);
			}
		};
		model.addPropertyChangeListener(MindMapModel.NP_CONCEPT_CREATED, this.onConceptCreated);
	}
	
	public ConceptView searchViewOfModel(ConceptModel model){
		return conceptIndex.get(model);
	}
	
	
	public void addViewToMap(View v){
		this.mapView.addView(v);
	}
	
	public void addViewToMap(View v, int index, LayoutParams params){
		this.mapView.addView(v, index, params);
	}
	
	public void removeViewFromMap(View v){
		this.mapView.removeView(v);
	}

}
