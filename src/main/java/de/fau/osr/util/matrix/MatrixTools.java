package de.fau.osr.util.matrix;

import au.com.bytecode.opencsv.CSVWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import de.fau.osr.bl.RequirementFileImpactValue;
import de.fau.osr.bl.RequirementFilePair;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;

import java.io.*;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * static tools for matrix
 * todo use abstract matrix
 * todo move to tracker?
 * Created by Dmitry Gorelenkov on 20.05.2015.
 */
public class MatrixTools {

    public static enum ExportType {
        PDF,CSV;
    }

    /**
     * saves matrix to {@code path} in csv format
     * @param matrix matrix to save
     * @param path file where matrix will be saved
     */
    public static boolean SaveMatrixToCsv(RequirementsTraceabilityMatrixByImpact matrix, File path) {
        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(path, true), Charset.forName("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try (CSVWriter csvwriter = new CSVWriter(writer, ';')) {
            ArrayList<String> headers = new ArrayList<>();
            headers.add("File name");
            java.util.List<String> reqs = matrix.getRequirements();
            //create headers
            for (String headerName : reqs){
                headers.add("Req-" + headerName);
            }

            csvwriter.writeNext(headers.toArray(new String[headers.size()]));

            //add content row by row
            ArrayList<String> row;
            List<String> files = matrix.getFiles();
            for (String fileName : files) {
                row = new ArrayList<>();
                row.add(fileName);
                //for each req (column)
                for (String req : reqs) {
                    RequirementFileImpactValue value = matrix.getImpactValue(new RequirementFilePair(req, fileName));
                    if (value == null) {
                        value = new RequirementFileImpactValue(0);
                    }
                    row.add(String.valueOf(value.getImpactPercentage()));
                }
                //write line
                csvwriter.writeNext(row.toArray(new String[row.size()]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  true;

    }

    /**
     * /**
     * saves matrix to {@code path} in pdf format
     * @param matrix matrix to save
     * @param path file where matrix will be saved
     */
    public static boolean SaveMatrixToPdf(RequirementsTraceabilityMatrixByImpact matrix, File path,String matrixTitle) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.addTitle(matrixTitle);

            Paragraph headerPar = new Paragraph("Traceability Matrix");
            headerPar.add(new Paragraph(" ")); //new line
            document.add(headerPar);


            java.util.List<String> reqs = matrix.getRequirements();

            //table setup
            PdfPTable table = new PdfPTable(reqs.size()+1); //reqs + "file name" col
            table.setHeaderRows(1);
            table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            table.setWidthPercentage(100);
            table.setSpacingBefore(0);
            table.setSpacingAfter(0);

            //file name col width/req col width 4:1
            float[] widths = new float[reqs.size()+1];
            widths[0] = 4;
            for (int i = 0; i < reqs.size(); i++) {
                widths[i+1] = 1;
            }
            table.setWidths(widths);


            //header row
            //add filename col to header
            addCell(table, "File name", 10, Element.ALIGN_LEFT);
            //add req cols to header
            for (String req : reqs){
                addCell(table, "req-" + req, 8, Element.ALIGN_CENTER);
            }
            //content, row by row:
            for (int i = 0; i < matrix.getFiles().size(); i++) {
                //filename col
                String fileName = matrix.getFiles().get(i);
                addCell(table, fileName, 5, Element.ALIGN_LEFT, 18);


                //impact value cols
                for (String req : reqs) {
                    RequirementFileImpactValue value = matrix.getImpactValue(new RequirementFilePair(req, fileName));
                    if (value == null) {
                        value = new RequirementFileImpactValue(0);
                    }
                    //round
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    String valString = df.format(value.getImpactPercentage());

                    addCell(table, valString, 10, Element.ALIGN_CENTER);
                }
            }

            document.add(table);


        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            document.close();
        }
        return true;

    }

    /**
     * This method returns the file name for the generated traceability matrix.
     * @param Export file type 
     * @param Prefix for the file name 
     * @param name of the file Git repository name + timestamp
     * @param seperator 
     * @return
     */
    public static String generateFileName(ExportType exportType,String prefix,String name,char seperator){
        StringBuffer filename = new StringBuffer();
        filename.append(prefix);
        filename.append(seperator);
        filename.append(name);
        filename.append(seperator);
        filename.append(getCurrentTimestamp());
        if(exportType.equals(ExportType.PDF))
            filename.append(".pdf");
        if(exportType.equals(ExportType.CSV))
            filename.append(".csv");
        return filename.toString();
    }
    
    /**
     * This is a utility method to format the date timestamp.
     * @return
     */
    private static String getCurrentTimestamp(){
        return new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
    }

    /**
     * adds new cell to table
     * @param table table to work with
     * @param text content of cell
     * @param fontSize font fontSize
     * @param alignment enum alignment
     * @param rowFixedHeight cell fixed height
     * @return added cell
     */
    private static PdfPCell addCell(PdfPTable table, String text, int fontSize, int alignment, float rowFixedHeight) {
        Phrase ph = new Phrase(text, getFont(fontSize));
        PdfPCell cell = new PdfPCell(ph);
        cell.setHorizontalAlignment(alignment);
        if (rowFixedHeight > 0) cell.setFixedHeight(rowFixedHeight);
        table.addCell(cell);
        return cell;
    }

    private static PdfPCell addCell(PdfPTable table, String text, int fontSize, int alignment) {
        return addCell(table, text, fontSize, alignment, 0);
    }

    private static com.itextpdf.text.Font getFont(int fontSize) {
        return new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
                fontSize);
    }
}
