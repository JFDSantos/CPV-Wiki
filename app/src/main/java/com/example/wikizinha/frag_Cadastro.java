package com.example.wikizinha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class frag_Cadastro extends AppCompatActivity {
    //Inicializar Variaveis
    DrawerLayout drawerLayout;
    private EditText txt_nome, txt_email_cadastro, txt_senha_cadrasto;
    private Button btn_cadastro;

    String[] mensagens = {"Por favor, preencha todos os campos","Cadastro realizado com sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_cadastro);
        getSupportActionBar().hide();
        IniciarComponentes();

        btn_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = txt_nome.getText().toString();
                String email_cadastro = txt_email_cadastro.getText().toString();
                String senha_cadastro = txt_senha_cadrasto.getText().toString();

                if(nome.isEmpty()||email_cadastro.isEmpty()||senha_cadastro.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.parseColor("#B72A2A"));
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }
                else{
                    CadastrarUsuario(v);
                }
            }
        });
    }

    private void CadastrarUsuario(View v){
        String email_cadastro = txt_email_cadastro.getText().toString();
        String senha_cadastro = txt_senha_cadrasto.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_cadastro, senha_cadastro).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){

                  SalvarDadosUsuario();

                  Snackbar snackbar = Snackbar.make(v, mensagens[1],Snackbar.LENGTH_SHORT);
                  snackbar.setBackgroundTint(Color.parseColor("#0F491A"));
                  snackbar.setTextColor(Color.WHITE);
                  snackbar.show();
                  FirebaseAuth.getInstance().signOut();
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          Intent intent = new Intent(frag_Cadastro.this, frag_Login.class);
                          startActivity(intent);}
                  },1500);

              }
              else
              {
                  String erro;
                  try{
                      throw task.getException();
                  }catch (FirebaseAuthWeakPasswordException e){
                      erro = "A senha deve ter no mínimo 6 caracteres";

                  }catch (FirebaseAuthUserCollisionException e){
                      erro = "O e-mail digitado já está em uso";

                  }catch(FirebaseAuthInvalidCredentialsException e){
                      erro = "Por favor, digite um e-mail válido";

                  } catch (Exception e){
                    erro = "Erro a cadastrar o usuário";
                  }

                  Snackbar snackbar = Snackbar.make(v, erro,Snackbar.LENGTH_SHORT);
                  snackbar.setBackgroundTint(Color.parseColor("#B72A2A"));
                  snackbar.setTextColor(Color.WHITE);
                  snackbar.show();
              }
            }
        });
    }

    private void SalvarDadosUsuario(){
        String nome = txt_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Dados salvos com sucesso");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Ocorreu um erro ao salvar os dados"+ e.toString());
            }
        });
    }

    private void IniciarComponentes()
    {
        drawerLayout = findViewById(R.id.drawer_layout);
        txt_nome = findViewById(R.id.txt_nome);
        txt_email_cadastro = findViewById(R.id.txt_email_cadastro);
        txt_senha_cadrasto = findViewById(R.id.txt_senha_cadastro);
        btn_cadastro = findViewById(R.id.btn_cadastro);
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
        MainActivity.redirectActivity(this,frag_Login.class);
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