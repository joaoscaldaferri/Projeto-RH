package br.com.rhmanager.view;

import br.com.rhmanager.model.*;
import br.com.rhmanager.service.*;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class TelaPrincipal {

    private final CandidatoService candidatoService = new CandidatoService();
    private final FuncionarioService funcionarioService = new FuncionarioService();
    private final FeriasEscalaService feriasEscalaService = new FeriasEscalaService();
    private final EntrevistaService entrevistaService = new EntrevistaService();
    private final FeedbackService feedbackService = new FeedbackService();
    private final ExportadorService exportadorService = new ExportadorService();

    private final TextArea areaResultado = new TextArea();

    public Parent montarTela() {
        BorderPane tela = new BorderPane();
        Label titulo = new Label("Sistema de Gestão de Recursos Humanos");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TabPane abas = new TabPane();
        abas.getTabs().add(criarAba("Candidatos / Currículos", criarAbaCandidatos()));
        abas.getTabs().add(criarAba("Funcionários", criarAbaFuncionarios()));
        abas.getTabs().add(criarAba("Férias e Escalas", criarAbaFeriasEscalas()));
        abas.getTabs().add(criarAba("Entrevistas", criarAbaEntrevistas()));
        abas.getTabs().add(criarAba("Feedback de Funcionários", criarAbaFeedbacks()));
        abas.getTabs().add(criarAba("Relatórios", criarAbaRelatorios()));

        areaResultado.setEditable(false);
        areaResultado.setPrefHeight(200);
        areaResultado.setWrapText(true);

        VBox topo = new VBox(titulo);
        topo.setPadding(new Insets(15));

        tela.setTop(topo);
        tela.setCenter(abas);
        tela.setBottom(areaResultado);
        return tela;
    }

    private Tab criarAba(String titulo, Parent conteudo) {
        Tab aba = new Tab(titulo);
        aba.setContent(conteudo);
        aba.setClosable(false);
        return aba;
    }

    private VBox layoutModulo(String titulo, String descricao) {
        Label labelTitulo = new Label(titulo);
        labelTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label labelDescricao = new Label(descricao);

        VBox layout = new VBox(12, labelTitulo, labelDescricao);
        layout.setPadding(new Insets(15));
        return layout;
    }

    private GridPane gridPadrao() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        return grid;
    }

    private void mostrar(String texto) {
        areaResultado.setText(texto);
    }

    // candidatos

    private VBox criarAbaCandidatos() {
        VBox layout = layoutModulo("Módulo de Banco de Currículos",
                "Cadastro e busca de currículos por área de atuação e anos de experiência.");

        Button cadastrar = new Button("Cadastrar");
        Button listar = new Button("Listar");
        Button buscar = new Button("Buscar");
        Button atualizar = new Button("Atualizar");
        Button excluir = new Button("Excluir");

        cadastrar.setOnAction(e -> cadastrarCandidato());
        listar.setOnAction(e -> listarCandidatos());
        buscar.setOnAction(e -> buscarCandidato());
        atualizar.setOnAction(e -> atualizarCandidato());
        excluir.setOnAction(e -> excluirCandidato());
        layout.getChildren().addAll(cadastrar, listar, buscar, atualizar, excluir);
        return layout;
    }

    private void cadastrarCandidato() {
        Dialog<CandidatoDados> dialog = dialogCandidato("Cadastrar candidato", null);
        Optional<CandidatoDados> resultado = dialog.showAndWait();

        resultado.ifPresent(dados -> {
            try {
                boolean ok = candidatoService.cadastrarCandidato(
                        dados.nome, dados.cpf, dados.email, dados.telefone,
                        dados.area, Integer.parseInt(dados.experiencia),
                        dados.formacao, dados.resumo
                );

                mostrar(ok ? "Candidato cadastrado!" :
                        "Erro ao cadastrar candidato. Verifique CPF, email, telefone e campos obrigatórios!.");
            } catch (NumberFormatException erro) {
                mostrar("Erro: anos de experiência deve ser em número.");
            }
        });
    }

    private void listarCandidatos() {
        List<Candidato> candidatos = candidatoService.listarTodos();

        if (candidatos.isEmpty()) {
            mostrar("Nenhum candidato cadastrado.");
            return;
        }
        StringBuilder texto = new StringBuilder("      CANDIDATOS CADASTRADOS      \n");
        for (Candidato candidato : candidatos) {
            texto.append(candidato.exibirResumo()).append("\n");
        }
        mostrar(texto.toString());
    }

    private void buscarCandidato() {
        ChoiceDialog<String> tipoBusca = new ChoiceDialog<>("ID", "ID", "Nome", "Área", "Experiência mínima");
        tipoBusca.setTitle("Buscar candidato");
        tipoBusca.setHeaderText("Escolha o tipo de busca");

        Optional<String> tipo = tipoBusca.showAndWait();
        if (tipo.isEmpty()) return;
        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Buscar candidato");
        entrada.setHeaderText("Digite o valor da busca:");
        Optional<String> valor = entrada.showAndWait();
        if (valor.isEmpty()) return;
        StringBuilder texto = new StringBuilder();
        try {
            switch (tipo.get()) {
                case "ID":
                    Candidato candidato = candidatoService.buscarPorId(Integer.parseInt(valor.get()));
                    if (candidato != null) texto.append(candidato.exibirResumo());
                    break;

                case "Nome":
                    for (Candidato c : candidatoService.buscarPorNome(valor.get())) {
                        texto.append(c.exibirResumo()).append("\n");
                    }
                    break;

                case "Área":
                    for (Candidato c : candidatoService.buscarPorArea(valor.get())) {
                        texto.append(c.exibirResumo()).append("\n");
                    }
                    break;

                case "Experiência mínima":
                    for (Candidato c : candidatoService.buscarPorExperiencia(Integer.parseInt(valor.get()))) {
                        texto.append(c.exibirResumo()).append("\n");
                    }
                    break;
            }
            mostrar(texto.length() == 0 ? "Nenhum candidato encontrado." : texto.toString());
        } catch (NumberFormatException erro) {
            mostrar("Erro: para ID ou experiência, digite apenas números.");
        }
    }

    private void atualizarCandidato() {
        TextInputDialog entradaId = new TextInputDialog();
        entradaId.setTitle("Atualizar candidato");
        entradaId.setHeaderText("Digite o ID do candidato:");

        Optional<String> idTexto = entradaId.showAndWait();
        if (idTexto.isEmpty()) return;
        try {
            int id = Integer.parseInt(idTexto.get());
            Candidato candidato = candidatoService.buscarPorId(id);

            if (candidato == null) {
                mostrar("Candidato não encontrado.");
                return;
            }
            CandidatoDados dadosAtuais = new CandidatoDados(
                    candidato.getNome(), candidato.getCpf(), candidato.getEmail(), candidato.getTelefone(),
                    candidato.getAreaAtuacao(), String.valueOf(candidato.getAnosExperiencia()),
                    candidato.getFormacao(), candidato.getResumoProfissional()
            );

            Dialog<CandidatoDados> dialog = dialogCandidato("Atualizar candidato", dadosAtuais);
            Optional<CandidatoDados> resultado = dialog.showAndWait();
            resultado.ifPresent(dados -> {
                try {
                    boolean ok = candidatoService.atualizarCandidato(
                            id, dados.nome, dados.email, dados.telefone,
                            dados.area, Integer.parseInt(dados.experiencia),
                            dados.formacao, dados.resumo
                    );
                    mostrar(ok ? "Candidato atualizado com sucesso!" : "Erro ao atualizar candidato.");
                } catch (NumberFormatException erro) {
                    mostrar("Erro: experiência tem que ser número.");
                }
            });
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID tem ser número.");
        }
    }

    private void excluirCandidato() {
        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Excluir candidato");
        entrada.setHeaderText("Digite o ID do candidato:");

        Optional<String> valor = entrada.showAndWait();
        if (valor.isEmpty()) return;
        try {
            boolean ok = candidatoService.excluirCandidato(Integer.parseInt(valor.get()));
            mostrar(ok ? "Candidato excluído com sucesso!" : "Candidato não encontrado.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID tem que ser número.");
        }
    }

    private Dialog<CandidatoDados> dialogCandidato(String titulo, CandidatoDados dados) {
        Dialog<CandidatoDados> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText(titulo);

        ButtonType botaoSalvar = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botaoSalvar, ButtonType.CANCEL);

        TextField nome = new TextField(dados == null ? "" : dados.nome);
        TextField cpf = new TextField(dados == null ? "" : dados.cpf);
        TextField email = new TextField(dados == null ? "" : dados.email);
        TextField telefone = new TextField(dados == null ? "" : dados.telefone);
        TextField area = new TextField(dados == null ? "" : dados.area);
        TextField experiencia = new TextField(dados == null ? "" : dados.experiencia);
        TextField formacao = new TextField(dados == null ? "" : dados.formacao);
        TextField resumo = new TextField(dados == null ? "" : dados.resumo);

        if (dados != null) cpf.setDisable(true);

        GridPane grid = gridPadrao();
        grid.add(new Label("Nome:"), 0, 0); grid.add(nome, 1, 0);
        grid.add(new Label("CPF:"), 0, 1); grid.add(cpf, 1, 1);
        grid.add(new Label("Email:"), 0, 2); grid.add(email, 1, 2);
        grid.add(new Label("Telefone:"), 0, 3); grid.add(telefone, 1, 3);
        grid.add(new Label("Área:"), 0, 4); grid.add(area, 1, 4);
        grid.add(new Label("Experiência:"), 0, 5); grid.add(experiencia, 1, 5);
        grid.add(new Label("Formação:"), 0, 6); grid.add(formacao, 1, 6);
        grid.add(new Label("Resumo:"), 0, 7); grid.add(resumo, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(botao -> {
            if (botao == botaoSalvar) {
                return new CandidatoDados(nome.getText(), cpf.getText(), email.getText(), telefone.getText(),
                        area.getText(), experiencia.getText(), formacao.getText(), resumo.getText());
            }
            return null;
        });
        return dialog;
    }

    //funcionarios

    private VBox criarAbaFuncionarios() {
        VBox layout = layoutModulo("Módulo de Funcionários",
                "Cadastro, consulta, listagem, atualização e exclusão de funcionários.");

        Button cadastrar = new Button("Cadastrar");
        Button listar = new Button("Listar");
        Button buscar = new Button("Buscar");
        Button atualizar = new Button("Atualizar");
        Button excluir = new Button("Excluir");

        cadastrar.setOnAction(e -> cadastrarFuncionario());
        listar.setOnAction(e -> listarFuncionarios());
        buscar.setOnAction(e -> buscarFuncionario());
        atualizar.setOnAction(e -> atualizarFuncionario());
        excluir.setOnAction(e -> excluirFuncionario());

        layout.getChildren().addAll(cadastrar, listar, buscar, atualizar, excluir);
        return layout;
    }

    private void cadastrarFuncionario() {
        Dialog<FuncionarioDados> dialog = dialogFuncionario("Cadastrar funcionário", null);
        Optional<FuncionarioDados> resultado = dialog.showAndWait();

        resultado.ifPresent(dados -> {
            boolean ok = funcionarioService.cadastrarFuncionario(
                    dados.nome, dados.cpf, dados.email, dados.telefone,
                    dados.cargo, dados.setor, dados.turno
            );
            mostrar(ok ? "Funcionário cadastrado com sucesso!" :
                    "Erro ao cadastrar funcionário. Verifique CPF, email, telefone e campos obrigatórios.");
        });
    }

    private void listarFuncionarios() {
        List<Funcionario> funcionarios = funcionarioService.listarTodos();

        if (funcionarios.isEmpty()) {
            mostrar("Nenhum funcionário cadastrado.");
            return;
        }
        StringBuilder texto = new StringBuilder("      FUNCIONÁRIOS CADASTRADOS      \n");
        for (Funcionario funcionario : funcionarios) {
            texto.append(funcionario.exibirResumo()).append("\n");
        }
        mostrar(texto.toString());
    }

    private void buscarFuncionario() {
        ChoiceDialog<String> tipoBusca = new ChoiceDialog<>("ID", "ID", "Nome");
        tipoBusca.setTitle("Buscar funcionário");
        tipoBusca.setHeaderText("Escolha o tipo de busca");

        Optional<String> tipo = tipoBusca.showAndWait();
        if (tipo.isEmpty()) return;

        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Buscar funcionário");
        entrada.setHeaderText("Digite o valor da busca:");

        Optional<String> valor = entrada.showAndWait();
        if (valor.isEmpty()) return;

        StringBuilder texto = new StringBuilder();
        try {
            if (tipo.get().equals("ID")) {
                Funcionario funcionario = funcionarioService.buscarPorId(Integer.parseInt(valor.get()));
                if (funcionario != null) texto.append(funcionario.exibirResumo());
            } else {
                for (Funcionario f : funcionarioService.buscarPorNome(valor.get())) {
                    texto.append(f.exibirResumo()).append("\n");
                }
            }
            mostrar(texto.length() == 0 ? "Nenhum funcionário encontrado." : texto.toString());
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID tem que ser número.");
        }
    }

    private void atualizarFuncionario() {
        TextInputDialog entradaId = new TextInputDialog();
        entradaId.setTitle("Atualizar funcionário");
        entradaId.setHeaderText("Digite o ID do funcionário:");

        Optional<String> idTexto = entradaId.showAndWait();
        if (idTexto.isEmpty()) return;
        try {
            int id = Integer.parseInt(idTexto.get());
            Funcionario funcionario = funcionarioService.buscarPorId(id);
            if (funcionario == null) {
                mostrar("Funcionário não encontrado.");
                return;
            }

            FuncionarioDados dadosAtuais = new FuncionarioDados(
                    funcionario.getNome(), funcionario.getCpf(), funcionario.getEmail(),
                    funcionario.getTelefone(), funcionario.getCargo(), funcionario.getSetor(),
                    funcionario.getTurno()
            );

            Dialog<FuncionarioDados> dialog = dialogFuncionario("Atualizar funcionário", dadosAtuais);
            Optional<FuncionarioDados> resultado = dialog.showAndWait();

            resultado.ifPresent(dados -> {
                boolean ok = funcionarioService.atualizarFuncionario(
                        id, dados.nome, dados.email, dados.telefone,
                        dados.cargo, dados.setor, dados.turno
                );

                mostrar(ok ? "Funcionário atualizado com sucesso!" : "Erro ao atualizar funcionário.");
            });
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID tem que ser número.");
        }
    }

    private void excluirFuncionario() {
        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Excluir funcionário");
        entrada.setHeaderText("Digite o ID do funcionário:");

        Optional<String> valor = entrada.showAndWait();
        if (valor.isEmpty()) return;
        try {
            boolean ok = funcionarioService.excluirFuncionario(Integer.parseInt(valor.get()));
            mostrar(ok ? "Funcionário excluído com sucesso!" : "Funcionário não encontrado.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID deve ser número.");
        }
    }

    private Dialog<FuncionarioDados> dialogFuncionario(String titulo, FuncionarioDados dados) {
        Dialog<FuncionarioDados> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText(titulo);

        ButtonType botaoSalvar = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botaoSalvar, ButtonType.CANCEL);

        TextField nome = new TextField(dados == null ? "" : dados.nome);
        TextField cpf = new TextField(dados == null ? "" : dados.cpf);
        TextField email = new TextField(dados == null ? "" : dados.email);
        TextField telefone = new TextField(dados == null ? "" : dados.telefone);
        TextField cargo = new TextField(dados == null ? "" : dados.cargo);
        TextField setor = new TextField(dados == null ? "" : dados.setor);
        TextField turno = new TextField(dados == null ? "" : dados.turno);

        if (dados != null) cpf.setDisable(true);

        GridPane grid = gridPadrao();
        grid.add(new Label("Nome:"), 0, 0); grid.add(nome, 1, 0);
        grid.add(new Label("CPF:"), 0, 1); grid.add(cpf, 1, 1);
        grid.add(new Label("Email:"), 0, 2); grid.add(email, 1, 2);
        grid.add(new Label("Telefone:"), 0, 3); grid.add(telefone, 1, 3);
        grid.add(new Label("Cargo:"), 0, 4); grid.add(cargo, 1, 4);
        grid.add(new Label("Setor:"), 0, 5); grid.add(setor, 1, 5);
        grid.add(new Label("Turno:"), 0, 6); grid.add(turno, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(botao -> {
            if (botao == botaoSalvar) {
                return new FuncionarioDados(nome.getText(), cpf.getText(), email.getText(), telefone.getText(),
                        cargo.getText(), setor.getText(), turno.getText());
            }
            return null;
        });

        return dialog;
    }

    //ferias e escala

    private VBox criarAbaFeriasEscalas() {
        VBox layout = layoutModulo("Módulo de Férias e Escalas",
                "Lista de funcionários com datas de férias e turnos.");

        Button cadastrar = new Button("Cadastrar");
        Button listar = new Button("Listar");
        Button buscar = new Button("Buscar");
        Button atualizar = new Button("Atualizar");
        Button excluir = new Button("Excluir");

        cadastrar.setOnAction(e -> cadastrarFeriasEscala());
        listar.setOnAction(e -> listarFeriasEscalas());
        buscar.setOnAction(e -> buscarFeriasEscala());
        atualizar.setOnAction(e -> atualizarFeriasEscala());
        excluir.setOnAction(e -> excluirFeriasEscala());

        layout.getChildren().addAll(cadastrar, listar, buscar, atualizar, excluir);
        return layout;
    }

    private void cadastrarFeriasEscala() {
        if (funcionarioService.listarTodos().isEmpty()) {
            mostrar("Cadastre um funcionário antes de cadastrar ferias e escalas.");
            return;
        }

        TextInputDialog idFuncionarioDialog = new TextInputDialog();
        idFuncionarioDialog.setTitle("Cadastrar férias e escala");
        idFuncionarioDialog.setHeaderText("Digite o ID do funcionário:");

        Optional<String> idFuncionarioTexto = idFuncionarioDialog.showAndWait();
        if (idFuncionarioTexto.isEmpty()) return;
        try {
            Funcionario funcionario = funcionarioService.buscarPorId(Integer.parseInt(idFuncionarioTexto.get()));
            if (funcionario == null) {
                mostrar("Funcionário não encontrado.");
                return;
            }

            TextInputDialog inicioDialog = new TextInputDialog();
            inicioDialog.setTitle("Data de início");
            inicioDialog.setHeaderText("Digite a data de início das férias (dd/mm/aaaa):");
            Optional<String> inicio = inicioDialog.showAndWait();
            if (inicio.isEmpty()) return;

            TextInputDialog fimDialog = new TextInputDialog();
            fimDialog.setTitle("Data de fim");
            fimDialog.setHeaderText("Digite a data de fim das férias (dd/mm/aaaa):");
            Optional<String> fim = fimDialog.showAndWait();
            if (fim.isEmpty()) return;

            TextInputDialog turnoDialog = new TextInputDialog();
            turnoDialog.setTitle("Turno");
            turnoDialog.setHeaderText("Digite o turno da escala:");
            Optional<String> turno = turnoDialog.showAndWait();
            if (turno.isEmpty()) return;

            boolean ok = feriasEscalaService.cadastrarFeriasEscala(funcionario, inicio.get(), fim.get(), turno.get());
            mostrar(ok ? "Férias e escala cadastradas com sucesso!" :
                    "Erro ao cadastrar. Use  datas no formato dd/mm/aaaa.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID do funcionário tem que ser número.");
        }
    }

    private void listarFeriasEscalas() {
        List<FeriasEscala> lista = feriasEscalaService.listarTodos();

        if (lista.isEmpty()) {
            mostrar("Nenhum registro de férias e escala cadastrado.");
            return;
        }

        StringBuilder texto = new StringBuilder("      FÉRIAS E ESCALAS      \n");
        for (FeriasEscala f : lista) {
            texto.append(f.exibirResumo()).append("\n");
        }
        mostrar(texto.toString());
    }

    private void buscarFeriasEscala() {
        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Buscar férias e escala");
        entrada.setHeaderText("Digite o nome do funcionário:");

        Optional<String> nome = entrada.showAndWait();
        if (nome.isEmpty()) return;

        List<FeriasEscala> lista = feriasEscalaService.buscarPorFuncionario(nome.get());

        if (lista.isEmpty()) {
            mostrar("Nenhum registro encontrado.");
            return;
        }

        StringBuilder texto = new StringBuilder();
        for (FeriasEscala f : lista) {
            texto.append(f.exibirResumo()).append("\n");
        }
        mostrar(texto.toString());
    }

    private void atualizarFeriasEscala() {
        TextInputDialog entradaId = new TextInputDialog();
        entradaId.setTitle("Atualizar férias e escala");
        entradaId.setHeaderText("Digite o ID do registro:");

        Optional<String> idTexto = entradaId.showAndWait();
        if (idTexto.isEmpty()) return;

        try {
            int id = Integer.parseInt(idTexto.get());

            TextInputDialog inicioDialog = new TextInputDialog();
            inicioDialog.setTitle("Nova data de início");
            inicioDialog.setHeaderText("Digite a nova data de início (dd/mm/aaaa):");
            Optional<String> inicio = inicioDialog.showAndWait();
            if (inicio.isEmpty()) return;

            TextInputDialog fimDialog = new TextInputDialog();
            fimDialog.setTitle("Nova data de fim");
            fimDialog.setHeaderText("Digite a nova data de fim (dd/mm/aaaa):");
            Optional<String> fim = fimDialog.showAndWait();
            if (fim.isEmpty()) return;

            TextInputDialog turnoDialog = new TextInputDialog();
            turnoDialog.setTitle("Novo turno");
            turnoDialog.setHeaderText("Digite o novo turno:");
            Optional<String> turno = turnoDialog.showAndWait();
            if (turno.isEmpty()) return;

            boolean ok = feriasEscalaService.atualizarFeriasEscala(id, inicio.get(), fim.get(), turno.get());
            mostrar(ok ? "Registro atualizado com sucesso!" : "Erro ao atualizar registro.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID tem que ser número.");
        }
    }

    private void excluirFeriasEscala() {
        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Excluir férias e escala");
        entrada.setHeaderText("Digite o ID do registro:");

        Optional<String> valor = entrada.showAndWait();
        if (valor.isEmpty()) return;
        try {
            boolean ok = feriasEscalaService.excluirFeriasEscala(Integer.parseInt(valor.get()));
            mostrar(ok ? "Registro excluído com sucesso!" : "Registro não encontrado.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID tem que ser número.");
        }
    }

    //entrevista

    private VBox criarAbaEntrevistas() {
        VBox layout = layoutModulo("Módulo de Agendamento de Entrevistas",
                "Candidatos são inseridos com prioridade baseada no cargo e na urgência.");

        Button cadastrar = new Button("Cadastrar");
        Button listar = new Button("Listar");
        Button listarPrioridade = new Button("Listar por prioridade");
        Button buscar = new Button("Buscar");
        Button atualizar = new Button("Atualizar");
        Button excluir = new Button("Excluir");

        cadastrar.setOnAction(e -> cadastrarEntrevista());
        listar.setOnAction(e -> listarEntrevistas());
        listarPrioridade.setOnAction(e -> listarEntrevistasPrioridade());
        buscar.setOnAction(e -> buscarEntrevista());
        atualizar.setOnAction(e -> atualizarEntrevista());
        excluir.setOnAction(e -> excluirEntrevista());

        layout.getChildren().addAll(cadastrar, listar, listarPrioridade, buscar, atualizar, excluir);
        return layout;
    }

    private void cadastrarEntrevista() {
        if (candidatoService.listarTodos().isEmpty()) {
            mostrar("Cadastre um candidato antes de agendar entrevistas.");
            return;
        }

        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Cadastrar entrevista");
        idDialog.setHeaderText("Digite o ID do candidato:");
        Optional<String> idCandidato = idDialog.showAndWait();
        if (idCandidato.isEmpty()) return;
        try {
            Candidato candidato = candidatoService.buscarPorId(Integer.parseInt(idCandidato.get()));
            if (candidato == null) {
                mostrar("Candidato não encontrado.");
                return;
            }

            TextInputDialog cargoDialog = new TextInputDialog();
            cargoDialog.setTitle("Cargo pretendido");
            cargoDialog.setHeaderText("Digite o cargo pretendido:");
            Optional<String> cargo = cargoDialog.showAndWait();
            if (cargo.isEmpty()) return;

            TextInputDialog urgenciaDialog = new TextInputDialog();
            urgenciaDialog.setTitle("Urgência");
            urgenciaDialog.setHeaderText("Digite a urgência (1-baixa, 2-média, 3-alta):");
            Optional<String> urgencia = urgenciaDialog.showAndWait();
            if (urgencia.isEmpty()) return;

            TextInputDialog dataDialog = new TextInputDialog();
            dataDialog.setTitle("Data");
            dataDialog.setHeaderText("Digite a data (dd/mm/aaaa):");
            Optional<String> data = dataDialog.showAndWait();
            if (data.isEmpty()) return;

            TextInputDialog horarioDialog = new TextInputDialog();
            horarioDialog.setTitle("Horário");
            horarioDialog.setHeaderText("Digite o horário (hh:mm):");
            Optional<String> horario = horarioDialog.showAndWait();
            if (horario.isEmpty()) return;

            boolean ok = entrevistaService.cadastrarEntrevista(candidato, cargo.get(),
                    Integer.parseInt(urgencia.get()), data.get(), horario.get());

            mostrar(ok ? "Entrevista cadastrada com sucesso!" :
                    "Erro ao cadastrar entrevista. Urgência: 1 a 3. Data: dd/mm/aaaa. Horário: hh:mm.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID e urgência devem ser números.");
        }
    }

    private void listarEntrevistas() {
        List<Entrevista> lista = entrevistaService.listarTodos();

        if (lista.isEmpty()) {
            mostrar("Nenhuma entrevista cadastrada.");
            return;
        }
        StringBuilder texto = new StringBuilder("      ENTREVISTAS      \n");
        for (Entrevista e : lista) {
            texto.append(e.exibirResumo()).append("\n");
        }
        mostrar(texto.toString());
    }

    private void listarEntrevistasPrioridade() {
        List<Entrevista> lista = entrevistaService.listarPorPrioridade();

        if (lista.isEmpty()) {
            mostrar("Nenhuma entrevista cadastrada.");
            return;
        }
        StringBuilder texto = new StringBuilder("      ENTREVISTAS POR PRIORIDADE       \n");
        for (Entrevista e : lista) {
            texto.append(e.exibirResumo()).append("\n");
        }
        mostrar(texto.toString());
    }

    private void buscarEntrevista() {
        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Buscar entrevista");
        entrada.setHeaderText("Digite o nome do candidato:");

        Optional<String> nome = entrada.showAndWait();
        if (nome.isEmpty()) return;

        List<Entrevista> lista = entrevistaService.buscarPorCandidato(nome.get());

        if (lista.isEmpty()) {
            mostrar("Nenhuma entrevista encontrada.");
            return;
        }
        StringBuilder texto = new StringBuilder();
        for (Entrevista e : lista) {
            texto.append(e.exibirResumo()).append("\n");
        }
        mostrar(texto.toString());
    }

    private void atualizarEntrevista() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Atualizar entrevista");
        idDialog.setHeaderText("Digite o ID da entrevista:");
        Optional<String> idTexto = idDialog.showAndWait();
        if (idTexto.isEmpty()) return;
        try {
            int id = Integer.parseInt(idTexto.get());

            TextInputDialog cargoDialog = new TextInputDialog();
            cargoDialog.setTitle("Novo cargo");
            cargoDialog.setHeaderText("Digite o novo cargo pretendido:");
            Optional<String> cargo = cargoDialog.showAndWait();
            if (cargo.isEmpty()) return;

            TextInputDialog urgenciaDialog = new TextInputDialog();
            urgenciaDialog.setTitle("Nova urgência");
            urgenciaDialog.setHeaderText("Digite a nova urgência (1, 2 ou 3):");
            Optional<String> urgencia = urgenciaDialog.showAndWait();
            if (urgencia.isEmpty()) return;

            TextInputDialog dataDialog = new TextInputDialog();
            dataDialog.setTitle("Nova data");
            dataDialog.setHeaderText("Digite a nova data (dd/mm/aaaa):");
            Optional<String> data = dataDialog.showAndWait();
            if (data.isEmpty()) return;

            TextInputDialog horarioDialog = new TextInputDialog();
            horarioDialog.setTitle("Novo horário");
            horarioDialog.setHeaderText("Digite o novo horário (hh:mm):");
            Optional<String> horario = horarioDialog.showAndWait();
            if (horario.isEmpty()) return;

            boolean ok = entrevistaService.atualizarEntrevista(id, cargo.get(),
                    Integer.parseInt(urgencia.get()), data.get(), horario.get());

            mostrar(ok ? "Entrevista atualizada com sucesso!" : "Erro ao atualizar entrevista.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID e urgência tem ser números.");
        }
    }

    private void excluirEntrevista() {
        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Excluir entrevista");
        entrada.setHeaderText("Digite o ID da entrevista:");

        Optional<String> valor = entrada.showAndWait();
        if (valor.isEmpty()) return;
        try {
            boolean ok = entrevistaService.excluirEntrevista(Integer.parseInt(valor.get()));
            mostrar(ok ? "Entrevista excluída com sucesso!" : "Entrevista não encontrada.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID tem que ser número.");
        }
    }

    //feedbacks

    private VBox criarAbaFeedbacks() {
        VBox layout = layoutModulo("Módulo de Feedback de Funcionários",
                "Armazena avaliações internas em ordem cronológica inversa.");

        Button cadastrar = new Button("Cadastrar");
        Button listar = new Button("Listar");
        Button buscar = new Button("Buscar");
        Button atualizar = new Button("Atualizar");
        Button excluir = new Button("Excluir");

        cadastrar.setOnAction(e -> cadastrarFeedback());
        listar.setOnAction(e -> listarFeedbacks());
        buscar.setOnAction(e -> buscarFeedback());
        atualizar.setOnAction(e -> atualizarFeedback());
        excluir.setOnAction(e -> excluirFeedback());

        layout.getChildren().addAll(cadastrar, listar, buscar, atualizar, excluir);
        return layout;
    }

    private void cadastrarFeedback() {
        if (funcionarioService.listarTodos().isEmpty()) {
            mostrar("Cadastre um funcionário antes de registrar feedback.");
            return;
        }

        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Cadastrar feedback");
        idDialog.setHeaderText("Digite o ID do funcionário:");
        Optional<String> idFuncionario = idDialog.showAndWait();
        if (idFuncionario.isEmpty()) return;
        try {
            Funcionario funcionario = funcionarioService.buscarPorId(Integer.parseInt(idFuncionario.get()));
            if (funcionario == null) {
                mostrar("Funcionário não encontrado.");
                return;
            }

            TextInputDialog avaliadorDialog = new TextInputDialog();
            avaliadorDialog.setTitle("Avaliador");
            avaliadorDialog.setHeaderText("Digite o nome do avaliador:");
            Optional<String> avaliador = avaliadorDialog.showAndWait();
            if (avaliador.isEmpty()) return;

            TextInputDialog comentarioDialog = new TextInputDialog();
            comentarioDialog.setTitle("Comentário");
            comentarioDialog.setHeaderText("Digite o comentário:");
            Optional<String> comentario = comentarioDialog.showAndWait();
            if (comentario.isEmpty()) return;

            TextInputDialog notaDialog = new TextInputDialog();
            notaDialog.setTitle("Nota");
            notaDialog.setHeaderText("Digite a nota de 0 a 10:");
            Optional<String> nota = notaDialog.showAndWait();
            if (nota.isEmpty()) return;

            TextInputDialog dataDialog = new TextInputDialog();
            dataDialog.setTitle("Data");
            dataDialog.setHeaderText("Digite a data (dd/mm/aaaa):");
            Optional<String> data = dataDialog.showAndWait();
            if (data.isEmpty()) return;

            boolean ok = feedbackService.cadastrarFeedback(funcionario, avaliador.get(),
                    comentario.get(), Integer.parseInt(nota.get()), data.get());

            mostrar(ok ? "Feedback cadastrado com sucesso!" :
                    "Erro ao cadastrar feedback. Nota: 0 a 10. Data: dd/mm/aaaa.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID do funcionário e nota devem ser números.");
        }
    }

    private void listarFeedbacks() {
        List<Feedback> lista = feedbackService.listarTodos();

        if (lista.isEmpty()) {
            mostrar("Nenhum feedback cadastrado.");
            return;
        }
        StringBuilder texto = new StringBuilder("       FEEDBACKS      \n");
        for (Feedback f : lista) {
            texto.append(f.exibirResumo()).append("\n");
        }
        mostrar(texto.toString());
    }

    private void buscarFeedback() {
        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Buscar feedback");
        entrada.setHeaderText("Digite o nome do funcionário:");

        Optional<String> nome = entrada.showAndWait();
        if (nome.isEmpty()) return;

        List<Feedback> lista = feedbackService.buscarPorFuncionario(nome.get());

        if (lista.isEmpty()) {
            mostrar("Nenhum feedback encontrado.");
            return;
        }
        StringBuilder texto = new StringBuilder();
        for (Feedback f : lista) {
            texto.append(f.exibirResumo()).append("\n");
        }
        mostrar(texto.toString());
    }

    private void atualizarFeedback() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Atualizar feedback");
        idDialog.setHeaderText("Digite o ID do feedback:");
        Optional<String> idTexto = idDialog.showAndWait();
        if (idTexto.isEmpty()) return;
        try {
            int id = Integer.parseInt(idTexto.get());

            TextInputDialog avaliadorDialog = new TextInputDialog();
            avaliadorDialog.setTitle("Novo avaliador");
            avaliadorDialog.setHeaderText("Digite o novo avaliador:");
            Optional<String> avaliador = avaliadorDialog.showAndWait();
            if (avaliador.isEmpty()) return;

            TextInputDialog comentarioDialog = new TextInputDialog();
            comentarioDialog.setTitle("Novo comentário");
            comentarioDialog.setHeaderText("Digite o novo comentário:");
            Optional<String> comentario = comentarioDialog.showAndWait();
            if (comentario.isEmpty()) return;

            TextInputDialog notaDialog = new TextInputDialog();
            notaDialog.setTitle("Nova nota");
            notaDialog.setHeaderText("Digite a nova nota de 0 a 10:");
            Optional<String> nota = notaDialog.showAndWait();
            if (nota.isEmpty()) return;

            TextInputDialog dataDialog = new TextInputDialog();
            dataDialog.setTitle("Nova data");
            dataDialog.setHeaderText("Digite a nova data (dd/mm/aaaa):");
            Optional<String> data = dataDialog.showAndWait();
            if (data.isEmpty()) return;

            boolean ok = feedbackService.atualizarFeedback(id, avaliador.get(),
                    comentario.get(), Integer.parseInt(nota.get()), data.get());

            mostrar(ok ? "Feedback atualizado com sucesso!" : "Erro ao atualizar feedback.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID e nota tem que ser números.");
        }
    }

    private void excluirFeedback() {
        TextInputDialog entrada = new TextInputDialog();
        entrada.setTitle("Excluir feedback");
        entrada.setHeaderText("Digite o ID do feedback:");

        Optional<String> valor = entrada.showAndWait();
        if (valor.isEmpty()) return;
        try {
            boolean ok = feedbackService.excluirFeedback(Integer.parseInt(valor.get()));
            mostrar(ok ? "Feedback excluído com sucesso!" : "Feedback não encontrado.");
        } catch (NumberFormatException erro) {
            mostrar("Erro: ID tem que  ser número.");
        }
    }

    //relatorio

    private VBox criarAbaRelatorios() {
        VBox layout = layoutModulo("Módulo de Relatórios",
                "Relatório na tela e exportação para arquivo texto(.txt).");

        Button relatorioTela = new Button("Gerar relatório na tela");
        Button relatorioConsole = new Button("Gerar relatório no console");
        Button exportar = new Button("Exportar TXT");

        relatorioTela.setOnAction(e -> gerarRelatorioTela());
        relatorioConsole.setOnAction(e -> gerarRelatorioConsole());
        exportar.setOnAction(e -> exportarTxt());

        layout.getChildren().addAll(relatorioTela, relatorioConsole, exportar);
        return layout;
    }

    private void gerarRelatorioTela() {
        StringBuilder texto = new StringBuilder();

        texto.append("      RELATÓRIO GERAL DO SISTEMA DE RH      \n\n");

        texto.append("Total de candidatos: ").append(candidatoService.listarTodos().size()).append("\n");
        texto.append("Total de funcionários: ").append(funcionarioService.listarTodos().size()).append("\n");
        texto.append("Total de férias/escalas: ").append(feriasEscalaService.listarTodos().size()).append("\n");
        texto.append("Total de entrevistas: ").append(entrevistaService.listarTodos().size()).append("\n");
        texto.append("Total de feedbacks: ").append(feedbackService.listarTodos().size()).append("\n\n");

        texto.append("      CANDIDATOS:      \n");
        for (Candidato c : candidatoService.listarTodos()) texto.append(c.exibirResumo()).append("\n");

        texto.append("\n      FUNCIONÁRIOS:      \n");
        for (Funcionario f : funcionarioService.listarTodos()) texto.append(f.exibirResumo()).append("\n");

        texto.append("\n      FÉRIAS E ESCALAS:      \n");
        for (FeriasEscala f : feriasEscalaService.listarTodos()) texto.append(f.exibirResumo()).append("\n");

        texto.append("\n       ENTREVISTAS POR PRIORIDADE:      \n");
        for (Entrevista e : entrevistaService.listarPorPrioridade()) texto.append(e.exibirResumo()).append("\n");

        texto.append("\n      FEEDBACKS DE FUNCIONARIOS:     \n");
        for (Feedback f : feedbackService.listarTodos()) texto.append(f.exibirResumo()).append("\n");
        mostrar(texto.toString());
    }

    private void gerarRelatorioConsole() {
        candidatoService.gerarRelatorioConsole();
        funcionarioService.gerarRelatorioConsole();
        feriasEscalaService.gerarRelatorioConsole();
        entrevistaService.gerarRelatorioConsole();
        feedbackService.gerarRelatorioConsole();

        mostrar("Relatórios impressos no console.");
    }

    private void exportarTxt() {
        boolean ok = exportadorService.exportarDados(
                "relatorio_rh_interface.txt",
                candidatoService.listarTodos(),
                funcionarioService.listarTodos(),
                feriasEscalaService.listarTodos(),
                entrevistaService.listarTodos(),
                feedbackService.listarTodos()
        );

        mostrar(ok ? "Arquivo relatorio_rh_interface.txt exportado com sucesso!" : "Erro ao exportar arquivo.");
    }

    //classes auxiliares internas

    private static class CandidatoDados {
        String nome, cpf, email, telefone, area, experiencia, formacao, resumo;

        CandidatoDados(String nome, String cpf, String email, String telefone, String area,
                       String experiencia, String formacao, String resumo) {
            this.nome = nome;
            this.cpf = cpf;
            this.email = email;
            this.telefone = telefone;
            this.area = area;
            this.experiencia = experiencia;
            this.formacao = formacao;
            this.resumo = resumo;
        }
    }

    private static class FuncionarioDados {
        String nome, cpf, email, telefone, cargo, setor, turno;

        FuncionarioDados(String nome, String cpf, String email, String telefone,
                         String cargo, String setor, String turno) {
            this.nome = nome;
            this.cpf = cpf;
            this.email = email;
            this.telefone = telefone;
            this.cargo = cargo;
            this.setor = setor;
            this.turno = turno;
        }
    }
}
