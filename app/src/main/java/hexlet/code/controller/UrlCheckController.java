package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlUtil;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;

import java.sql.SQLException;

public class UrlCheckController {

    public static void checkCreate(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        HttpResponse<String> response;
        try {
            response = Unirest.get(url.getName()).asString();
            Unirest.shutDown();
        } catch (Exception e) {
            UrlUtil.setSessionAttribute(ctx, "Некорректный URL-адрес", "danger");
            ctx.redirect(NamedRoutes.urlPath(urlId));
            return;
        }
        var document = Jsoup.parse(response.getBody());

        var statusCode = response.getStatus();
        var title = document.title();
        var h1 = document.select("h1").first();
        var description = document.select("meta[name=description]").first();

        var urlCheck = new UrlCheck(statusCode, urlId);
        urlCheck.setTitle(title.isEmpty() ? null : title);
        urlCheck.setH1(h1 == null ? null : h1.text());
        urlCheck.setDescription(description == null ? null : description.attr("content"));
        UrlCheckRepository.save(urlCheck);

        UrlUtil.setSessionAttribute(ctx, "Страница успешно проверена", "success");
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
