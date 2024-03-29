package app.mr.venky.smd.objects;

import com.google.firebase.Timestamp;

public class SmdObject {
    private String name;
    private Timestamp timestamp;
    private float accuracy;
    private String image;
    private String id;

    public SmdObject() {
    }

    public SmdObject(String name, Timestamp timestamp, float accuracy, String image, String id) {
        this.name = name;
        this.timestamp = timestamp;
        this.accuracy = accuracy;
        this.image = image;
        this.id = id;
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

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SmdObject{" +
                "name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", accuracy=" + accuracy +
                ", image='" + image + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
