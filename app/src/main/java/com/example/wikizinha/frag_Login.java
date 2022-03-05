package com.example.wikizinha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class frag_Login extends AppCompatActivity {
    //Inicializar Variáveis
    TextView txt_cadastro;
    DrawerLayout drawerLayout;
    private EditText txt_email, txt_senha;
    private Button btn_login;
    private ProgressBar progressBar;


    String mensagens = "Por favor, preencha todos os campos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_login);
        getSupportActionBar().hide();
        IniciarComponentes();

        txt_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (frag_Login.this,frag_Cadastro.class);
                startActivity(intent);
            }

        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                String senha = txt_senha.getText().toString();

                if(email.isEmpty()||senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.parseColor("#B72A2A")); // Cor vermelha
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }
                else {
                    AutenticarUsuario(v);
                }
            }
        });

    }

    private void AutenticarUsuario(View v){
        String email = txt_email.getText().toString();
        String senha = txt_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DadosConta();
                        }
                    }, 3000);
                }
                else{
                    String erro;
                    try{
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Ocorreu um erro ao tentar efetuar o login";
                    }

                    Snackbar snackbar = Snackbar.make(v, erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.parseColor("#B72A2A"));
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }
            }
        });
    }
    public void DadosConta(){
        Intent intent = new Intent(frag_Login.this, frag_Sua_Conta.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            DadosConta();
        }
    }

    private void IniciarComponentes(){
        txt_cadastro=(TextView)findViewById(R.id.txt_cadastro);
        drawerLayout = findViewById(R.id.drawer_layout);
        txt_email = findViewById(R.id.txt_email);
        txt_senha = findViewById(R.id.txt_senha);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressbar);
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
        //Redirecionar activity para Categorias
        MainActivity.redirectActivity(this,Categorias.class);
        finish();
    }
    public void ClickMaisVistos(View view){
        //Redirecionar activity para Mais Vistos
        MainActivity.redirectActivity(this,MaisVistos.class);
        finish();
    }
    public void ClickConta(View view){
        //Recriar activity
        recreate();
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