package com.rittmann.leagueoflegendschamps.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.rittmann.leagueoflegendschamps.R
import com.rittmann.leagueoflegendschamps.base.BaseActivity
import com.rittmann.leagueoflegendschamps.data.local.LocalChampionInfo
import com.rittmann.leagueoflegendschamps.data.model.Champion
import com.rittmann.leagueoflegendschamps.data.model.ChampionSkins
import com.rittmann.leagueoflegendschamps.data.model.ResumedChampionData
import com.rittmann.leagueoflegendschamps.data.model.ResumedChampionStats
import com.rittmann.leagueoflegendschamps.data.network.ImageUrls
import com.rittmann.leagueoflegendschamps.screens.comum.BorderAnimThree
import com.rittmann.leagueoflegendschamps.screens.comum.BorderAnimTwo
import com.rittmann.leagueoflegendschamps.screens.comum.ChampionImageLoading
import com.rittmann.leagueoflegendschamps.screens.comum.CircularProgressWithShadowAnimated
import com.rittmann.leagueoflegendschamps.screens.comum.DropDownListHorizontal
import com.rittmann.leagueoflegendschamps.screens.comum.FromTo
import com.rittmann.leagueoflegendschamps.screens.comum.HorizontalDivisor
import com.rittmann.leagueoflegendschamps.screens.comum.HorizontalSelector
import com.rittmann.leagueoflegendschamps.screens.comum.PositionBorderAnim
import com.rittmann.leagueoflegendschamps.screens.comum.SurfaceScreen
import com.rittmann.leagueoflegendschamps.screens.comum.borderAnim
import com.rittmann.leagueoflegendschamps.themes.BorderAnimationEndColor
import com.rittmann.leagueoflegendschamps.themes.ChampionDataAttackColor
import com.rittmann.leagueoflegendschamps.themes.ChampionDataBorderStrokeWidth
import com.rittmann.leagueoflegendschamps.themes.ChampionDataDefenceColor
import com.rittmann.leagueoflegendschamps.themes.ChampionDataDifficultColor
import com.rittmann.leagueoflegendschamps.themes.ChampionDataMagicColor
import com.rittmann.leagueoflegendschamps.themes.ChampionDetailsStatsBoxCorners
import com.rittmann.leagueoflegendschamps.themes.ChampionTagSupportColor
import com.rittmann.leagueoflegendschamps.themes.DefaultBorderStrokeWidth
import com.rittmann.leagueoflegendschamps.themes.LeagueOfLegendsChampionsTheme
import com.rittmann.leagueoflegendschamps.themes.LevelSelectorSize
import com.rittmann.leagueoflegendschamps.themes.NormalSpacer
import com.rittmann.leagueoflegendschamps.themes.PatternNormalPadding
import com.rittmann.leagueoflegendschamps.themes.PatternSmallPadding
import com.rittmann.leagueoflegendschamps.themes.PatternSmallPadding_X
import com.rittmann.leagueoflegendschamps.themes.PlaceholderColor
import com.rittmann.leagueoflegendschamps.themes.SmallSpacer
import com.rittmann.leagueoflegendschamps.themes.TabPadding
import com.rittmann.leagueoflegendschamps.themes.TextFieldIconSize
import com.rittmann.leagueoflegendschamps.themes.ToolbarHeight
import com.rittmann.leagueoflegendschamps.themes.UnselectedColor
import com.rittmann.leagueoflegendschamps.util.log
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.launch

