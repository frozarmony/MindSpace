package com.utcLABS.mindspace.model;

import java.util.LinkedList;

import android.graphics.Color;

public class ConceptModel {
	
	enum MindSpaceShapes{
		square,
		circle,
		triangle
	}

	// Data Members
	private String						name;
	
	// Form Members
	private float						x;
	private float						y;
	private Color						color;
	private MindSpaceShapes				shape;
	
	// Link Members
	private ConceptModel				parent;
	//private boolean						linked;
	private LinkedList<ConceptModel>	children;
	//private LinkedList<ConceptModel>	externalLink;
	
	// Constructor
	public ConceptModel(float x, float y, ConceptModel parent) {
		super();
		
		// Data
		this.name = "";
		
		// Forms
		this.x = x;
		this.y = y;
		this.color = null;
		this.shape = null;

		// Link
		if( parent != null )
			parent.addChildNode(this);
		else
			this.parent = null;
		children = new LinkedList<ConceptModel>();
	}
	
	// Getters & Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public MindSpaceShapes getShape() {
		return shape;
	}
	public void setShape(MindSpaceShapes shape) {
		this.shape = shape;
	}
	public ConceptModel getParent() {
		return parent;
	}
	public void setParent(ConceptModel parent) {
		this.parent = parent;
	}
	public LinkedList<ConceptModel> getChildren(){
		return children;
	}
	
	// Method
	public void addChildNode(ConceptModel child){
		if( child != null && !children.contains(child) ){
			children.add(child);
			child.parent = this;
		}
	}
	
}
