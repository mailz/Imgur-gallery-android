package org.mailzz.imgurgallery.models;

import java.io.Serializable;

/**
 * AppWell.org
 * Created by dmitrijtrandin on 15.06.15.
 */
public class ObjectForDetailView implements Serializable {

    private String id;
    private boolean isAlbum;
    private String imageLink;
    private String fullLink;

    public ObjectForDetailView(String id, boolean isAlbum, String imageLink, String fullLink) {
        this.id = id;
        this.isAlbum = isAlbum;
        this.imageLink = imageLink;
        this.fullLink = fullLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public void setIsAlbum(boolean isAlbum) {
        this.isAlbum = isAlbum;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getFullLink() {
        return fullLink;
    }

    public void setFullLink(String fullLink) {
        this.fullLink = fullLink;
    }
}
