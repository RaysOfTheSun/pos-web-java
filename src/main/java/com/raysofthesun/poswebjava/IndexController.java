package com.raysofthesun.poswebjava;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class IndexController {

    @GetMapping("/")
    public RedirectView swaggerRedirect() {
        return new RedirectView("/swagger-ui/index.html#/");
    }
}
