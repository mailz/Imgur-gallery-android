package org.mailzz.imgurgallery;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.mailzz.imgurgallery.API.ImgurService;
import org.mailzz.imgurgallery.common.GeneralSwipeRefreshLayout;
import org.mailzz.imgurgallery.models.ObjectForDetailView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GalleryPreviewFragment extends Fragment implements Callback<JsonElement>, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private static final String TITLE_PARAM = "title_param";
    private static final String CATEGORY = "category";
    private static final String IS_TOPIC = "topic";

    private static final String TAG = "PREVIEW FRAGMENT";

    private static final String PAGE_NUMBER_STATE = "pageState";
    private static final String VALUES_STATE = "valuesState";
    private static final String CATEGORY_STATE = "categoryState";
    private static final String TOPIC_STATE = "topicState";
    private static final String SORT_STATE = "sortState";
    private static final String WINDOW_STATE = "windowState";


    private String mTitle;

    private OnFragmentInteractionListener mListener;

    private View mInfoLayout;
    private View mLoadingProgressBarBar;
    private View mErrorTextView;
    private GridView mGridView;
    private View mFooterProgressBar;

    private ArrayList<ObjectForDetailView> values;
    private GridGalleryAdapter gridAdapter;
    private GeneralSwipeRefreshLayout swipeLayout;

    //parameters for query
    private boolean isTopic; //of hot,top or topic name in category
    private String category; //hot,top
    private String sort;    //viral, top, time
    private String window;  //if section is top. day, week, month, year
    private int pageNumber;
    private boolean isEndOfFeed = false;
    private boolean isDataLoading;


    public GalleryPreviewFragment() {
        // Required empty public constructor
    }

    public static GalleryPreviewFragment newInstance(String param1, String param2, boolean param3) {
        GalleryPreviewFragment fragment = new GalleryPreviewFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_PARAM, param1);
        args.putString(CATEGORY, param2);
        args.putBoolean(IS_TOPIC, param3);
        fragment.setArguments(args);
        return fragment;
    }

    private void showContent() {
        mGridView.setVisibility(View.VISIBLE);
        mInfoLayout.setVisibility(View.GONE);
        mLoadingProgressBarBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
    }

    private void showError() {
        mGridView.setVisibility(View.GONE);
        mInfoLayout.setVisibility(View.VISIBLE);
        mLoadingProgressBarBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mGridView.setVisibility(View.GONE);
        mInfoLayout.setVisibility(View.VISIBLE);
        mLoadingProgressBarBar.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.GONE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE_PARAM);
            category = getArguments().getString(CATEGORY);
            isTopic = getArguments().getBoolean(IS_TOPIC);
        } else {
            mTitle = "Gallery";
            category = "hot";
            isTopic = false;
        }

        if (savedInstanceState != null) {
            pageNumber = savedInstanceState.getInt(PAGE_NUMBER_STATE);
            values = (ArrayList<ObjectForDetailView>) savedInstanceState.getSerializable(VALUES_STATE);
            category = savedInstanceState.getString(CATEGORY_STATE);
            sort = savedInstanceState.getString(SORT_STATE);
            window = savedInstanceState.getString(WINDOW_STATE);
        } else {
            pageNumber = 0;
            values = new ArrayList<>();
            sort = "viral";
            window = "";
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_NUMBER_STATE, pageNumber);
        outState.putSerializable(VALUES_STATE, values);
        outState.putString(CATEGORY_STATE, category);
        outState.putBoolean(TOPIC_STATE, isTopic);
        outState.putString(SORT_STATE, sort);
        outState.putString(WINDOW_STATE, window);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            pageNumber = savedInstanceState.getInt(PAGE_NUMBER_STATE);
            values = (ArrayList<ObjectForDetailView>) savedInstanceState.getSerializable(VALUES_STATE);
            category = savedInstanceState.getString(CATEGORY_STATE);
            isTopic = savedInstanceState.getBoolean(TOPIC_STATE);
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

        mInfoLayout = view.findViewById(R.id.loadingLayout);
        mLoadingProgressBarBar = mInfoLayout.findViewById(R.id.loadingProgressBar);
        mErrorTextView = mInfoLayout.findViewById(R.id.errorTextView);
        mGridView = (GridView) view.findViewById(R.id.gridView);

        mFooterProgressBar = view.findViewById(R.id.loadingBar);

        gridAdapter = new GridGalleryAdapter(getActivity(), R.layout.grid_item, values);
        mGridView.setAdapter(gridAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        swipeLayout = (GeneralSwipeRefreshLayout) view.findViewById(R.id.swipe);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
            @Override
            public boolean canChildScrollUp() {
                return mGridView.getFirstVisiblePosition() > 0 ||
                        mGridView.getChildAt(0) == null ||
                        mGridView.getChildAt(0).getTop() < 0;
            }
        });
        ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setTitle(mTitle);
            ab.setElevation(6);
        }
    }

    private void startDataLoad() {
        isDataLoading = true;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.BASE_ADDRESS)
                .build();
        ImgurService service = restAdapter.create(ImgurService.class);
        if(!isTopic){
            if (!window.isEmpty()){
                Log.d(TAG,"address is:  /gallery/"+category+"/"+sort+"/"+window+"/"+pageNumber+"/");
                service.getGallery(category, sort, window, pageNumber, this);
            }else{
                Log.d(TAG,"address is:  /gallery/"+category+"/"+sort+"/"+pageNumber+"/");
                service.getGallery(category, sort, pageNumber, this);
            }
        }else {
            if (!window.isEmpty()) {
                Log.d(TAG,"address is:  /topics/"+category+"/"+sort+"/"+window+"/"+pageNumber+"/");
                service.getTopic(category, sort, window, pageNumber, this);
            } else {
                Log.d(TAG,"address is:  /topics/"+category+"/"+sort+"/"+pageNumber+"/");
                service.getTopic(category, sort, pageNumber, this);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popularity:
                Toast.makeText(getActivity(), "popularity", Toast.LENGTH_SHORT).show();
                //sort hot
                sort = "hot";
                window = "";
                // start query
                pageNumber = 1;
                isEndOfFeed = false;
                values.clear();
                showLoading();
                startDataLoad();
                return true;
            case R.id.action_newest_first:
                Toast.makeText(getActivity(), "newest_first", Toast.LENGTH_SHORT).show();
                //sort time
                sort = "time";
                window = "";
                // start query
                pageNumber = 1;
                isEndOfFeed = false;
                values.clear();
                showLoading();
                startDataLoad();
                return true;
            case R.id.action_highest_scoring:
                //sort top
                sort = "top";
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.period_dialog_title))
                        .setCancelable(true)
                        .setItems(R.array.period_array, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        window = "day";
                                        break;
                                    case 1:
                                        window = "week";
                                        break;
                                    case 2:
                                        window = "month";
                                        break;
                                    case 3:
                                        window = "year";
                                        break;
                                    case 4:
                                        window = "all";
                                        break;
                                }
                                //start query
                                pageNumber = 1;
                                isEndOfFeed = false;
                                values.clear();
                                showLoading();
                                startDataLoad();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Config.IS_LOGGING) {
            Log.d(TAG, "values.isEmpty() " + values.isEmpty());
        }
        if (values != null && values.isEmpty()) {
            showLoading();
            startDataLoad();
        } else {
            gridAdapter.addAll(values);
            gridAdapter.notifyDataSetChanged();
            showContent();
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
        //parse json and create data for GridAdapter
        swipeLayout.setRefreshing(false);
        if (Config.IS_LOGGING) {
            Log.d(TAG, "data is recived");
        }
//        Toast.makeText(getActivity(), "data is recived", Toast.LENGTH_SHORT).show();
        JsonObject jo = jsonElement.getAsJsonObject();
        JsonArray data = jo.get("data").getAsJsonArray();
        if (Config.IS_LOGGING) {
            Log.d(TAG, "all data" + data.toString());
        }
        if (data.size() < 1) {
            mFooterProgressBar.setVisibility(View.GONE);
        }
        for (JsonElement je : data) {
            if (je != null) {
                if (Config.IS_LOGGING) {
                    Log.d(TAG, "element " + je.toString());
                }
                if (je.getAsJsonObject().has("is_album")) {
                    if (je.getAsJsonObject().get("is_album").getAsBoolean() && je.getAsJsonObject().has("cover")) {
                        if (!je.getAsJsonObject().get("cover").isJsonNull()) {
                            // create picture
                            String cover = je.getAsJsonObject().get("cover").getAsString();
                            cover = "http://i.imgur.com/" + cover + "t.jpg";
                            values.add(new ObjectForDetailView(je.getAsJsonObject().get("id").getAsString(), je.getAsJsonObject().get("is_album").getAsBoolean(), cover,je.getAsJsonObject().get("link").getAsString()));
                        } else {
                            values.add(new ObjectForDetailView(je.getAsJsonObject().get("id").getAsString(), je.getAsJsonObject().get("is_album").getAsBoolean(),"R.drawable.noimage",je.getAsJsonObject().get("link").getAsString()));
                        }
                    } else {
                        // get picture
                        String link = je.getAsJsonObject().get("id").getAsString();
                        link = "http://i.imgur.com/" + link + "t.jpg";
                        values.add(new ObjectForDetailView(je.getAsJsonObject().get("id").getAsString(), je.getAsJsonObject().get("is_album").getAsBoolean(),link,je.getAsJsonObject().get("link").getAsString()));
                    }
                }
            }
        }
        Log.d(TAG, "count in values " + values.size());
        gridAdapter.notifyDataSetChanged();
        showContent();
        isDataLoading = false;
    }

    @Override
    public void failure(RetrofitError error) {
        swipeLayout.setRefreshing(false);
        Log.e(TAG, "data isn't recived");
        Log.e(TAG, error.getMessage());
        showError();
        isDataLoading = false;
    }


    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh. data start refreshing.");
        swipeLayout.setRefreshing(true);
        pageNumber = 1;
        isEndOfFeed = false;
        values.clear();
        showLoading();
        startDataLoad();
        mFooterProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //download next page
        if (visibleItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
            if (!isEndOfFeed) {
                if (!isDataLoading) {
                    pageNumber++;
                    startDataLoad();
                    Log.d(TAG, "load next. page is " + pageNumber);
                }
            } else {
                Log.d(TAG, "there is no data to load");
                //TODO delete progressBar

            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(values.get(position));
        }

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
         void onFragmentInteraction(ObjectForDetailView obj);
    }


}
