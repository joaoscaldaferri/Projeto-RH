package rhpoo.validaçao;

import rhpoo.modelo.Candidato;
import rhpoo.modelo.Funcionario;

import java.util.List;

//classe que valida o CPF e verifica a se nao e repitido
public class ValidadorCPF {

    //remove pontos, traços e espaços do CPF
    public static String limparCPF(String cpf) {
        if (cpf == null) {
            return "";
        }
        return cpf.replaceAll("[^0-9]", "");
    }

    //valida se o CPF é real usando o cálculo oficial dos dígitos
    public static boolean validarCPF(String cpf) {
        cpf = limparCPF(cpf);
        if (cpf.length() != 11) {
            return false;
        }

        //nao deixa o CPF com todos os números iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        int soma = 0;
        int peso = 10;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * peso;
            peso--;
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }
        if (primeiroDigito != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }
        soma = 0;
        peso = 11;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * peso;
            peso--;
        }
        int segundoDigito = 11 - (soma % 11);

        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }
        return segundoDigito == Character.getNumericValue(cpf.charAt(10));
    }

    //valida se o CPF já tem entre candidatos
    public static boolean cpfDuplicadoCandidato(String cpf, List<Candidato> candidatos) {
        String cpfLimpo = limparCPF(cpf);

        for (Candidato candidato : candidatos) {
            if (limparCPF(candidato.getCpf()).equals(cpfLimpo)) {
                return true;
            }
        }
        return false;
    }

    //valida se o CPF já tem entre funcionarios
    public static boolean cpfDuplicadoFuncionario(String cpf, List<Funcionario> funcionarios) {
        String cpfLimpo = limparCPF(cpf);

        for (Funcionario funcionario : funcionarios) {
            if (limparCPF(funcionario.getCpf()).equals(cpfLimpo)) {
                return true;
            }
        }
        return false;
    }
}
