package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * Class handling the association between images of the folder "images" and
 * their corresponding byte, which is the first 3 digits of their file name.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class ImageCollection {
    private final Map<Byte, Image> images = new HashMap<>();

    /**
     * Constructor of ImageCollection. Extracts all the images in the directory
     * with the given name and associates them with their corresponding byte,
     * which is the first 3 digits of their file name.
     * 
     * @param dirName
     *            - name of the directory from which the images are to be
     *            extracted
     */
    public ImageCollection(String dirName) {
        try {
            File dir = new File(ImageCollection.class.getClassLoader()
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

    /**
     * Returns the image corresponding to the given byte.
     * 
     * @param byteForImage
     *            - byte of the image to be returned
     * @return the image corresponding to the given byte
     * @throws NoSuchElementException
     *             - if there images doesn't contain the given byte
     */
    public Image image(byte byteForImage) throws NoSuchElementException {
        if (!images.containsKey(byteForImage)) {
            System.out.println(byteForImage);
            throw new NoSuchElementException();
        }

        return images.get(byteForImage);
    }

    public Image imageOrNull(byte byteForImage) {
        return images.getOrDefault(byteForImage, null);
    }
}
