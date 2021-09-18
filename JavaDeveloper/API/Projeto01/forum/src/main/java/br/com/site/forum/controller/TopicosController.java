package br.com.site.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.site.forum.controller.form.AtualizacaoTopicoForm;
import br.com.site.forum.controller.form.DetalhesTopicoDto;
import br.com.site.forum.controller.form.TopicoDto;
import br.com.site.forum.controller.form.TopicoForm;
import br.com.site.forum.modelo.Topico;
import br.com.site.forum.repository.CursoRepository;
import br.com.site.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
    
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDto> lista(String nomedoCurso){

            if(nomedoCurso == null){
                List<Topico> topicos = topicoRepository.findAll(); 
                return TopicoDto.converter(topicos);

            }else{
                List<Topico> topicos = topicoRepository.findByCurso_Nome(nomedoCurso); 
                return TopicoDto.converter(topicos);
            }
    }
    
    @PostMapping
    @RequestMapping(value="/topicos",method=RequestMethod.POST)
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid  TopicoForm form,
     UriComponentsBuilder uriBuilder){
            Topico topico = form.converter(cursoRepository);
            topicoRepository.save(topico);
            URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
            return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")    
    public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable Long id){
            //(@PathVariable("id") Long codigo){    
            // Topico topico = topicoRepository.getById(id);
            Optional<Topico> topico = topicoRepository.findById(id);
            if(topico.isPresent()){
                return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));    
            }
            return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id,
     @RequestBody @Valid  AtualizacaoTopicoForm form){

            Optional<Topico> optional = topicoRepository.findById(id);
                if(optional.isPresent()){
                    Topico topico =   form.atualizar(id,topicoRepository);
                    return ResponseEntity.ok(new TopicoDto(topico));
                }
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id){
            Optional<Topico> optional = topicoRepository.findById(id);
            if(optional.isPresent()){
                topicoRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();


       

    }

}
