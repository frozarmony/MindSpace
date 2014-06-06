package com.utcLABS.mindspace.view;

import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ext.R;
import android.widget.TextView;

import com.utcLABS.mindspace.model.MindMapModel;
import com.utcLABS.mindspace.utilities.MindmapAdapter;

public class CreateMindmapDialog extends AlertDialog {

	private Context context;
	private View dialogView;
	private MindmapAdapter mindmapAdapter ;
	
	public CreateMindmapDialog(Context context, View dialogView, MindmapAdapter mindmapListAdapter) {
			super(context);
			this.context = context;
			this.dialogView = dialogView;
			this.mindmapAdapter = mindmapListAdapter;
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
			
			dialogBuilder.setView(dialogView);
			dialogBuilder.setPositiveButton("Créer", PositiveButtonListener);
			dialogBuilder.setNegativeButton("Annuler", NegativeButtonListener);
			
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();
	}

	OnClickListener PositiveButtonListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			MindMapModel newMindmap = new MindMapModel();
			
			String title = ((TextView)dialogView.findViewById(R.id.new_mindmap_title)).getText().toString();
			
			newMindmap.setTitle(title);
			newMindmap.setLastModificationDate(new Date().toString());
			
			
			mindmapAdapter.getMindmaps().add(newMindmap);
			mindmapAdapter.notifyDataSetChanged();	
		}
		
	};
	
	OnClickListener NegativeButtonListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int id) {
			dialog.cancel();
		}
	};
}
