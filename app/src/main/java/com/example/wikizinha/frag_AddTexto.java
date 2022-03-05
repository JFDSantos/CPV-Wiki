package com.example.wikizinha;

import androidx.annotation.NonNull;
import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class frag_AddTexto extends AppCompatActivity {
ImageView imvVoltar, imvAlinharEsqueda, imvCentralizar,imvAlinharDireita, imvNegrito, imvItalico, imvSublinhado, imvLimparFormato;
EditText txtTitulo, txtConteudo;
FloatingActionButton btnSalvarTexto;
FirebaseFirestore db = FirebaseFirestore.getInstance();
FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_add_texto);
        getSupportActionBar().hide();
        IniciarComponentes();
        TituloNegrito();
    }

    public void ImvVoltar(View view){
        Voltar();
    }

    public void ImvAlinharEsqueda(View view){
        txtConteudo.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        Spannable spannableString = new SpannableStringBuilder(txtConteudo.getText());
        txtConteudo.setText(spannableString);
    }

    public void ImvCentralizar(View view){
        txtConteudo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Spannable spannableString = new SpannableStringBuilder(txtConteudo.getText());
        txtConteudo.setText(spannableString);
    }

    public void ImvAlinharDireita(View view){
        txtConteudo.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        Spannable spannableString = new SpannableStringBuilder(txtConteudo.getText());
        txtConteudo.setText(spannableString);
    }

    public void ImvNegrito(View view){
        Spannable spannableString = new SpannableStringBuilder(txtConteudo.getText());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                txtConteudo.getSelectionStart(),
                txtConteudo.getSelectionEnd(),0);
        txtConteudo.setText(spannableString);
    }

    public void imvItalico(View view){
        Spannable spannableString = new SpannableStringBuilder(txtConteudo.getText());
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC),
                txtConteudo.getSelectionStart(),
                txtConteudo.getSelectionEnd(),0);
        txtConteudo.setText(spannableString);
    }

    public void imvSublinhado(View view){
        Spannable spannableString = new SpannableStringBuilder(txtConteudo.getText());
        spannableString.setSpan(new UnderlineSpan(),
                txtConteudo.getSelectionStart(),
                txtConteudo.getSelectionEnd(),0);
        txtConteudo.setText(spannableString);
    }

    public void setImvLimparFormato(View view){
        String limparFormato = txtConteudo.getText().toString();
        txtConteudo.setText(limparFormato);
    }

    public void BtnSalvarTexto(View view){
       String nTitulo = txtTitulo.getText().toString();
       String nConteudo = txtConteudo.getText().toString();

       if (nTitulo.isEmpty() || nConteudo.isEmpty()){
           Snackbar snackbar = Snackbar.make(view, "Por favor, preencha todos os campos",Snackbar.LENGTH_SHORT);
           snackbar.setBackgroundTint(Color.parseColor("#B72A2A"));
           snackbar.setTextColor(Color.WHITE);
           snackbar.show();
       }
       //salvar texto
        else{
            //Add em artigos recentes
           DocumentReference documentReferenceAll = db.collection("notes").document();
           Map<String,Object> note = new HashMap<>();
           note.put("titulo",nTitulo);
           note.put("conteudo",nConteudo);

           //Add em artigos recentes
           documentReferenceAll.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void unused) {
                   Snackbar snackbar = Snackbar.make(view, "Texto salvo",Snackbar.LENGTH_SHORT);
                   snackbar.setBackgroundTint(Color.parseColor("#0F491A"));
                   snackbar.setTextColor(Color.WHITE);
                   snackbar.show();
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Voltar();}
                   },1500);

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Snackbar snackbar = Snackbar.make(view, "Ocorreu um erro ao salvar o texto",Snackbar.LENGTH_SHORT);
                   snackbar.setBackgroundTint(Color.parseColor("#B72A2A"));
                   snackbar.setTextColor(Color.WHITE);
                   snackbar.show();
               }
           });
        }


    }

    public void Voltar() {
        Intent intent = new Intent (frag_AddTexto.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void IniciarComponentes()
    {
        imvVoltar = findViewById(R.id.ic_voltar);
        imvAlinharEsqueda = findViewById(R.id.ic_alinhado_esquerda);
        imvAlinharDireita = findViewById(R.id.ic_alinhado_direita);
        imvCentralizar = findViewById(R.id.ic_centralizado);
        imvNegrito = findViewById(R.id.ic_negrito);
        imvItalico = findViewById(R.id.ic_italico);
        imvSublinhado = findViewById(R.id.ic_sublinhado);
        imvLimparFormato = findViewById(R.id.ic_limparformato);
        txtTitulo = findViewById(R.id.txt_inserirtitulo);
        txtConteudo = findViewById(R.id.txt_inserirtexto);
        btnSalvarTexto = findViewById(R.id.btn_salvar_texto);
    }

    public void TituloNegrito(){
        Spannable spannableString = new SpannableStringBuilder(txtTitulo.getText());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                txtTitulo.getSelectionStart(),
                txtTitulo.getSelectionEnd(),0);
                txtTitulo.setText(spannableString);
    }

}