package com.example.andre.runnerartist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.andre.runnerartist.model.Drawing;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends GenericActivity {
    final Context ctx = this;
    ListView lstDrawings;
    Button btnListProfile;
    FloatingActionButton fabBeginDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstDrawings = (ListView) findViewById(R.id.lstDrawings);
        btnListProfile = (Button) findViewById(R.id.btnListProfile);
        fabBeginDraw = (FloatingActionButton) findViewById(R.id.fabBeginDraw);

        setLstDrawings(lstDrawings);

        btnListProfile.setOnClickListener(v -> {
            PopupMenu pp = new PopupMenu(ctx, v);
            String[] popupItems = getResources().getStringArray(R.array.profiles);
            for (String popupItem : popupItems) {
                pp.getMenu().add(popupItem);
            }
            pp.show();
        });
        fabBeginDraw.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, MapsActivity.class);
            intent.putExtra("new_drawing", true);
            startActivity(intent);
        });
    }

    private void setLstDrawings(ListView lstView) {
        List<Drawing> drawings = db().getDrawings();
        List<Long> creationTime = new ArrayList<>(drawings.size());
        for (Drawing drawing : drawings) {
            creationTime.add(drawing.getFinishCreationTime());
        }
        ListAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                creationTime);
        lstView.setAdapter(adapter);
        lstView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ctx, MapsActivity.class);
            intent.putExtra("drawing_id", id);
            startActivity(intent);
        });
    }
}
