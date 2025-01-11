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
public class Url {
    private long id;
    private final String name;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    public Url(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
