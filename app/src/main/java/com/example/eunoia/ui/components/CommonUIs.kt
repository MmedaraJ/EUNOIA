package com.example.eunoia.ui.components

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.eunoia.R
import com.example.eunoia.ui.theme.*

val bioRhymeFonts = FontFamily(
    Font(R.font.biorhyme_regular, weight = FontWeight.Normal),
    Font(R.font.biorhyme_bold1, weight =  FontWeight.Bold),
    Font(R.font.biorhyme_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.biorhyme_light, weight =  FontWeight.Light),
    Font(R.font.biorhyme_extralight, weight = FontWeight.ExtraLight)
)

@Composable
fun StandardBlueButton(text: String, lambda: () -> Unit){
    Button(
        onClick = { lambda() },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        modifier = Modifier
            .size(
                width = dimensionResource(id = R.dimen.sign_in_out_width_dimen),
                height = dimensionResource(id = R.dimen.sign_in_out_height_dimen)
            )
            .testTag(text)
    ) {
        NormalText(
            text,
            Black,
            15,
            0,
            0
        )
    }
}

@Composable
fun StandardSubscribeButton(text: String, lambda: () -> Unit){
    Button(
        onClick = { lambda() },
        colors = ButtonDefaults.buttonColors(backgroundColor = background),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
        border = BorderStroke(0.5.dp, Black),
        modifier = Modifier
            .height(height = 43.dp)
    ) {
        NormalText(
            text,
            Black,
            12,
            0,
            0
        )
    }
}

@Composable
fun ContributorSubscribeButton(text: String, lambda: () -> Unit){
    Button(
        onClick = { lambda() },
        colors = ButtonDefaults.buttonColors(backgroundColor = ContributorSubscribeButtonBackground),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
        border = BorderStroke(0.5.dp, Black),
        modifier = Modifier
            .height(height = 43.dp)
    ) {
        NormalText(
            text,
            Black,
            12,
            0,
            0
        )
    }
}

@Composable
fun standardOutlinedTextInput(width: Int, height: Int, placeholder: String, offset: Int): String{
    var text by rememberSaveable{ mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            focusedBorderColor = MaterialTheme.colors.onPrimary,
            unfocusedBorderColor = MaterialTheme.colors.onPrimary,
            textColor = MaterialTheme.colors.onPrimary
        ),
        singleLine = true,
        textStyle = MaterialTheme.typography.h3,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(0.dp)
                    .offset(x = offset.dp)
            )
        },
        modifier = Modifier
            .size(width.dp, height.dp)
            .padding(0.dp)
            .testTag(placeholder)
    )
    return text
}

@Composable
fun standardCentralizedOutlinedTextInput(placeholder: String, color: Color): String{
    var text by rememberSaveable{ mutableStateOf(placeholder) }
    val context = LocalContext.current
    val maxLength = 30
    OutlinedTextField(
        value = text,
        onValueChange = {
            if (it.length <= maxLength) text = it
            else Toast.makeText(context, "Sound name cannot be more than $maxLength characters", Toast.LENGTH_SHORT).show()
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = color.copy(alpha = 0f),
            focusedBorderColor = color.copy(alpha = 0f),
            unfocusedBorderColor = color.copy(alpha = 0f),
            textColor = MaterialTheme.colors.onPrimary
        ),
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontFamily = bioRhymeFonts,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        ),
        singleLine = true,
        modifier = Modifier
            .wrapContentSize()
            .padding(0.dp)
    )
    return text
}

@Composable
fun bigOutlinedTextInput(
    height: Int,
    placeholder: String,
    backgroundColor: Color,
    focusedBorderColor: Color,
    unfocusedBorderColor: Color,
    textColor: Color,
    placeholderColor: Color,
    placeholderTextSize: Int
): String{
    var text by rememberSaveable{ mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = backgroundColor,
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            textColor = textColor
        ),
        textStyle = MaterialTheme.typography.h3,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
        placeholder = {
            Column(verticalArrangement = Arrangement.Top){
                ExtraLightText(
                    placeholder,
                    placeholderColor,
                    placeholderTextSize,
                    0,
                    0
                )
            }
        },
        modifier = Modifier
            .height(height.dp)
            .padding(0.dp)
            .fillMaxWidth()
    )
    return text
}

