package com.utcLABS.mindspace.view;

import com.utcLABS.mindspace.model.ConceptModel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MindMapPanel extends SurfaceView implements SurfaceHolder.Callback {
	
	// Member
	private MindMapPanelThread _thread;
	private ConceptView conceptView;
	private float zoom;
	private Paint testPaint;
	private String testString;
	private Rect rec;

	public MindMapPanel(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        getHolder().addCallback(this);
        /*testPaint = new Paint();
        testPaint.setStyle(Style.STROKE);
        testPaint.setColor(Color.BLACK);
        testPaint.setAntiAlias(true);
        testPaint.setTextAlign(Paint.Align.CENTER);
        testPaint.setTextSize(100);
        
        testString = new String("TADADATA");
        rec = new Rect();*/
        // Creation Test ConceptModel
 		// Root
 		ConceptModel root = new ConceptModel(1000f, 500f, null);
 		root.setName("Music");
 		
 		// Creativity
 		ConceptModel creativity = new ConceptModel(500f, 250f, null);
 		creativity.setName("Creativity");
 		root.addChildNode(creativity);
 		
 		// Rigour
 		ConceptModel rigour = new ConceptModel(1500f, 250f, null);
 		rigour.setName("Rigour");
 		root.addChildNode(rigour);
 		
 		// Sociability
 		ConceptModel sociability = new ConceptModel(400f, 800f, null);
 		sociability.setName("Sociability");
 		root.addChildNode(sociability);
 		
 		// Titi
 		ConceptModel titi = new ConceptModel(200f, 950f, null);
 		titi.setName("People");
 		sociability.addChildNode(titi);
 		
 		this.conceptView = new ConceptView(root, null);
        
 		zoom = 1.4f;
    }


    @Override 
    public void onDraw(Canvas canvas) {
    	
    	canvas.drawColor(Color.WHITE);
    	
    	canvas.scale(zoom, zoom);
    	
    	this.conceptView.draw(canvas);
    	
    	/*canvas.save();
    	canvas.translate(canvas.getWidth()/2f, canvas.getHeight()/2f);
    	
    	testPaint.getTextBounds(testString, 0, testString.length(), rec);
    	canvas.drawText(testString, 0, 0, testPaint);
    	
    	canvas.scale(1.1f, 1.2f);
    	canvas.translate(-rec.width()/2f, rec.height()*0.1f);
    	canvas.drawRect(rec, testPaint);
    	
    	canvas.restore();*/
    }


    @Override 
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
        int height) { 


    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    	setWillNotDraw(false); //Allows us to use invalidate() to call onDraw()

		_thread = new MindMapPanelThread(getHolder(), this);	//Start the thread that
		_thread.setRunning(true);                   			//will make calls to 
		_thread.start();                              			//onDraw()
		
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	try {
    		_thread.setRunning(false);                //Tells thread to stop
    		_thread.join();                           //Removes thread from mem.
    	} catch (InterruptedException e) {}
    }

}
