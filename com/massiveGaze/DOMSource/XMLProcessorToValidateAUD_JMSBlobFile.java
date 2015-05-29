package com.massiveGaze.DOMSource;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import oracle.iam.platform.OIMClient;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.massiveGaze.connection.DataSource;
import com.thortech.xl.audit.engine.AuditMessage;

public class XMLProcessorToValidateAUD_JMSBlobFile {

	protected static OIMClient platform = null;

	// Transformer transformer = factory.newTransformer();
	// transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	public static String getStringValue(Document document)
			throws TransformerException {

		CharArrayWriter writer = new CharArrayWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty("encoding", "UTF-8");

		 DOMImplementationLS lsImpl = (DOMImplementationLS)document.getDocumentElement().getOwnerDocument().getImplementation().getFeature("LS", "3.0");
		LSSerializer serializer = lsImpl.createLSSerializer();
		serializer.getDomConfig().setParameter("xml-declaration", Boolean.valueOf(true));
	     System.out.println(" Will Exception Occurs During  new DOMSource Object  ? ");
	     new DOMSource(document.getDocumentElement());
	      System.out.println(" No Exception Occurs During  new DOMSource Object  ? ");
	      String str = serializer.writeToString(document.getDocumentElement());
	      System.out.println(" After Setting reason getDocumentElement STRIGN XML IS *****->" + str);
	     System.out.println(" Will Exception Occurs During Transform Object  ?");
	     transformer.transform(new DOMSource(document.getDocumentElement()), result);
	      System.out.println(" No Exception Occurs After Transform Object..");
	     System.out.println("Writer Object -> \n \n" + new String(writer.toCharArray()).toString());
	     System.out.println(" Returning From getStringValue() method, Class: XMLProcessor");
	      return new String(writer.toCharArray()); 	

	}

	public static void main(String args[]) throws TransformerException,
			SQLException, IOException, ClassNotFoundException {

		// getBlob();

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		builderFactory.setNamespaceAware(true); // Set namespace aware
		builderFactory.setValidating(false); // and validating parser feaures
		builderFactory.setIgnoringElementContentWhitespace(false);
	
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder(); // Create the parser
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document xmlDoc = null;
		try {
			
			xmlDoc = builder.parse(new InputSource(new StringReader(xmlString)));
			/*NodeList list = xmlDoc.getChildNodes();
			Node node = list.item(0);
		     System.out.println(" Node Object from List array   ? ");
		     if ((node != null) && (node.getAttributes() != null)) {
		       System.out.println(" Node is not null and checking for Attributes  ? ");
		      NamedNodeMap attr = node.getAttributes();
		      System.out.println(" Retrived attributes. checking for null and namespace ? ");
		      if (attr != null) {
		      System.out.println(" Retrived attributes. Not Null ? ");
		       if (attr.getNamedItem("reason") != null) {
		          System.out.println(" Exist attributes. with reason namespace ");
		          if (((Attr)attr.getNamedItem("reason")).getValue().equals("")) {
		            System.out.println(" reason is having Empty string ");
		            System.out.println("Before Setting Reason value :" + ((Attr)attr.getNamedItem("reason")).getValue());
		            ((Attr)attr.getNamedItem("reason")).setValue("ADMIN");
		            System.out.println("attribute Reason Set To Value : " + ((Attr)attr.getNamedItem("reason")).getValue());
		          }
		         }
		       }
		    }*/
			 // platform = OIMConnection.connection();
			//  XMLProcessor unauth = platform.getService(XMLProcessor.class);
			
			 com.thortech.xl.audit.genericauditor.utils.XMLProcessor unauth = new com.thortech.xl.audit.genericauditor.utils.XMLProcessor(null);
			 System.out.println("**************************");
			//System.out.println( unauth.getStringValue(xmlDoc));
			 getStringValue(xmlDoc);

		} catch (SAXException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void getBlob() throws SQLException, IOException,
			ClassNotFoundException {

		Connection connectionObject = DataSource.getConnection();
		String sql = "select JMS_VALUE from aud_jms where aud_jms_key=?";
		PreparedStatement query = connectionObject.prepareStatement(sql);
		query.setInt(1, 141);
		ResultSet rs = query.executeQuery();
		rs.next();
		Blob jmsValue = rs.getBlob("JMS_VALUE");
		InputStream is = jmsValue.getBinaryStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		AuditMessage auditMsg = (AuditMessage) ois.readObject();
		System.out.println(" ->>>>>>>>>>>>>>> "+ auditMsg.getAuditDeltas());
	
		Object o = ois.readObject();
		//XMLStream xStream = new XStream(new DomDriver());
		//String xmldata = xStream.toXML(o);
	}

	static String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Changes><Change action=\"update\" where=\"/UserProfileSnapshot/UserInfo\" order=\"0\" reason=\"admin\" reasonKey=\"0\">"
			+ "<Attribute name=\"Users.Update Date\">"
			+ "	<OldValue>2014-01-11 12:32:59 -0500</OldValue>"
			+ "	<NewValue>2014-01-11 12:33:58 -0500</NewValue>"
			+ "</Attribute>"
			+ "<Attribute name=\"Users.Password\">"
			+ "	<OldValue>*******</OldValue>"
			+ "	<NewValue>********</NewValue>"
			+ "</Attribute>"
			+ "<Attribute name=\"Users.Updated By Login\">"
			+ "	<OldValue key=\"252145\">BXT1</OldValue>"
			+ "	<NewValue key=\"4\">OIMINTERNAL</NewValue>"
			+ "</Attribute>"
			+ "</Change></Changes>";

}
