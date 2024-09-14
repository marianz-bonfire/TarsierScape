package com.tarsier.marianz.tarsierscape.models;

public class History {
    public long id;
    public String word;
    public long searchCount;
    public boolean active;

    public History(long id, String word, long searchCount) {
        this.id = id;
        this.word = word;
        this.searchCount = searchCount;
    }

    public History() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", searchCount=" + searchCount +
                '}';
    }
}
