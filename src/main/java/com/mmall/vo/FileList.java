package com.mmall.vo;

import java.io.File;
import java.util.List;

/**
 *
 **/
public class FileList {

    private String remotePath;

    private List<File > files;

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
