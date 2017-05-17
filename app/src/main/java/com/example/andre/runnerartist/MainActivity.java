package com.example.andre.runnerartist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import com.example.andre.runnerartist.model.Drawing;
import com.example.andre.runnerartist.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends GenericActivity {
    private final Context ctx = this;
    private ListView lstDrawings;
    private Switch swtContinuo;
    private Button btnListProfile;
    private FloatingActionButton fabBeginDraw;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstDrawings = (ListView) findViewById(R.id.lstDrawings);
        btnListProfile = (Button) findViewById(R.id.btnListProfile);
        swtContinuo = (Switch) findViewById(R.id.swtContinuo);
        fabBeginDraw = (FloatingActionButton) findViewById(R.id.fabBeginDraw);

        setLstDrawings(lstDrawings);

        btnListProfile.setOnClickListener(v -> {
            List<Profile> profiles = db().getProfiles();
            PopupMenu pp = new PopupMenu(ctx, v);
            for (Profile p : profiles) {
                pp.getMenu().add(Menu.NONE, p.getId().intValue(), Menu.NONE, p.getName());
            }
            pp.getMenu().add(Menu.NONE, -1, Menu.NONE, "Adicionar");
            pp.setOnMenuItemClickListener(item -> {
                Boolean found = false;
                for (Profile p : profiles) {
                    if (p.getId() == item.getItemId()) {
                        updateProfile(p);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Intent intent = new Intent(ctx, InsertProfileActivity.class);
                    startActivityForResult(intent, 1001);
                }
                return true;
            });
            pp.show();
        });
        fabBeginDraw.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, MapsActivity.class);
            intent.putExtra("autosave", swtContinuo.isChecked());
            intent.putExtra("profileId", profile.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Profile newProfile = new Profile()
                    .withName(data.getStringExtra("profileName"));
            updateProfile(db().insertProfile(newProfile));

        }
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
            intent.putExtra("drawingId", id);
            startActivity(intent);
        });
    }

    private void updateProfile(Profile p) {
        profile = p;
        btnListProfile.setText(p.getName());
    }
}
