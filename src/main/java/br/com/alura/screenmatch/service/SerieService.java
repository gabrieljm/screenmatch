package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repository.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    private SerieDTO converteDados(Serie serie) {
        return new SerieDTO(serie.getId(), serie.getTitulo(), serie.getTotalTemporadas(), serie.getAvaliacao(),
                serie.getGenero(), serie.getAtores(), serie.getPoster(), serie.getSinopse());
    }

    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(this::converteDados)
                .collect(Collectors.toList());
    }

    private EpisodioDTO converteDados(Episodio episodio) {
        return new EpisodioDTO(episodio.getTemporada(), episodio.getNumero(), episodio.getTitulo());
    }

    private List<EpisodioDTO> converteDadosEpisodios(List<Episodio> episodios) {
        return episodios.stream()
                .map(this::converteDados)
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            return converteDados(serie.get());
        }

        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            return serie.get().getEpisodios().stream()
                    .map(episodio -> new EpisodioDTO(
                            episodio.getTemporada(), episodio.getNumero(), episodio.getTitulo()))
                    .collect(Collectors.toList());
        }

        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Integer numero) {
        return converteDadosEpisodios(repository.obterEpisodiosPorTemporada(id, numero));
    }

    public List<SerieDTO> obterSeriesPorCategoria(String genero) {
        Categoria categoria = Categoria.fromPortugues(genero);

        return converteDados(repository.findByGenero(categoria));
    }

    public List<EpisodioDTO> obterTop5EpisodiosPorSerie(Long id) {
        return converteDadosEpisodios(repository.obterTop5EpisodiosPorSerie(id));
    }
}
