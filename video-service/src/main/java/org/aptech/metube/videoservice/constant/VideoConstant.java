package org.aptech.metube.videoservice.constant;

/*
    @author: Dinh Quang Anh
    Date   : 8/28/2023
    Project: metube-server
*/
public class VideoConstant {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int CHUNK_SIZE = 314700;
    public static final int BYTE_RANGE = 1024;

    private VideoConstant() {
    }
}
