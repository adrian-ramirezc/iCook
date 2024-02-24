package com.example.icook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.icook.data.models.Post
import com.example.icook.data.models.User
import com.example.icook.ui.components.BottomNavigationBar
import com.example.icook.ui.components.FeedPostList

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user : User = User(),
    feedPosts: List<Post> = listOf<Post>(),
    onProfileButtonClicked: () -> Unit,
    onCreatePostButtonClicked: () -> Unit,
    ) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Hello, ${user.name} !",
                modifier = Modifier.padding(15.dp)
            )
        }
        FeedPostList(
            modifier = Modifier.weight(1f),
            posts = feedPosts,
        )
        BottomNavigationBar(
            onHomeButtonClicked = {},
            onProfileButtonClicked = onProfileButtonClicked,
            onCreatePostButtonClicked = onCreatePostButtonClicked,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        user = User(username = "aramirez", name="Adrian"),
        onProfileButtonClicked = {},
        onCreatePostButtonClicked = {},
        feedPosts = listOf(
            Post(username = "wfrezaq"),
            Post(username = "usertest"),
            Post(username = "helloitsme")
        )
    )
}