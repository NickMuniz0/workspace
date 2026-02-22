package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.DoencaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Specification.DoencaSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DoencaService {
    @Autowired
    private DoencaRepository doencaRepository;

    public List<Doenca> listarTodasSemPagina() {
        return doencaRepository.findAll();
    }


    public Page<Doenca> listarTodas(Pageable pageable) {
        return doencaRepository.findAll(pageable);
    }

    public Optional<Doenca> buscarPorId(Long id) {
        return doencaRepository.findById(id);
    }

    public void salvar(Doenca doenca) {
        doencaRepository.save(doenca);
    }

    public void deletarPorId(Long id) {
        doencaRepository.deleteById(id);
    }

    public Page<Doenca> buscaSpecification(String filtro, Pageable pageable) {
        String termo = filtro != null ? filtro.trim().toLowerCase() : "";

        if (termo.isEmpty()) {
            return doencaRepository.findAll(pageable);
        }
        return doencaRepository.findAll(DoencaSpecification.contemTermo(termo), pageable);
    }

    public void deletarPorIds(List<Long> ids) {
        doencaRepository.deleteAllById(ids);
    }
}
