package com.songify.song.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "song")
public class Song {

    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }
    public Song(Long id, String name, String artist) {
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String artist;
}
