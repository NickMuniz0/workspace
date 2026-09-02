package io.github.prefeituradorecife.jogospessoaidosa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.RepresentanteRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private RepresentanteRepository representanteRepository;

    @Mock
    private EntityManager entityManager;

    private PessoaService pessoaService;

    @BeforeEach
    void setUp() throws Exception {
        pessoaService = new PessoaService(pessoaRepository, representanteRepository);
        Field field = PessoaService.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(pessoaService, entityManager);
    }

    @Test
    void listarTodasSemPaginaDeveOrdenarPorNome() {
        Pessoa joao = new Pessoa();
        joao.setId(2L);
        joao.setNome("João");

        Pessoa ana = new Pessoa();
        ana.setId(1L);
        ana.setNome("Ana");

        when(pessoaRepository.findAll()).thenReturn(List.of(joao, ana));

        List<Pessoa> resultado = pessoaService.listarTodasSemPagina();

        assertEquals(2, resultado.size());
        assertEquals("Ana", resultado.get(0).getNome());
        assertEquals("João", resultado.get(1).getNome());
    }

    @Test
    void buscarPorIdDeveLancarExcecaoQuandoNaoExiste() {
        when(pessoaRepository.findById(10L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pessoaService.buscarPorId(10L));

        assertEquals("Pessoa não encontrada: 10", exception.getMessage());
    }

    @Test
    void buscarDisponiveisSemFiltroDeveRetornarPaginaCompleta() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pessoa> pagina = new PageImpl<>(List.of(new Pessoa()), pageable, 1);
        when(pessoaRepository.findAll(pageable)).thenReturn(pagina);

        Page<Pessoa> resultado = pessoaService.buscarDisponiveis(null, null, pageable);

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void removerEquipeDasPessoasDevePersistirMudancas() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(4L);
        pessoa.setNome("Maria");
        pessoa.getEquipes().add(new io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe(10L));

        when(pessoaRepository.findAllById(List.of(4L))).thenReturn(List.of(pessoa));

        pessoaService.removerEquipeDasPessoas(10L, List.of(4L));

        verify(pessoaRepository).saveAll(List.of(pessoa));
    }

    @Test
    void deletarPorIdsDeveRemoverRepresentantesEUsuarios() {
        pessoaService.deletarPorIds(List.of(1L, 2L));

        verify(representanteRepository).deleteByPessoaIds(List.of(1L, 2L));
        verify(pessoaRepository).deleteAllByIdInBatch(List.of(1L, 2L));
    }

    @Test
    void buscarPorIdsDeveManterOrdemInformada() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(2L);
        pessoa1.setNome("B");

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(1L);
        pessoa2.setNome("A");

        when(pessoaRepository.findAllById(List.of(2L, 1L))).thenReturn(List.of(pessoa1, pessoa2));

        List<Pessoa> resultado = pessoaService.buscarPorIds(List.of(2L, 1L));

        assertEquals(2L, resultado.get(0).getId());
        assertEquals(1L, resultado.get(1).getId());
    }

    @Test
    void contarTodosDeveRetornarQuantidade() {
        when(pessoaRepository.count()).thenReturn(8L);

        long resultado = pessoaService.contarTodos();

        assertEquals(8L, resultado);
    }
}
