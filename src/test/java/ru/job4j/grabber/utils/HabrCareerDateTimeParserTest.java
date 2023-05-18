package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {

    @Test
    void parseTimeFirstExample() {
        HabrCareerDateTimeParser habrCareerDateTimeParser = new HabrCareerDateTimeParser();
        String timeFormatForParsing = "2023-05-10T18:34:36+03:00";
        String expectedTimeFormat = "2023-05-10T18:34:36";
        assertThat(habrCareerDateTimeParser.parse(timeFormatForParsing)).isAfterOrEqualTo(expectedTimeFormat);
    }

    @Test
    void parseTimeSecondExample() {
        HabrCareerDateTimeParser habrCareerDateTimeParser = new HabrCareerDateTimeParser();
        String timeFormatForParsing = "2023-05-17T12:52:07-03:00";
        String expectedTimeFormat = "2023-05-17T12:52:07";
        assertThat(habrCareerDateTimeParser.parse(timeFormatForParsing)).isAfterOrEqualTo(expectedTimeFormat);
    }
}