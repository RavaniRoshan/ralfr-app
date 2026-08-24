package com.example.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricScooter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserManager
import com.example.ui.components.RappiGrabber
import com.example.ui.theme.PillShape
import com.example.ui.theme.RappiColors

enum class AuthStep {
    Welcome,
    GoogleAccountPicker,
    PhoneOtpVerification,
    ProfileSetup
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WelcomeScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(AuthStep.Welcome) }
    var selectedCountry by remember { mutableStateOf("🇨🇴 Colombia") }
    var showCountryMenu by remember { mutableStateOf(false) }
    val countries = listOf("🇨🇴 Colombia", "🇧🇷 Brasil", "🇲🇽 México", "🇦🇷 Argentina", "🇵🇪 Perú", "🇨🇱 Chile")

    // Mock Login / Setup state
    var selectedGoogleAccount by remember { mutableStateOf("Brayan Smith") }
    var userNameInput by remember { mutableStateOf("Brayan Smith") }
    var userEmailInput by remember { mutableStateOf("brayan.smith@gmail.com") }
    var userPhoneInput by remember { mutableStateOf("+57 312 984 7621") }
    var selectedAddressLine by remember { mutableStateOf("Calle 93 # 11A-28, Chicó, Bogotá") }
    var selectedAvatar by remember { mutableStateOf("👨🏻‍💻") }

    val preferenceOptions = listOf("🍔 Burgers", "⚡ Turbo 10 min", "🥑 Fruver", "🍣 Sushi", "🍕 Pizza", "💊 Pharmacy", "🍺 Beverages", "🥐 Bakery")
    var selectedPreferences by remember { mutableStateOf(setOf("🍔 Burgers", "⚡ Turbo 10 min", "🥑 Fruver")) }

    // Phone OTP state
    var otpPhoneInput by remember { mutableStateOf("+57 312 984 7621") }
    var otpCode by remember { mutableStateOf("4821") }
    var isOtpVerifying by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        RappiColors.OrangeGradientTop,
                        RappiColors.OrangeGradientBottom
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        when (currentStep) {
            AuthStep.Welcome -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    // Country Selector (Top Right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(PillShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { showCountryMenu = true }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = selectedCountry,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showCountryMenu,
                            onDismissRequest = { showCountryMenu = false }
                        ) {
                            countries.forEach { country ->
                                DropdownMenuItem(
                                    text = { Text(country) },
                                    onClick = {
                                        selectedCountry = country
                                        showCountryMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Center Content: Iconic Mustache + Hero Branding
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Iconic Rappi Mustache Emblem
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👨🏻", fontSize = 52.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Rappi",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 42.sp,
                            letterSpacing = (-1).sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "O app que entrega DE TUDO más Rappidinho",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Turbo 10 min · Restaurants · Supermarket · Pharmacy",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Bottom Action Buttons
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Continue with Google
                        Button(
                            onClick = { currentStep = AuthStep.GoogleAccountPicker },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = RappiColors.TextPrimary
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("login_google_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = "🌐", fontSize = 18.sp)
                                Text(
                                    text = "Continue with Google",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Continue with Phone Number
                        OutlinedButton(
                            onClick = { currentStep = AuthStep.PhoneOtpVerification },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.horizontalGradient(listOf(Color.White, Color.White))
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("login_phone_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Continue with Phone",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Explore directly as Guest / Continue
                        Text(
                            text = "Explore as Guest →",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.95f),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    UserManager.loginAsGuest()
                                    onContinue()
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }

            AuthStep.GoogleAccountPicker -> {
                GoogleAccountPickerSheet(
                    onSelectAccount = { name, email ->
                        userNameInput = name
                        userEmailInput = email
                        selectedGoogleAccount = name
                        currentStep = AuthStep.ProfileSetup
                    },
                    onDismiss = { currentStep = AuthStep.Welcome }
                )
            }

            AuthStep.PhoneOtpVerification -> {
                PhoneOtpVerificationSheet(
                    phone = otpPhoneInput,
                    onPhoneChange = { otpPhoneInput = it },
                    onVerified = {
                        userNameInput = "Rappi Explorer"
                        userEmailInput = "phone.user@rappi.com"
                        userPhoneInput = otpPhoneInput
                        currentStep = AuthStep.ProfileSetup
                    },
                    onDismiss = { currentStep = AuthStep.Welcome }
                )
            }

            AuthStep.ProfileSetup -> {
                ProfileSetupView(
                    name = userNameInput,
                    onNameChange = { userNameInput = it },
                    email = userEmailInput,
                    onEmailChange = { userEmailInput = it },
                    phone = userPhoneInput,
                    onPhoneChange = { userPhoneInput = it },
                    selectedAddress = selectedAddressLine,
                    onAddressChange = { selectedAddressLine = it },
                    selectedAvatar = selectedAvatar,
                    onAvatarChange = { selectedAvatar = it },
                    preferenceOptions = preferenceOptions,
                    selectedPreferences = selectedPreferences,
                    onTogglePreference = { pref ->
                        selectedPreferences = if (selectedPreferences.contains(pref)) {
                            selectedPreferences - pref
                        } else {
                            selectedPreferences + pref
                        }
                    },
                    onCompleteSetup = {
                        UserManager.login(
                            name = userNameInput,
                            email = userEmailInput,
                            phone = userPhoneInput,
                            avatar = selectedAvatar
                        )
                        UserManager.addNewAddress(
                            label = "Home",
                            addressLine = selectedAddressLine,
                            neighborhood = "Chicó Norte, Bogotá",
                            instructions = "Main residence"
                        )
                        onContinue()
                    }
                )
            }
        }
    }
}

@Composable
private fun GoogleAccountPickerSheet(
    onSelectAccount: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RappiGrabber(modifier = Modifier.align(Alignment.CenterHorizontally))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "🌐", fontSize = 24.sp)
                    Column {
                        Text(
                            text = "Sign in with Google",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.TextPrimary
                        )
                        Text(
                            text = "to continue to Rappi Colombia",
                            style = MaterialTheme.typography.bodySmall,
                            color = RappiColors.TextSecondary
                        )
                    }
                }

                // Account 1
                GoogleAccountRow(
                    name = "Brayan Smith",
                    email = "brayan.smith@gmail.com",
                    avatarEmoji = "👨🏻‍💻",
                    onClick = { onSelectAccount("Brayan Smith", "brayan.smith@gmail.com") }
                )

                // Account 2
                GoogleAccountRow(
                    name = "Sofia Gomez",
                    email = "sofia.gomez@gmail.com",
                    avatarEmoji = "👩🏻‍💼",
                    onClick = { onSelectAccount("Sofia Gomez", "sofia.gomez@gmail.com") }
                )

                // Account 3
                GoogleAccountRow(
                    name = "Alex Rodriguez",
                    email = "alex.rodriguez@gmail.com",
                    avatarEmoji = "🧑🏻‍🚀",
                    onClick = { onSelectAccount("Alex Rodriguez", "alex.rodriguez@gmail.com") }
                )

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Cancel", color = RappiColors.TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun GoogleAccountRow(
    name: String,
    email: String,
    avatarEmoji: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = RappiColors.SurfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = avatarEmoji, fontSize = 24.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = RappiColors.TextPrimary
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    color = RappiColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun PhoneOtpVerificationSheet(
    phone: String,
    onPhoneChange: (String) -> Unit,
    onVerified: () -> Unit,
    onDismiss: () -> Unit
) {
    var otpStep by remember { mutableStateOf(1) } // 1: Enter phone, 2: Auto-verify OTP
    var codeValue by remember { mutableStateOf("4821") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RappiGrabber(modifier = Modifier.align(Alignment.CenterHorizontally))

                if (otpStep == 1) {
                    Text(
                        text = "Enter Phone Number",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )
                    Text(
                        text = "We will send a 4-digit verification code via SMS.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RappiColors.TextSecondary
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = onPhoneChange,
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RappiColors.Orange,
                            cursorColor = RappiColors.Orange
                        )
                    )

                    Button(
                        onClick = { otpStep = 2 },
                        colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Send SMS Code", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        text = "Verify SMS Code",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = RappiColors.TextPrimary
                    )
                    Text(
                        text = "Enter 4-digit code sent to $phone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RappiColors.TextSecondary
                    )

                    // 4 Digit Boxes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        listOf("4", "8", "2", "1").forEach { digit ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(RappiColors.OrangeLight)
                                    .border(1.5.dp, RappiColors.Orange, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = digit,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = RappiColors.TextPrimary
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(RappiColors.LightGreenTag)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✅ Code Verified Automatically!",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.DarkGreenText
                        )
                    }

                    Button(
                        onClick = onVerified,
                        colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Continue to Profile Setup", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Back", color = RappiColors.TextSecondary)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProfileSetupView(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    selectedAddress: String,
    onAddressChange: (String) -> Unit,
    selectedAvatar: String,
    onAvatarChange: (String) -> Unit,
    preferenceOptions: List<String>,
    selectedPreferences: Set<String>,
    onTogglePreference: (String) -> Unit,
    onCompleteSetup: () -> Unit
) {
    val avatars = listOf("👨🏻‍💻", "👩🏻‍💼", "🧑🏻‍🚀", "👸🏻", "🦁", "🚀", "🛵", "🍕")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = RappiColors.Background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Welcome to Rappi! 🎉",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = RappiColors.TextPrimary
                )
                Text(
                    text = "Let's personalize your delivery experience.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RappiColors.TextSecondary
                )
            }

            // Avatar picker
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Choose your profile avatar",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.TextPrimary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            avatars.forEach { av ->
                                val isSelected = selectedAvatar == av
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) RappiColors.OrangeLight else RappiColors.ChipGreyAlt)
                                        .border(if (isSelected) 2.dp else 0.dp, RappiColors.Orange, CircleShape)
                                        .clickable { onAvatarChange(av) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = av, fontSize = 22.sp)
                                }
                            }
                        }
                    }
                }
            }

            // User Info Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Your Details",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.TextPrimary
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = onNameChange,
                            label = { Text("Full Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RappiColors.Orange,
                                cursorColor = RappiColors.Orange
                            )
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = onEmailChange,
                            label = { Text("Email Address") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RappiColors.Orange,
                                cursorColor = RappiColors.Orange
                            )
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = onPhoneChange,
                            label = { Text("Phone Number") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RappiColors.Orange,
                                cursorColor = RappiColors.Orange
                            )
                        )
                    }
                }
            }

            // Default Delivery Address
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Default Delivery Address",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.TextPrimary
                        )

                        OutlinedTextField(
                            value = selectedAddress,
                            onValueChange = onAddressChange,
                            label = { Text("Street Address & City") },
                            placeholder = { Text("Calle 93 # 11A-28, Bogotá") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RappiColors.Orange,
                                cursorColor = RappiColors.Orange
                            )
                        )
                    }
                }
            }

            // Food & Category Preferences
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Favorite categories & cuisine",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = RappiColors.TextPrimary
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            preferenceOptions.forEach { pref ->
                                val isSelected = selectedPreferences.contains(pref)
                                Box(
                                    modifier = Modifier
                                        .clip(PillShape)
                                        .background(if (isSelected) RappiColors.Orange else RappiColors.ChipGreyAlt)
                                        .clickable { onTogglePreference(pref) }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = pref,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else RappiColors.TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Complete Setup CTA
            item {
                Button(
                    onClick = onCompleteSetup,
                    colors = ButtonDefaults.buttonColors(containerColor = RappiColors.Orange),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("complete_setup_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Enter Rappi & Start Shopping",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(text = "🚀", fontSize = 18.sp)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
