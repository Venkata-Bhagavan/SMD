package app.mr.venky.smd.objects;

import com.google.firebase.Timestamp;

public class SmdObject {
    private String name;
    private Timestamp timestamp;
    private String image;

    public SmdObject(String name, Timestamp timestamp, String image) {
        this.name = name;
        this.timestamp = timestamp;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