@Composable
fun standardPasswordOutlinedTextInput(placeholder: String, offset: Int): String{
    var password by rememberSaveable{ mutableStateOf("") }
    var passwordVisible by rememberSaveable{ mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            focusedBorderColor = MaterialTheme.colors.onPrimary,
            unfocusedBorderColor = MaterialTheme.colors.onPrimary,
            textColor = MaterialTheme.colors.onPrimary
        ),
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        textStyle = MaterialTheme.typography.h3,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(0.dp)
                    .offset(x = offset.dp)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, description)
            }
        },
        modifier = Modifier
            .size(211.dp, 50.dp)
            .padding(0.dp)
    )
    return password
}

@Composable
fun EunoiaLogo(width: Dp, height: Dp, xOffset: Int, yOffset: Int){
    Image(
        painter = painterResource(id = R.drawable.eunoia_1),
        contentDescription = stringResource(id = R.string.logo_text),
        modifier = Modifier
            .size(width = width, height = height)
            .offset(xOffset.dp, yOffset.dp)
    )
}

@Composable
fun EunoiaStar(width: Dp, height: Dp, xOffset: Int, yOffset: Int){
    Image(
        painter = painterResource(id = R.drawable.star_1),
        contentDescription = "Settings",
        modifier = Modifier
            .size(width = width, height = height)
            .offset(xOffset.dp, yOffset.dp)
    )
}

@Composable
fun AnImage(id: Int, contentDescription: String, width: Dp, height: Dp, xOffset: Int, yOffset: Int, lambda: () -> Unit){
    Image(
        painter = painterResource(id = id),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(width = width, height = height)
            .offset(xOffset.dp, yOffset.dp)
            .clickable { lambda() }
    )
}

@Composable
fun ProfilePictureDefault(width: Dp, height: Dp, xOffset: Int, yOffset: Int, stroke: Double, lambda: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.lifesavers_avatar),
        contentDescription = stringResource(id = R.string.profile_picture_text),
        modifier = Modifier
            .size(width = width, height = height)
            .offset(xOffset.dp, yOffset.dp)
            .border(
                BorderStroke(stroke.dp, MaterialTheme.colors.primary),
                RoundedCornerShape(139.dp)
            )
            .clickable { lambda() }
    )
}

@Composable
fun ProfilePictureEdited(width: Dp, height: Dp, bitmap: ImageBitmap, xOffset: Int, yOffset: Int, lambda: () -> Unit){
    Image(
        bitmap = bitmap,
        contentDescription = stringResource(id = R.string.profile_picture_text),
        modifier = Modifier
            .size(width = width, height = height)
            .fillMaxSize()
            .offset(xOffset.dp, yOffset.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colors.primary), RoundedCornerShape(139.dp))
            .clickable { lambda() }
    )
}

