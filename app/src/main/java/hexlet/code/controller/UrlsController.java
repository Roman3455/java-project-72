package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlsRepository;
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
                    .check(v -> !v.isEmpty(), "Поле не может быть пустым")
                    .get()
                    .toLowerCase()
                    .strip());
            if (UrlsRepository.isUrlExist(name)) {
                UrlUtil.setSessionAttribute(ctx, "Страница уже существует", "info");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                var url = new Url(name);
                UrlsRepository.save(url);
                UrlUtil.setSessionAttribute(ctx, "Страница успешно добавлена", "success");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            UrlUtil.setSessionAttribute(ctx, "URL-адрес указан некорректно", "danger");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlsRepository.getEntities();
        var lastChecks = UrlCheckRepository.getLastChecks();
        var page = new UrlsPage(urls, lastChecks);
        page.setFlashMessage(ctx.consumeSessionAttribute("flash-message"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Page not found"));
        var urlChecks = UrlCheckRepository.getChecks(id);
        var page = new UrlPage(url, urlChecks);
        page.setFlashMessage(ctx.consumeSessionAttribute("flash-message"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/show.jte", model("page", page));
    }
}
