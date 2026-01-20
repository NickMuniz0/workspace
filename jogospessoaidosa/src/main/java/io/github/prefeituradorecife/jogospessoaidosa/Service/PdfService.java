package io.github.prefeituradorecife.jogospessoaidosa.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfService {

    public ByteArrayInputStream gerarPdf(List<Pessoa> pessoa, Equipe equipe) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            // Título centralizado
            String titleString = equipe.getNome() + " - " + equipe.getRpa().getLabel();
            Paragraph title = new Paragraph(titleString, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Espaço

            // Criar tabela com 3 colunas
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Cabeçalhos
            Stream.of("Nome", "CPF", "Data de Nascimento").forEach(header -> {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new BaseColor(230, 230, 250)); // Lavanda
                table.addCell(cell);
            });

            // Dados dos representantes
            for (Pessoa rep : pessoa) {
                table.addCell(new PdfPCell(new Phrase(rep.getNome(), cellFont)));
                table.addCell(new PdfPCell(new Phrase(rep.getCpf(), cellFont)));
                table.addCell(new PdfPCell(new Phrase(rep.getDataNascimento().toString(), cellFont)));
            }

            document.add(table);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}