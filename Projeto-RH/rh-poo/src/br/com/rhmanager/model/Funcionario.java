package br.com.rhmanager.model;

public class Funcionario extends Pessoa {
    private String cargo;
    private String setor;
    private String turno;

    public Funcionario(int id, String nome, String cpf, String email, String telefone,
                       String cargo, String setor, String turno) {
        super(id, nome, cpf, email, telefone);
        this.cargo = cargo;
        this.setor = setor;
        this.turno = turno;
    }
    @Override
    public String exibirResumo() {
        return "Funcionário: " + getNome()
                + " | CPF: " + getCpf()
                + " | Cargo: " + cargo
                + " | Setor: " + setor
                + " | Turno: " + turno;
    }

    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public String getSetor() {
        return setor;
    }
    public void setSetor(String setor) {
        this.setor = setor;
    }
    public String getTurno() {
        return turno;
    }
    public void setTurno(String turno) {
        this.turno = turno;
    }
}