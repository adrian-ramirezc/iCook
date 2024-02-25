package com.example.icook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.icook.ICookState
import com.example.icook.data.models.Comment
import com.example.icook.data.models.Post
import com.example.icook.data.models.PostWithUser
import com.example.icook.data.models.User
import com.example.icook.ui.components.BottomNavigationBar
import com.example.icook.ui.components.FeedPostList

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user : User = User(),
    postsWithComments : Map<Post, List<Comment>> = mapOf(),
    feedPostsWithUsers: List<PostWithUser> = listOf(),
    onProfileButtonClicked: () -> Unit = {},
    onCreatePostButtonClicked: () -> Unit = {},
    onOtherUserPictureClicked: (user: User) -> Unit = {},
    onCreateNewCommentButtonClicked: (String, Post) -> Unit = {_,_ -> },
    onViewAllCommentsClicked: (Post) -> Unit = { _ ->},
    onLikePostClicked: (Int, Boolean) -> Unit = {_,_->}
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
            postsWithUsers = feedPostsWithUsers,
            loggedInUser = user,
            onOtherUserPictureClicked = { user: User -> onOtherUserPictureClicked(user) },
            onCreateNewCommentButtonClicked = onCreateNewCommentButtonClicked,
            onViewAllCommentsClicked = onViewAllCommentsClicked,
            postsWithComments = postsWithComments,
            onLikePostClicked=onLikePostClicked,
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
        feedPostsWithUsers = listOf(
            PostWithUser(post = Post(username = "wfrezaq"), user= User()),
            PostWithUser(post = Post(username = "usertest"), user = User()),
            PostWithUser(post = Post(username = "helloitsme"), user = User())
        )
    )
}