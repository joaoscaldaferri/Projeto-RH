package rhpoo.serviço;

import rhpoo.modelo.FeriasEscala;
import rhpoo.modelo.Funcionario;
import rhpoo.validaçao.ValidadorEntrada;

import java.util.ArrayList;
import java.util.List;

//classe que controlaa ferias e escalas
public class ServicoFeriasEscala {
    private List<FeriasEscala> feriasEscalas;
    private int proximoId;

    public ServicoFeriasEscala() {
        this.feriasEscalas = new ArrayList<>();
        this.proximoId = 1;
    }

    //cadastro de ferias e escala
    public boolean cadastrarFeriasEscala(Funcionario funcionario, String dataInicioFerias,
                                         String dataFimFerias, String turnoEscala) {

        if (funcionario == null
                || !ValidadorEntrada.dataValida(dataInicioFerias)
                || !ValidadorEntrada.dataValida(dataFimFerias)
                || !ValidadorEntrada.textoValido(turnoEscala)) {
            return false;
        }
        FeriasEscala feriasEscala = new FeriasEscala(
                proximoId,
                funcionario,
                dataInicioFerias,
                dataFimFerias,
                turnoEscala
        );
        feriasEscalas.add(feriasEscala);
        proximoId++;
        return true;
    }

    //lista completa
    public List<FeriasEscala> listarTodos() {
        return feriasEscalas;
    }

    //consulta por ID
    public FeriasEscala buscarPorId(int id) {
        for (FeriasEscala feriasEscala : feriasEscalas) {
            if (feriasEscala.getId() == id) {
                return feriasEscala;
            }
        }
        return null;
    }

    //consulta pelo nome do funcionario
    public List<FeriasEscala> buscarPorFuncionario(String nome) {
        List<FeriasEscala> encontrados = new ArrayList<>();
        for (FeriasEscala feriasEscala : feriasEscalas) {
            if (feriasEscala.getFuncionario().getNome().toLowerCase().contains(nome.toLowerCase())) {
                encontrados.add(feriasEscala);
            }
        }
        return encontrados;
    }

    //atualiza ferias e escala
    public boolean atualizarFeriasEscala(int id, String dataInicioFerias,
                                         String dataFimFerias, String turnoEscala) {

        FeriasEscala feriasEscala = buscarPorId(id);

        if (feriasEscala == null) {
            return false;
        }
        if (!ValidadorEntrada.dataValida(dataInicioFerias)
                || !ValidadorEntrada.dataValida(dataFimFerias)
                || !ValidadorEntrada.textoValido(turnoEscala)) {
            return false;
        }
        feriasEscala.setDataInicioFerias(dataInicioFerias);
        feriasEscala.setDataFimFerias(dataFimFerias);
        feriasEscala.setTurnoEscala(turnoEscala);
        return true;
    }

    //excluir registro
    public boolean excluirFeriasEscala(int id) {
        FeriasEscala feriasEscala = buscarPorId(id);
        if (feriasEscala != null) {
            feriasEscalas.remove(feriasEscala);
            return true;
        }
        return false;
    }

    //relatorio impresso no console
    public void gerarRelatorioConsole() {
        System.out.println("      RELATÓRIO DE FÉRIAS E ESCALAS:      ");
        System.out.println("Total de registros: " + feriasEscalas.size());
        for (FeriasEscala feriasEscala : feriasEscalas) {
            System.out.println(feriasEscala.exibirResumo());
        }
    }
}