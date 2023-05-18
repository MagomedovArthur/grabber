package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Date;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static int pageNumber = 1;
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=%s",
            SOURCE_LINK, pageNumber);

    public static void main(String[] args) throws IOException {
        while (pageNumber <= 5) {
            Connection connection = Jsoup.connect(PAGE_LINK);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Elements dateElements = row.select(".vacancy-card__date");
                Element dateElement = dateElements.first().child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String date = dateElement.attr("datetime");
                System.out.printf("%s %s %s %n", vacancyName, link, date);
            });
            pageNumber++;
        }
    }
}