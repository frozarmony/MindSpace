package com.utcLABS.mindspace.view;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.utcLABS.mindspace.model.ConceptModel;

public class ConceptView {

	// Model Member
	private ConceptModel				model;
	
	// View Member
	private Paint						paint;
	private Path						parentBranch;
	private LinkedList<ConceptView>		childrenViews;

	// Constructor
	public ConceptView(ConceptModel model, float width, float height) {
		this.model = model;
		
		// Paint Config
		this.paint = new Paint();
    	paint.setStyle(Paint.Style.FILL_AND_STROKE);
    	paint.setColor(Color.BLACK);
    	paint.setAntiAlias(true);
    	paint.setTextAlign(Paint.Align.CENTER);
    	paint.setTextSize(30);
    	
    	// Path Config
    	ConceptModel parent = model.getParent();
    	if( parent != null ){
    		this.parentBranch = new Path();
    		if(parent.getX()<model.getX()){
        		this.parentBranch.moveTo(parent.getX()*width, parent.getY()*height);
        		this.parentBranch.lineTo(model.getX()*width, model.getY()*height);
    		}
    		else{
        		this.parentBranch.moveTo(model.getX()*width, model.getY()*height);
        		this.parentBranch.lineTo(parent.getX()*width, parent.getY()*height);
    		}
    	}
    	else{
    		this.parentBranch = null;
    	}
    	
    	// Children Views
    	this.childrenViews = new LinkedList<ConceptView>();
    	for( ConceptModel m : model.getChildren() )
    		this.childrenViews.add(new ConceptView(m, width, height));
    	
	}
	
	// Draw
	public void draw(Canvas pCanvas, float width, float height){
		
		// Children first
		for( ConceptView v : this.childrenViews )
			v.draw(pCanvas,width,height);
		
		// This Draw
		if( this.parentBranch != null ){

			this.parentBranch.reset();
	    	ConceptModel parent = model.getParent();
			if(parent.getX()<model.getX()){
        		this.parentBranch.moveTo(parent.getX()*width, parent.getY()*height);
        		this.parentBranch.lineTo(model.getX()*width, model.getY()*height);
    		}
    		else{
        		this.parentBranch.moveTo(model.getX()*width, model.getY()*height);
        		this.parentBranch.lineTo(parent.getX()*width, parent.getY()*height);
    		}
			
			this.paint.setStyle(Paint.Style.STROKE);
			pCanvas.drawPath(this.parentBranch, this.paint);
			this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
			pCanvas.drawTextOnPath(model.getName(), this.parentBranch, 0, 0, this.paint);
		}
		else{
			RectF rect = new RectF(model.getX()*width-50f,model.getY()*height-50f,model.getX()*width+50f,model.getY()*height+50f);
			pCanvas.drawOval(rect, paint);
			this.paint.setColor(Color.WHITE);
			pCanvas.drawText(model.getName(), model.getX()*width, model.getY()*height+10f, paint);
			this.paint.setColor(Color.BLACK);
		}
		
	}
	
}
