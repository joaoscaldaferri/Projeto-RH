package rhpoo;

import rhpoo.modelo.Candidato;
import rhpoo.modelo.Entrevista;
import rhpoo.modelo.Feedback;
import rhpoo.modelo.FeriasEscala;
import rhpoo.modelo.Funcionario;
import rhpoo.serviço.ServicoCandidato;
import rhpoo.serviço.ServicoEntrevista;
import rhpoo.serviço.ServicoExportador;
import rhpoo.serviço.ServicoFeedback;
import rhpoo.serviço.ServicoFeriasEscala;
import rhpoo.serviço.ServicoFuncionario;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    private static ServicoCandidato ServicoCandidato = new ServicoCandidato();
    private static ServicoFuncionario ServicoFuncionario = new ServicoFuncionario();
    private static ServicoFeriasEscala servicoFeriasEscala = new ServicoFeriasEscala();
    private static ServicoEntrevista servicoEntrevista = new ServicoEntrevista();
    private static ServicoFeedback servicoFeedback = new ServicoFeedback();
    private static ServicoExportador servicoExportador = new ServicoExportador();

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n      SISTEMA DE GESTÃO DE RECURSOS HUMANOS      ");
            System.out.println("1 - Candidatos / Currículos");
            System.out.println("2 - Funcionários");
            System.out.println("3 - Férias e Escalas");
            System.out.println("4 - Entrevistas");
            System.out.println("5 - Feedbacks");
            System.out.println("6 - Relatórios no console");
            System.out.println("7 - Exportar dados para TXT");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao =lerInteiro();

            switch (opcao) {
                case 1:
                    menuCandidatos();
                    break;
                case 2:
                    menuFuncionarios();
                    break;
                case 3:
                    menuFeriasEscalas();
                    break;
                case 4:
                    menuEntrevistas();
                    break;
                case 5:
                    menuFeedbacks();
                    break;
                case 6:
                    gerarRelatorios();
                    break;
                case 7:
                    exportarDados();
                    break;
                case 0:
                    System.out.println("Sistema finalizado.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static int lerInteiro() {
        while (!scanner.hasNextInt()) {
            System.out.println("Digite apenas números.");
            scanner.nextLine();
            System.out.print("Tente novamente: ");
        }
        int numero = scanner.nextInt();
        scanner.nextLine();
        return numero;
    }
    //menu de candidatos

    private static void menuCandidatos() {
        int opcao;
        do {
            System.out.println("\n      MÓDULO DE CANDIDATOS / CURRÍCULOS     ");
            System.out.println("1 - Cadastrar candidato");
            System.out.println("2 - Listar candidatos");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Buscar por nome");
            System.out.println("5 - Buscar por área de atuação");
            System.out.println("6 - Buscar por anos de experiência");
            System.out.println("7 - Atualizar candidato");
            System.out.println("8 - Excluir candidato");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    cadastrarCandidato();
                    break;
                case 2:
                    listarCandidatos();
                    break;
                case 3:
                    buscarCandidatoPorId();
                    break;
                case 4:
                    buscarCandidatoPorNome();
                    break;
                case 5:
                    buscarCandidatoPorArea();
                    break;
                case 6:
                    buscarCandidatoPorExperiencia();
                    break;
                case 7:
                    atualizarCandidato();
                    break;
                case 8:
                    excluirCandidato();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção invalida.");
            }
        } while (opcao != 0);
    }
    private static void cadastrarCandidato() {
        System.out.println("\n      CADASTRO DE CANDIDATO      ");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Área de atuação: ");
        String areaAtuacao = scanner.nextLine();

        System.out.print("Anos de experiência: ");
        int anosExperiencia = lerInteiro();

        System.out.print("Formação: ");
        String formacao = scanner.nextLine();

        System.out.print("Resumo profissional: ");
        String resumoProfissional = scanner.nextLine();

        boolean cadastrado = ServicoCandidato.cadastrarCandidato(
                nome, cpf, email, telefone, areaAtuacao, anosExperiencia, formacao, resumoProfissional
        );
        if (cadastrado) {
            System.out.println("Candidato cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar candidato. Verifique CPF, email, telefone ou campos vazios.");
        }
    }
    private static void listarCandidatos() {
        System.out.println("\n     LISTA DE CANDIDATOS      ");

        if (ServicoCandidato.listarTodos().isEmpty()) {
            System.out.println("Nenhum candidato cadastrado.");
            return;
        }
        for (Candidato candidato : ServicoCandidato.listarTodos()) {
            System.out.println(candidato.exibirResumo());
        }
    }
    private static void buscarCandidatoPorId() {
        System.out.print("\nDigite o ID do candidato: ");
        int id = lerInteiro();

        Candidato candidato = ServicoCandidato.buscarPorId(id);

        if (candidato != null) {
            System.out.println(candidato.exibirResumo());
        } else {
            System.out.println("Candidato não encontrado.");
        }
    }
    private static void buscarCandidatoPorNome() {
        System.out.print("\nDigite o nome do candidato: ");
        String nome = scanner.nextLine();

        List<Candidato> candidatos = ServicoCandidato.buscarPorNome(nome);

        if (candidatos.isEmpty()) {
            System.out.println("Nenhum candidato encontrado.");
            return;
        }

        for (Candidato candidato : candidatos) {
            System.out.println(candidato.exibirResumo());
        }
    }
    private static void buscarCandidatoPorArea() {
        System.out.print("\nDigite a área de atuação: ");
        String area = scanner.nextLine();

        List<Candidato> candidatos = ServicoCandidato.buscarPorArea(area);

        if (candidatos.isEmpty()) {
            System.out.println("Nenhum candidato encontrado nessa área.");
            return;
        }

        for (Candidato candidato : candidatos) {
            System.out.println(candidato.exibirResumo());
        }
    }
    private static void buscarCandidatoPorExperiencia() {
        System.out.print("\nDigite os anos mínimos de experiência: ");
        int anos = lerInteiro();

        List<Candidato> candidatos = ServicoCandidato.buscarPorExperiencia(anos);

        if (candidatos.isEmpty()) {
            System.out.println("Nenhum candidato encontrado com essa experiência.");
            return;
        }

        for (Candidato candidato : candidatos) {
            System.out.println(candidato.exibirResumo());
        }
    }
    private static void atualizarCandidato() {
        System.out.print("\nDigite o ID do candidato que deseja atualizar: ");
        int id = lerInteiro();
        Candidato candidato = ServicoCandidato.buscarPorId(id);
        if (candidato == null) {
            System.out.println("Candidato não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();

        System.out.print("Novo email: ");
        String email = scanner.nextLine();

        System.out.print("Novo telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Nova área de atuação: ");
        String areaAtuacao = scanner.nextLine();

        System.out.print("Novos anos de experiência: ");
        int anosExperiencia = lerInteiro();

        System.out.print("Nova formação: ");
        String formacao = scanner.nextLine();

        System.out.print("Novo resumo profissional: ");
        String resumoProfissional = scanner.nextLine();

        boolean atualizado = ServicoCandidato.atualizarCandidato(
                id, nome, email, telefone, areaAtuacao, anosExperiencia, formacao, resumoProfissional
        );
        if (atualizado) {
            System.out.println("Candidato atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar candidato.");
        }
    }
    private static void excluirCandidato() {
        System.out.print("\nDigite o ID do candidato que deseja excluir: ");
        int id = lerInteiro();
        boolean excluido = ServicoCandidato.excluirCandidato(id);
        if (excluido) {
            System.out.println("Candidato excluído com sucesso!");
        } else {
            System.out.println("Candidato não encontrado.");
        }
    }

    //menu funcionarios

    private static void menuFuncionarios() {
        int opcao;
        do {
            System.out.println("\n      MÓDULO DE FUNCIONÁRIOS      ");
            System.out.println("1 - Cadastrar funcionário");
            System.out.println("2 - Listar funcionários");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Buscar por nome");
            System.out.println("5 - Atualizar funcionário");
            System.out.println("6 - Excluir funcionário");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    cadastrarFuncionario();
                    break;
                case 2:
                    listarFuncionarios();
                    break;
                case 3:
                    buscarFuncionarioPorId();
                    break;
                case 4:
                    buscarFuncionarioPorNome();
                    break;
                case 5:
                    atualizarFuncionario();
                    break;
                case 6:
                    excluirFuncionario();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void cadastrarFuncionario() {
        System.out.println("\n      CADASTRO DE FUNCIONÁRIO      ");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Cargo: ");
        String cargo = scanner.nextLine();

        System.out.print("Setor: ");
        String setor = scanner.nextLine();

        System.out.print("Turno: ");
        String turno = scanner.nextLine();

        boolean cadastrado = ServicoFuncionario.cadastrarFuncionario(
                nome, cpf, email, telefone, cargo, setor, turno
        );
        if (cadastrado) {
            System.out.println("Funcionário cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar funcionário. Verifique CPF, email, telefone ou campos vazios.");
        }
    }

    private static void listarFuncionarios() {
        System.out.println("\n      LISTA DE FUNCIONÁRIOS      ");

        if (ServicoFuncionario.listarTodos().isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }
        for (Funcionario funcionario : ServicoFuncionario.listarTodos()) {
            System.out.println(funcionario.exibirResumo());
        }
    }

    private static void buscarFuncionarioPorId() {
        System.out.print("\nDigite o ID do funcionário: ");
        int id = lerInteiro();
        Funcionario funcionario = ServicoFuncionario.buscarPorId(id);
        if (funcionario != null) {
            System.out.println(funcionario.exibirResumo());
        } else {
            System.out.println("Funcionário não encontrado.");
        }
    }

    private static void buscarFuncionarioPorNome() {
        System.out.print("\nDigite o nome do funcionário: ");
        String nome = scanner.nextLine();

        List<Funcionario> funcionarios = ServicoFuncionario.buscarPorNome(nome);

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário encontrado.");
            return;
        }
        for (Funcionario funcionario : funcionarios) {
            System.out.println(funcionario.exibirResumo());
        }
    }

    private static void atualizarFuncionario() {
        System.out.print("\nDigite o ID do funcionário que deseja atualizar: ");
        int id = lerInteiro();

        Funcionario funcionario = ServicoFuncionario.buscarPorId(id);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();

        System.out.print("Novo email: ");
        String email = scanner.nextLine();

        System.out.print("Novo telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Novo cargo: ");
        String cargo = scanner.nextLine();

        System.out.print("Novo setor: ");
        String setor = scanner.nextLine();

        System.out.print("Novo turno: ");
        String turno = scanner.nextLine();

        boolean atualizado = ServicoFuncionario.atualizarFuncionario(
                id, nome, email, telefone, cargo, setor, turno
        );

        if (atualizado) {
            System.out.println("Funcionário atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar funcionário.");
        }
    }

    private static void excluirFuncionario() {
        System.out.print("\nDigite o ID do funcionário que deseja excluir: ");
        int id = lerInteiro();

        boolean excluido = ServicoFuncionario.excluirFuncionario(id);

        if (excluido) {
            System.out.println("Funcionário excluído com sucesso!");
        } else {
            System.out.println("Funcionário não encontrado.");
        }
    }

    //menu de ferias e escala

    private static void menuFeriasEscalas() {
        int opcao;
        do {
            System.out.println("\n      MÓDULO DE FÉRIAS E ESCALAS      ");
            System.out.println("1 - Cadastrar férias e escala");
            System.out.println("2 - Listar férias e escalas");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Buscar por funcionário");
            System.out.println("5 - Atualizar férias e escala");
            System.out.println("6 - Excluir férias e escala");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    cadastrarFeriasEscala();
                    break;
                case 2:
                    listarFeriasEscalas();
                    break;
                case 3:
                    buscarFeriasEscalaPorId();
                    break;
                case 4:
                    buscarFeriasEscalaPorFuncionario();
                    break;
                case 5:
                    atualizarFeriasEscala();
                    break;
                case 6:
                    excluirFeriasEscala();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void cadastrarFeriasEscala() {
        System.out.println("\n      CADASTRO DE FÉRIAS E ESCALA      ");

        listarFuncionarios();

        System.out.print("Digite o ID do funcionário: ");
        int idFuncionario = lerInteiro();

        Funcionario funcionario = ServicoFuncionario.buscarPorId(idFuncionario);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        System.out.print("Data de início das férias (dd/mm/aaaa): ");
        String dataInicio = scanner.nextLine();

        System.out.print("Data de fim das férias (dd/mm/aaaa): ");
        String dataFim = scanner.nextLine();

        System.out.print("Turno da escala: ");
        String turno = scanner.nextLine();

        boolean cadastrado = servicoFeriasEscala.cadastrarFeriasEscala(
                funcionario, dataInicio, dataFim, turno
        );

        if (cadastrado) {
            System.out.println("Férias e escala cadastradas com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar. Verifique as datas e os campos.");
        }
    }

    private static void listarFeriasEscalas() {
        System.out.println("\n      LISTA DE FÉRIAS E ESCALAS      ");

        if (servicoFeriasEscala.listarTodos().isEmpty()) {
            System.out.println("Nenhum registro cadastrado.");
            return;
        }
        for (FeriasEscala feriasEscala : servicoFeriasEscala.listarTodos()) {
            System.out.println(feriasEscala.exibirResumo());
        }
    }

    private static void buscarFeriasEscalaPorId() {
        System.out.print("\nDigite o ID do registro: ");
        int id = lerInteiro();
        FeriasEscala feriasEscala = servicoFeriasEscala.buscarPorId(id);
        if (feriasEscala != null) {
            System.out.println(feriasEscala.exibirResumo());
        } else {
            System.out.println("Registro não encontrado.");
        }
    }

    private static void buscarFeriasEscalaPorFuncionario() {
        System.out.print("\nDigite o nome do funcionário: ");
        String nome = scanner.nextLine();

        List<FeriasEscala> registros = servicoFeriasEscala.buscarPorFuncionario(nome);
        if (registros.isEmpty()) {
            System.out.println("Nenhum registro encontrado.");
            return;
        }
        for (FeriasEscala feriasEscala : registros) {
            System.out.println(feriasEscala.exibirResumo());
        }
    }

    private static void atualizarFeriasEscala() {
        System.out.print("\nDigite o ID do registro que deseja atualizar: ");
        int id = lerInteiro();
        FeriasEscala feriasEscala = servicoFeriasEscala.buscarPorId(id);
        if (feriasEscala == null) {
            System.out.println("Registro não encontrado.");
            return;
        }
        System.out.print("Nova data de início das férias (dd/mm/aaaa): ");
        String dataInicio = scanner.nextLine();

        System.out.print("Nova data de fim das férias (dd/mm/aaaa): ");
        String dataFim = scanner.nextLine();

        System.out.print("Novo turno da escala: ");
        String turno = scanner.nextLine();

        boolean atualizado = servicoFeriasEscala.atualizarFeriasEscala(id, dataInicio, dataFim, turno);
        if (atualizado) {
            System.out.println("Registro atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar registro.");
        }
    }

    private static void excluirFeriasEscala() {
        System.out.print("\nDigite o ID do registro que deseja excluir: ");
        int id = lerInteiro();

        boolean excluido = servicoFeriasEscala.excluirFeriasEscala(id);

        if (excluido) {
            System.out.println("Registro excluído com sucesso!");
        } else {
            System.out.println("Registro não encontrado.");
        }
    }

    //menu entrevista

    private static void menuEntrevistas() {
        int opcao;
        do {
            System.out.println("\n      MÓDULO DE ENTREVISTAS      ");
            System.out.println("1 - Cadastrar entrevista");
            System.out.println("2 - Listar entrevistas");
            System.out.println("3 - Listar entrevistas por prioridade");
            System.out.println("4 - Buscar por ID");
            System.out.println("5 - Buscar por candidato");
            System.out.println("6 - Atualizar entrevista");
            System.out.println("7 - Excluir entrevista");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    cadastrarEntrevista();
                    break;
                case 2:
                    listarEntrevistas();
                    break;
                case 3:
                    listarEntrevistasPorPrioridade();
                    break;
                case 4:
                    buscarEntrevistaPorId();
                    break;
                case 5:
                    buscarEntrevistaPorCandidato();
                    break;
                case 6:
                    atualizarEntrevista();
                    break;
                case 7:
                    excluirEntrevista();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void cadastrarEntrevista() {
        System.out.println("\n      CADASTRO DE ENTREVISTA      ");

        listarCandidatos();

        System.out.print("Digite o ID do candidato: ");
        int idCandidato = lerInteiro();

        Candidato candidato = ServicoCandidato.buscarPorId(idCandidato);
        if (candidato == null) {
            System.out.println("Candidato não encontrado.");
            return;
        }
        System.out.print("Cargo pretendido: ");
        String cargo = scanner.nextLine();

        System.out.print("Urgência da entrevista (1-baixa, 2-média, 3-alta): ");
        int urgencia = lerInteiro();

        System.out.print("Data da entrevista (dd/mm/aaaa): ");
        String data = scanner.nextLine();

        System.out.print("Horário da entrevista (hh:mm): ");
        String horario = scanner.nextLine();

        boolean cadastrado = servicoEntrevista.cadastrarEntrevista(
                candidato, cargo, urgencia, data, horario
        );
        if (cadastrado) {
            System.out.println("Entrevista cadastrada com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar entrevista. Verifique os dados.");
        }
    }

    private static void listarEntrevistas() {
        System.out.println("\n      LISTA DE ENTREVISTAS      ");

        if (servicoEntrevista.listarTodos().isEmpty()) {
            System.out.println("Nenhuma entrevista cadastrada.");
            return;
        }
        for (Entrevista entrevista : servicoEntrevista.listarTodos()) {
            System.out.println(entrevista.exibirResumo());
        }
    }

    private static void listarEntrevistasPorPrioridade() {
        System.out.println("\n      ENTREVISTAS POR PRIORIDADE      ");

        if (servicoEntrevista.listarPorPrioridade().isEmpty()) {
            System.out.println("Nenhuma entrevista cadastrada.");
            return;
        }
        for (Entrevista entrevista : servicoEntrevista.listarPorPrioridade()) {
            System.out.println(entrevista.exibirResumo());
        }
    }

    private static void buscarEntrevistaPorId() {
        System.out.print("\nDigite o ID da entrevista: ");
        int id = lerInteiro();

        Entrevista entrevista = servicoEntrevista.buscarPorId(id);

        if (entrevista != null) {
            System.out.println(entrevista.exibirResumo());
        } else {
            System.out.println("Entrevista não encontrada.");
        }
    }

    private static void buscarEntrevistaPorCandidato() {
        System.out.print("\nDigite o nome do candidato: ");
        String nome = scanner.nextLine();

        List<Entrevista> entrevistas = servicoEntrevista.buscarPorCandidato(nome);

        if (entrevistas.isEmpty()) {
            System.out.println("Nenhuma entrevista encontrada.");
            return;
        }
        for (Entrevista entrevista : entrevistas) {
            System.out.println(entrevista.exibirResumo());
        }
    }

    private static void atualizarEntrevista() {
        System.out.print("\nDigite o ID da entrevista que deseja atualizar: ");
        int id = lerInteiro();

        Entrevista entrevista = servicoEntrevista.buscarPorId(id);

        if (entrevista == null) {
            System.out.println("Entrevista não encontrada.");
            return;
        }

        System.out.print("Novo cargo pretendido: ");
        String cargo = scanner.nextLine();

        System.out.print("Nova urgência (1-baixa, 2-média, 3-alta): ");
        int urgencia = lerInteiro();

        System.out.print("Nova data (dd/mm/aaaa): ");
        String data = scanner.nextLine();

        System.out.print("Novo horário (hh:mm): ");
        String horario = scanner.nextLine();

        boolean atualizado = servicoEntrevista.atualizarEntrevista(id, cargo, urgencia, data, horario);
        if (atualizado) {
            System.out.println("Entrevista atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar entrevista.");
        }
    }

    private static void excluirEntrevista() {
        System.out.print("\nDigite o ID da entrevista que deseja excluir: ");
        int id = lerInteiro();

        boolean excluido = servicoEntrevista.excluirEntrevista(id);

        if (excluido) {
            System.out.println("Entrevista excluída com sucesso!");
        } else {
            System.out.println("Entrevista não encontrada.");
        }
    }

    //menu feedbacks

    private static void menuFeedbacks() {
        int opcao;
        do {
            System.out.println("\n      MÓDULO DE FEEDBACKS      ");
            System.out.println("1 - Cadastrar feedback");
            System.out.println("2 - Listar feedbacks");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Buscar por funcionário");
            System.out.println("5 - Atualizar feedback");
            System.out.println("6 - Excluir feedback");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    cadastrarFeedback();
                    break;
                case 2:
                    listarFeedbacks();
                    break;
                case 3:
                    buscarFeedbackPorId();
                    break;
                case 4:
                    buscarFeedbackPorFuncionario();
                    break;
                case 5:
                    atualizarFeedback();
                    break;
                case 6:
                    excluirFeedback();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void cadastrarFeedback() {
        System.out.println("\n      CADASTRO DE FEEDBACK      ");

        listarFuncionarios();

        System.out.print("Digite o ID do funcionário: ");
        int idFuncionario = lerInteiro();

        Funcionario funcionario = ServicoFuncionario.buscarPorId(idFuncionario);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        System.out.print("Nome do avaliador: ");
        String avaliador = scanner.nextLine();

        System.out.print("Comentário: ");
        String comentario = scanner.nextLine();

        System.out.print("Nota de 0 a 10: ");
        int nota = lerInteiro();

        System.out.print("Data do feedback (dd/mm/aaaa): ");
        String data = scanner.nextLine();

        boolean cadastrado = servicoFeedback.cadastrarFeedback(
                funcionario, avaliador, comentario, nota, data
        );
        if (cadastrado) {
            System.out.println("Feedback cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar feedback. Verifique os dados.");
        }
    }

    private static void listarFeedbacks() {
        System.out.println("\n      LISTA DE FEEDBACKS      ");

        if (servicoFeedback.listarTodos().isEmpty()) {
            System.out.println("Nenhum feedback cadastrado.");
            return;
        }
        for (Feedback feedback : servicoFeedback.listarTodos()) {
            System.out.println(feedback.exibirResumo());
        }
    }

    private static void buscarFeedbackPorId() {
        System.out.print("\nDigite o ID do feedback: ");
        int id = lerInteiro();

        Feedback feedback = servicoFeedback.buscarPorId(id);

        if (feedback != null) {
            System.out.println(feedback.exibirResumo());
        } else {
            System.out.println("Feedback não encontrado.");
        }
    }

    private static void buscarFeedbackPorFuncionario() {
        System.out.print("\nDigite o nome do funcionário: ");
        String nome = scanner.nextLine();

        List<Feedback> feedbacks = servicoFeedback.buscarPorFuncionario(nome);

        if (feedbacks.isEmpty()) {
            System.out.println("Nenhum feedback encontrado.");
            return;
        }
        for (Feedback feedback : feedbacks) {
            System.out.println(feedback.exibirResumo());
        }
    }

    private static void atualizarFeedback() {
        System.out.print("\nDigite o ID do feedback que deseja atualizar: ");
        int id = lerInteiro();

        Feedback feedback = servicoFeedback.buscarPorId(id);

        if (feedback == null) {
            System.out.println("Feedback não encontrado.");
            return;
        }

        System.out.print("Novo avaliador: ");
        String avaliador = scanner.nextLine();

        System.out.print("Novo comentário: ");
        String comentario = scanner.nextLine();

        System.out.print("Nova nota de 0 a 10: ");
        int nota = lerInteiro();

        System.out.print("Nova data (dd/mm/aaaa): ");
        String data = scanner.nextLine();

        boolean atualizado = servicoFeedback.atualizarFeedback(id, avaliador, comentario, nota, data);
        if (atualizado) {
            System.out.println("Feedback atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar feedback.");
        }
    }

    private static void excluirFeedback() {
        System.out.print("\nDigite o ID do feedback que deseja excluir: ");
        int id = lerInteiro();

        boolean excluido = servicoFeedback.excluirFeedback(id);
        if (excluido) {
            System.out.println("Feedback excluído com sucesso!");
        } else {
            System.out.println("Feedback não encontrado.");
        }
    }

    //relatorio e exportaçao

    private static void gerarRelatorios() {
        System.out.println("\n      RELATÓRIOS GERAIS DO SISTEMA      ");

        ServicoCandidato.gerarRelatorioConsole();
        ServicoFuncionario.gerarRelatorioConsole();
        servicoFeriasEscala.gerarRelatorioConsole();
        servicoEntrevista.gerarRelatorioConsole();
        servicoFeedback.gerarRelatorioConsole();
    }

    private static void exportarDados() {
        boolean exportado = servicoExportador.exportarDados(
                "relatorio_rh.txt",
                ServicoCandidato.listarTodos(),
                ServicoFuncionario.listarTodos(),
                servicoFeriasEscala.listarTodos(),
                servicoEntrevista.listarTodos(),
                servicoFeedback.listarTodos()
        );
        if (exportado) {
            System.out.println("Arquivo relatorio_rh.txt exportado com sucesso!");
        } else {
            System.out.println("Erro ao exportar arquivo.");
        }
    }
}
