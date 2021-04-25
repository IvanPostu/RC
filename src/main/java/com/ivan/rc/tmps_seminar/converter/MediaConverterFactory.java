package com.ivan.rc.tmps_seminar.converter;

public class MediaConverterFactory implements ConverterFactory {

    @Override
    public IConverter create(Class<?> inputFileFormat, Class<?> outputFileFormat) {
        return new MediaConverter(inputFileFormat, outputFileFormat);
    }

}