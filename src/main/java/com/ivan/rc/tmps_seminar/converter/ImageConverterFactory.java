package com.ivan.rc.tmps_seminar.converter;

public class ImageConverterFactory implements ConverterFactory {

    @Override
    public IConverter create(Class<?> inputFileFormat, Class<?> outputFileFormat) {
        return new ImageConverter(inputFileFormat, outputFileFormat);
    }

}