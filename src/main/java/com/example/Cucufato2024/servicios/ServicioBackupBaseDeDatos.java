package com.example.Cucufato2024.servicios;


import org.springframework.web.multipart.MultipartFile;

public interface ServicioBackupBaseDeDatos {


    public String backup();

    public void importDB(MultipartFile file) throws Exception;

    public void zipBackup();


}
