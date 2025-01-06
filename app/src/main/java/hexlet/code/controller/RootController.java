package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {
    public static void index(Context ctx) {
        var page = new BasePage();
        page.setFlashMessage(ctx.consumeSessionAttribute("flash-message"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("index.jte", model("page", page));
    }
}
