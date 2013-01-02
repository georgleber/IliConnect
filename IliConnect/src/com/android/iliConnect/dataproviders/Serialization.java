package com.android.iliConnect.dataproviders;

import java.io.File;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import com.android.iliConnect.MainActivity;

public class Serialization {

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

	public Object deserialize(Class<?> targetClass, String filename) throws Exception {
		// XML-Dokument parsen
		Serializer serializer = new Persister();
		File source = new File(MainActivity.instance.getFilesDir() + "/" + filename);
		
		if(!source.exists())
			throw new Exception();
		
		// Object targetObject = targetClass.newInstance();
		Object example = null;
		example = serializer.read(targetClass, source, false);
		
		return example;

	}
	
	public void serialize(Object targetObject, String filename) throws Exception {
		Serializer serializer = new Persister();
		File target = new File(MainActivity.instance.getFilesDir() + "/" + filename);
		serializer.write(targetObject, target);
	}
	

}
