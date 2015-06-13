package org.mailzz.imgurgallery.models;

import java.io.Serializable;

/**
 * AppWell.org
 * Created by dmitrijtrandin on 12.06.15.
 */
public class ResponseObject implements Serializable {

    private Object data;
    private boolean success;
    private int status;

    public ResponseObject() {

    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

