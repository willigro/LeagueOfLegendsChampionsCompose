package com.rittmann.leagueoflegendschamps.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.rittmann.leagueoflegendschamps.R
import com.rittmann.leagueoflegendschamps.data.local.LocalChampionInfo
import com.rittmann.leagueoflegendschamps.data.model.ResumedChampion
import com.rittmann.leagueoflegendschamps.data.network.ImageUrls
import com.rittmann.leagueoflegendschamps.screens.comum.AvatarImageLoading
import com.rittmann.leagueoflegendschamps.screens.comum.Border
import com.rittmann.leagueoflegendschamps.screens.comum.BorderAnim
import com.rittmann.leagueoflegendschamps.screens.comum.ConnectionErrorScreen
import com.rittmann.leagueoflegendschamps.screens.comum.HorizontalDivisor
import com.rittmann.leagueoflegendschamps.screens.comum.Rows
import com.rittmann.leagueoflegendschamps.screens.comum.borderAnim
import com.rittmann.leagueoflegendschamps.screens.comum.borderBySides
import com.rittmann.leagueoflegendschamps.themes.BorderAnimationEndColor
import com.rittmann.leagueoflegendschamps.themes.BorderAnimationInitialColor
import com.rittmann.leagueoflegendschamps.themes.ChampionAvatarBorder
import com.rittmann.leagueoflegendschamps.themes.ClickableNormalPadding
import com.rittmann.leagueoflegendschamps.themes.DefaultBorderStrokeWidth
import com.rittmann.leagueoflegendschamps.themes.LeagueOfLegendsChampionsTheme
import com.rittmann.leagueoflegendschamps.themes.LoadingBackground
import com.rittmann.leagueoflegendschamps.themes.PatternNormalPadding
import com.rittmann.leagueoflegendschamps.themes.PatternSmallPadding
import com.rittmann.leagueoflegendschamps.themes.PatternSmallPadding_X
import com.rittmann.leagueoflegendschamps.themes.PlaceholderColor
import com.rittmann.leagueoflegendschamps.themes.TextFieldIconSize
import com.rittmann.leagueoflegendschamps.themes.ToolbarHeight
import com.rittmann.leagueoflegendschamps.themes.TopTextToIconPadding
import dev.chrisbanes.accompanist.coil.CoilImage

