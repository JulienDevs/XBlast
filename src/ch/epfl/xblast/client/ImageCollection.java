package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class ImageCollection {
    private final Map<Byte, Image> images = new HashMap<>();

    public ImageCollection(String dirName) {
        File dir;
        try {
            dir = new File(ImageCollection.class.getClassLoader()
                    .getResource(dirName).toURI());

            for (File file : dir.listFiles()) {
                try {
                    byte byteForImage = Byte
                            .parseByte(file.getName().substring(0, 3));

                    images.put(byteForImage, ImageIO.read(file));
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    public Image image(byte byteForImage) throws NoSuchElementException {
        if (!images.containsKey(byteForImage))
            throw new NoSuchElementException();

        return images.get(byteForImage);
    }

    public Image imageOrNull(byte byteForImage) {
        return images.getOrDefault(byteForImage, null);
    }
}
