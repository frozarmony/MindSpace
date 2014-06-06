package com.utcLABS.mindspace.model;

import android.app.Application;

public class CurrentMindMap extends Application {

	MindMapModel currentMindMap;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public MindMapModel getCurrentMindMap() {
		return currentMindMap;
	}

	public void setCurrentMindMap(MindMapModel currentMindMap) {
		this.currentMindMap = currentMindMap;
	}
}
