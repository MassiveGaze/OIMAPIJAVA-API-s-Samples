package com.massiveGaze.auditMessage;
/*
 * 
 classpath:
			xlAPI.jar
			xlAuditor.jar
			JDBC driver
			iam-platform-utils.jar
			xlLogger.jar
			xlVO.jar
			xlCrypto.jar
 * 
 * 
 */
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import com.massiveGaze.connection.DataSource;
import com.thortech.xl.audit.engine.AuditDelta;
import com.thortech.xl.audit.engine.AuditDelta.Attribute;
import com.thortech.xl.audit.engine.AuditDelta.ForeignAttribute;
import com.thortech.xl.audit.engine.AuditDelta.XMLNodeTag;
import com.thortech.xl.audit.engine.AuditMessage;

public class ReadDataFromAUD_JMSTable {
	public static void main(String[] args) throws Exception {

		long audJmsKey = 6002;	
		Connection con = DataSource.getConnection();

		Statement stmt = con.createStatement();
		String query = "select aud_jms_key, aud_class, identifier, delay, "
				+ "failed, create_date, update_date, jms_value from aud_jms where aud_jms_key = "
				+ audJmsKey;
		ResultSet rs = stmt.executeQuery(query);

		if (rs.next()) {
			// Blob b = rs.getBlob("jms_value");
			// byte[] byteArray = b.getBytes((long)1, (int)b.length());

			System.out.println("Dump of Audit Message having aud_jms_key: "
					+ audJmsKey);
			System.out.println("Aud Class: " + rs.getString("aud_class"));
			System.out.println("Identifier: " + rs.getString("identifier"));
			System.out.println("Delay: " + rs.getShort("delay"));
			System.out.println("Failed: " + rs.getString("failed"));
			System.out.println("Create Date: " + rs.getDate("create_date"));
			System.out.println("Update Date: " + rs.getDate("update_date"));
			System.out.println("Contents of JMS Message:");

			ByteArrayInputStream bais = new ByteArrayInputStream(
					rs.getBytes("jms_value"));
			ObjectInputStream oip = new ObjectInputStream(bais);

			Object o = oip.readObject();

			AuditMessage msg = (AuditMessage) o;

			System.out.println("(Audit message) API: " + msg.getAPI());
			System.out.println("(Audit message) Auditor Name: "			+ msg.getAuditorName());
			System.out.println("(Audit message) Method: "			 + msg.getMethod());
			System.out.println("(Audit message) User Name: "		+ msg.getUsername());
			System.out.println("(Audit message) Audit Epoch: "		+ msg.getAuditEpoch());
			List lst = msg.getAuditDeltas();
			Iterator it = lst.listIterator();
			System.out.println();

			while (it.hasNext()) {
				AuditDelta delta = (AuditDelta) it.next();
				System.out.println("(Audit message Delta) XML Action: "	+ delta.getXMLAction());
				System.out.println("(Audit message Delta) Auditee ID: "	+ delta.getAuditeeID());
				System.out.println("(Audit message Delta) Change reason: "	+ delta.getChangeReason());
				System.out.println("(Audit message Delta) Change reason Key: "+ delta.getChangeReasonKey());	
				System.out.println("");
				List lst1 = delta.getAttributes();
				Iterator it1 = lst1.listIterator();

				while (it1.hasNext()) {
					Attribute attr = (Attribute) it1.next();
					System.out.println("");
					System.out.println("(Audit message Attribute): " + attr.toString());						
					  System.out.println("(Audit message Attribute) Name: " +			  attr.getName());
					  System.out.println("(Audit message Attribute) newvalue: "			  + attr.getNewValue());
					  System.out.println("(Audit message Attribute) oldvalue: "			  + attr.getOldValue());
					  System.out.println("(Audit message Attribute) encrypted: "		  + attr.isEncrypted());
					  System.out.println("(Audit message Attribute) password: "			  + attr.isPassword());
					  System.out.println("");
				}

				lst1 = delta.getForeignAttributes();
				it1 = lst1.listIterator();
				System.out.println();

				while (it1.hasNext()) {
					ForeignAttribute attr = (ForeignAttribute) it1.next();
					System.out.println("(Audit message ForeignAttribute): " 				  + attr.toString());					
					  System.out.println("(Audit message ForeignAttribute) Name: "			  + attr.getName());
					  System.out.println("(Audit message ForeignAttribute) value: "			  + attr.getValue()); 
					  System.out.println( "(Audit message ForeignAttribute) track changes: "  +	attr.isTrackChanges()); 
					  System.out.println( "(Audit message ForeignAttribute) changes tracked implicitly: "		  + attr.isChangesTrackedImplicitly());
					 
				}

				lst1 = delta.getTargetXMLNodePath();
				it1 = lst1.listIterator();

				while (it1.hasNext()) {
					XMLNodeTag attr = (XMLNodeTag) it1.next();
					System.out.println("(Audit message XMLNodeTag): "
							+ attr.toString());
				}
			}
		} else {
			System.out
					.println("There is no data in AUD_JMS for the given AUD_JMS_KEY "
							+ audJmsKey);
		}

		stmt.close();
		rs.close();
		con.close();

		System.exit(0);

	}
}
