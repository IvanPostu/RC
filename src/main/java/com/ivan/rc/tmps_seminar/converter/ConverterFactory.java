package com.ivan.rc.tmps_seminar.converter;

import com.ivan.rc.tmps_seminar.format.AbstractDocumentFormat;
import com.ivan.rc.tmps_seminar.format.AbstractImageFormat;

public interface ConverterFactory {
    IConverter create(Class<?> inputFileFormat, Class<?> outputFileFormat);

    static ConverterFactory buildFactory(Class<?> inputFileFormat, Class<?> outputFileFormat) {
        if (inputFileFormat.getSuperclass() == AbstractDocumentFormat.class) {
            return new DocumentConverterFactory();
        }
        if (inputFileFormat.getSuperclass() == AbstractImageFormat.class) {
            return new ImageConverterFactory();
        }

        return new MediaConverterFactory();
    }

}
