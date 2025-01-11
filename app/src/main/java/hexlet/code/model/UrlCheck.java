package hexlet.code.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UrlCheck {
    private long id;
    private final int statusCode;
    private String title;
    private String h1;
    private String description;
    private final long urlId;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    public UrlCheck(long id, int statusCode, String title, String h1, String description, long urlId) {
        this.id = id;
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
    }
}
