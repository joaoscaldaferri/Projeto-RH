package br.com.rhmanager.model;

//classe que representar as férias e a escala
public class FeriasEscala {
    private int id;
    private Funcionario funcionario;
    private String dataInicioFerias;
    private String dataFimFerias;
    private String turnoEscala;

    public FeriasEscala(int id, Funcionario funcionario, String dataInicioFerias,
                        String dataFimFerias, String turnoEscala) {
        this.id = id;
        this.funcionario = funcionario;
        this.dataInicioFerias = dataInicioFerias;
        this.dataFimFerias = dataFimFerias;
        this.turnoEscala = turnoEscala;
    }
    public String exibirResumo() {
        return "ID: " + id
                + " | Funcionário: " + funcionario.getNome()
                + " | Cargo: " + funcionario.getCargo()
                + " | Férias: " + dataInicioFerias + " até " + dataFimFerias
                + " | Turno: " + turnoEscala;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Funcionario getFuncionario() {
        return funcionario;
    }
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    public String getDataInicioFerias() {
        return dataInicioFerias;
    }
    public void setDataInicioFerias(String dataInicioFerias) {
        this.dataInicioFerias = dataInicioFerias;
    }
    public String getDataFimFerias() {
        return dataFimFerias;
    }
    public void setDataFimFerias(String dataFimFerias) {
        this.dataFimFerias = dataFimFerias;
    }
    public String getTurnoEscala() {
        return turnoEscala;
    }
    public void setTurnoEscala(String turnoEscala) {
        this.turnoEscala = turnoEscala;
    }
}