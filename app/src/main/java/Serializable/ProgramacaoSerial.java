package Serializable;

import java.io.Serializable;

/**
 * Created by TI on 23/03/2016.
 */
public class ProgramacaoSerial implements Serializable {

    private int id_programacao;
    private String horarios;
    private String descricaoPrograma;
    private String diaprograma;


    public ProgramacaoSerial() {

        this.id_programacao = 0;
        this.horarios = "";
        this.descricaoPrograma = "";
        this.diaprograma = "";
    }

    public int getId_programacao() {
        return id_programacao;
    }

    public void setId_programacao(int id_programacao) {
        this.id_programacao = id_programacao;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getDescricaoPrograma() {
        return descricaoPrograma;
    }

    public void setDescricaoPrograma(String descricaoPrograma) {
        this.descricaoPrograma = descricaoPrograma;
    }

    public String getDiaprograma() {
        return diaprograma;
    }

    public void setDiaprograma(String diaprograma) {
        this.diaprograma = diaprograma;
    }
}




