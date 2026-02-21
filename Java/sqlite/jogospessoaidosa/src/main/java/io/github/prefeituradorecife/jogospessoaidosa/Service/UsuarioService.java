package io.github.prefeituradorecife.jogospessoaidosa.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Dtos.LoginDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.ResetSenhaDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.RegistroDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Usuario;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public Usuario registrar(RegistroDTO dto) {
        Usuario u = new Usuario();
        u.setNome(dto.nome());
        u.setEmail(dto.email());
        u.setPalavraMagica(dto.palavraMagica());
        u.setSenha(encoder.encode(dto.senha()));
        return repo.save(u);
    }

    public Usuario login(LoginDTO dto) {
        Usuario u = repo.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!encoder.matches(dto.senha(), u.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return u; // retorna o usuário autenticado
    }

    public Usuario resetSenha(ResetSenhaDTO dto) {
        Usuario u = repo.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // valida palavra mágica
        if (!u.getPalavraMagica().equals(dto.palavraMagica())) {
            throw new RuntimeException("Palavra mágica inválida");
        }

        // atualiza a senha com a nova (encodada)
        u.setSenha(encoder.encode(dto.senhaNova()));
        repo.save(u);

        return u; // retorna o usuário autenticado    
    }
}