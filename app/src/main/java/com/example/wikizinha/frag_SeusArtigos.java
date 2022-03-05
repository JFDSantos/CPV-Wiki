package com.example.wikizinha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class frag_SeusArtigos extends AppCompatActivity {
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
        setContentView(R.layout.activity_frag_seus_artigos);
        getSupportActionBar().hide();
        //Definir Variável
        IniciarComponentes();

        Query query = db.collection("notes").document(user.getUid()).collection("mynotes").orderBy("titulo", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> Notes = new FirestoreRecyclerOptions.Builder<Note>().
                setQuery(query, Note.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(Notes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder NoteViewHolder, int position, @NonNull Note note) {
                NoteViewHolder.noteTitle.setText(note.getTitulo());
                NoteViewHolder.noteContent.setText(note.getConteudo());
                String docId = getSnapshots().getSnapshot(position).getId();
                NoteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), content_note_details.class);
                        intent.putExtra("title", note.getTitulo());
                        intent.putExtra("content", note.getConteudo());
                        intent.putExtra("noteId", docId);
                        v.getContext().startActivity(intent);
                    }
                });

                ImageView icMenu = NoteViewHolder.view.findViewById(R.id.menuIcon);
                icMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();
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
                                        DocumentReference documentReference = db.collection("notes").document(user.getUid()).collection("mynotes").document(docId);
                                        Map<String,Object> note = new HashMap<>();
                                        documentReference.update(note);
                                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Snackbar snackbar = Snackbar.make(v, "Ocorreu um erro ao exluir o texto",Snackbar.LENGTH_SHORT);
                                                snackbar.setBackgroundTint(Color.parseColor("#B72A2A"));
                                                snackbar.setTextColor(Color.WHITE);
                                                snackbar.show();
                                            }
                                        });
                                    }
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

    public void ClickAddTexto(View view){
        Intent intent = new Intent (frag_SeusArtigos.this, frag_AddTexto.class);
        startActivity(intent);
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
        if(noteAdapter!= null){
            noteAdapter.stopListening();
        }

    }
}