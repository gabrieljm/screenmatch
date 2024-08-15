package br.com.alura.screenmatch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EpisodioDTO(Integer temporada,
                          @JsonProperty("numeroEpisodio") Integer numero,
                          String titulo) {
}