@Composable
fun ChampionDetailsScreen(champion: Champion) {
    LeagueOfLegendsChampionsTheme {
        Scaffold(topBar = {
            ChampionDetailsScreenToolbar(champion)
        }) {
            SurfaceScreen {
                val columns = stringArrayResource(R.array.champion_details_titles_array)

                var selectedTab by remember { mutableStateOf(columns.first()) }

                Column(
                    Modifier.verticalScroll(rememberScrollState())
                ) {

                    ChampionDetailsTabLayout(columns, selectedTab) { selectedTab = it }

                    when (selectedTab) {
                        columns[0] -> {
                            ChampionDetailsMain(champion)
                        }
                        columns[1] -> {
                            ChampionDetailsLore(champion)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChampionDetailsTabLayout(
    columns: Array<String>,
    selectedTab: String,
    selectedTabCallback: (String) -> Unit
) {
    BoxWithConstraints {
        val contentPadding = TabPadding

        val selectorWidth = (maxWidth / columns.size) - contentPadding

        val selectorAnimationTrigger =
            remember { mutableStateOf(selectedTab) }
        val selectorAnimationTransition = updateTransition(selectorAnimationTrigger)

        val xPos by selectorAnimationTransition.animateDp {
            selectorWidth * columns.indexOf(it.value)
        }

        Column(modifier = Modifier.padding(contentPadding)) {
            Row {
                columns.forEach {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                selectedTabCallback(it)
                                selectorAnimationTrigger.value = it
                            }
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )

                        HorizontalDivisor(
                            color = UnselectedColor
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalSelector(
                    modifier = Modifier.offset(x = xPos),
                    color = Color.Red,
                    width = selectorWidth
                )
            }
        }
    }
}

@Composable
fun ChampionDetailsScreenToolbar(champion: Champion) {
    ConstraintLayout(
        modifier = Modifier
            .height(ToolbarHeight)
            .fillMaxWidth()
            .padding(
                start = PatternNormalPadding,
                end = PatternNormalPadding
            )
    ) {
        val (title, divisor, back) = createRefs()
        Text(text = champion.id,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    bottom.linkTo(divisor.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = PatternNormalPadding)
        )

        HorizontalDivisor(modifier = Modifier
            .constrainAs(divisor) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        val context = LocalContext.current
        IconButton(
            modifier = Modifier
                .size(TextFieldIconSize)
                .constrainAs(back) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            onClick = {
                (context as BaseActivity).finish()
            }
        ) {
            Icon(
                Icons.Outlined.ArrowBack,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ChampionDetailsMain(champion: Champion) {
    lastPage = -1
    Column {
        ChampionDetailsImageSkinsViewPager(champion)

        ChampionDetailsTags(champion.resumedChampionTags)

        ChampionDetailsData(champion.resumedChampionData)

        ChampionDetailsStats(champion.resumedChampionStats)
    }
}

@Composable
fun ChampionDetailsTags(resumedChampionTags: List<String>) {
    val tags = LocalChampionInfo.tagsToTagsWithColor(resumedChampionTags)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PatternNormalPadding)
    ) {
        tags.forEach { tag ->
            Text(
                text = tag.tag,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(PatternSmallPadding_X)
                    .background(tag.color)
                    .padding(PatternSmallPadding)
            )
        }
    }
}

@Composable
fun ChampionDetailsImageSkinsViewPager(champion: Champion) {
    BoxWithConstraints {
        val maxWidth = maxWidth
        var currentPage by remember { mutableStateOf(0) }

        Column {
            val coroutineScope = rememberCoroutineScope()

            val state: LazyListState = rememberLazyListState()
            state.AddViewPagerListener(
                count = champion.skins.size,
                updatePage = { page ->
                    currentPage = page
                },
                goTo = { page ->
                    coroutineScope.launch {
                        "go to $page".log()
                        currentPage = page
                        state.animateScrollToItem(page)
                    }
                }
            )

            PageIndicator(
                currentPage = currentPage,
                numberOfPages = champion.skins.size
            ) { page ->
                coroutineScope.launch {
                    state.animateScrollToItem(page)
                    currentPage = page
                }
            }

            championDetailsImageMaxHeight = 0.dp
            LazyRow(
                state = state
            ) {
                items(champion.skins) { skin ->
                    ChampionDetailsImage(maxWidth, champion, skin)
                }
            }
        }
    }
}

@Composable
fun ChampionDetailsData(resumedChampionData: ResumedChampionData) {
    BoxWithConstraints {
        val maxWidthToCircle = maxWidth / 4
        Column {
            ChampionDetailsDataCircleRow(
                maxWidthToCircle = maxWidthToCircle,
                dataOne = ChampionDetailsDataUiModel(
                    stringResource(id = R.string.label_attack),
                    resumedChampionData.attack,
                    ChampionDataAttackColor
                ),
                dataTwo = ChampionDetailsDataUiModel(
                    stringResource(id = R.string.label_magic),
                    resumedChampionData.magic,
                    ChampionDataMagicColor
                )
            )

            ChampionDetailsDataCircleRow(
                maxWidthToCircle = maxWidthToCircle,
                dataOne = ChampionDetailsDataUiModel(
                    stringResource(id = R.string.label_defense),
                    resumedChampionData.defense,
                    ChampionDataDefenceColor
                ),
                dataTwo = ChampionDetailsDataUiModel(
                    stringResource(id = R.string.label_difficult),
                    resumedChampionData.difficulty,
                    ChampionDataDifficultColor
                )
            )
        }
    }
}

@Composable
fun ChampionDetailsDataCircleRow(
    maxWidthToCircle: Dp,
    dataOne: ChampionDetailsDataUiModel,
    dataTwo: ChampionDetailsDataUiModel,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (left, right) = createRefs()
        ChampionDetailsDataCircle(
            Modifier.constrainAs(left) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(right.start)
            },
            maxWidthToCircle,
            dataOne
        )
        ChampionDetailsDataCircle(
            Modifier.constrainAs(right) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(left.end)
                end.linkTo(parent.end)
            },
            maxWidthToCircle,
            dataTwo
        )
    }
}

class ChampionDetailsDataUiModel(
    val label: String,
    val data: Int,
    val color: Color
)

@Composable
fun ChampionDetailsDataCircle(
    modifier: Modifier = Modifier,
    maxWidthToCircle: Dp,
    data: ChampionDetailsDataUiModel
) {
    Column(
        modifier = modifier
            .padding(PatternSmallPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data.label,
            style = MaterialTheme.typography.body2
        )

        ConstraintLayout(
            modifier = Modifier.size(maxWidthToCircle)
        ) {
            val (progress, valueColumn) = createRefs()

            CircularProgressWithShadowAnimated(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
                    .constrainAs(progress) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                progress = data.data.toFloat() / 10,
                color = data.color,
                strokeWidth = (ChampionDataBorderStrokeWidth * data.data) / 10
            )

            Column(
                modifier = Modifier
                    .constrainAs(valueColumn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.data.toString(),
                    style = MaterialTheme.typography.h6,
                )
            }
        }
    }
}

/**
 * Todo: export to a file
 * */
enum class ScrollingTo {
    LEFT, RIGHT, NONE
}

var lastScrolling = ScrollingTo.NONE
var lastPage = -1
var waitingToChange = false

@Composable
private fun LazyListState.AddViewPagerListener(
    count: Int,
    updatePage: (Int) -> Unit,
    goTo: (Int) -> Unit
) {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }

    lastScrolling = when {
        firstVisibleItemScrollOffset > previousScrollOffset -> if (firstVisibleItemScrollOffset > layoutInfo.viewportEndOffset * .4) ScrollingTo.RIGHT else ScrollingTo.NONE
        firstVisibleItemScrollOffset < previousScrollOffset -> if (firstVisibleItemScrollOffset < layoutInfo.viewportEndOffset * .7) ScrollingTo.LEFT else ScrollingTo.NONE
        else -> lastScrolling
    }

    previousIndex = firstVisibleItemIndex
    previousScrollOffset = firstVisibleItemScrollOffset

    "waitingToChange $waitingToChange previousIndex $previousIndex lastPage $lastPage previousScrollOffset $previousScrollOffset layoutInfo ${layoutInfo.viewportEndOffset} isScrollInProgress $isScrollInProgress lastScrolling $lastScrolling".log()

    if (lastScrolling == ScrollingTo.LEFT) {
        if (lastPage != previousIndex) {
            updatePage(previousIndex)
            lastPage = previousIndex
            waitingToChange = true
        }
    } else if (lastScrolling == ScrollingTo.RIGHT) {
        updatePage(previousIndex + 1)
        lastPage = previousIndex + 1
        waitingToChange = true
    }

    if (isScrollInProgress.not() && count > 0) {
        if (lastScrolling == ScrollingTo.LEFT) {
            if (previousIndex != lastPage || waitingToChange)
                if (previousIndex == 0) {
                    lastPage = 0
                    goTo(0)
                } else {
                    lastPage = previousIndex
                    goTo(previousIndex)
                }
        } else if (lastScrolling == ScrollingTo.RIGHT) {
            if (previousIndex == count - 1) {
                lastPage = count
                goTo(count)
            } else {
                lastPage = previousIndex + 1
                goTo(previousIndex + 1)
            }
        } else {
            if (lastPage != -1)
                goTo(lastPage)
        }
        waitingToChange = false
    }
}

var championDetailsImageBorderAnimationDelay = 1500
var championDetailsImageMaxHeight = 0.dp

@Composable
fun ChampionDetailsImage(
    maxWidth: Dp,
    champion: Champion,
    championSkins: ChampionSkins
) {
    val padding = 20.dp
    val maxWidthAnim = maxWidth - (padding * 2)

    val initialAnimationTriggered = remember { mutableStateOf(false) }
    val toTopAnimationTriggered = remember { mutableStateOf(false) }
    val finalAnimationTriggered = remember { mutableStateOf(false) }

    val borderInitialAnimationTrigger = remember { mutableStateOf(false) }
    val initialTransition = updateTransition(borderInitialAnimationTrigger)

    val borderToTopAnimationTrigger = remember { mutableStateOf(false) }
    val toTopTransition = updateTransition(borderToTopAnimationTrigger)

    val borderFinalAnimationTrigger = remember { mutableStateOf(false) }
    val finalTransition = updateTransition(borderFinalAnimationTrigger)

    /**
     * Mid to sides
     * */
    val xPosInitialToStart by initialTransition.animateDp(
        transitionSpec = {
            tween(durationMillis = championDetailsImageBorderAnimationDelay)
        }
    ) {
        if (it.value) 0.dp else maxWidthAnim / 2
    }

    val xPosInitialToEnd by initialTransition.animateDp(
        transitionSpec = {
            tween(durationMillis = championDetailsImageBorderAnimationDelay)
        }
    ) {
        if (it.value) maxWidthAnim else maxWidthAnim / 2
    }

    /**
     * Bottom to Top
     * */
    val yPosToTop by toTopTransition.animateDp(
        transitionSpec = {
            tween(durationMillis = championDetailsImageBorderAnimationDelay)
        }
    ) {
        if (it.value) 0.dp else championDetailsImageMaxHeight
    }

    /**
     * Sides to mid
     * */
    val xPosFinalStart by finalTransition.animateDp(
        transitionSpec = {
            tween(durationMillis = championDetailsImageBorderAnimationDelay)
        }
    ) {
        if (it.value) maxWidthAnim / 2 else 0.dp
    }

    val xPosFinalEnd by finalTransition.animateDp(
        transitionSpec = {
            tween(durationMillis = championDetailsImageBorderAnimationDelay)
        }
    ) {
        if (it.value) maxWidthAnim / 2 else maxWidthAnim
    }

    val path = arrayListOf<Dp>().apply {
        add(xPosInitialToStart)
        add(xPosInitialToEnd)

        add(yPosToTop)

        add(xPosFinalStart)
        add(xPosFinalEnd)
    }

    Column(
        modifier = Modifier
            .width(maxWidth)
            .padding(padding)
            .borderAnim(
                BorderAnimTwo(
                    2.dp,
                    BorderAnimationEndColor,
                    path,
                    initialAnimationTriggered.value
                ) { height ->
                    if (height == 0f) return@BorderAnimTwo

                    if (initialAnimationTriggered.value.not()) {
                        if (championDetailsImageMaxHeight == 0.dp)
                            championDetailsImageMaxHeight = height.dp - (padding * 2)

                        initialAnimationTriggered.value = true
                        borderInitialAnimationTrigger.value = true

                        toTopAnimationTriggered.value = true
                        borderToTopAnimationTrigger.value = true

                        finalAnimationTriggered.value = true
                        borderFinalAnimationTrigger.value = true
                    }
                })
    ) {
        val height = maxWidth * .55f
        CoilImage(
            modifier = Modifier.height(height),
            data = ImageUrls.getSplashUrl(champion.id, championSkins.num),
            contentDescription = "Picture from ${champion.id}",
            contentScale = ContentScale.Inside,
            loading = {

                ChampionImageLoading(
                    Modifier
                        .width(maxWidth)
                        .height(height)
                )
            }
        )
    }
}

@Composable
private fun PageIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    numberOfPages: Int,
    selectedPage: (Int) -> Unit
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val row = createRef()
        Row(modifier = Modifier.constrainAs(row) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            repeat(times = numberOfPages) { page ->
                val circleColor = if (page == currentPage) {
                    Color.Red
                } else {
                    MaterialTheme.colors.onSecondary
                }
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(shape = CircleShape)
                        .background(color = circleColor)
                        .size(8.dp)
                        .clickable {
                            selectedPage(page)
                        }
                )
            }
        }
    }
}

@Composable
fun ChampionDetailsStats(resumedChampionStats: ResumedChampionStats) {

    var currentLevel by remember { mutableStateOf(1) }

    LevelSelection {
        currentLevel = it
    }

    with(resumedChampionStats) {
        setLevel(currentLevel)

        ChampionDetailsVitalityStats(this)

        ChampionDetailsResistanceStats(this)

        ChampionDetailsAttackStats(this)
    }
}

@Composable
fun ChampionDetailsAttackStats(resumedChampionStats: ResumedChampionStats) {
    ChampionDetailsVitalityExpandableBox(
        title = stringResource(id = R.string.champion_damage),
        titleBackground = ChampionDataAttackColor
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PatternNormalPadding)
        ) {
            val (left, right) = createRefs()

            with(resumedChampionStats) {
                ChampionDetailsStatsRow(
                    modifier = Modifier.constrainAs(left) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(right.start)
                    },
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.champion_damage),
                        attackdamage.getByLevel(attackdamageperlevel),
                        stringResource(id = R.string.label_attackdamageperlevel),
                        attackdamageperlevel
                    ),
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.label_attackspeed),
                        attackspeed.getByLevel(attackspeedperlevel),
                        stringResource(id = R.string.label_attackspeedperlevel),
                        attackspeedperlevel
                    )
                )

                ChampionDetailsStatsRow(
                    modifier = Modifier.constrainAs(right) {
                        start.linkTo(left.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.label_crit),
                        crit.getByLevel(critperlevel),
                        stringResource(id = R.string.label_critperlevel),
                        critperlevel
                    ),
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.label_movespeed),
                        movespeed,
                        stringResource(id = R.string.label_attackrange),
                        attackrange
                    )
                )
            }
        }
    }
}

