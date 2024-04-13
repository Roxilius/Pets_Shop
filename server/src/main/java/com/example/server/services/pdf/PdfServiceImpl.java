package com.example.server.services.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.models.Products;
import com.example.server.repositorys.ProductsRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfServiceImpl implements PdfService {
    
    @Autowired
    private ProductsRepository productsRepository;
    @Override
    public byte[] generatePdfFromDatabase() throws IOException, DocumentException {  
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        addTitle(document);
        addTable(document);

        document.close();
        return baos.toByteArray();
    }

    private void addTitle(Document document) throws DocumentException{
        Paragraph title = new Paragraph("Produk Petshop");
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
    }

    private void addTable(Document document) throws DocumentException{
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.addCell("No");
        table.addCell("Nama Barang");
        table.addCell("Kategori");
        table.addCell("Harga");
        table.addCell("Stock");

        List<Products> producList = productsRepository.findAll();

        int count = 1;
        for(Products products : producList){
            table.addCell(String.valueOf(count++));
            table.addCell(products.getName());
            table.addCell(String.valueOf(products.getCategory()));
            table.addCell(String.valueOf(products.getPrice()));
            table.addCell(String.valueOf(products.getStock()));
        }

        document.add(table);
    }

    @SuppressWarnings("unused")
    private void addDataToPdf(Document document, Products data) throws DocumentException{
        Paragraph paragraph =  new Paragraph(data.toString());
        document.add(paragraph);
    }
     
}
