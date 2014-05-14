package com.utcLABS.mindspace.model;


public class MindMapModel {
	
	public class MindMapLoadingException extends Exception{
		private static final long serialVersionUID = 4874840719052182938L;

		public MindMapLoadingException(){
			super("Erreur lors du chargement du fichier de mindmap");
		}
	}
	
	String path;
	ConceptModel root;
	
	public MindMapModel(String _path){
		path = _path;
	}
	
	public void load() throws MindMapLoadingException{
		// TODO charger le mindmap à partir d'un fichier
		root = new ConceptModel(0.5f, 0.5f, null);
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
	}
	
	public ConceptModel getRoot(){
		return root;
	}
	
}
