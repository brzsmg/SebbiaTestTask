# Test task for Sebbia

Operations:
<p><b>GET</b> /v1/news/categories</p>
<p><b>GET</b> /v1/news/categories/{id}/news</p>
<p><b>GET</b> /v1/news/details</p>

<div style="display:flex;">
<img alt="App image" src="documents/screenshots/categories.jpg" width="30%">
<img alt="App image" src="documents/screenshots/news_list.jpg" width="30%">
<img alt="App image" src="documents/screenshots/news_details.jpg" width="30%">
</div>

```kotlin
//Entities

class ResultEntity<T> {
    var code : Int? = null
    var list : List<T>? = null
}

class Category {
    var id : Int? = null
    var name : String? = null
}

class News {
    var id: Int? = null
    var date: DateTime? = null
    var fullDescription: String? = null
    var shortDescription: String? = null
    var title: String? = null
}

class NewsEntity {
    var code : Int? = null
    var news : News? = null
}

```

<a href='http://testtask.sebbia.com/swagger-ui.html'>Swagger</a>
