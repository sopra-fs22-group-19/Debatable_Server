package ch.uzh.ifi.hase.soprafs22.entity;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static ch.uzh.ifi.hase.soprafs22.entity.DebateTopic.readTopicListCSV;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DebateTopicTest {

    @Test
    void loadDefaultUserList_NotEmptyTest() throws CsvValidationException, IOException {
        Path defaultListPath = Paths.get("setup", "defaultTopics.csv");

        DebateTopic.readTopicListCSV(defaultListPath.toString());

        List<DebateTopic> defaultDebateTopicsList = readTopicListCSV(defaultListPath.toString());

        assertFalse(defaultDebateTopicsList.isEmpty());
    }

    @Test
    void loadDefaultUserList_MoreThanFiveTest() throws CsvValidationException, IOException {
        Path defaultListPath = Paths.get("setup", "defaultTopics.csv");

        DebateTopic.readTopicListCSV(defaultListPath.toString());

        List<DebateTopic> defaultDebateTopicsList = readTopicListCSV(defaultListPath.toString());

        assertTrue(defaultDebateTopicsList.size() > 5);
    }


}
