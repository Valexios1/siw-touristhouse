package it.uniroma3.siw.touristhouse.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RegolaPrezzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataInizio;

    private LocalDate dataFine;

    private String variazionePrezzo;

    private Integer minimoNottiSoggiorno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public String getVariazionePrezzo() {
        return variazionePrezzo;
    }

    public void setVariazionePrezzo(String variazionePrezzo) {
        this.variazionePrezzo = variazionePrezzo;
    }

    public Integer getMinimoNottiSoggiorno() {
        return minimoNottiSoggiorno;
    }

    public void setMinimoNottiSoggiorno(Integer minimoNottiSoggiorno) {
        this.minimoNottiSoggiorno = minimoNottiSoggiorno;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegolaPrezzo that = (RegolaPrezzo) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
}