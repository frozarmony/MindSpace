package com.utcLABS.mindspace.view;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.utcLABS.mindspace.model.ConceptModel;

public class ConceptView {
	
	private enum ViewMode{
		root,
		simpleBranch
	}

	// Model Member
	private ConceptModel				model;
	
	// View Member
	private ConceptView					parentView;
	private LinkedList<ConceptView>		childrenViews;
	private ViewMode					viewMode;
	private int							depth;
	
	// Text
	private Paint						textPaint;
	
	// Parent Branch
	private Path						parentBranch;
	private Paint						parentBranchPaint;
	
	// Shape Node
	

	/*
	 * Inititialisation
	 */
	
	// Constructor
	public ConceptView(ConceptModel model, ConceptView parentView) {
		super();
		
		// Init Model
		this.model = model;
		
		// Init View Member
		this.parentView = parentView;
		this.viewMode = ViewMode.root;
		
		if(parentView == null)
			depth = 0;
		else
			depth = parentView.depth+1;
		
		initTextPaint();
    	
    	// Children Views
    	this.childrenViews = new LinkedList<ConceptView>();
    	for( ConceptModel m : model.getChildren() ){
    		ConceptView childView = new ConceptView(m, this);
    		this.childrenViews.add(childView);
    	}
	}
	
	// Init TextPaint
	private void initTextPaint(){
		textPaint = new Paint();
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.BLACK);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(100);
	}
	
	// Init Parent Branch
	private void initParentBranch(){
		// Init Path
		parentBranch = new Path();
		updateParentBranch();
		
		// Init Paint
		parentBranchPaint = new Paint();
		parentBranchPaint.setStyle(Paint.Style.STROKE);
		parentBranchPaint.setColor(Color.BLACK);
		parentBranchPaint.setAntiAlias(true);
	}
	
	/*
	 * Drawing Update
	 */
	
	// Update Path
	private void updateParentBranch(){
		parentBranch.reset();
		float parentOrientation = (float) Math.atan2(parentView.getX()-getX(), parentView.getY()-getY());
		PointF begin = parentView.getBoundPointFromAngle(parentOrientation+(float)Math.PI);
		PointF end = getBoundPointFromAngle(parentOrientation);
		
		if(parentView.getX()<this.getX()){
			parentBranch.moveTo(begin.x, begin.y);
			parentBranch.lineTo(end.x, end.y);
		}
		else{
			parentBranch.moveTo(end.x, end.y);
			parentBranch.lineTo(begin.x, begin.y);
		}
	}
	
	/*
	 * Draw
	 */
	public void draw(Canvas pCanvas){

		// Children first
		for( ConceptView v : this.childrenViews )
			v.draw(pCanvas);
		
		pCanvas.save();
		pCanvas.translate(getX(), getY());
		pCanvas.scale(1.0f-depth*0.2f, 1.0f-depth*0.2f);
		
		// This Draw
		switch(viewMode){
		case root:
			pCanvas.drawText(model.getName(), 0, 0, textPaint);
			break;
		case simpleBranch:
			pCanvas.drawPath(this.parentBranch, this.parentBranchPaint);
			pCanvas.drawTextOnPath(model.getName(), this.parentBranch, 0, 0, this.textPaint);
			break;
		default:
			break;
		}
		
		pCanvas.restore();
		/*if( this.parentBranch != null ){
			
			
		}
		else{
			RectF rect = new RectF(model.getX()*width-50f,model.getY()*height-50f,model.getX()*width+50f,model.getY()*height+50f);
			pCanvas.
			pCanvas.drawOval(rect, paint);
			this.paint.setColor(Color.WHITE);
			pCanvas.drawText(model.getName(), model.getX()*width, model.getY()*height+10f, paint);
			this.paint.setColor(Color.BLACK);
		}*/
		
	}
	
	/*
	 * Geometry Info
	 */
	
	// Getters
	public float getX(){	return model.getX();	}
	public float getY(){	return model.getY();	}
	
	// For Path Drawing
	public PointF getBoundPointFromAngle(float radiant){
		switch(viewMode){
		case root:
			return new PointF(getX(), getY());
		case simpleBranch:
			return new PointF(getX(), getY());
		default:
			return new PointF(getX(), getY());
		}
	}
	
}
