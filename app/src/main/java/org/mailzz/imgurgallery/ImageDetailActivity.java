package org.mailzz.imgurgallery;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.mailzz.imgurgallery.API.ImgurService;
import org.mailzz.imgurgallery.common.FullscreenImageAdapter;
import org.mailzz.imgurgallery.models.ObjectForDetailView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ImageDetailActivity extends AppCompatActivity implements Callback<JsonElement>{

    private static final String TAG = "ImageDetailActivity";

    private ViewPager mViewPager;
    private View mInfoLayout;
    private View mLoadingProgressBarBar;
    private View mErrorTextView;
    private ObjectForDetailView imageData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mInfoLayout = findViewById(R.id.loadingLayout);
        mLoadingProgressBarBar = mInfoLayout.findViewById(R.id.loadingProgressBar);
        mErrorTextView = mInfoLayout.findViewById(R.id.errorTextView);

        if(getIntent().hasExtra("object")){
            imageData = (ObjectForDetailView) getIntent().getExtras().getSerializable("object");
        }else{
            finish();
        }
    }

    private void showContent() {
        mViewPager.setVisibility(View.VISIBLE);
        mInfoLayout.setVisibility(View.GONE);
        mLoadingProgressBarBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
    }

    private void showError() {
        mViewPager.setVisibility(View.GONE);
        mInfoLayout.setVisibility(View.VISIBLE);
        mLoadingProgressBarBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mViewPager.setVisibility(View.GONE);
        mInfoLayout.setVisibility(View.VISIBLE);
        mLoadingProgressBarBar.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(imageData.isAlbum()){
            showLoading();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Config.BASE_ADDRESS)
                    .build();
            ImgurService service = restAdapter.create(ImgurService.class);
            service.getAlbum(imageData.getId(), this);
        }else{
            ArrayList<String> arr = new ArrayList<>();
            Log.d(TAG, imageData.getFullLink());
            arr.add(imageData.getFullLink());
            FullscreenImageAdapter adapter = new FullscreenImageAdapter(this,arr);
            mViewPager.setAdapter(adapter);
            showContent();
        }
    }

    @Override
    public void success(JsonElement jsonElement, Response response) {
        ArrayList<String> arr = new ArrayList<>();
        Log.d(TAG, "data is " + jsonElement);
        JsonObject jo = jsonElement.getAsJsonObject();
        JsonObject jo2 = jo.get("data").getAsJsonObject();
        JsonArray data = jo2.get("images").getAsJsonArray();
        for (JsonElement je : data) {
            String link = je.getAsJsonObject().get("link").getAsString();
            arr.add(link);
        }
        FullscreenImageAdapter adapter = new FullscreenImageAdapter(this,arr);
        mViewPager.setAdapter(adapter);
        showContent();
    }

    @Override
    public void failure(RetrofitError error) {
        showError();
    }
}
