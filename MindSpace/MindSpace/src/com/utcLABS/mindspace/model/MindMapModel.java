package com.utcLABS.mindspace.model;

import java.util.LinkedList;

import android.graphics.PointF;

public class MindMapModel {

	// Member
	private LinkedList<ConceptModel>			mainConceptNodes;
	
	// Constructor
	public MindMapModel(){
		super();
		
		//Init
		mainConceptNodes = new LinkedList<ConceptModel>();
	}
	
	// API Interface
	public ConceptModel createNewConcept(PointF position){
		ConceptModel newConcept = new ConceptModel(this, position.x, position.y, null);
		mainConceptNodes.add(newConcept);
		return newConcept;
	}
	
	public ConceptModel createNewConcept(ConceptModel parent){
		// TO DO check if parent exist in this mindMap
		if( parent == null )
			return null;
		else{
			ConceptModel newConcept = new ConceptModel(
					this,
					parent.getPosition().x+200f,
					parent.getPosition().y+200f,
					parent);
			return newConcept;
		}
	}
	
	public void detachConcept(ConceptModel concept){
		// TO DO
	}
	
	public void deleteConcept(ConceptModel concept){
		// TO DO check if concept exist
		if( concept != null ){
			concept.delete();
		}
	}
	
	public void save(){
		// TO DO save
	}
	
	// Tool Method
	public void remove(ConceptModel model){
		this.mainConceptNodes.remove(model);
	}
	
}
