package com.utcLABS.mindspace.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Color;
import android.util.Xml;

public class MindMapXmlParser {

	private LinkedList<ConceptModel> res;
	private MindMapModel model;

//	public MindMapXmlParser(MindMapModel _model) {
//		model = _model;
//	}

	public MindMapModel parse(InputStream in) throws XmlPullParserException, IOException {
		res = new LinkedList<ConceptModel>();
        MindMapModel readMindmap = new MindMapModel();
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
	        int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT){
	            String name = null;
	            switch (eventType){
	                case XmlPullParser.START_TAG:
	                    name = parser.getName();
	                    if (name.equalsIgnoreCase("title")){
	                    	readMindmap.setTitle(parser.nextText());
	                    }
	                    else if (name.equalsIgnoreCase("lastModificationDate")){
	                    	readMindmap.setLastModificationDate(parser.nextText());
	                    }
	                    else if (name.equalsIgnoreCase("concepts")){
	                    	readConcepts(parser, readMindmap);           
	                    	readMindmap.setConceptIndex(res);
	                    }
//	                case XmlPullParser.END_TAG:
//	                    name = parser.getName();
//	                    if (name.equalsIgnoreCase("concept") && currentConcept != null){
//	                    	readMindmap.getConceptIndex().add(currentConcept);
//	                    	currentConcept = null;
//	                    } 
	            }
	            eventType = parser.next();
			}
			return readMindmap;
		} finally {
			in.close();
		}
	}

	/*
	 * Pour écrire dans un fichier : FileWriter fw = new
	 * FileWriter(adressedufichier, true); BufferedWriter output = new
	 * BufferedWriter(fw);
	 */
	public boolean save(BufferedWriter output) {

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		// TODO : Ajouter les informations en entête..
		xml += "<concepts>\n";
		List<ConceptModel> list = model.copyOfConceptsList();
		Iterator<ConceptModel> i = list.iterator();
		while (i.hasNext()) {
			ConceptModel x = i.next();
			if (x.getParent() == null) {
				xml += getConceptXml(x);
			}
		}
		xml += "\n</concepts>";
		System.out.println("Résultat XML : " + xml);
		try {
			output.write(xml);
			output.flush();
			output.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getConceptXml(ConceptModel cm) {
		String res = "";
		res += "<concept name=\"" + cm.getName() + "\" x=\""
				+ cm.getPosition().x + "\" y=\"" + cm.getPosition().y
				+ "\" size=\"" + cm.getSize() + "\" color=\""
				+ getColorString(cm.getColor()) + "\" shape=\"" + cm.getShape()
				+ "\">\n";

		for (int i = 0; i < cm.getChildrenCount(); i++) {
			res += getConceptXml(cm.getChildAt(i));
		}
		res += "</concept>\n";
		return res;
	}

	private String getColorString(int color) {
		return "rgb(" + Color.red(color) + "," + Color.green(color) + ","
				+ Color.blue(color) + ")";
	}

	private LinkedList<ConceptModel> readConcepts(XmlPullParser parser, MindMapModel model)
			throws XmlPullParserException, IOException {
		
		parser.require(XmlPullParser.START_TAG, "", "concepts");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("concept")) {
				res.add(readConcept(parser, null, model));

				// root.addChildNode(readConcept(parser));
			}
		}

		return res;
	}

	private ConceptModel readConcept(XmlPullParser parser, ConceptModel parent, MindMapModel model)
			throws XmlPullParserException, IOException {

		float x = Float.parseFloat(parser.getAttributeValue("", "x"));
		float y = Float.parseFloat(parser.getAttributeValue("", "y"));

		ConceptModel node = new ConceptModel(model, x, y, parent);

	//	node.setColor(Color.parseColor(parser.getAttributeValue("", "color")));
		node.setSize(Float.parseFloat(parser.getAttributeValue("", "size")));
		node.setName(parser.getAttributeValue("", "name"));
		node.setShape(ConceptModel.getShape(parser.getAttributeValue("","shape")));

		parser.require(XmlPullParser.START_TAG, "", "concept");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("concept")) {
				ConceptModel child = readConcept(parser, node, model);
				res.add(child);
			}
		}

		return node;
	}
}
