package io.github.prefeituradorecife.jogospessoaidosa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.data.domain.Sort;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.DoencaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Service.DoencaService;

@ExtendWith(MockitoExtension.class)
class DoencaServiceTest {

    @Mock
    private DoencaRepository doencaRepository;

    private DoencaService doencaService;

    @BeforeEach
    void setUp() {
        doencaService = new DoencaService(doencaRepository);
    }

    @Test
    void listarTodasSemPaginaDeveOrdenarPorNome() {
        Doenca doenca = new Doenca();
        doenca.setNome("Hipertensão");
        when(doencaRepository.findAll(Sort.by("nome").ascending())).thenReturn(List.of(doenca));

        List<Doenca> resultado = doencaService.listarTodasSemPagina();

        assertEquals(1, resultado.size());
        assertEquals("Hipertensão", resultado.get(0).getNome());
    }

    @Test
    void buscarPorIdDeveRetornarDoencaSeExistir() {
        Doenca doenca = new Doenca();
        doenca.setId(5L);
        doenca.setNome("Diabetes");
        when(doencaRepository.findById(5L)).thenReturn(Optional.of(doenca));

        Optional<Doenca> resultado = doencaService.buscarPorId(5L);

        assertTrue(resultado.isPresent());
        assertEquals("Diabetes", resultado.get().getNome());
    }

    @Test
    void salvarDeveDelegarAoRepository() {
        Doenca doenca = new Doenca();
        doenca.setNome("Asma");

        doencaService.salvar(doenca);

        verify(doencaRepository).save(doenca);
    }

    @Test
    void buscarPorSpecificationDeveRetornarPaginaFiltrada() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doenca> pagina = new PageImpl<>(List.of(new Doenca()));
        when(doencaRepository.findAll((org.springframework.data.jpa.domain.Specification<Doenca>) any(), eq(pageable))).thenReturn(pagina);

        Page<Doenca> resultado = doencaService.buscaSpecification("asma", pageable);

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void contarTodosDeveRetornarQuantidade() {
        when(doencaRepository.count()).thenReturn(7L);

        long resultado = doencaService.contarTodos();

        assertEquals(7L, resultado);
    }
}
