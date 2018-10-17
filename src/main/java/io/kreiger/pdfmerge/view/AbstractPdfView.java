package io.kreiger.pdfmerge.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

public abstract class AbstractPdfView extends AbstractView {
    private final Logger logger = LogManager.getLogger(getClass());

    public AbstractPdfView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // IE workaround: write into byte array first.
        ByteArrayOutputStream baos = createTemporaryOutputStream();

        //Set Headers
        setHeaders(model, response);

        // Apply preferences and build metadata.
        Document document = new Document();
        PdfWriter writer = newWriter(document, baos);
        prepareWriter(model, writer, request);
        buildPdfMetadata(model, document, request);

        // Build PDF document.
        document.open();
        buildPdfDocument(model, document, writer, request, response);
        document.close();

        // Flush to HTTP response.
        writeToResponse(response, baos);
    }

    protected Document newDocument() {
        return new Document(PageSize.LETTER);
    }

    protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
        return PdfWriter.getInstance(document, os);
    }

    protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request) {
        writer.setViewerPreferences(getViewerPreferences());
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
    }

    protected int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }

    protected void setHeaders(Map<String, Object> model, HttpServletResponse response) {
        String fileType;
        String mFileType;
        String fileName;
        String mFileName;
        String inline;
        String attachment;

        inline = "inline;filename=";
        attachment = "attachment;filename=";

        mFileType = (String)model.get("fileType");
        mFileName = (String)model.get("fileName");

        //If file type is not set or is set incorrectly
        //default file to be sent as an attachment.
        if(mFileType != null || mFileType.equals("")) {
            if(mFileType.equals("Attachment")) {
                fileType = attachment;
            } else if(mFileType.equals("Inline")) {
                fileType = inline;
            } else {
                fileType = attachment;
            }
        } else {
            fileType = attachment;
        }

        if(mFileName != null || !mFileName.equals("")) {
            fileName = mFileName;
        } else {
            fileName = "NoFileNameSet.pdf";
        }

        response.setHeader("Content-Disposition", fileType.concat('"' + fileName + '"'));
    }

    protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
        String title = (String)model.get("Title");
        String author = (String)model.get("Author");
        String subject = (String)model.get("Subject");
        String keywords = (String)model.get("Keywords");
        String appName = (String)model.get("appName");
        String appVer = (String)model.get("appVer");

        if(title == null || title.equals("")) {
            logger.debug("Title: Not Set");
        } else {
            document.addTitle(title);
        }

        if(author == null || author.equals("")) {
            logger.debug("Author: Not Set");
        } else {
            document.addAuthor(author);
        }

        if(subject == null || subject.equals("")) {
            logger.debug("Subject: Not Set");
        } else {
            document.addSubject(subject);
        }

        if(keywords == null || keywords.equals("")) {
            logger.debug("Keywords: Not Set");
        } else {
            document.addKeywords(keywords);
        }

        document.addCreator(appName + " Ver: " + appVer);
        document.addProducer();
    }

    protected abstract void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
