import com.urbanfresh.ui.home.homeResponse.HomeResponse
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {

    @POST("api/home_test_section")
    fun getWeather(@Query("categoty_id") city: String): Observable<HomeResponse>

}
