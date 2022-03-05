package com.example.wikizinha;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.snackbar.Snackbar;

public class Categorias extends AppCompatActivity {
    //Iniciar variáveis
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        //Esconder barra feia
        getSupportActionBar().hide();
        //Declarar variáveis
        drawerLayout = findViewById(R.id.drawer_layout);


    }
    public void ClickEmBreve(View view){
        Toast.makeText(this, "Disponível em breve", Toast.LENGTH_SHORT).show();
    }


    public void ClickMenu(View view){
        //Abrir Drawer
        MainActivity.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        //Fechar Drawer
        MainActivity.closeDrawer(drawerLayout);
    }
    public void ClickArtigosRecentes(View view){
        //Redirecionar activity para Artigos Recentes (Home)
        MainActivity.redirectActivity(this,MainActivity.class);
        finish();
    }
    public void ClickCategorias(View view){
        //Recriar activity
        recreate();
    }
    public void ClickMaisVistos(View view){
        //Redirecionar activity para Mais Vistos
        MainActivity.redirectActivity(this,MaisVistos.class);
        finish();
    }
    public void ClickConta(View view){
        //Redirecionar activity para Conta
            MainActivity.redirectActivity(this, frag_Login.class);
            finish();
    }
    public void ClickSobre(View view){
        //Redirecionar activity para Sobre
        MainActivity.redirectActivity(this,Sobre.class);
        finish();
    }
    public void ClickLogout(View view){
        //Fechar app
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Fechar Drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}