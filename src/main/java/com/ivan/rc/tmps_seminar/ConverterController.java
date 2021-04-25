package com.ivan.rc.tmps_seminar;

import java.util.HashMap;
import java.util.Map;

import com.ivan.rc.tmps_seminar.converter.ConverterFactory;
import com.ivan.rc.tmps_seminar.converter.IConverter;
import com.ivan.rc.tmps_seminar.file.ResultFile;
import com.ivan.rc.tmps_seminar.format.DOCFormat;
import com.ivan.rc.tmps_seminar.format.IFormat;
import com.ivan.rc.tmps_seminar.format.JPEGFormat;
import com.ivan.rc.tmps_seminar.format.MP3Format;
import com.ivan.rc.tmps_seminar.format.MP4Format;
import com.ivan.rc.tmps_seminar.format.PDFFormat;
import com.ivan.rc.tmps_seminar.format.PNGFormat;

public class ConverterController {

    public ResultFile doPost(RequestData data, ResultFile userFile) {

        Map<String, Class<? extends IFormat>> formaters = new HashMap<>();
        formaters.put(".mp3", MP3Format.class);
        formaters.put(".mp4", MP4Format.class);

        formaters.put(".docx", DOCFormat.class);
        formaters.put(".mp4", PDFFormat.class);

        formaters.put(".jpeg", JPEGFormat.class);
        formaters.put(".mp4", PNGFormat.class);

        Class<? extends IFormat> inputFormat = formaters.get(data.getRequestExtension());
        Class<? extends IFormat> outFormat = formaters.get(data.getFileExtension());

        ConverterFactory factory = ConverterFactory.buildFactory(inputFormat, outFormat);

        IConverter converter = factory.create(inputFormat, outFormat);
        ResultFile result = converter.convert(userFile);

        return result;
    }

}
