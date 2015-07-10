package io.indico.api.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Created by Chris on 6/23/15.
 */
public class ImageUtils {
    public static String encodeImage(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.encodeBase64String(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageString;
    }

    public static List<BufferedImage> convertToImage(List<?> images, int size) throws IOException {
        List<BufferedImage> convertedInput = new ArrayList<>();
        for (Object entry : images) {
            if (entry instanceof File) {
                convertedInput.add(convertToImage((File) entry, size));
            } else if (entry instanceof String) {
                convertedInput.add(convertToImage((String) entry, size));
            } else {
                throw new IllegalArgumentException(
                        "imageCall method only supports lists of Files and lists of Strings"
                );
            }
        }
        return convertedInput;
    }

    public static BufferedImage convertToImage(File imageFile, int size) throws IOException {
        if (size == -1) {
            return ImageIO.read(imageFile);
        }

        return Scalr.resize(ImageIO.read(imageFile), size);
    }

    public static BufferedImage convertToImage(String filePath, int size) throws IOException {
        return convertToImage(new File(filePath), size);
    }

    public static String grabType(String filePath) throws IOException {
        return grabType(new File(filePath));
    }

    public static String grabType(File imageFile) throws IOException {
        return FilenameUtils.getExtension(imageFile.getName());
    }

    public static String grabType(List<?> images) {
        String type;
        Object entry = images.get(0);

        if (entry instanceof File) {
            type = FilenameUtils.getExtension(((File) entry).getName());
        } else if (entry instanceof String) {
            type = FilenameUtils.getExtension((String) entry);
        } else {
            throw new IllegalArgumentException(
                    "imageCall method only supports lists of Files and lists of Strings"
            );
        }

        return type;
    }
}
