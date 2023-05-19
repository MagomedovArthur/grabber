package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static int pageNumber = 1;
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);
    private static String description = "";
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) throws IOException {
        HabrCareerDateTimeParser habrCareerDateTimeParser = new HabrCareerDateTimeParser();
        HabrCareerParse habrCareerParse = new HabrCareerParse(habrCareerDateTimeParser);
        List<Post> postList = habrCareerParse.list(PAGE_LINK);
        for (Post posts : postList) {
            System.out.println(posts);
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

    @Override
    public List<Post> list(String pageLink) throws IOException {
        List<Post> result = new ArrayList<>();
        while (pageNumber <= 5) {
            String page = String.format("%s?page=%d", pageLink, pageNumber);
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
                LocalDateTime created = this.dateTimeParser.parse(date);
                result.add(new Post(vacancyName, link, descriptions, created));
            });
            description = "";
            pageNumber++;
        }
        return result;
    }
}