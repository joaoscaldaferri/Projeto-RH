package br.com.rhmanager.service;

import br.com.rhmanager.model.Funcionario;
import br.com.rhmanager.util.ValidadorCPF;
import br.com.rhmanager.util.ValidadorEntrada;

import java.util.ArrayList;
import java.util.List;

//classe que controla as operaçoes relacionadas a funcionários
public class FuncionarioService {
    private List<Funcionario> funcionarios;
    private int proximoId;

    public FuncionarioService() {
        this.funcionarios = new ArrayList<>();
        this.proximoId = 1;
    }

    //cadastro de funcionario
    public boolean cadastrarFuncionario(String nome, String cpf, String email, String telefone,
                                        String cargo, String setor, String turno) {

        if (!ValidadorEntrada.textoValido(nome)
                || !ValidadorCPF.validarCPF(cpf)
                || !ValidadorEntrada.emailValido(email)
                || !ValidadorEntrada.telefoneValido(telefone)
                || !ValidadorEntrada.textoValido(cargo)
                || !ValidadorEntrada.textoValido(setor)
                || !ValidadorEntrada.textoValido(turno)) {
            return false;
        }
        if (ValidadorCPF.cpfDuplicadoFuncionario(cpf, funcionarios)) {
            return false;
        }
        Funcionario funcionario = new Funcionario(
                proximoId,
                nome,
                cpf,
                email,
                telefone,
                cargo,
                setor,
                turno
        );
        funcionarios.add(funcionario);
        proximoId++;
        return true;
    }

    //lista completa
    public List<Funcionario> listarTodos() {
        return funcionarios;
    }

    //consulta por ID
    public Funcionario buscarPorId(int id) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getId() == id) {
                return funcionario;
            }
        }

        return null;
    }

    //consulta por nome
    public List<Funcionario> buscarPorNome(String nome) {
        List<Funcionario> encontrados = new ArrayList<>();

        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getNome().toLowerCase().contains(nome.toLowerCase())) {
                encontrados.add(funcionario);
            }
        }

        return encontrados;
    }

    //atualiza dados
    public boolean atualizarFuncionario(int id, String nome, String email, String telefone,
                                        String cargo, String setor, String turno) {

        Funcionario funcionario = buscarPorId(id);
        if (funcionario == null) {
            return false;
        }
        if (!ValidadorEntrada.textoValido(nome)
                || !ValidadorEntrada.emailValido(email)
                || !ValidadorEntrada.telefoneValido(telefone)
                || !ValidadorEntrada.textoValido(cargo)
                || !ValidadorEntrada.textoValido(setor)
                || !ValidadorEntrada.textoValido(turno)) {
            return false;
        }
        funcionario.setNome(nome);
        funcionario.setEmail(email);
        funcionario.setTelefone(telefone);
        funcionario.setCargo(cargo);
        funcionario.setSetor(setor);
        funcionario.setTurno(turno);
        return true;
    }

    //excluir registro
    public boolean excluirFuncionario(int id) {
        Funcionario funcionario = buscarPorId(id);

        if (funcionario != null) {
            funcionarios.remove(funcionario);
            return true;
        }
        return false;
    }

    //relatorio impresso no console
    public void gerarRelatorioConsole() {
        System.out.println("      RELATÓRIO DE FUNCIONÁRIOS:      ");
        System.out.println("Total de funcionários cadastrados: " + funcionarios.size());

        for (Funcionario funcionario : funcionarios) {
            System.out.println(funcionario.exibirResumo());
        }
    }
}