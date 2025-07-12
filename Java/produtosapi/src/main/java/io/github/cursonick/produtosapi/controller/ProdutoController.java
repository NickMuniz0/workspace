package io.github.cursonick.produtosapi.controller;

import io.github.cursonick.produtosapi.model.Produto;
import io.github.cursonick.produtosapi.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    public Produto salvar(@RequestBody Produto produto){
        var id = UUID.randomUUID().toString();
        produto.setId_produto(id);
        produtoRepository.save(produto);
        return produto;
    }

    @GetMapping("{id}")
    public Produto objeterPorId(@PathVariable("id")  String id){
        return produtoRepository.findById(id).orElse(null);
    }

    @DeleteMapping("{id}")
    public void deletar(@PathVariable("id") String id ){
        produtoRepository.deleteById(id);
    }

    @PutMapping("{id}")
    public void atualizar(@PathVariable("id") String id, @RequestBody Produto produto){
        produto.setId_produto(id);
        produtoRepository.save(produto);
    }
    @GetMapping
    public List<Produto> buscar(@RequestParam("nome") String nome){
           return produtoRepository.findByNome(nome);
    }


}
