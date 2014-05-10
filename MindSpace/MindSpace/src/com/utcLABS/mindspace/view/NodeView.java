package com.utcLABS.mindspace.view;

import com.utcSABB.mindspace.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class NodeView extends TextView {
	
	// Member
	Drawable shape;

	@SuppressLint("NewApi")
	public NodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		Resources res = getResources();
		shape = res.getDrawable(R.drawable.nodeshape);
		this.setBackground(shape);
	}

}
