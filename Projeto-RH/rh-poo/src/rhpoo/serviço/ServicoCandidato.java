package rhpoo.serviço;

import rhpoo.modelo.Candidato;
import rhpoo.validaçao.ValidadorCPF;
import rhpoo.validaçao.ValidadorEntrada;

import java.util.ArrayList;
import java.util.List;

//classe que controla as operaçoes relacionadas aos candidatos
public class ServicoCandidato {
    private List<Candidato> candidatos;
    private int proximoId;

    public ServicoCandidato() {
        this.candidatos = new ArrayList<>();
        this.proximoId = 1;
    }

    //cadastro de candidato
    public boolean cadastrarCandidato(String nome, String cpf, String email, String telefone,
                                      String areaAtuacao, int anosExperiencia,
                                      String formacao, String resumoProfissional) {
        if (!ValidadorEntrada.textoValido(nome)
                || !ValidadorCPF.validarCPF(cpf)
                || !ValidadorEntrada.emailValido(email)
                || !ValidadorEntrada.telefoneValido(telefone)
                || !ValidadorEntrada.textoValido(areaAtuacao)
                || !ValidadorEntrada.experienciaValida(anosExperiencia)
                || !ValidadorEntrada.textoValido(formacao)) {
            return false;
        }
        if (ValidadorCPF.cpfDuplicadoCandidato(cpf, candidatos)) {
            return false;
        }
        Candidato candidato = new Candidato(
                proximoId,
                nome,
                cpf,
                email,
                telefone,
                areaAtuacao,
                anosExperiencia,
                formacao,
                resumoProfissional
        );
        candidatos.add(candidato);
        proximoId++;
        return true;
    }

    //lista completa
    public List<Candidato> listarTodos() {
        return candidatos;
    }

    //consulta por ID
    public Candidato buscarPorId(int id) {
        for (Candidato candidato : candidatos) {
            if (candidato.getId() == id) {
                return candidato;
            }
        }
        return null;
    }

    //consulta por nome
    public List<Candidato> buscarPorNome(String nome) {
        List<Candidato> encontrados = new ArrayList<>();

        for (Candidato candidato : candidatos) {
            if (candidato.getNome().toLowerCase().contains(nome.toLowerCase())) {
                encontrados.add(candidato);
            }
        }
        return encontrados;
    }

    //busca por área de atuaçao
    public List<Candidato> buscarPorArea(String area) {
        List<Candidato> encontrados = new ArrayList<>();

        for (Candidato candidato : candidatos) {
            if (candidato.getAreaAtuacao().toLowerCase().contains(area.toLowerCase())) {
                encontrados.add(candidato);
            }
        }
        return encontrados;
    }

    //busca por anos minimos de experiencia
    public List<Candidato> buscarPorExperiencia(int anosMinimos) {
        List<Candidato> encontrados = new ArrayList<>();

        for (Candidato candidato : candidatos) {
            if (candidato.getAnosExperiencia() >= anosMinimos) {
                encontrados.add(candidato);
            }
        }
        return encontrados;
    }

    //atualiza dados
    public boolean atualizarCandidato(int id, String nome, String email, String telefone,
                                      String areaAtuacao, int anosExperiencia,
                                      String formacao, String resumoProfissional) {

        Candidato candidato = buscarPorId(id);
        if (candidato == null) {
            return false;
        }
        if (!ValidadorEntrada.textoValido(nome)
                || !ValidadorEntrada.emailValido(email)
                || !ValidadorEntrada.telefoneValido(telefone)
                || !ValidadorEntrada.textoValido(areaAtuacao)
                || !ValidadorEntrada.experienciaValida(anosExperiencia)
                || !ValidadorEntrada.textoValido(formacao)) {
            return false;
        }
        candidato.setNome(nome);
        candidato.setEmail(email);
        candidato.setTelefone(telefone);
        candidato.setAreaAtuacao(areaAtuacao);
        candidato.setAnosExperiencia(anosExperiencia);
        candidato.setFormacao(formacao);
        candidato.setResumoProfissional(resumoProfissional);
        return true;
    }

    //excluir registro
    public boolean excluirCandidato(int id) {
        Candidato candidato = buscarPorId(id);
        if (candidato != null) {
            candidatos.remove(candidato);
            return true;
        }
        return false;
    }

    //relatorio impresso no console
    public void gerarRelatorioConsole() {
        System.out.println("      RELATÓRIO DE CANDIDATOS:     ");
        System.out.println("Total de candidatos cadastrados: " + candidatos.size());
        for (Candidato candidato : candidatos) {
            System.out.println(candidato.exibirResumo());
        }
    }
}