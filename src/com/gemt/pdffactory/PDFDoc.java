package com.gemt.pdffactory;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.print.PrintService;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

/**
 * @author Prashanta.s
 *
 */
public class PDFDoc{

	/** File name for PDF */
	private String filename = null;
	
	/** The PDF document  */
	private PDDocument document = null;
	
	/** List of pages in PDF */
	private ArrayList<PDPage> pageList = new ArrayList<PDPage>();	
	
	/** Content stream thing */
	private PDPageContentStream contentStream = null;
	
	/** Indicates which page is open */
	private int openPage = -1; 
	
	// FONTS
	PDFont fontPlain = PDType1Font.HELVETICA;
    PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    PDFont fontMono = PDType1Font.COURIER;
    
    float pageWidth= 595.27563f;
    float pageHeight= 841.8898f;
    float dpi = 200f;
    float upi = 72f;
    private Color defaultInkColor = Color.BLACK;
	
	/**
	 * Constructor
	 * @param filename Name of the file to save as
	 */
	public PDFDoc(String filename){
		this.filename = filename;
		document = new PDDocument();
	}
	
	/**
	 * Create a new single A4 page and add to document
	 */
	public void createNewPage(){
		createNewPages(1);
	}
	
	/**
	 * Create new multiple A4 pages and add to document 	
	 * @param pages Number of pages to create
	 */
	public void createNewPages(int pages){
		while(pages > 0){
			PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
			pageList.add(page);
			this.document.addPage(page);
			pages--;
		}				 		
	}
 
	/**
	 * Get page object by page number
	 * @param pageNum Page number to get
	 * @return
	 */
	private PDPage getPage(int pageNum){
		return (pageNum > pageList.size())? null : pageList.get(pageNum - 1);
	}
	
	/**
	 * Open a page in document to start writing. Closes previous open page.
	 * @param pageNum Page number to open
	 * @return
	 */
	public int openPage(int pageNum){
		// Check if page number is within range		
		if(pageNum >= 1 && pageNum <= document.getNumberOfPages())
		{
			// Close any open page
			if(openPage > -1) 
				closePage();			
			// Get Page
			PDPage page = getPage(pageNum);
			if(page == null) 
				return -1;
			try {
				// Start a new content stream
				contentStream = new PDPageContentStream(document, page, true, true, true);
				openPage = pageNum;
				return pageNum;
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		return -1;
	}
		
	/**
	 * Close page 
	 */
	public void closePage(){
		try {
			// Close content stream;
			contentStream.close();
			openPage = -1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get default ink color
	 * @return
	 */
	public Color getDefaultInkColor() {
		return defaultInkColor;
	}

	/**
	 * Set default ink color
	 * @param defaultInkColor
	 */
	public void setDefaultiNKColor(Color defaultInkColor) {
		this.defaultInkColor = defaultInkColor;
	}
	
	/**
	 * Write text to open page in default color.
	 * @param x 	Position x
	 * @param y 	Position y
	 * @param text	Text to write
	 * @param font	Text font
	 * @return
	 */
	public int writeText(float x, float y, String text, int font){
		// Default color is black
		return writeText(x, y, text, font, getDefaultInkColor());
	}
	
	/**
	 * Write text to open page.
	 * @param x		Position x
	 * @param y		Position y
	 * @param text	Text to write
	 * @param font 	Text font
	 * @param color	Text color
	 * @return
	 */
	public int writeText(float x, float y, String text, int font, Color color){
        try {
        	Point2D.Float pt = translate(x, y);
        	contentStream.beginText();
			contentStream.setFont(fontPlain, font);
			contentStream.setNonStrokingColor(color);
			contentStream.setTextTranslation(pt.x, pt.y);
			contentStream.drawString(text);
			contentStream.endText();
			return 1;
        } catch (IOException e) {			
        	e.printStackTrace();
        	return -1;
        }                
	}
	
	public void writeImageCenter(float x, float y, String filename){
		try {			
			BufferedImage awtImage = ImageIO.read(new File(filename));
			float imgWidth = awtImage.getWidth();
			float imgHeight = awtImage.getHeight();			
			Rectangle2D.Float rect = centerPositionImage(x, y, imgWidth, imgHeight);
			writeImage(rect.x, rect.y, rect.width, rect.height, awtImage);				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeImageCenter(float x, float y, BufferedImage awtImage){
		float imgWidth = awtImage.getWidth();
		float imgHeight = awtImage.getHeight();			
		Rectangle2D.Float rect = centerPositionImage(x, y, imgWidth, imgHeight);
		writeImage(rect.x, rect.y, rect.width, rect.height, awtImage);				
	}
	
	public void writeImage(float x, float y, float width, float height, BufferedImage awtImage){		
		try {
			Point2D.Float pt = translate(x, y);								
			PDXObjectImage ximage = new PDPixelMap(document, awtImage);			
			contentStream.drawXObject(ximage, pt.x, pt.y, width, height);
		} catch (Exception ex) {
			ex.printStackTrace();			
		}
	}
	
	public Rectangle2D.Float centerPositionImage(float x, float y, float imgWidth, float imgHeight){
		Rectangle2D.Float rect = new Rectangle2D.Float();
		
		float posx = 0f;
		float posy = 0f;
		
		// in inch for 200dpi
		imgWidth = imgWidth / dpi;
		imgHeight = imgHeight / dpi;
		
		// in user unit
		imgWidth = imgWidth * upi;
		imgHeight = imgHeight * upi ;
		float aspectRatio = imgWidth / imgHeight;
		
		if(imgWidth > pageWidth){
			imgWidth = pageWidth  - 72;
			imgHeight = imgWidth / aspectRatio;
			posx = 36;
			// Adjust y position according to image height
			posy = y+imgHeight;
		}						
		else{				
			posx = (pageWidth - imgWidth)/2;
			// Adjust y position according to image height
			posy = y+imgHeight;
		}
			
		rect.x = posx;
		rect.y = posy;
		rect.width = imgWidth;
		rect.height= imgHeight;
		return rect;
	}
	
	public Point2D.Float translate(float x, float y){
		Point2D.Float pt = new Point2D.Float();
		pt.x = x;
		pt.y = pageHeight - y;
		return pt;
	}
	
	public void printFile(String printerName, String jobName){
		PrintService services[] = PrinterJob.lookupPrintServices();
		int serviceId = -1;
		int i=0;
        for(PrintService ps : services){        	
        	if(ps.getName().equals(printerName)){
        		serviceId = i;
        		break;
        	}
        	i++;
        }
        if(serviceId >= 0){        	
        	try {
        		PrinterJob job = PrinterJob.getPrinterJob();        		
        		job.setPrintService(services[serviceId]);        		
				job.setJobName(jobName);                	
				document.silentPrint(job);
			} catch (PrinterException e) {
				e.printStackTrace();
			}
        }
	}
	
	/**
	 *	Save PDF to file 
	 */
	public void saveFile(){
		try {
			document.save(filename);			
		} catch (COSVisitorException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
		
	/**
	 *	Close PDF document 
	 */
	public void close(){
		try {
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
