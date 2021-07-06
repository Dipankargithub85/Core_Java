package com;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.lang.*;


public class  ReadXmlFile {


	 List<String> objList = Collections.synchronizedList(new ArrayList<String>());
	 List<String> finallst = new ArrayList<String>();
	 

public String readfile(String fileName) throws ParserConfigurationException, SAXException, IOException
{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
	            .newInstance();
	    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

	    Document document = docBuilder.parse(new File(fileName));
	    
	    document.getDocumentElement().normalize();
	    NodeList nodeList;
	    Element ele = null;

	    NodeList list = document.getChildNodes();
	    for (int i = 0; i < list.getLength(); i++) {
	      if (list.item(i) instanceof Element) {
	    	  ele = (Element) list.item(i);
	        break;
	      }
	    }
	    String rootval = ele.getNodeName();
	    //if (rootval.equals("soapenv:Envelope"))
	    if (rootval.contains(":Envelope") || rootval.contains(":envelope"))
	    {
    	    	 
	    	NodeList nodeListT = document.getElementsByTagName("*");
	    	String root="";
	          for (int i = 0; i < nodeListT.getLength(); i++)
	          {
	        	  String val = nodeListT.item(i).getNodeName();
	        	//if (!nodeListT.item(i).getNodeName().equals("soapenv:Body"))
	        	if (!nodeListT.item(i).getNodeName().contains(":Body"))
		        {
		        	continue;
		        }
	        	
		        	root =  nodeListT.item(i+1).getNodeName() ;
		        	break;
	            	
	            }
	          nodeList = document.getElementsByTagName(root);
	    }
	    else
	    {
	    	nodeList = document.getElementsByTagName(rootval);
	    }

	    
		visitChildNodes(nodeList);
	
	   String getTimestamp = getTmstmp();
	   String [] filepath = fileName.split(".xml");
	   String xcelName = filepath[0] + "_Xpath_" + getTimestamp + ".xlsx";
	   if (writexcel(xcelName)){
		   return xcelName;
	   }
	   else
	   {
		   return "error";
	   }

	
	
	
}

//This function is called recursively
public  void visitChildNodes(NodeList nList)
{

   for (int temp = 0; temp < nList.getLength(); temp++)
   {
      Node node = nList.item(temp);
      if (node.getNodeType() == Node.ELEMENT_NODE)
      {
        // System.out.println("Node Name = " + node.getNodeName() + "; Value = " + node.getTextContent());
        // System.out.println("Node Name = " + node.getNodeName());
         //String[] result = node.getNodeName().split(":");
         String[] result = new String[2];
         if ((node.getNodeName()).contains(":"))
         {
        	  result = node.getNodeName().split(":");
         }
         else
         {
        	 result[1] = node.getNodeName();
        	 
         }
        	 
        if (node.hasChildNodes()) {
               //We got more childs; Let's visit them as well
            	int childcnt = node.getChildNodes().getLength();
            //	System.out.println(childcnt);
            	
				if ( childcnt != 1){
            		objList.add(result[1]);
            	}
            	else
            	{
            		objList.add(result[1]);
            		String app = "";
            		for (int i=0; i<objList.size();i++){
            			 app = app + "/" + objList.get(i);
            		}
            		//app = app +"|" + node.getNodeName();
            		finallst.add(app.substring(1,app.length())+":"+result[1]);
            	}

            	
               visitChildNodes(node.getChildNodes());
            }
}
      }
   if (objList.size() > 0){
	   objList.remove(objList.size() - 1);
   }
   
   }



public List<String> getListValue(){
	
	return finallst;
	
}

public boolean writexcel(String fileName)
{
	//String FILENAME = "C:\\testfl\\MyFirstExcel.xlsx";
	boolean  status = false;
	XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("xpathvalue");
    List<String> xpath = getListValue();
    
    XSSFCellStyle  cs = workbook.createCellStyle();
    cs.setBorderTop(BorderStyle.THIN);
    cs.setBorderBottom(BorderStyle.THIN);
    cs.setBorderRight(BorderStyle.THIN);
    cs.setBorderLeft(BorderStyle.THIN);
 
    
    int rowCount = 0;
    int columncnt = 0;
    Row row = sheet.createRow(rowCount);
    Cell cel = row.createCell(columncnt);
    cel.setCellValue("Xpath");
    sheet.autoSizeColumn((short)0);
    cel.setCellStyle(cs);
    cel = row.createCell(++columncnt);
    cel.setCellValue("Filed_Name");
    sheet.autoSizeColumn((short)0);
    cel.setCellStyle(cs);
    
    for (String val : xpath) {
         row = sheet.createRow(++rowCount);
         
        int columnCount = 0;
        String[] result = val.split(":");
        
        for (String field : result) {
            Cell cell = row.createCell(columnCount);
            if (field instanceof String) {
                cell.setCellValue((String) field);
                
                sheet.autoSizeColumn(columnCount);
                cell.setCellStyle(cs);
            } 
            columnCount++;
        }
         
    }

    try {
        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
     //   System.out.println("Done");
        return true;
         //workbook.
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    return status;
    
}


public String getTmstmp(){
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
	String dtstring  = dateFormat.format(new Date());
	
	return dtstring;

}


public void writeFile()
{
	 String FILENAME = "C:\\testfl\\xpath.txt";

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			

			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			for(int j = 0 ; j<finallst.size();j++){
				//String[] resultdata = finallst.get(j).split(":");
				bw.write(finallst.get(j));
				bw.newLine();
			}
				
			

			//System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}


/*public String loadProperties(){
	
	Properties prop = new Properties();
	InputStream input = null;
	String flpath = null;

	try {

		input = new FileInputStream("C:\\prop\\config.properties");

		// load a properties file
		prop.load(input);

		// get the property value and print it out
		 flpath = prop.getProperty("FileName");
		

	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	return flpath;
	
}*/

}
/*public class ReadXmlFile {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		ReadXmlFileq rd = new ReadXmlFileq();
		rd.readfile();
		List<String> finallst = new ArrayList<String>();
	//	finallst = rd.getListValue();
	//	for(int j = 0 ; j<finallst.size();j++){
	//		System.out.println(finallst.get(j));
//		}
		rd.writeFile();
		rd.writexcel();
		
	}

}*/


