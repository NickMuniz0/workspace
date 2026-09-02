package io.github.prefeituradorecife.jogospessoaidosa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.prefeituradorecife.jogospessoaidosa.Dtos.LoginDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.RegistroDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Dtos.ResetSenhaDTO;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Usuario;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.UsuarioRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
    }

    @Test
    void registrarDeveSalvarUsuarioCriptografado() {
        RegistroDTO dto = new RegistroDTO("Ana", "ana@email.com", "123456", "palavra");
        when(passwordEncoder.encode("123456")).thenReturn("hash123");

        Usuario usuario = new Usuario();
        usuario.setNome("Ana");
        usuario.setEmail("ana@email.com");
        usuario.setSenha("hash123");
        usuario.setPalavraMagica("palavra");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario salvo = usuarioService.registrar(dto);

        assertEquals("hash123", salvo.getSenha());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void loginDeveAutenticarUsuarioQuandoSenhaForValida() {
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@email.com");
        usuario.setSenha("hash123");
        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "hash123")).thenReturn(true);

        Usuario autenticado = usuarioService.login(new LoginDTO("ana@email.com", "123456"));

        assertEquals(usuario, autenticado);
    }

    @Test
    void loginDeveLancarExcecaoQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioService.login(new LoginDTO("inexistente@email.com", "123")));

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void resetSenhaDeveAtualizarSenhaQuandoPalavraMagicaCorreta() {
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@email.com");
        usuario.setPalavraMagica("palavra");
        usuario.setSenha("senhaAntiga");
        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("novaSenha123")).thenReturn("hashNova");

        Usuario atualizado = usuarioService.resetSenha(new ResetSenhaDTO("ana@email.com", "novaSenha123", "palavra"));

        assertEquals("hashNova", atualizado.getSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void resetSenhaDeveLancarExcecaoQuandoPalavraMagicaInvalida() {
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@email.com");
        usuario.setPalavraMagica("palavraCorreta");
        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioService.resetSenha(new ResetSenhaDTO("ana@email.com", "novaSenha123", "errada")));

        assertEquals("Palavra mágica inválida", exception.getMessage());
    }
}