@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    errorConnection: Boolean,
    errorConnectionRetry: () -> Unit,
    list: List<ResumedChampion>,
    onFilterTagsSelected: (List<LocalChampionInfo.TagWithColor>) -> Unit,
    onChampionNameChanged: (String) -> Unit,
    onClickSelectedChampion: (ResumedChampion) -> Unit
) {
    LeagueOfLegendsChampionsTheme {
        Scaffold(
            topBar = {
                BoxWithConstraints {
                    HomeScreenToolbar(maxWidth, onFilterTagsSelected, onChampionNameChanged)
                }
            }
        ) {
            HomeScreenContent(errorConnection, errorConnectionRetry, list, onClickSelectedChampion)
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun HomeScreenContent(
    errorConnection: Boolean,
    errorConnectionRetry: () -> Unit,
    list: List<ResumedChampion>,
    onClickSelectedChampion: (ResumedChampion) -> Unit
) {
    BoxWithConstraints {
        Surface(
            modifier = Modifier
                .padding(
                    top = PatternNormalPadding,
                    start = PatternNormalPadding,
                    end = PatternNormalPadding
                )
        ) {
            if (errorConnection)
                ConnectionErrorScreen(errorConnectionRetry)
            else {
                var yPos by remember { mutableStateOf(1.dp) }
                HomeScreenContentBorderLine(maxHeight) {
                    yPos = it
                }

                if (yPos == 0.dp)
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ResumedChampionList(
                            list = list,
                            onClickSelectedChampion = onClickSelectedChampion
                        )
                    }
            }
        }
    }
}

var borderAnimationStarttime = 600
var borderAnimationLargetime = 3000
var animationColorIsTriggered = false
var animationColorTime = borderAnimationStarttime

@Composable
fun HomeScreenContentBorderLine(maxHeight: Dp, currentBorderPositionY: (Dp) -> Unit) {
    val borderAnimationTrigger = remember { mutableStateOf(true) }
    val borderAnimationTriggerColor = remember { mutableStateOf(true) }

    val transition = updateTransition(borderAnimationTrigger)
    val transitionColor = updateTransition(borderAnimationTriggerColor)

    val yPos by transition.animateDp(
        transitionSpec = {
            tween(durationMillis = borderAnimationStarttime)
        }
    ) {
        if (it.value) maxHeight else {
            currentBorderPositionY(0.dp)
            animationColorTime = borderAnimationLargetime
            0.dp
        }
    }

    val borderColor by transitionColor.animateColor(
        transitionSpec = {
            tween(durationMillis = animationColorTime)
        }
    ) {
        if (it.value) {
            BorderAnimationInitialColor
        } else {
            BorderAnimationEndColor
        }
    }

    if (animationColorIsTriggered) {
        if (borderColor == BorderAnimationEndColor)
            borderAnimationTriggerColor.value = true
        else if (borderColor == BorderAnimationInitialColor)
            borderAnimationTriggerColor.value = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .borderAnim(
                start = BorderAnim(
                    DefaultBorderStrokeWidth,
                    borderColor,
                    yPos
                ),
                end = BorderAnim(
                    DefaultBorderStrokeWidth,
                    borderColor,
                    yPos
                )
            )
    )

    if (animationColorIsTriggered.not()) {
        animationColorIsTriggered = true
        borderAnimationTrigger.value = false
        borderAnimationTriggerColor.value = false
    }
}

enum class HomeToolbarState {
    HomeTitle,
    SearchField,
    MoreFilters
}

@Composable
fun HomeScreenToolbarTitle(
    modifier: Modifier = Modifier,
    stateChanged: (HomeToolbarState) -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (title, divisor, searchIcon) = createRefs()

        HorizontalDivisor(modifier = Modifier
            .constrainAs(divisor) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(text = stringResource(id = R.string.home_toolbar_title),
            style = typography.h6,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    bottom.linkTo(divisor.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = PatternNormalPadding)
        )

        IconButton(
            modifier = Modifier
                .size(TextFieldIconSize)
                .constrainAs(searchIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            onClick = {
                stateChanged(HomeToolbarState.SearchField)
            }
        ) {
            Icon(
                Icons.Outlined.Search,
                contentDescription = null
            )
        }
    }
}

/**
 * TODO: Try make all heights be equals
 * */
@Composable
fun HomeScreenToolbar(
    maxWidth: Dp,
    onFilterTagSelected: (List<LocalChampionInfo.TagWithColor>) -> Unit,
    onChampionNameChanged: (String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = PatternNormalPadding,
                end = PatternNormalPadding
            )
    ) {

        var currentState by remember { mutableStateOf(HomeToolbarState.HomeTitle) }

        val transition = updateTransition(currentState)
        val xPos by transition.animateDp { state ->
            when (state) {
                HomeToolbarState.HomeTitle -> 0.dp
                HomeToolbarState.SearchField -> -maxWidth
                HomeToolbarState.MoreFilters -> -maxWidth * 2
            }
        }

        Box(modifier = Modifier.offset(x = xPos)) {
            HomeScreenToolbarTitle(
                modifier = Modifier.height(ToolbarHeight),
                stateChanged = { currentState = it }
            )

            HomeScreenToolbarSearchField(
                modifier = Modifier.height(ToolbarHeight),
                position = maxWidth,
                stateChanged = { currentState = it },
                onChampionNameChanged = onChampionNameChanged
            )

            HomeScreenToolbarMoreFilters(
                modifier = Modifier.height(ToolbarHeight),
                position = maxWidth * 2,
                selectedTags = onFilterTagSelected,
                stateChanged = { currentState = it }
            )
        }
    }
}


@Composable
fun HomeScreenToolbarSearchField(
    modifier: Modifier = Modifier,
    position: Dp,
    stateChanged: (HomeToolbarState) -> Unit,
    onChampionNameChanged: (String) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = position)
    ) {
        val (searchField, backToTitle, showMoreFilters) = createRefs()

        SearchField(Modifier.constrainAs(searchField) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(backToTitle.end)
            end.linkTo(showMoreFilters.start)
        }, onChampionNameChanged)

        IconButton(
            modifier = Modifier
                .size(TextFieldIconSize)
                .constrainAs(backToTitle) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            onClick = {
                stateChanged(HomeToolbarState.HomeTitle)
            }
        ) {
            Icon(
                Icons.Outlined.ArrowBack,
                contentDescription = null
            )
        }

        IconButton(
            modifier = Modifier
                .size(TextFieldIconSize)
                .constrainAs(showMoreFilters) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            onClick = {
                stateChanged(HomeToolbarState.MoreFilters)
            }
        ) {
            Icon(
                Icons.Outlined.FilterAlt,
                contentDescription = null
            )
        }
    }
}


