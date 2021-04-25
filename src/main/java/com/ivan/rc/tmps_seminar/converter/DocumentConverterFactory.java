package com.ivan.rc.tmps_seminar.converter;

public class DocumentConverterFactory implements ConverterFactory {

    @Override
    public IConverter create(Class<?> inputFileFormat, Class<?> outputFileFormat) {
        return new DocumentConverter(inputFileFormat, outputFileFormat);
    }

}
