package br.com.rhmanager.model;

public class Candidato extends Pessoa {
    private String areaAtuacao;
    private int anosExperiencia;
    private String formacao;
    private String resumoProfissional;

    public Candidato(int id, String nome, String cpf, String email, String telefone,
                     String areaAtuacao, int anosExperiencia, String formacao,
                     String resumoProfissional) {
        super(id, nome, cpf, email, telefone);
        this.areaAtuacao = areaAtuacao;
        this.anosExperiencia = anosExperiencia;
        this.formacao = formacao;
        this.resumoProfissional = resumoProfissional;
    }
    @Override
    public String exibirResumo() {
        return "Candidato: " + getNome()
                + " | CPF: " + getCpf()
                + " | Área: " + areaAtuacao
                + " | Experiência: " + anosExperiencia + " anos"
                + " | Formação: " + formacao;
    }
    public String getAreaAtuacao() {
        return areaAtuacao;
    }
    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }
    public int getAnosExperiencia() {
        return anosExperiencia;
    }
    public void setAnosExperiencia(int anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }
    public String getFormacao() {
        return formacao;
    }
    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }
    public String getResumoProfissional() {
        return resumoProfissional;
    }
    public void setResumoProfissional(String resumoProfissional) {
        this.resumoProfissional = resumoProfissional;
    }
}