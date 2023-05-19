package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static int pageNumber = 1;
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);
    private static String description = "";

    public static void main(String[] args) throws IOException {
        while (pageNumber <= 5) {
            String page = String.format("%s?page=%d", PAGE_LINK, pageNumber);
            Connection connection = Jsoup.connect(page);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Elements dateElements = row.select(".vacancy-card__date");
                Element dateElement = dateElements.first().child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String descriptions = "";
                try {
                    descriptions = retrieveDescription(link);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String date = dateElement.attr("datetime");
                System.out.printf("%s %s %s %n %s %n", vacancyName, link, date, descriptions);
            });
            description = "";
            pageNumber++;
        }
    }

    private static String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".basic-section.basic-section--appearance-vacancy-description");
        rows.forEach(row -> {
            Element titleElement = row.select(".faded-content").first();
            description = titleElement.text();
        });
        return description;
    }
}