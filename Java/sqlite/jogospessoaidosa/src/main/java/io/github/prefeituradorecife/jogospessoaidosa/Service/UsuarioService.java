package io.github.prefeituradorecife.jogospessoaidosa.Service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Dtos.LoginDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.ResetSenhaDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.RegistroDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Usuario;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Usuario registrar(RegistroDTO dto) {
        Usuario u = new Usuario();
        u.setNome(dto.nome());
        u.setEmail(dto.email());
        u.setPalavraMagica(dto.palavraMagica());
        u.setSenha(encoder.encode(dto.senha()));
        return repo.save(u);
    }

    @Async("virtualTaskExecutor")
    public CompletableFuture<Usuario> registrarAsync(RegistroDTO dto) {
        return CompletableFuture.completedFuture(registrar(dto));
    }

    public Usuario login(LoginDTO dto) {
        Usuario u = repo.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!encoder.matches(dto.senha(), u.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return u;
    }

    @Async("virtualTaskExecutor")
    public CompletableFuture<Usuario> loginAsync(LoginDTO dto) {
        return CompletableFuture.completedFuture(login(dto));
    }

    public Usuario resetSenha(ResetSenhaDTO dto) {
        Usuario u = repo.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!u.getPalavraMagica().equals(dto.palavraMagica())) {
            throw new RuntimeException("Palavra mágica inválida");
        }

        u.setSenha(encoder.encode(dto.senhaNova()));
        repo.save(u);

        return u;
    }

    @Async("virtualTaskExecutor")
    public CompletableFuture<Usuario> resetSenhaAsync(ResetSenhaDTO dto) {
        return CompletableFuture.completedFuture(resetSenha(dto));
    }
}