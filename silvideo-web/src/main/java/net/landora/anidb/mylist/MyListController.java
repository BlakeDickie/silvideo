/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.anidb.mylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author bdickie
 */
@Controller
public class MyListController {

    @Autowired
    private ListReaderManager manager;

    public static final String MODEL_ERROR = "errorMessage";
    public static final String MODEL_INFO = "infoMessage";

    @GetMapping( "/mylist/import" )
    public String mylistFileSelect( Model model ) {
        ListImportState state = manager.getState();
        switch ( state ) {
            case Completed:
                model.addAttribute( MODEL_INFO, "Import Completed" );
                break;
            case Error:
                model.addAttribute( MODEL_ERROR, "Import Errored" );
                break;
            case Running:
                return "redirect:/mylist/import-status";
        }

        return "mylist_file_select";
    }

    @PostMapping( "/mylist/import" )
    public String mylistFileImport( @RequestParam( "file" ) MultipartFile file ) {

        return "mylist_file_select";
    }

    @GetMapping( "/mylist/import-status" )
    public String mylistStatus() {
        return "mylist_file_select";
    }

}
