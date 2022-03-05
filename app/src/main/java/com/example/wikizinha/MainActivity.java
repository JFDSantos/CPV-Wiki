package com.example.wikizinha;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.wikizinha.model.Adapter;
import com.example.wikizinha.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    //Inicialize as variáveis
    DrawerLayout drawerLayout;
    FloatingActionButton btnAddTexto_AR;
    RecyclerView noteLists;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirestoreRecyclerAdapter<Note, NoteViewHolder> noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Esconder barra feia
        getSupportActionBar().hide();
        //Definir Variável
        IniciarComponentes();
        VerificarLogin();

        Query queryAll = db.collection("notes").orderBy("titulo", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> allNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(queryAll,Note.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {
            @Override
            public void onBindViewHolder(@NonNull NoteViewHolder noteViewholder, int i, @NonNull Note note) {
                noteViewholder.noteTitle.setText(note.getTitulo());
                noteViewholder.noteContent.setText(note.getConteudo());
                String docId = getSnapshots().getSnapshot(i).getId();
                noteViewholder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), content_note_details.class);
                        intent.putExtra("title", note.getTitulo());
                        intent.putExtra("content", note.getConteudo());
                        intent.putExtra("noteId", docId);
                        v.getContext().startActivity(intent);
                    }
                });

                ImageView icMenu = noteViewholder.view.findViewById(R.id.menuIcon);
                icMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();
                        PopupMenu menu = new PopupMenu(v.getContext(), v);
                        menu.setGravity(Gravity.END);
                        menu.getMenu().add("Editar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent intent = new Intent(v.getContext(),frag_EditTexto.class);
                                intent.putExtra("title", note.getTitulo());
                                intent.putExtra("content",note.getConteudo());
                                intent.putExtra("noteId", docId);
                                startActivity(intent);
                                return false;
                            }
                        });

                        menu.getMenu().add("Excluir").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                DocumentReference documentReferenceAll = db.collection("notes").document(docId);
                                documentReferenceAll.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Snackbar snackbar = Snackbar.make(v, "Texto salvo",Snackbar.LENGTH_SHORT);
                                        snackbar.setBackgroundTint(Color.parseColor("#0F491A"));
                                        snackbar.setTextColor(Color.WHITE);
                                        snackbar.show();                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Snackbar snackbar = Snackbar.make(v, "Ocorreu um erro ao exluir o texto",Snackbar.LENGTH_SHORT);
                                        snackbar.setBackgroundTint(Color.parseColor("#B72A2A"));
                                        snackbar.setTextColor(Color.WHITE);
                                        snackbar.show();
                                    }
                                });


                                return false;
                            }
                        });

                        menu.show();
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };

        noteLists.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        noteLists.setAdapter(noteAdapter);
    }

    public void IniciarComponentes(){
        drawerLayout = findViewById(R.id.drawer_layout);
        btnAddTexto_AR = findViewById(R.id.btn_addtexto_AR);
        noteLists = findViewById(R.id.notelists);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }
    private void VerificarLogin(){
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            btnAddTexto_AR.setVisibility(View.VISIBLE);
        }

    }
    public void ClickMenu(View view)
    {
        //Abrir drawer
        openDrawer(drawerLayout);

    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //Abrir drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view)
    {
        //Fechar Drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Fechar drawer layout
        //Verificar condições
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            //Quando aberta
            //Fechar Drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }
    public void ClickArtigosRecentes(View view)
    {
        //Recriar activity;
        recreate();
    }
    public void ClickCategorias(View view){
        //Redireciona activity para Categorias
        redirectActivity(this,Categorias.class);
        finish();
    }
    public void ClickMaisVistos(View view){
        //Redireciona activity para Mais Vistos
        redirectActivity(this,MaisVistos.class);
        finish();
    }
    public void ClickConta(View view){
        //Redireciona activity para Conta
            redirectActivity(this, frag_Login.class);
            finish();
    }
    public void ClickSobre(View view){
        //Redireciona activity para Sobre
        redirectActivity(this,Sobre.class);
        finish();
    }
    public void ClickLogout(View view){
        //Fecha o app
        logout(this);
    }

    public void ClickAddTexto(View view){
        Intent intent = new Intent (MainActivity.this, frag_AddTexto.class);
        startActivity(intent);
    }

    public static void logout(Activity activity) {
        //Inicializa Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Define o título
        builder.setTitle("Logout");
        //Define a mensagem
        builder.setMessage("Deseja encerrar o app?");
        //Button positivo
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Fecha a activity
                activity.finishAffinity();
                //Encerra o app
                System.exit(0);
            }
        });
        //Button negativo
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Fecha a Dialog
                dialog.dismiss();
            }
        });
        //Mostra a Dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity,Class aClass) {
        //Inicia o intent
        Intent intent = new Intent(activity, aClass);
        //"Seta" o flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Inicia a activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Fecha o Drawer
        closeDrawer(drawerLayout);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteContent;
        View view;
        CardView mCardView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            mCardView = itemView.findViewById(R.id.noteCard);
            view = itemView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!= null) {
            noteAdapter.stopListening();
        }

    }
}