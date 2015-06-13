package org.mailzz.imgurgallery;

import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.mailzz.imgurgallery.API.ImgurService;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;



public class GalleryPreviewFragment extends Fragment implements Callback<JsonElement> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE_PARAM = "title_param";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "PREVIEW FRAGMENT";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mTextForDesc;

    private OnFragmentInteractionListener mListener;

    private View infoLayout;
    private View loadingProgressBarBar;
    private View errorTextView;
    private GridView gridView;

    private ArrayList<String> values;
    private GridGalleryAdapter gridAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryPreviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryPreviewFragment newInstance(String param1, String param2) {
        GalleryPreviewFragment fragment = new GalleryPreviewFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_PARAM, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GalleryPreviewFragment() {
        // Required empty public constructor
    }

    private void showContent() {
        gridView.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        loadingProgressBarBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
    }

    private void showError() {
        gridView.setVisibility(View.GONE);
        infoLayout.setVisibility(View.VISIBLE);
        loadingProgressBarBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        gridView.setVisibility(View.GONE);
        infoLayout.setVisibility(View.VISIBLE);
        loadingProgressBarBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE_PARAM);
            mTextForDesc = getArguments().getString(ARG_PARAM2);
        } else{
            mTitle = "Gallery";
            mTextForDesc = "Nothing";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        infoLayout = view.findViewById(R.id.loadingLayout);
        loadingProgressBarBar = infoLayout.findViewById(R.id.loadingProgressBar);
        errorTextView = infoLayout.findViewById(R.id.errorTextView);
        gridView = (GridView)view.findViewById(R.id.gridView);

        showLoading();

        ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        ab.setTitle(mTitle);
        ab.setElevation(6);


    }


    @Override
    public void onStart() {
        super.onStart();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.BASE_ADDRESS)
                .build();

        ImgurService service = restAdapter.create(ImgurService.class);

        service.getGallery("hot","viral", 1, this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onFragmentInteraction(string);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void success(JsonElement jsonElement, Response response) {
        //TODO parse json and create data for GridAdapter
        Log.d(TAG, "data is recived");
        Toast.makeText(getActivity(),"data is recived",Toast.LENGTH_SHORT).show();
        values = new ArrayList<String>();
        JsonObject jo =  jsonElement.getAsJsonObject();
        JsonArray data = jo.get("data").getAsJsonArray();
        if(Config.IS_LOGGING){
            Log.d(TAG, "all data" + data.toString());
        }
        for(JsonElement je : data){
            if(Config.IS_LOGGING){
                Log.d(TAG, "element " + je.toString());
            }
            if(je.getAsJsonObject().has("is_album")){
                if(je.getAsJsonObject().get("is_album").getAsBoolean()){
                    // create picture
                    String cover = je.getAsJsonObject().get("cover").getAsString();
                    cover = "http://i.imgur.com/"+cover+"t.jpg";
                    values.add(cover);
                }else{
                    // get picture
//                    String link = je.getAsJsonObject().get("link").getAsString();
                    String link = je.getAsJsonObject().get("id").getAsString();
                    link = "http://i.imgur.com/"+link+"t.jpg";
                    values.add(link);
                }
            }
        }
        for(String str : values){
            Log.d(TAG, "image is " + str);
        }

        gridAdapter = new GridGalleryAdapter(getActivity(), R.layout.grid_item, values);
        gridView.setAdapter(gridAdapter);
        showContent();
    }

    @Override
    public void failure(RetrofitError error) {
        Log.e(TAG, "data isn't recived");
        Log.e(TAG, error.getMessage());
        showError();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String string);
    }

}
