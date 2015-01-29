package com.gemt.pdffactory;


public class Test {

	public static void main(String[] args) {
		PDFDoc doc = new PDFDoc("sample.pdf");
		doc.createNewPage();
		doc.openPage(1);
		doc.writeText(36, 40, "OPERATION STEP :", 12);
		doc.writeText(36, 60, "REVISION :", 12);
		doc.writeText(36, 80, "PART NUMBER : 199-2093-CU", 12);
		doc.writeText(36, 100, "DATE : 29-JAN-2015, 12:00:51", 12);
		doc.writeImageCenter(10, 120, "Sample.jpg");			
		doc.closePage();
		doc.saveFile();
		
        //doc.printFile("NBS_Printer");
		doc.close();
	}

}
