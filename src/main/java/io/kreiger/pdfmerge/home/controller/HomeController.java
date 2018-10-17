package io.kreiger.pdfmerge.home.controller;

import io.kreiger.pdfmerge.component.Directory;
import io.kreiger.pdfmerge.component.MultipartFileUpload;
import io.kreiger.pdfmerge.component.PdfMerge;
import io.kreiger.pdfmerge.view.PdfMergeView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    private final Logger logger = LogManager.getLogger(getClass());
    private final UUID uuid = UUID.randomUUID();

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.version}")
    private String appVer;

    @Value("${path.temp}")
    private String pathTemp;

    @Autowired
    private PdfMerge pdfMerge;

    @GetMapping("")
    public ModelAndView getMerge(ModelAndView mav) {
        mav.addObject("appName", appName);
        mav.addObject("appVer", appVer);
        mav.setViewName("home");

        return mav;
    }

    @PostMapping("")
    public void postMerge(@RequestParam MultipartFile file, HttpServletResponse response, @Autowired Directory directory, @Autowired MultipartFileUpload mfu) throws Exception {
        boolean uploadSuccess;
        String uploadPath;

        directory.setPath(pathTemp);
        directory.setDirectoryName(this.uuid.toString());

        if(!directory.exists()) {
            directory.createDirectory();
        }

        uploadPath = directory.getFullPath();

        uploadSuccess = mfu.uploadFile(uploadPath, file);

        if(uploadSuccess) {
            response.sendError(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/download")
    public ModelAndView postDownload(ModelAndView mav, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        List<InputStream> fileList;

        mav.setView(new PdfMergeView());

        try {
            logger.info("PDF Merge started for: " + request.getRemoteAddr() + " (" + request.getRemoteHost() + ")" + session.getId());

            pdfMerge.setPath(pathTemp);
            pdfMerge.setDirectory(this.uuid.toString());
            fileList = pdfMerge.sortFiles();
            pdfMerge.fileCleanup();

            mav.addObject("fileList", fileList);
            mav.addObject("Author", "PDF Merge");
            mav.addObject("Title", "Merged PDF");
            mav.addObject("Subject", "Merged with PDF Merge Openshift App");
            mav.addObject("Keywords", "PDF Merge");
            mav.addObject("fileType", "Attachment");
            mav.addObject("fileName", "Merged.pdf");
            mav.addObject("appName", this.appName);
            mav.addObject("AppVer", this.appVer);
        } catch (Exception ex) {
            logger.trace(ex);
        } finally {
            return mav;
        }
    }
}
