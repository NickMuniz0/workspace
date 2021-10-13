package br.com.site.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Cacheable(value="listaDeTopicos")
    public Page<TopicoDto> lista(@RequestParam(required = false) 
                String nomedoCurso, 
                @PageableDefault(sort = "id", direction= Direction.DESC,page=0,size=10) Pageable paginacao
                // @RequestParam int pagina, 
                // @RequestParam int qtd,
                // @RequestParam String ordenacao
                ){
            // page=0&size=10@sort=id,desc

            // Pageable paginacao = PageRequest.of(pagina,qtd,Direction.ASC,ordenacao );

            if(nomedoCurso == null){
                // List<Topico> topicos = topicoRepository.findAll(); 
                Page<Topico> topicos = topicoRepository.findAll(paginacao); 

                return TopicoDto.converter(topicos);

            }else{
                // List<Topico> topicos = topicoRepository.findByCurso_Nome(nomedoCurso); 
                Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomedoCurso, paginacao); 

                return TopicoDto.converter(topicos);
            }
    }
    
    @PostMapping
    @RequestMapping(method=RequestMethod.POST)
    @Transactional
    @CacheEvict(value="listaDeTopicos",allEntries = true)
    public ResponseEntity<TopicoDto> cadastrar(
        @RequestBody @Valid  TopicoForm form,
        UriComponentsBuilder uriBuilder){
            Topico topico = form.converter(cursoRepository);
            topicoRepository.save(topico);
            URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
            return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")    
    @CacheEvict(value="listaDeTopicos",allEntries = true)
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
    @CacheEvict(value="listaDeTopicos",allEntries = true)
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
    @CacheEvict(value="listaDeTopicos",allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id){
            Optional<Topico> optional = topicoRepository.findById(id);
            if(optional.isPresent()){
                topicoRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();


       

    }

}
