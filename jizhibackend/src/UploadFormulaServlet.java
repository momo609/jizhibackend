import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class UploadFormulaServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");  //���ñ���  
        //��ô����ļ���Ŀ����  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //��ȡ�ļ���Ҫ�ϴ�����·��  
        String path = request.getRealPath("/upload");  
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        factory.setRepository(new File(path));  
        //���� ����Ĵ�С
        factory.setSizeThreshold(1024*1024) ;  
        //�ļ��ϴ�����  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        try {  
            //�����ϴ�����ļ�  
        	
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request);  
            for(FileItem item : list){  
                //��ȡ��������  
                String name = item.getFieldName();  
                //�����ȡ�� ����Ϣ����ͨ�� �ı� ��Ϣ  
                if(item.isFormField()){                     
                    //��ȡ�û�����������ַ���,��Ϊ���ύ�������� �ַ������͵�  
                    String value = item.getString() ;  
                    request.setAttribute(name, value);  
                }else{  
                    //��ȡ·����  
                    
                    String filename = UUID.randomUUID().toString()+".png";
                    request.setAttribute(name, filename);  
                    //д��������  
                    File f=new File(path,filename);
                    item.write(f);//�������ṩ��  
                    setAlpha(Color.WHITE, f);
                    
                  
                    System.out.println("�ϴ��ɹ���"+filename);
                    response.getWriter().print(filename);//��·�����ظ��ͻ���
                }  
            }  
              
        } catch (Exception e) {  
        	System.out.println("�ϴ�ʧ��");
        	response.getWriter().print("�ϴ�ʧ�ܣ�"+e.getMessage());
        }  
		
	}
	 private static void setAlpha(Color color,File imageFile) {
		  /**
		   * ���Ӳ�����
		   * ��ȡͼƬ�����Ƴɰ�͸��
		   */
		 int markerRGB = color.getRGB() | 0xFF000000;
		  try {
		   Image image=ImageIO.read(imageFile);
		    ImageIcon imageIcon = new ImageIcon(image);
		    BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(),imageIcon.getIconHeight()
		        , BufferedImage.TYPE_4BYTE_ABGR);
		    Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
		    g2D.drawImage(imageIcon.getImage(), 0, 0,
		                         imageIcon.getImageObserver());
		    //ѭ��ÿһ�����ص㣬�ı����ص��Alphaֵ
		    int alpha = 11;
		    for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
		      for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
		        int rgb = bufferedImage.getRGB(j2, j1);
		        if((rgb | 0xFF000000) == markerRGB)
		        rgb = ( (alpha + 1) << 24) | (rgb & 0x00ffffff);
		        bufferedImage.setRGB(j2, j1, rgb);
		      }
		    }
		    g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
		    
		    //����ͼƬΪPNG
		 
		    ImageIO.write(bufferedImage, "png",  imageFile);
		  }
		  catch (Exception e) {
		    e.printStackTrace();
		  }
		 
		}

	public static Image makeColorTransparent(Image im, final Color color) {
	    ImageFilter filter = new RGBImageFilter() {
	        // the color we are looking for... Alpha bits are set to opaque
	        public int markerRGB = color.getRGB() | 0xFF000000;

	        @Override
	        public final int filterRGB(int x, int y, int rgb) {
	            if ((rgb | 0xFF000000) == markerRGB) {
	                // Mark the alpha bits as zero - transparent
	                return 0x00FFFFFF;
	            } else {
	                // nothing to do
	                return rgb;
	            }
	        }
	    };

	    ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	}
	public static BufferedImage toBufferedImage(Image image) {  
	    if (image instanceof BufferedImage) {  
	        return (BufferedImage)image;  
	     }  
	    
	    // This code ensures that all the pixels in the image are loaded  
	     image = new ImageIcon(image).getImage();  
	    
	    // Determine if the image has transparent pixels; for this method's  
	    // implementation, see e661 Determining If an Image Has Transparent Pixels  
	    //boolean hasAlpha = hasAlpha(image);  
	    
	    // Create a buffered image with a format that's compatible with the screen  
	     BufferedImage bimage = null;  
	     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();  
	    try {  
	        // Determine the type of transparency of the new buffered image  
	        int transparency = Transparency.BITMASK; 
	         
	    
	        // Create the buffered image  
	         GraphicsDevice gs = ge.getDefaultScreenDevice();  
	         GraphicsConfiguration gc = gs.getDefaultConfiguration();  
	         bimage = gc.createCompatibleImage(  
	         image.getWidth(null), image.getHeight(null), transparency);  
	     } catch (HeadlessException e) {  
	        // The system does not have a screen  
	     }  
	    
	    if (bimage == null) {  
	    
	        int type = BufferedImage.TYPE_INT_RGB;  
	         bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);  
	     }  

	     Graphics g = bimage.createGraphics();  
	     g.drawImage(image, 0, 0, null);  
	     g.dispose();  
	    return bimage;  
	} 

}
