package com.utcLABS.mindspace.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
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
	
	public MindMapXmlParser(MindMapModel _model){
		model = _model;
	}
	
	public LinkedList<ConceptModel> parse(InputStream in) throws XmlPullParserException, IOException {
		res = new LinkedList<ConceptModel>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readHead(parser);
            return readConcepts(parser);
        } finally {
            in.close();
        }
    }
	
	private void readHead(XmlPullParser parser) {
		try {

			parser.require(XmlPullParser.START_TAG, "", "mindmap");
			parser.next();
			parser.next();
			parser.require(XmlPullParser.START_TAG, "", "head");
			String lastModif = "";
			//String  = "";
			while (parser.next() != XmlPullParser.END_TAG && lastModif == "") {
		        if (parser.getEventType() != XmlPullParser.START_TAG) {
		            continue;
		        }
		        parser.next();
		        String name = parser.getText();
		        System.out.println(name);
		    }
			
			/*
			parser.require(XmlPullParser.START_TAG, "", "mindmap");
			//parser.require(XmlPullParser.START_TAG, "", "title");
			parser.require(XmlPullParser.TEXT, "", "title");
			model.setTitle(parser.getText());
			
			parser.require(XmlPullParser.TEXT, "", "lastModified");
			model.setLastModificationDate(parser.getText());*/
			
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Pour écrire dans un fichier :
	 * FileWriter fw = new FileWriter(adressedufichier, true);
	 * BufferedWriter output = new BufferedWriter(fw);
	 */
	public boolean save(BufferedWriter output){
		model.setLastModificationDate(new Date().toString());
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		
		xml += "<mindmap>\n"
				+ "\t<head>\n"
					+ "\t\t<title>" + model.getTitle() + "</title>\n"
					+ "\t\t<lastModified>" + model.getLastModificationDate() + "</lastModified>\n"
				+ "\t</head>\n";
		
		xml += "<concepts>\n";
		List<ConceptModel> list = model.copyOfConceptsList();
		Iterator<ConceptModel> i = list.iterator();
		while(i.hasNext()){
		  ConceptModel x = i.next();
		  if(x.getParent() == null){
			  xml += getConceptXml(x);
		  }
		}
		xml+="\n</concepts>\n</mindmap>";
		System.out.println("Résultat XML : "+xml);
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
	
	private String getConceptXml(ConceptModel cm){
		String res = "";
		res+="<concept name=\""+cm.getName()+"\" x=\""+cm.getPosition().x+"\" y=\""+cm.getPosition().y+"\" size=\""+cm.getSize()+"\" color=\""+getColorString(cm.getColor())+"\" shape=\""+cm.getShape()+"\">\n";
		
		for(int i=0;i<cm.getChildrenCount();i++){
			res+=getConceptXml(cm.getChildAt(i));
		}
		res+="</concept>\n";
		return res;
	}
	
	private String getColorString(int color){
		return "rgb("+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+")";
	}

	private LinkedList<ConceptModel> readConcepts(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, "", "concepts");
		while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("concept")) {
            	res.add(readConcept(parser,null));
            	
            	//root.addChildNode(readConcept(parser));
            }
        }
		
		return res;
	}

	private ConceptModel readConcept(XmlPullParser parser, ConceptModel parent) throws XmlPullParserException, IOException {
		
		float x = Float.parseFloat(parser.getAttributeValue("","x"));
		float y = Float.parseFloat(parser.getAttributeValue("","y"));
		
		ConceptModel node = new ConceptModel(model,x,y,parent);
		
		node.setColor(Color.parseColor(parser.getAttributeValue("","color")));
		node.setSize(Float.parseFloat(parser.getAttributeValue("","size")));
		node.setName(parser.getAttributeValue("", "name"));
		node.setShape(ConceptModel.getShape(parser.getAttributeValue("", "shape")));
		
		parser.require(XmlPullParser.START_TAG, "", "concept");
		while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("concept")) {
            	ConceptModel child = readConcept(parser,node);
            	res.add(child);
            }
		}
		
		return node;
	}
}
