package com.tarsier.marianz.tarsierscape.models;

public class WordInfo {
    public long id;
    public String word;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(long searchCount) {
        this.searchCount = searchCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long searchCount;
    public boolean active;

    public WordInfo(long id, String word, long searchCount, boolean active) {
        this.id = id;
        this.word = word;
        this.searchCount = searchCount;
        this.active = active;
    }

    public WordInfo() {
    }
}
