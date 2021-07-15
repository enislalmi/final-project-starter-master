package ui.custom;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import org.openstreetmap.gui.jmapviewer.Style;
import util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CustomizedMap extends MapMarkerCircle {

    public static final double defaultMarkerSize = 30.0;

    public BufferedImage profilePhoto;
    public String tweet;
    public String profilePhotoURL;



    public CustomizedMap(Layer layer, Coordinate coordinate, Color color, String profilePhotoURL, String tweet) {
        super(layer, "", coordinate, defaultMarkerSize, STYLE.FIXED, getDefaultStyle());
        setColor(color);
        setBackColor(color);
        profilePhoto = Util.imageFromURL(profilePhotoURL);
        this.tweet = tweet;
        this.profilePhotoURL= profilePhotoURL;
    }


    public static double getDefaultMarkerSize() {
        return defaultMarkerSize;
    }

    public BufferedImage getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(BufferedImage profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    @Override
    public void paint (Graphics graphics, Point position, int radius)
    {
        int size = radius*2;
        if (graphics instanceof Graphics2D && this.getColor() != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            Composite graphicComposite = graphics2D.getComposite();
            graphics2D.setComposite(AlphaComposite.getInstance(3));
            graphics2D.setPaint(this.getBackColor());
            graphics2D.fillOval(position.x - radius, position.y - radius, size, size);
            graphics2D.setComposite(graphicComposite);
            graphics2D.drawImage(profilePhoto, position.x - 10, position.y - 10, 20,20,null);
    }
}}

