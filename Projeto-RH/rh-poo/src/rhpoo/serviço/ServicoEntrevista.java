package rhpoo.serviço;

import rhpoo.modelo.Candidato;
import rhpoo.modelo.Entrevista;
import rhpoo.validaçao.ValidadorEntrada;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//classe que controla entrevista de candidatos
public class ServicoEntrevista {
    private List<Entrevista> entrevistas;
    private int proximoId;

    public ServicoEntrevista() {
        this.entrevistas = new ArrayList<>();
        this.proximoId = 1;
    }

    //cadastro de entrevista
    public boolean cadastrarEntrevista(Candidato candidato, String cargoPretendido,
                                       int urgencia, String data, String horario) {

        if (candidato == null
                || !ValidadorEntrada.textoValido(cargoPretendido)
                || !ValidadorEntrada.urgenciaValida(urgencia)
                || !ValidadorEntrada.dataValida(data)
                || !ValidadorEntrada.horarioValido(horario)) {
            return false;
        }
        Entrevista entrevista = new Entrevista(
                proximoId,
                candidato,
                cargoPretendido,
                urgencia,
                data,
                horario
        );
        entrevistas.add(entrevista);
        proximoId++;
        return true;
    }

    //lista completa
    public List<Entrevista> listarTodos() {
        return entrevistas;
    }

    //lista entrevistas por prioridade
    public List<Entrevista> listarPorPrioridade() {
        List<Entrevista> listaOrdenada = new ArrayList<>(entrevistas);
        Collections.sort(listaOrdenada);
        return listaOrdenada;
    }

    //consulta por ID
    public Entrevista buscarPorId(int id) {
        for (Entrevista entrevista : entrevistas) {
            if (entrevista.getId() == id) {
                return entrevista;
            }
        }

        return null;
    }

    //consulta por nome do candidato
    public List<Entrevista> buscarPorCandidato(String nome) {
        List<Entrevista> encontrados = new ArrayList<>();
        for (Entrevista entrevista : entrevistas) {
            if (entrevista.getCandidato().getNome().toLowerCase().contains(nome.toLowerCase())) {
                encontrados.add(entrevista);
            }
        }
        return encontrados;
    }

    //atualizar entrevista
    public boolean atualizarEntrevista(int id, String cargoPretendido,
                                       int urgencia, String data, String horario) {

        Entrevista entrevista = buscarPorId(id);
        if (entrevista == null) {
            return false;
        }
        if (!ValidadorEntrada.textoValido(cargoPretendido)
                || !ValidadorEntrada.urgenciaValida(urgencia)
                || !ValidadorEntrada.dataValida(data)
                || !ValidadorEntrada.horarioValido(horario)) {
            return false;
        }
        entrevista.setCargoPretendido(cargoPretendido);
        entrevista.setUrgencia(urgencia);
        entrevista.setData(data);
        entrevista.setHorario(horario);
        return true;
    }

    //excluir registro
    public boolean excluirEntrevista(int id) {
        Entrevista entrevista = buscarPorId(id);
        if (entrevista != null) {
            entrevistas.remove(entrevista);
            return true;
        }
        return false;
    }

    //relatorio impresso no console
    public void gerarRelatorioConsole() {
        System.out.println("      RELATÓRIO DE ENTREVISTAS:      ");
        System.out.println("Total de entrevistas cadastradas: " + entrevistas.size());
        for (Entrevista entrevista : listarPorPrioridade()) {
            System.out.println(entrevista.exibirResumo());
        }
    }
}