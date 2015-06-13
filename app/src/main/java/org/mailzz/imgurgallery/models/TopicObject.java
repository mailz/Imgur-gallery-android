package org.mailzz.imgurgallery.models;

/**
 * AppWell.org
 * Created by dmitrijtrandin on 12.06.15.
 */
public class TopicObject {
   private int id ;
   private String name;
   private String description;
   private String css;
   private boolean ephemeral;

    public TopicObject() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    public void setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
    }
}
