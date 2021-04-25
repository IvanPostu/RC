package com.ivan.rc.tmps_seminar.file;

public class ResultFile {
    private byte[] data = "".getBytes();

    public ResultFile(byte[] data) {
        this.data = data;
    }

    public byte[] getBytes() {
        return data;
    }

    public void setBytes(byte[] data) {
        this.data = data;
    }

}
