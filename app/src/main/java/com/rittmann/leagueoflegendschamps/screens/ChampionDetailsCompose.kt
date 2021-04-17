package com.rittmann.leagueoflegendschamps.screens

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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.rittmann.leagueoflegendschamps.R
import com.rittmann.leagueoflegendschamps.data.local.LocalChampionInfo
import com.rittmann.leagueoflegendschamps.data.model.Champion
import com.rittmann.leagueoflegendschamps.data.model.ChampionSkins
import com.rittmann.leagueoflegendschamps.data.model.ResumedChampionData
import com.rittmann.leagueoflegendschamps.data.model.ResumedChampionStats
import com.rittmann.leagueoflegendschamps.data.network.ImageUrls
import com.rittmann.leagueoflegendschamps.screens.comum.BorderAnimTwo
import com.rittmann.leagueoflegendschamps.screens.comum.DropDownListHorizontal
import com.rittmann.leagueoflegendschamps.screens.comum.ChampionImageLoading
import com.rittmann.leagueoflegendschamps.screens.comum.HorizontalDivisor
import com.rittmann.leagueoflegendschamps.screens.comum.SurfaceScreen
import com.rittmann.leagueoflegendschamps.screens.comum.borderAnim
import com.rittmann.leagueoflegendschamps.themes.BorderAnimationEndColor
import com.rittmann.leagueoflegendschamps.themes.LeagueOfLegendsChampionsTheme
import com.rittmann.leagueoflegendschamps.themes.PatternNormalPadding
import com.rittmann.leagueoflegendschamps.themes.TabPadding
import com.rittmann.leagueoflegendschamps.themes.ToolbarHeight
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
                    Modifier
                        .verticalScroll(rememberScrollState())
                ) {

                    Row(modifier = Modifier.padding(TabPadding)) {

                        columns.forEach {

                            val selectedColor =
                                if (selectedTab == it) MaterialTheme.colors.primary else Color.Transparent

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedTab = it
                                    }
                                    .background(selectedColor)
                            ) {
                                Text(
                                    text = it,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

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
        val (title, divisor) = createRefs()
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

    }
}

@Composable
fun ChampionDetailsMain(champion: Champion) {
    lastPage = -1
    Column {
        ChampionDetailsImageSkinsViewPager(champion)

        ChampionDetailsData(champion.resumedChampionData)

        ChampionDetailsStats(champion.resumedChampionStats)
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
    with(resumedChampionData) {
        Text(text = "${stringResource(id = R.string.label_attack)} $attack")
        Text(text = "${stringResource(id = R.string.label_defense)} $defense")
        Text(text = "${stringResource(id = R.string.label_magic)} $magic")
        Text(text = "${stringResource(id = R.string.label_difficult)} $difficulty")
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
        CoilImage(
            data = ImageUrls.getSplashUrl(champion.id, championSkins.num),
            contentDescription = "Picture from ${champion.id}",
            contentScale = ContentScale.Inside,
            loading = {
                val height = maxWidth * .55f

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

        ChampionVitalityStats(this)

        Text(text = "${stringResource(id = R.string.label_movespeed)} $movespeed")
        Text(text = "${stringResource(id = R.string.label_attackrange)} $attackrange")
        Text(text = "${stringResource(id = R.string.label_crit)} ${crit.getByLevel(critperlevel)}")
        Text(text = "${stringResource(id = R.string.label_critperlevel)} $critperlevel")
        Text(
            text = "${stringResource(id = R.string.label_attackdamage)} ${
                attackdamage.getByLevel(
                    attackdamageperlevel
                )
            }"
        )
        Text(text = "${stringResource(id = R.string.label_attackdamageperlevel)} $attackdamageperlevel")
        Text(
            text = "${stringResource(id = R.string.label_attackspeed)} ${
                attackspeed.getByLevel(
                    attackspeedperlevel
                )
            }"
        )
        Text(text = "${stringResource(id = R.string.label_attackspeedperlevel)} $attackspeedperlevel")
    }
}

@Composable
fun ChampionVitalityStats(resumedChampionStats: ResumedChampionStats) {
    with(resumedChampionStats) {
        Row {
            Image(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                painter = painterResource(id = R.drawable.shield_icon),
                contentDescription = null
            )
        }

        Column {
            Text(text = "${stringResource(id = R.string.label_hp)} ${hp.getByLevel(hpperlevel)}")
            Text(text = "${stringResource(id = R.string.label_hpperlevel)} $hpperlevel")

            Text(
                text = "${stringResource(id = R.string.label_hpregen)} ${
                    hpregen.getByLevel(
                        hpregenperlevel
                    )
                }"
            )
            Text(text = "${stringResource(id = R.string.label_hpregenperlevel)} $hpregenperlevel")


            Text(text = "${stringResource(id = R.string.label_mp)} ${mp.getByLevel(mpperlevel)}")
            Text(text = "${stringResource(id = R.string.label_mpperlevel)} $mpperlevel")

            Text(
                text = "${stringResource(id = R.string.label_mpregen)} ${
                    mpregen.getByLevel(
                        mpregenperlevel
                    )
                }"
            )
            Text(text = "${stringResource(id = R.string.label_mpregenperlevel)} $mpregenperlevel")


            Text(
                text = "${stringResource(id = R.string.label_armor)} ${
                    armor.getByLevel(
                        armorperlevel
                    )
                }"
            )
            Text(text = "${stringResource(id = R.string.label_armorperlevel)} $armorperlevel")

            Text(
                text = "${stringResource(id = R.string.label_spellblock)} ${
                    spellblock.getByLevel(
                        spellblockperlevel
                    )
                }"
            )
            Text(text = "${stringResource(id = R.string.label_spellblockperlevel)} $spellblockperlevel")
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
    ) {
        /**
         * Current level label
         * */
        Text(
            text = stringResource(id = R.string.label_selected_level),
            modifier = Modifier
                .padding(PatternNormalPadding)
        )

        /**
         * Open the dropDown and show the current level
         * */
        Surface(
            modifier = Modifier
                .size(50.dp)
                .clickable(
                    onClick = { isOpen.value = true }
                ),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentSelectedLevel.value
                )
            }
        }

        /**
         * Add levels
         * */
        IconButton(onClick = {
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