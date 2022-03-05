package com.example.wikizinha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class content_note_details extends AppCompatActivity {
    Intent data;
    ImageView imvVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_note_details);
        getSupportActionBar().hide();

        data = getIntent();

        TextView content = findViewById(R.id.details_content);
        TextView title = findViewById(R.id.details_title);

        content.setText(data.getStringExtra("content"));
        title.setText(data.getStringExtra("title"));
    }

    public void ClickVoltar(View view){
        Intent intent = new Intent(content_note_details.this, frag_SeusArtigos.class);
        startActivity(intent);
    }

    public void ClickEditar(View view){
        Intent intent = new Intent(view.getContext(),frag_EditTexto.class);
        intent.putExtra("title", data.getStringExtra("title"));
        intent.putExtra("content",data.getStringExtra("content"));
        intent.putExtra("noteId", data.getStringExtra("noteId"));
        startActivity(intent);
    }
}