package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String REDIRECT_INDEX = "redirect:/index";

    private final UsuarioService usuarioService;

    public ApiAuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute("usuario") RegistroDTO dto, RedirectAttributes redirectAttributes) {
        usuarioService.registrar(dto);
        redirectAttributes.addFlashAttribute("sucesso", "Usuário registrado com sucesso!");
        return REDIRECT_LOGIN;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("login") LoginDTO dto,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {
        try {
            Usuario usuario = usuarioService.login(dto);
            autenticarUsuario(usuario, session);
            redirectAttributes.addFlashAttribute("sucesso", "Login realizado com sucesso!");
            return REDIRECT_INDEX;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return REDIRECT_LOGIN;
        }
    }

    @PostMapping("/resetar")
    public String reset(@ModelAttribute("reset") ResetSenhaDTO dto,
                        RedirectAttributes redirectAttributes) {
        try {
            usuarioService.resetSenha(dto);
            redirectAttributes.addFlashAttribute("sucesso", "Senha redefinida com sucesso!");
            return REDIRECT_LOGIN;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return REDIRECT_LOGIN;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        SecurityContextHolder.clearContext();
        return REDIRECT_LOGIN;
    }

    private void autenticarUsuario(Usuario usuario, HttpSession session) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(usuario.getEmail(), null, authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
    }
}
