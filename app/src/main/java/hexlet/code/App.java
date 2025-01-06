package hexlet.code;

import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlsController;
import hexlet.code.util.Configurator;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.io.IOException;
import java.sql.SQLException;

public final class App {

    public static void main(String[] args) throws IOException, SQLException {
        Javalin app = getApp();
        app.start(Configurator.getPort());
    }

    public static Javalin getApp() throws IOException, SQLException {
        Configurator.getDatabaseConnection();

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(Configurator.createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.post(NamedRoutes.urlsPath(), UrlsController::create);
        app.get(NamedRoutes.urlsPath(), UrlsController::index);
        app.get(NamedRoutes.urlPath("{id}"), UrlsController::show);

        return app;
    }
}
