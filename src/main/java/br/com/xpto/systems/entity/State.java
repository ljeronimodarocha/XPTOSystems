package br.com.xpto.systems.entity;


public enum State {
    AM("Amazonas", "AM"),
    AL("Alagoas", "AL"),
    AC("Acre", "AC"),
    AP("Amapá", "AP"),
    BA("Bahia", "BA"),
    PA("Pará", "PA"),
    MT("Mato Grosso", "MT"),
    MG("Minas Gerais", "MG"),
    MS("Mato Grosso do Sul", "MS"),
    GO("Goiás", "GO"),
    MA("Maranhão", "MA"),
    RS("Rio Grande do Sul", "RS"),
    TO("Tocantins", "TO"),
    PI("Piauí", "PI"),
    SP("São Paulo", "SP"),
    RO("Rondônia", "RO"),
    RR("Roraima", "RR"),
    PR("Paraná", "PR"),
    CE("Ceará", "CE"),
    PE("Pernambuco", "PE"),
    SC("Santa Catarina", "SC"),
    PB("Paraíba", "PB"),
    RN("Rio Grande do Norte", "RN"),
    ES("Espírito Santo", "ES"),
    RJ("Rio de Janeiro", "RJ"),
    SE("Sergipe", "SE"),
    DF("Distrito Federal", "DF");

    private final String nome;
    private final String sigla;

    State(final String nome, final String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

}
