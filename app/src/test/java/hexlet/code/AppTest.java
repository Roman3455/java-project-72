package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public final class AppTest {
    Javalin app;
    static MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @BeforeAll
    static void setMockWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        var htmlResponse = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Test Title</title>
            <meta name="description" content="Test description">
        </head>
        <body>
            <h1>Test Header</h1>
        </body>
        </html>
            """;

        mockWebServer.enqueue(new MockResponse().setBody(htmlResponse));
        mockWebServer.start();
    }

    @AfterAll
    static void shutdownMockWebServer() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testRootPath() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.body().string()).contains("Анализатор страниц");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testUrlsPath() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.body().string()).contains("Список сайтов");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testUrlPath() throws SQLException {
        var url = new Url("https://ya.ru");
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.body().string()).contains("URL");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testAddToUrlsPage1() throws SQLException {
        var url = new Url("https://ya.ru");
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.body().string()).contains("https://ya.ru");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testAddToUrlsPage2() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=https://google.com");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://google.com");
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    void testCheckUrl() throws SQLException {
        var mockUrl = mockWebServer.url("/").toString();
        var url = new Url(mockUrl);
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.post(NamedRoutes.urlChecksPath(url.getId()));
            var urlChecks = UrlCheckRepository.getChecks(url.getId());
            var lastUrlChecks = UrlCheckRepository.getLastChecks();
            assertThat(response.code()).isEqualTo(200);
            assertThat(urlChecks.size()).isEqualTo(1);
            assertThat(lastUrlChecks.get(1L).getUrlId()).isEqualTo(1);
            assertThat(lastUrlChecks.get(1L).getId()).isEqualTo(1);
            assertThat(lastUrlChecks.get(1L).getCreatedAt()).isToday();
            assertThat(lastUrlChecks.get(1L).getH1()).contains("Test Header");
            assertThat(lastUrlChecks.get(1L).getDescription()).contains("Test description");
            assertThat(lastUrlChecks.get(1L).getTitle()).contains("Test Title");
            assertThat(lastUrlChecks.get(1L).getStatusCode()).isEqualTo(200);
        });
    }
}
