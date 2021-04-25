package com.ivan.rc.tmps_seminar.converter;

import java.lang.reflect.InvocationTargetException;

import com.ivan.rc.tmps_seminar.file.ResultFile;
import com.ivan.rc.tmps_seminar.format.AbstractImageFormat;

public class ImageConverter<I extends AbstractImageFormat, O extends AbstractImageFormat> implements IConverter {
    private Class<I> inputFileFormat;
    private Class<O> outputFileFormat;

    public ImageConverter(Class<I> inputFileFormat, Class<O> outputFileFormat) {
        this.inputFileFormat = inputFileFormat;
        this.outputFileFormat = outputFileFormat;
    }

    @Override
    public ResultFile convert(ResultFile f) {

        try {
            AbstractImageFormat inputFormat = inputFileFormat.getConstructor().newInstance();
            AbstractImageFormat outputFormat = outputFileFormat.getConstructor().newInstance();

            byte[] intermediateData = inputFormat.formatToIntermediateData(f.getBytes());
            byte[] newFormatData = outputFormat.formatToThisFileFormat(intermediateData);

            System.out.println(
                    String.format("Convert from %s to %s", inputFileFormat.getName(), outputFileFormat.getName()));

            return new ResultFile(newFormatData);

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
