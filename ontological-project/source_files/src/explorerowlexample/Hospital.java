/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorerowlexample;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author TJR
 */
public class Hospital {

    public static int THUMBNAIL_HEIGHT = 75;
    
    private String name;
    private String imageUrl;
    private String address;
    private double rating;
    private String telephoneNo;
    private List<String> addresses = new ArrayList<>();
    
    private boolean thumbnailSet;
    private ImageIcon image;
    private ImageIcon thumbnail;
    private BufferedImage bufferedImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }    
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public ImageIcon getThumbnail() {
        if(!thumbnailSet){
            return MainFrame.hospitalDefaultImageIcon;
        }
        return thumbnail;
    }

    public void downloadImage() {
        try {
            URL url = new URL(imageUrl);
            System.out.println(imageUrl);
            BufferedImage img = ImageIO.read(url);
            image = new ImageIcon(img);
            
            int height = img.getHeight();
            int width = img.getWidth();
            bufferedImage = img;        
            thumbnail = getResizedImage(THUMBNAIL_HEIGHT*width/height, THUMBNAIL_HEIGHT);       
            thumbnailSet = true;
        } catch (IOException ex) {
            Logger.getLogger(HospitalStrip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ImageIcon getResizedImageFromHeight(int height){
        if(bufferedImage != null)
            return getResizedImage(height*bufferedImage.getWidth()/bufferedImage.getHeight(), height);
        return new ImageIcon();
    }
    public ImageIcon getResizedImage(int width, int height) {
        if(bufferedImage == null)return null;
        try {
            Image dimg = bufferedImage.getScaledInstance(width,
                    height,
                    Image.SCALE_SMOOTH);
            return new ImageIcon(dimg);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
