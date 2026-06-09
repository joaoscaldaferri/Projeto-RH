package br.com.rhmanager.model;

//classe que representar a entrevista
//ordenar as entrevistas por prioridade
public class Entrevista implements Comparable<Entrevista> {
    private int id;
    private Candidato candidato;
    private String cargoPretendido;
    private int urgencia;
    private String data;
    private String horario;

    public Entrevista(int id, Candidato candidato, String cargoPretendido,
                      int urgencia, String data, String horario) {
        this.id = id;
        this.candidato = candidato;
        this.cargoPretendido = cargoPretendido;
        this.urgencia = urgencia;
        this.data = data;
        this.horario = horario;
    }

    //calcula a prioridade da entrevista
    public int calcularPrioridade() {
        int prioridade = urgencia;

        String cargo = cargoPretendido.toLowerCase();

        if (cargo.contains("gerente") || cargo.contains("coordenador")) {
            prioridade += 3;
        } else if (cargo.contains("analista") || cargo.contains("supervisor")) {
            prioridade += 2;
        } else {
            prioridade += 1;
        }

        return prioridade;
    }

    //ordena de maior para a menor prioridade
    @Override
    public int compareTo(Entrevista outraEntrevista) {
        return Integer.compare(outraEntrevista.calcularPrioridade(), this.calcularPrioridade());
    }
    public String exibirResumo() {
        return "ID: " + id
                + " | Candidato: " + candidato.getNome()
                + " | Cargo: " + cargoPretendido
                + " | Urgência: " + urgencia
                + " | Prioridade calculada: " + calcularPrioridade()
                + " | Data: " + data
                + " | Horário: " + horario;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Candidato getCandidato() {
        return candidato;
    }
    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }
    public String getCargoPretendido() {
        return cargoPretendido;
    }
    public void setCargoPretendido(String cargoPretendido) {
        this.cargoPretendido = cargoPretendido;
    }

    public int getUrgencia() {
        return urgencia;
    }
    public void setUrgencia(int urgencia) {
        this.urgencia = urgencia;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }
    public void setHorario(String horario) {
        this.horario = horario;
    }
}