package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.ProjectItem
import com.example.ui.AppViewModel
import com.example.ui.screens.*
import com.example.ui.theme.BhatiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel = viewModel()
            val isDark by viewModel.isDarkTheme.collectAsState()
            val language by viewModel.selectedLanguage.collectAsState()

            BhatiaTheme(darkTheme = isDark) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "home"

                // State to share a deep-linked project click from Home to Browse Details
                var deepLinkedProjectFromHome by remember { mutableStateOf<ProjectItem?>(null) }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    topBar = {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .statusBarsPadding()
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                                    Text(
                                        text = viewModel.translate("app_title"),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Serif,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = viewModel.translate("app_subtitle"),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Row(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Language Switcher Toggle
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                                            .clickable { viewModel.toggleLanguage() }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                            .testTag("toggle_language_btn"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (language == "EN") "हिन्दी" else "ENG",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    // Light / Dark Showroom Theme Switcher
                                    IconButton(
                                        onClick = { viewModel.toggleTheme() },
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                                            .testTag("toggle_theme_btn")
                                    ) {
                                        Icon(
                                            imageVector = if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                            contentDescription = "Theme Switcher",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                            Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))
                        }
                    },
                    bottomBar = {
                        Column {
                            Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.navigationBarsPadding()
                            ) {
                                val navItems = listOf(
                                    Triple("home", viewModel.translate("nav_home"), Icons.Filled.Home),
                                    Triple("browse", viewModel.translate("nav_browse"), Icons.Filled.PhotoLibrary),
                                    Triple("customize", viewModel.translate("nav_customize"), Icons.Filled.Chair),
                                    Triple("portal", viewModel.translate("nav_projects"), Icons.Filled.Assignment),
                                    Triple("contact", viewModel.translate("nav_account"), Icons.Filled.ContactSupport)
                                )

                                navItems.forEach { (route, label, icon) ->
                                    val isActive = currentRoute == route
                                    NavigationBarItem(
                                        selected = isActive,
                                        onClick = {
                                            if (currentRoute != route) {
                                                navController.navigate(route) {
                                                    popUpTo("home") { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = label,
                                                tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        ),
                                        modifier = Modifier.testTag("nav_item_$route")
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToBrowse = { navController.navigate("browse") },
                                onNavigateToCustomize = { navController.navigate("customize") },
                                onNavigateToContact = { navController.navigate("contact") },
                                onSelectProject = { proj ->
                                    deepLinkedProjectFromHome = proj
                                    navController.navigate("browse")
                                },
                                onSelectProjectDetail = { /* No-op, managed via browse */ },
                                onSelectArticle = { art ->
                                    // Deep link blog post to contact/tips or view detail in dialog
                                    navController.navigate("contact")
                                }
                            )
                        }

                        composable("browse") {
                            BrowseScreen(
                                viewModel = viewModel,
                                selectedProjectFromHome = deepLinkedProjectFromHome,
                                onClearSelectedProject = { deepLinkedProjectFromHome = null },
                                onSelectProject = {}
                            )
                        }

                        composable("customize") {
                            CustomizeScreen(viewModel = viewModel)
                        }

                        composable("portal") {
                            ClientPortalScreen(viewModel = viewModel)
                        }

                        composable("contact") {
                            ContactScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

// Temporary custom Composable bindings to solve parameter mismatches
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onNavigateToBrowse: () -> Unit,
    onNavigateToCustomize: () -> Unit,
    onNavigateToContact: () -> Unit,
    onSelectProject: (ProjectItem) -> Unit,
    onSelectProjectDetail: (ProjectItem) -> Unit,
    onSelectArticle: (com.example.data.BlogArticle) -> Unit
) {
    HomeScreen(
        viewModel = viewModel,
        onNavigateToBrowse = onNavigateToBrowse,
        onNavigateToCustomize = onNavigateToCustomize,
        onNavigateToContact = onNavigateToContact,
        onSelectProject = onSelectProject,
        onSelectArticle = onSelectArticle
    )
}
