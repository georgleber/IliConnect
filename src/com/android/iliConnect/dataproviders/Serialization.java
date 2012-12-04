package com.android.iliConnect.dataproviders;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.android.iliConnect.models.ClassAlias;
import com.android.iliConnect.models.Settings;

import android.util.Base64;
import android.util.Xml;

import android.widget.Toast;

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
	public Object deserialize(String in, List<ClassAlias> classAliases, String rootNodeName, boolean isArray) throws Exception {
		return deserialize(new ByteArrayInputStream(in.getBytes(encoding)), classAliases, rootNodeName, isArray);
	}

	public Object deserialize(String in, List<ClassAlias> classAliases, String rootNodeName) throws Exception {
		return deserialize(new ByteArrayInputStream(in.getBytes(encoding)), classAliases, rootNodeName, false);
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
	public Object deserialize(InputStream in, List<ClassAlias> classAliases, String rootNodeName) throws Exception {
		return deserialize(in, classAliases, rootNodeName, false);
	}

	public Object deserialize(InputStream in, List<ClassAlias> classAliases, String rootNodeName, boolean isArray) throws Exception {
		// XML-Dokument parsen
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(in);

		NodeList nodes = doc.getElementsByTagName(rootNodeName);

		Class mClass = Class.forName("models." + rootNodeName);
		Object targetObj = mClass.newInstance();
		// Das zu liefernde Objekt (dem Root-Knoten zugeordnet) generieren.
		// Node rootNode = doc.getFirstChild();

		for (int i = 0; i < nodes.getLength(); i++) {
			// Unterknoten deserialisieren
			if (isArray)
				fillField(targetObj, nodes.item(i));
			else {
				NodeList children = nodes.item(i).getChildNodes();
				for (int c = 0; c < children.getLength(); c++)
					fillField(targetObj, children.item(c));
			}
		}

		return targetObj;
	}

	/**
	 * Ein Feld innerhalb von <targetObj> mit dem Knoten <node> füllen. Das Füllen findet rekursiv statt.
	 * 
	 * @param targetObj
	 * @param node
	 */
	private void fillField(Object targetObj, Node node) {

		try {

			String fieldName = node.getNodeName();
			if (fieldName.equalsIgnoreCase("#text") && ( !fieldName.equalsIgnoreCase("java.util.List") || fieldName.equalsIgnoreCase("java.util.List")))
				return;
			
			node.normalize(); // 20111216 RHSS: muss auf jeden Fall drin bleiben, da sonst < und > in Texten falsch interpretiert werden
			
			Class<?> targetObjClass = targetObj.getClass();
			Field fieldInfo = targetObjClass.getField(fieldName);
			Class<?> fieldClass = fieldInfo.getType();
			String fieldClassName = fieldClass.getName();

		

			// fieldValue repräsentiert den Wert der Node, sofern es sich um eine Node eines einfachen Typs mit nur einem Wert handelt.
			String fieldValue = "";
			if (node.getChildNodes().getLength() == 1)
				fieldValue = node.getChildNodes().item(0).getNodeValue();

			// Die Klasse des zu füllenden Objekts --> Daraus können die einzelnen Felder ausgelesen werden.

			// Per reflection das zu füllende Feld ermitteln

			// Um was für ein Feld handelt es sich? Primitiver Typ, Klasse, Array, Liste?

			// Primitive Typen
			if (fieldClass.isPrimitive()) {
				if (fieldClassName.equalsIgnoreCase("boolean"))
					fieldInfo.setBoolean(targetObj, primitiveParser.parseBoolean(fieldValue));
				else if (fieldClassName.equalsIgnoreCase("byte"))
					fieldInfo.setByte(targetObj, primitiveParser.parseByte(fieldValue));
				else if (fieldClassName.equalsIgnoreCase("char"))
					fieldInfo.setChar(targetObj, primitiveParser.parseChar(fieldValue));
				else if (fieldClassName.equalsIgnoreCase("double"))
					fieldInfo.setDouble(targetObj, primitiveParser.parseDouble(fieldValue));
				else if (fieldClassName.equalsIgnoreCase("float"))
					fieldInfo.setFloat(targetObj, primitiveParser.parseFloat(fieldValue));
				else if (fieldClassName.equalsIgnoreCase("int"))
					fieldInfo.setInt(targetObj, primitiveParser.parseInt(fieldValue));
				else if (fieldClassName.equalsIgnoreCase("long"))
					fieldInfo.setLong(targetObj, primitiveParser.parseLong(fieldValue));
				else if (fieldClassName.equalsIgnoreCase("short"))
					fieldInfo.setShort(targetObj, primitiveParser.parseShort(fieldValue));
			} else {
				// Kein primitiver Typ
				// String
				if (fieldClassName.equalsIgnoreCase("java.lang.String"))
					fieldInfo.set(targetObj, fieldValue);
				else if (fieldClassName.equalsIgnoreCase("java.util.ArrayList") || fieldClassName.equalsIgnoreCase("java.util.List") || fieldClass.isArray()) {
					Object fieldObject = ArrayList.class.newInstance();

					// Liste: die einzelnen Unterknoten deserialisieren und zur Liste hinzufügen.

					// Klasse der Element bestimmen
					Class<?> componentClass = null;
					if (fieldClass.isArray())
						componentClass = fieldClass.getComponentType();
					else
						componentClass = (Class<?>) ((ParameterizedType) fieldInfo.getGenericType()).getActualTypeArguments()[0];

					NodeList children = node.getChildNodes();
					for (int c = 0; c < children.getLength(); c++) {
						Node listSubNode = children.item(c);
						if (listSubNode.getNodeType() == Node.ELEMENT_NODE) {
							// Objekt-Instanz erstellen
							Object toAdd = componentClass.newInstance();

							// Die einzelnen Felder des Objekts füllen
							NodeList subChildren = listSubNode.getChildNodes();
							for (int c2 = 0; c2 < subChildren.getLength(); c2++) {
								Node n = subChildren.item(c2);
								if (componentClass.getClass().getName().equalsIgnoreCase("java.util.String"))
									toAdd = n.getTextContent();
								else
									fillField(toAdd, n);
							}

							// Das Objekt zur Liste hinzufügen
							Method add = List.class.getDeclaredMethod("add", Object.class);
							add.invoke(fieldObject, toAdd);
						}
					}

					// Die gefüllte Liste dem übergeordneten Objekt zuweisen
					if (fieldClass.isArray()) {
						ArrayList<?> fieldObjArrayList = (ArrayList<?>) fieldObject;
						Object arrayObj = fieldObjArrayList.toArray((Object[]) Array.newInstance(componentClass, fieldObjArrayList.size()));
						fieldInfo.set(targetObj, arrayObj);
					} else
						fieldInfo.set(targetObj, fieldObject);
				} else if (fieldClassName.equalsIgnoreCase("java.util.Date")) {
					Date fieldObject = new Date(0);

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");// 2011-07-04T12:33:07
					try {
						fieldObject = format.parse(fieldValue);
					} catch (Exception ex) {
						try {
							format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
							fieldObject = format.parse(fieldValue);
						} catch (Exception ex2) {
						}
					}
					fieldInfo.set(targetObj, fieldObject);
				} else {
					// Selbstdefinierte Klassen

					// Eine Instanz der Klasse erzeugen
					Object fieldObject = fieldClass.newInstance();

					// Die einzelnen Felder der Klasse füllen
					NodeList children = node.getChildNodes();
					for (int c = 0; c < children.getLength(); c++)
						fillField(fieldObject, children.item(c));

					// Das Objekt dem übergeordneten Objekt zuweisen
					fieldInfo.set(targetObj, fieldObject);
				}
			}
		} catch (NoSuchFieldException ex) {
			// Feld ist in Klasse nicht vorhanden, macht nix!
		} catch (Exception ex) {
			String exep = ex.getMessage();
		}
	}

	/**
	 * Gibt, falls in der Liste der Class-Aliases gefunden, ein leeres Objekt (passend zum Name des übergebenen Knotens) zurück.
	 * 
	 * @param rootNode
	 * @return
	 * @throws Exception
	 */
	private static Object getObjectInstanceFromNode(Node rootNode, boolean ignoreErrors, List<ClassAlias> classAliases) throws Exception {
		String expectedClassName = rootNode.getNodeName();
		for (ClassAlias alias : classAliases)
			if (alias.className.equalsIgnoreCase(expectedClassName))
				return alias.classInfo.newInstance();

		if (ignoreErrors)
			return null;
		else
			throw new Exception("Knoten konnte keiner Klasse zugeordnet werden: " + expectedClassName);
	}
}
