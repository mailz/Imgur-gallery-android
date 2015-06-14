package org.mailzz.imgurgallery.API;

import com.google.gson.JsonElement;

import org.mailzz.imgurgallery.Config;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * AppWell.org
 * Created by dmitrijtrandin on 12.06.15.
 */
public interface ImgurService {


//    section	optional	hot | top | user - defaults to hot
//    sort	optional	viral | top | time | rising (only available with user section) - defaults to viral
//    page	optional	integer - the data paging number
//    window	optional	Change the date range of the request if the section is "top", day | week | month | year | all, defaults to day
//    showViral	optional	true | false - Show or hide viral images from the 'user' section. Defaults to true
    @Headers("Authorization: Client-ID " + Config.CLIENT_ID)
    @GET("/gallery/{section}/{sort}/{window}/{page}/")
    void getGallery(@Path("section") String section, @Path("sort") String sort, @Path("window") String window, @Path("page") int page, Callback<JsonElement> cb);

    @Headers("Authorization: Client-ID " + Config.CLIENT_ID)
    @GET("/gallery/{section}/{sort}/{window}/{page}/")
    void getGallery(@Path("section") String section, @Path("sort") String sort, @Path("page") int page, Callback<JsonElement> cb);

    @Headers("Authorization: Client-ID " + Config.CLIENT_ID)
    @GET("/topics/{section}/{sort}/{window}/{page}/")
    void getTopic(@Path("section") String section, @Path("sort") String sort, @Path("window") String window, @Path("page") int page, Callback<JsonElement> cb);

    @Headers("Authorization: Client-ID " + Config.CLIENT_ID)
    @GET("/topics/{section}/{sort}/{window}/{page}/")
    void getTopic(@Path("section") String section, @Path("sort") String sort, @Path("page") int page, Callback<JsonElement> cb);

    @Headers("Authorization: Client-ID " + Config.CLIENT_ID)
    @GET("/gallery/{id}/")
    void getAlbum(@Path("id") String id, Callback<JsonElement> cb);
}
