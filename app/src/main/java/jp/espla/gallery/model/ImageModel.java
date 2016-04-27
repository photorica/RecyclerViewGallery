package jp.espla.gallery.model;

import java.io.Serializable;

public class ImageModel implements Serializable {

    public long id;
    public String name;
    public String description;
    public String mimeType;
    public int width;
    public int height;
    public int size;
    public double latitude;
    public double longitude;
    public int orientation;
    public String path;
    public String dateAdded;
    public String dateModified;

    @Override
    public String toString() {
        return String.format(
                "ImageModel {%n    id: %d%n    name: %s%n    description: %s%n    mimeType: %s%n    width: %d%n    height: %d%n    size: %d%n    latitude: %f%n    longitude: %f%n    orientation: %d%n    path: %s%n    dateAdded: %s%n    dateModified: %s%n}",
                id, name, description, mimeType, width, height, size, latitude, longitude, orientation, path, dateAdded, dateModified
        );
    }
}
