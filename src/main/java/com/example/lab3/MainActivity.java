package com.example.lab3;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnOpenFileClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // разрешение не предоставлено
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else {
            // разрешение предоставлено
            OpenFileDialog();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // если пользователь закрыл запрос на разрешение, не дав ответа, массив grantResults будет пустым
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // разрешение было предоставлено
                    // выполните здесь необходимые операции для включения функциональности приложения, связанной с запрашиваемым разрешением
                    OpenFileDialog();
                } else {
                    // разрешение не было предоставлено
                    // выполните здесь необходимые операции для выключения функциональности приложения, связанной с запрашиваемым разрешением
                }

                return;
            }
            case 2:
            {
                SaveFile();

                return;
            }



        }
    }











    public void OnSaveFileClick(View view)
    {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        else {

            SaveFile();
        }
    }




    public void OpenFileDialog()
    {
        OpenFileDialog fileDialog = new OpenFileDialog(this) //Создаем новый класс OpenFileDialog
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() { //Устанавливаем новый обработчик выбора файла
                    @Override
                    public void OnSelectedFile(String selectedFile) {

                        File file = new File(selectedFile); //Создаем экземпляр класса File, используя в качестве аргумента конструктора путь к выбранному файлу
                        fileName = selectedFile; // задаем значение выбранного файла для последующего сохранения
                        StringBuilder text = new StringBuilder();

                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file)); //создаем буффер для чтения файла
                            String line;

                            while ((line = br.readLine()) != null) { //читаем построчно до каонца файла
                                text.append(line);
                                text.append('\n');
                            }
                            br.close(); //закрываем файл
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        TextView tv = findViewById(R.id.textView); //получаем наш textView
                        tv.setText(text.toString()); //устанавливаем полученных из файла текст в textView

                    }
                });

        fileDialog.show(); // показываем диалог выбора файла
    }

    public void SaveFile()
    {
        if (fileName != "" && fileName != null) { //если мы открыли файл
            try {
                FileOutputStream fos = new FileOutputStream(fileName); // открываем поток для чтения
                TextView tv = findViewById(R.id.textView); //выбираем наш textView
                fos.write(tv.getText().toString().getBytes()); //пишем содержимое textView в выбранный файл
                fos.close(); //закрываем поток
                Toast.makeText(this, "Файл сохранен!",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}