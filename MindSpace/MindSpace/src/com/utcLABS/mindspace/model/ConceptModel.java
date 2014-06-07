package com.utcLABS.mindspace.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;



public class ConceptModel implements Parcelable {
	
	/*
	 * Constant
	 */
	public final static String			NP_NAME						= "name";
	public final static String			NP_POSITION					= "position";
	public final static String			NP_SIZE						= "size";
	public final static String			NP_COLOR					= "color";
	public final static String			NP_SHAPE					= "shape";
	public final static String			NP_MOVE						= "move";
	
	public final static float			MAX_SIZE_RATIO				= 0.9f;
	
	public final static float			DEFAULT_SIZE				= 1f;
	public final static float			DEFAULT_SIZE_RATIO			= 0.7f;
	public final static int				DEFAULT_COLOR				= Color.WHITE;
	public final static MindSpaceShape	DEFAULT_SHAPE				= MindSpaceShape.oval;
	
	/* Counter to generate the concept id */
	public static int id_counter = 0;
	
	public static MindSpaceShape getShape(String shape){
		if(shape.equals("rectangle"))
			return MindSpaceShape.rectangle;
		else if(shape.equals("roundedRectangle"))
			return MindSpaceShape.roundedRectangle;
		else
			return MindSpaceShape.oval;
	}
	
	// Available Shapes
	public enum MindSpaceShape{
										rectangle,
										roundedRectangle,
										oval
	}

	/*
	 * Members
	 */
	private MindMapModel				mindMap;
	
	// Data Members
	private String						name;
	private String						description;
	
	// Form Members
	private PointF						position;
	private float						size;
	private int							color;
	private MindSpaceShape				shape;
	
	// Link Members
	private ConceptModel				parent;
	private LinkedList<ConceptModel>	children;
	
	// Bean
	private PropertyChangeSupport		propertyChangeSupport;

	/*
	 * Constructors
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
	
	public ConceptModel(Parcel in){
		this.name = in.readString();
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
		
	
	public MindMapModel getMindMap() {
		return mindMap;
	}

	public String getDescription() {
		return description;
	}

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
	public void setName(String newName) {
		if(!this.name.equals(newName)){
			String oldName = this.name;
			this.name = newName;
			this.propertyChangeSupport.firePropertyChange(NP_NAME, oldName, newName);
		}
	}

	public void setMindMap(MindMapModel mindMap) {
		this.mindMap = mindMap;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setPosition(PointF newPosition) {
		if(!this.position.equals(newPosition)){
			// Compute Translation
			PointF translation = new PointF(newPosition.x-this.position.x, newPosition.y-this.position.y);
			
			// Update this concept Position
			PointF oldPosition = this.position;
			this.position = newPosition;
			this.propertyChangeSupport.firePropertyChange(NP_POSITION, oldPosition, newPosition);
			
			// Update Children Positions
			for(ConceptModel child : children){
				child.setPosition(child.position.x+translation.x, child.position.y+translation.y);
			}
		}
	}
	
	public void setPosition(float x, float y){
		setPosition(new PointF(x,y));
	}
	
	public void setSize(float newSize) {
		// Compute Size Ceil
		if(this.parent!=null)
			newSize = Math.min(newSize, this.parent.size*MAX_SIZE_RATIO);
		
		// Update Size if necessary
		if(this.size!=newSize){
			float oldSize = this.size;
			this.size = newSize;
			this.propertyChangeSupport.firePropertyChange(NP_SIZE, oldSize, newSize);
			
			// Update Children's Size Ceil
			for( ConceptModel c : this.children )
				c.setSize(c.size*(newSize/oldSize));
		}
	}
	
	public void setColor(int newColor) {
		if(this.color!=newColor){
			int oldColor = this.color;
			this.color = newColor;
			this.propertyChangeSupport.firePropertyChange(NP_COLOR, oldColor, newColor);
		}
	}
	
	public void setShape(MindSpaceShape newShape) {
		if(this.shape!=newShape){
			MindSpaceShape oldShape = this.shape;
			this.shape = newShape;
			this.propertyChangeSupport.firePropertyChange(NP_SHAPE, oldShape, newShape);
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
				translation = new PointF( 200f, 200f );	// TODO Default Position
			}
			
			// Move to new parent
			newParent.children.add(this);
			this.parent = newParent;
			this.propertyChangeSupport.firePropertyChange(NP_MOVE, oldParent, newParent);
			
			// Update Position
			this.setPosition(newParent.position.x+translation.x, newParent.position.y+translation.y);
			
			// Update Size Ceil
			this.setSize(this.size);

			// Reset to Default Properties
			//setDefaultProperties();
		}
		else if( newParent == null ){
			if( oldParent != null ){
				oldParent.children.remove(this);
			}
			
			// Set as Root Concept
			this.parent = null;
			this.propertyChangeSupport.firePropertyChange(NP_MOVE, oldParent, null);
			
			// Reset to Default Properties
			//setDefaultProperties();
		}
	}
	
	public void delete(){
		this.mindMap.deleteConcept(this);
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
	
	/*private void setDefaultProperties(){
		// Default Change
		this.setSize( defaultSize(this.parent) );
		this.setColor( defaultColor(this.parent) );
		
		// Repeat For Childs
		for( ConceptModel c : this.children )
			c.setDefaultProperties();
	}*/

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
<<<<<<< HEAD

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
	}
	
=======
>>>>>>> branch 'master' of https://github.com/frozarmony/MindSpace.git
}
