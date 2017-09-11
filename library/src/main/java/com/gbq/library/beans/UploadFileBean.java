package com.gbq.library.beans;

/**
 * 类说明：上传文件返回
 * Author: Kuzan
 * Date: 2017/9/7 16:19.
 */
public class UploadFileBean {
    String fileUrl;
    String fileId;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "UploadFileBean{" +
                "fileUrl='" + fileUrl + '\'' +
                ", fileId='" + fileId + '\'' +
                '}';
    }
}
