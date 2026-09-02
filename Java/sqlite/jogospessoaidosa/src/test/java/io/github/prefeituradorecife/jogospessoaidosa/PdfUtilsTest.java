package io.github.prefeituradorecife.jogospessoaidosa;

import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Telefone;
import io.github.prefeituradorecife.jogospessoaidosa.Utils.PdfUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfUtilsTest {

    @Test
    void deveFormatarSubtituloComRepresentantesSelecionados() {
        Pessoa ana = new Pessoa();
        ana.setNome("Ana");
        Telefone telAna = new Telefone();
        telAna.setDd("11");
        telAna.setNumero("99999-8888");
        ana.setTelefones(List.of(telAna));

        Pessoa bruno = new Pessoa();
        bruno.setNome("Bruno");
        Telefone telBruno = new Telefone();
        telBruno.setDd("21");
        telBruno.setNumero("88888-7777");
        bruno.setTelefones(List.of(telBruno));

        String resultado = PdfUtils.formatarRepresentantesSelecionados(List.of(ana, bruno));

        assertEquals("- Ana - (11) 99999-8888\n- Bruno - (21) 88888-7777", resultado);
    }

    @Test
    void deveGerarPdfAsyncComConteudoValido() throws Exception {
        PdfUtils pdfUtils = new PdfUtils();

        Pessoa ana = new Pessoa();
        ana.setNome("Ana");
        ana.setIdade(68);

        Equipe equipe = new Equipe();
        equipe.setNome("Equipe A");
        equipe.setRpa(RPA.RPA1);

        ByteArrayInputStream pdf = pdfUtils.gerarPdfAsync(List.of(ana), equipe).join();

        assertNotNull(pdf);
        byte[] bytes = pdf.readAllBytes();
        assertTrue(bytes.length > 0);
        assertEquals('%', bytes[0]);
        assertEquals('P', bytes[1]);
        assertEquals('D', bytes[2]);
        assertEquals('F', bytes[3]);
    }
}
