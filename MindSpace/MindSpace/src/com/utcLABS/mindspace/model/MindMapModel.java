package com.utcLABS.mindspace.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;

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
	private LinkedList<ConceptModel>			conceptIndex;

	// Bean
	private PropertyChangeSupport				propertyChangeSupport;

	/*
	 * Constructor
	 */
	public MindMapModel(){
		super();
		
		//Init
		this.conceptIndex = new LinkedList<ConceptModel>();
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	/*
	 * API Interface
	 */
	public ConceptModel createNewConcept(PointF position){
		// Create Concept
		ConceptModel newConcept = new ConceptModel(this, position.x, position.y, null);
		conceptIndex.add(newConcept);
		
		// Signal Concept Created
		this.propertyChangeSupport.firePropertyChange(NP_CONCEPT_CREATED, null, newConcept);
		
		// Return Concept
		return newConcept;
	}
	
	public ConceptModel createNewConcept(ConceptModel parent){
		// Check if parent exists and is in this mindMap
		if( parent == null && conceptIndex.contains(parent) )
			return null; // Do nothing
		else{
			// Create Concept
			ConceptModel newConcept = new ConceptModel(
					this,
					parent.getPosition().x+200f,
					parent.getPosition().y+200f,
					parent);
			conceptIndex.add(newConcept);
			
			// Signal Concept Created
			this.propertyChangeSupport.firePropertyChange(NP_CONCEPT_CREATED, null, newConcept);
			
			// Return Concept
			return newConcept;
		}
	}
	
	public void deleteConcept(ConceptModel concept){
		// Check if concept is not null and is in this mindMap
		if( concept != null && conceptIndex.contains(concept) ){
			// Delete Children First
			while(concept.getChildrenCount()>0)
				deleteConcept(concept.getChildAt(0));
			
			// Delete this Concept
			concept.detachFromOtherConcepts();
			this.conceptIndex.remove(concept);	// Remove from index
			this.propertyChangeSupport.firePropertyChange(NP_CONCEPT_DELETED, null, concept);
		}
	}
	
	public List<ConceptModel> copyOfConceptsList(){
		return new LinkedList<ConceptModel>(this.conceptIndex);
	}
	
	public String toXml(){
		return null;	// TODO
	}
	
	public boolean loadXml(String xml){
		return false;	// TODO
	}

	/*
	 * Property Change Support Delegate
	 */
	
	// For General PropertyChangeListener
	public void addPropertyChangeListener(PropertyChangeListener listener){
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}

	// For Specific PropertyChangeListener
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener){
		this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener){
		this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}
	
}
