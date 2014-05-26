package com.utcLABS.mindspace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.utcLABS.mindspace.model.MindMapModel;
import com.utcLABS.mindspace.model.MindMapModel.MindMapLoadingException;
import com.utcLABS.mindspace.view.ConceptView;

public class MindMapSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    // Le holder
    SurfaceHolder mSurfaceHolder;
    boolean keepDrawing = true;
    // Le thread dans lequel le dessin se fera
    private Thread drawingThread;
    private float test;
    private ConceptView conceptView;
    
    //model
    private MindMapModel model;
    
    // variables pour le controleur
    private float centerX;
    private float centerY;
    
    private float offsetX = 0;
    private float offsetY = 0;
    
    private float scaleFactor;
    
    private ScaleGestureDetector mScaleDetector;
    

    public MindMapSurfaceView (Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        
        mScaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
        
		this.model = new MindMapModel("");
		try {
			model.load();
		} catch (MindMapLoadingException e) {
			e.printStackTrace();
		}
		this.conceptView = new ConceptView(model.getRoot(), this.getWidth(), this.getHeight());
		
		test = 0.8f;
		
		drawingThread = new Thread(this);
    }
    
    @Override
	public void draw(Canvas pCanvas) {
    	
    	pCanvas.drawColor(Color.WHITE);
    	//float size = 50;
    	//pCanvas.drawRect(new RectF(centerX-size,centerY-size,centerX+size,centerY+size), new Paint());
    	pCanvas.save();
    	pCanvas.translate(centerX - (getWidth()/2)*scaleFactor,centerY - (getHeight()/2)*scaleFactor);
    	
    	pCanvas.scale(scaleFactor, scaleFactor);
    	
    	if(this.conceptView != null)
    		this.conceptView.draw(pCanvas, getWidth(), getHeight());
    	pCanvas.restore();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Que faire quand le surface change ? (L'utilisateur tourne son téléphone par exemple)
    	//TODO
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        keepDrawing = true;
        drawingThread.start();
        
        centerX = getWidth()/2;
        centerY = getHeight()/2;
        scaleFactor = 0.5f;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        keepDrawing = false;
		
        boolean joined = false;
        while (!joined) {
            try {
                drawingThread.join();
                joined = true;
            } catch (InterruptedException e) {}
        }
    }

    @Override
	public void run() {
    	while (keepDrawing) {
    		if(!mSurfaceHolder.getSurface().isValid()){
				continue;
			}
        	// On récupère le canvas pour dessiner dessus
            Canvas canvas = mSurfaceHolder.lockCanvas();
            this.draw(canvas);
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
	}
    
    
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        scaleFactor *= detector.getScaleFactor();
	
	        // Don't let the object get too small or too large.
	        scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
	        //System.out.println(scaleFactor);
	        invalidate();
	        return true;
	    }
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		float _x = ev.getX();
		float _y = ev.getY();
		
		if(ev.getPointerCount() == 2)
			ev.setLocation((ev.getX(0)+ev.getX(1))/2, (ev.getY(0)+ev.getY(1))/2);
		
		mScaleDetector.onTouchEvent(ev);
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			offsetX = _x-centerX;
			offsetY = _y-centerY;
			break;
		case MotionEvent.ACTION_MOVE:
			if(offsetX != 0 && offsetY != 0){
				centerX = _x - offsetX;
				centerY = _y - offsetY;
			}
			break;
		case MotionEvent.ACTION_UP:
			offsetX = 0;
			offsetY = 0;
			break;
		}
		return true;
	}
	
}