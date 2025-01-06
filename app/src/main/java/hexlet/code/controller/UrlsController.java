package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlUtil;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void create(Context ctx) throws SQLException {
        try {
            var name = UrlUtil.uriToUrl(ctx.formParamAsClass("url", String.class)
                    .check(v -> !v.isEmpty(), "Field cannot be empty")
                    .get()
                    .toLowerCase()
                    .strip());
            if (UrlRepository.isUrlExist(name)) {
                ctx.sessionAttribute("flash-message", "Страница уже существует");
                ctx.sessionAttribute("flash-type", "info");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                var url = new Url(name);
                UrlRepository.save(url);
                ctx.sessionAttribute("flash-message", "Страница успешно добавлена");
                ctx.sessionAttribute("flash-type", "success");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            ctx.sessionAttribute("flash-message", "URL-адрес указан некорректно");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlashMessage(ctx.consumeSessionAttribute("flash-message"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Page not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }
}
