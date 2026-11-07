package it.uniroma3.siw.touristhouse.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class RegolaPrezzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nome;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInizio;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFine;

    @NotNull
    private String variazionePrezzo;

    @NotNull
    private Integer minimoNottiSoggiorno;

    @ManyToMany(mappedBy = "regolePrezzo")
    private List<Camera> camere;

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

    public List<Camera> getCamere() {return camere;}

    public void setCamere(List<Camera> camere) {this.camere = camere;}

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