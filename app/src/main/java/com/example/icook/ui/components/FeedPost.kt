package com.example.icook.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.icook.R
import com.example.icook.data.models.Post
import com.example.icook.utils.loadImageFromBase64

@Composable
fun FeedPost(
    post : Post = Post(),
    modifier : Modifier = Modifier,
) {
    var context = LocalContext.current
    Column(
        modifier = modifier
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_user),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "@${post.username}",
                modifier = Modifier.padding(start=5.dp)
            )
        }
        Image(
            painter = if (post.picture != "") {
                loadImageFromBase64(base64Image = post.picture, context = context)
            } else {
                painterResource(id = R.drawable.default_post)
                   },
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ){
            Icon(
                Icons.Outlined.Favorite,
                contentDescription = null,
                modifier = Modifier.padding(end = 20.dp))
            Icon(Icons.Outlined.Create, contentDescription = null)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(Icons.Outlined.Star, contentDescription = null)
            }
        }
        Text(
            text = "999 likes",
            modifier = Modifier.padding(start = 20.dp, bottom = 5.dp)
        )
        Text(
            text = "@${post.username}: ${post.description}",
            modifier = Modifier.padding(start = 20.dp)
        )
        TextButton(onClick = { /*TODO*/ }) {
            Text(
                text = "View all comments",
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Text(
            text = "Posted at 12/12/12",
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)
        )


    }

}

@Composable
fun FeedPostList(
    modifier : Modifier = Modifier,
    posts: List<Post> = listOf(),
){
    LazyColumn(
        modifier = modifier
    ) {
        items(posts) {post ->
            FeedPost(post = post)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedPostComponentPreview() {
    FeedPost(
        post = Post(
            username = "aramirez",
            description = "This is an awesome description"
        )
    )
}