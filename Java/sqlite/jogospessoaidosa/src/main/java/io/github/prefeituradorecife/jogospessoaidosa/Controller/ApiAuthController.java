package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.prefeituradorecife.jogospessoaidosa.Dtos.LoginDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.ResetSenhaDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.RegistroDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Usuario;
import io.github.prefeituradorecife.jogospessoaidosa.Service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class ApiAuthController {
    @Autowired private UsuarioService usuarioService;

    @PostMapping("/registro")
    public String registrar(@ModelAttribute RegistroDTO dto, RedirectAttributes redirectAttributes) {
        usuarioService.registrar(dto);
        redirectAttributes.addFlashAttribute("sucesso", "Usuário registrado com sucesso!");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO dto,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {
        try {
            Usuario usuario = usuarioService.login(dto);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(usuario, null, List.of());

            // registra no contexto
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            // salva o contexto na sessão (ESSENCIAL!)
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);

            redirectAttributes.addFlashAttribute("sucesso", "Login realizado com sucesso!");
            return "redirect:/index";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/login";
        }
    }


    @PostMapping("/resetar")
    public String reset(@ModelAttribute ResetSenhaDTO dto) {
        usuarioService.resetSenha(dto);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login"; // redireciona para a página de login com parâmetro
    }


}