@Composable
fun ChampionDetailsResistanceStats(resumedChampionStats: ResumedChampionStats) {
    ChampionDetailsVitalityExpandableBox(
        title = stringResource(id = R.string.champion_resistance),
        titleBackground = ChampionDataDefenceColor
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = PatternNormalPadding)
        ) {
            val (left, right) = createRefs()

            with(resumedChampionStats) {
                ChampionDetailsStatsRow(
                    modifier = Modifier.constrainAs(left) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(right.start)
                    },
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.label_armor),
                        armor.getByLevel(armorperlevel),
                        stringResource(id = R.string.label_armorperlevel),
                        armorperlevel
                    ),
                    null
                )

                ChampionDetailsStatsRow(
                    modifier = Modifier.constrainAs(right) {
                        start.linkTo(left.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.label_spellblock),
                        spellblock.getByLevel(spellblockperlevel),
                        stringResource(id = R.string.label_spellblockperlevel),
                        spellblockperlevel
                    ),
                    null
                )
            }
        }
    }
}

@Composable
fun ChampionDetailsVitalityExpandableBox(
    title: String,
    titleBackground: Color,
    composable: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable {
                expanded = !expanded
            }
            .padding(PatternNormalPadding),
        shape = RoundedCornerShape(
            topStart = ChampionDetailsStatsBoxCorners, topEnd = ChampionDetailsStatsBoxCorners
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(titleBackground)
                    .padding(PatternSmallPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6
                )
            }

            if (expanded) {
                val draw = remember { mutableStateOf(false) }
                val height = remember { mutableStateOf(0.dp) }
                val transitionStartEnd = updateTransition(draw)
                val transitionBottom = updateTransition(draw)

                val xPos by transitionBottom.animateDp(
                    transitionSpec = {
                        tween(durationMillis = championDetailsImageBorderAnimationDelay)
                    }) {
                    if (it.value) PositionBorderAnim.END_BOTTOM.value else 0.dp
                }

                val yPos by transitionStartEnd.animateDp(
                    transitionSpec = {
                        tween(durationMillis = championDetailsImageBorderAnimationDelay)
                    }) {
                    if (it.value) height.value else 0.dp
                }

                val path = arrayListOf<FromTo>().apply {
                    // Start
                    add(
                        FromTo(
                            Pair(
                                0.dp,
                                0.dp
                            ),
                            Pair(
                                0.dp,
                                yPos
                            )
                        )
                    )

                    // End
                    add(
                        FromTo(
                            Pair(
                                PositionBorderAnim.END_TOP.value,
                                0.dp
                            ),
                            Pair(
                                PositionBorderAnim.END_TOP.value,
                                yPos
                            )
                        )
                    )

                    // Bottom
                    add(
                        FromTo(
                            Pair(
                                0.dp,
                                PositionBorderAnim.START_BOTTOM.value,
                            ),
                            Pair(
                                xPos,
                                PositionBorderAnim.START_BOTTOM.value
                            )
                        )
                    )
                }

                val border = BorderAnimThree(
                    DefaultBorderStrokeWidth, BorderAnimationEndColor, path, draw.value
                ) {
                    if (it > 0f && height.value == 0.dp) {
                        height.value = it.dp
                        draw.value = true
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .borderAnim(border)
                ) {
                    composable()
                }
            }
        }
    }
}

