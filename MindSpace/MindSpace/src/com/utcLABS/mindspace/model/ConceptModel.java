package com.utcLABS.mindspace.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import android.graphics.Color;
import android.graphics.PointF;

public class ConceptModel {
	
	/*
	 * Constant
	 */
	public final static String			NP_NAME						= "name";
	public final static String			NP_POSITION					= "position";
	public final static String			NP_SIZE						= "size";
	public final static String			NP_COLOR					= "color";
	public final static String			NP_SHAPE					= "shape";
	public final static String			NP_MOVE						= "move";
	
	public final static float			DEFAULT_SIZE				= 1f;
	public final static float			DEFAULT_SIZE_RATIO			= 0.7f;
	public final static int				DEFAULT_COLOR				= Color.WHITE;
	public final static MindSpaceShape	DEFAULT_SHAPE				= MindSpaceShape.oval;
	
	// Available Shapes
	public enum MindSpaceShape{
										rectangle,
										roundedRectangle,
										oval
	}

	/*
	 * Member
	 */
	private MindMapModel				mindMap;
	
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

	/*
	 * Constructor
	 */
	public ConceptModel(MindMapModel mindMap, float x, float y, ConceptModel parent) {
		super();
		// Init
		this.mindMap	= mindMap;
		
		// Data
		this.name		= "Concept";
		
		// Forms
		this.position	= new PointF(x,y);
		this.size		= defaultSize(parent);
		this.color		= defaultColor(parent);
		this.shape		= defaultShape(parent);
		
		// Bean
		this.propertyChangeSupport = new PropertyChangeSupport(this);

		// Link
		this.children = new LinkedList<ConceptModel>();
		if( parent != null )
			parent.addChildNode(this);
		else
			this.parent = null;
	}

	/*
	 * Getters
	 */
	
	// Property
	public String getName() {					return name;			}
	public PointF getPosition() {				return position;		}
	public float getSize() {					return size;			}
	public int getColor() {						return color;			}
	public MindSpaceShape getShape() {			return shape;			}
	public ConceptModel getParent() {			return parent;			}
	
	// Children
	public int getChildrenCount(){				return children.size();	}
	
	public ConceptModel getChildAt(int index){
		if(index >= children.size())
			return null;
		return children.get(index);
	}

	/*
	 * Setters
	 */
	public void setName(String name) {
		if(!this.name.equals(name)){
			this.propertyChangeSupport.firePropertyChange(NP_NAME, this.name, name);
			this.name = name;
		}
	}
	
	public void setPosition(PointF position) {
		if(!this.position.equals(position)){
			// Compute Translation
			PointF translation = new PointF(position.x-this.position.x, position.y-this.position.y);
			
			// Update this concept Position
			this.propertyChangeSupport.firePropertyChange(NP_POSITION, this.position, position);
			this.position = position;
			
			// Update Children Positions
			for(ConceptModel child : children){
				child.setPosition(child.position.x+translation.x, child.position.y+translation.y);
			}
		}
	}
	
	public void setPosition(float x, float y){
		setPosition(new PointF(x,y));
	}
	
	public void setSize(float size) {
		if(this.size!=size){
			this.propertyChangeSupport.firePropertyChange(NP_SIZE, this.size, size);
			this.size = size;
		}
	}
	
	public void setColor(int color) {
		if(this.color!=color){
			this.propertyChangeSupport.firePropertyChange(NP_COLOR, this.color, color);
			this.color = color;
		}
	}
	
	public void setShape(MindSpaceShape shape) {
		if(this.shape!=shape){
			this.propertyChangeSupport.firePropertyChange(NP_SHAPE, this.shape, shape);
			this.shape = shape;
		}
	}

	/*
	 * Default Values
	 */
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

	/*
	 * Method
	 */
	private void addChildNode(ConceptModel child){
		if( child != null && !children.contains(child) ){
			children.add(child);
			child.parent = this;
		}
	}
	
	public void moveTo(ConceptModel newParent){
		ConceptModel oldParent = this.parent;
		if( newParent != null && !this.isThisOrAscendantOf(newParent) ){
			
			// Compute Translation
			PointF translation;
			
			// Remove from old parent
			if( oldParent != null ){
				translation = new PointF(position.x-oldParent.position.x, position.y-oldParent.position.y);
				oldParent.children.remove(this);
			}
			else{
				translation = new PointF( 200f, 200f );	// TO DO Default Position
			}
			
			// Move to new parent
			newParent.children.add(this);
			this.parent = newParent;
			this.propertyChangeSupport.firePropertyChange(NP_MOVE, oldParent, newParent);
			
			// Update Position
			this.setPosition(newParent.position.x+translation.x, newParent.position.y+translation.y);
		}
		else if( newParent == null ){
			if( oldParent != null ){
				oldParent.children.remove(this);
			}
			
			this.parent = null;
			this.propertyChangeSupport.firePropertyChange(NP_MOVE, oldParent, null);
		}
	}
	
	protected void detachFromOtherConcepts(){
		if( this.parent != null ){
			this.parent.children.remove(this);
		}
	}

	/*
	 * Tools
	 */
	private boolean isThisOrAscendantOf(ConceptModel concept){
		if( this == concept )
			return true;
		for( ConceptModel c : this.children )
			if( c.isThisOrAscendantOf(concept) )
				return true;
		return false;
	}

	/*
	 * Property Change Support Delegate
	 */
	
	// For General PropertyChangeListener
	public void addPropertyChangeListener(PropertyChangeListener listener){
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}

	// For Specific PropertyChangeListener
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener){
		this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
}
