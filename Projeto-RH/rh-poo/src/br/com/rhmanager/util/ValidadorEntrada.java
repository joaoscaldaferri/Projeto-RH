package br.com.rhmanager.util;

//classe que valida dados digitado pelo usuario
public class ValidadorEntrada {

    public static boolean textoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean emailValido(String email) {
        return email != null
                && email.contains("@")
                && email.contains(".")
                && email.length() >= 6;
    }
    public static boolean telefoneValido(String telefone) {
        if (telefone == null) {
            return false;
        }
        String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
        return telefoneLimpo.length() >= 8 && telefoneLimpo.length() <= 11;
    }
    public static boolean experienciaValida(int anosExperiencia) {
        return anosExperiencia >= 0 && anosExperiencia <= 60;
    }
    public static boolean urgenciaValida(int urgencia) {
        return urgencia >= 1 && urgencia <= 3;
    }
    public static boolean notaValida(int nota) {
        return nota >= 0 && nota <= 10;
    }
    public static boolean idValido(int id) {
        return id > 0;
    }

    public static boolean dataValida(String data) {
        return data != null && data.matches("\\d{2}/\\d{2}/\\d{4}");
    }
    public static boolean horarioValido(String horario) {
        return horario != null && horario.matches("\\d{2}:\\d{2}");
    }
}