package tanks;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private Map<String, Image> imageAlbum;

    public ImageLoader() {
        imageAlbum = new HashMap<String, Image>();
    }

    public void putImageInAlbum(String imageFileName, String name) {
        try {
            imageAlbum.put(name, new Image(imageFileName));
        }
        catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
        }
    }

    public void putMapIntoImageAlbum(Map<String, String> hashMap) {
        for (HashMap.Entry<String, String> entry : hashMap.entrySet()) {
            putImageInAlbum(entry.getKey(), entry.getValue());
        }
    }

    public Image getImageFromAlbum(String name) {
        return imageAlbum.get(name);
    }
}