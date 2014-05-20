package com.utcLABS.mindspace.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import android.graphics.PointF;

public class MindMapModel {
	
	/*
	 * Constant
	 */
	public final static String					NP_CONCEPT_CREATED			= "concept created";
	public final static String					NP_CONCEPT_DELETED			= "concept deleted";

	/*
	 * Member
	 */
	private LinkedList<ConceptModel>			mainConceptNodes;

	// Bean
	private PropertyChangeSupport				propertyChangeSupport;

	/*
	 * Constructor
	 */
	public MindMapModel(){
		super();
		
		//Init
		this.mainConceptNodes = new LinkedList<ConceptModel>();
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	/*
	 * API Interface
	 */
	public ConceptModel createNewConcept(PointF position){
		// Create Concept
		ConceptModel newConcept = new ConceptModel(this, position.x, position.y, null);
		mainConceptNodes.add(newConcept);
		
		// Signal Concept Created
		this.propertyChangeSupport.firePropertyChange(NP_CONCEPT_CREATED, null, newConcept);
		
		// Return Concept
		return newConcept;
	}
	
	public ConceptModel createNewConcept(ConceptModel parent){
		// TO DO check if parent exist in this mindMap
		if( parent == null )
			return null; // Do nothing
		else{
			// Create Concept
			ConceptModel newConcept = new ConceptModel(
					this,
					parent.getPosition().x+200f,
					parent.getPosition().y+200f,
					parent);
			
			// Signal Concept Created
			this.propertyChangeSupport.firePropertyChange(NP_CONCEPT_CREATED, null, newConcept);
			
			// Return Concept
			return newConcept;
		}
	}
	
	public void detachConcept(ConceptModel concept){
		// TO DO
	}
	
	public void deleteConcept(ConceptModel concept){
		// TO DO check if concept exist
		if( concept != null ){
			// Delete Children First
			while(concept.getChildrenCount()>0)
				deleteConcept(concept.getChildAt(0));
			
			// Delete this Concept
			concept.detachFromOtherConcepts();
			this.mainConceptNodes.remove(concept);	// Remove if exist
			this.propertyChangeSupport.firePropertyChange(NP_CONCEPT_DELETED, null, concept);
		}
	}
	
	public void save(){
		// TO DO save
	}
	
	// For General PropertyChangeListener
	public void addPropertyChangeListener(PropertyChangeListener listener){
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}

	// For Specific PropertyChangeListener
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener){
		this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
}
