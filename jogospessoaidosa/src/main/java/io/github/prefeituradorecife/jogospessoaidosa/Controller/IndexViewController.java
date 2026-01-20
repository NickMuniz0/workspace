package io.github.prefeituradorecife.jogospessoaidosa.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


@Controller
@RequestMapping("/")
public class IndexViewController {

    @GetMapping
    public String showIndex() throws IOException {
        return "index";
    }
}
