package com.utcLABS.mindspace.model;
import android.view.ext.R;


public class Mindmap {
	String title;
	String lastModificationDate;
	int deleteIcon = R.drawable.delete_mindmap;
	int renameIcon = R.drawable.rename_mindmap;


	public String getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(String lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public int getDeleteIcon() {
		return deleteIcon;
	}

	public void setDeleteIcon(int deleteIcon) {
		this.deleteIcon = deleteIcon;
	}

	public int getRenameIcon() {
		return renameIcon;
	}

	public void setRenameIcon(int renameIcon) {
		this.renameIcon = renameIcon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean loadXml(String xml){
		return false;	// TODO
	}

}