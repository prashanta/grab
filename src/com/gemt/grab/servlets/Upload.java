package com.gemt.grab.servlets;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.gemt.grab.beans.FolderBean;
import com.gemt.grab.beans.UserProfileBean;
import com.gemt.grab.dao.UserProfileDAO;
import com.gemt.grab.utility.Utility;
import com.gemt.pdffactory.PDFDoc;

@WebServlet("/Upload")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private boolean isMultipart;
	private int MAX_REQUEST_SIZE = 50000 * 1024;
	private int MAX_MEMORY_SIZE = 40000 * 1024;	
	String storagePath;
	

	public Upload() {
		super();
	}  
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {				
		String username = "";
		String password = "";
		String path = "";
		String partName = "";
		String filename = "";
		String ext = "";
		
		// Check that we have a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);				
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		if( !isMultipart ){
			out.println("No file to upload");			
			return;
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MAX_MEMORY_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        InputStream input = null;
		try{ 
			// Parse the request
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if(item.isFormField()){
                	if(item.getFieldName().equals("filename")){
                		partName = item.getString();                		
                	}                	
                }                	
                else if (!item.isFormField()) {
                	input = item.getInputStream();
                	ext = item.getName();
                	ext = ext.substring(ext.lastIndexOf('.'));
                }
            }            
            if(input != null){
            	// Prepare to save image to remote folder            	
            	DateFormat dateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
                Date date = new Date();
                String dateString = dateFormat.format(date);
                filename = partName +  "_" + dateString + ext;
                
                int uid = Integer.valueOf(Utility.getCookie("uid", request));
                UserProfileBean profile = UserProfileDAO.getUserProfile(uid);
                username = profile.getUsername();
                password = UserProfileDAO.getPassword(uid);
                int folderIndex = profile.getCurrentFolder();
                FolderBean folder = UserProfileDAO.getCurrentFolder(uid, folderIndex);                
                path = folder.getPath(); 
                
                // Check if it is a remote path or local path
                
                // START If remote path
                String finalPath = path.replaceAll("\\\\", "/");
        		finalPath = "smb:" + (finalPath.indexOf("//")  == -1? ("//" + finalPath) : finalPath);
        		finalPath = finalPath.lastIndexOf("/") == finalPath.length()-1? finalPath : finalPath + "/";
        		
        		BufferedImage resizedImage = null;  
        		
        		if(folder.getImageResize() == 1){
        			Image img = ImageIO.read(input);
            		int h = img.getHeight(null);
            		int w = img.getWidth(null);       		
            		
            		w = (w * 80) / 100;
            		h = (h * 80) / 100;
            		
            		resizedImage = resizeImage(img, w, h);            		
            		ByteArrayOutputStream baos = new ByteArrayOutputStream();
            		ImageIO.write(resizedImage, "jpg", baos);        		            		
            		input = new ByteArrayInputStream(baos.toByteArray());
        		}        		        		
				if(uploadToSamba(username, password, finalPath, filename, input) == 1)
					out.println("1");
				else
					out.println("0");
				
				// END If remote path
				
				// START If local path
				
				// END If local path
				
				// Check if print required
				if(profile.getPrinter() != null){
					if(folder.getName().contains("print")){
						System.out.println("Printing ... " + partName);
						PDFDoc doc = new PDFDoc("sample.pdf");
						doc.createNewPage();
						doc.openPage(1);
						doc.writeText(36, 40, "OPERATION STEP :", 12);
						doc.writeText(36, 60, "REVISION :", 12);
						doc.writeText(36, 80, "PART NUMBER : " + partName, 12);
						dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
						doc.writeText(36, 100, "DATE : " + dateFormat.format(date), 12);
						doc.writeImageCenter(10, 120, resizedImage);			
						doc.closePage();						
						doc.printFile(profile.getPrinter(), partName);
						System.out.println("Printing complete");
						doc.close();
					}
				}					
            }
		}catch(Exception ex) {
			ex.printStackTrace();
			out.println("0");
		}
	}    

	protected int uploadToSamba(String username, String password, String path, String fileName, InputStream is){
		String user = username + ":" + password;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
		path = path + fileName;
		System.out.println("Saving file: " + path);
		try {
			SmbFile sFile = new SmbFile(path, auth);		
			OutputStream os = sFile.getOutputStream();
			final byte[] buf = new byte[16 * 1024 * 1024];
			int len;
			while ((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
			os.close();
			is.close();
			return 1;
		} catch (Exception e) {				
			e.printStackTrace();
			return 0;
		}
	}
	
	public BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        //below three lines are for RenderingHints for better image quality at cost of higher processing time
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }
}