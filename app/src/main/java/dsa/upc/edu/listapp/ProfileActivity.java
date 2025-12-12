package dsa.upc.edu.listapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dsa.upc.edu.listapp.github.API;
import dsa.upc.edu.listapp.github.EETACBROSSystemService;
import dsa.upc.edu.listapp.github.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private EETACBROSSystemService api;
    private SharedPreferences prefs;

    private Button gamesBtn,settingsBtn, logoutBtn, shopBtn, profileBtn;

    private TextView usernameTextView, coinsTextView;
    private RecyclerView inventoryRecycler;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Item> userItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences("EETACBROSPreferences", MODE_PRIVATE);

        gamesBtn = findViewById(R.id.gamesBtn);

        settingsBtn = findViewById(R.id.settingsBtn);

        shopBtn = findViewById(R.id.shopBtn);

        logoutBtn = findViewById(R.id.logoutBtn);


        inventoryRecycler = findViewById(R.id.inventoryRecycler);

        usernameTextView = findViewById(R.id.username);

        coinsTextView = findViewById(R.id.totalCoins);

        if (inventoryRecycler != null) {
            inventoryRecycler.setHasFixedSize(true);
            layoutManager = new GridLayoutManager(this, 3);
            inventoryRecycler.setLayoutManager(layoutManager);
            mAdapter = new MyAdapter();
            inventoryRecycler.setAdapter(mAdapter);
        }

        api = API.getGithub();

        loadUserData();

        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(v -> logOut());
        }
        if (shopBtn != null) {
            shopBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, ShopActivity.class);
                startActivity(intent);
            });
        }
        if (settingsBtn != null) {
            settingsBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfileDisplay();
    }

    private void updateProfileDisplay() {
        String username = prefs.getString("username", "Unknown");
        int coins = prefs.getInt("coins", 0);
        if (usernameTextView != null) {
            usernameTextView.setText(username);
        }
        if (coinsTextView != null) {
            coinsTextView.setText(String.valueOf(coins));
        }
    }

    private void renderUserItems() {
        if (mAdapter != null) {
            mAdapter.setData(userItems);
        }
    }

    private void logOut() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("username");
        editor.remove("password");
        editor.remove("coins");
        editor.putInt("userId", -1);
        editor.putBoolean("isLoggedIn", false);
        editor.commit();
        editor.apply();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private int getUserIdSafely() {
        int userId = -1;
        try {
            userId = prefs.getInt("userId", -1);
        } catch (ClassCastException e) {
            String legacyUserId = prefs.getString("userId", null);
            if (legacyUserId != null) {
                try {
                    userId = Integer.parseInt(legacyUserId);
                    // Update to new format
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("userId", userId);
                    editor.apply();
                } catch (NumberFormatException nfe) {
                    // Ignored
                }
            }
        }
        return userId;
    }

    private void loadUserData() {
        int userId = getUserIdSafely();
        if (userId == -1) {
            Toast.makeText(ProfileActivity.this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        api.getUserItems(userId).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userItems.clear();
                    userItems.addAll(response.body());
                    renderUserItems();
                    Toast.makeText(ProfileActivity.this,
                            "Inventory loaded: " + userItems.size() + " items",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this,
                            "Failed to load inventory",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e(TAG, "Connection error", t);
                Toast.makeText(ProfileActivity.this,
                        "Connection error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
