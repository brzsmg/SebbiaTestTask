# Test task for Sebbia

<h3>Operations</h3>
<p><b>GET</b> /v1/news/categories</p>
<p><b>GET</b> /v1/news/categories/{id}/news</p>
<p><b>GET</b> /v1/news/details</p>

<h3>Screenshots</h3>
<div style="display:flex;">
<img alt="App image" src="documents/screenshots/categories.jpg" width="30%">
<img alt="App image" src="documents/screenshots/news_list.jpg" width="30%">
<img alt="App image" src="documents/screenshots/news_details.jpg" width="30%">
</div>
<div>
<p><img alt="App image" src="documents/screenshots/server_unavailable.jpg" width="100%"></p>
<p><img alt="App image" src="documents/screenshots/refresh.jpg"></p>
<p><img alt="App image" src="documents/screenshots/toast.jpg"></p>
</div>

Entities:

```kotlin
class ResultEntity<T> (
    val code : Int,
    val list : List<T>
)

class Category(
    val id : Int,
    val name : String
)

class News (
    val id: Int,
    val date: DateTime,
    val fullDescription: String,
    val shortDescription: String,
    val title: String
)

class NewsEntity (
    val code : Int,
    val news : News
)
```

Interface:

```kotlin
interface NewsApi {

    @GET("/v1/news/categories")
    fun requestCategories() : Single<Response<ResultEntity<Category>>>

    @GET("/v1/news/categories/{id}/news")
    fun requestCategory(
        @Path("id") id : Int,
        @Query("page") page : Int
    ) : Single<Response<ResultEntity<News>>>

    @GET("/v1/news/details")
    fun requestNewsDetails(@Query("id") id : Int) : Single<Response<NewsEntity>>

}
```

Links:
<p><a href='http://testtask.sebbia.com/swagger-ui.html'>Swagger</a></p>
