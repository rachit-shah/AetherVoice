package org.psykin.aethervoice.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.psykin.aethervoice.viewmodel.HomeScreenModel


enum class SortOption {
    NAME,
    FORMAT,
    DATE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SortOptionsDialog(
    screenModel: HomeScreenModel,
    selectedOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    if (screenModel.isSortOptionsVisible.value) {
        BasicAlertDialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sort By", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    SortOption.entries.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = selectedOption == option,
                                    onClick = { onSortOptionSelected(option) }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption == option,
                                onClick = { onSortOptionSelected(option) }
                            )
                            Text(
                                text = option.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
