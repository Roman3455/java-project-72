package hexlet.code.util;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import io.javalin.http.Context;

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
        return String.format("%s://%s%s",
                url.getProtocol(),
                url.getHost(),
                (url.getPort() == -1 ? "" : ":" + uri.getPort()));
    }

    public static void setSessionAttribute(Context ctx, String message, String type) {
        ctx.sessionAttribute("flash-message", message);
        ctx.sessionAttribute("flash-type", type);
    }

    public static Url getUrlFormDB(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var name = resultSet.getString("name");
        var createdAt = resultSet.getTimestamp("created_at");

        var url = new Url(id, name);
        url.setCreatedAt(createdAt);
        return url;
    }

    public static UrlCheck getUrlCheckFromDB(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var statusCode = resultSet.getInt("status_code");
        var title = resultSet.getString("title");
        var h1 = resultSet.getString("h1");
        var description = resultSet.getString("description");
        var urlId = resultSet.getLong("url_id");
        var createdAt = resultSet.getTimestamp("created_at");

        var urlCheck = new UrlCheck(id, statusCode, title, h1, description, urlId);
        urlCheck.setCreatedAt(createdAt);
        return urlCheck;
    }
}
