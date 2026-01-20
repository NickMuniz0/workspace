package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.DoencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DoencaService {
    @Autowired
    private DoencaRepository doencaRepository;

    public List<Doenca> listarTodas() {
        return doencaRepository.findAll();
    }

    public Optional<Doenca> buscarPorId(String id) {
        return doencaRepository.findById(id);
    }

    public void salvar(Doenca doenca) {
        doencaRepository.save(doenca);
    }

    public void deletarPorId(String id) {
        doencaRepository.deleteById(id);
    }

    public List<Doenca> buscarPorNome(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return doencaRepository.findAll();
        }
        return doencaRepository.findByNomeContainingIgnoreCase(filtro);
    }

    public void deletarPorIds(List<String> ids) {
        doencaRepository.deleteAllById(ids);
    }
}
