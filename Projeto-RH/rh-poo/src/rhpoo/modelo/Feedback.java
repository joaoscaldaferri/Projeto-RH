package rhpoo.modelo;

//classe que representa o feedback do funcionário
public class Feedback {
    private int id;
    private Funcionario funcionario;
    private String avaliador;
    private String comentario;
    private int nota;
    private String data;

    public Feedback(int id, Funcionario funcionario, String avaliador,
                    String comentario, int nota, String data) {
        this.id = id;
        this.funcionario = funcionario;
        this.avaliador = avaliador;
        this.comentario = comentario;
        this.nota = nota;
        this.data = data;
    }

    public String exibirResumo() {
        return "ID: " + id
                + " | Funcionário: " + funcionario.getNome()
                + " | Avaliador: " + avaliador
                + " | Nota: " + nota
                + " | Data: " + data
                + " | Comentário: " + comentario;
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
    public String getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(String avaliador) {
        this.avaliador = avaliador;
    }
    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public int getNota() {
        return nota;
    }
    public void setNota(int nota) {
        this.nota = nota;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}