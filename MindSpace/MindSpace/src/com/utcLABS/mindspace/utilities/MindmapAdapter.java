package com.utcLABS.mindspace.utilities;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ext.R;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.utcLABS.mindspace.model.Mindmap;

public class MindmapAdapter extends BaseAdapter {   
	ArrayList<Mindmap> mindmaps = new ArrayList<Mindmap>(); 
	ListView mindmapsList;
	LayoutInflater inflater; 
	Context context;  
	private static final String TAG = "MindmapAdapter";
	
	public MindmapAdapter(Context context, ArrayList<Mindmap> myList, ListView myListView) { 
		super();
		this.mindmaps = myList; 
		this.context = context; 
		this.inflater = LayoutInflater.from(this.context); 	
		this.mindmapsList = myListView;
	}   
	
	@Override 
	public int getCount() { 
		return mindmaps.size(); 
	}  
	
	@Override 
	public Mindmap getItem(int position) { 
		return mindmaps.get(position); 
	}   
	
	@Override 
	public long getItemId(int position) { 
		return 0; 
	}   
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent) { 
		MyViewHolder mViewHolder;   
		if(convertView == null) { 
			convertView = inflater.inflate(R.layout.layout_list_mindmap, null); 
			mViewHolder = new MyViewHolder(); 
			convertView.setTag(mViewHolder); 
		} 
		else { 
			mViewHolder = (MyViewHolder) convertView.getTag(); 
		}  
		this.mindmapsList.setOnItemClickListener(ItemListener);
		mViewHolder.tvTitle = detail(convertView, R.id.title, mindmaps.get(position).getTitle()); 
		mViewHolder.tvModificationDate = detail(convertView, R.id.modification_date, mindmaps.get(position).getLastModificationDate()); 
		mViewHolder.btDelete = detail(convertView, R.id.delete_mindmap, mindmaps.get(position).getDeleteIcon());   
		mViewHolder.btRename = detail(convertView, R.id.rename_mindmap, mindmaps.get(position).getRenameIcon());   

		mViewHolder.btDelete.setOnClickListener(DeleteClickListener);
		mViewHolder.btRename.setOnClickListener(RenameClickListener);
		convertView.setTag(mViewHolder);
		
		return convertView; 
		
	}   
	
	private TextView detail(View v, int resId, String text) { 
		TextView tv = (TextView) v.findViewById(resId); 
		tv.setText(text); 
		return tv; 
	}  
	
	private Button detail(View v, int resId, int icon) { 
		Button bt = (Button) v.findViewById(resId); 
		bt.setBackgroundResource(icon);   
		return bt; 
	}   
	
	private OnClickListener DeleteClickListener = new OnClickListener() {
		
		@Override
        public void onClick(View v) {
			final int position = mindmapsList.getPositionForView((View) v.getParent());
            final Mindmap deletedMindmap = mindmaps.get(position);
            
			//View dialogView = inflater.inflate(R.layout.new_mindmap_dialog, null);
			//final TextView input = (TextView) dialogView.findViewById(R.id.new_mindmap_title);
			//input.setText(renamedMindmap.getTitle());

			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MindmapAdapter.this.context)
						.setTitle("Supprimer Mindmap")
						.setMessage("Voulez-vous supprimer le Mindmap \""+ deletedMindmap.getTitle()+"\"");
			
			dialogBuilder.setPositiveButton("Supprimer",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {	
		            mindmaps.remove(position);           
		            MindmapAdapter.this.notifyDataSetChanged();
				}
			});
			
			dialogBuilder.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});	
			
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();      
        }	
	};
	
	private OnClickListener RenameClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			final int position = mindmapsList.getPositionForView((View) v.getParent());
            final Mindmap renamedMindmap = mindmaps.get(position);
            
			View dialogView = inflater.inflate(R.layout.new_mindmap_dialog, null);
			final TextView input = (TextView) dialogView.findViewById(R.id.new_mindmap_title);
			input.setText(renamedMindmap.getTitle());

			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MindmapAdapter.this.context)
						.setView(dialogView)
						.setTitle("Renommer Mindmap");
			
			dialogBuilder.setPositiveButton("Renommer",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
					renamedMindmap.setTitle(input.getText().toString());            
		            MindmapAdapter.this.notifyDataSetChanged();
				}
			});
			
			dialogBuilder.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});	
			
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();
		}
	};
	
	private OnItemClickListener ItemListener = new OnItemClickListener() {
       
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           String selected = ((TextView) view.findViewById(R.id.title)).getText().toString();

            Toast toast=Toast.makeText(MindmapAdapter.this.context, selected+" a été cliqué.", Toast.LENGTH_SHORT);
            toast.show();
        }
      };
	
	private class MyViewHolder { 
		TextView tvTitle, tvModificationDate; 
		Button btDelete, btRename; 
	}   
}
	

