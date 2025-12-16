package dsa.upc.edu.listapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import dsa.upc.edu.listapp.github.API;
import dsa.upc.edu.listapp.github.EETACBROSSystemService;
import dsa.upc.edu.listapp.github.Group;
import dsa.upc.edu.listapp.github.JoinGroupRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {

    private RecyclerView rvGroups;
    private GroupAdapter adapter;

    private EETACBROSSystemService api;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        api = API.getGithub();

        rvGroups = (RecyclerView) findViewById(R.id.rvGroups);
        rvGroups.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupAdapter(group -> joinGroup(group.id));
        rvGroups.setAdapter(adapter);


        userId = getLoggedUserId();
        if (userId == -1) {
            userId = 1;
        }



        loadGroups();
    }


    private void loadGroups() {
        api.getGroups().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setData(response.body());
                } else {
                    Toast.makeText(GroupActivity.this, "Error carregant grups (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Toast.makeText(GroupActivity.this, "Fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void joinGroup(int groupId) {
        if (userId == -1) {
            Toast.makeText(this, "Error: User not logged", Toast.LENGTH_SHORT).show();
            return;
        }

        api.joinGroup(groupId, new JoinGroupRequest(userId)).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GroupActivity.this,
                            "Unit al grup " + groupId,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupActivity.this,
                            "No s'ha pogut unir (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Toast.makeText(GroupActivity.this,
                        "Connexió fallida: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private int getLoggedUserId() {
        SharedPreferences prefs = getSharedPreferences("EETACBROSPreferences", MODE_PRIVATE);

        int userId = -1;
        try {
            userId = prefs.getInt("userId", -1);
        } catch (ClassCastException e) {
            String legacyUserId = prefs.getString("userId", null);
            if (legacyUserId != null) {
                try {
                    userId = Integer.parseInt(legacyUserId);
                    prefs.edit().putInt("userId", userId).apply();
                } catch (NumberFormatException ignored) {}
            }
        }
        return userId;
    }


}
