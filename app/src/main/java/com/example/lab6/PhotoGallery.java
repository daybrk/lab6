package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.lab6.api.FlickrAPI;
import com.example.lab6.api.ServiceAPI;
import com.example.lab6.db.PhotosDB;
import com.example.lab6.db.PhotosDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoGallery extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private static Context context;
    private static String q = null;
    private static List<Photo> photos;
    private PhotosDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        recyclerView = (RecyclerView) findViewById(R.id.listR);
        PhotoGallery.context = getApplicationContext();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bnv);

        db = PhotosDB.getDatabase(getApplicationContext());
        context = this;

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener
            navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.button_click:
                    updateItems(q);
                    break;
                case R.id.motion_event:
                    photos = db.photoDao().LoadAll();
                    recyclerView.setAdapter(new PhotoAdapter(photos));
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchView);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                updateItems(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    private void updateItems(String query) {
        FlickrAPI flickrAPI = ServiceAPI.getRetrofit().create(FlickrAPI.class);
        Call<Example> call;
        if (query == null) {
            call = flickrAPI.getRecent();
        } else {
            q = query;
            call = flickrAPI.getSearchPhotos(query);
        }
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.isSuccessful()) {
                    Example example = response.body();
                    photos = example.getPhotos().getPhoto();
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                    recyclerView.setAdapter(new PhotoAdapter(photos));
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });
    }

    public static Context getAppContext() {
        return PhotoGallery.context;
    }

}
