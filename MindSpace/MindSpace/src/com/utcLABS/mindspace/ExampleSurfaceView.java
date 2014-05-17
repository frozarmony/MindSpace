package com.utcLABS.mindspace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.utcLABS.mindspace.model.ConceptModel;
import com.utcLABS.mindspace.view.ConceptView;

public class ExampleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // Le holder
    SurfaceHolder mSurfaceHolder;
    // Le thread dans lequel le dessin se fera
    DrawingThread mThread;
    private float test;
    private ConceptView conceptView;

    public ExampleSurfaceView (Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
		
		// Creation Test ConceptModel
		// Root
		ConceptModel root = new ConceptModel(0.5f, 0.5f, null);
		root.setName("Music");
		
		// Creativity
		ConceptModel creativity = new ConceptModel(0.25f, 0.25f, null);
		creativity.setName("Creativity");
		root.addChildNode(creativity);
		
		// Rigour
		ConceptModel rigour = new ConceptModel(0.75f, 0.25f, null);
		rigour.setName("Rigour");
		root.addChildNode(rigour);
		
		// Sociability
		ConceptModel sociability = new ConceptModel(0.2f, 0.8f, null);
		sociability.setName("Sociability");
		root.addChildNode(sociability);
		
		// Titi
		ConceptModel titi = new ConceptModel(0.1f, 0.95f, null);
		titi.setName("People");
		sociability.addChildNode(titi);
		
		this.conceptView = new ConceptView(root, 1280, 800);
		
		test = 0.8f;
		
        mThread = new DrawingThread();
    }

    @Override
    protected void onDraw(Canvas pCanvas) {
    	
    	pCanvas.drawColor(Color.WHITE);
    	
    	this.conceptView.draw(pCanvas, 1280*test, 800*test);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Que faire quand le surface change ? (L'utilisateur tourne son téléphone par exemple)
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread.keepDrawing = true;
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mThread.keepDrawing = false;
		
        boolean joined = false;
        while (!joined) {
            try {
                mThread.join();
                joined = true;
            } catch (InterruptedException e) {}
        }
    }

    private class DrawingThread extends Thread {
	// Utilisé pour arrêter le dessin quand il le faut
        boolean keepDrawing = true;

        @SuppressLint("WrongCall")
		@Override
        public void run() {
		    
            while (keepDrawing) {
                Canvas canvas = null;

                try {
		    // On récupère le canvas pour dessiner dessus
                    canvas = mSurfaceHolder.lockCanvas();
		    // On s'assure qu'aucun autre thread n'accède au holder
                    synchronized (mSurfaceHolder) {
			// Et on dessine
                        onDraw(canvas);
                        test += 0.002f;
                        if(test > 1.2f)
                        	test = 0.2f;
                    }
                } finally {
		    // Notre dessin fini, on relâche le Canvas pour que le dessin s'affiche
                    if (canvas != null)
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                }

                // Pour dessiner à 50 fps
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {}
            }
        }
    }
}