package ch.uzh.ifi.hase.soprafs22.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class TranslationResponse {

    public TranslationResponse() {
    }

    @JsonProperty
    private List<Translations> translations;

    public List<Translations> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translations> translations) {
        this.translations = translations;
    }
}
