package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Jugador {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Usuario usuario;
    private Integer puntaje = 0;


    public Integer getPuntaje() { return puntaje; }

    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }
}
