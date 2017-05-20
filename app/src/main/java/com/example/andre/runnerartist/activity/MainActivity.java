package com.example.andre.runnerartist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import com.example.andre.runnerartist.R;
import com.example.andre.runnerartist.misc.ConfigConstant;
import com.example.andre.runnerartist.misc.DrawingItemAdapter;
import com.example.andre.runnerartist.model.Profile;

import java.util.List;
import java.util.Objects;

public class MainActivity extends GenericActivity {
    private final Context ctx = this;
    private ListView lstDrawings;
    private Switch swtContinuo;
    private Button btnListProfile;
    private FloatingActionButton fabBeginDraw;

    private List<Profile> profiles;
    private Profile selectedProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstDrawings = (ListView) findViewById(R.id.lstDrawings);
        btnListProfile = (Button) findViewById(R.id.btnListProfile);
        swtContinuo = (Switch) findViewById(R.id.swtContinuo);
        fabBeginDraw = (FloatingActionButton) findViewById(R.id.fabBeginDraw);

        db().getUserConfig(ConfigConstant.DEFAULT_CONTINUOUS, value -> {
            swtContinuo.setChecked(Boolean.valueOf(value));
            swtContinuo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                db().setUserConfig(ConfigConstant.DEFAULT_CONTINUOUS, Boolean.toString(isChecked));
            });
            return null;
        });

        fabBeginDraw.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, MapsActivity.class);
            intent.putExtra("autosave", swtContinuo.isChecked());
            intent.putExtra("profileId", selectedProfile.getId());
            startActivity(intent);
        });

        db().getProfiles(result -> {
            profiles = result;

            lstDrawings.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(ctx, MapsActivity.class);
                intent.putExtra("drawingId", id);
                startActivity(intent);
            });

            btnListProfile.setOnClickListener(v -> {
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

            // setup default profile
            db().getUserConfig(ConfigConstant.DEFAULT_PROFILE_ID, profileId -> {
                Profile defProf = null;
                if (profileId != null) {
                    Long defProfId = Long.parseLong(profileId);
                    for (Profile p : profiles) {
                        if (Objects.equals(p.getId(), defProfId)) {
                            defProf = p;
                            break;
                        }
                    }
                } else {
                    // there is no config. Init config if exists a profile
                    if (!profiles.isEmpty()) {
                        defProf = profiles.get(0);
                        db().setUserConfig(ConfigConstant.DEFAULT_PROFILE_ID, defProf.getId().toString());
                    }
                }
                if (defProf != null) {
                    updateProfile(defProf);
                }
                return null;
            });
            return null;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Profile newProfile = new Profile()
                    .withName(data.getStringExtra("profileName"));
            db().insertProfile(newProfile, profile -> {
                updateProfile(profile);
                return null;
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        db().getProfiles(result -> {
            profiles = result;
            return null;
        });
    }

    private void updateProfile(Profile p) {
        // update name text
        selectedProfile = p;
        btnListProfile.setText(p.getName());
        db().setUserConfig(ConfigConstant.DEFAULT_PROFILE_ID, p.getId().toString(), a -> null);

        // update drawings from profile
        db().getDrawingsFromProfile(selectedProfile.getId(), drawings -> {
            ListAdapter adapter = new DrawingItemAdapter(this, drawings);
            lstDrawings.setAdapter(adapter);
            return null;
        });
    }
}
