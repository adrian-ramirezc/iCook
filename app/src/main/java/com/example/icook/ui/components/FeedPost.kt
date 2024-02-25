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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.sp
import com.example.icook.R
import com.example.icook.data.models.Comment
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
    comments : List<Comment> = listOf(),
    onOtherUserPictureClicked: (user: User) -> Unit = { _ -> },
    onPostOptionClicked: (Post, UserPostOptions) -> Unit = { _, _ -> },
    isUserPost: Boolean = false,
    onCreateNewCommentButtonClicked: (String, Post) -> Unit = {_,_ ->},
    onViewAllCommentsClicked: (Post) -> Unit = {_ ->}
) {
    var dropMenuExpanded by remember {
        mutableStateOf(false)
    }

    var writeNewCommentEnabled by remember {
        mutableStateOf(false)
    }

    var commentText by remember {
        mutableStateOf("")
    }

    var showComments by remember {
        mutableStateOf(false)
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = { onOtherUserPictureClicked(user) }) {
                    Image(
                        painter = if (user.picture != "") {
                            loadImageFromBase64(
                                base64Image = user.picture,
                                context = LocalContext.current
                            )
                        } else {
                            painterResource(id = R.drawable.default_user)
                        },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
                Text(
                    text = "@${post.username}",
                    modifier = Modifier.padding(start = 5.dp)
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
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconToggleButton(checked = true, onCheckedChange = {}) {
                    Icon(
                        Icons.Outlined.Favorite,
                        contentDescription = null,
                    )
                }
                IconToggleButton(
                    checked = writeNewCommentEnabled,
                    onCheckedChange = { writeNewCommentEnabled = true }
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null,
                    )
                }
            }
            Text(
                text = "0 likes",
            )
            Text(
                text = "@${post.username}: ${post.description}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )
            if (writeNewCommentEnabled) {
                OutlinedTextField(
                    value = commentText,
                    placeholder = { Text(text = "Add your comment here") },
                    onValueChange = {value : String -> commentText = value},
                    trailingIcon = {
                        TextButton(
                            onClick = {
                                writeNewCommentEnabled = false
                                onCreateNewCommentButtonClicked(commentText, post)
                                commentText = ""
                                showComments = false
                            }
                        ) {
                            Icon(Icons.Outlined.Send, contentDescription = null)
                        }
                                   },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                )
            }
            if (showComments) {
                if (comments.isNotEmpty()) {
                    Column(
                        modifier = Modifier.padding(start = 5.dp)
                    ) {
                        for (comment in comments) {
                            Row {
                                Text(
                                    text = "@${comment.username}: ",
                                    fontSize = 12.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                )
                                Text(
                                    text = comment.text!!,
                                    fontSize = 12.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                )
                            }

                        }
                    }
                } else {
                    Text(
                        text = "There are no comments yet",
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        )
                }
            }
            TextButton(
                onClick = {
                    onViewAllCommentsClicked(post)
                    showComments = !showComments
                          },
            ) {
                Text( text = if (!showComments) {"View all comments"} else {"Hide all comments"})
            }
            Text( text = "Posted ${getAge(post.date)}")
        }
    }
}

@Composable
fun FeedPostList(
    modifier : Modifier = Modifier,
    postsWithUsers: List<PostWithUser> = listOf(),
    postsWithComments : Map<Post, List<Comment>> = mapOf(),
    onPostOptionClicked: (Post, UserPostOptions) -> Unit = { _, _ ->},
    isUserPosts: Boolean = false,
    onOtherUserPictureClicked: (user: User) -> Unit = {},
    onCreateNewCommentButtonClicked: (String, Post) -> Unit = {_,_ ->},
    onViewAllCommentsClicked: (Post) -> Unit = {_ ->}
){
    LazyColumn(
        modifier = modifier
    ) {
        items(postsWithUsers) { postWithUser ->
            FeedPost(
                post = postWithUser.post,
                user = postWithUser.user,
                onOtherUserPictureClicked = { user: User -> onOtherUserPictureClicked(user) },
                onPostOptionClicked = onPostOptionClicked,
                isUserPost = isUserPosts,
                onCreateNewCommentButtonClicked = onCreateNewCommentButtonClicked,
                onViewAllCommentsClicked = onViewAllCommentsClicked,
                comments = postsWithComments[postWithUser.post]?: listOf()
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
        comments = listOf(
            Comment(username = "aramirez", text="It looks delicious"),
            Comment(username = "fjordan", text="Amazing!")
        )
    )
}