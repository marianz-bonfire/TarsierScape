package com.tarsier.marianz.tarsierscape.models;

public class WordHeader {
    public WordHeader() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String header;
    private String description;
    private String count;

    public WordHeader(String header, String description, String count) {
        this.header = header;
        this.description = description;
        this.count = count;
    }
}