class ChampionDetailsVitalityUiModel(
    val labelOne: String,
    val valueOne: Float,
    val labelTwo: String,
    val valueTwo: Float
)

@Composable
fun ChampionDetailsVitalityStats(resumedChampionStats: ResumedChampionStats) {
    ChampionDetailsVitalityExpandableBox(
        title = stringResource(id = R.string.champion_vitality),
        titleBackground = ChampionTagSupportColor
    ) {
        with(resumedChampionStats) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = PatternNormalPadding)
            ) {
                val (left, right) = createRefs()
                ChampionDetailsStatsRow(
                    modifier = Modifier.constrainAs(left) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(right.start)
                    },
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.label_hp),
                        hp.getByLevel(hpperlevel),
                        stringResource(id = R.string.label_hpperlevel),
                        hpperlevel
                    ),
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.label_hpregen),
                        hpregen.getByLevel(hpregenperlevel),
                        stringResource(id = R.string.label_hpregenperlevel),
                        hpregenperlevel
                    )
                )

                ChampionDetailsStatsRow(
                    modifier = Modifier.constrainAs(right) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(left.end)
                    },
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.label_mp),
                        mp.getByLevel(mpperlevel),
                        stringResource(id = R.string.label_mpperlevel),
                        mpperlevel
                    ),
                    ChampionDetailsVitalityUiModel(
                        stringResource(id = R.string.label_mpregen),
                        mpregen.getByLevel(mpregenperlevel),
                        stringResource(id = R.string.label_mpregenperlevel),
                        mpregenperlevel
                    )
                )
            }
        }
    }
}

