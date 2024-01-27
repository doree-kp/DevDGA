package doree.devg.extra;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.Document;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RelevePDFGenerator {
    public static byte[] genererRelevePDF(String contenuReleve){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(outputStream);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);

            document.add(new Paragraph(contenuReleve));

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
