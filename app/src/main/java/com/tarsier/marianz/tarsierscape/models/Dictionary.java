package com.tarsier.marianz.tarsierscape.models;

public class Dictionary {
    public long id;
    public boolean active;
    public String word;
    public String meaning;

    public Dictionary() {
    }

    public Dictionary(long id, String word, String meaning) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
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

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return "Dictionaries{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                '}';
    }
}
