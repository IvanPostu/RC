package com.ivan.rc.tmps_seminar.format;

public interface IFormat {

    byte[] formatToIntermediateData(byte[] data);

    byte[] formatToThisFileFormat(byte[] data);

}
