package com.example.icook.ui.components

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.icook.data.models.PostWithUser
import com.example.icook.data.models.User
import com.example.icook.data.models.UserPostOptions
import com.example.icook.utils.getAge
import com.example.icook.utils.loadImageFromBase64

@Composable
fun FeedPost(
    post : Post = Post(),
    user: User = User(),
    onPostOptionClicked: (Post, UserPostOptions) -> Unit = { _, _ -> },
    isUserPost: Boolean = false,
) {
    var dropMenuExpanded by remember {
        mutableStateOf(false)
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                ){
                Image(
                    painter = if (user.picture != "") {
                        loadImageFromBase64(base64Image = user.picture, context = LocalContext.current)
                    } else {
                        painterResource(id = R.drawable.default_user)
                    },
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

            if (isUserPost) {
                IconButton(
                    onClick = { dropMenuExpanded = true }
                ) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    DropdownMenu(
                        expanded = dropMenuExpanded,
                        onDismissRequest = { dropMenuExpanded = false }
                    ) {
                        UserPostOptions.entries.forEach { postOption ->
                            DropdownMenuItem(
                                text = { Text(text = postOption.name) },
                                onClick = {
                                    dropMenuExpanded = false
                                    onPostOptionClicked(post, postOption)
                                }
                            )
                        }
                    }
                }
            }
        }
        Image(
            painter = if (post.picture != "") {
                loadImageFromBase64(base64Image = post.picture, context = LocalContext.current)
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
            text = "Posted ${getAge(post.date)}",
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)
        )
    }
}

@Composable
fun FeedPostList(
    modifier : Modifier = Modifier,
    postsWithUsers: List<PostWithUser> = listOf(),
    onPostOptionClicked: (Post, UserPostOptions) -> Unit = { _, _ ->},
    isUserPosts: Boolean = false,
){
    LazyColumn(
        modifier = modifier
    ) {
        items(postsWithUsers) { postWithUser ->
            FeedPost(
                post = postWithUser.post,
                user = postWithUser.user,
                onPostOptionClicked = onPostOptionClicked,
                isUserPost = isUserPosts,
            )
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
        ),
        isUserPost = true,
    )
}