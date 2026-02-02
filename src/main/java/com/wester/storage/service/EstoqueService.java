package com.wester.storage.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.wester.storage.dto.EstoquePosicaoDTO;
import com.wester.storage.dto.ItemEstoqueUpsertRequestDTO;
import com.wester.storage.model.ItemEstoque;
import com.wester.storage.model.Nivel;
import com.wester.storage.model.Produto;
import com.wester.storage.persistence.EstoquePosicaoProjection;
import com.wester.storage.repository.EstoqueRepository;
import com.wester.storage.repository.ItemEstoqueRepository;
import com.wester.storage.repository.NivelRepository;
import com.wester.storage.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ItemEstoqueRepository itemEstoqueRepository;
    private final NivelRepository nivelRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(
            EstoqueRepository estoqueRepository,
            ItemEstoqueRepository itemEstoqueRepository,
            NivelRepository nivelRepository,
            ProdutoRepository produtoRepository
    ) {
        this.estoqueRepository = estoqueRepository;
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.nivelRepository = nivelRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<EstoquePosicaoDTO> buscarMapaPorArea(Long areaId) {
        List<EstoquePosicaoProjection> rows = estoqueRepository.buscarMapaPorArea(areaId);

        return rows.stream()
                .map(row -> new EstoquePosicaoDTO(
                        row.getFileiraId(),
                        row.getFileiraIdentificador(),
                        row.getGradeId(),
                        row.getGradeIdentificador(),
                        row.getNivelId(),
                        row.getNivelIdentificador(),
                        row.getNivelOrdem(),
                        row.getItemEstoqueId(),
                        row.getQuantidade(),
                        row.getProdutoId(),
                        row.getCodigoSistemaWester(),
                        row.getNomeModelo(),
                        row.getCor(),
                        row.getDescricao()
                ))
                .toList();
    }

    public Optional<ItemEstoque> buscarPrimeiroItemPorNivel(Long nivelId) {
        return itemEstoqueRepository.findFirstByNivelId(nivelId);
    }

    public ItemEstoque salvarPorNivel(Long nivelId, ItemEstoqueUpsertRequestDTO request, Long usuarioId) {
        return salvarPorNivelInterno(nivelId, request, usuarioId);
    }

    private ItemEstoque salvarPorNivelInterno(Long nivelId, ItemEstoqueUpsertRequestDTO request, Long usuarioId) {
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
}
