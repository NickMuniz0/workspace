package io.github.prefeituradorecife.jogospessoaidosa.Enum;

public enum RPA {
    RPA1("RPA1"),
    RPA2("RPA2"),
    RPA3("RPA3"),
    RPA4("RPA4"),
    RPA5("RPA5"),
    RPA6("RPA6");
    private final String label;

    RPA(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
