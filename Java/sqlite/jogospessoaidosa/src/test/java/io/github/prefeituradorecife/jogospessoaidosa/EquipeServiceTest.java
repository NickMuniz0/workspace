package io.github.prefeituradorecife.jogospessoaidosa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.EquipeRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class EquipeServiceTest {

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    private EquipeService equipeService;

    @BeforeEach
    void setUp() {
        equipeService = new EquipeService(equipeRepository, pessoaRepository);
    }

    @Test
    void salvarOuAtualizarEquipeDeveSalvarRepresentantes() {
        Equipe equipe = new Equipe();
        equipe.setNome("Equipe Beta");
        equipe.setRpa(RPA.RPA2);

        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1L);
        pessoa1.setNome("Ana");

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2L);
        pessoa2.setNome("Bia");

        when(pessoaRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(pessoa1, pessoa2));

        equipeService.salvarOuAtualizarEquipe(equipe, List.of(1L, 2L));

        verify(equipeRepository).save(any(Equipe.class));
    }

    @Test
    void buscarPorIdDeveLancarExcecaoQuandoEquipeNaoExiste() {
        when(equipeRepository.findByIdWithRepresentantes(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> equipeService.buscarPorId(99L));

        assertEquals("Equipe não encontrada", exception.getMessage());
    }

    @Test
    void adicionarPessoasNaEquipeDevePersistirParticipantes() {
        Equipe equipe = new Equipe();
        equipe.setId(10L);
        equipe.setNome("Equipe A");
        when(equipeRepository.findById(10L)).thenReturn(Optional.of(equipe));

        Pessoa pessoa = new Pessoa();
        pessoa.setId(3L);
        pessoa.setNome("Carlos");
        when(pessoaRepository.findAllById(List.of(3L))).thenReturn(List.of(pessoa));

        equipeService.adicionarPessoasNaEquipe(10L, List.of(3L));

        verify(equipeRepository).save(equipe);
    }

    @Test
    void buscaSpecificationDeveRetornarPagina() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Equipe> pagina = new PageImpl<>(List.of(new Equipe()));
        when(equipeRepository.findAll((org.springframework.data.jpa.domain.Specification<Equipe>) any(), eq(pageable))).thenReturn(pagina);

        Page<Equipe> resultado = equipeService.buscaSpecification("equipe", pageable);

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void contarTodosDeveRetornarQuantidade() {
        when(equipeRepository.count()).thenReturn(12L);

        long resultado = equipeService.contarTodos();

        assertEquals(12L, resultado);
    }
}
