package com.example.icook.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.icook.R
import com.example.icook.data.models.Comment
import com.example.icook.data.models.Post
import com.example.icook.data.models.PostWithUser
import com.example.icook.data.models.UserPostOptions
import com.example.icook.data.models.User
import com.example.icook.ui.components.BottomNavigationBar
import com.example.icook.ui.components.FeedPostList
import com.example.icook.ui.components.Snackbar
import com.example.icook.ui.components.UpdateUserPicture
import com.example.icook.utils.loadImageFromBase64

@Composable
fun ProfileScreen(
    user: User = User(),
    likes_counter: Int = 0,
    posts: List<Post> = listOf(),
    postsWithComments : Map<Post, List<Comment>> = mapOf(),
    onHomeButtonClicked: () -> Unit = {},
    onCreatePostButtonClicked: () -> Unit = {},
    onProfileButtonClicked: () -> Unit = {},
    persistNewUserDescription: (newUserDescription: String ) -> Unit = {},
    onPostOptionClicked: (Post, UserPostOptions) -> Unit = { _, _ ->},
    onUpdateUserPictureClicked: (Uri?) -> Unit = {},
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    userPictureScreenEnabled : Boolean = false,
    setUserPictureScreenEnabled : (newValue : Boolean) -> Unit = {_ -> },
    onLogOutButtonClicked: () -> Unit = {},
    isMyProfile: Boolean = true,
    onCreateNewCommentButtonClicked: (String, Post) -> Unit = {_,_ ->},
    onViewAllCommentsClicked: (Post) -> Unit = {_ ->}
) {
    var descriptionEditingEnabled by remember { mutableStateOf<Boolean>(false) }
    var userDescription by remember { mutableStateOf<String>(user.description?: "Description not found") }

    Column(
        modifier = if (userPictureScreenEnabled) {Modifier.blur(3.dp)} else {Modifier}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, top = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "@${user.username}",
            )
            if (isMyProfile) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Log out")
                    TextButton(
                        onClick = { onLogOutButtonClicked() }
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            TextButton(
                enabled=isMyProfile,
                onClick = {
                    setUserPictureScreenEnabled(true)
                }
            ) {
                Image(
                    painter = if (user.picture != "") {
                        loadImageFromBase64(base64Image = user.picture, context = LocalContext.current)
                    } else {
                        painterResource(id = R.drawable.default_user)
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.DarkGray, CircleShape)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "${posts.size}")
                    Text(text = "posts")
                }
                Column(
                    modifier = Modifier.padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$likes_counter")
                    Text(text = "likes")
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                label = {Text(text = "${user.name} ${user.lastname}")},
                modifier = Modifier.weight(0.8f),
                maxLines = 3,
                enabled = descriptionEditingEnabled,
                value = userDescription,
                trailingIcon = {
                    IconToggleButton(
                        checked = true,
                        modifier = Modifier.weight(0.2f),
                        onCheckedChange = {checked ->
                            descriptionEditingEnabled = !descriptionEditingEnabled
                            if (!checked) {
                                persistNewUserDescription(userDescription)
                            }
                        }
                    ) {
                        if (isMyProfile) {
                            Icon(
                                if (descriptionEditingEnabled) {Icons.Default.Check} else {Icons.Default.Create}
                                , contentDescription = null
                            )
                        }
                      }
                               },
                onValueChange ={newValue -> userDescription  = newValue}
            )
        }
        Text(
            text = "Posts",
            modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 4.dp)
        )
        FeedPostList(
            modifier = Modifier.weight(1f),
            postsWithUsers = posts.map{ post -> PostWithUser(post = post, user = user) },
            onPostOptionClicked = onPostOptionClicked,
            isUserPosts = isMyProfile,
            onCreateNewCommentButtonClicked = onCreateNewCommentButtonClicked,
            onViewAllCommentsClicked = onViewAllCommentsClicked,
            postsWithComments = postsWithComments,
        )
        BottomNavigationBar(
            onHomeButtonClicked = onHomeButtonClicked,
            onCreatePostButtonClicked = onCreatePostButtonClicked,
            onProfileButtonClicked = onProfileButtonClicked,
        )
    }
    if (userPictureScreenEnabled) {
        UpdateUserPicture(
            picture = user.picture,
            onCloseButtonClicked = { setUserPictureScreenEnabled(false) },
            onUpdateButtonClicked = { uri: Uri? -> onUpdateUserPictureClicked(uri)},
        )
    }
    Snackbar(hostState = snackbarHostState)

}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen(
        user=User(
            username = "aramirez",
            name = "Adrian",
            lastname = "Ramirez",
            description = "This is a basic description"
        ),
        posts = listOf(Post(), Post()),
        isMyProfile = true,
    )
}