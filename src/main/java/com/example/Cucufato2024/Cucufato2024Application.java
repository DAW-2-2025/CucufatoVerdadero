package com.example.Cucufato2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Cucufato2024Application {

	//E:\Archivos de Programa\XAMPP\mysql\bin
	//"C:\\xampp\\mysql\\bin\\mysqld.exe"
	public static void main(String[] args) {
		try{
			Process process = Runtime.getRuntime().exec("C:\\XAMPP\\mysql\\bin\\mysqld.exe");
		}catch (IOException e){
			e.printStackTrace();
		}
		SpringApplication.run(Cucufato2024Application.class, args);
	}

}
