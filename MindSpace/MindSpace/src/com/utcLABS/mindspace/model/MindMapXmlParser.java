package com.utcLABS.mindspace.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

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
            return readConcepts(parser);
        } finally {
            in.close();
        }
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
