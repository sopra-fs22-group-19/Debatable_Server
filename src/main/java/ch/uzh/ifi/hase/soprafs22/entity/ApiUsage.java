package ch.uzh.ifi.hase.soprafs22.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



//for dev and test only
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiUsage {

    private static ApiUsage instance = new ApiUsage();

    private int character_count;
    private int character_limit;

    private ApiUsage() {
    }

    public int getCharacter_count() {
        return character_count;
    }

    public void setCharacter_count(int character_count) {
        this.character_count = character_count;
    }

    public int getCharacter_limit() {
        return character_limit;
    }

    public void setCharacter_limit(int character_limit) {
        this.character_limit = character_limit;
    }


    public static ApiUsage getUsage() {
        if (instance == null) {
            instance = new ApiUsage();
        }
        return instance;
    }



    @Override
    public String toString() {
        return "Usage{" +
                "Count='" + character_count + '\'' +
                ", Limit=" + character_limit +
                '}';
    }
}
