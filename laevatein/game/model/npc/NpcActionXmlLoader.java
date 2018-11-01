package laevatein.game.model.npc;

import java.io.*;
import java.util.concurrent.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/*
 * server/database/NpcActionTable參考
 */
public class NpcActionXmlLoader
{
	private static final String XML_PATH = "./XML/NpcActions" ;
	
	/*
	 * 解析標前的遞迴層階計算
	 */
	private static int depth = 0;
	
	
	public NpcActionXmlLoader () {
		//
	}
	
	public void load (ConcurrentHashMap<String, Document> _npcActionTable) {
		File xmlFileFolder = new File (XML_PATH);
		try {
			File[] xmlFiles = xmlFileFolder.listFiles ();
			for (File xmlFile : xmlFiles) {
				ParseXmlFile (xmlFile, _npcActionTable) ;
			}
		} catch (Exception e) {
			System.out.println (e.toString () ) ;
			e.printStackTrace () ;
		}
	}
	
	private static void ParseXmlFile (File XmlFile, ConcurrentHashMap<String, Document> NpcActionTable) {
		try {
			DocumentBuilder XmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder () ;
			Document Result = XmlBuilder.parse (XmlFile) ;
			
			NpcActionTable.put (XmlFile.getName (), Result) ;
			
			/* 建立至快取內 */
			Document doc = (Document) NpcActionTable.get (XmlFile.getName () ) ;
			NodeList ActionList = doc.getDocumentElement ().getChildNodes () ;
			
			/* 開始遞迴走訪 */
			parseXmlActionFile (ActionList) ; 
			
		} catch (Exception e) {
			System.out.println (e.toString ());
			e.printStackTrace ();
		}
		
	}
	
	/* 遞迴解析! 小心使用! */
	public static void parseXmlActionFile (NodeList _rootNode) {
		depth ++;
		
		for (int index = 0; index < _rootNode.getLength (); index++) {
			Node node = _rootNode.item (index) ;
			
			if (!node.getNodeName ().startsWith ("#") ) {
				//System.out.println () ;
				for (int d = 0; d < depth; d++) {
					//System.out.print ("   ") ;
				}
				//System.out.printf ("[%s] ", node.getNodeName ());
			}
			
			if (node.hasAttributes ()) {
				NamedNodeMap atrrList = node.getAttributes ();
				for (int j = 0; j < atrrList.getLength (); j++) {
					Node attr = atrrList.item (j) ;
					if (!attr.getNodeName ().startsWith ("#")) {
						//System.out.printf ("%s=%s ", attr.getNodeName (), attr.getNodeValue () ) ;
					}
				}
			}
			
			if (node.hasChildNodes ()) {
				NodeList subNodes = node.getChildNodes ();
				for (int k = 0; k < subNodes.getLength (); k++) {
					parseXmlActionFile (subNodes); //Recursion
				}
			}
			
		}
		depth --;
	}
}
