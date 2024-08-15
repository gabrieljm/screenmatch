package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String ator, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    @Query("SELECT serie FROM Serie serie " +
            "WHERE serie.totalTemporadas <= :totalTemporadas " +
            "  AND serie.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(Integer totalTemporadas, Double avaliacao);

    @Query("SELECT episodio FROM Serie serie JOIN serie.episodios episodio " +
            "WHERE episodio.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT episodio FROM Serie serie JOIN serie.episodios episodio " +
            "WHERE serie = :serie " +
            "  AND episodio.avaliacao IS NOT NULL " +
            "ORDER BY episodio.avaliacao DESC " +
            "LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT episodio FROM Serie serie JOIN serie.episodios episodio " +
            "WHERE serie = :serie " +
            "  AND YEAR(episodio.dataLancamento) >= :anoLancamento ")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

    @Query("SELECT serie FROM Serie serie " +
            " JOIN serie.episodios episodio " +
            "GROUP BY serie " +
            "ORDER BY MAX(episodio.dataLancamento) DESC LIMIT 5")
    List<Serie> encontrarEpisodiosMaisRecentes();

    @Query("SELECT episodio " +
            " FROM Serie serie " +
            " JOIN serie.episodios episodio " +
            "WHERE serie.id = :id " +
            "  AND episodio.temporada = :numero")
    List<Episodio> obterEpisodiosPorTemporada(Long id, Integer numero);

    @Query("SELECT episodio " +
            " FROM Serie serie " +
            " JOIN serie.episodios episodio " +
            "WHERE serie.id = :id " +
            "ORDER BY episodio.avaliacao DESC " +
            "LIMIT 5")
    List<Episodio> obterTop5EpisodiosPorSerie(Long id);
}
