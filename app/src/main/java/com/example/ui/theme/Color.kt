package com.example.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object RappiColors {
    // ── Brand ──────────────────────────────────────────────
    val Orange           = Color(0xFFFF441F) // primary brand red-orange
    val OrangeDark       = Color(0xFFDE5838) // sampled: courier scooter body
    val OrangePressed    = Color(0xFFE63A18)
    val OrangeLight      = Color(0xFFFFECE8)
    val OrangeGradientTop = Color(0xFFFF5024)
    val OrangeGradientBottom = Color(0xFFDE3915)

    // ── Turbo (express delivery sub-brand) ─────────────────
    val TurboGreenDeep   = Color(0xFF083411) // ETA pill background  ← sampled
    val TurboTextOnDeep  = Color(0xFFEEFFEF) // ETA pill text        ← sampled
    val TurboBadgeFill   = Color(0xFF67947B) // "Turbo Express" map badge ← sampled
    val TurboBadgeRing   = Color(0xFF4D8E6D) // its outer ring       ← sampled
    val TurboHeaderBg    = Color(0xFF2E7D32)
    val TurboPillGreen   = Color(0xFF43A047)

    // ── Progress rail ──────────────────────────────────────
    val ProgressGreen    = Color(0xFF4DC168) // completed segment    ← sampled
    val ProgressTrack    = Color(0xFFECECEC) // remaining segment    ← sampled
    val ProgressBadgeBg  = Color(0xFFC9FFE0) // mint halo behind rider glyph ← sampled

    // ── Neutrals ───────────────────────────────────────────
    val Surface          = Color(0xFFFFFFFF)
    val SurfaceMuted     = Color(0xFFF9F9FB) // destination badge, map pin fill ← sampled
    val ChipGrey         = Color(0xFFEDEFF3) // "Help" chip fill      ← sampled
    val ChipGreyAlt      = Color(0xFFEEF0F2) // "Tip +" chip fill     ← sampled
    val BubbleGrey       = Color(0xFFEBECF0) // chat bubble           ← sampled
    val Grabber          = Color(0xFFECEFF2) // sheet drag handle     ← sampled
    val Divider          = Color(0xFFE8EAED)
    val SearchBarBg      = Color(0xFFF4F5F7)
    val CardBg           = Color(0xFFFFFFFF)
    val CardBgAlt        = Color(0xFFF8F9FA)

    // ── Text ───────────────────────────────────────────────
    val TextPrimary      = Color(0xFF0A0A0A)
    val TextSecondary    = Color(0xFF6B6E76)
    val TextTertiary     = Color(0xFF9E9FA4) // "Help" label, meta row ← sampled
    val IconDark         = Color(0xFF2A2A2C) // send icon             ← sampled
    val IconMuted        = Color(0xFF79797B) // call icon             ← sampled
    val StarGrey         = Color(0xFF8C8F99)

    // ── Map ────────────────────────────────────────────────
    val MapBase          = Color(0xFFFCF8FA) // warm off-white canvas ← sampled
    val MapRoad          = Color(0xFFF8F9FB) // road fill             ← sampled
    val MapRoadStroke    = Color(0xFFEFDCD6) // warm pink road casing
    val MapHighwayStroke = Color(0xFFE9C9C0)
    val MapGreenspace    = Color(0xFFA5E3B8) // parks                 ← sampled
    val MapWater         = Color(0xFFD9EAF2)
    val MapLabel         = Color(0xFF8A8D93)
    val RoutePolyline    = Color(0xFF000000) // solid black, 6dp

    // ── Semantic ───────────────────────────────────────────
    val Success          = Color(0xFF4DC168)
    val Warning          = Color(0xFFFFB020)
    val Error            = Color(0xFFE5342A)
    val StarGold         = Color(0xFFFFC107)
    val DiscountYellow   = Color(0xFFFFDC00)
    val ColdBadgeBlue    = Color(0xFFE3F5FF)
    val ColdBadgeText    = Color(0xFF0077B6)
    val OutsourceTagBg   = Color(0xFFF0F4F8)
    val OutsourceTagText = Color(0xFF64748B)

    // ── Semantic Aliases & Badges ──────────────────────────
    val Background       = Color(0xFFF9F9FB)
    val SurfaceVariant   = Color(0xFFF4F5F7)
    val LightGreenTag    = Color(0xFFE8F8EE)
    val DarkGreenText    = Color(0xFF2E7D32)
    val TurboNeonGreen   = Color(0xFF00E676)
}

val RappiLightColors = lightColorScheme(
    primary            = RappiColors.Orange,
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFFFFE9E4),
    onPrimaryContainer = Color(0xFF5C1000),
    secondary          = RappiColors.TurboGreenDeep,
    onSecondary        = RappiColors.TurboTextOnDeep,
    secondaryContainer = RappiColors.ProgressBadgeBg,
    tertiary           = RappiColors.ProgressGreen,
    background         = RappiColors.Surface,
    onBackground       = RappiColors.TextPrimary,
    surface            = RappiColors.Surface,
    onSurface          = RappiColors.TextPrimary,
    surfaceVariant     = RappiColors.ChipGrey,
    onSurfaceVariant   = RappiColors.TextSecondary,
    outlineVariant     = RappiColors.Divider,
    error              = RappiColors.Error,
)

val RappiDarkColors = darkColorScheme(
    primary            = RappiColors.Orange,
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFF5C1000),
    onPrimaryContainer = Color(0xFFFFE9E4),
    secondary          = Color(0xFF0F5220),
    onSecondary        = RappiColors.TurboTextOnDeep,
    secondaryContainer = Color(0xFF083411),
    tertiary           = RappiColors.ProgressGreen,
    background         = Color(0xFF0E0E10),
    onBackground       = Color(0xFFF0F0F2),
    surface            = Color(0xFF17171A),
    onSurface          = Color(0xFFF0F0F2),
    surfaceVariant     = Color(0xFF232327),
    onSurfaceVariant   = Color(0xFF9E9FA4),
    outlineVariant     = Color(0xFF333338),
    error              = RappiColors.Error,
)

