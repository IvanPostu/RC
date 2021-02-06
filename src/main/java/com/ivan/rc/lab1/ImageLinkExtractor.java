package com.ivan.rc.lab1;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ivan
 */
public class ImageLinkExtractor {

    private static final String IMG_REGEXP = "<img\\s+[^>]*src=\"([^\"]*)\"[^>]*>";

    public static List<String> extractImageRelativeLinksFromHtml(String html,
            String[] imageExtensions, String websiteUrl) {

        List<String> result = new LinkedList<>();
        Pattern p = Pattern.compile(IMG_REGEXP);
        Matcher m = p.matcher(html);

        while (m.find()) {
            // String fullImgTag = m.group();
            String imgSrc = m.group(1);

            /**
             * Exclude base64 image
             */
            if (imgSrc.contains("data:image"))
                continue;

            for (int i = 0; i < imageExtensions.length; i++) {
                String imageExtension = imageExtensions[i];



                if (imgSrc.toLowerCase().contains(imageExtension.toLowerCase())) {

                    /**
                     * Convert absolute path to relative
                     */
                    if (imgSrc.startsWith(websiteUrl + '/')) {
                        imgSrc = imgSrc.replace(websiteUrl + '/', "");
                    }


                    result.add(imgSrc);
                    break;
                }

            }
        }

        return result;
    }

}
