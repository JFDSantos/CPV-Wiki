package com.example.wikizinha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.wikizinha.model.Adapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class frag_Sua_Conta extends AppCompatActivity {
    //Inicializar Variáveis
    DrawerLayout drawerLayout;
    private TextView nomeUsuario, emailUsuario;
    private Button btn_deslogar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_sua_conta);
        getSupportActionBar().hide();
        IniciarComponentes();

        btn_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); //Deslogar ou sair da sessão
                Intent intent = new Intent(frag_Sua_Conta.this, frag_Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void IniciarComponentes(){
        drawerLayout = findViewById(R.id.drawer_layout);
        nomeUsuario = findViewById(R.id.i_nome_usuario);
        emailUsuario = findViewById(R.id.i_email_usuario);
        btn_deslogar = findViewById(R.id.btn_deslogar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Pega o ID do usuário, conectando-se no db
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot!=null){
                    nomeUsuario.setText(documentSnapshot.getString("nome"));
                    emailUsuario.setText(email);
                }
            }
        });
    }
    public void ClickSeusArtigos(View view){
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