package com.example.eunoia.ui.components

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.R
import com.example.eunoia.dashboard.bedtimeStory.bedtimeStoryActivityPlayButtonTexts
import com.example.eunoia.dashboard.home.routineActivityPlayButtonTexts
import com.example.eunoia.dashboard.sound.soundActivityPlayButtonTexts
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.formatMilliSecond

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
            .sizeIn(
                minWidth = dimensionResource(id = R.dimen.sign_in_out_width_dimen),
                minHeight = dimensionResource(id = R.dimen.sign_in_out_height_dimen)
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
        colors = ButtonDefaults.buttonColors(backgroundColor = OldLace),
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
fun CustomizableButton(
    text: String,
    height: Int,
    fontSize: Int,
    textColor: Color,
    backgroundColor: Color,
    corner: Int,
    borderStroke: Double,
    borderColor: Color,
    textType: String,
    maxWidthFraction: Float,
    clicked: () -> Unit
){
    Button(
        onClick = {
            clicked()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(corner),
        border = BorderStroke(borderStroke.dp, borderColor),
        modifier = Modifier
            .height(height = height.dp)
            .fillMaxWidth(maxWidthFraction)
    ) {
        when(textType) {
            "normal" -> NormalText(
                text,
                textColor,
                fontSize,
                0,
                0
            )
            "light" -> LightText(
                text,
                textColor,
                fontSize,
                0,
                0
            )
            "morge" -> MorgeNormalText(
                text,
                textColor,
                fontSize,
                0,
                0
            )
            else -> LightText(
                text,
                textColor,
                fontSize,
                0,
                0
            )
        }
    }
}

@Composable
fun CustomizableFloatingActionButton(
    text: String,
    height: Int,
    fontSize: Int,
    textColor: Color,
    backgroundColor: Color,
    corner: Int,
    textType: String,
    maxWidthFraction: Float,
    clicked: () -> Unit
){
    ExtendedFloatingActionButton(
        onClick = {
            clicked()
        },
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(corner),
        contentColor = textColor,
        modifier = Modifier
            .height(height = height.dp)
            .fillMaxWidth(maxWidthFraction),
        text = {
            when(textType) {
                "normal" -> NormalText(
                    text,
                    textColor,
                    fontSize,
                    0,
                    0
                )
                "light" -> LightText(
                    text,
                    textColor,
                    fontSize,
                    0,
                    0
                )
                "morge" -> MorgeNormalText(
                    text,
                    textColor,
                    fontSize,
                    0,
                    0
                )
                else -> LightText(
                    text,
                    textColor,
                    fontSize,
                    0,
                    0
                )
            }
        }
    )
}

@Composable
fun CustomizableLRButton(
    text: String,
    height: Int,
    fontSize: Int,
    textColor: Color,
    backgroundColor: Color,
    corner: Int,
    borderStroke: Double,
    borderColor: Color,
    textType: String,
    maxWidthFraction: Float,
    icon: Int,
    iconWidth: Int,
    iconHeight: Int,
    iconColor: Color,
    clicked: () -> Unit
){
    Button(
        onClick = {
            clicked()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(corner),
        border = BorderStroke(borderStroke.dp, borderColor),
        modifier = Modifier
            .height(height = height.dp)
            .fillMaxWidth(maxWidthFraction)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 0.dp)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            when (textType) {
                "normal" -> NormalText(
                    text,
                    textColor,
                    fontSize,
                    0,
                    0
                )
                "light" -> LightText(
                    text,
                    textColor,
                    fontSize,
                    0,
                    0
                )
                "morge" -> MorgeNormalText(
                    text,
                    textColor,
                    fontSize,
                    0,
                    0
                )
                else -> LightText(
                    text,
                    textColor,
                    fontSize,
                    0,
                    0
                )
            }
            AnImageWithColor(
                icon,
                "icon",
                iconColor,
                iconWidth.dp,
                iconHeight.dp,
                0,
                0
            ) {

            }
        }
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
            .sizeIn(width.dp, height.dp)
            .padding(0.dp)
            .testTag(placeholder)
    )
    return text
}

@Composable
fun standardOutlinedTextInputMax30(width: Int, height: Int, placeholder: String, offset: Int): String{
    var text by rememberSaveable{ mutableStateOf("") }
    val maxLength = 50
    val context = LocalContext.current
    OutlinedTextField(
        value = text,
        onValueChange = {
            if (it.length <= maxLength) text = it
            else Toast.makeText(context, "Name cannot be more than $maxLength characters", Toast.LENGTH_SHORT).show()
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
fun customizedOutlinedTextInput(
    maxLength: Int,
    height: Int,
    color: Color,
    focusedBorderColor: Color,
    unfocusedBorderColor: Color,
    inputFontSize: Int,
    placeholder: String,
    placeholderColor: Color,
    placeholderFontSize: Int,
    offset: Int
): String{
    var text by rememberSaveable{ mutableStateOf("") }
    val context = LocalContext.current
    OutlinedTextField(
        value = text,
        onValueChange = {
            if (it.length <= maxLength) text = it
            else Toast.makeText(context, "Only $maxLength characters allowed", Toast.LENGTH_SHORT).show()
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = color,
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            textColor = Black
        ),
        singleLine = true,
        textStyle = TextStyle(
            textAlign = TextAlign.Start,
            fontFamily = bioRhymeFonts,
            fontWeight = FontWeight.Light,
            fontSize = inputFontSize.sp
        ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.h4,
                color = placeholderColor,
                fontSize = placeholderFontSize.sp,
                modifier = Modifier
                    .padding(0.dp)
                    .offset(x = offset.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .padding(0.dp)
    )
    return text
}

@Composable
fun standardCentralizedOutlinedTextInput(placeholder: String, color: Color, readOnly: Boolean): String{
    var text by rememberSaveable{ mutableStateOf(placeholder) }
    val context = LocalContext.current
    val maxLength = 30
    OutlinedTextField(
        value = text,
        onValueChange = {
            if (it.length <= maxLength) text = it
            else Toast.makeText(context, "Name cannot be more than $maxLength characters", Toast.LENGTH_SHORT).show()
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
        readOnly = readOnly,
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
    placeholderTextSize: Int,
    completed: (comment: String) -> Unit
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
        textStyle = MaterialTheme.typography.h4,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
        placeholder = {
            Column(verticalArrangement = Arrangement.Top){
                LightText(
                    placeholder,
                    placeholderColor,
                    placeholderTextSize,
                    0,
                    0
                )
            }
        },
        trailingIcon = {
            val image = Icons.Filled.Send
            val description = "Create Comment"
            IconButton(
                onClick = {
                    completed(text)
                    text = ""
                }
            ){
                Icon(imageVector  = image, description)
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
fun customizableBigOutlinedTextInput(
    maxLength: Int,
    height: Int,
    placeholder: String,
    backgroundColor: Color,
    focusedBorderColor: Color,
    unfocusedBorderColor: Color,
    textColor: Color,
    placeholderColor: Color,
    placeholderTextSize: Int,
    inputFontSize: Int,
    completed: (comment: String) -> Unit
): String{
    var text by rememberSaveable{ mutableStateOf("") }
    val context = LocalContext.current
    OutlinedTextField(
        value = text,
        onValueChange = {
            if (it.length <= maxLength) text = it
            else Toast.makeText(context, "Only $maxLength characters allowed", Toast.LENGTH_SHORT).show()
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = backgroundColor,
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            textColor = textColor
        ),
        textStyle = TextStyle(
            textAlign = TextAlign.Start,
            fontFamily = bioRhymeFonts,
            fontWeight = FontWeight.Light,
            fontSize = inputFontSize.sp
        ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
        placeholder = {
            Column(verticalArrangement = Arrangement.Top){
                LightText(
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
            .sizeIn(211.dp, 50.dp)
            .padding(0.dp)
    )
    return password
}

@Composable
fun EunoiaLogo(width: Dp, height: Dp, xOffset: Int, yOffset: Int, clicked: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.eunoia_1),
        contentDescription = stringResource(id = R.string.logo_text),
        modifier = Modifier
            .size(width = width, height = height)
            .offset(xOffset.dp, yOffset.dp)
            .clickable { clicked() }
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
fun AnImageWithColor(
    id: Int,
    contentDescription: String,
    color: Color,
    width: Dp,
    height: Dp,
    xOffset: Int,
    yOffset: Int,
    lambda: () -> Unit
){
    Image(
        painter = painterResource(id = id),
        contentDescription = contentDescription,
        colorFilter = ColorFilter.tint(color),
        modifier = Modifier
            .size(width = width, height = height)
            .offset(xOffset.dp, yOffset.dp)
            .clickable { lambda() }
    )
}

@Composable
fun AnImage(
    id: Int,
    contentDescription: String,
    width: Double,
    height: Double,
    xOffset: Int,
    yOffset: Int,
    context: Context,
    lambda: () -> Unit
){
    val image = remember {
        ContextCompat.getDrawable(context, id)?.toBitmap()?.asImageBitmap()!!
    }
    Image(
        image,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(width.dp, height.dp)
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
                width = drawableWidth,
                height = drawableHeight,
                xOffset = 0,
                yOffset = 0,
                LocalContext.current
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
                width = drawableWidth,
                height = drawableHeight,
                xOffset = 0,
                yOffset = 0,
                LocalContext.current
            ){}
        }
    }
}

@Composable
fun BackArrowHeader(backArrowClicked: () -> Unit, eunoiaLogoClicked: () -> Unit, settingsButtonClicked: () -> Unit){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ){
        AnImageWithColor(
            id = R.drawable.back_arrow,
            contentDescription = "Back arrow",
            Black,
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
        ){
            eunoiaLogoClicked()
        }
        AnImageWithColor(
            id = R.drawable.star_1,
            contentDescription = "Settings",
            Black,
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
fun ProfilePictureHeader(profilePictureClicked: () -> Unit, eunoiaLogoClicked: () -> Unit, settingsButtonClicked: () -> Unit){
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
        ){
            eunoiaLogoClicked()
        }
        AnImageWithColor(
            id = R.drawable.star_1,
            contentDescription = "Settings",
            Black,
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
fun LogoAndStarHeader(eunoiaLogoClicked: () -> Unit, settingsButtonClicked: () -> Unit){
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
        ){
            eunoiaLogoClicked()
        }
        Column(
            modifier = Modifier.rotate(45F)
        ){
            AnImageWithColor(
                id = R.drawable.star_1,
                contentDescription = "Settings",
                Black,
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
        backgroundColor = PeriwinkleGray,
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
                    154.0,
                    150.0,
                    0,
                    16,
                    LocalContext.current
                ){}
            }
        }
    }
}

@Composable
fun UserRoutineRelationshipCard(
    userRoutineRelationship: UserRoutineRelationship,
    index: Int,
    startClicked: (index: Int) -> Unit,
    clicked: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(height = 163.dp)
            .clickable { clicked() }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = Color(userRoutineRelationship.userRoutineRelationshipRoutine.colorHex),
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                times_used,
                steps,
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
                    text = "[${userRoutineRelationship.userRoutineRelationshipRoutine.displayName}]",
                    color = Black,
                    fontSize = 14,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(times_used) {
                        top.linkTo(title.bottom, margin = 2.dp)
                    }
            ) {
                val timesUsedText = if(userRoutineRelationship.numberOfTimesPlayed == 1) "time" else "times"
                ExtraLightText(
                    text = "You have used this routine ${userRoutineRelationship.numberOfTimesPlayed} $timesUsedText.",
                    color = Black,
                    fontSize = 10,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(steps) {
                        top.linkTo(times_used.bottom, margin = 2.dp)
                    }
            ) {
                var step = "0 steps"
                var playTimeString = "0:00"
                var finalText = "$step ~ $playTimeString"

                if(userRoutineRelationship.numberOfSteps != null){
                    step = if(userRoutineRelationship.numberOfSteps > 1) "steps" else "step"
                    playTimeString = formatMilliSecond(userRoutineRelationship.fullPlayTime.toLong())
                    finalText = "${userRoutineRelationship.numberOfSteps} $step ~ $playTimeString"
                }
                LightText(
                    text = finalText,
                    color = Grey,
                    fontSize = 7,
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
                    }
                    .clickable {
                        startClicked(index)
                    },
                contentAlignment = Alignment.Center
            ){
                MorgeNormalText(
                    text = routineActivityPlayButtonTexts[index]!!.value,
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
                        top.linkTo(times_used.bottom, 2.dp)
                    }
            ) {
                AnImage(
                    userRoutineRelationship.userRoutineRelationshipRoutine.icon,
                    "${userRoutineRelationship.userRoutineRelationshipRoutine.displayName} icon",
                    97.0,
                    104.0,
                    0,
                    0,
                    LocalContext.current
                ){}
            }
        }
    }
}

@Composable
fun SoundCard(
    sound: SoundData,
    index: Int,
    startClicked: (index: Int) -> Unit,
    clicked: (index: Int) -> Unit
){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(height = 163.dp)
            .clickable { clicked(index) }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = Color(sound.colorHex),
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                times_used,
                steps,
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
                    text = "[${sound.displayName}]",
                    color = Black,
                    fontSize = 14,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(times_used) {
                        top.linkTo(title.bottom, margin = 2.dp)
                    }
            ) {
                ExtraLightText(
                    text = sound.shortDescription,
                    color = Black,
                    fontSize = 10,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(steps) {
                        top.linkTo(times_used.bottom, margin = 2.dp)
                    }
            ) {
                val playTimeString = formatMilliSecond(sound.fullPlayTime.toLong())
                LightText(
                    text = playTimeString,
                    color = Grey,
                    fontSize = 7,
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
                    }
                    .clickable {
                        startClicked(index)
                    },
                contentAlignment = Alignment.Center
            ){
                MorgeNormalText(
                    text = soundActivityPlayButtonTexts[index]!!.value,
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
                        top.linkTo(times_used.bottom, 2.dp)
                    }
            ) {
                AnImage(
                    sound.icon,
                    "${sound.displayName} icon",
                    97.0,
                    104.0,
                    0,
                    0,
                    LocalContext.current
                ){}
            }
        }
    }
}

@Composable
fun DisplayUsersBedtimeStories(
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    index: Int,
    startClicked: (index: Int) -> Unit,
    clicked: (index: Int) -> Unit
){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(height = 163.dp)
            .clickable { clicked(index) }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = GoldSand,
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                times_used,
                steps,
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
                    text = "[${bedtimeStoryInfoData.displayName}]",
                    color = Black,
                    fontSize = 14,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(times_used) {
                        top.linkTo(title.bottom, margin = 2.dp)
                    }
            ) {
                var shortDescription = "Short description"
                if(bedtimeStoryInfoData.shortDescription != null){
                    shortDescription = bedtimeStoryInfoData.shortDescription
                }

                ExtraLightText(
                    text = shortDescription,
                    color = Black,
                    fontSize = 10,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(steps) {
                        top.linkTo(times_used.bottom, margin = 2.dp)
                    }
            ) {
                val playTimeString = formatMilliSecond(bedtimeStoryInfoData.fullPlayTime.toLong())
                LightText(
                    text = playTimeString,
                    color = Grey,
                    fontSize = 7,
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
                    }
                    .clickable {
                        startClicked(index)
                    },
                contentAlignment = Alignment.Center
            ){
                MorgeNormalText(
                    text = bedtimeStoryActivityPlayButtonTexts[index]!!.value,
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
                        top.linkTo(times_used.bottom, 2.dp)
                    }
            ) {
                val icon = if(bedtimeStoryInfoData.icon == null) R.drawable.danger_of_sleeping_pills_icon
                else bedtimeStoryInfoData.icon
                AnImage(
                    icon,
                    "${bedtimeStoryInfoData.displayName} icon",
                    97.0,
                    104.0,
                    0,
                    0,
                    LocalContext.current
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
        backgroundColor = SwansDown,
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
                    97.0,
                    104.0,
                    0,
                    0,
                    LocalContext.current
                ){}
            }
        }
    }
}

@Composable
fun SurpriseMeSound(lambda: () -> Unit){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(height = 163.dp)
            .clickable { lambda() }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = SwansDown,
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
                    text = "Try out new sounds by other users.",
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
                    97.0,
                    104.0,
                    0,
                    0,
                    LocalContext.current
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
        backgroundColor = Pampas,
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
                    97.0,
                    104.0,
                    0,
                    0,
                    LocalContext.current
                ) {}
            }
        }
    }
}

@Composable
fun dropdownMenuSoftPeach(list: List<String>, title: String): String{
    var expanded by remember { mutableStateOf(false)}
    var selectedIndex by remember { mutableStateOf(-1) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (
            select,
            dropdown,
        ) = createRefs()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { expanded = true }
                )
                .constrainAs(select) {
                    top.linkTo(parent.top, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                },
            shape = MaterialTheme.shapes.small,
        ){
            Column(
                modifier = Modifier
                    .background(SoftPeach)
            ){
                if(selectedIndex > -1){
                    Row(
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp,
                                horizontal = 12.dp,
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        LightText(
                            text = list[selectedIndex],
                            color = Black,
                            fontSize = 15,
                            xOffset = 0,
                            yOffset = 0
                        )
                        AnImage(
                            R.drawable.dropdown_icon,
                            "dropdown icon",
                            14.0,
                            8.0,
                            0,
                            0,
                            LocalContext.current
                        ) {expanded = true }
                    }
                }else{
                    Row(
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp,
                                horizontal = 12.dp,
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        LightText(
                            title,
                            color = BeautyBush,
                            fontSize = 15,
                            xOffset = 0,
                            yOffset = 0
                        )
                        AnImage(
                            R.drawable.dropdown_icon,
                            "dropdown icon",
                            14.0,
                            8.0,
                            0,
                            0,
                            LocalContext.current
                        ) {expanded = true }
                    }
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(SoftPeach)
                .constrainAs(dropdown) {
                    top.linkTo(select.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .wrapContentHeight(),
        ) {
            list.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                    }
                ) {
                    LightText(
                        text = s,
                        color = Black,
                        fontSize = 15,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
        }
    }
    if(selectedIndex > -1) {
        return list[selectedIndex]
    }
    return "0"
}

@Composable
fun PurpleBackgroundInfo(
    titleString: String,
    info: String,
    lambda: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 2.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PeriwinkleGray.copy(alpha = 0.3F),
                            Color(0xFFCBCBE8).copy(alpha = 0.4F),
                            Mischka.copy(alpha = 0.6F),
                        ),
                        center = Offset.Unspecified,
                        radius = 300f,
                        tileMode = TileMode.Clamp
                    )
                )
                .padding(16.dp, 8.dp, 16.dp, 16.dp)
        ) {
            val (
                title,
                information,
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = titleString,
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(information) {
                        top.linkTo(title.bottom, margin = 8.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = info,
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
        }
    }
}

@Composable
fun PurpleBackgroundStart(
    titleString: String,
    secondaryTitle: String,
    lambda: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 0.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PeriwinkleGray.copy(alpha = 0.3F),
                            Color(0xFFCBCBE8).copy(alpha = 0.4F),
                            Mischka.copy(alpha = 0.6F),
                        ),
                        center = Offset.Unspecified,
                        radius = 300f,
                        tileMode = TileMode.Clamp
                    )
                )
        ) {
            val (
                title,
                secondary,
                button
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(button.start, margin = 32.dp)
                    }
            ) {
                NormalText(
                    text = titleString,
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(secondary) {
                        top.linkTo(title.bottom, margin = 0.dp)
                        end.linkTo(title.end, margin = 0.dp)
                        start.linkTo(title.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = secondaryTitle,
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Box(
                modifier = Modifier
                    .size(77.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .constrainAs(button) {
                        top.linkTo(parent.top, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        end.linkTo(parent.end, margin = 48.dp)
                    }
                    .clickable {
                        //startClicked(index)
                    },
                contentAlignment = Alignment.Center
            ){
                MorgeNormalText(
                    text = "start",
                    color = Color.White,
                    fontSize = 20,
                    xOffset = 0,
                    yOffset = 0
                )
            }
        }
    }
}

@Composable
fun WrappedPurpleBackgroundStart(
    titleString: String,
    secondaryTitle: String,
    lambda: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 0.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PeriwinkleGray.copy(alpha = 0.3F),
                            Color(0xFFCBCBE8).copy(alpha = 0.4F),
                            Mischka.copy(alpha = 0.6F),
                        ),
                        center = Offset.Unspecified,
                        radius = 300f,
                        tileMode = TileMode.Clamp
                    )
                )
        ) {
            val (
                title,
                secondary,
                button
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 32.dp)
                        //end.linkTo(button.start, margin = 32.dp)
                    }
            ) {
                var textSize = titleString.length
                var i = 0
                val maxLength = 25
                while(textSize > 0) {
                    var limit = i+maxLength+1
                    if(limit > titleString.length){
                        limit = titleString.length
                    }
                    if(titleString.substring(i, limit).isNotEmpty()) {
                        NormalText(
                            text = titleString.substring(i, limit),
                            color = Black,
                            fontSize = 12,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                    i += limit
                    if(i > titleString.length){
                        i = titleString.length
                    }
                    textSize -= maxLength
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(secondary) {
                        top.linkTo(title.bottom, margin = 0.dp)
                        start.linkTo(title.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = secondaryTitle,
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Box(
                modifier = Modifier
                    .size(77.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .constrainAs(button) {
                        top.linkTo(parent.top, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        end.linkTo(parent.end, margin = 48.dp)
                    }
                    .clickable {
                        //startClicked(index)
                    },
                contentAlignment = Alignment.Center
            ){
                MorgeNormalText(
                    text = "start",
                    color = Color.White,
                    fontSize = 20,
                    xOffset = 0,
                    yOffset = 0
                )
            }
        }
    }
}

@Composable
fun Or(){
    ConstraintLayout {
        val (
            line_1,
            text,
            line_2
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(line_1){
                    top.linkTo(text.top, margin = 0.dp)
                    end.linkTo(text.start, margin = 3.dp)
                    bottom.linkTo(text.bottom, margin = 0.dp)
                }
        ) {
            Canvas(modifier = Modifier){
                val canvasWidth = size.width
                drawLine(
                    start = Offset(x = canvasWidth, y = 0f),
                    end = Offset(x = canvasWidth - 140f, y = 0f),
                    color = Black,
                    strokeWidth = 1F
                )
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(text){
                    top.linkTo(parent.top, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            NormalText(
                text = "or",
                color = Black,
                fontSize = 12,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(line_2){
                    top.linkTo(text.top, margin = 0.dp)
                    start.linkTo(text.end, margin = 3.dp)
                    bottom.linkTo(text.bottom, margin = 0.dp)
                }
        ) {
            Canvas(modifier = Modifier){
                drawLine(
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = 140f, y = 0f),
                    color = Black,
                    strokeWidth = 1F
                )
            }
        }
    }
}

@Composable
fun SimpleFlowRow(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    verticalGap: Dp = 0.dp,
    horizontalGap: Dp = 0.dp,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->
    val hGapPx = horizontalGap.roundToPx()
    val vGapPx = verticalGap.roundToPx()
    val rows = mutableListOf<MeasuredRow>()
    val itemConstraints = constraints.copy(minWidth = 0)

    for (measurable in measurables) {
        val lastRow = rows.lastOrNull()
        val placeable = measurable.measure(itemConstraints)
        if (lastRow != null && lastRow.width + hGapPx + placeable.width <= constraints.maxWidth) {
            lastRow.items.add(placeable)
            lastRow.width += hGapPx + placeable.width
            lastRow.height = kotlin.math.max(lastRow.height, placeable.height)
        } else {
            val nextRow = MeasuredRow(
                items = mutableListOf(placeable),
                width = placeable.width,
                height = placeable.height
            )
            rows.add(nextRow)
        }
    }

    val width = rows.maxOfOrNull { row -> row.width } ?: 0
    val height = rows.sumBy { row -> row.height } + kotlin.math.max(vGapPx.times(rows.size - 1), 0)
    val coercedWidth = width.coerceIn(constraints.minWidth, constraints.maxWidth)
    val coercedHeight = height.coerceIn(constraints.minHeight, constraints.maxHeight)

    layout(coercedWidth, coercedHeight) {
        var y = 0
        for (row in rows) {
            var x = when(alignment) {
                Alignment.Start -> 0
                Alignment.CenterHorizontally -> (coercedWidth - row.width) / 2
                Alignment.End -> coercedWidth - row.width
                else -> throw Exception("unsupported alignment")
            }
            for (item in row.items) {
                item.place(x, y)
                x += item.width + hGapPx
            }
            y += row.height + vGapPx
        }
    }
}

private data class MeasuredRow(
    val items: MutableList<Placeable>,
    var width: Int,
    var height: Int
)

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
        CustomizableLRButton(
            text = "Page 1",
            height = 55,
            fontSize = 16,
            textColor = Black,
            backgroundColor = WePeep,
            corner = 10,
            borderStroke = 0.0,
            borderColor = Black.copy(alpha = 0F),
            textType = "light",
            maxWidthFraction = 1F,
            R.drawable.little_right_arrow,
            8,
            25,
            BeautyBush
        ) {

        }
    }
}