@Composable
fun HomeScreenToolbarMoreFilters(
    modifier: Modifier = Modifier,
    position: Dp,
    selectedTags: (tags: List<LocalChampionInfo.TagWithColor>) -> Unit,
    stateChanged: (HomeToolbarState) -> Unit
) {

    var currentSelectedTags by remember {
        mutableStateOf<ArrayList<LocalChampionInfo.TagWithColor>>(
            arrayListOf()
        )
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = position)
    ) {

        val (backToSearchField, filterTags) = createRefs()

        IconButton(
            modifier = Modifier
                .size(TextFieldIconSize)
                .constrainAs(backToSearchField) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            onClick = {
                currentSelectedTags.clear()
                selectedTags(currentSelectedTags)

                stateChanged(HomeToolbarState.SearchField)
            }
        ) {
            Icon(
                Icons.Outlined.ArrowBack,
                contentDescription = null
            )
        }

        Rows(modifier = Modifier
            .padding(top = PatternNormalPadding, start = PatternNormalPadding)
            .constrainAs(filterTags) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(backToSearchField.end)
                end.linkTo(parent.end)
            }
        ) {
            LocalChampionInfo.allAvailableTagsWithColor.forEach { tagWithColor ->

                val containsTag = currentSelectedTags.contains(tagWithColor)

                val bottom =
                    if (containsTag) null else Border(DefaultBorderStrokeWidth, tagWithColor.color)

                val animationColorLabel = "Filter color"
                val transition = updateTransition(
                    containsTag,
                    label = animationColorLabel
                )

                val color by transition.animateColor(label = animationColorLabel) {
                    if (it) tagWithColor.color else Color.Transparent
                }

                Text(
                    text = tagWithColor.tag,
                    style = typography.body2,
                    modifier = Modifier
                        .clickable {
                            val tags = arrayListOf<LocalChampionInfo.TagWithColor>()
                            tags.addAll(currentSelectedTags)
                            if (tags.contains(tagWithColor))
                                tags.remove(tagWithColor)
                            else
                                tags.add(tagWithColor)

                            currentSelectedTags = tags

                            selectedTags(currentSelectedTags)
                        }
                        .padding(PatternSmallPadding_X)
                        .borderBySides(bottom = bottom)
                        .background(color)
                        .padding(PatternSmallPadding)
                )
            }
        }
    }
}

@Composable
fun SearchField(modifier: Modifier = Modifier, valueChanged: (String) -> Unit) {
    ConstraintLayout(modifier = modifier) {
        val (input, buttonCancel) = createRefs()
        var value by remember { mutableStateOf("") }

        TextField(
            value = value,
            onValueChange = {
                value = it
                valueChanged(it)
            },
            modifier = Modifier
                .constrainAs(input) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = MaterialTheme.colors.secondary,
                placeholderColor = PlaceholderColor
            ),
            placeholder = {
                Text(text = stringResource(id = R.string.filter_by_name_hint))
            }
        )

        IconButton(
            modifier = Modifier
                .size(TextFieldIconSize)
                .constrainAs(buttonCancel) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(input.end)
                },
            onClick = {
                if (value.isNotEmpty()) {
                    value = ""
                    valueChanged("")
                }
            }
        ) {
            Icon(
                Icons.Outlined.Cancel,
                contentDescription = null
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ResumedChampionList(
    list: List<ResumedChampion>,
    onClickSelectedChampion: (ResumedChampion) -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3)
    ) {
        items(list) { champion ->
            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable { onClickSelectedChampion(champion) }
                .padding(ClickableNormalPadding)
            ) {

                val (surface, text) = createRefs()

                val tagsColors = arrayListOf<Color>()
                champion.resumedChampionTags.forEach { tag ->
                    LocalChampionInfo.allAvailableTagsWithColor.forEach { colorTag ->
                        if (tag == colorTag.tag)
                            tagsColors.add(colorTag.color)
                    }
                }

                if (tagsColors.size == 1) {
                    tagsColors.add(tagsColors[0])
                }

                val linearGradientBrush = Brush.linearGradient(
                    colors = tagsColors
                )

                val nameIsLoading = remember { mutableStateOf(true) }

                Surface(
                    modifier = Modifier
                        .constrainAs(surface) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .border(ChampionAvatarBorder, linearGradientBrush, CircleShape)
                        .padding(2.dp)
                        .wrapContentSize(),
                    shape = CircleShape,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                ) {
                    CoilImage(
                        data = ImageUrls.getChampionSmallImageUrl(champion.id),
                        contentDescription = "Picture from ${champion.id}",
                        contentScale = ContentScale.Inside,
                        loading = {
                            AvatarImageLoading()
                            nameIsLoading.value = true
                        },
                        onRequestCompleted = {
                            nameIsLoading.value = false
                        }
                    )
                }
                if (nameIsLoading.value) {
                    Box(
                        modifier = Modifier
                            .constrainAs(text) {
                                top.linkTo(surface.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(TopTextToIconPadding)
                            .fillMaxWidth()
                            .height(typography.body2.fontSize.value.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(color = LoadingBackground)
                                .fillMaxSize()
                                .alpha(ContentAlpha.disabled)
                        ) {

                        }
                    }
                } else {
                    Text(
                        text = champion.id,
                        style = typography.body2,
                        modifier = Modifier
                            .constrainAs(text) {
                                top.linkTo(surface.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(TopTextToIconPadding)
                            .alpha(ContentAlpha.high)
                    )
                }
            }
        }
    }
}