@Composable
fun StarSurroundedText(text: String){
    ConstraintLayout(
    ) {
        val (
            left_star,
            center_text,
            right_star
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(left_star) {
                    start.linkTo(parent.start, margin = 0.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            EunoiaStar(
                10.dp,
                10.dp,
                0,
                0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(right_star) {
                    end.linkTo(parent.end, margin = 0.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            EunoiaStar(
                10.dp,
                10.dp,
                0,
                0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(center_text) {
                    start.linkTo(left_star.end, margin = 4.dp)
                    end.linkTo(right_star.start, margin = 4.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            NormalText(
                text = text,
                color = MaterialTheme.colors.primary,
                fontSize = 18,
                xOffset = 0,
                yOffset = 0
            )
        }
    }
}

@Composable
fun StarSurroundedTextWithIconRight(text: String, drawable: Int, drawableWidth: Double, drawableHeight: Double){
    ConstraintLayout(
    ) {
        val (
            left_star,
            center_text,
            right_star,
            icon
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(left_star) {
                    start.linkTo(parent.start, margin = 0.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            EunoiaStar(
                10.dp,
                10.dp,
                0,
                0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(right_star) {
                    end.linkTo(parent.end, margin = 0.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            EunoiaStar(
                10.dp,
                10.dp,
                0,
                0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(center_text) {
                    start.linkTo(left_star.end, margin = 16.dp)
                    end.linkTo(right_star.start, margin = 16.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            NormalText(
                text = text,
                color = MaterialTheme.colors.primary,
                fontSize = 18,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(icon) {
                    start.linkTo(right_star.end, margin = 0.dp)
                    top.linkTo(right_star.top, margin = 0.dp)
                    bottom.linkTo(right_star.bottom, margin = 0.dp)
                }
        ) {
            AnImage(
                id = drawable,
                contentDescription = "article icon",
                width = drawableWidth.dp,
                height = drawableHeight.dp,
                xOffset = 0,
                yOffset = 0
            ){}
        }
    }
}

@Composable
fun StarSurroundedTextWithIconLeft(text: String, drawable: Int, drawableWidth: Double, drawableHeight: Double){
    ConstraintLayout(
    ) {
        val (
            left_star,
            center_text,
            right_star,
            icon
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(left_star) {
                    start.linkTo(parent.start, margin = 0.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            EunoiaStar(
                10.dp,
                10.dp,
                0,
                0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(right_star) {
                    end.linkTo(parent.end, margin = 0.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            EunoiaStar(
                10.dp,
                10.dp,
                0,
                0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(center_text) {
                    start.linkTo(left_star.end, margin = 16.dp)
                    end.linkTo(right_star.start, margin = 16.dp)
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            NormalText(
                text = text,
                color = MaterialTheme.colors.primary,
                fontSize = 18,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(icon) {
                    end.linkTo(left_star.start, margin = 0.dp)
                    top.linkTo(left_star.top, margin = 0.dp)
                    bottom.linkTo(left_star.bottom, margin = 0.dp)
                }
        ) {
            AnImage(
                id = drawable,
                contentDescription = "article icon",
                width = drawableWidth.dp,
                height = drawableHeight.dp,
                xOffset = 0,
                yOffset = 0
            ){}
        }
    }
}

@Composable
fun BackArrowHeader(backArrowClicked: () -> Unit, settingsButtonClicked: () -> Unit){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ){
        AnImage(
            id = R.drawable.back_arrow,
            contentDescription = "Back arrow",
            width = 26.5.dp,
            height = 14.5.dp,
            xOffset = 0,
            yOffset = 18
        ){
            backArrowClicked()
        }
        EunoiaLogo(
            dimensionResource(id = R.dimen.logo_top_width_dimen),
            dimensionResource(id = R.dimen.logo_top_height_dimen),
            0,
            6
        )
        AnImage(
            id = R.drawable.star_1,
            contentDescription = "Settings",
            32.86.dp,
            32.86.dp,
            xOffset = 0,
            yOffset = 9
        ){
            settingsButtonClicked()
        }
    }
}

@Composable
fun ProfilePictureHeader(profilePictureClicked: () -> Unit, settingsButtonClicked: () -> Unit){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ){
        ProfilePictureDefault(
            dimensionResource(id = R.dimen.small_profile_picture_width_dimen_big),
            dimensionResource(id = R.dimen.small_profile_picture_height_dimen_big),
            0,
            1,
            0.5
        ){profilePictureClicked()}
        EunoiaLogo(
            dimensionResource(id = R.dimen.logo_top_width_dimen),
            dimensionResource(id = R.dimen.logo_top_height_dimen),
            0,
            6
        )
        AnImage(
            id = R.drawable.star_1,
            contentDescription = "Settings",
            32.86.dp,
            32.86.dp,
            xOffset = 0,
            yOffset = 9
        ){
            settingsButtonClicked()
        }
    }
}

@Composable
fun LogoAndStarHeader(settingsButtonClicked: () -> Unit){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ){
        Column(modifier = Modifier.width(28.dp)){}
        EunoiaLogo(
            dimensionResource(id = R.dimen.logo_top_width_dimen),
            dimensionResource(id = R.dimen.logo_top_height_dimen),
            0,
            6
        )
        Column(
            modifier = Modifier.rotate(45F)
        ){
            AnImage(
                id = R.drawable.star_1,
                contentDescription = "Settings",
                32.86.dp,
                32.86.dp,
                xOffset = 0,
                yOffset = 9
            ){
                settingsButtonClicked()
            }
        }
    }
}

@Composable
fun EmptyRoutine(lambda: () -> Unit){
    Card(
        modifier = Modifier
            .height(height = 179.dp)
            .padding(bottom = 16.dp)
            .clickable { lambda() }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = emptyRoutineBackground,
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                templates,
                build,
                image
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "Your routine is empty.",
                    color = Black,
                    fontSize = 16,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(templates) {
                        top.linkTo(title.bottom, margin = 6.dp)
                    }
            ) {
                ClickableBoldText(
                    text = "Check out these templates for your routine.",
                    color = Blue,
                    fontSize = 10,
                    xOffset = 0,
                    yOffset = 0
                ){lambda()}
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .constrainAs(build) {
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    },
                contentAlignment = Alignment.Center
            ){
                MorgeNormalText(
                    text = "build",
                    color = Color.White,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(image) {
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        top.linkTo(parent.top, 0.dp)
                        start.linkTo(templates.end, margin = 0.dp)
                    }
            ) {
                AnImage(
                    R.drawable.hands_point,
                    "hands point",
                    154.dp,
                    150.dp,
                    0,
                    16
                ){}
            }
        }
    }
}

@Composable
fun SurpriseMeRoutine(lambda: () -> Unit){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(height = 163.dp)
            .clickable { lambda() }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = surpriseMeRoutineBackground,
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                tryout_text,
                shuffle,
                image
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "[surprise me with something different]",
                    color = Black,
                    fontSize = 14,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(tryout_text) {
                        top.linkTo(title.bottom, margin = 2.dp)
                    }
            ) {
                ExtraLightText(
                    text = "Try out new activities and routines by other users.",
                    color = Black,
                    fontSize = 10,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .constrainAs(shuffle) {
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    },
                contentAlignment = Alignment.Center
            ){
                MorgeNormalText(
                    text = "shuffle",
                    color = Color.White,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(image) {
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        top.linkTo(tryout_text.bottom, 2.dp)
                    }
            ) {
                AnImage(
                    R.drawable.miroodles_sticker,
                    "Miroodles Sticker",
                    97.dp,
                    104.dp,
                    0,
                    0
                ){}
            }
        }
    }
}

@Composable
fun Article(title: String, summary: String, icon: Int, lambda: () -> Unit){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(height = 163.dp)
            .clickable { lambda() }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = ArticleBackground,
        elevation = 8.dp
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title_text,
                desc,
                shuffle,
                image
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title_text) {
                        top.linkTo(parent.top, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "[$title]",
                    color = Black,
                    fontSize = 14,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(desc) {
                        top.linkTo(title_text.bottom, margin = 2.dp)
                    }
            ) {
                ExtraLightText(
                    text = summary,
                    color = Black,
                    fontSize = 10,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .constrainAs(shuffle) {
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    },
                contentAlignment = Alignment.Center
            ) {
                MorgeNormalText(
                    text = "read",
                    color = Color.White,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(image) {
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        top.linkTo(desc.bottom, 2.dp)
                    }
            ) {
                AnImage(
                    icon,
                    "$title icon",
                    97.dp,
                    104.dp,
                    0,
                    0
                ) {}
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Light mode"
)
@Preview(
    showBackground = true,
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun Preview() {
    EUNOIATheme {
        standardCentralizedOutlinedTextInput("pouring rain", MixerBackground2)
        //StarSurroundedTextWithIconRight("Beauty Sleep", R.drawable.beauty_sleep_icon, 98.0, 87.62)
    }
}