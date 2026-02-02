package com.wester.storage.service;

import com.wester.storage.dto.ItemEstoqueUpsertRequestDTO;
import com.wester.storage.model.ItemEstoque;
import com.wester.storage.model.Nivel;
import com.wester.storage.model.Produto;
import com.wester.storage.repository.ItemEstoqueRepository;
import com.wester.storage.repository.NivelRepository;
import com.wester.storage.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ItemEstoqueService {

    private final ItemEstoqueRepository itemEstoqueRepository;
    private final NivelRepository nivelRepository;
    private final ProdutoRepository produtoRepository;

    public ItemEstoqueService(
            ItemEstoqueRepository itemEstoqueRepository,
            NivelRepository nivelRepository,
            ProdutoRepository produtoRepository
    ) {
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.nivelRepository = nivelRepository;
        this.produtoRepository = produtoRepository;
    }

    public Optional<ItemEstoque> buscarPorNivel(Long nivelId) {
        if (nivelId == null) {
            throw new IllegalArgumentException("nivelId não pode ser nulo.");
        }

        boolean nivelExiste = nivelRepository.existsById(nivelId);
        if (!nivelExiste) {
            throw new IllegalArgumentException("Nível não encontrado: " + nivelId);
        }

        return itemEstoqueRepository.findFirstByNivelId(nivelId);
    }

//    public List<ItemEstoque> listarItensPorNivel(Long nivelId) {
//        return itemEstoqueRepository.findByNivelId(nivelId);
//    }

    public Optional<ItemEstoque> buscarPrimeiroItemPorNivel(Long nivelId) {
        return itemEstoqueRepository.findFirstByNivelId(nivelId);
    }

    public ItemEstoque salvarPorNivel(Long nivelId, ItemEstoqueUpsertRequestDTO request, Long usuarioId) {
        if (nivelId == null) {
            throw new IllegalArgumentException("nivelId não pode ser nulo.");
        }

        Nivel nivel = nivelRepository.findById(nivelId)
                .orElseThrow(() -> new IllegalArgumentException("Nível não encontrado: " + nivelId));

        ItemEstoque item = itemEstoqueRepository.findFirstByNivelId(nivelId)
                .orElseGet(() -> {
                    ItemEstoque novo = new ItemEstoque();
                    novo.setNivel(nivel);
                    return novo;
                });

        Produto produto = item.getProduto();
        if (produto == null) {
            produto = new Produto();
        }

        if (request == null) {
            throw new IllegalArgumentException("request não pode ser nulo.");
        }

        if (request.getProduto() != null) {
            produto.setCodigoSistemaWester(request.getProduto().getCodigoSistemaWester());
            produto.setCor(request.getProduto().getCor());
            produto.setNomeModelo(request.getProduto().getNomeModelo());
            produto.setDescricao(request.getProduto().getDescricao());
        }

        Produto produtoSalvo = produtoRepository.save(produto);

        item.setProduto(produtoSalvo);
        item.setQuantidade(request.getQuantidade());
        item.setDataAtualizacao(LocalDateTime.now());

        return itemEstoqueRepository.save(item);
    }

    @Transactional
    public void removerPorNivel(Long nivelId) {
        itemEstoqueRepository.findByNivelId(nivelId)
                .ifPresent(itemEstoqueRepository::delete);
    }
}
