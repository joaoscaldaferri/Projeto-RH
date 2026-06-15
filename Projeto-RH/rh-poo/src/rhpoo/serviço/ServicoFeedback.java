package rhpoo.serviço;

import rhpoo.modelo.Feedback;
import rhpoo.modelo.Funcionario;
import rhpoo.validaçao.ValidadorEntrada;

import java.util.ArrayList;
import java.util.List;

//classe que controla feedbacks dos funcionario
public class ServicoFeedback {
    private List<Feedback> feedbacks;
    private int proximoId;

    public ServicoFeedback() {
        this.feedbacks = new ArrayList<>();
        this.proximoId = 1;
    }

    //cadastro de feedback
    //feedback é colocado na posição 0 para ficar na ordem inversa
    public boolean cadastrarFeedback(Funcionario funcionario, String avaliador,
                                     String comentario, int nota, String data) {

        if (funcionario == null
                || !ValidadorEntrada.textoValido(avaliador)
                || !ValidadorEntrada.textoValido(comentario)
                || !ValidadorEntrada.notaValida(nota)
                || !ValidadorEntrada.dataValida(data)) {
            return false;
        }
        Feedback feedback = new Feedback(
                proximoId,
                funcionario,
                avaliador,
                comentario,
                nota,
                data
        );
        feedbacks.add(0, feedback);
        proximoId++;
        return true;
    }

    // Lista completa por ordem inversa
    public List<Feedback> listarTodos() {
        return feedbacks;
    }

    //consulta por ID
    public Feedback buscarPorId(int id) {
        for (Feedback feedback : feedbacks) {
            if (feedback.getId() == id) {
                return feedback;
            }
        }
        return null;
    }

    //consulta por nome do funcionario
    public List<Feedback> buscarPorFuncionario(String nome) {
        List<Feedback> encontrados = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            if (feedback.getFuncionario().getNome().toLowerCase().contains(nome.toLowerCase())) {
                encontrados.add(feedback);
            }
        }
        return encontrados;
    }

    //atualiza feedback
    public boolean atualizarFeedback(int id, String avaliador,
                                     String comentario, int nota, String data) {

        Feedback feedback = buscarPorId(id);
        if (feedback == null) {
            return false;
        }
        if (!ValidadorEntrada.textoValido(avaliador)
                || !ValidadorEntrada.textoValido(comentario)
                || !ValidadorEntrada.notaValida(nota)
                || !ValidadorEntrada.dataValida(data)) {
            return false;
        }
        feedback.setAvaliador(avaliador);
        feedback.setComentario(comentario);
        feedback.setNota(nota);
        feedback.setData(data);
        return true;
    }

    //excluir registro
    public boolean excluirFeedback(int id) {
        Feedback feedback = buscarPorId(id);
        if (feedback != null) {
            feedbacks.remove(feedback);
            return true;
        }
        return false;
    }

    //relatorio impresso no console
    public void gerarRelatorioConsole() {
        System.out.println("      RELATÓRIO DE FEEDBACKS:      ");
        System.out.println("Total de feedbacks cadastrados: " + feedbacks.size());
        for (Feedback feedback : feedbacks) {
            System.out.println(feedback.exibirResumo());
        }
    }
}