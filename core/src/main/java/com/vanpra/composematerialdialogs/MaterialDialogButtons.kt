package com.vanpra.composematerialdialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import java.util.*

internal enum class MaterialDialogButtonTypes {
    Text,
    Positive,
    Negative,
    Accessibility
}

/**
 * A class used to build a buttons layout for a MaterialDialog. This should be used in conjunction
 * with the [com.vanpra.composematerialdialogs.MaterialDialog.buttons] function
 */
class MaterialDialogButtons(private val dialog: MaterialDialog) {
    /**
     * Adds a button which is always enabled to the bottom of the dialog. This should
     * only be used for neutral actions.
     *
     * @param text the string literal text shown in the button
     * @param res the string resource text shown in the button
     * @param onClick a callback which is called when the button is pressed
     */
    @Composable
    fun button(
        text: String? = null,
        @StringRes res: Int? = null,
        onClick: () -> Unit = {}
    ) {
        val buttonText = getString(res, text).toUpperCase(Locale.ROOT)
        TextButton(
            onClick = {
                onClick()
            },
            modifier = Modifier.layoutId(MaterialDialogButtonTypes.Text),
        ) {
            Text(text = buttonText, style = MaterialTheme.typography.button)
        }
    }

    /**
     * Adds a positive button to the dialog
     *
     * @param text the string literal text shown in the button
     * @param res the string resource text shown in the button
     * @param disableDismiss when true this will stop the dialog closing when the button is pressed
     * even if autoDismissing is disabled
     * @param onClick a callback which is called when the button is pressed
     */
    @Composable
    fun positiveButton(
        text: String? = null,
        @StringRes res: Int? = null,
        disableDismiss: Boolean = false,
        onClick: () -> Unit = {}
    ) {
        val buttonText = getString(res, text).toUpperCase(Locale.ROOT)
        val buttonEnabled = remember(dialog.positiveEnabled) { dialog.positiveEnabled.all { it } }
        val focusManager = LocalFocusManager.current

        TextButton(
            onClick = {
                if (dialog.isAutoDismiss() && !disableDismiss) {
                    dialog.hide(focusManager)
                }

                dialog.callbacks.forEach {
                    it()
                }

                onClick()
            },
            modifier = Modifier.layoutId(MaterialDialogButtonTypes.Positive),
            enabled = buttonEnabled && dialog.positiveButtonEnabledOverride
        ) {
            Text(text = buttonText, style = MaterialTheme.typography.button)
        }
    }

    /**
     * Adds a negative button to the dialog
     *
     * @param text the string literal text shown in the button
     * @param res the string resource text shown in the button
     * even if autoDismissing is disabled
     * @param onClick a callback which is called when the button is pressed
     */
    @Composable
    fun negativeButton(
        text: String? = null,
        @StringRes res: Int? = null,
        onClick: () -> Unit = {}
    ) {
        val buttonText = getString(res, text).toUpperCase(Locale.ROOT)
        val focusManager = LocalFocusManager.current

        TextButton(
            onClick = {
                if (dialog.isAutoDismiss()) {
                    dialog.hide(focusManager)
                }
                onClick()
            },
            modifier = Modifier.layoutId(MaterialDialogButtonTypes.Negative)
        ) {
            Text(text = buttonText, style = MaterialTheme.typography.button)
        }
    }

    /**
     * Adds a accessibility button to the bottom left of the dialog
     *
     * @param icon the icon to be shown on the button
     * @param onClick a callback which is called when the button is pressed
     */
    @Composable
    fun accessibilityButton(icon: ImageVector, onClick: () -> Unit) {
        Box(
            Modifier.size(48.dp).layoutId(MaterialDialogButtonTypes.Accessibility)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
            )
        }
    }
}
