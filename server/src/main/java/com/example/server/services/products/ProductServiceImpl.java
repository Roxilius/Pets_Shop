package com.example.server.services.products;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_access_object.ProductDao;
import com.example.server.data_transfer_object.PageResponse;
import com.example.server.data_transfer_object.products.ProductRequest;
import com.example.server.data_transfer_object.products.ProductResponse;
import com.example.server.models.Category;
import com.example.server.models.Products;
import com.example.server.repositorys.CategoryRepository;
import com.example.server.repositorys.ProductsRepository;
import com.example.server.services.image.ImageService;

import jakarta.transaction.Transactional;


@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductDao productDao;
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageService imageService;
    @Override
    public PageResponse<ProductResponse> getAllProducts(String name, String category, int page, int size, String sortBy, String sortOrder) {
        Category categoryName = categoryRepository.findCategoryByName(category);
        PageResponse<Products> productPage = productDao.getAll(name, categoryName, page, size, sortBy, sortOrder);
        List<ProductResponse> productResponse = productPage.getItems().stream()
        .map(this::toProduct)
        .collect(Collectors.toList());
        return PageResponse.success(productResponse, productPage.getPage(), productPage.getSize(), productPage.getTotalItem());
    }

    public ProductResponse toProduct(Products product) {
        try {
            return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .stock(product.getStock())
            .image(imageService.convertImage(product.getImage()))
            .category(product.getCategory().getName())
            .description(product.getDescription())
            .build();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProductResponse getProduct(String id) {
        Products products = productsRepository.findProductsById(id);
        return toProduct(products);
    }

    @Override
    @Transactional
    public void add(ProductRequest request, MultipartFile productImage) throws IOException, SQLException{
        if (!productImage.getContentType().startsWith("image")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported File Type");
        }
        Products product = productsRepository.findByName(request.getName()).orElse(null);
        if (product == null) {
            Products newProduct = new Products();
            newProduct.setName(request.getName());
            Category category = categoryRepository.findCategoryByName(request.getCategory());
            if (category != null) {
                newProduct.setCategory(category);
            } else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Tidak Ada");
            }
            newProduct.setDescription(request.getCategory());
            newProduct.setPrice(request.getPrice());
            newProduct.setStock(request.getStock());
            newProduct.setImage(new SerialBlob(productImage.getBytes()));
            productsRepository.save(newProduct);
        } else{
            product.setDescription(request.getCategory());
            product.setCategory(categoryRepository.findCategoryByName(request.getCategory()));
            product.setStock(product.getStock() + request.getStock());
            productsRepository.save(product);
        }
    }
    @Override
    @Transactional
    public void edit(ProductRequest request, MultipartFile productImage, String id) throws IOException, SQLException{
        if (!productImage.getContentType().startsWith("image")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported File Type");
        }
        Products product = productsRepository.findById(id).orElse(null);
        product.setName(request.getName());
        Category category = categoryRepository.findCategoryByName(request.getCategory());
        if (category != null) {
            product.setCategory(category);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Tidak Ada");
        }
        product.setDescription(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImage(new SerialBlob(productImage.getBytes()));
        productsRepository.saveAndFlush(product);
    }

    @Override
    public void delete(String id) {
        productsRepository.deleteById(id);
    }

    @Override
    public byte[] generateReport() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet();

        CellStyle headerCellStyle = createCellStyle(workbook, true, false, false);
        CellStyle titleCellStyle = createCellStyle(workbook, false, true, false);
        CellStyle tableCellStyle = createCellStyle(workbook, false, false, true);

        generateTitle(sheet, titleCellStyle);
        generateHeader(sheet, headerCellStyle);

        List<Products> products = productsRepository.findAll();
        if (products.isEmpty()) {
            generateEmptyData(sheet, tableCellStyle);
        } else {
            int currentRowIndex = 3;
            int rowNum = 1;
            for (Products product : products) {
                Row row = sheet.createRow(currentRowIndex);
                generateTabelData(row, product, tableCellStyle, rowNum);
                currentRowIndex++;
                rowNum++;
            }
        }

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(byteArrayOutputStream);
        workbook.close();

        return byteArrayOutputStream.toByteArray();
    }

    private void generateTitle(Sheet sheet, CellStyle cellStyle) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Laporan Data Products");
        titleCell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
    }

    private void generateHeader(Sheet sheet, CellStyle cellStyle) {
        Row headerRow = sheet.createRow(2);
        int currentCellIndex = 0;
        headerRow.createCell(currentCellIndex).setCellValue("No");
        headerRow.createCell(++currentCellIndex).setCellValue("Nama Produt");
        headerRow.createCell(++currentCellIndex).setCellValue("Kategori");
        headerRow.createCell(++currentCellIndex).setCellValue("Harga");
        headerRow.createCell(++currentCellIndex).setCellValue("Stock");
        headerRow.createCell(++currentCellIndex).setCellValue("");

        for (int i = 0; i < currentCellIndex; i++) {
            headerRow.getCell(i).setCellStyle(cellStyle);
        }
    }

    private CellStyle createCellStyle(Workbook workbook, boolean isHeader, boolean isTitle, boolean isTable) {
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        if (isHeader) {
            font.setFontHeightInPoints((short) 15);
            font.setBold(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        if (isTitle) {
            font.setFontHeightInPoints((short) 10);
            font.setBold(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        if (isTable) {
            font.setFontHeightInPoints((short) 10);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderTop(BorderStyle.DASHED);
            cellStyle.setBorderBottom(BorderStyle.DASHED);
            cellStyle.setBorderLeft(BorderStyle.DASHED);
            cellStyle.setBorderRight(BorderStyle.DASHED);
        }
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void generateEmptyData(Sheet sheet, CellStyle cellStyle) {
        Row emptyRow = sheet.createRow(3);
        emptyRow.createCell(0).setCellValue("No Data");
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));
        emptyRow.getCell(0).setCellStyle(cellStyle);
    }

    private void generateTabelData(Row row, Products product, CellStyle cellStyle,
        int rowNum) {
        int currentCellIndex = 0;

        Cell numberRow = row.createCell(currentCellIndex);
        numberRow.setCellValue(rowNum);
        numberRow.setCellStyle(cellStyle);

        Cell namaRow = row.createCell(++currentCellIndex);
        namaRow.setCellValue(product.getName());
        namaRow.setCellStyle(cellStyle);

        Cell kategoriRow = row.createCell(++currentCellIndex);
        kategoriRow.setCellValue(product.getCategory().getName());
        kategoriRow.setCellStyle(cellStyle);

        Cell hargaRow = row.createCell(++currentCellIndex);
        hargaRow.setCellValue(product.getPrice());
        hargaRow.setCellStyle(cellStyle);

        Cell stockRow = row.createCell(++currentCellIndex);
        stockRow.setCellValue(product.getStock());
        stockRow.setCellStyle(cellStyle);
    }
}