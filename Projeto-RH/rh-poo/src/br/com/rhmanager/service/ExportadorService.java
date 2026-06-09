package br.com.rhmanager.service;

import br.com.rhmanager.model.Candidato;
import br.com.rhmanager.model.Entrevista;
import br.com.rhmanager.model.Feedback;
import br.com.rhmanager.model.FeriasEscala;
import br.com.rhmanager.model.Funcionario;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//classe que exporta dados para arquivo de texto(.txt)
public class ExportadorService {

    public boolean exportarDados(String nomeArquivo,
                                 List<Candidato> candidatos,
                                 List<Funcionario> funcionarios,
                                 List<FeriasEscala> feriasEscalas,
                                 List<Entrevista> entrevistas,
                                 List<Feedback> feedbacks) {

        try {
            FileWriter arquivo = new FileWriter(nomeArquivo);
            PrintWriter escritor = new PrintWriter(arquivo);

            escritor.println(" RELATÓRIO GERAL DO SISTEMA DE RH");
            escritor.println("\n");
            escritor.println("      CANDIDATOS:      ");
            if (candidatos.isEmpty()) {
                escritor.println("Nenhum candidato cadastrado.");
            } else {
                for (Candidato candidato : candidatos) {
                    escritor.println(candidato.exibirResumo());
                }
            }
            escritor.println();
            escritor.println("      FUNCIONÁRIOS:      ");
            if (funcionarios.isEmpty()) {
                escritor.println("Nenhum funcionário cadastrado.");
            } else {
                for (Funcionario funcionario : funcionarios) {
                    escritor.println(funcionario.exibirResumo());
                }
            }
            escritor.println();
            escritor.println("      FÉRIAS E ESCALAS:      ");
            if (feriasEscalas.isEmpty()) {
                escritor.println("Nenhum registro de férias ou escala cadastrado.");
            } else {
                for (FeriasEscala feriasEscala : feriasEscalas) {
                    escritor.println(feriasEscala.exibirResumo());
                }
            }
            escritor.println();
            escritor.println("      ENTREVISTAS:      ");
            if (entrevistas.isEmpty()) {
                escritor.println("Nenhuma entrevista cadastrada.");
            } else {
                for (Entrevista entrevista : entrevistas) {
                    escritor.println(entrevista.exibirResumo());
                }
            }
            escritor.println();
            escritor.println("      FEEDBACKS:      ");
            if (feedbacks.isEmpty()) {
                escritor.println("Nenhum feedback cadastrado.");
            } else {
                for (Feedback feedback : feedbacks) {
                    escritor.println(feedback.exibirResumo());
                }
            }
            escritor.println();
            escritor.println(" FIM DO RELATÓRIO");
            escritor.close();
            arquivo.close();
            return true;
        } catch (IOException erro) {
            System.out.println("Erro ao exportar arquivo: " + erro.getMessage());
            return false;
        }
    }
}