package ch.uzh.ifi.hase.soprafs22.entity;


import com.fasterxml.jackson.annotation.JsonProperty;


public class TranslationResponse {

    public TranslationResponse() {
    }

    @JsonProperty
    private Translations Translations;

    public Translations getTranslations() {
        return Translations;
    }

    public void setTranslations(Translations translations) {
        Translations = translations;
    }
}