fun Modifier.championStatsValue() = this.padding(PatternSmallPadding)

@Composable
fun ChampionDetailsStatsRow(
    modifier: Modifier,
    dataOne: ChampionDetailsVitalityUiModel,
    dataTwo: ChampionDetailsVitalityUiModel?
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dataOne.labelOne,
            style = MaterialTheme.typography.body1,
        )
        Text(
            modifier = Modifier.championStatsValue(),
            text = dataOne.valueOne.toString(),
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.height(SmallSpacer))

        Text(
            text = dataOne.labelTwo,
            style = MaterialTheme.typography.body1,
        )
        Text(
            modifier = Modifier.championStatsValue(),
            text = dataOne.valueTwo.toString(),
            style = MaterialTheme.typography.h6
        )

        dataTwo?.also { dataTwo ->
            Spacer(modifier = Modifier.height(NormalSpacer))

            Text(
                text = dataTwo.labelOne,
                style = MaterialTheme.typography.body1,
            )
            Text(
                modifier = Modifier.championStatsValue(),
                text = dataTwo.valueOne.toString(),
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(SmallSpacer))

            Text(
                text = dataTwo.labelTwo,
                style = MaterialTheme.typography.body1,
            )
            Text(
                modifier = Modifier.championStatsValue(),
                text = dataTwo.valueTwo.toString(),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
fun ChampionDetailsLore(champion: Champion) {
    Text(text = champion.lore)
}

@Preview
@Composable
fun ChampionDetailsScreenPreview() {
    ChampionDetailsScreen(Champion())
}

@Composable
fun LevelSelection(selectedLevel: (Int) -> Unit) {
    val levels = LocalChampionInfo.levels

    val currentSelectedLevel = remember { mutableStateOf("1") } // initial value
    val isOpen = remember { mutableStateOf(false) } // initial value
    val openCloseOfDropDownList: (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelectedString: (String) -> Unit = {
        currentSelectedLevel.value = it
        selectedLevel(it.toInt())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PatternNormalPadding),
        horizontalArrangement = Arrangement.Center
    ) {

        /**
         * Current level label
         * */
        Text(
            text = stringResource(id = R.string.label_selected_level),
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .padding(top = PatternNormalPadding, bottom = PatternNormalPadding)
        )

        /**
         * Open the dropDown and show the current level
         * */
        Surface(
            modifier = Modifier
                .padding(start = PatternNormalPadding, end = PatternNormalPadding)
                .size(LevelSelectorSize)
                .clickable(
                    onClick = { isOpen.value = true }
                ),
            shape = CircleShape,
            color = PlaceholderColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentSelectedLevel.value,
                    style = MaterialTheme.typography.h6,
                )
            }
        }
        /**
         * Add levels
         * */
        IconButton(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(color = MaterialTheme.colors.primaryVariant),
            onClick = {
                var level = currentSelectedLevel.value.toInt()
                if (level < levels.last().toInt()) {
                    level++
                    selectedLevel(level)
                    currentSelectedLevel.value = level.toString()
                }
            }) {
            Icon(
                Icons.Filled.PlusOne,
                contentDescription = null
            )
        }
    }

    /**
     * Levels dropDown
     * */
    DropDownListHorizontal(
        requestToOpen = isOpen.value,
        list = levels,
        openCloseOfDropDownList,
        userSelectedString
    )
}