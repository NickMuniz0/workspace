package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.github.prefeituradorecife.jogospessoaidosa.Dtos.LoginDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.ResetSenhaDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.RegistroDTO;

@Controller
public class PageController {
    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("usuario", new RegistroDTO("", "", "", ""));
        return "registro";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("login", new LoginDTO("", ""));
        return "login";
    }

    @GetMapping("/resetar")
    public String recuperarForm(Model model) {
        model.addAttribute("reset", new ResetSenhaDTO("", "", ""));
        return "resetar-senha";
    }

    @GetMapping("/index")
    public String perfil(Model model, Principal principal) {
        String usuarioLogado = principal != null ? principal.getName() : "Visitante";
        model.addAttribute("usuario", usuarioLogado);
        return "index";
    }
}