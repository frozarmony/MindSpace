package com.utcLABS.mindspace.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import android.graphics.Color;
import android.graphics.PointF;

@SuppressWarnings("serial")
public class ConceptModel {
	
	/*
	 * Constant
	 */
	public final static String			NP_NAME						= "name";
	public final static String			NP_POSITION					= "position";
	public final static String			NP_SIZE						= "size";
	public final static String			NP_COLOR					= "color";
	public final static String			NP_SHAPE					= "shape";
	
	public final static float			DEFAULT_SIZE				= 80f;
	public final static float			DEFAULT_SIZE_RATIO			= 0.7f;
	public final static int				DEFAULT_COLOR				= Color.TRANSPARENT;
	public final static MindSpaceShape	DEFAULT_SHAPE				= MindSpaceShape.oval;
	
	// Available Shape
	public enum MindSpaceShape{
		rectangle,
		oval
	}

	/*
	 * Member
	 */
	
	// Data Members
	private String						name;
	
	// Form Members
	private PointF						position;
	private float						size;
	private int							color;
	private MindSpaceShape				shape;
	
	// Link Members
	private ConceptModel				parent;
	//private boolean						linked;
	private LinkedList<ConceptModel>	children;
	//private LinkedList<ConceptModel>	externalLink;
	
	// Bean
	private PropertyChangeSupport		propertyChangeSupport;
	
	// Constructor
	public ConceptModel(float x, float y, ConceptModel parent) {
		super();
		
		// Data
		this.name = "Concept";
		
		// Forms
		this.position = new PointF(x,y);
		this.size = defaultSize(parent);
		this.color = defaultColor(parent);
		this.shape = defaultShape(parent);
		
		// Bean
		this.propertyChangeSupport = new PropertyChangeSupport(this);

		// Link
		if( parent != null )
			parent.addChildNode(this);
		else
			this.parent = null;
		this.children = new LinkedList<ConceptModel>();
	}
	
	// Getters & Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(!this.name.equals(name)){
			this.propertyChangeSupport.firePropertyChange(NP_NAME, this.name, name);
			this.name = name;
		}
	}
	public PointF getPosition() {
		return position;
	}
	public void setPosition(PointF position) {
		if(!this.position.equals(position)){
			this.propertyChangeSupport.firePropertyChange(NP_POSITION, this.position, position);
			this.position = position;
		}
	}
	public void setPosition(float x, float y){
		setPosition(new PointF(x,y));
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		if(this.size!=size){
			this.propertyChangeSupport.firePropertyChange(NP_SIZE, this.size, size);
			this.size = size;
		}
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		if(this.color!=color){
			this.propertyChangeSupport.firePropertyChange(NP_COLOR, this.color, color);
			this.color = color;
		}
	}
	public MindSpaceShape getShape() {
		return shape;
	}
	public void setShape(MindSpaceShape shape) {
		if(this.shape!=shape){
			this.propertyChangeSupport.firePropertyChange(NP_SHAPE, this.shape, shape);
			this.shape = shape;
		}
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
	
	// Default Values
	private float defaultSize(ConceptModel parent){
		if(parent!=null)
			return parent.size*DEFAULT_SIZE_RATIO;
		else
			return DEFAULT_SIZE;
	}
	
	private int defaultColor(ConceptModel parent){
		if(parent!=null)
			return parent.color;
		else
			return DEFAULT_COLOR;
	}
	
	private MindSpaceShape defaultShape(ConceptModel parent){
		if(parent!=null)
			return parent.shape;
		else
			return DEFAULT_SHAPE;
	}
	
	// Method
	public void addChildNode(ConceptModel child){
		if( child != null && !children.contains(child) ){
			children.add(child);
			child.parent = this;
		}
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
