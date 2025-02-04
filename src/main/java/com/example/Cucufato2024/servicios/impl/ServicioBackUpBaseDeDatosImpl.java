package com.example.Cucufato2024.servicios.impl;

import com.example.Cucufato2024.servicios.ServicioBackupBaseDeDatos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Configuration
@EnableScheduling
@Service
public class ServicioBackUpBaseDeDatosImpl implements ServicioBackupBaseDeDatos {

    @Value("${db.host}")
    private String host;
    @Value("${db.port}")
    private String port;
    @Value("${db.name}")
    private String database;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${backupPath}")
    private String exportPath;
    @Value("${filePath}")
    private String importPath;
    @Value("${mysqldump.path}")
    private String mysqldumpPath;
    @Value("${mysql.path}")
    private String mysqlPath;

    @Override
    public String backup(){
        String fechaActual = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
        String rutaExportCompleta = exportPath + fechaActual + ".sql";

        File directorioBackup = new File(exportPath);
        if (!directorioBackup.exists()) {
            directorioBackup.mkdirs();
        }

        int processComplete =0;
        String passParam = password.isEmpty() ? "" : password;
        try {
            String command = String.format("%s -u%s --password=%s %s --databases -r %s",
                    mysqldumpPath, user, passParam, database, rutaExportCompleta);
            System.out.println(command);
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            processComplete = process.waitFor();
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.printf("Copia hecha");

        return rutaExportCompleta;
    }

    @Override
    public void importDB(MultipartFile file) throws Exception {
        String rutaFichero = importPath + file.getOriginalFilename();
        File dest = new File(rutaFichero);
        file.transferTo(dest);

        int processComplete = 0;
        String passParam = password.isEmpty() ? "" : "--password=" + password;
        try {

            String command = String.format("%s -u%s %s %s < %s",
                    exportPath, user, passParam, database, rutaFichero);

            System.out.println(command);

            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            processComplete = process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (processComplete == 0) {
            System.out.println("Importación hecha con éxito");
        } else {
            System.err.println("Hubo un problema durante la importación.");
        }
    }

    @Scheduled(cron = "0 0 0 1-7 * MON")
    public void scheduledBackup() {
        try {
            zipBackup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zipBackup(){
        String fechaActual = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());

        //La carpeta imágenes habrá que cambiarla según donde vayan a estar todas las imágenes del proyecto. Quizá sea buena idea meterla en el application properties.
        String carpetaImagenes = "E:\\CESUR\\Asignaturas\\Segundo\\Despliegue de aplicaciones Web\\Cucufato\\src\\main\\resources\\png";
        String arhivoSql = backup();
        //Al igual que habrá que decidir donde se guarda el zip. Por ahora en uploads
        String archivoZip = importPath + "backup_" + fechaActual + ".zip";

        try {
            createZip(carpetaImagenes, arhivoSql, archivoZip);
            System.out.println("¡Archivo ZIP creado exitosamente!");
        } catch (IOException e) {
            System.err.println("Error al crear el ZIP" + e.getMessage());
        }
    }

    public void createZip(String carpetaImagenes, String archivoSql, String archivoZip) throws IOException{
        try (FileOutputStream fos = new FileOutputStream(archivoZip);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            Path carpetaPath = Path.of(carpetaImagenes);
            Files.walk(carpetaPath).filter(path -> !Files.isDirectory(path)).forEach(file -> {
                String zipEntryName = "imagenes/" + carpetaPath.relativize(file).toString();
                agregarArchivoAlZip(zos, file, zipEntryName);
            });

            Path sqlPath = Paths.get(archivoSql);

            agregarArchivoAlZip(zos, sqlPath, sqlPath.getFileName().toString());
        }
    }

    private static void agregarArchivoAlZip(ZipOutputStream zos, Path file, String zipEntryName) {
        try {
            ZipEntry zipEntry = new ZipEntry(zipEntryName.replace("\\", "/"));
            zos.putNextEntry(zipEntry);
            Files.copy(file, zos);
            zos.closeEntry();
        } catch (IOException e) {
            System.err.println("Error al agregar archivo al ZIP: " + file + " - " + e.getMessage());
        }
    }

}
