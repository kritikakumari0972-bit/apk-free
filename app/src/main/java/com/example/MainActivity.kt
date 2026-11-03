package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.CleanerScreen
import com.example.ui.screens.GamingScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MemoryScreen
import com.example.ui.screens.MoreDestination
import com.example.ui.screens.MoreScreen
import com.example.ui.theme.NovaBgDark
import com.example.ui.theme.NovaBoostTheme
import com.example.ui.theme.NovaCardBg
import com.example.ui.theme.NovaNeonBlue
import com.example.ui.theme.NovaNeonCyan
import com.example.ui.theme.NovaTextDim
import com.example.ui.theme.NovaTextGray
import com.example.ui.viewmodel.NovaMainViewModel

enum class MainTab(val label: String, val icon: ImageVector, val tag: String) {
    HOME("Home", Icons.Default.Home, "tab_home"),
    CLEANER("Cleaner", Icons.Default.Delete, "tab_cleaner"),
    MEMORY("Memory", Icons.Default.Memory, "tab_memory"),
    GAMING("Gaming", Icons.Default.Gamepad, "tab_gaming"),
    MORE("More", Icons.Default.Menu, "tab_more")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: NovaMainViewModel = viewModel()
            val isDark by mainViewModel.darkTheme.collectAsState()

            NovaBoostTheme(darkTheme = isDark) {
                NovaBoostApp(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun NovaBoostApp(viewModel: NovaMainViewModel) {
    var selectedTab by remember { mutableStateOf(MainTab.HOME) }
    var moreTargetDestination by remember { mutableStateOf(MoreDestination.MENU) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = NovaCardBg,
                tonalElevation = 8.dp
            ) {
                MainTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            selectedTab = tab
                            if (tab == MainTab.MORE) {
                                moreTargetDestination = MoreDestination.MENU
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NovaNeonCyan,
                            selectedTextColor = NovaNeonCyan,
                            indicatorColor = NovaNeonBlue.copy(alpha = 0.25f),
                            unselectedIconColor = NovaTextDim,
                            unselectedTextColor = NovaTextDim
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NovaBgDark)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToSettings = {
                        selectedTab = MainTab.MORE
                        moreTargetDestination = MoreDestination.SETTINGS
                    },
                    onShowPerformanceReport = {
                        selectedTab = MainTab.MORE
                        moreTargetDestination = MoreDestination.PERFORMANCE_REPORT
                    }
                )
                MainTab.CLEANER -> CleanerScreen(
                    viewModel = viewModel,
                    onNavigateToAppCache = {
                        selectedTab = MainTab.MORE
                        moreTargetDestination = MoreDestination.APP_CACHE
                    }
                )
                MainTab.MEMORY -> MemoryScreen(
                    viewModel = viewModel
                )
                MainTab.GAMING -> GamingScreen(
                    viewModel = viewModel
                )
                MainTab.MORE -> MoreScreen(
                    viewModel = viewModel,
                    initialDestination = moreTargetDestination
                )
            }
        }
    }
}
