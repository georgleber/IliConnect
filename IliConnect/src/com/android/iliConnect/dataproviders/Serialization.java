package com.android.iliConnect.dataproviders;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.android.iliConnect.models.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSInput;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.models.ClassAlias;
import com.android.iliConnect.models.Item;

public class Serialization {

	private String encoding = "UTF-8";
	private static PrimitiveParser primitiveParser = new PrimitiveParser();

	/**
	 * Das im String enthaltene XML-Dokument deserialisieren. Falls rootObject ungleich null ist, wird das XML-Dokument in das Objekt hinein deserialisiert (In
	 * diesem Fall muss das XML-Dokument also ein Feld von rootObject darstellen!). Falls rootObject gleich null ist, wird entsprechend der ClassAlias-Liste
	 * eine passende Klasse gesucht und das Objekt automatisch generiert.
	 * 
	 * @param in
	 * @param rootObject
	 * @return
	 * @throws Exception
	 */

	public Object deserialize(String in, String rootNodeName) throws Exception {
		return deserialize(new ByteArrayInputStream(in.getBytes(encoding)), rootNodeName);
	}

	/**
	 * Das im InputStream enthaltene XML-Dokument deserialisieren. Falls rootObject ungleich null ist, wird das XML-Dokument in das Objekt hinein deserialisiert
	 * (In diesem Fall muss das XML-Dokument also ein Feld von rootObject darstellen!). Falls rootObject gleich null ist, wird entsprechend der ClassAlias-Liste
	 * eine passende Klasse gesucht und das Objekt automatisch generiert.
	 * 
	 * @param in
	 * @param rootObject
	 * @return
	 * @throws Exception
	 */

	
	public Object deserialize(Class<?> targetClass , String filename) throws Exception {
		// XML-Dokument parsen
		Serializer serializer = new Persister();
		File source = new File (MainActivity.instance.getFilesDir()+"/"+filename);
		//Object targetObject = targetClass.newInstance();
		Object example = serializer.read(targetClass, source,false);
		return example;
	}

	
	
	
	public Object deserialize(InputStream in, String rootNodeName) throws Exception {
		// XML-Dokument parsen
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(in);
		String rootNode = rootNodeName.split("\\.")[rootNodeName.split("\\.").length - 1];
		NodeList nodes = doc.getElementsByTagName(rootNode);
		return deserialize(nodes, rootNodeName);
	}

	public Object deserialize(NodeList nodes, String rootNodeName) throws Exception {

		Class<?> mClass = Class.forName(rootNodeName);
		Object targetObj = mClass.newInstance();
		// Das zu liefernde Objekt (dem Root-Knoten zugeordnet) generieren.
		// Node rootNode = doc.getFirstChild();
		for (int i = 0; i < nodes.getLength(); i++) {
			fillObject(targetObj, nodes.item(i));
		}

		return targetObj;
	}

	public void fillObject(Object targetObject, Node actNode) {

		Class<?> actClass = targetObject.getClass();
		try {
			Field actField = actClass.getField(actNode.getNodeName());
			String fieldClassName = actField.getType().getName();

			if (fieldClassName.contains("String")) {

				actField.set(targetObject, actNode.getTextContent());

			} else if (fieldClassName.contains("List")) {

				Object listContainer = ArrayList.class.newInstance();

				NodeList childNodes = actNode.getChildNodes();
				Method add = List.class.getDeclaredMethod("add", Object.class);

				for (int i = 0; i < childNodes.getLength(); i++) {
					Node childNode = childNodes.item(i);
					Node nextNode = childNode;
					do {

						if (nextNode.getNodeType() == Node.ELEMENT_NODE) {

							Class<?> componentClass = (Class<?>) ((ParameterizedType) actField.getGenericType()).getActualTypeArguments()[0];
							Object toAdd = componentClass.newInstance();

							fillObject(toAdd, nextNode);

							add.invoke(listContainer, toAdd);
							nextNode = nextNode.getNextSibling();
						}
						}while ( nextNode != null); 
					}
			
			
				actField.set(targetObject, listContainer);

			} else {

				// Eine Instanz der Klasse erzeugen

				Class actFieldClass = actField.getClass();
				Object fieldObject = actFieldClass.newInstance();

				// Die einzelnen Felder der Klasse füllen

				for (int j = 0; j < actNode.getChildNodes().getLength(); j++)
					fillObject(targetObject, actNode.getChildNodes().item(j));

				// Das Objekt dem übergeordneten Objekt zuweisen
				actField.set(targetObject, fieldObject);

			}

		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (DOMException e) {
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {

		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
