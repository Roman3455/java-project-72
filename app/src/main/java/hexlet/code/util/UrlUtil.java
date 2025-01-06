package hexlet.code.util;

import hexlet.code.model.Url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UrlUtil {

    public static String uriToUrl(String input) throws URISyntaxException,
            MalformedURLException,
            IllegalArgumentException {
        var uri = new URI(input);
        var url = uri.toURL();
        return String.format("%s://%s:%s",
                url.getProtocol(),
                url.getHost(),
                (url.getPort() == -1 ? "" : ":" + uri.getPort()));
    }

    public static Url getUrlFormDB(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var name = resultSet.getString("name");
        var createdAt = resultSet.getTimestamp("created_at");

        var url = new Url(name);
        url.setId(id);
        url.setCreatedAt(createdAt);

        return url;
    }
}
