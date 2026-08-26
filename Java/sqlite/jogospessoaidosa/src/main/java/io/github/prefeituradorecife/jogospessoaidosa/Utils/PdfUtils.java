package io.github.prefeituradorecife.jogospessoaidosa.Utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Representante;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PdfUtils {

    public static String formatarRepresentantesSelecionados(List<Pessoa> representantes) {
        if (representantes == null || representantes.isEmpty()) {
            return "";
        }

        return representantes.stream()
                .filter(Objects::nonNull)
                .map(rep -> {
                    String nome = rep.getNome() != null && !rep.getNome().isBlank() ? rep.getNome() : "Sem nome";
                    String telefone = "Sem telefone";

                    if (rep.getTelefones() != null && !rep.getTelefones().isEmpty()) {
                        telefone = rep.getTelefones().stream()
                                .filter(Objects::nonNull)
                                .map(t -> {
                                    String ddd = t.getDd() != null && !t.getDd().isBlank() ? t.getDd() : "";
                                    String numero = t.getNumero() != null ? t.getNumero() : "";
                                    return (ddd.isBlank() ? "" : "(" + ddd + ") ") + numero;
                                })
                                .filter(s -> !s.isBlank())
                                .findFirst()
                                .orElse("Sem telefone");
                    }

                    return "- " + nome + " - " + telefone;
                })
                .collect(Collectors.joining("\n"));
    }

    public ByteArrayInputStream gerarPdf(List<Pessoa> pessoa, Equipe equipe) {
        return gerarPdf(pessoa, equipe, (String) null);
    }

    public ByteArrayInputStream gerarPdf(List<Pessoa> pessoa, Equipe equipe, String subtitulo) {
        return gerarPdf(pessoa, equipe, subtitulo, true);
    }

    /**
     * Gera PDF com opção de ordenação por nome.
     * @param pessoa lista de pessoas
     * @param equipe equipe
     * @param ascending true = ordem ascendente, false = descendente
     */
    public ByteArrayInputStream gerarPdf(List<Pessoa> pessoa, Equipe equipe, boolean ascending) {
        return gerarPdf(pessoa, equipe, null, ascending);
    }

    private ByteArrayInputStream gerarPdf(List<Pessoa> pessoa, Equipe equipe, String subtitulo, boolean ascending) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            // Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            int quantidadeNomes = pessoa != null ? pessoa.size() : 0;
            String titleString = equipe.getNome() + " - " + equipe.getRpa().getLabel() + " (" + quantidadeNomes + ")";
            Paragraph title = new Paragraph(titleString, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // String textoSubtitulo = "";
            // if (subtitulo != null && !subtitulo.isBlank()) {
            //     String trimmed = subtitulo.trim();
            //     // Se o subtítulo for uma lista de representantes (começa com "- " ou contém quebras de linha), não exibe
            //     if (!trimmed.startsWith("- ") && !trimmed.contains("\n")) {
            //         textoSubtitulo = subtitulo;
            //     }
            // } else {
            //     textoSubtitulo = String.valueOf(quantidadeNomes);
            // }

            // if (textoSubtitulo != null && !textoSubtitulo.isBlank()) {
            //     Paragraph subtitle = new Paragraph(textoSubtitulo, subtitleFont);
            //     subtitle.setAlignment(Element.ALIGN_CENTER);
            //     subtitle.setSpacingAfter(8f);
            //     document.add(subtitle);
            // }

            Paragraph separator = new Paragraph(" ");
            separator.setSpacingBefore(4f);
            separator.setSpacingAfter(4f);
            document.add(separator);
            document.add(new Paragraph(" ")); // Espaço

            // Tabela de representantes (nome + telefone)
            if (equipe != null && equipe.getRepresentantes() != null && !equipe.getRepresentantes().isEmpty()) {
                PdfPTable repTable = new PdfPTable(2);
                repTable.setWidthPercentage(100);
                repTable.setSpacingBefore(6f);
                repTable.setSpacingAfter(6f);

                PdfPCell h1 = new PdfPCell(new Phrase("Representante", headerFont));
                h1.setHorizontalAlignment(Element.ALIGN_CENTER);
                h1.setBackgroundColor(new BaseColor(230, 230, 250));
                repTable.addCell(h1);

                PdfPCell h2 = new PdfPCell(new Phrase("Telefone", headerFont));
                h2.setHorizontalAlignment(Element.ALIGN_CENTER);
                h2.setBackgroundColor(new BaseColor(230, 230, 250));
                repTable.addCell(h2);

                for (Representante rep : equipe.getRepresentantes()) {
                    String nomeRep = rep.getNome() != null && !rep.getNome().isBlank()
                            ? rep.getNome()
                            : (rep.getPessoa() != null && rep.getPessoa().getNome() != null ? rep.getPessoa().getNome() : "Sem nome");

                    String telefone = "Sem telefone";
                    // Primeiro tenta os telefones associados ao Representante
                    if (rep.getTelefones() != null && !rep.getTelefones().isEmpty()) {
                        telefone = rep.getTelefones().stream()
                                .filter(Objects::nonNull)
                                .map(t -> {
                                    String ddd = t.getDd() != null && !t.getDd().isBlank() ? t.getDd() : "";
                                    String numero = t.getNumero() != null ? t.getNumero() : "";
                                    return (ddd.isBlank() ? "" : "(" + ddd + ") ") + numero;
                                })
                                .filter(s -> !s.isBlank())
                                .findFirst()
                                .orElse("Sem telefone");
                    }
                    // Se não encontrou, tenta os telefones vinculados à Pessoa associada
                    if ((telefone == null || telefone.isBlank() || "Sem telefone".equals(telefone))
                            && rep.getPessoa() != null
                            && rep.getPessoa().getTelefones() != null
                            && !rep.getPessoa().getTelefones().isEmpty()) {
                        telefone = rep.getPessoa().getTelefones().stream()
                                .filter(Objects::nonNull)
                                .map(t -> {
                                    String ddd = t.getDd() != null && !t.getDd().isBlank() ? t.getDd() : "";
                                    String numero = t.getNumero() != null ? t.getNumero() : "";
                                    return (ddd.isBlank() ? "" : "(" + ddd + ") ") + numero;
                                })
                                .filter(s -> !s.isBlank())
                                .findFirst()
                                .orElse("Sem telefone");
                    }

                    PdfPCell nameCell = new PdfPCell(new Phrase(nomeRep, cellFont));
                    nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    nameCell.setPaddingLeft(4f);
                    repTable.addCell(nameCell);

                    PdfPCell phoneCell = new PdfPCell(new Phrase(telefone, cellFont));
                    phoneCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    phoneCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    phoneCell.setPaddingLeft(4f);
                    repTable.addCell(phoneCell);
                }

                document.add(repTable);
            }

            // Criar tabela com 3 colunas (Nome, Idade, Doenças)
            PdfPTable table = new PdfPTable(3);                                                     // 3 colunas: Nome, Idade, Doenças
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Cabeçalhos
            Stream.of("Nome", "Idade", "Doenças" ).forEach(header -> {                              // 3 cabeçalhos
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new BaseColor(230, 230, 250)); // Lavanda
                table.addCell(cell);
            });

            // Dados dos representantes (ordenados por nome)
            Comparator<Pessoa> cmp = Comparator.comparing(Pessoa::getNome, String.CASE_INSENSITIVE_ORDER);
            if (!ascending) cmp = cmp.reversed();

            List<Pessoa> sortedPessoas = pessoa.stream()
                    .sorted(cmp)
                    .collect(Collectors.toList());

            for (Pessoa rep : sortedPessoas) {
                table.addCell(new PdfPCell(new Phrase(rep.getNome(), cellFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(rep.getIdade()), cellFont)));           // Se quiser incluir a idade, descomente esta linha

                // String idosoStr = rep.getIdoso() != null && rep.getIdoso() ? "Sim" : "Não";
                // table.addCell(new PdfPCell(new Phrase(idosoStr, cellFont)));
                // Concatenar nomes das doenças (se houver)
                String doencasStr = "-";
                if (rep.getDoencas() != null && !rep.getDoencas().isEmpty()) {
                    doencasStr = rep.getDoencas().stream()
                            .map(Doenca::getNome)
                            .filter(n -> n != null && !n.isBlank())
                            .collect(Collectors.joining(", "));
                }
                table.addCell(new PdfPCell(new Phrase(doencasStr, cellFont)));

              
            }

            document.add(table);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}