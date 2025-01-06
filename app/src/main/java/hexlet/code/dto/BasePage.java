package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BasePage {
    private String flashMessage;
    private String flashType;
    private List<Object> errors;
}
