package com.ivan.rc.lab1;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ivan
 */
public class Utils {

    private static final String IMG_REGEXP = "<img\\s+[^>]" + "*src=\"([^\"]*)\"[^>]*>";

    public List<String> extractImageLinksFromHtml(String html, String[] imageExtensions) {

        List<String> result = new LinkedList<>();
        Pattern p = Pattern.compile(IMG_REGEXP);
        Matcher m = p.matcher(html);

        while (m.find()) {
            // String fullImgTag = m.group();
            String imgSrc = m.group(1);

            for (int i = 0; i < imageExtensions.length; i++) {
                String imageExtension = imageExtensions[i];
                if (imgSrc.toLowerCase().contains(imageExtension.toLowerCase())) {
                    result.add(imgSrc);
                    break;
                }

            }
        }

        return result;
    }

}
